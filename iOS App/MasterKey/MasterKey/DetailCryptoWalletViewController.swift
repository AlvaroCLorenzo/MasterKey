//
//  DetailCryptoWalletViewController.swift
//  MasterKey
//
//  Created by Wembley on 21/4/22.
//

import UIKit

class DetailCryptoWalletViewController: UIViewController {
    
    let VIEW_TITLE = "YOUR CRYPTO WALLET:"
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_FIELDS = "Please, compleate all the fields."
    let EDITION_SUCCESS_LABEL = "SUCCESFUL EDITION: "
    let EDITION_SUCCESS_MESSAGE = "Your crypto wallet was edited succesfuly"
    let DELETION_SUCCESS_LABEL = "SUCCESFUL DELETION: "
    let DELETION_SUCCESS_MESSAGE = "Your crypto wallet was deleted succesfuly"
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    @IBOutlet var walletNameTextField: UITextField!
    @IBOutlet var typeOfWalletTextField: UITextField!
    @IBOutlet var emailTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    @IBOutlet var publicKeyTextField: UITextField!
    @IBOutlet var privateKeyTextField: UITextField!
        
    var myCryptoWallet: CryptoWallet!
    
    override func viewDidLoad() {
        title = VIEW_TITLE
        //We show the fields of our selected crypto wallet on the screen
        walletNameTextField.text = myCryptoWallet.walletName
        typeOfWalletTextField.text = myCryptoWallet.walletType
        emailTextField.text = myCryptoWallet.email
        passwordTextField.text = myCryptoWallet.password
        publicKeyTextField.text = myCryptoWallet.publicKey
        privateKeyTextField.text = myCryptoWallet.privateKey
    }
    
    //Método que pide al usuario su master pass y realiza la comprobación, antes de editar o borrar una crypto wallet
    @IBAction func askForMasterPassword(_ sender: UIButton) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                //Depende del botón pulsado...
                switch sender.tag{
                    //Si pulsó el boton de editar...
                    case 0:
                        //editamos la crypto wallet
                    self.editSocialNetwork(key: sha256.encrypt(message: ac.textFields![0].text!))
                    //Si pulsó el boton de borrar...
                    case 1:
                        //eliminamos la crypto wallet
                    self.deleteCryptoWallet(key: sha256.encrypt(message: ac.textFields![0].text!))
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
    
    //Método que edita la crypto wallet seleccionada por el usuario.
    func editSocialNetwork(key: String) {
        let walletName = walletNameTextField.text ?? ""
        let walletType = typeOfWalletTextField.text ?? ""
        let email = emailTextField.text ?? ""
        let password = passwordTextField.text ?? ""
        let publicKey = publicKeyTextField.text ?? "" //OPCIONAL
        let privateKey = privateKeyTextField.text ?? "" //OPCIONAL
        //Si el usuario completó todos los campos...
        if(!walletName.isEmpty && !walletType.isEmpty && !email.isEmpty && !password.isEmpty){
            //Recorremos las social networks del usuario.
            var cryptoWallets = SecretFileManager.shared.secretBox.accounts.cryptoWallets
            for cryptoWallet in cryptoWallets {
                //Cuando encontramos la seleccionada... Editamos sus datos.
                if cryptoWallet.id == self.myCryptoWallet.id {
                    cryptoWallets[cryptoWallet.id-1].walletName = walletName
                    cryptoWallets[cryptoWallet.id-1].walletType = walletType
                    cryptoWallets[cryptoWallet.id-1].email = email
                    cryptoWallets[cryptoWallet.id-1].password = password
                    cryptoWallets[cryptoWallet.id-1].publicKey = publicKey
                    cryptoWallets[cryptoWallet.id-1].privateKey = privateKey
                }
            }
            //Actualizamos el array de crypto wallets
            SecretFileManager.shared.secretBox.accounts.cryptoWallets = cryptoWallets
            //Obtenemos la ruta del archivo secreto del usuario
            let fileURL = SecretFileManager.shared.getMyFileURL()!
            //Escribimos el archivo con los nuevos datos
            SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
            
            showEditionSuccesful() //Mostramos mensaje de exito y volvemos a la vista padre.
            
        }else{
            self.showError(ERROR_LABEL, ERROR_FIELDS) //Error: campos vacios
        }
    }
    
    //Método que elimina la crypto wallet seleccionada por el usuario.
    func deleteCryptoWallet(key: String) {
        var newCryptoWallets = [CryptoWallet]()
        //Recorremos las crypto wallets del usuario
        var cryptoWallets = SecretFileManager.shared.secretBox.accounts.cryptoWallets
        for cryptoWallet in cryptoWallets {
            //Mientras no encontremos la seleccionada... Editamos sus datos.
            if cryptoWallet.id != self.myCryptoWallet.id {
                //Actualizamos su ID
                cryptoWallets[cryptoWallet.id - 1].id = newCryptoWallets.count + 1
                newCryptoWallets.append(cryptoWallets[cryptoWallet.id - 1]) //Añadimos las crypto wallets cuyos ids no sean igual que el elegido
            }
            //Si coincide el iD, no la añadimos
        }
        //Actualizamos el array de crypto wallets
        SecretFileManager.shared.secretBox.accounts.cryptoWallets = newCryptoWallets
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
    
    //Show an alert with a edition successful message
    func showEditionSuccesful() {
        let ac = UIAlertController(title: EDITION_SUCCESS_LABEL, message: EDITION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToCryptoWallets))
        present(ac, animated: true)
    }
    
    //Show an alert with a edition successful message
    func showDeletionSuccesful() {
        let ac = UIAlertController(title: DELETION_SUCCESS_LABEL, message: DELETION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToCryptoWallets))
        present(ac, animated: true)
    }
    
    //We go back to the father view (SocialNetwork)
    func goBackToCryptoWallets(_ action: UIAlertAction){
        self.navigationController?.popViewController(animated: true)
    }
    
}
