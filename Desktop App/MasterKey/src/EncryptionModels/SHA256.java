package EncryptionModels;

import java.security.MessageDigest;

/**
 * @autor: Alvaro
 */
public class SHA256 {

    /*Clase dedicada a retornar hashes a partir de Strings.
    Usa el algotitmo de cifrado hash SHA-256, que pertenece al conjunto de SHA-2 el cual fue diseñado por la NSA.
    Su función es crear una cadena resumen (digest) del texto original (En este caso la password).
    Este valor resultante tiene una longitud fija y no se puede obtener la cadena original, ni su longitud a partir de este.
    Esta función hash está pensada para que sea posible una encriptacion muy consistente,
    y una dificil desencriptacion, por lo que para encriptar las passwords de la app en la BBDD es idonea.
    (Sólo es posible desencriptar las cadenas comparando hashes (DIFICIL TAREA)).
    PARA ELLO USO EL PAQUETE: java.security*/
    public SHA256() {
    }

    //MEtodo que recoge una cadena y a traves del algoritmo SHA-256 retorna su hash.
    public static String encrypt(String message) {
        try {
            //Creo una instancia de Message Diggest, clase que me permite recoger 
            //el algoritmo de codificacion y realizar metodos con él.
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //Recojo el resultado del algoritmo en un array de bytes.
            byte[] hashedBytesArray = md.digest(message.getBytes("UTF-8"));

            //Creo un StringBuffer para recoger la password encriptada usando su funcion append().
            StringBuffer hexString = new StringBuffer();

            //Añado al String cada byte del array de bytes anterior, obteniendo la cadena codificada de nuestra password.
            for (int i = 0; i < hashedBytesArray.length; i++) {
                hexString.append(String.format("%02X", hashedBytesArray[i])); //Añado cada byte con String.format(x) x=hexadecimal
            }

            return hexString.toString(); //Retorna el hash SHA-256 del mensaje.

        } catch (Exception e) {
            return null;
        }
    }
}
