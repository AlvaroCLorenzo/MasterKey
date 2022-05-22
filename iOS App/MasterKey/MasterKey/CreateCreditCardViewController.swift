//
//  DetailCreditCardView.swift
//  MasterKey
//
//  Created by Wembley on 19/4/22.
//

import UIKit

class CreateCreditCardViewController: UIViewController {
    
    let VIEW_TITLE = "CREATE A CREDIT CARD: "
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_FIELDS = "Please, compleate all the fields."
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let ERROR_NUMBER = "The fields: number of card, CCV & PIN, must be complete with numbers.\nPlease, complete your data correctly."
    let CREATION_SUCESS_LABEL = "SUCCESFUL CREATION: "
    let CREATION_SUCCESS_MESSAGE = "Your credit card was created succesfuly"
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    @IBOutlet var banKNameTextField: UITextField!
    @IBOutlet var typeOfCardTextField: UITextField!
    @IBOutlet var numberOfCardTextField: UITextField!
    @IBOutlet var userNameTextField: UITextField!
    @IBOutlet var endDateTextField: UITextField!
    @IBOutlet var CCVtextField: UITextField!
    @IBOutlet var PINTextField: UITextField!
    
    override func viewDidLoad() {
        title = VIEW_TITLE
    }
    
    //Método que pide al usuario su master password y realiza la comprobación, antes de crear una credit card con los datos introducidos.
    @IBAction func askMasterPasswordToCreate(_ sender: UIButton) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la master pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                
                self.createCreditCard(key: sha256.encrypt(message: ac.textFields![0].text!)) //creamos la credit card
            //Si es incorrecta...
            } else {
                self.showError(self.ERROR_LABEL, self.ERROR_PRIVATE_KEY) //ERROR: Master password incorrecta
            }
        }
        ac.addAction(validateAction)
        ac.addAction(UIAlertAction(title: CANCEL_BUTTON, style: .cancel))
        present(ac, animated: true)
    }
   
    //Metodo que crea una nueva credit card con los datos introducidos.
    func createCreditCard(key: String) {
        //Recojo los campos...
        let bankName = banKNameTextField.text ?? ""
        let typeOfCard = typeOfCardTextField.text ?? ""
        let numberOfCard = numberOfCardTextField.text ?? ""
        let userName = userNameTextField.text ?? ""
        let endDate = endDateTextField.text ?? ""
        let CCV = CCVtextField.text ?? ""
        let PIN = PINTextField.text ?? ""
        //Si completó todos los campos correctamente...
        if (!bankName.isEmpty && !typeOfCard.isEmpty && !numberOfCard.isEmpty && !endDate.isEmpty && !CCV.isEmpty && !PIN.isEmpty) {
            if let numOfCard = Int(numberOfCard){
                if let CCVnum = Int(CCV) {
                    if let PINnum = Int(PIN) {
                        //Creamos una credit card y volvemos a la vista padre.
                        let newID = SecretFileManager.shared.secretBox.accounts.creditCards.count + 1
                        let newCreditCard: CreditCard = CreditCard(id: newID, nameOfBank: bankName, typeOfCard: typeOfCard, userName: userName, numberOfCard: numOfCard, expirationDate: endDate, CCV: CCVnum, PIN: PINnum)
                        
                        //Agregamos la nueva credit card creada, al secret box del usuario.
                        SecretFileManager.shared.secretBox.accounts.creditCards.append(newCreditCard)
                        //Recojo la ubicación del fichero que contiene el SecretBox.
                        let fileURL = SecretFileManager.shared.getMyFileURL()!
                        //Escribimos el archivo con los nuevos datos
                        SecretFileManager.shared.writeSecretBoxInFile(secretBox: SecretFileManager.shared.secretBox, atPath: fileURL.path, privateKey: key)
                        
                        showCreationSuccesful() //Mostramos mensaje de exito al crear, que nos devolverá a la vista padre.
                    }else {
                        showError(ERROR_LABEL, ERROR_NUMBER) //Error numerico... (debido a PIN)
                    }
                }
                else {
                    showError(ERROR_LABEL, ERROR_NUMBER)  //Error numerico... (debido a CCV)
                }
            }else{
                showError(ERROR_LABEL, ERROR_NUMBER)  //Error numerico... (debido a Card number)
            }
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
    
    //CREACIÓN EXITOSA: Mostrar una alerta con un mensaje de creación exitosa que nos devuelve al listado de credit cards.
    func showCreationSuccesful() {
        let ac = UIAlertController(title: CREATION_SUCESS_LABEL, message: CREATION_SUCCESS_MESSAGE, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default, handler: goBackToCreditCards))
        present(ac, animated: true)
    }
    
    //Volvemos a la vista padre (Credit Cards)
    func goBackToCreditCards(_ action: UIAlertAction){
        self.navigationController?.popViewController(animated: true)
    }
    
}

