package Controllers;

import Models.CreditCard;
import Models.SecretBox;
import StaticData.Texts;
import Views.OpenCreditCardView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class OpenCreditCardViewController implements ActionListener {

    private OpenCreditCardView view;
    private CreditCard myCreditCard;

    public OpenCreditCardViewController(OpenCreditCardView view, CreditCard creditCard) {
        this.view = view;
        this.myCreditCard = creditCard;
        this.viewMyCreditCard();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.EDIT_BUTTON:
                this.editCreditCard();
                break;
            case Texts.DELETE_BUTTON:
                this.deleteCreditCard();
                break;
            default:
                break;
        }
    }

    //Metodo que muestra por pantalla los campos de nuestra Credit Card guardada.
    private void viewMyCreditCard() {
        this.view.setBankNameText(this.myCreditCard.getBankName());
        this.view.setCardTypeText(this.myCreditCard.getCardType());
        this.view.setCardNumberText(String.valueOf(this.myCreditCard.getCardNumber()));
        this.view.setUserNameText(this.myCreditCard.getUserName());
        this.view.setEndDateText(this.myCreditCard.getEndDate());
        this.view.setCCVText(String.valueOf(this.myCreditCard.getCCV()));
        this.view.setPINText(String.valueOf(this.myCreditCard.getPIN()));
    }

    //METODO DEDICADO A EDITAR UNA CREDIT CARD Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void editCreditCard() {
        //Rocojo los campos
        String bankName = this.view.getBankNameText();
        String cardType = this.view.getCardTypeText();
        String cardNumber = this.view.getCardNumberText();
        String userName = this.view.getUserNameText();
        String endDate = this.view.getEndDateText();

        int CCV = 0, PIN = 0;
        boolean numberError = false;
        //Si los campos numericos se insertaron correctamente...
        try {

            CCV = Integer.valueOf(this.view.getCCVText());
            PIN = Integer.valueOf(this.view.getPINText());
        } catch (NumberFormatException ex) {
            numberError = true;
            this.view.showNumericFieldError();//Error numerico
        }
        //Si ningun campo se quedó vacio...
        if (!numberError && !bankName.isEmpty() && !cardType.isEmpty() && !cardNumber.isEmpty() && !userName.isEmpty() && !endDate.isEmpty()) {
            //Preguntamos por la master password
            String humanPrivatekey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            //Si coincide... continuamos con la creacion.
            if (fm.validateMasterPassword(humanPrivatekey)) {
                //Recojo mi secretBox
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
                //Actualizamos la lista en la vista padre.
                this.view.getVistaPadre().refreshList(humanPrivatekey);
                //Cerramos la ventana de creación
                this.view.dispose();
                //Mensaje Credit Card editada con exito.
                this.view.showCreditCardEdited();
            } else {
                this.view.showErrorIncorrectPrivateKey(); //Error clave privada
            }
        } else {
            this.view.showErrorFields(); //Error campos vacios
        }

    }

    //METODO DEDICADO A ELIMINAR UNA CREDIT CARD Y ACTUALIZAR EL SECRETBOX DEL USUARIO.
    private void deleteCreditCard() {
        //Preguntamos por la master password
        String humanPrivatekey = this.view.askForMasterPassword();
        FileManager fm = FileManager.getInstance();
        //Si coincide... continuamos con la creacion.
        if (fm.validateMasterPassword(humanPrivatekey)) {
            //Recojo mi secretBox
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
            //Actualizamos la lista en la vista padre.
            this.view.getVistaPadre().refreshList(humanPrivatekey);
            //Cerramos la ventana de creación
            this.view.dispose();
            //Mensaje social network borrada con exito.
            this.view.showCreditCardDeleted();
        }

    }

}
