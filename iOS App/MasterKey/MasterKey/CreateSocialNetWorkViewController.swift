//
//  DetailSocialNetwork.swift
//  MasterKey
//
//  Created by Wembley on 19/4/22.
//

import UIKit

class CreateSocialNetWorkViewController: UIViewController {
    
    let VIEW_TITLE = "CREATE A SOCIAL NETWORK:"
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_FIELDS = "Please, compleate all the fields."
    let CREATION_SUCESS_LABEL = "SUCCESFUL CREATION: "
    let CREATION_SUCCESS_MESSAGE = "Your social network was created succesfuly"
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    @IBOutlet var webNameTextField: UITextField!
    @IBOutlet var urlTextField: UITextField!
    @IBOutlet var emailTextField: UITextField!
    @IBOutlet var userNameTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    
    override func viewDidLoad() {
        title = VIEW_TITLE
    }
    
    //Método que pide al usuario su master password y realiza la comprobación, antes de crear una social network con los datos introducidos.
   @IBAction func askMasterPasswordToCreate(_ sender: UIButton){
       let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
       ac.addTextField()
       ac.textFields?[0].textContentType = .password
       ac.textFields?[0].isSecureTextEntry = true
       
       let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
           //Recojemos la master pass insertada por el usuario
           //Si es correcta...
           if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
               
               self.createSocialnetwork(key: sha256.encrypt(message: ac.textFields![0].text!)) //Creamos la Social network.
               
           //Si es incorrecta...
           } else {
               self.showError(self.ERROR_LABEL, self.ERROR_PRIVATE_KEY) //Error: master password incorrecta...
           }
       }
       ac.addAction(validateAction)
       ac.addAction(UIAlertAction(title: CANCEL_BUTTON, style: .cancel))
       present(ac, animated: true)
   }
    
    //Metodo que crea una nueva social network con los datos introducidos.
    func createSocialnetwork(key: String){
        //Recojo los campos...
        let webName = webNameTextField.text ?? ""
        let url = urlTextField.text ?? ""
        let email = emailTextField.text ?? ""
        let userNameText = userNameTextField.text ?? ""
        let password = passwordTextField.text ?? ""
        //Si el usuario completó todos los campos...
        if(!webName.isEmpty && !url.isEmpty && !email.isEmpty && !userNameText.isEmpty && !password.isEmpty){
            
            //Creamos una social network
            let newID = SecretFileManager.shared.secretBox.accounts.socialNetworks.count + 1
            let newSocialNetwork: SocialNetwork = SocialNetwork(id: newID, webname: webName, url: url, email: email, username: userNameText, password: password)

            //Agregamos la nueva red social creada, al secret box del usuario.
            SecretFileManager.shared.secretBox.accounts.socialNetworks.append(newSocialNetwork)
            //Recojo la ubicación del fichero que contiene el SecretBox.
            let fileURL = SecretFileManager.shared.getMyFileURL()!
            //Escribimos el archivo del usuario con los nuevos datos.
            SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
            
            showCreationSuccesful() //Mostramos mensaje de exito al crear, y devolverá al usuario a la vista padre.
            
        }else{
            showError(ERROR_LABEL, ERROR_FIELDS) //Error campos vacíos...
        }
    }
    
    //Metodo que muestra un alert al usuario indicandole un error.
    func showError(_ title: String, _ message: String){
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
    
    //CREACIÓN EXITOSA: Mostrar una alerta con un mensaje de creación exitosa que nos devuelve al listado de social networks.
    func showCreationSuccesful() {
        let ac = UIAlertController(title: CREATION_SUCESS_LABEL, message: CREATION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToSocialNetworks))
        present(ac, animated: true)
    }
    
    //Volvemos a la vista padre (SocialNetwork)
    func goBackToSocialNetworks(_ action: UIAlertAction){
        self.navigationController?.popViewController(animated: true)
    }

}
