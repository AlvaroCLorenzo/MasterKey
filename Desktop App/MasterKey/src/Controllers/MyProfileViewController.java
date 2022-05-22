package Controllers;

import StaticData.Texts;
import Views.MyProfileView;
import Views.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @autor: Alvaro
 */
public class MyProfileViewController implements ActionListener {

    private MyProfileView vista;

    public MyProfileViewController(MyProfileView vista) {
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.UPDATE_PROFILE_BUTTON:
                this.updateProfile();
                break;
            case Texts.IMPORT_BACKUP_BUTTON:
                this.importBackUp();
                break;
            case Texts.EXPORT_BACKUP_BUTTON:
                this.exportBackUp();
                break;
            default:
                break;
        }
    }

    //UPDATE USER PROFILE DATA
    private void updateProfile() {
        //Recojo los campos de actualizacion de perfil...
        String nickname = this.vista.getNicknameTextField();
        String privateKey = this.vista.getPrivateKeyTextField();
        //Si los campos están completados...
        if (!nickname.isEmpty() && !privateKey.isEmpty()) {
            //Pido clave privada antigua.
            String oldPassword = this.vista.askForMasterPassword(Texts.SECURITY_STEP_INSERT_OLD_MASTERPASS);
            //Si coincide la clave...
            FileManager fm = FileManager.getInstance();
            if (fm.validateMasterPassword(oldPassword)) {
                //Actualizo las credenciales del Secretbox del usuario por las que acaba de introducir.
                if (fm.updateCredentialsInSecretBox(nickname, privateKey, oldPassword)) {
                    //Si todo fue correctamente...
                    this.vista.showUpdateProfileSuccesful(); //Mensaje actualizacion exitosa
                    //Reiniciamos la app.
                    new LoginView();
                    this.vista.getVistaPadre().dispose(); //Volvemos al login
                } else {
                    this.vista.showErrorUpdatingProgile(); //Error al actualizar
                }
            } else {
                this.vista.showErrorIncorrectPrivateKey(); //Error con la master password
            }
        } else {
            this.vista.showErrorFields();
        }
    }

    //IMPORT A BACKUP AND UPDATE YOUR DATA...
    private void importBackUp() {
        FileManager fm = FileManager.getInstance();
        //Recojo el path del fichero que debe escoger el usuario.
        String filePath = fm.getImportBackUpFilePath();
        //Si el usuario escogio un fichero...
        if (filePath != null) {
            //Pido clave privada.
            String humanPassword = this.vista.askForMasterPassword(Texts.SECURITY_STEP_INSERT_MASTERPASS);
            if (fm.validateMasterPassword(humanPassword)) {
                //Si todo va bien...actualizamos el perfil
                if (fm.getImportBackUp(filePath, humanPassword, this.vista)) {
                    //Mensaje backup actualizado...
                    this.vista.showImportBackupSuccesful();
                    //Reiniciamos la app.
                    new LoginView();
                    this.vista.getVistaPadre().dispose();
                }
            } else {
                this.vista.showErrorIncorrectPrivateKey(); //Error con la master password
            }
        } else {
            this.vista.showErrorNoFileSelected(); //No escogio ningun fichero.
        }
    }

    //EXPORT A BACKUP AND SECURE YOUR DATA...
    private void exportBackUp() {
        FileManager fm = FileManager.getInstance();
        //Recojo el path del directorio escogido...
        String directoryPath = fm.getExportBackUpFilePath();
        if (directoryPath != null) {
            //Pido clave privada.
            String humanPassword = this.vista.askForMasterPassword(Texts.SECURITY_STEP_INSERT_MASTERPASS);
            //Si coincide...
            if (fm.validateMasterPassword(humanPassword)) {
                //Creo fichero encriptado en el path escogido
                if (fm.exportBackUpFile(directoryPath)) {
                    //Mensaje backup actualizado...
                    this.vista.dispose();
                    this.vista.showExportBackupSuccesful(); //BackUp actualizado
                } else {
                    this.vista.showErrorExportingBackUp(); //Algo fue mal...
                }
            } else {
                this.vista.showErrorIncorrectPrivateKey(); //Error con la master password
            }
        } else {
            this.vista.showErrorNoDirectorySelected(); //NO escogió ningun directorio.
        }
    }
}
