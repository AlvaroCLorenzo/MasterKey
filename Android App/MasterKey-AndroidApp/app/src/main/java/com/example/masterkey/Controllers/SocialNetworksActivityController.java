package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.Models.SocialNetwork;
import com.example.masterkey.R;
import com.example.masterkey.Views.CreateNewSocialNetworkActivity;
import com.example.masterkey.Views.OpenSocialNetworkActivity;
import com.example.masterkey.Views.SocialNetworksActivity;

import java.util.ArrayList;

public class SocialNetworksActivityController implements View.OnClickListener, AdapterView.OnItemClickListener {

    private SocialNetworksActivity view;
    private ArrayList<SocialNetwork> mySocialNetworks;

    public SocialNetworksActivityController(SocialNetworksActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //CREATE NEW
            case R.id.createNewSocialNetworkButton:
                askForMasterPasswordToIntent(CreateNewSocialNetworkActivity.class, this.view.getString(R.string.create_new_social_network_toast), -1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Si tenemos alguna social network...
        if(!parent.getItemAtPosition(position).equals(this.view.getString(R.string.create_new_to_list))){
            //OPEN: abrimos la social network seleccionada
            this.askForMasterPasswordToIntent(OpenSocialNetworkActivity.class, this.view.getString(R.string.your_social_network_toast), position);
        //Si no tenemos ninguna social network...
        }else{
            this.view.showToast(this.view.getString(R.string.error_no_social_networks_yet));
        }
    }

    //Metodo para recoger las social networks del secret box del usuario.
    public ArrayList<SocialNetwork> getMySocialNetWorks(String humanPrivateKey){
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivateKey);
        this.mySocialNetworks = mySecretBox.getAccounts().getSocialNetworks();
        return this.mySocialNetworks;  //Devolvemos las social networks
    }

    //Metodo que recoge las social networks para visualizarlas en el listview.
    public ArrayList<String> listSocialNetworks(String humanPrivateKey) {
        //Recojo los datos
        ArrayList<SocialNetwork> mySocialNetworks = this.getMySocialNetWorks(humanPrivateKey);
        ArrayList<String> mySocialNetworksView = new ArrayList<>();
        //Mostramos las social networks en la lista
        if(!mySocialNetworks.isEmpty()){
            for (SocialNetwork mySocialNetwork : mySocialNetworks) {
                mySocialNetworksView.add("ID: " + mySocialNetwork.getId() + ", Name: " + mySocialNetwork.getWebName() + ", URL: " + mySocialNetwork.getUrl() + "");
            }
        //Si aun no tiene ninguna...
        }else{
            mySocialNetworksView.add(this.view.getString(R.string.create_new_to_list));
        }
        return mySocialNetworksView;
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO. REALIZA EL INTENT DESDE EL CUADRO MODAL.
    //true = open social network, false = create social network operation.
    public void askForMasterPasswordToIntent(Class activityClass, String messageToast, int positionToOpen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.view);
        builder.setTitle(this.view.getString(R.string.security_step_title));

        builder.setMessage(this.view.getString(R.string.security_step_introduce_pass_message));

        final EditText input = new EditText(this.view);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(input);

        builder.setPositiveButton(this.view.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = input.getText().toString();
                dialog.cancel();
                FileManager fm = FileManager.getInstance();
                if(fm.validateMasterPassword(pass)){
                    if(positionToOpen>=0){
                        //Enviamos la posicion de la social network seleccionada.
                        //Abrimos el nuevo activity (Open)
                        goIntentWithPassAndPosition(activityClass, messageToast, pass, positionToOpen);
                    }else{
                        //Abrimos el nuevo activity (Create)
                        goIntentWithPass(activityClass, messageToast, pass);
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

    //Metodo que realiza el intent aportando la masterpassword para una operacion posterior necesaria.
    public void goIntentWithPass(Class activityClass, String message, String pass){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

    //Metodo que realiza el intent aportando la masterpassword y la posicion escogida para una operacion posterior necesaria.
    public void goIntentWithPassAndPosition(Class activityClass, String message, String pass, int position){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        intent.putExtra("position", position);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

}
