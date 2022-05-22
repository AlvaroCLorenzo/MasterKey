package com.example.masterkey.EncryptionModels;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    //Atributos de la clase.
    private static final String ALGORITMO = "AES/ECB/PKCS5Padding";

    //Contructor
    public AES(){
    }

    //Returns the Secret Key Specification for AES algorithm from a human Private Key with SHA256
    public static SecretKeySpec getSecretKeySpec(String humanPrivateKey) {
        try {
            //We hash the human key with SHA-256 to make the encryption more robust.
            String hashedPrivateKey = SHA256.encrypt(humanPrivateKey);

            byte[] hashedKeyBytesArray = Arrays.copyOf(hashedPrivateKey.getBytes("UTF-8"), 16); //We assigned a new lenght to the bytes array

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKeyBytesArray, "AES");

            return secretKeySpec; //Returns the secret key specification for AES algorithm.

        } catch (Exception ex) {
            return null;
        }
    }

    //Encrypts a String with a specific private key.
    public static String encrypt(String message, String humanPrivateKey) {
        try {
            SecretKeySpec secretKeySpec = AES.getSecretKeySpec(humanPrivateKey);

            //Cipher init with AES alghorithm on Encrypt mode, with our secret key specification.
            Cipher cipher = Cipher.getInstance(ALGORITMO);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] bytes = message.getBytes("UTF-8");
            byte[] encryptedBytes = cipher.doFinal(bytes); //We cipher the array of bytes of our message.

            String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes); //We encode on Base64.

            return encryptedMessage; //Returns the encrypted String.

        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    //Decrypts a encrypted String with a specific private key.
    public static String decrypt(String encryptedMessage, String humanPrivateKey){
        try {
            SecretKeySpec secretKeySpec = AES.getSecretKeySpec(humanPrivateKey); //Get the secretKeySpec from human key.

            //Cipher init with AES alghorithm on Decrypt mode, with our secret key specification.
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] bytes = Base64.getDecoder().decode(encryptedMessage); //We decode on Base64.

            byte[] decryptedBytes = cipher.doFinal(bytes); //We decipher the encrypted bytes array with our Cipher object.

            String decryptedMessage = new String(decryptedBytes); //Convert to String.

            return decryptedMessage; //Returns the decrypted String.

        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

}
