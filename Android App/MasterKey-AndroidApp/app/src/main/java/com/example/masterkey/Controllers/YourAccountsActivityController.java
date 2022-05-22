package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.masterkey.R;
import com.example.masterkey.Views.CreditCardsActivity;
import com.example.masterkey.Views.CryptoWalletsActivity;
import com.example.masterkey.Views.EncrypterDecrypterActivity;
import com.example.masterkey.Views.MyProfileActivity;
import com.example.masterkey.Views.SocialNetworksActivity;
import com.example.masterkey.Views.YourAccountsActivity;

public class YourAccountsActivityController implements View.OnClickListener {

    private YourAccountsActivity view;

    public YourAccountsActivityController(YourAccountsActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.socialNetworksButton:
                this.askForMasterPasswordToIntent(SocialNetworksActivity.class, this.view.getString(R.string.your_secure_social_networks_toast)); //Pedimos la master password para acceder.
                break;
            case R.id.creditCardsButton:
                this.askForMasterPasswordToIntent(CreditCardsActivity.class, this.view.getString(R.string.your_secure_credit_cards_toast)); //Pedimos la master password para acceder.
                break;
            case R.id.cryptoWalletsButton:
                this.askForMasterPasswordToIntent(CryptoWalletsActivity.class, this.view.getString(R.string.your_secure_crypto_wallets_toast)); //Pedimos la master password para acceder.
                break;
            case R.id.encrypterDecryperButton:
                this.goIntent(EncrypterDecrypterActivity.class, this.view.getString(R.string.encrypter_decripter_toast));
                break;
            case R.id.myProfileButton:
                this.goIntent(MyProfileActivity.class, this.view.getString(R.string.my_profile_toast));
                break;
        }
    }

    public void goIntent(Class activityClass, String message){
        Intent intent = new Intent(this.view, activityClass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

    //Metodo que realiza el intent y además aporta la masterpassword para una operacion posterior necesaria.
    public void goIntentWithPass(Class activityClass, String message, String pass){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO. REALIZA EL INTENT DESDE EL CUADRO MODAL.
    public void askForMasterPasswordToIntent(Class activityClass, String messageToast) {
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

}
