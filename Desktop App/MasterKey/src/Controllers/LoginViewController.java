package Controllers;

import StaticData.Texts;
import Views.YourAcountsView;
import Views.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @autor: Alvaro
 */
public class LoginViewController implements ActionListener, MouseListener {

    private LoginView vista;
    private FileManager fileManager;

    public LoginViewController(LoginView vista) {
        this.vista = vista;
        this.fileManager = FileManager.getInstance();
        this.fileManager.getSecretFileFromDisk();

        this.fileManager.readEncryptedFile(this.fileManager.getSecretFile());
        if (this.fileManager.isNewUser()) {
            this.vista.showNewUserMessage(); //Si no hay un fichero se saludará al nuevo usuario indicandole el uso de la app.
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        switch (ae.getActionCommand()) {
            case Texts.OPEN_BUTTON:
                this.openLogin();
                break;
            default:
                break;
        }
    }

    //Metodo encargado de recoger las credenciales del usuario y realizar la comprobacion para dar o no paso al la vista de usuario.
    public void openLogin() {
        //Recojo las credenciales introducidas por el usuario.
        String userNickName = this.vista.getNickNameText();
        String userPassword = this.vista.getMasterPassText();
        //Si los campos no son vacios...
        if (!userNickName.isEmpty() && !userPassword.isEmpty()) {
            //Recojo el contenido encriptado del fichero.
            String encryptedContent = this.fileManager.getEncryptedContent();

            //Exite un SecretBox (LOGIN de un usuario)
            if (encryptedContent != null) {
                //Validamos que el fichero pertenece al usuario que esta usando la app actualmente.
                //Si es correcto...
                if (this.fileManager.validateLogin(userNickName, userPassword)) {
                    showAccountsView(userNickName); //Nos desplazamos a la vista de usuario y cerramos la ventana actual.
                    //Si es incorrecto...
                } else {
                    this.vista.showErrorLogin();
                    this.vista.vaciarCampos();
                }
            } //No existe un SecretBox (REGISTRO de un usuario).
            else if (this.fileManager.isNewUser()) {
                //Generamos un nuevo usuario y SecretBox y escribimos el contenido en el fichero encryptado
                this.fileManager.writeNewUserAndSecretBoxInFile(userNickName, userPassword);
                //Nos desplazamos a la vista de usuario.
                showAccountsView(userNickName);
                //Login incorrecto...    
            } else {
                this.vista.showErrorLogin(); //Lanzamos mensaje de error (Tambien dificulta el ataque por fuerza bruta)
            }
        } else {
            this.vista.showErrorFields(); //Lanzamos mensaje de error 
        }
    }

    //Crea la vista de cuentas del usuario y cierra la ventana de login.
    public void showAccountsView(String user) {
        new YourAcountsView(); //Abrimos la vista de usuario.
        this.vista.dispose(); //Cerramos la ventana actual.
        this.vista.showWelcomeUser(user); //Lanzamos mensaje de bienvenida.    
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        this.vista.isPasswordVisible(true); //Mientras pulsa el boton...
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        this.vista.isPasswordVisible(false); //Cuando suelta el boton...
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
