package Controllers;

import Models.CreditCard;
import Models.SecretBox;
import StaticData.Texts;
import Views.CreateNewCreditCardView;
import Views.CreditCardsView;
import Views.OpenCreditCardView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class CreditCardsViewController implements ActionListener {

    private CreditCardsView view;
    ArrayList<CreditCard> myCreditCards;

    public CreditCardsViewController(CreditCardsView view, String humanPrivateKey) {
        this.view = view;
        this.getCreditCards(humanPrivateKey); //Recogemos las Credit Cards con la masterpassword
        this.listCreditCards(); //Recogemos las Credit Cards del usuario y las mostramos en pantalla.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.OPEN_BUTTON:
                openCreditCard();
                break;
            case Texts.CREATE_NEW_BUTTON:
                createNewCreditCard();
                break;
            default:
                break;
        }
    }

    //Metodo para recoger las Credit Cards del secret box del usuario.
    public void getCreditCards(String humanPrivateKey) {
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivateKey);
        this.myCreditCards = mySecretBox.getAccounts().getCreditCards();
    }

    //Listar.
    public void listCreditCards() {
        this.view.getCreditCardList().removeAll(); //Borramos todo.
        //Mostramos las Credit Cards en la lista
        if (!this.myCreditCards.isEmpty()) {
            for (CreditCard myCreditCard : myCreditCards) {
                this.view.getCreditCardList().add("ID: " + myCreditCard.getId() + ", Bank: " + myCreditCard.getBankName() + ", Type: " + myCreditCard.getCardType());
            }
            this.view.getCreditCardList().select(0); //Dejamos seleccionada la primera.
            //Si aun no tiene ninguna...
        } else {
            this.view.getCreditCardList().add(Texts.EMPTY_LIST_MESSAGE);
        }
    }

    //Metodo que permite al usuario visualizar una credit card guardada.
    private void openCreditCard() {
        //Si hay para mostrar...
        if (!this.myCreditCards.isEmpty()) {
            //Pedimos masterpassword
            String humanPrivateKey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            //Si es correcto...
            if (fm.validateMasterPassword(humanPrivateKey)) {
                //Seleccionamos la credit card escogida por el usuario.
                CreditCard creditCard = this.myCreditCards.get(this.view.getCreditCardList().getSelectedIndex());
                //Mostramos a vista para ver la Credit Card
                new OpenCreditCardView(this.view, creditCard); //Mandamos la credit card escogida
                //Si es incorrecto...    
            } else {
                this.view.showErrorIncorrectPrivateKey();
            }
            //Si aun no hay...    
        } else {
            this.view.showErrorNoCreditCards();
        }
    }

    //Metodo que invoca a la ventana de creacion de nueva credit card
    private void createNewCreditCard() {
        //Pedimos masterpassword
        String humanPrivateKey = this.view.askForMasterPassword();
        FileManager fm = FileManager.getInstance();
        //Si es correcto...
        if (fm.validateMasterPassword(humanPrivateKey)) {
            //crear vista crear nueva credit card
            new CreateNewCreditCardView(this.view);
            //Si es incorrecto...    
        } else {
            this.view.showErrorIncorrectPrivateKey();
        }
    }

}
