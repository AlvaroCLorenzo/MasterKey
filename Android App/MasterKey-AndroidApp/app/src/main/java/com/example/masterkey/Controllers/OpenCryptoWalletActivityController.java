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
import com.example.masterkey.Views.CryptoWalletsActivity;
import com.example.masterkey.Views.OpenCryptoWalletActivity;

import java.util.ArrayList;

public class OpenCryptoWalletActivityController implements View.OnClickListener {

    private OpenCryptoWalletActivity view;
    private CryptoWallet myCryptoWallet;

    public OpenCryptoWalletActivityController(OpenCryptoWalletActivity view, String pass, int position){
        this.view = view;
        this.myCryptoWallet = FileManager.getInstance().getMySecretBox(pass).getAccounts().getCryptoWallets().get(position);
        this.viewMyCryptoWallet(); //Mostramos la crypto wallet
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //EDIT
            case R.id.editButton:
                askForMasterPassword(this.myCryptoWallet, 0); //Editar
                break;
            //DELETE
            case R.id.deleteButton:
                askForMasterPassword(this.myCryptoWallet, 1); //Borrar
                break;
            default:
                break;
        }
    }

    //Metodo que muestra por pantalla los campos de nuestra crypto wallet guardada.
    private void viewMyCryptoWallet() {
        this.view.setWalletNameEditText(this.myCryptoWallet.getWalletName());
        this.view.setWalletTypeEditText(this.myCryptoWallet.getWalletType());
        this.view.setEmailAsociatedEditText(this.myCryptoWallet.getEmailAsotiated());
        this.view.setPasswordEditText(this.myCryptoWallet.getPassword());
        this.view.setPublicKeyEditText(this.myCryptoWallet.getPublicKey());
        this.view.setPrivateKeyEditText(this.myCryptoWallet.getPrivateKey());
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO.
    //0 = edit , 1 = delete
    public void askForMasterPassword(CryptoWallet newSocialNetwork, int operation) {
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
                            editCryptoWallet(pass); //Editamos la crypto wallet
                            //Volvemos al anterior activity refrescandolo.
                            goIntentWithPass(CryptoWalletsActivity.class, view.getString(R.string.crypto_wallet_edited_toast), pass);
                            break;
                        //DELETE
                        case 1:
                            deleteCryptoWallet(pass); //Borramos la crypto wallet
                            //Volvemos al anterior activity refrescandolo.
                            goIntentWithPass(CryptoWalletsActivity.class, view.getString(R.string.crypto_wallet_deleted_toast), pass);
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

    //METODO DEDICADO A EDITAR UNA CRYPTO WALLET Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void editCryptoWallet(String humanPrivatekey) {
        //Recojo los campos
        String walletName = this.view.getWalletNameEditText();
        String walletType = this.view.getWalletTypeEditText();
        String emailAsotiated = this.view.getEmailAsociatedEditText();
        String password = this.view.getPasswordEditText();
        String publicKey = this.view.getPublicKeyEditText(); //Opcional (Algunos exchange no comparten las claves con el usuario)
        String privateKey = this.view.getPrivateKeyEditText(); //Opcional
        //Si fue correctamente...
        if (!walletName.isEmpty() && !walletType.isEmpty() && !emailAsotiated.isEmpty() && !password.isEmpty()) {
            //Recojo mi secretBox
            FileManager fm = FileManager.getInstance();
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
            //Recorremos las Crypto Wallets del usuario
            for (CryptoWallet cryptoWallet : mySecretBox.getAccounts().getCryptoWallets()) {
                //Cuando encontramos la Crypto Wallet...
                if (cryptoWallet.getId() == this.myCryptoWallet.getId()) {
                    //La editamos...
                    cryptoWallet.setWalletName(walletName);
                    cryptoWallet.setWalletType(walletType);
                    cryptoWallet.setEmailAsotiated(emailAsotiated);
                    cryptoWallet.setPassword(password);
                    cryptoWallet.setPublicKey(publicKey);
                    cryptoWallet.setPrivateKey(privateKey);
                    break;
                }
            }
            //Reescribo el Secret box y el encrypted content del file manager.
            fm.setMySecretBox(mySecretBox, humanPrivatekey);
            //Escribo el fichero secret.masterkey con la nueva informacion.
            fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
            //Crypto wallet editada...
        }else {
            this.view.showToast(this.view.getString(R.string.error_empty_fields)); //Error campos vacios
        }
    }

    //METODO DEDICADO A ELIMINAR UNA CREDIT CARD Y ACTUALIZAR EL SECRETBOX DEL USUARIO.
    private void deleteCryptoWallet(String humanPrivatekey) {
        //Recojo mi secretBox
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
        ArrayList<CryptoWallet> newCryptoWallets = new ArrayList<>();
        //Recorremos las Crypto Wallets del usuario
        for (CryptoWallet cryptoWallet : mySecretBox.getAccounts().getCryptoWallets()) {
            //Si no tiene el mismo ID...
            if (cryptoWallet.getId() != this.myCryptoWallet.getId()) {
                //Actualizamos su ID.
                cryptoWallet.setId(newCryptoWallets.size() + 1);
                //La incluimos...
                newCryptoWallets.add(cryptoWallet);
            }
        }
        //Actualizamos las Crypto Wallets  en el secret box.
        mySecretBox.getAccounts().setCryptoWallets(newCryptoWallets);
        //Reescribo el Secret box y el encrypted content del file manager.
        fm.setMySecretBox(mySecretBox, humanPrivatekey);
        //Escribo el fichero secret.masterkey con la nueva informacion.
        fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
        //Crypto wallet borrada...
    }

    //Metodo que realiza el intent y adem??s aporta la masterpassword para una operacion posterior necesaria.
    public void goIntentWithPass(Class activityClass, String message, String pass){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }
}
