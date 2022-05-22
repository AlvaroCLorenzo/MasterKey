//
//  SecretFileManager.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import Foundation

struct SecretFileManager {
    
    //Patron Singleton para que solo haya una instancia de SecretFileManager en la app.
    static var shared = SecretFileManager()
    
    var secretBox: SecretBox!
    
    var isNewUser: Bool = true;
    
    private init() {}
    
    //Metodo que obtiene la URL de la localizacion archivo encriptado del usuario
    func getMyFileURL() -> URL? {
        if let docDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
            let fileURL = docDirectory.appendingPathComponent("secret").appendingPathExtension("masterkey")
            return fileURL //Retornamos la URL
        }
        return nil
    }
    
    //Metodo que obtiene el archivo secreto desde el disco y actualiza los datos de la app con los guardados por el usuario en dicho archivo.
    mutating func getSecretBoxFile() {
        //Obtenemos la ruta del archivo secreto del usuario
        let fileURL = getMyFileURL()!
        //Obtenemos el archivo secreto
        let file: FileHandle? = FileHandle(forReadingAtPath: fileURL.path)
    
        //Si el fichero existe...
        if file != nil {
            
            //Fichero encontrado....leemos su contenido
            let data = file!.readDataToEndOfFile()
            file!.closeFile()
            
            //Si al leer el secret box vemos que es un nuevo usuario...(Registro)
            if data.isEmpty {
                self.isNewUser = true //Activamos el valor de nuevo usuario
                
            //Si al leer el secret box vemos que ya existe un usuario guardado...(Login)
            }else{
                self.isNewUser = false //Desactivamos el valor de Nuevo Usuario
            }
            
        //Si el fichero aun no existe, se trata de un (Registro).
        } else {
            self.isNewUser = true //Activamos el valor de Nuevo Usuario
        }
    }
            
    //Metodo que crea un nuevo fichero en una determinada URL proporcionada.
    func createNewfile(fileURL: URL){
        FileManager.default.createFile(atPath: fileURL.path, contents: nil, attributes: nil) //creamos archivo secreto
    }
        
    //Metodo que crea un nuevo perfil de usuario y lo asocia a la secret box que utiliza este objeto Secret File Manager.
    mutating func createANewProfile(){
        let user = User(nickname: "", masterPassword: "") //Creamos usuario
        let acccounts = Accounts() //Creamos objeto accounts.
        let secretBox = SecretBox(user: user, accounts: acccounts)
        self.secretBox = secretBox
    }
        
    //Escribe el contenido de un SecretBox en formato JSON en el fichero.
    func writeSecretBoxInFile(secretBox: SecretBox, atPath: String, privateKey: String) {
        let encoder = JSONEncoder()
        if let jsonPetition = try? encoder.encode(secretBox) {
            
            //Encriptamos el contenido del fichero
            if let encryptedJSON = AESEncryption.encrypt(message: String(data: jsonPetition, encoding: .utf8)!, key: privateKey) { //ENCRIPTAR
                do{
                    //Escribimos el contenido encriptado en el fichero.
                    try encryptedJSON.write(toFile: atPath, atomically: false, encoding: .utf8)
                }catch {}
            }
        }
    }
        
    //Metodo que recibe un fichero, y tras leerlo, recoge su secretbox y devuelve su contenido.
    mutating func readSecretFile(file: FileHandle, privateKey: String) -> NSString {
        //Leemos el contenido del fichero
        let data = file.readDataToEndOfFile()
        file.closeFile()
        
        //Desencriptamos el contenido del fichero
        if let decriptedContent = AESEncryption.decrypt(encryptedMessage: String(data: data, encoding: .utf8)!, key: privateKey) {  //DESENCRIPTAR
            //Lo decodificamos con JSON
            let decoder = JSONDecoder()
            self.secretBox = try! decoder.decode(SecretBox.self, from: Data(decriptedContent.utf8)) //Actualizamos nuestro SecretBox.
        }
        
        let str = NSString(data: data, encoding: String.Encoding.utf8.rawValue)
        
        return str! //Devolvemos el contenido del secret box.
    }
        
    //Metodo que recoge un secret box y actualiza los datos de las cuentas del usuario.
    func updateAccountsInSecretBox(newSecretBox: SecretBox) {
        //Recogemos el secret box y actualizamos los diferentes tipos de cuentas:
        updateNewSocialnetworks(newSecretBox: newSecretBox) //Actualizamos las social networks
        updateNewCreditCards(newSecretBox: newSecretBox) //Actualizamos las credit cards
        updateNewCryptoWallets(newSecretBox: newSecretBox) //Actualizamos las crypto wallets
        
        //SecretBox actualizado...
    }
    
    //Actualizacion de Social Networks
    func updateNewSocialnetworks(newSecretBox: SecretBox){
        var newSocialNetworks = newSecretBox.accounts.socialNetworks
        //Recorremos las social networks
        for newSocialNetwork in newSecretBox.accounts.socialNetworks {
            var founded: Bool = false
            for socialNetwork in SecretFileManager.shared.secretBox.accounts.socialNetworks {
                //Si no coindicen, la añado al array del usuario...
                if (newSocialNetwork.webname == socialNetwork.webname && newSocialNetwork.url == socialNetwork.url && newSocialNetwork.email == socialNetwork.email && newSocialNetwork.username == socialNetwork.username && newSocialNetwork.password == socialNetwork.password) {
                    //Si se encontró...
                    founded = true;
                    break;
                }
            }
            //Si no se encontró...
            if !founded {
                //Seteo su ID
                let index = SecretFileManager.shared.secretBox.accounts.socialNetworks.count
                if index != 0 {
                    newSocialNetworks[newSocialNetwork.id - 1].id = index + 1
                } else {
                    newSocialNetworks[newSocialNetwork.id - 1].id = 1
                }
                //Añado la social network
                SecretFileManager.shared.secretBox.accounts.socialNetworks.append(newSocialNetworks[newSocialNetwork.id - 1])
            }
        }
    }
    
    //Actualizacion de Credit Cards
    func updateNewCreditCards(newSecretBox: SecretBox){
        var newCreditCards = newSecretBox.accounts.creditCards
        //Recorremos las credit cards
        for newCreditCard in  newSecretBox.accounts.creditCards {
            var founded: Bool = false
            for creditCard in SecretFileManager.shared.secretBox.accounts.creditCards {
                //Si no coindicen, la añado al array del usuario...
                if (newCreditCard.nameOfBank == creditCard.nameOfBank && newCreditCard.typeOfCard == creditCard.typeOfCard && newCreditCard.userName == creditCard.userName &&  newCreditCard.numberOfCard == creditCard.numberOfCard && newCreditCard.expirationDate == creditCard.expirationDate && newCreditCard.CCV == creditCard.CCV &&  newCreditCard.PIN == creditCard.PIN) {
                    //Si se encontró...
                    founded = true;
                    break;
                }
            }
            //Si no se encontró...
            if !founded {
                //Seteo su ID
                let index = SecretFileManager.shared.secretBox.accounts.creditCards.count
                if index != 0 {
                    newCreditCards[newCreditCard.id - 1].id = index + 1
                } else {
                    newCreditCards[newCreditCard.id - 1].id = 1
                }
                //Añado la credit card
                SecretFileManager.shared.secretBox.accounts.creditCards.append(newCreditCards[newCreditCard.id - 1])
            }
        }
    }
    
    //Actualizacion de Crypto Wallets
    func updateNewCryptoWallets(newSecretBox: SecretBox){
        var newCryptoWallets = newSecretBox.accounts.cryptoWallets
        //Recorremos las crypto wallets
        for newCryptoWallet in newSecretBox.accounts.cryptoWallets {
            var founded: Bool = false
            for cryptoWallet in SecretFileManager.shared.secretBox.accounts.cryptoWallets {
                //Si no coindicen, la añado al array del usuario...
                if (newCryptoWallet.walletName == cryptoWallet.walletName && newCryptoWallet.walletType == cryptoWallet.walletType && newCryptoWallet.email == cryptoWallet.email &&  newCryptoWallet.password == cryptoWallet.password && newCryptoWallet.publicKey == cryptoWallet.publicKey && newCryptoWallet.privateKey == cryptoWallet.privateKey) {
                    //Si se encontró...
                    founded = true;
                    break;
                }
            }
            //Si no se encontró...
            if !founded {
                //Seteo su ID
                let index = SecretFileManager.shared.secretBox.accounts.cryptoWallets.count
                if index != 0 {
                    newCryptoWallets[newCryptoWallet.id - 1].id = index + 1
                } else {
                    newCryptoWallets[newCryptoWallet.id - 1].id = 1
                }
                //Añado la credit card
                SecretFileManager.shared.secretBox.accounts.cryptoWallets.append(newCryptoWallets[newCryptoWallet.id - 1])
            }
        }
    }
    
}
