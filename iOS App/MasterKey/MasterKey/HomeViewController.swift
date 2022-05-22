//
//  HomeViewController.swift
//  MasterKey
//
//  Created by Wembley on 18/4/22.
//

import UIKit

class HomeViewController: UIViewController {
    
    let APP_TITLE = "MASTER KEY"
    let SECURITY_STEP_LABEL = "SECURITY STEP: "
    let SECURITY_STEP_MESSAGE = "Please, introduce your master password: "
    let ERROR_LABEL = "ERROR: "
    let ERROR_PRIVATE_KEY = "The Master password do not match.\nPlease make sure your master password is correct."
    let OK_BUTTON = "OK"
    let CANCEL_BUTTON = "Cancel"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = APP_TITLE
        SecretFileManager.shared.isNewUser = false //Al entrar en esta vista, ya no es un usuario nuevo
    }
    
    //Método que pide al usuario su master password y realiza la comprobación, antes de dar paso a ciertas vistas.
    @IBAction func askMasterPasswordToCreate(_ sender: UIButton) {
        let ac = UIAlertController(title: SECURITY_STEP_LABEL, message: SECURITY_STEP_MESSAGE, preferredStyle: .alert)
        ac.addTextField()
        ac.textFields?[0].textContentType = .password
        ac.textFields?[0].isSecureTextEntry = true
        
        let validateAction = UIAlertAction(title: OK_BUTTON, style: .default) { (action) in
            //Recojemos la master pass insertada por el usuario
            //Si es correcta...
            if ac.textFields![0].text == SecretFileManager.shared.secretBox.user.masterPassword {
                switch sender.tag{
                case 0:
                    self.showSocialNetworks() //Lanzamos vista de social networks
                case 1:
                    self.showCreditCards() //Lanzamos vista de credit cards
                case 2:
                    self.showCryptoWallets() //Lanzamos vista de crypto wallets
                default:
                    break
                }
                
            //Si es incorrecta...
            } else {
                self.showError(self.ERROR_LABEL, self.ERROR_PRIVATE_KEY) //Error: master password incorrecta...
            }
        }
        ac.addAction(validateAction)
        ac.addAction(UIAlertAction(title: CANCEL_BUTTON, style: .cancel))
        present(ac, animated: true)
    }
    
    //Método que lanza la vista de la tabla con las social networks del usuario.
    func showSocialNetworks() {
        let storyboard = UIStoryboard(name: "SocialNetworks", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "SocialNetworks") as? SocialNetWorksViewController {
            navigationController?.pushViewController(vc, animated: true)
        }
    
    }
    
    //Método que lanza la vista de la tabla con las credit cards del usuario.
    func showCreditCards() {
        let storyboard = UIStoryboard(name: "CreditCards", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "CreditCards") as? CreditCardsViewController {
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    //Método que lanza la vista de la tabla con las crypto wallets del usuario.
    func showCryptoWallets() {
        let storyboard = UIStoryboard(name: "CryptoWallets", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "CryptoWallets") as? CryptoWalletsViewController {
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    //Método que lanza la vista de encriptacion/desencriptacion
    @IBAction func showEncrypterDecrypter(_ sender: UIButton) {
        if let vc = navigationController?.storyboard?.instantiateViewController(withIdentifier: "EncrypterDecrypter") as? EncrypterDecrypterViewController {
            navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    //Lanza la vista del perfil de usuario
    @IBAction func showMyProfile(_ sender: Any) {
        if let vc = navigationController?.storyboard?.instantiateViewController(withIdentifier: "MyProfile") as? MyProfileViewController {
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
