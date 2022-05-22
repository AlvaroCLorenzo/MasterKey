package Controllers;

import Models.CreditCard;
import Models.SecretBox;
import StaticData.Texts;
import Views.CreateNewCreditCardView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @autor: Alvaro
 */
public class CreateNewCreditCardViewController implements ActionListener {

    private CreateNewCreditCardView view;

    public CreateNewCreditCardViewController(CreateNewCreditCardView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.CREATE_BUTTON:
                createNewCreditCard();
                break;
            default:
                break;
        }
    }

    //METODO DEDICADO A CREAR UNA NUEVA CREDIT CARD Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.  
    private void createNewCreditCard() {
        //Recojo los campos
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
            this.view.showNumericFieldError(); //Error numerico
        }
        //Si fue correctamente...
        if (!numberError && !bankName.isEmpty() && !cardType.isEmpty() && !cardNumber.isEmpty() && !userName.isEmpty() && !endDate.isEmpty()) {
            //Preguntamos por la master password
            String humanPrivatekey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey); //Recojo mi secretBox
            //Si coincide... continuamos con la creacion.
            if (fm.validateMasterPassword(humanPrivatekey)) {
                //Creo una social network
                int newID = mySecretBox.getAccounts().getCreditCards().size() + 1; //Le asigno un nuevo ID.
                CreditCard newCreditCard = new CreditCard(newID, bankName, cardType, userName, cardNumber, endDate, CCV, PIN);

                //Inserto la nueva credit card en el Secret box
                mySecretBox.getAccounts().getCreditCards().add(newCreditCard); //Añado la nueva credit card
                //Reescribo el Secret box y el encrypted content del file manager.
                fm.setMySecretBox(mySecretBox, humanPrivatekey);
                //Escribo el fichero secret.masterkey con la nueva informacion.
                fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());

                //Actualizamos la lista en la vista padre.
                this.view.getVistaPadre().refreshList(humanPrivatekey);
                //Cerramos la ventana de creación
                this.view.dispose();
                //Mensaje nueva credit card creada.
                this.view.showNewCreditCardCreated();
            } else {
                this.view.showErrorIncorrectPrivateKey(); //Error clave privada
            }
        } else {
            this.view.showErrorFields(); //Error campos vacios
        }
    }

}
