//
//  AutoritationViewController.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import UIKit

class AutoritationViewController: UIViewController {
    
    let APP_LABEL = "MASTER KEY"
    let APP_WELCOME = "Welcome!\nNice to see you again."
    let ERROR_LABEL = "ERROR: "
    let ERROR_FIELDS = "Please, complete all the fields before opening"
    let ERROR_CREDENTIALS = "The credentials do not match.\nPlease make sure your nickname and master password are correct."
    let OK_BUTTON = "OK"
    let REGISTER_LABEL = "REGISTER OF A NEW USER"
    let REGISTER_OF_NEW_USER = "We are glad that you are using MasterKey to improve the security of your accounts.\nWe use military grade encryption to protect your data.\nPlease enter a nickname and a masterpassword and they will be the only secret words you will have to remember.\nBest regards, MASTER-KEY 2022"
    let SHOW_PASSWORD_LABEL = "SHOW PASSWORD"
    let HIDE_PASSWORD_LABEL = "HIDE PASSWORD"
    
    @IBOutlet var nickNameTextField: UITextField!
    @IBOutlet var masterPassTextField: UITextField!
    @IBOutlet var showPasswordButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = APP_LABEL
                        
        //Cuando la aplicación se inicia, pedimos el archivo secreto...
        SecretFileManager.shared.getSecretBoxFile()
        
        //Depende de si es un nuevo usuario, mostramos un mensaje de Registro o de bienvenida de inicio de sesión.
        if SecretFileManager.shared.isNewUser {
            self.showRegisterWelcome()
        }else{
            self.showLoginWelcome()
        }
        
        //RELLENO DE DATOS DEL LOGIN PARA PRUEBAS:
        nickNameTextField.text = "Alvaro"
        masterPassTextField.text = "12345"
        
    }
    
    //Método llamado cuando la vista desaparece, borramos los datos en pantalla
    override func viewDidDisappear(_ animated: Bool) {
        nickNameTextField.text = ""
        masterPassTextField.text = ""
    }
    
    
    //Método asociado al boton de mostrar contraseña, y que permite al usuario visualizarla u ocultarla cuando desee.
    @IBAction func showPassword(_ sender: UIButton) {
        if masterPassTextField.isSecureTextEntry {
            masterPassTextField.isSecureTextEntry = false
            showPasswordButton.setAttributedTitle(HIDE_PASSWORD_LABEL.getAttributedBoldText(text: HIDE_PASSWORD_LABEL, size: 16), for: .normal)
        }else{
            masterPassTextField.isSecureTextEntry = true
            showPasswordButton.setAttributedTitle(SHOW_PASSWORD_LABEL.getAttributedBoldText(text: SHOW_PASSWORD_LABEL, size: 16), for: .normal)
        }
    }
        
    //MÉTODO LLAMADO POR EL BOTÓN DE APERTURA
    @IBAction func openMyAccount(_ sender: UIButton) {
        //Obtenemos las credenciales introducidas por el usuario
        if let nickName = nickNameTextField.text{
            if let masterPassword = masterPassTextField.text {
                //Si los campos no estaban vacios...
                if !nickName.isEmpty || !masterPassword.isEmpty {
                    
                    //Si es un nuevo usuario...REGISTRO DE NUEVO USUARIO
                    if SecretFileManager.shared.isNewUser {
                        
                        //Creamos un perfil vacio en el secretbox
                        SecretFileManager.shared.createANewProfile()
                        //Escribimos las nuevas credenciales en el secret box
                        SecretFileManager.shared.secretBox.user.nickName = nickName
                        SecretFileManager.shared.secretBox.user.masterPassword = masterPassword
                        
                        //Obtenemos la ruta donde se situa el archivo
                        let fileURL = SecretFileManager.shared.getMyFileURL()!
                        
                        //Escribimos el contenido de nuestro secret box en el fichero. (Realizamos el hasheo de la masterPassword)
                        SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: sha256.encrypt(message: masterPassword))
                        
                        showYourAccountsView() //Lanzamos la Home View (Your Accounts)
                        
                    //Si no es un nuevo Usuario... LOGIN
                    }else{
                        //Leemos el archivo encriptado a traves de nuestra master password
                        let _ = SecretFileManager.shared.readSecretFile(file: FileHandle(forReadingAtPath: SecretFileManager.shared.getMyFileURL()!.path)!, privateKey: sha256.encrypt(message: masterPassword))
                        //Comprobamos que exista el secretBox.
                        if SecretFileManager.shared.secretBox != nil {
                            //Comprobamos que las credenciales coinciden...
                            if nickName == SecretFileManager.shared.secretBox.user.nickName
                                && masterPassword == SecretFileManager.shared.secretBox.user.masterPassword {
                                showYourAccountsView() //ABRIMOS LA VISTA YOUR ACCOUNTS
                                
                            } else {
                                showErrorCredentials() //Error credenciales incorrectas
                            }
                        }
                    }
                }else{
                    showErrorFields() //Error campos vacios
                }
            }
        }
    }
    
    //Método dedicado a abrir la vista de Your Accounts o Home View.
    func showYourAccountsView() {
        if let vc = navigationController?.storyboard?.instantiateViewController(withIdentifier: "HomeView") as? HomeViewController {
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
        
    //LOGIN WELCOME: Muestra un mensaje al usuario cuando se está logeando.
    func showLoginWelcome() {
        let ac = UIAlertController(title: APP_LABEL, message: APP_WELCOME, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
        
    //REGISTER WELCOME: Muestra un mensaje al usuario cuando se está registrando.
    func showRegisterWelcome() {
        let ac = UIAlertController(title: REGISTER_LABEL, message: REGISTER_OF_NEW_USER, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
        
    //ERROR: Campos vacios
    func showErrorFields(){
        let ac = UIAlertController(title: ERROR_LABEL, message: ERROR_FIELDS, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
    
    //ERROR: Credenciales incorrectas para login
    func showErrorCredentials(){
        let ac = UIAlertController(title: ERROR_LABEL, message: ERROR_CREDENTIALS, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }

}

//Extension para strings que hace que sea capaz de atribuir un texto en negrita directamente.
//Usado para el boton "show password"
extension String {
    // Retorna el Atributted String
    // Texto del parámetro: Cadena de texto para el texto en negrita./Size: tamaño de la letra
    func getAttributedBoldText(text: String, size: CGFloat) -> NSMutableAttributedString {
        let attributedString = NSMutableAttributedString(string: self, attributes: [.foregroundColor: UIColor.black])
        if let range = self.range(of: text) {
            let startIndex = self.distance(from: self.startIndex, to: range.lowerBound)
            let range = NSMakeRange(startIndex, text.count)
            attributedString.addAttributes([.font : UIFont.boldSystemFont(ofSize: size)], range: range)
        }
        return attributedString //Retorna el string con atributos
    }
}

