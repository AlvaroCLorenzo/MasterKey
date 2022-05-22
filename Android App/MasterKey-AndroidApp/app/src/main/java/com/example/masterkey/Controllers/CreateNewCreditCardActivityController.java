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
import com.example.masterkey.Views.CreateNewCreditCardActivity;
import com.example.masterkey.Views.CreditCardsActivity;

public class CreateNewCreditCardActivityController implements View.OnClickListener {

    private CreateNewCreditCardActivity view;
    private CreditCard myNewCreditCard;

    public CreateNewCreditCardActivityController(CreateNewCreditCardActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createButton:
                this.createNewCreditCard();
                break;
            default:
                break;
        }
    }

    //METODO DEDICADO A CREAR UNA NUEVA CREDIT CARD Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void createNewCreditCard() {
        //Recojo los campos
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
            this.view.showToast(this.view.getString(R.string.error_numerical_fields)); //Error numerico
        }
        //Si fue correctamente...
        if (!numberError && !bankName.isEmpty() && !cardType.isEmpty() && !cardNumber.isEmpty() && !userName.isEmpty() && !endDate.isEmpty()) {
            //Creo una credit card con los datos proporcionados por el usuario
            this.myNewCreditCard = new CreditCard(0, bankName, cardType, cardNumber, userName, endDate, CCV, PIN);
            //Preguntamos por la master password para crear la nueva credit card si es correcta. (Enviamos la credit card)
            this.askForMasterPasswordToCreate(this.myNewCreditCard);
        }else{
            this.view.showToast(this.view.getString(R.string.error_empty_fields));
        }
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO.
    public void askForMasterPasswordToCreate(CreditCard newCreditCard) {
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

                    int newID = mySecretBox.getAccounts().getCreditCards().size() + 1; //Le asigno un nuevo ID.
                    newCreditCard.setId(newID);

                    //Inserto la nueva credit card en el Secret box
                    mySecretBox.getAccounts().getCreditCards().add(newCreditCard); //Añado la nueva credit card
                    //Reescribo el Secret box y el encrypted content del file manager.
                    fm.setMySecretBox(mySecretBox, pass);
                    //Escribo el fichero secret.masterkey con la nueva informacion.
                    fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());

                    //Volvemos al anterior activity refrescandolo. (Con mensaje de exito de creacion)
                    goIntentWithPass(CreditCardsActivity.class, view.getString(R.string.new_credit_card_created), pass);
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
