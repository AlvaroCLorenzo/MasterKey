package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.masterkey.Models.CryptoWallet;
import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.R;
import com.example.masterkey.Views.CreateNewCryptoWalletActivity;
import com.example.masterkey.Views.CryptoWalletsActivity;
import com.example.masterkey.Views.OpenCryptoWalletActivity;

import java.util.ArrayList;

public class CryptoWalletsActivityController implements View.OnClickListener, AdapterView.OnItemClickListener{

    private CryptoWalletsActivity view;
    private ArrayList<CryptoWallet> myCryptoWallets;

    public CryptoWalletsActivityController(CryptoWalletsActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createNewCryptoWalletButton:
                askForMasterPasswordToIntent(CreateNewCryptoWalletActivity.class, this.view.getString(R.string.create_new_crypto_wallet_toast));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Si tenemos alguna crypto wallet...
        if(!parent.getItemAtPosition(position).equals(this.view.getString(R.string.create_new_to_list))){
            //OPEN: abrimos la crypto wallet seleccionada
            this.askForMasterPasswordToIntent(OpenCryptoWalletActivity.class, this.view.getString(R.string.your_crypto_wallet_toast));
            //Si no tenemos ninguna crypto wallet...
        }else{
            this.view.showToast(this.view.getString(R.string.error_no_crypto_wallets_yet));
        }
    }

    //Metodo para recoger las crypto wallets del secret box del usuario.
    public ArrayList<CryptoWallet> getMyCryptoWallets(String humanPrivateKey){
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivateKey);
        this.myCryptoWallets = mySecretBox.getAccounts().getCryptoWallets();
        return this.myCryptoWallets;  //Devolvemos las crypto wallets
    }

    //Metodo que recoge las crypto wallets para visualizarlas en el listview.
    public ArrayList<String> listCryptoWallets(String humanPrivateKey) {
        //Recojo los datos
        ArrayList<CryptoWallet> myCryptoWallets = this.getMyCryptoWallets(humanPrivateKey);
        ArrayList<String> myCryptoWalletsView = new ArrayList<>();
        //Mostramos las social networks en la lista
        if(!myCryptoWallets.isEmpty()){
            for (CryptoWallet myCryptoWallet : myCryptoWallets) {
                myCryptoWalletsView.add("ID: " + myCryptoWallet.getId() + ", Wallet Name: " + myCryptoWallet.getWalletName() + ", Type: " + myCryptoWallet.getWalletType() + "");
            }
            //Si aun no tiene ninguna...
        }else{
            myCryptoWalletsView.add(this.view.getString(R.string.create_new_to_list));
        }
        return myCryptoWalletsView;
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO. REALIZA EL INTENT DESDE EL CUADRO MODAL.
    public void askForMasterPasswordToIntent(Class activityClass, String messageToast) {
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
                if(FileManager.getInstance().validateMasterPassword(pass)){
                    //Abrimos el nuevo activity si es correcta la master password.
                    goIntentWithPass(activityClass, messageToast, pass);
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
}
