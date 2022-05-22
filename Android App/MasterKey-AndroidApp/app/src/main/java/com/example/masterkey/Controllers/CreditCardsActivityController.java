package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.masterkey.Models.CreditCard;
import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.R;
import com.example.masterkey.Views.CreateNewCreditCardActivity;
import com.example.masterkey.Views.CreditCardsActivity;
import com.example.masterkey.Views.OpenCreditCardActivity;

import java.util.ArrayList;

public class CreditCardsActivityController implements View.OnClickListener, AdapterView.OnItemClickListener{

    private CreditCardsActivity view;
    private ArrayList<CreditCard> myCreditCards;

    public CreditCardsActivityController(CreditCardsActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createNewCreditCardButton:
                askForMasterPasswordToIntent(CreateNewCreditCardActivity.class, this.view.getString(R.string.create_new_credit_card_toast), -1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Si tenemos alguna credit card...
        if(!parent.getItemAtPosition(position).equals(this.view.getString(R.string.create_new_to_list))){
            //OPEN: abrimos la credit card seleccionada
            this.askForMasterPasswordToIntent(OpenCreditCardActivity.class, this.view.getString(R.string.your_credit_card_toast), position);
            //Si no tenemos ninguna credit card...
        }else{
            this.view.showToast(this.view.getString(R.string.error_no_credit_cards_yet));
        }
    }

    //Metodo para recoger las credit cards del secret box del usuario.
    public ArrayList<CreditCard> getMyCreditCards(String humanPrivateKey){
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivateKey);
        this.myCreditCards = mySecretBox.getAccounts().getCreditCards();
        return this.myCreditCards;  //Devolvemos las credit cards
    }

    //Metodo que recoge las credit cards para visualizarlas en el listview.
    public ArrayList<String> listCreditCards(String humanPrivateKey) {
        //Recojo los datos
        ArrayList<CreditCard> myCreditCards = this.getMyCreditCards(humanPrivateKey);
        ArrayList<String> myCreditCardsView = new ArrayList<>();
        //Mostramos las social networks en la lista
        if(!myCreditCards.isEmpty()){
            for (CreditCard myCreditCard : myCreditCards) {
                myCreditCardsView.add("ID: " + myCreditCard.getId() + ", Bank: " + myCreditCard.getBankName() + ", Card Type: " + myCreditCard.getCardType() + "");
            }
            //Si aun no tiene ninguna...
        }else{
            myCreditCardsView.add(this.view.getString(R.string.create_new_to_list));
        }
        return myCreditCardsView;
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO. REALIZA EL INTENT DESDE EL CUADRO MODAL.
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
                if(FileManager.getInstance().validateMasterPassword(pass)){
                    if(positionToOpen>=0){
                        //Enviamos la posicion de la credit card seleccionada.
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
