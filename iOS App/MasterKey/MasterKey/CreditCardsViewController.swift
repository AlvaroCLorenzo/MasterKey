//
//  CreditCarsViewController.swift
//  MasterKey
//
//  Created by Wembley on 19/4/22.
//
import UIKit

class CreditCardsViewController: UITableViewController {
    
    let VIEW_TITLE = "YOUR CREDIT CARDS: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let CRETE_ADVISE: String = "Please, create a new credit card..."
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    var creditCards = [CreditCard]()
    
    override func viewWillAppear(_ animated: Bool) {
        //Cargamos las credit cards antes de que aparezca la vista y que así se puedan mostrar.
        creditCards = SecretFileManager.shared.secretBox.accounts.creditCards
        self.tableView.reloadData()
    }
    
    override func viewDidLoad() {
        title = VIEW_TITLE
        //Botón para crear una nueva credit card.
        navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(self.showCreateCreditCardView))
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var count = creditCards.count
        //Si aun no se ha creado ninguna...
        if count == 0 {
            count = 1 //Seteamos = 1 para que aparezca un mensaje si no hay redes sociales todavía.
        }
        return count //Devuelve el numero de elementos que aparecen.
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: .default, reuseIdentifier: "CreditCard")
        //Si hay credit cards, mostramos sus datos identificativos...(No sensibles)
        if creditCards.count > 0 {
            cell.textLabel?.text = "ID: \(creditCards[indexPath.row].id), Bank: \(creditCards[indexPath.row].nameOfBank), Type: \(creditCards[indexPath.row].typeOfCard)"
            cell.accessoryType = .disclosureIndicator
        //Si aun no se han creado social networks...
        } else {
            cell.textLabel?.text = CRETE_ADVISE //Mostramos mensaje al usuario en la tabla.
        }
        return cell; //Retorna la celda modificada
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //Si hay alguna credit card creada...
        if let content =  tableView.cellForRow(at: indexPath)?.textLabel?.text {
            if content != CRETE_ADVISE {
                //Preguntamos por la master password del usuario para abrir su contenido...
                self.askForMasterPassword(self.creditCards[indexPath.row])
            }
        }
    }
    
    //Método que pide al usuario su master password y realiza la comprobación, antes de dar paso a la vista de creación o de edición.
    func askForMasterPassword(_ creditCard: CreditCard) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true //Añadimos al alert un campo de textfield de tipo password
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la master pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                self.showDetailCreditCardView(creditCard) //Visualizamos la credit card escogida en su vista de detalle
            //Si es incorrecta...
            } else {
                self.showError(self.ERROR_LABEL, self.ERROR_PRIVATE_KEY) //Error con la master password
            }
        }
        ac.addAction(validateAction)
        ac.addAction(UIAlertAction(title: CANCEL_BUTTON, style: .cancel))
        present(ac, animated: true)
    }
    
    //Método que lanza la vista de crecion de una credit card para guardar sus datos.
    @objc func showCreateCreditCardView() {
        let storyboard = UIStoryboard(name: "CreditCards", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "CreateCreditCard") as? CreateCreditCardViewController {
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    //Método que lanza la vista de detalle de una credit card para su edicion. (Recible la credit card)
    func showDetailCreditCardView(_ creditCard: CreditCard) {
        let storyboard = UIStoryboard(name: "CreditCards", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "DetailCreditCard") as? DetailCreditCardViewController {
            vc.myCreditCard = creditCard
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    //Metodo que muestra un alert al usuario indicandole un error.
    func showError(_ title: String, _ message: String){
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
    
    
}
