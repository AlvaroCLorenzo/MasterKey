package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.Models.SocialNetwork;
import com.example.masterkey.R;
import com.example.masterkey.Views.OpenSocialNetworkActivity;
import com.example.masterkey.Views.SocialNetworksActivity;

import java.util.ArrayList;

public class OpenSocialNetworkActivityController implements View.OnClickListener {

    private OpenSocialNetworkActivity view;
    private SocialNetwork mySocialNetwork;

    public OpenSocialNetworkActivityController(OpenSocialNetworkActivity view, String pass, int position) {
        this.view = view;
        this.mySocialNetwork = FileManager.getInstance().getMySecretBox(pass).getAccounts().getSocialNetworks().get(position);
        this.viewMySocialNetWork(); //Mostramos la social network.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //EDIT
            case R.id.editButton:
                askForMasterPassword(this.mySocialNetwork, 0); //Editar
                break;
            //DELETE
            case R.id.deleteButton:
                askForMasterPassword(this.mySocialNetwork, 1); //Borrar
                break;
            default:
                break;
        }
    }

    //Metodo que muestra por pantalla los campos de nuestra social network guardada.
    private void viewMySocialNetWork() {
        this.view.setWebNameEditText(this.mySocialNetwork.getWebName());
        this.view.setUrlEditText(this.mySocialNetwork.getUrl());
        this.view.setEmailEditText(this.mySocialNetwork.getEmail());
        this.view.setUserNameEditText(this.mySocialNetwork.getUserName());
        this.view.setPasswordEditText(this.mySocialNetwork.getPassword());
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO.
    //0 = edit , 1 = delete
    public void askForMasterPassword(SocialNetwork newSocialNetwork, int operation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.view);
        builder.setTitle(this.view.getString(R.string.security_step_title)); //Security Step

        builder.setMessage(this.view.getString(R.string.security_step_introduce_pass_message));

        final EditText input = new EditText(this.view);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(input);

        builder.setPositiveButton(this.view.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = input.getText().toString();
                FileManager fm = FileManager.getInstance();
                dialog.cancel();
                //Si coincide... continuamos con la operacion.
                if(fm.validateMasterPassword(pass)){
                    switch (operation){
                        //EDIT
                        case 0:
                            editSocialNetwork(pass); //Editamos la social network
                            //Volvemos al anterior activity refrescandolo.
                            goIntentWithPass(SocialNetworksActivity.class, view.getString(R.string.social_network_edited_toast), pass);
                            break;
                        //DELETE
                        case 1:
                            deleteSocialNetwork(pass); //Borramos la social network
                            //Volvemos al anterior activity refrescandolo.
                            goIntentWithPass(SocialNetworksActivity.class, view.getString(R.string.social_network_deleted_toast), pass);
                            break;
                        default:
                            break;
                    }
                }else{
                    view.showToast(view.getString(R.string.error_private_key)); //Error clave master password.
                }
            }
        });
        builder.setNegativeButton(this.view.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //METODO QUE EDITA LA SOCIAL NETWORK SELECCIONADA POR EL USUARIO UNA VEZ HA INTRODUCIDO LA MASTER PASSWORD.
    private void editSocialNetwork(String humanPrivatekey) {
        //Rocojo los campos
        String webName = this.view.getWebNameEditText();
        String url = this.view.getUrlEditText();
        String email = this.view.getEmailEditText();
        String userName = this.view.getUserNameEditText();
        String password = this.view.getPasswordEditText();
        //Si fue correctamente...
        if (!webName.isEmpty() && !url.isEmpty() && !email.isEmpty() && !userName.isEmpty() && !password.isEmpty()) {
            //Recojo mi secretBox
            FileManager fm = FileManager.getInstance();
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
            //Recorremos las social networks del usuario
            for (SocialNetwork socialNetwork : mySecretBox.getAccounts().getSocialNetworks()) {
                //Cuando encontramos la social network
                if (socialNetwork.getId() == this.mySocialNetwork.getId()) {
                    //Editamos la social network.
                    socialNetwork.setWebName(webName);
                    socialNetwork.setUrl(url);
                    socialNetwork.setEmail(email);
                    socialNetwork.setUserName(userName);
                    socialNetwork.setPassword(password);
                    break;
                }
            }
            //Reescribo el Secret box y el encrypted content del file manager.
            fm.setMySecretBox(mySecretBox, humanPrivatekey);
            //Escribo el fichero secret.masterkey con la nueva informacion.
            fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
            //Social network edited...
        }else {
            this.view.showToast(this.view.getString(R.string.error_empty_fields)); //Error campos vacios
        }
    }

    //METODO QUE ELIMINA LA SOCIAL NETWORK SELECCIONADA POR EL USUARIO UNA VEZ HA INTRODUCIDO LA MASTER PASSWORD.
    private void deleteSocialNetwork(String humanPrivatekey) {
        //Recojo mi secretBox
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
        ArrayList<SocialNetwork> newSocialNetworks = new ArrayList<>();
        //Recorremos las social networks del usuario
        for (SocialNetwork socialNetwork : mySecretBox.getAccounts().getSocialNetworks()) {
            //Si no tiene el mismo ID...
            if (socialNetwork.getId() != this.mySocialNetwork.getId()) {
                //Actualizamos su ID.
                socialNetwork.setId(newSocialNetworks.size() + 1);
                //La incluimos...
                newSocialNetworks.add(socialNetwork);
            }
        }
        //Actualizamos las social networks  en el secret box.
        mySecretBox.getAccounts().setSocialNetworks(newSocialNetworks);
        //Reescribo el Secret box y el encrypted content del file manager.
        fm.setMySecretBox(mySecretBox, humanPrivatekey);
        //Escribo el fichero con la nueva informacion.
        fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
        //Social network borrada...
    }

    //Metodo que realiza el intent y además aporta la masterpassword para una operacion posterior necesaria.
    public void goIntentWithPass(Class activityClass, String message, String pass){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

}
