package Controllers;

import Models.SecretBox;
import Models.SocialNetwork;
import StaticData.Texts;
import Views.OpenSocialNetworkView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class OpenSocialNetworkViewController implements ActionListener {

    private OpenSocialNetworkView view;
    private SocialNetwork mySocialNetwork;

    public OpenSocialNetworkViewController(OpenSocialNetworkView view, SocialNetwork socialNetwork) {
        this.view = view;
        this.mySocialNetwork = socialNetwork;
        this.viewMySocialNetWork();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.EDIT_BUTTON:
                this.editSocialNetwork();
                break;
            case Texts.DELETE_BUTTON:
                this.deleteSocialNetwork();
                break;
            default:
                break;
        }
    }

    //Metodo que muestra por pantalla los campos de nuestra social network guardada.
    private void viewMySocialNetWork() {
        this.view.setWebNameText(this.mySocialNetwork.getWebName());
        this.view.setUrlText(this.mySocialNetwork.getUrl());
        this.view.setEmailText(this.mySocialNetwork.getEmail());
        this.view.setUserNameText(this.mySocialNetwork.getUserName());
        this.view.setPasswordText(this.mySocialNetwork.getPassword());
    }

    //METODO DEDICADO A EDITAR UNA RED SOCIAL Y ALMACENARLA SEGUN LOS DATOS QUE INTRODUJO EL USUARIO.
    private void editSocialNetwork() {
        //Rocojo los campos
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
            //Si coincide... continuamos con la creacion.
            if (fm.validateMasterPassword(humanPrivatekey)) {
                //Recojo mi secretBox
                SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
                //Recorremos las social networks del usuario
                for (SocialNetwork socialNetwork : mySecretBox.getAccounts().getSocialNetworks()) {
                    //Cuando encontramos la social network
                    if (socialNetwork.getId() == this.mySocialNetwork.getId()) {
                        //Editamos la social network.
                        socialNetwork.setWebName(webName);
                        socialNetwork.setUrl(url);
                        socialNetwork.setEmail(email);
                        socialNetwork.setUserName(userName);
                        socialNetwork.setPassword(password);
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
                //Mensaje social network editada con exito.
                this.view.showSocialNetworkEdited();
            } else {
                this.view.showErrorIncorrectPrivateKey(); //Error clave privada
            }
        } else {
            this.view.showErrorFields(); //Error campos vacios
        }
    }

    //METODO DEDICADO A ELIMINAR UNA RED SOCIAL Y ACTUALIZAR EL SECRETBOX DEL USUARIO.
    private void deleteSocialNetwork() {
        //Preguntamos por la master password
        String humanPrivatekey = this.view.askForMasterPassword();
        FileManager fm = FileManager.getInstance();
        //Si coincide... continuamos con la creacion.
        if (fm.validateMasterPassword(humanPrivatekey)) {
            //Recojo mi secretBox
            SecretBox mySecretBox = fm.getMySecretBox(humanPrivatekey);
            ArrayList<SocialNetwork> newSocialNetworks = new ArrayList<>();
            //Recorremos las social networks del usuario
            for (SocialNetwork socialNetwork : mySecretBox.getAccounts().getSocialNetworks()) {
                //Si no tiene el mismo ID...
                if (socialNetwork.getId() != this.mySocialNetwork.getId()) {
                    //Actualizamos su ID.
                    socialNetwork.setId(newSocialNetworks.size() + 1);
                    //La incluimos...
                    newSocialNetworks.add(socialNetwork);
                }
            }
            //Actualizamos las social networks  en el secret box.
            mySecretBox.getAccounts().setSocialNetworks(newSocialNetworks);
            //Reescribo el Secret box y el encrypted content del file manager.
            fm.setMySecretBox(mySecretBox, humanPrivatekey);
            //Escribo el fichero secret.masterkey con la nueva informacion.
            fm.writeFileInit(fm.getSecretFile(), fm.getEncryptedContent());
            //Actualizamos la lista en la vista padre.
            this.view.getVistaPadre().refreshList(humanPrivatekey);
            //Cerramos la ventana de creación
            this.view.dispose();
            //Mensaje social network borrada con exito.
            this.view.showSocialNetworkDeleted();
        }
    }
}
