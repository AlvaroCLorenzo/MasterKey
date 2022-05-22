//
//  EncrypterDecrypterViewController.swift
//  MasterKey
//
//  Created by Wembley on 21/4/22.
//

import UIKit

class EncrypterDecrypterViewController: UIViewController {
    
    let VIEW_TITLE = "ENCRYPTER/DECRYPTER: "
    let ENCRYPT_LABEL = "ENCRYPT"
    let DECRYPT_LABEL = "DECRYPT"
    let ERROR_LABEL = "ERROR: "
    let ERROR_FIELDS = "Please, introduce a private key to encrypt/decrypt your text."
    let OK_BUTTON = "OK"
    
    @IBOutlet var privateKeyTextField: UITextField!
    @IBOutlet var textArea1: UITextView!
    @IBOutlet var textArea2: UITextView!
    @IBOutlet var switchEncryptDecrypt: UISwitch!{ //Para cambiar el color del swich cuando el usuario cambia su estado
        didSet{
            switchEncryptDecrypt.tintColor = .darkGray
            switchEncryptDecrypt.subviews[0].subviews[0].backgroundColor = .darkGray
        }
    }
    @IBOutlet var encyrptDecryptLabel: UILabel!
    @IBOutlet var encrypterDecrypterButton: UIButton!
    
    var mode = "ENCRYPT"
    
    override func viewDidLoad() {
        title = VIEW_TITLE
        textArea2.isEditable = false //Para que el usuario no pueda modificar el text area 2, solo copiar texto.
    }
    
    //Metodo que cambia de modo (Encriptacion/Desencriptacion) de la vista, cambiando tambien colores para mejor visualizacion.
    @IBAction func switchEncrypterMode(_ sender: UISwitch) {
        //Si el modo encrypt está activado... Encrypt.
        if self.switchEncryptDecrypt.isOn {
            
            self.mode = self.ENCRYPT_LABEL
            self.encyrptDecryptLabel.text = "Encrypt"
            self.encrypterDecrypterButton.setTitle(self.ENCRYPT_LABEL, for: .normal)
            self.encrypterDecrypterButton.setAttributedTitle(self.ENCRYPT_LABEL.getAttributedBoldText(text: self.ENCRYPT_LABEL, size: 18), for: .normal)
            self.encrypterDecrypterButton.setTitleColor(.white, for: .highlighted)
            self.encrypterDecrypterButton.backgroundColor = .darkGray
            self.switchEncryptDecrypt.onTintColor = UIColor(named: "PrimaryGreen")
        //Si el modo encrypt está destivado... Decrypt.
        } else {
            self.mode = self.DECRYPT_LABEL
            self.encyrptDecryptLabel.text = "Decrypt"
            self.switchEncryptDecrypt.onTintColor = .black
            self.encrypterDecrypterButton.setAttributedTitle(self.DECRYPT_LABEL.getAttributedBoldText(text: self.DECRYPT_LABEL, size: 18), for: .normal)
            self.encrypterDecrypterButton.backgroundColor = UIColor(named: "PrimaryOrange")
            self.encrypterDecrypterButton.setTitleColor(.black, for: .highlighted)
        }
    }
    
    //Metodo activado por el botón (ENCRYPT/DECRYPT) el cual recoje la private key y el texto cifrado/descifrado y lo devuelve cifrado/descifrado.
    @IBAction func encryptDecrypt(_ sender: UIButton) {
        if let privateKey = privateKeyTextField.text {
            //Si se introdujo una private key (puede ser cualquiera)...
            if !privateKey.isEmpty {
                //Producimos el hash de la master password con SHA256, el cual sera la verdadera clave privada
                let hashedPrivateKey = sha256.encrypt(message: privateKey)
                //Recogemos el primer campo de texto
                let message = textArea1.text!
                //Dependiendo del modo (encrypted/decrypted)
                switch mode {
                    //ENCRYPT...
                    case ENCRYPT_LABEL:
                        //Encriptamos con AES...
                        if let encryptedMessage = AESEncryption.encrypt(message: message, key: hashedPrivateKey){
                            textArea2.text = encryptedMessage
                        }
                    //DECRYPT...
                    case DECRYPT_LABEL:
                        //Desencriptamos con AES...
                        if let decryptedMessage = AESEncryption.decrypt(encryptedMessage: message, key: hashedPrivateKey){
                            textArea2.text = decryptedMessage
                        }
                    default:
                        break
                }
            }else {
                self.showError(ERROR_LABEL, ERROR_FIELDS) //Error: campos vacios
            }
        }
    }
    
    //Metodo que muestra un alert al usuario indicandole un error.
    func showError(_ title: String, _ message: String){
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: OK_BUTTON, style: .default))
        present(ac, animated: true)
    }
}
