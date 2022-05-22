//
//  DetailSocialNetworkViewController.swift
//  MasterKey
//
//  Created by Wembley on 20/4/22.
//
import UIKit

class DetailSocialNetworkViewController: UIViewController {
    
    let VIEW_TITLE = "YOUR SOCIAL NETWORK:"
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_FIELDS = "Please, compleate all the fields."
    let EDITION_SUCESS_LABEL = "SUCCESFUL EDITION: "
    let EDITION_SUCCESS_MESSAGE = "Your social network was edited succesfuly"
    let DELETION_SUCESS_LABEL = "SUCCESFUL DELETION: "
    let DELETION_SUCCESS_MESSAGE = "Your social network was deleted succesfuly"
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    @IBOutlet var webNameTextField: UITextField!
    @IBOutlet var urlTextField: UITextField!
    @IBOutlet var emailTextField: UITextField!
    @IBOutlet var userNameTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    
    var mySocialNetwork: SocialNetwork!
    
    override func viewDidLoad() {
        title = VIEW_TITLE
        //Mostramos los campos de nuestra social network seleccionada en pantalla
        webNameTextField.text = mySocialNetwork.webname
        urlTextField.text = mySocialNetwork.url
        emailTextField.text = mySocialNetwork.email
        userNameTextField.text = mySocialNetwork.username
        passwordTextField.text = mySocialNetwork.password
    }
    
    //Método que pide al usuario su master pass y realiza la comprobación, antes de editar o borrar una social network.
    @IBAction func askForMasterPassword(_ sender: UIButton) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                //Depende del botón...
                switch sender.tag{
                    //Si pulsó el boton de editar...
                    case 0:
                        //editamos la red social
                        self.editSocialNetwork(key: sha256.encrypt(message: ac.textFields![0].text!))
                    //Si pulsó el boton de borrar...
                    case 1:
                        //eliminamos la red social
                        self.deleteSocialNetwork(key: sha256.encrypt(message: ac.textFields![0].text!))
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
    
    //Método que edita la social network seleccionada por el usuario.
    func editSocialNetwork(key: String) {
        //Recojo los campos introducidos
        let webName = webNameTextField.text ?? ""
        let url = urlTextField.text ?? ""
        let email = emailTextField.text ?? ""
        let userNameText = userNameTextField.text ?? ""
        let password = passwordTextField.text ?? ""
        //Si el usuario completó todos los campos...
        if(!webName.isEmpty && !url.isEmpty && !email.isEmpty && !userNameText.isEmpty && !password.isEmpty){
            //Recorremos las social networks del usuario.
            var socialNetWorks = SecretFileManager.shared.secretBox.accounts.socialNetworks
            for socialNetWork in socialNetWorks {
                //Cuando encontramos la seleccionada... Editamos sus datos.
                if socialNetWork.id == self.mySocialNetwork.id {
                    socialNetWorks[socialNetWork.id-1].webname = webName
                    socialNetWorks[socialNetWork.id-1].url = url
                    socialNetWorks[socialNetWork.id-1].email = email
                    socialNetWorks[socialNetWork.id-1].username = userNameText
                    socialNetWorks[socialNetWork.id-1].password = password
                }
            }
            //Actualizamos el array de social networks
            SecretFileManager.shared.secretBox.accounts.socialNetworks = socialNetWorks
            
            //Obtenemos la ruta del archivo secreto del usuario
            let fileURL = SecretFileManager.shared.getMyFileURL()!
            //Escribimos el archivo con los nuevos datos
            SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
            
            showEditionSuccesful() //Mostramos mensaje de exito y volvemos a la vista padre.
            
        }else{
            self.showError(ERROR_LABEL, ERROR_FIELDS) //Error: campos vacios
        }
    }
    
    //Método que elimina la social network seleccionada por el usuario.
    func deleteSocialNetwork(key: String) {
        var newSocialNetworks = [SocialNetwork]()
        //Recorremos las socialnetworks del usuario
        var socialNetworks = SecretFileManager.shared.secretBox.accounts.socialNetworks
        for socialNetwork in socialNetworks {
            //Mientras no encontremos la seleccionada... Editamos sus datos.
            if socialNetwork.id != self.mySocialNetwork.id {
                //Actualizamos su ID
                socialNetworks[socialNetwork.id - 1].id = newSocialNetworks.count + 1
                newSocialNetworks.append(socialNetworks[socialNetwork.id - 1]) //Añadimos las social networks cuyos ids no sean igual que el elegido
            }
            //Si coincide el iD, no la añadimos.
        }
        //Actualizamos el array de social networks
        SecretFileManager.shared.secretBox.accounts.socialNetworks = newSocialNetworks
        
        //Obtenemos la ruta del archivo secreto del usuario
        let fileURL = SecretFileManager.shared.getMyFileURL()!
        //Escribimos el archivo con los nuevos datos
        SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
        
        self.showDeletionSuccesful() //Mostramos mensaje de exito y volvemos a la vista padre.
    }
    
    //Metodo que muestra un alert al usuario indicandole un error.
    func showError(_ title: String, _ message: String){
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
    
    //Mostrar una alerta con un mensaje de edicion exitosa y que nos devuelve a la vista padre al aceptarlo
    func showEditionSuccesful() {
        let ac = UIAlertController(title: EDITION_SUCESS_LABEL, message: EDITION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToSocialNetworks))
        present(ac, animated: true)
    }
    
    //Mostrar una alerta con un mensaje de borrado exitoso y que nos devuelve a la vista padre al aceptarlo
    func showDeletionSuccesful() {
        let ac = UIAlertController(title: DELETION_SUCESS_LABEL, message: DELETION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToSocialNetworks))
        present(ac, animated: true)
    }
    
    //Volvemos a la vista padre (SocialNetwork)
    func goBackToSocialNetworks(_ action: UIAlertAction){
        self.navigationController?.popViewController(animated: true)
    }
    
}
    
