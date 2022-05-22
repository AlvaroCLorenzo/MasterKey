//
//  sha256.swift
//  MasterKey
//
//  Created by Wembley on 27/4/22.
//

import Foundation
import CryptoKit

//Clase dedicada a retornar un hash resultante en SHA256
class sha256 {
    
    //Metodo estatico que recibe una cadena y devuelve su hash a traves del algoritmo SHA256
    static func encrypt(message: String) -> String {
        //Creamos un objeto data a partir del mensaje
        let inputData = Data(message.utf8)
        //Obtenemos el hash sha256 de la cadena
        let hashed = SHA256.hash(data: inputData)
        //Obtenemos el formato de cadena hexadecimal del hash
        let hashedString = hashed.compactMap { String(format: "%02x", $0) }.joined()

        return hashedString //retorna el hash SHA256 resultante de la cadena recibida por parametro
    }
}
