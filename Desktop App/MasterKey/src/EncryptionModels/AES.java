package EncryptionModels;

import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @autor: Alvaro
 */
public class AES {

    //Contructor
    public AES() {
    }

    //Metodo que retorna la especificación de la clave privada
    //para el algoritmo AES a partir del hash SHA256 de una clave privada humana
    public static SecretKeySpec getSecretKeySpec(String humanPrivateKey) {
        try {
            //Hacemos un hash de la clave humana con SHA-256 para que el cifrado sea más robusto.
            String hashedPrivateKey = SHA256.encrypt(humanPrivateKey);

            //Asignamos una nueva longitud al array de bytes
            byte[] hashedKeyBytesArray = Arrays.copyOf(hashedPrivateKey.getBytes("UTF-8"), 16);

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKeyBytesArray, "AES");

            return secretKeySpec; //Retorna la especificación de la clave secreta para el algoritmo AES.

        } catch (Exception ex) {
            return null;
        }
    }

    //Metodo que retorna una cadena encriptada con el algoritmo AES a partir de clave privada específica.
    public static String encrypt(String message, String humanPrivateKey) {
        try {
            //Recogemos la especificacion de clave privada para nuestro Algoritmo a traves de la password del usuario.
            SecretKeySpec secretKeySpec = AES.getSecretKeySpec(humanPrivateKey);

            //Objeto Cipher, que nos facilita metodos y algoritmos utiles para la encriptacion en AES.
            //con nuestra especificación de clave secreta.
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); //MODO ENCRYPT. 

            byte[] bytes = message.getBytes("UTF-8");
            byte[] encryptedBytes = cipher.doFinal(bytes);  //Ciframos el array de bytes de nuestro mensaje con nuestro objeto Cipher.

            String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes); //Codificamos en Base64.

            return encryptedMessage; //Retornamos la cadena encriptada.

        } catch (Exception e) {
            return null;
        }
    }

    //Metodo que retorna una cadena desencriptada con el algoritmo AES a partir de una cadena encriptada y una clave privada específica.
    public static String decrypt(String encryptedMessage, String humanPrivateKey) {
        try {
            //Recogemos la especificacion de clave privada para nuestro Algoritmo a traves de la password del usuario.
            SecretKeySpec secretKeySpec = AES.getSecretKeySpec(humanPrivateKey);

            ///Objeto Cipher, que nos facilita metodos y algoritmos utiles para la encriptacion en AES.
            //con nuestra especificación de clave secreta.
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec); //DECRIPT MODE

            byte[] bytes = Base64.getDecoder().decode(encryptedMessage);  //Descodificamos en Base64.

            byte[] decryptedBytes = cipher.doFinal(bytes); //Desciframos el array de bytes encriptada con nuestro objeto Cipher.

            String decryptedMessage = new String(decryptedBytes); //Convertimos a String.

            return decryptedMessage; //Retornamos la cadena desencriptada.

        } catch (Exception e) {
            return null;
        }
    }

}
