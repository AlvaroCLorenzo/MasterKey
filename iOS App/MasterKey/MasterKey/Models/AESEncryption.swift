//
//  AESCrypt.swift
//  MasterKey
//
//  Created by Wembley on 27/4/22.
//

import Foundation
import CommonCrypto

class AESEncryption{

    //Método de encriptacion simetrica con el algoritmo AES en el que se recoge un mensaje y una clave privada, y retorna el mensaje encriptado.
    static func encrypt(message: String, key: String) -> String? {
        //Creamos un objeto NSData a partir del data de la clave privada (Nos vendrá hasheada a traves de sha256)
        let keyData: NSData! = (key as NSString).data(using: String.Encoding.utf8.rawValue) as NSData?
        
        //Creamos un objeto NSData a partir del data del mensaje a encriptar
        let data: NSData! = (message as NSString).data(using: String.Encoding.utf8.rawValue) as NSData?
         
        //Creamos y configuramos los atributos del objeto Cifrador (key lenght, operation, algoritm, options...)
        let cryptData = NSMutableData(length: Int(data.length) + kCCBlockSizeAES128)!
        
        let keyLength = size_t(kCCKeySizeAES128)
        let operation: CCOperation = UInt32(kCCEncrypt) //Operation: Encrypt
        let algoritm: CCAlgorithm = UInt32(kCCAlgorithmAES128)
        let options: CCOptions = UInt32(kCCOptionECBMode + kCCOptionPKCS7Padding)
            
        var numBytesEncrypted: size_t = 0
            
        let cryptStatus = CCCrypt(operation, algoritm, options, keyData.bytes, keyLength,
                                  nil, data.bytes, data.length, cryptData.mutableBytes,
                                  cryptData.length, &numBytesEncrypted)
                
        //Si se formó correctamente...
        if UInt32(cryptStatus) == UInt32(kCCSuccess) {
            cryptData.length = Int(numBytesEncrypted)
            
            var bytes = [UInt8](repeating: 0, count: cryptData.length)
            cryptData.getBytes(&bytes, length: cryptData.length)
            
            //Retornamos los bytes resultantes en una cadena codificada en base 64
            return Data(bytes).base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
        }
        return nil //Si algo falló, retornamos nil...
    }
    
    //Método de desencriptacion simetrica AES en el que se recoge un mensaje encriptado y una clave privada, y devuelve el mensaje desencriptado
    static func decrypt(encryptedMessage: String, key: String) -> String? {
        //Decodificamos en base64 el mensaje cifrado y obtengo sus bytes(Data)
        if let messageData = Data(base64Encoded: encryptedMessage) {
            
            //Obtenemos un objeto NSData a traves de los bytes del mensaje
            let data: NSData! = messageData as NSData?
            
            //Obtenemos un objeto NSData a traves de los bytes de la private key
            let keyData: NSData! = (key as NSString).data(using: String.Encoding.utf8.rawValue) as NSData?
            
            //Creamos y configuramos los atributos del objeto Cifrador (key lenght, operation, algoritm, options...)
            let cryptData = NSMutableData(length: Int(data.length) + kCCBlockSizeAES128)!
            
            let keyLength = size_t(kCCKeySizeAES128)
            let operation: CCOperation = UInt32(kCCDecrypt) //Operation: Decrypt
            let algoritm: CCAlgorithm = UInt32(kCCAlgorithmAES128)
            let options: CCOptions = UInt32(kCCOptionECBMode + kCCOptionPKCS7Padding)
            
            var numBytesEncrypted: size_t = 0
                
            let cryptStatus = CCCrypt(operation, algoritm, options, keyData.bytes, keyLength,
                                      nil, data.bytes, data.length, cryptData.mutableBytes,
                                      cryptData.length, &numBytesEncrypted)
            
            //Si se formó correctamente...
            if UInt32(cryptStatus) == UInt32(kCCSuccess) {
                cryptData.length = Int(numBytesEncrypted)
                
                var bytes = [UInt8](repeating: 0, count: cryptData.length)
                cryptData.getBytes(&bytes, length: cryptData.length)
                
                //Recogemos los bytes y creamos el string resultante
                if let messageTextPlain = String(bytes: bytes, encoding: .utf8){
                    return messageTextPlain //Devolvemos el mensaje descifrado en texto plano.
                }
            }
        }
        return nil //Si algo falló, retornamos nil...
    }
}
