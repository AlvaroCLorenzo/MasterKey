package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.masterkey.Models.CryptoWallet;
import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.R;
import com.example.masterkey.Views.CreateNewCryptoWalletActivity;
import com.example.masterkey.Views.CryptoWalletsActivity;

public class CreateNewCryptoWalletActivityController implements View.OnClickListener {

    private CreateNewCryptoWalletActivity view;
    private CryptoWallet myNewCryptoWallet;

    public CreateNewCryptoWalletActivityController(CreateNewCryptoWalletActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createButton:
                this.createNewCryptoWallet();
                break;
            default:
                break;
        }
    }

    //METODO DEDICADO A CREAR UNA NUEVA CRYPTO WALLET Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void createNewCryptoWallet() {
        //Recojo los campos
        String walletName = this.view.getWalletNameEditText();
        String walletType = this.view.getWalletTypeEditText();
        String emailAsotiated = this.view.getEmailAsociatedEditText();
        String password = this.view.getPasswordEditText();
        String publicKey = this.view.getPublicKeyEditText(); //Opcional (Algunos exchange no comparten las claves con el usuario)
        String privateKey = this.view.getPrivateKeyEditText(); //Opcional
        //Si fue correctamente...
        if (!walletName.isEmpty() && !walletType.isEmpty() && !emailAsotiated.isEmpty() && !password.isEmpty()) {
            //Creo una crypto wallet con los datos proporcionados por el usuario
            this.myNewCryptoWallet = new CryptoWallet(0, walletName, walletType, emailAsotiated, password, publicKey, privateKey);
            //Preguntamos por la master password para crear la nueva crypto wallet si es correcta. (Enviamos la crypto wallet)
            this.askForMasterPasswordToCreate(this.myNewCryptoWallet);
        }else {
            this.view.showToast(this.view.getString(R.string.error_empty_fields)); //Error campos vacios.
        }
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO.
    public void askForMasterPasswordToCreate(CryptoWallet newCryptoWallet) {
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

                    int newID = mySecretBox.getAccounts().getCryptoWallets().size() + 1; //Le asigno un nuevo ID.
                    newCryptoWallet.setId(newID);

                    //Inserto la nueva crypto wallet en el Secret box
                    mySecretBox.getAccounts().getCryptoWallets().add(newCryptoWallet); //Añado la nueva crypto wallet
                    //Reescribo el Secret box y el encrypted content del file manager.
                    fm.setMySecretBox(mySecretBox, pass);
                    //Escribo el fichero secret.masterkey con la nueva informacion.
                    fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());

                    //Volvemos al anterior activity refrescandolo. (Con mensaje de exito de creacion)
                    goIntentWithPass(CryptoWalletsActivity.class, view.getString(R.string.new_crypto_wallet_created), pass);
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
