package Controllers;

import Models.CryptoWallet;
import Models.SecretBox;
import StaticData.Texts;
import Views.CreateNewCryptoWalletView;
import Views.CryptoWalletsView;
import Views.OpenCryptoWalletView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class CryptoWalletsViewController implements ActionListener {

    private CryptoWalletsView view;
    ArrayList<CryptoWallet> myCryptoWallets;

    public CryptoWalletsViewController(CryptoWalletsView view, String humanPrivateKey) {
        this.view = view;
        this.getCryptoWallets(humanPrivateKey); //Recogemos las Crypto Wallets con la masterpassword
        this.listCryptoWallets(); //Recogemos las Crypto Wallets del usuario y las mostramos en pantalla.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.OPEN_BUTTON:
                openCryptoWallet();
                break;
            case Texts.CREATE_NEW_BUTTON:
                createNewCryptoWallet();
                break;
            default:
                break;
        }
    }

    //Metodo para recoger las Crypto Wallets del secret box del usuario.
    public void getCryptoWallets(String humanPrivateKey) {
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivateKey);
        this.myCryptoWallets = mySecretBox.getAccounts().getCryptoWallets();
    }

    //Listar.
    public void listCryptoWallets() {
        this.view.getCryptoWalletsList().removeAll(); //Borramos todo.
        //Mostramos las Crypto Wallets en la lista
        if (!this.myCryptoWallets.isEmpty()) {
            for (CryptoWallet myCryptoWallet : myCryptoWallets) {
                this.view.getCryptoWalletsList().add("ID: " + myCryptoWallet.getId() + ", Wallet: " + myCryptoWallet.getWalletName() + ", Type: " + myCryptoWallet.getWalletType());
            }
            this.view.getCryptoWalletsList().select(0); //Dejamos seleccionada la primera.
            //Si aun no tiene ninguna...
        } else {
            this.view.getCryptoWalletsList().add(Texts.EMPTY_LIST_MESSAGE);
        }
    }

    //Metodo que permite al usuario visualizar una crypto wallet guardada.
    private void openCryptoWallet() {
        //Si hay para mostrar...
        if (!this.myCryptoWallets.isEmpty()) {
            //Pedimos masterpassword
            String humanPrivateKey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            //Si es correcto...
            if (fm.validateMasterPassword(humanPrivateKey)) {
                //Seleccionamos la credit card escogida por el usuario.
                CryptoWallet cryptoWallet = this.myCryptoWallets.get(this.view.getCryptoWalletsList().getSelectedIndex());
                //Creamos vista de visualizacion de Crypto Wallet
                new OpenCryptoWalletView(this.view, cryptoWallet);
                //Si es incorrecto...    
            } else {
                this.view.showErrorIncorrectPrivateKey();
            }
            //Si aun no hay...    
        } else {
            this.view.showErrorNoCryptoWallets();
        }
    }

    //Metodo que invoca a la ventana de creacion de nueva crypto wallet
    private void createNewCryptoWallet() {
        //Pedimos masterpassword
        String humanPrivateKey = this.view.askForMasterPassword();
        FileManager fm = FileManager.getInstance();
        //Si es correcto...
        if (fm.validateMasterPassword(humanPrivateKey)) {
            //vista crear nueva crypto wallet
            new CreateNewCryptoWalletView(this.view);
            //Si es incorrecto...    
        } else {
            this.view.showErrorIncorrectPrivateKey();
        }
    }

}
