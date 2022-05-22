package Controllers;

import Models.SecretBox;
import Models.SocialNetwork;
import StaticData.Texts;
import Views.CreateNewSocialNetworkView;
import Views.OpenSocialNetworkView;
import Views.SocialNetworksView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class SocialNetworksViewController implements ActionListener {

    private SocialNetworksView view;
    ArrayList<SocialNetwork> mySocialNetworks;

    public SocialNetworksViewController(SocialNetworksView view, String humanPrivateKey) {
        this.view = view;
        this.getSocialNetWorks(humanPrivateKey); //Recogemos las Social Networks con la masterpassword
        this.listSocialNetworks(); //Recogemos las Social Networks del usuario y las mostramos en pantalla.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.OPEN_BUTTON:
                openSocialNetwork();
                break;
            case Texts.CREATE_NEW_BUTTON:
                createNewSocialNetwork();
                break;
            default:
                break;
        }
    }

    //Metodo para recoger las social networks del secret box del usuario.
    public void getSocialNetWorks(String humanPrivateKey) {
        FileManager fm = FileManager.getInstance();
        SecretBox mySecretBox = fm.getMySecretBox(humanPrivateKey);
        this.mySocialNetworks = mySecretBox.getAccounts().getSocialNetworks();
    }

    //Listar.
    public void listSocialNetworks() {
        this.view.getSocialNetworkList().removeAll(); //Borramos todo.
        //Mostramos las social networks en la lista
        if (!this.mySocialNetworks.isEmpty()) {
            for (SocialNetwork mySocialNetwork : mySocialNetworks) {
                this.view.getSocialNetworkList().add("ID: " + mySocialNetwork.getId() + ", Name: " + mySocialNetwork.getWebName() + ", URL: " + mySocialNetwork.getUrl() + "");
            }
            this.view.getSocialNetworkList().select(0); //Dejamos seleccionada la primera.
            //Si aun no tiene ninguna...
        } else {
            this.view.getSocialNetworkList().add(Texts.EMPTY_LIST_MESSAGE);
        }
    }

    //Metodo que permite al usuario visualizar una social network guardada.
    private void openSocialNetwork() {
        //Si hay para mostrar...
        if (!this.mySocialNetworks.isEmpty()) {
            //Pedimos masterpassword
            String humanPrivateKey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            //Si es correcto...
            if (fm.validateMasterPassword(humanPrivateKey)) {
                //Seleccionamos la Social Network escogida por el usuario.
                SocialNetwork socialNetwork = this.mySocialNetworks.get(this.view.getSocialNetworkList().getSelectedIndex());
                //Abrimos la info de la red social
                new OpenSocialNetworkView(this.view, socialNetwork);
                //Si es incorrecto...    
            } else {
                this.view.showErrorIncorrectPrivateKey();
            }
            //Si aun no hay...    
        } else {
            this.view.showErrorNoSocialNetworks();
        }
    }

    //Metodo que invoca a la ventana de creacion de nueva social network
    private void createNewSocialNetwork() {
        //Pedimos masterpassword
        String humanPrivateKey = this.view.askForMasterPassword();
        FileManager fm = FileManager.getInstance();
        //Si es correcto...
        if (fm.validateMasterPassword(humanPrivateKey)) {
            //crear vista crear nueva red social
            new CreateNewSocialNetworkView(this.view);
            //Al volver del cuadro modal...actualizamos la lista
            //Si es incorrecto...    
        } else {
            this.view.showErrorIncorrectPrivateKey();
        }
    }

}
