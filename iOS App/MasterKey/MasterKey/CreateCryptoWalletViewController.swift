//
//  DetailCryptoWalletViewController.swift
//  MasterKey
//
//  Created by Wembley on 19/4/22.
//

import UIKit

class CreateCryptoWalletViewController: UIViewController {

    let VIEW_TITLE = "CREATE A CRYPTO WALLET: "
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_FIELDS = "Please, compleate all the fields."
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_NUMBER = "The fields: number of card, CCV & PIN, must be complete with numbers.\nPlease, complete your data correctly."
    let CREATION_SUCESS_LABEL = "SUCCESFUL CREATION: "
    let CREATION_SUCCESS_MESSAGE = "Your crypto wallet was created succesfuly"
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    @IBOutlet var walletNameTextField: UITextField!
    @IBOutlet var typeOfWalletTextField: UITextField!
    @IBOutlet var emailAsotiatedTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    @IBOutlet var publicKeyTextField: UITextField!
    @IBOutlet var privateKeyTextField: UITextField!
    
    override func viewDidLoad() {
        title = VIEW_TITLE
    }
    
    //Método que pide al usuario su master password y realiza la comprobación, antes de crear una crypto wallet con los datos introducidos.
    @IBAction func askMasterPasswordToCreate(_ sender: UIButton) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la master pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                
                self.createCryptoWallet(key: sha256.encrypt(message: ac.textFields![0].text!)) //creamos la crypto wallet
            //Si es incorrecta...
            } else {
                self.showError(self.ERROR_LABEL, self.ERROR_PRIVATE_KEY) //Error: master password incorrecta.
            }
        }
        ac.addAction(validateAction)
        ac.addAction(UIAlertAction(title: CANCEL_BUTTON, style: .cancel))
        present(ac, animated: true)
    }
    
    //Metodo que crea una nueva crypto wallet con los datos introducidos.
    func createCryptoWallet(key: String) {
        //Recojo los campos...
        let walletName = walletNameTextField.text ?? ""
        let typeOfWallet = typeOfWalletTextField.text ?? ""
        let emailAsotiated = emailAsotiatedTextField.text ?? ""
        let passwordText = passwordTextField.text ?? ""
        let publicKey = publicKeyTextField.text ?? ""
        let privateKey = privateKeyTextField.text ?? ""
        //Si completó todos los campos obligatorio correctamente...
        if (!walletName.isEmpty && !typeOfWallet.isEmpty && !emailAsotiated.isEmpty && !passwordText.isEmpty) {
            //Creamos una Crypto wallet y volvemos a la vista padre.
            let newID = SecretFileManager.shared.secretBox.accounts.cryptoWallets.count + 1
            let newCryptoWallet: CryptoWallet = CryptoWallet(id: newID, walletName: walletName, walletType: typeOfWallet, email: emailAsotiated, password: passwordText, publicKey: publicKey, privateKey: privateKey)
            
            //Agregamos la nueva crypto wallet creada, al secret box del usuario.
            SecretFileManager.shared.secretBox.accounts.cryptoWallets.append(newCryptoWallet)
            //Recojo la ubicación del fichero que contiene el SecretBox.
            let fileURL = SecretFileManager.shared.getMyFileURL()!
            //Escribimos el archivo con los nuevos datos
            SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
            
            showCreationSuccesful() //Mostramos mensaje de exito al crear, que nos devolverá a la vista padre.
        } else{
            showError(ERROR_LABEL, ERROR_FIELDS) //Error campos vacíos...
        }
    }
    
    //Metodo que muestra un alert al usuario indicandole un error.
    func showError(_ title: String, _ message: String){
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
    
    //CREACIÓN EXITOSA: Muestra una alerta con un mensaje de creación exitosa que nos devuelve al listado de crypto wallets.
    func showCreationSuccesful() {
        let ac = UIAlertController(title: CREATION_SUCESS_LABEL, message: CREATION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToCryptoWallets))
        present(ac, animated: true)
    }
    
    //Volvemos a la vista padre (Crypto Wallets)
    func goBackToCryptoWallets(_ action: UIAlertAction){
        self.navigationController?.popViewController(animated: true)
    }
    
}
