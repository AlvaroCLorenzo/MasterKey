package Controllers;

import StaticData.Texts;
import Views.CreditCardsView;
import Views.CryptoWalletsView;
import Views.EncrypterDecrypterView;
import Views.MyProfileView;
import Views.SocialNetworksView;
import Views.YourAcountsView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @autor: Alvaro
 */
public class YourAccountsViewController implements ActionListener {

    private YourAcountsView vista;

    public YourAccountsViewController(YourAcountsView view) {
        this.vista = view;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        FileManager fm = FileManager.getInstance();
        String humanPassword;
        switch (ae.getActionCommand()) {
            case Texts.SOCIAL_NETWORKS_BUTTON:
                humanPassword = this.vista.askForMasterPassword(); //Pedimos la master password para acceder.
                if (fm.validateMasterPassword(humanPassword)) {
                    new SocialNetworksView(this.vista, humanPassword); //Abrimos social networks
                } else {
                    this.vista.showErrorIncorrectPrivateKey();
                }
                break;
            case Texts.CREDIT_CARDS_BUTTON:
                humanPassword = this.vista.askForMasterPassword(); //Pedimos la master password para acceder.
                if (fm.validateMasterPassword(humanPassword)) {
                    new CreditCardsView(this.vista, humanPassword); //Abrimos credit cards
                } else {
                    this.vista.showErrorIncorrectPrivateKey();
                }
                break;
            case Texts.CRYPTO_WALLETS_BUTTON:
                humanPassword = this.vista.askForMasterPassword(); //Pedimos la master password para acceder.
                if (fm.validateMasterPassword(humanPassword)) {
                    new CryptoWalletsView(this.vista, humanPassword); //Abrimos crypto wallets
                } else {
                    this.vista.showErrorIncorrectPrivateKey();
                }
                break;
            case Texts.ENCRYPTER_DECRYPTER_BUTTON:
                new EncrypterDecrypterView(this.vista); //Abrimos encrypter / decrypter
                break;
            case Texts.MY_PROFILE_BUTTON:
                new MyProfileView(this.vista); //Abrimos mi perfil
                break;
            default:
                break;
        }
    }

}
