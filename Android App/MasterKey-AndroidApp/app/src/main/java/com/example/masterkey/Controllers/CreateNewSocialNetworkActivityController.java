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
import com.example.masterkey.Views.CreateNewSocialNetworkActivity;
import com.example.masterkey.Views.SocialNetworksActivity;

public class CreateNewSocialNetworkActivityController implements View.OnClickListener {

    private CreateNewSocialNetworkActivity view;
    private SocialNetwork myNewSocialNetwork;

    public CreateNewSocialNetworkActivityController(CreateNewSocialNetworkActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createButton:
                this.createNewSocialNetwork();
                break;
            default:
                break;
        }
    }

    //METODO DEDICADO A CREAR UNA NUEVA SOCIAL NETWORK Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void createNewSocialNetwork() {
        //Recojo los campos
        String webName = this.view.getWebNameEditText();
        String url = this.view.getUrlEditText();
        String email = this.view.getEmailEditText();
        String userName = this.view.getUserNameEditText();
        String password = this.view.getPasswordEditText();
        //Si fue correctamente...
        if (!webName.isEmpty() && !url.isEmpty() && !email.isEmpty() && !userName.isEmpty() && !password.isEmpty()) {
            //Creo una social network con los datos proporcionados por el usuario
            this.myNewSocialNetwork = new SocialNetwork(0, webName, url, email, userName, password);
            //Preguntamos por la master password para crear la nueva social network si es correcta. (Enviamos la social network)
            this.askForMasterPasswordToCreate(this.myNewSocialNetwork);
        }else{
            this.view.showToast(this.view.getString(R.string.error_empty_fields));
        }
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO.
    public void askForMasterPasswordToCreate(SocialNetwork newSocialNetwork) {
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
                //Si coincide... continuamos con la creacion.
                if(fm.validateMasterPassword(pass)){
                    SecretBox mySecretBox = fm.getMySecretBox(pass); //Recojo mi secretBox

                    int newID = mySecretBox.getAccounts().getSocialNetworks().size() + 1; //Le asigno un nuevo ID.
                    newSocialNetwork.setId(newID);

                    //Inserto la nueva social network en el Secret box
                    mySecretBox.getAccounts().getSocialNetworks().add(newSocialNetwork); //Añado la nueva social network
                    //Reescribo el Secret box y el encrypted content del file manager.
                    fm.setMySecretBox(mySecretBox, pass);
                    //Escribo el fichero secret.masterkey con la nueva informacion.
                    fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());

                    //Volvemos al anterior activity refrescandolo. (Con mensaje de exito de creacion)
                    goIntentWithPass(SocialNetworksActivity.class, view.getString(R.string.new_social_network_created), pass);
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

    //Metodo que realiza el intent y además aporta la masterpassword para una operacion posterior necesaria.
    public void goIntentWithPass(Class activityClass, String message, String pass){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

}
