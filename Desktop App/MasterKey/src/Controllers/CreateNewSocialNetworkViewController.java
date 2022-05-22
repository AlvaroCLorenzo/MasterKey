package Controllers;

import Models.SecretBox;
import Models.SocialNetwork;
import StaticData.Texts;
import Views.CreateNewSocialNetworkView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @autor: Alvaro
 */
public class CreateNewSocialNetworkViewController implements ActionListener {

    private CreateNewSocialNetworkView view;

    public CreateNewSocialNetworkViewController(CreateNewSocialNetworkView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.CREATE_BUTTON:
                createNewSocialNetwork();
                break;
            default:
                break;
        }
    }

    //METODO DEDICADO A CREAR UNA NUEVA SOCIAL NETWORK Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void createNewSocialNetwork() {
        //Recojo los campos
        String webName = this.view.getWebNameText();
        String url = this.view.getUrlText();
        String email = this.view.getEmailText();
        String userName = this.view.getUserNameText();
        String password = this.view.getPasswordText();
        //Si fue correctamente...
        if (!webName.isEmpty() && !url.isEmpty() && !email.isEmpty() && !userName.isEmpty() && !password.isEmpty()) {
            //Preguntamos por la master password
            String humanPrivatekey = this.view.askForMasterPassword();
            FileManager fm = FileManager.getInstance();
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey); //Recojo mi secretBox
            //Si coincide... continuamos con la creacion.
            if (fm.validateMasterPassword(humanPrivatekey)) {
                //Creo una social network
                int newID = mySecretBox.getAccounts().getSocialNetworks().size() + 1; //Le asigno un nuevo ID.
                SocialNetwork newSocialNetwork = new SocialNetwork(newID, webName, url, email, userName, password);

                //Inserto la nueva social network en el Secret box
                mySecretBox.getAccounts().getSocialNetworks().add(newSocialNetwork); //Añado la nueva social network
                //Reescribo el Secret box y el encrypted content del file manager.
                fm.setMySecretBox(mySecretBox, humanPrivatekey);
                //Escribo el fichero secret.masterkey con la nueva informacion.
                fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());

                //Actualizamos la lista en la vista padre.
                this.view.getVistaPadre().refreshList(humanPrivatekey);
                //Cerramos la ventana de creación
                this.view.dispose();
                //Mensaje nueva social network creada.
                this.view.showNewSocialNetworkCreated();
            } else {
                this.view.showErrorIncorrectPrivateKey(); //Error clave privada
            }
        } else {
            this.view.showErrorFields(); //Error campos vacios
        }
    }

}
