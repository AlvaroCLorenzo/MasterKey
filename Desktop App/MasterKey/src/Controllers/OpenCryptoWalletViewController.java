package Controllers;

import Models.CryptoWallet;
import Models.SecretBox;
import StaticData.Texts;
import Views.OpenCryptoWalletView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class OpenCryptoWalletViewController implements ActionListener {

    private OpenCryptoWalletView view;
    private CryptoWallet myCryptoWallet;

    public OpenCryptoWalletViewController(OpenCryptoWalletView view, CryptoWallet cryptoWallet) {
        this.view = view;
        this.myCryptoWallet = cryptoWallet;
        this.viewMyCryptoWallet();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.EDIT_BUTTON:
                this.editCryptoWallet();
                break;
            case Texts.DELETE_BUTTON:
                this.deleteCryptoWallet();
                break;
            default:
                break;
        }
    }

    //Metodo que muestra por pantalla los campos de nuestra crypto wallet guardada.
    private void viewMyCryptoWallet() {
        this.view.setWalletNameText(this.myCryptoWallet.getWalletName());
        this.view.setWalletTypeText(this.myCryptoWallet.getWalletType());
        this.view.setEmailAsotiatedText(this.myCryptoWallet.getEmailAsotiated());
        this.view.setPasswordText(this.myCryptoWallet.getPassword());
        this.view.setPublicKeyText(this.myCryptoWallet.getPublicKey());
        this.view.setPrivateKeyText(this.myCryptoWallet.getPrivateKey());
    }

    //METODO DEDICADO A EDITAR UNA CRYPTO WALLET Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void editCryptoWallet() {
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
            //Si coincide... continuamos con la creacion.
            if (fm.validateMasterPassword(humanPrivatekey)) {
                //Recojo mi secretBox
                SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
                //Recorremos las Crypto Wallets del usuario
                for (CryptoWallet cryptoWallet : mySecretBox.getAccounts().getCryptoWallets()) {
                    //Cuando encontramos la Crypto Wallet...
                    if (cryptoWallet.getId() == this.myCryptoWallet.getId()) {
                        //La editamos...
                        cryptoWallet.setWalletName(walletName);
                        cryptoWallet.setWalletType(walletType);
                        cryptoWallet.setEmailAsotiated(emailAsotiated);
                        cryptoWallet.setPassword(password);
                        cryptoWallet.setPublicKey(publicKey);
                        cryptoWallet.setPrivateKey(privateKey);
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
                this.view.showCryptoWalletEdited();

            } else {
                this.view.showErrorIncorrectPrivateKey(); //Error clave privada
            }
        } else {
            this.view.showErrorFields(); //Error campos vacios
        }
    }

    //METODO DEDICADO A ELIMINAR UNA CREDIT CARD Y ACTUALIZAR EL SECRETBOX DEL USUARIO.
    private void deleteCryptoWallet() {
        //Preguntamos por la master password
        String humanPrivatekey = this.view.askForMasterPassword();
        FileManager fm = FileManager.getInstance();
        //Si coincide... continuamos con la creacion.
        if (fm.validateMasterPassword(humanPrivatekey)) {
            //Recojo mi secretBox
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
            ArrayList<CryptoWallet> newCryptoWallets = new ArrayList<>();
            //Recorremos las Crypto Wallets del usuario
            for (CryptoWallet cryptoWallet : mySecretBox.getAccounts().getCryptoWallets()) {
                //Si no tiene el mismo ID...
                if (cryptoWallet.getId() != this.myCryptoWallet.getId()) {
                    //Actualizamos su ID.
                    cryptoWallet.setId(newCryptoWallets.size() + 1);
                    //La incluimos...
                    newCryptoWallets.add(cryptoWallet);
                }
            }
            //Actualizamos las Crypto Wallets  en el secret box.
            mySecretBox.getAccounts().setCryptoWallets(newCryptoWallets);
            //Reescribo el Secret box y el encrypted content del file manager.
            fm.setMySecretBox(mySecretBox, humanPrivatekey);
            //Escribo el fichero secret.masterkey con la nueva informacion.
            fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
            //Actualizamos la lista en la vista padre.
            this.view.getVistaPadre().refreshList(humanPrivatekey);
            //Cerramos la ventana de creación
            this.view.dispose();
            //Mensaje Crypto Wallet borrada con exito.
            this.view.showCryptoWalletDeleted();
        }
    }

}
