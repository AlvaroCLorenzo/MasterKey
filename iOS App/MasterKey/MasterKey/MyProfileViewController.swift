//
//  MyProfileViewController.swift
//  MasterKey
//
//  Created by Wembley on 22/4/22.
//
import UIKit
import CoreServices

class MyProfileViewController: UIViewController{
    
    let VIEW_TITLE = "MY PROFILE:"
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let UPDATE_SUCCESS_LABEL = "SUCCESFUL PROFILE UPDATE: "
    let UPDATE_SUCCESS_MESSAGE = "Your profile credentials were updated succesfuly.\nThe app must be restarted."
    let IMPORT_SUCCESS_LABEL = "SUCCESSFUL IMPORT: "
    let IMPORT_SUCCESS_MESSAGE = "Your back up file was imported and your profile was updated succesfuly.\nThe app must be restarted."
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_FIELDS = "Please, introduce a private key to encrypt/decrypt your text."
    let ERROR_BACKUP_FILE = "The back up file is not a backup file or is corrupt.\nPlease make sure your back up file is correct."
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
        
    @IBOutlet var nickNameTextField: UITextField!
    @IBOutlet var masterPasswordTextField: UITextField!

    @IBOutlet var importBackUp: UIButton!
    @IBOutlet var exportBackUp: UIButton!
    
    var masterpassTemp: String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        title = VIEW_TITLE
    }
    
    //Método que pide al usuario su contraseña maestra y realiza la comprobación antes de editar su perfil o hacer alguna accion de backup
    @IBAction func askForMasterPassword(_ sender: UIButton) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                self.masterpassTemp = sha256.encrypt(message: ac.textFields![0].text!)
                //Depende del botón...
                switch sender.tag{
                    //Si pulsó el boton de actualizar perfil (UPDATE PROFILE)...
                    case 0:
                        //Editamos el perfil del usuario
                        self.updateCredentials()
                    //Si pulsó el boton de importar un backup (IMPORT)...
                    case 1:
                        //Importamos un backup desde el movil del usuario.
                        self.importABackUpFile()
                    //Si pulsó el boton de exportar un backup (EXPORT)...
                    case 2:
                        //Exportamos un backup en el dispositivo del usuario.
                        self.exportABackUpFile()
                    default:
                        break
                }
            //Si es incorrecta...
            } else {
                self.showError(self.ERROR_LABEL, self.ERROR_PRIVATE_KEY) //Error con la master password
            }
        }
        ac.addAction(validateAction)
        ac.addAction(UIAlertAction(title: CANCEL_BUTTON, style: .cancel))
        present(ac, animated: true)
    }
    
    //Metodo que recoje unas credenciales y edita los datos del perfil del usuario para poder hacer login y cifrar la informacion.
    func updateCredentials() {
        //Recojo los datos introducidos por el usuario
        let nickname = nickNameTextField.text ?? ""
        let masterPassword = masterPasswordTextField.text ?? ""
        //Si no estan vacios...
        if !nickname.isEmpty && !masterPassword.isEmpty {
                        
            //Actualizamos los datos del usuario del secret box de nuestro archivo secreto.
            SecretFileManager.shared.secretBox.user.nickName = nickname
            SecretFileManager.shared.secretBox.user.masterPassword = masterPassword
            
            //Obtenemos la ruta del archivo secreto del usuario
            let fileURL = SecretFileManager.shared.getMyFileURL()!
            //Escribimos el archivo con los nuevos datos
            SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: sha256.encrypt(message: masterPassword))
            
            showUpdateCredentialsSuccesful() //Mostramos mensaje de exito, perfil actualizado.
        } else {
            showError(ERROR_LABEL, ERROR_FIELDS) //Error campos vacios
        }
    }
    
    //Metodo dedicado a importar una copia del archivo encriptado del usuario.
    //Muestra un document picker para poder buscar archivos en el dispositivo sin salir de la app.
    func importABackUpFile(){
        let documentPicker = UIDocumentPickerViewController(documentTypes: [kUTTypeData as String], in: .import)
        documentPicker.delegate = self //Esta clase sera el delegate.
        documentPicker.allowsMultipleSelection = false
        present(documentPicker, animated:true, completion: nil)
    }
    
    //Metodo dedicado a exportar una copia del archivo encriptado del usuario.
    func exportABackUpFile() {
        //Obtenemos la ruta del archivo secreto del usuario
        if let fileURL = SecretFileManager.shared.getMyFileURL(){
            //Presentamos el activity view para que el usuario pueda guardar el archivo donde quiera o enviarlo por RRSS.
            let vc = UIActivityViewController(activityItems: [fileURL], applicationActivities: [])
            vc.popoverPresentationController?.barButtonItem = navigationItem.rightBarButtonItem
            present(vc, animated: true)
        }
    }
    
    //Muestra una alerta con el mensaje de perfil actualizado exitossamente
    func showUpdateCredentialsSuccesful() {
        let ac = UIAlertController(title: UPDATE_SUCCESS_LABEL, message: UPDATE_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToAutoritationView))
        present(ac, animated: true)
    }
    
    //Muestra una alerta con el mensaje de importación exitosa
    func showImportSuccesful(){
        let ac = UIAlertController(title: IMPORT_SUCCESS_LABEL, message: IMPORT_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToAutoritationView))
        present(ac, animated: true)
    }
    
    //We go back to the father view (Autoritation)
    func goBackToAutoritationView(_ action: UIAlertAction){
        self.navigationController?.popToRootViewController(animated: true) //to root view controller
    }
    
    //Metodo que muestra un alert al usuario indicandole un error.
    func showError(_ title: String, _ message: String){
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
}

//Extension para esta view en el que la hacemos controladora del document picker
extension MyProfileViewController: UIDocumentPickerDelegate{
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentAt url: URL) {
        //Recogemos el archivo de backup seleccionado por el usuario y actualizamos su perfil
        if url.lastPathComponent.contains(".masterkey") {
            
            let file = FileHandle(forReadingAtPath: url.path)!
            let data = file.readDataToEndOfFile()
            file.closeFile()
            
            if !data.isEmpty {
                //Desencriptamos el contenido del fichero
                if let decriptedContent = AESEncryption.decrypt(encryptedMessage: String(data: data, encoding: .utf8)!, key: self.masterpassTemp) { //DESENCRIPTAMOS
                    let decoder = JSONDecoder()
                    let newSecretBox = try! decoder.decode(SecretBox.self, from: decriptedContent.data(using: .utf8)!)
                    
                    //Actualizamos el secret box del usuario a con el contenido del secret box del archivo importado.
                    SecretFileManager.shared.updateAccountsInSecretBox(newSecretBox: newSecretBox)
                    
                    //Obtenemos la ruta del archivo secreto del usuario
                    let fileURL = SecretFileManager.shared.getMyFileURL()!
                    //Escribimos el archivo con los nuevos datos
                    SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: self.masterpassTemp)
                    self.masterpassTemp = nil //Despues de usarla la borramos.
                    self.showImportSuccesful() //Mostramos mensaje de import exitoso.
                }else{
                    self.showError(ERROR_LABEL, ERROR_BACKUP_FILE) //No coincide con un archivo de backup
                }
            }else{
                self.showError(ERROR_LABEL, ERROR_BACKUP_FILE) //No coincide con un archivo de backup
            }
        }else{
            self.showError(ERROR_LABEL, ERROR_BACKUP_FILE) //No coincide con un archivo de backup
        }
    }
    
    func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        controller.dismiss(animated: true, completion: nil) //Para cerrar el document picker controller
    }
    
}
