//
//  DetailCreditCardViewController.swift
//  MasterKey
//
//  Created by Wembley on 21/4/22.
//

import UIKit

class DetailCreditCardViewController: UIViewController {
    
    let VIEW_TITLE = "YOUR CREDIT CARD:"
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_FIELDS = "Please, compleate all the fields."
    let EDITION_SUCESS_LABEL = "SUCCESFUL EDITION: "
    let EDITION_SUCCESS_MESSAGE = "Your credit card was edited succesfuly"
    let DELETION_SUCESS_LABEL = "SUCCESFUL DELETION: "
    let DELETION_SUCCESS_MESSAGE = "Your credit card was deleted succesfuly"
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"

    @IBOutlet var bankNameTextField: UITextField!
    @IBOutlet var typeOfCardTextField: UITextField!
    @IBOutlet var numberOfCardTextField: UITextField!
    @IBOutlet var userNameTextField: UITextField!
    @IBOutlet var endDateTextField: UITextField!
    @IBOutlet var CCVTextField: UITextField!
    @IBOutlet var PINTextField: UITextField!
    
    var myCreditCard: CreditCard!
    
    override func viewDidLoad() {
        title = VIEW_TITLE
        //Mostramos los campos de nuestra credit card seleccionada en pantalla
        bankNameTextField.text = myCreditCard.nameOfBank
        typeOfCardTextField.text = myCreditCard.typeOfCard
        numberOfCardTextField.text = String(myCreditCard.numberOfCard)
        userNameTextField.text = myCreditCard.userName
        endDateTextField.text = myCreditCard.expirationDate
        CCVTextField.text = String(myCreditCard.CCV)
        PINTextField.text = String(myCreditCard.PIN)
    }
    
    //Método que pide al usuario su master pass y realiza la comprobación, antes de editar o borrar una credit card.
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
                        //editamos la credit card
                    self.editCreditCard(key: sha256.encrypt(message: ac.textFields![0].text!))
                    //Si pulsó el boton de borrar...
                    case 1:
                        //eliminamos la credit card
                    self.deleteCreditCard(key: sha256.encrypt(message: ac.textFields![0].text!))
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
    
    //Método que edita la credit card seleccionada por el usuario.
    func editCreditCard(key: String){
        //Recojo los campos introducidos
        let bankName = bankNameTextField.text ?? ""
        let typeOfCard = typeOfCardTextField.text ?? ""
        let numberOfCard = numberOfCardTextField.text ?? ""
        let userName = userNameTextField.text ?? ""
        let expirationDate = endDateTextField.text ?? ""
        let CCV = CCVTextField.text  ?? ""
        let PIN = PINTextField.text  ?? ""
        //Si el usuario completó todos los campos...
        if(!bankName.isEmpty && !typeOfCard.isEmpty && !numberOfCard.isEmpty && !userName.isEmpty && !expirationDate.isEmpty && !CCV.isEmpty && !PIN.isEmpty){
            //Recorremos las social networks del usuario.
            var creditCards = SecretFileManager.shared.secretBox.accounts.creditCards
            for creditCard in creditCards {
                //Cuando encontramos la seleccionada... Editamos sus datos.
                if creditCard.id == myCreditCard.id {
                    creditCards[creditCard.id - 1].nameOfBank = bankName
                    creditCards[creditCard.id - 1].typeOfCard = typeOfCard
                    creditCards[creditCard.id - 1].numberOfCard = Int(numberOfCard) ?? 0
                    creditCards[creditCard.id - 1].userName = userName
                    creditCards[creditCard.id - 1].expirationDate = expirationDate
                    creditCards[creditCard.id - 1].CCV = Int(CCV) ?? 0
                    creditCards[creditCard.id - 1].PIN = Int(PIN) ?? 0
                }
            }
            //Actualizamos el array de credit cards
            SecretFileManager.shared.secretBox.accounts.creditCards = creditCards
            //Obtenemos la ruta del archivo secreto del usuario
            let fileURL = SecretFileManager.shared.getMyFileURL()!
            //Escribimos el archivo con los nuevos datos
            SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
            
            self.showEditionSuccesful() //Mostramos mensaje de exito y volvemos a la vista padre.
            
        }else{
            self.showError(ERROR_LABEL, ERROR_FIELDS) //Error campos vacios
        }
    }
      
    //Método que elimina la credit card seleccionada por el usuario.
    func deleteCreditCard(key: String){
        var newCreditCards = [CreditCard]()
        //Recorremos las credit cards del usuario
        var creditCards = SecretFileManager.shared.secretBox.accounts.creditCards
        for creditCard in creditCards {
            //Mientras no encontremos la seleccionada... Editamos sus datos.
            if creditCard.id != self.myCreditCard.id {
                //Actualizamos su ID
                creditCards[creditCard.id - 1].id = newCreditCards.count + 1
                newCreditCards.append(creditCards[creditCard.id - 1]) //Añadimos las credit cards cuyos ids no sean igual que el elegido
            }
            //Si coincide el iD, no la añadimos
        }
        //Actualizamos el array de credit cards
        SecretFileManager.shared.secretBox.accounts.creditCards = newCreditCards
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
    
    //Muestra una alerta con un mensaje de edicion exitosa y que nos devuelve a la vista padre al aceptarlo
    func showEditionSuccesful() {
        let ac = UIAlertController(title: EDITION_SUCESS_LABEL, message: EDITION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToCreditCards))
        present(ac, animated: true)
    }
    
    //Muestra una alerta con un mensaje de borrado exitoso y que nos devuelve a la vista padre al aceptarlo
    func showDeletionSuccesful() {
        let ac = UIAlertController(title: DELETION_SUCESS_LABEL, message: DELETION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToCreditCards))
        present(ac, animated: true)
    }
    
    //Volvemos a la vista padre (Credit Cards)
    func goBackToCreditCards(_ action: UIAlertAction){
        self.navigationController?.popViewController(animated: true)
    }
}
