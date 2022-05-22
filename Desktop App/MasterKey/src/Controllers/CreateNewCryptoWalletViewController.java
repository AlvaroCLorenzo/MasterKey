package Controllers;

import Models.CryptoWallet;
import Models.SecretBox;
import StaticData.Texts;
import Views.CreateNewCryptoWalletView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @autor: Alvaro
 */
public class CreateNewCryptoWalletViewController implements ActionListener {

    private CreateNewCryptoWalletView view;

    public CreateNewCryptoWalletViewController(CreateNewCryptoWalletView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.CREATE_BUTTON:
                createNewCryptoWallet();
                break;
            default:
                break;
        }
    }

     //METODO DEDICADO A CREAR UNA NUEVA CRYPTO WALLET Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void createNewCryptoWallet() {
        //Recojo los campos
        String walletName = this.view.getWalletNameText();
        String walletType = this.view.getWalletTypeText();
        String emailAsotiated = this.view.getEmailAsotiatedText();
        String password = this.view.getPasswordText();
        String publicKey = this.view.getPublicKeyText(); //Opcional (Algunos exchange no comparten las claves con el usuario)
        String privateKey = this.view.getPrivateKeyText(); //Opcional
        //Si fue correctamente...
        if (!walletName.isEmpty() && !walletType.isEmpty() && !emailAsotiated.isEmpty() && !password.isEmpty()) {
            //Preguntamos por la master password
            String humanPrivatekey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey); //Recojo mi secretBox
            //Si coincide... continuamos con la creacion.
            if (fm.validateMasterPassword(humanPrivatekey)) {
                //Creo una crypto wallet 
                int newID = mySecretBox.getAccounts().getCryptoWallets().size() + 1; //Le asigno un nuevo ID.
                CryptoWallet newSocialNetwork = new CryptoWallet(newID, walletName, walletType, emailAsotiated, password, publicKey, privateKey);

                //Inserto la nueva  crypto wallet en el Secret box
                mySecretBox.getAccounts().getCryptoWallets().add(newSocialNetwork); //Añado la nueva social network
                //Reescribo el Secret box y el encrypted content del file manager.
                fm.setMySecretBox(mySecretBox, humanPrivatekey);
                //Escribo el fichero secret.masterkey con la nueva informacion.
                fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());

                //Actualizamos la lista en la vista padre.
                this.view.getVistaPadre().refreshList(humanPrivatekey);
                //Cerramos la ventana de creación
                this.view.dispose();
                //Mensaje nueva crypto wallet creada.
                this.view.showNewCryptoWalletCreated();
            } else {
                this.view.showErrorIncorrectPrivateKey(); //Error clave privada
            }
        } else {
            this.view.showErrorFields(); //Error campos vacios
        }
    }

}
