package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.masterkey.Models.CreditCard;
import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.R;
import com.example.masterkey.Views.CreditCardsActivity;
import com.example.masterkey.Views.OpenCreditCardActivity;

import java.util.ArrayList;

public class OpenCreditCardActivityController implements View.OnClickListener {

    private OpenCreditCardActivity view;
    private CreditCard myCreditCard;

    public OpenCreditCardActivityController(OpenCreditCardActivity view, String pass, int position) {
        this.view = view;
        this.myCreditCard = FileManager.getInstance().getMySecretBox(pass).getAccounts().getCreditCards().get(position);
        this.viewMyCreditCard(); //Mostramos la credit card.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //EDIT
            case R.id.editButton:
                askForMasterPassword(this.myCreditCard, 0); //Editar
                break;
            //DELETE
            case R.id.deleteButton:
                askForMasterPassword(this.myCreditCard, 1); //Borrar
                break;
            default:
                break;
        }
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO.
    //0 = edit , 1 = delete
    public void askForMasterPassword(CreditCard newCreditCard, int operation) {
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
                            editCreditCard(pass); //Editamos la credit card
                            //Volvemos al anterior activity refrescandolo.
                            goIntentWithPass(CreditCardsActivity.class, view.getString(R.string.credit_card_edited_toast), pass);
                            break;
                        //DELETE
                        case 1:
                            deleteCreditCard(pass); //Borramos la credit card
                            //Volvemos al anterior activity refrescandolo.
                            goIntentWithPass(CreditCardsActivity.class, view.getString(R.string.credit_card_deleted_toast), pass);
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

    //METODO DEDICADO A EDITAR UNA CREDIT CARD Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void editCreditCard(String humanPrivatekey) {
        //Rocojo los campos
        String bankName = this.view.getBankNameEditText();
        String cardType = this.view.getCardTypeEditText();
        String cardNumber = this.view.getCardNumberEditText();
        String userName = this.view.getUserNameEditText();
        String endDate = this.view.getEndDateEditText();
        int CCV = 0, PIN = 0;
        boolean numberError = false;
        //Si los campos numericos se insertaron correctamente...
        try {
            CCV = Integer.valueOf(this.view.getCCVEditText());
            PIN = Integer.valueOf(this.view.getPINEditText());
        } catch (NumberFormatException ex) {
            numberError = true;
            this.view.showToast(this.view.getString(R.string.error_numerical_fields));//Error numerico
        }
        //Si ningun campo se quedó vacio...
        if (!numberError && !bankName.isEmpty() && !cardType.isEmpty() && !cardNumber.isEmpty() && !userName.isEmpty() && !endDate.isEmpty()) {
            //Recojo mi secretBox
            FileManager fm = FileManager.getInstance();
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
            //Recorremos las credit cards del usuario
            for (CreditCard creditCard : mySecretBox.getAccounts().getCreditCards()) {
                //Cuando encontramos la credit card
                if (creditCard.getId() == this.myCreditCard.getId()) {
                    //Editamos la credit card
                    creditCard.setBankName(bankName);
                    creditCard.setCardType(cardType);
                    creditCard.setCardNumber(cardNumber);
                    creditCard.setUserName(userName);
                    creditCard.setEndDate(endDate);
                    creditCard.setCCV(CCV);
                    creditCard.setPIN(PIN);
                    break;
                }
            }
            //Reescribo el Secret box y el encrypted content del file manager.
            fm.setMySecretBox(mySecretBox, humanPrivatekey);
            //Escribo el fichero secret.masterkey con la nueva informacion.
            fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
            //Credit Card editada...
        }else{
            this.view.showToast(this.view.getString(R.string.error_empty_fields)); //error campos vacios.
        }
    }

    //METODO DEDICADO A ELIMINAR UNA CREDIT CARD Y ACTUALIZAR EL SECRETBOX DEL USUARIO.
    private void deleteCreditCard(String humanPrivatekey){
        //Recojo mi secretBox
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
        ArrayList<CreditCard> newCreditCards = new ArrayList<>();
        //Recorremos las Credit Cards del usuario
        for (CreditCard creditCard : mySecretBox.getAccounts().getCreditCards()) {
            //Si no tiene el mismo ID...
            if (creditCard.getId() != this.myCreditCard.getId()) {
                //Actualizamos su ID.
                creditCard.setId(newCreditCards.size() + 1);
                //La incluimos...
                newCreditCards.add(creditCard);
            }
        }
        //Actualizamos las Credit Cards  en el secret box.
        mySecretBox.getAccounts().setCreditCards(newCreditCards);
        //Reescribo el Secret box y el encrypted content del file manager.
        fm.setMySecretBox(mySecretBox, humanPrivatekey);
        //Escribo el fichero secret.masterkey con la nueva informacion.
        fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
        //Credit Card borrada...
    }

    //Metodo que muestra por pantalla los campos de nuestra Credit Card guardada.
    private void viewMyCreditCard() {
        this.view.setBankNameEditText(this.myCreditCard.getBankName());
        this.view.setCardTypeEditText(this.myCreditCard.getCardType());
        this.view.setCardNumberEditText(String.valueOf(this.myCreditCard.getCardNumber()));
        this.view.setUserNameEditText(this.myCreditCard.getUserName());
        this.view.setEndDateEditText(this.myCreditCard.getEndDate());
        this.view.setCCVEditText(String.valueOf(this.myCreditCard.getCCV()));
        this.view.setPINEditText(String.valueOf(this.myCreditCard.getPIN()));
    }

    //Metodo que realiza el intent y además aporta la masterpassword para una operacion posterior necesaria.
    public void goIntentWithPass(Class activityClass, String message, String pass){
        Intent intent = new Intent(this.view, activityClass);
        intent.putExtra("pass", pass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }
}
