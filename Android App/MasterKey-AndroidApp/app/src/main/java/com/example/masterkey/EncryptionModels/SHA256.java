package com.example.masterkey.EncryptionModels;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    //Atributos de la clase.
    private static final String ALGORITMO = "SHA-256";

    public SHA256() {
    }

    //Metodo estatico que recibe un parámetro (password) sin codificar y devuelve la cadena generada mediante el algoritmo SHA-256.
    public static String encrypt(@NonNull String password) {
        try {
            //Creo una instancia de Message Diggest con el algoritmo SHA256, clase que me permite recoger el algoritmo de codificado y realizar metodos con él.
            MessageDigest md = MessageDigest.getInstance(ALGORITMO);

            //Recojo el resultado del algoritmo en un array de bytes.
            byte[] hash = md.digest(password.getBytes()); //Mando la cadena al algoritmo en forma de bytes.

            //Creo un StringBuffer para recoger la password encriptada usando su funcion append().
            StringBuffer hashedPassword = new StringBuffer();

            //Añado al String cada byte del array de bytes anterior, obteniendo la cadena codificada de nuestra password.
            for (byte b : hash) {
                hashedPassword.append(String.format("%02x", b)); //Añado cada byte con String.format(x) x=hexadecimal
            }

            return hashedPassword.toString(); //Retorno la cadena hash resultante.

            //Si no existiese el algoritmo indicado al crear el Message Diggest, podría ocurrir una excepcion.
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
