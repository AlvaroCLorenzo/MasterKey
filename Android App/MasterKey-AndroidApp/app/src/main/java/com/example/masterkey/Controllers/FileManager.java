package com.example.masterkey.Controllers;

import android.content.Context;

import com.example.masterkey.EncryptionModels.AES;
import com.example.masterkey.Models.Accounts;
import com.example.masterkey.Models.CreditCard;
import com.example.masterkey.Models.CryptoWallet;
import com.example.masterkey.EncryptionModels.SHA256;
import com.example.masterkey.Models.SecretBox;
import com.example.masterkey.Models.SocialNetwork;
import com.example.masterkey.Models.User;
import com.example.masterkey.R;
import com.example.masterkey.Views.MyProfileActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {

    private final String FILE_NAME = "secret.masterkey";

    private File secretFile;
    private String encryptedContent;
    private boolean isNewUser;

    private static FileManager fileManager = null;

    //Patron Singleton para el FileManager (Solamente una instancia en toda la app).
    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager; //Retornamos el FileManager (nuevo o existente)
    }

    //Busca el archivo encriptado (masterkey.secret), si no existe, entonces es la primera vez del usuario y lo crea.
    public void getSecretFileFromDisk(Context context) {
        this.secretFile = new File(context.getFilesDir(), FILE_NAME); //Buscamos un archivo .masterkey
        //Si no existe..
        if(!this.secretFile.exists()){
            try{
                this.secretFile.createNewFile(); //Creamos el archivo secreto.
                System.out.println("FILE CREADO: "+this.secretFile.getAbsolutePath());
                this.isNewUser = true; //Nuevo usuario.
                System.out.println("NEW USER");
            } catch (IOException ex) {
                System.out.println("algo fue mal");
                //Algo fue mal...
            }
        }else{
            System.out.println("PATH"+this.secretFile.getAbsolutePath());
            this.isNewUser = false; //Ya existe un usuario...
            //Leemos el fichero para ver si tiene contenido...
            String content = readEncryptedFile(this.secretFile);
            //Si el archivo tiene contenido...
            if (content != null) {
                this.encryptedContent = content; //Obtenemos el contenido encryptado
                System.out.println("EXISTE UN FICHERO, Y TIENE CONTENIDO!");
                //Si esta vacio, es un nuevo usuario...
            } else {
                this.isNewUser = true; //No hay contenido en el fichero, no hay Secretbox (nuevo usuario/o backup)
                System.out.println("EXISTE UN FICHERO, PERO SIN CONTENIDO, NUEVO USUARIO!");
            }
        }
    }

    //We read the encrypted content of the file
    public String readEncryptedFile(File file) {
        try {
            String content = "";
            BufferedReader lector = new BufferedReader(new FileReader(file));
            String linea = lector.readLine();
            while (linea != null) {
                content += linea; //Recogemos el contenido del fichero encriptado.
                linea = lector.readLine();
            }
            //Si no es una cadena vacia...
            if (!content.isEmpty()) {
                System.out.println("CONTENIDO: "+content);
                return content; //Retornamos el contenido del fichero.
            }
        } catch (IOException e) {
        }
        return null; //Retornamos null si el fichero no tiene contenido
    }

    //METODO ESCRIBIR BORRANDO LO ANTERIOR:
    public boolean writeFileInit(File file, String encryptedContent) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            String linea = encryptedContent;
            writer.print(linea);
            writer.close(); //Importante cerrar el escritor.
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //METODO QUE VERIFICA SI EL USUARIO QUE ACCEDE ES EL PROPIETARIO DEL FICHERO ENCRIPTADO...
    public boolean validateLogin(String userNickName, String userPassword) {
        //Recojo el contenido JSON desencriptado del fichero
        //Convierto el JSON desencriptado del usuario guardado en un SecretBox con Gson.
        SecretBox secretBox  = this.getMySecretBox(userPassword);
        //Si fue correctamente..
        if (secretBox != null){
            //Recojo las credenciales del usuario guardado.
            String realNickName = secretBox.getUser().getNickName();
            String realMasterPass = secretBox.getUser().getMasterPassword();

            //Realizamos el hash con SHA256 para comparar las cadenas
            String encryptedName = SHA256.encrypt(userNickName);
            String encryptedPass = SHA256.encrypt(userPassword);

            //Si coinciden los hashes de los nicknames y password, además de haber descifrado el fichero con su clave...retorna true
            //Si algo fue mal en el proceso y no coindicen...Error de login
            return (encryptedName.equals(realNickName) && encryptedPass.equals(realMasterPass));
        }else {
            return false; //Error de login...
        }
    }

    //MÉTODO QUE RECOGE LA CLAVE PRIVADA DEL USUARIO Y RETORNA SU SECRET BOX.
    public SecretBox getMySecretBox(String humanPassword){
        //Recojo el secret box desde el encryptedContent
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String mySecretBoxJSON = AES.decrypt(this.encryptedContent, humanPassword);
        if (mySecretBoxJSON != null && !mySecretBoxJSON.isEmpty()){
            try{
                SecretBox mySecretBox = gson.fromJson(mySecretBoxJSON, SecretBox.class);
                return mySecretBox; //Retorna el secret box del usuario si fue exitoso.
            }catch(JsonSyntaxException ex){
            }
        }
        return null; //Null si algo falló...
    }

    //Genera el contenido  de una nueva SecretBox para un nuevo usuario en el fichero encriptado (REGISTRO).
    public void writeNewUserAndSecretBoxInFile(String userNickName, String userPassword) {
        //Creo un usuario con las credenciales introducidas hasheadas con SHA256.
        User user = new User(SHA256.encrypt(userNickName), SHA256.encrypt(userPassword));
        //Creo su SecretBox.
        SecretBox secretBox = new SecretBox(user);
        //Convierto el SecretBox a un String JSON con Gson ("Con human view")
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String secretBoxJSON = gson.toJson(secretBox, SecretBox.class);
        //Encripto el JSON con AES y la Master password (la password se cifrara en SHA256)
        String encryptedSecretBox = AES.encrypt(secretBoxJSON, userPassword);
        //Escribo el fichero.
        this.writeFileInit(this.getSecretFile(), encryptedSecretBox);
        //Actualizamos el FileManager.
        this.encryptedContent = this.readEncryptedFile(this.getSecretFile());
    }

    //METODO QUE RECOGE LA MASTER PASSWORD INTRODUCIDA POR EL USUARIO Y VALIDA SI ES LA MISMA QUE LA DEL SECRET BOX.
    public boolean validateMasterPassword(String humanPassword){
        //La clave privada ha coincidido y hay que convertirlo en SecretBox con GSON.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String mySecretBoxJSON = AES.decrypt(this.encryptedContent, humanPassword);
        //Si no consigue desencriptar es falso...
        if(mySecretBoxJSON != null) {
            SecretBox mySecretBox = gson.fromJson(mySecretBoxJSON, SecretBox.class);
            //Volvemos a realizar otra comprobación y retorna true si coinciden los hashes, false si no coinciden.
            return mySecretBox.getUser().getMasterPassword().equals(SHA256.encrypt(humanPassword));
        }else{
            return false; //Falso si no logró desencriptar en la (primera comprobacion).
        }
    }

    //METODO QUE ACTUALIZA LAS CREDENCIALES DEL USUARIO EN SU SECRETBOX.
    public boolean updateCredentialsInSecretBox(String nickname, String humanPassword, String oldPassword){
        //Recojo el Secret Box antes de actualizarse
        SecretBox mySecretBox = this.getMySecretBox(oldPassword); //Con la clave privada actual

        //Actualizo el nickname.
        mySecretBox.getUser().setNickName(SHA256.encrypt(nickname)); //Nuevo nickname
        //Actualizo la master password.
        mySecretBox.getUser().setMasterPassword(SHA256.encrypt(humanPassword)); //Nueva master password

        //Actualizamos el encrypted content(Con la nueva masterPassword)
        this.setMySecretBox(mySecretBox, humanPassword);

        //Actualizamos el contenido encritado en el fichero borrando el contenido anterior.
        if(this.writeFileInit(this.getSecretFile(), this.encryptedContent)){
            //Secret box actualizado.
            return true; //Si  fue bien, devuelve true.
        }
        return false; //Si algo falló, devuelve false.
    }

    //MÉTODO QUE RECOGE LA CLAVE PRIVADA DEL USUARIO Y ACTUALIZA EL VALOR DE SU ENCRIPTED CONTENT A TRAVES DE SU SECRET BOX.
    public void setMySecretBox(SecretBox mySecretBox, String humanPassword){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String updatedSecretBoxJSON = gson.toJson(mySecretBox, SecretBox.class);
        this.encryptedContent = AES.encrypt(updatedSecretBoxJSON, humanPassword); //Encryptamos con la masterpassword.
    }

    //METODO QUE RECOGE UN PATH DE UN ARCHIVO DE BACKUP A IMPORTAR Y LO LEE PARA ASÍ ACTUALIZAR LOS DATOS DEL USUARIO.
    public boolean getImportBackUp(String path, String humanPrivateKey, MyProfileActivity view){
        try{
            File file = new File(path);
            //Leo el contenido cifrado del backup y descifro para actualizar redes sociales, credit cards y cryptowalets.
            String encryptedBackupContent = this.readEncryptedFile(file);
            //Si el fichero de backup tiene contenido...
            if(encryptedBackupContent != null) {
                //1. Desencripto el json con la private key. (Pedir private key)
                String encryptedSecretBoxJSON = AES.decrypt(encryptedBackupContent, humanPrivateKey);
                //Si pudo desencriptar y hay contenido...
                if(encryptedSecretBoxJSON != null && !encryptedSecretBoxJSON.isEmpty()) {
                    //3. Si es una instancia de secretbox en JSON...
                    try{
                        //La clave privada ha coincidido y hay que convertirlo en SecretBox con GSON.
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        SecretBox importSecretBox = gson.fromJson(encryptedSecretBoxJSON, SecretBox.class);

                        //4. Añado los accounts del secretbox importado al secretbox actual del usuario.
                        SecretBox mySecretBox = this.getMySecretBox(humanPrivateKey);

                        //Actualizamos el secretbox actual del usuario.
                        this.updateAccountsInSecretBox(mySecretBox, importSecretBox);

                        //Volvemos a convertirlo en JSON
                        //Lo encriptamos con la misma private key
                        this.setMySecretBox(mySecretBox, humanPrivateKey);

                        //Escribo el contenido encritado en el fichero borrando el contenido anterior.
                        if(writeFileInit(this.secretFile, this.encryptedContent)){
                            return true; //Retornamos true, si el proceso se completó con exito...
                        }
                    }catch(JsonSyntaxException ex){
                        //El fichero no es un back-up de Master Key o está dañado.
                        view.showToast(view.getString(R.string.error_incorrect_backup_file));
                    }
                }else{
                    //La clave privada no coindice para desencriptar el fichero de backup.
                    view.showToast(view.getString(R.string.error_private_key));
                }
            //Si el fichero de back up no tiene contenido...
            }else{
                //El fichero de backup no tiene contenido...
                view.showToast(view.getString(R.string.error_empty_backup_file));
            }
        }catch(Exception ex){
        }
        return false; //Si algo falló... se devuelve false.
    }

    //METODO UTILIZADO PARA ACTUALIZAR LAS CUENTAS DEL USUARIO CUANDO IMPORTA UN BACKUP
    public void updateAccountsInSecretBox(SecretBox mySecretBox, SecretBox importSecretBox){
        //Recogemos nuestros accounts.
        Accounts myAccounts =  mySecretBox.getAccounts();
        ArrayList<SocialNetwork> mySocialNetworks = myAccounts.getSocialNetworks();
        ArrayList<CreditCard> myCreditCards = myAccounts.getCreditCards();
        ArrayList<CryptoWallet> myCryptoWallets = myAccounts.getCryptoWallets();

        //Recogemos los accounts importados...
        Accounts backUpAccounts = importSecretBox.getAccounts();
        //Importamos socialnetworks
        for (SocialNetwork socialNetwork : backUpAccounts.getSocialNetworks()) {
            boolean founded = false;
            for (SocialNetwork mySocialNetwork : mySocialNetworks) {
                //Nos aseguramos de que no haya repetidas
                if(socialNetwork.getWebName().equals(mySocialNetwork.getWebName()) &&
                        socialNetwork.getUrl().equals(mySocialNetwork.getUrl()) &&
                        socialNetwork.getEmail().equals(mySocialNetwork.getEmail()) &&
                        socialNetwork.getUserName().equals(mySocialNetwork.getUserName()) &&
                        socialNetwork.getPassword().equals(mySocialNetwork.getPassword())){
                    founded = true; //Repetido
                    break;
                }
            }
            if(!founded){
                socialNetwork.setId(mySocialNetworks.size() + 1); //Cambiamos su ID al ultimo.
                mySocialNetworks.add(socialNetwork); //Si no esta repetida la añadimos.
            }
        }
        //Importamos creditcards
        for (CreditCard creditCard : backUpAccounts.getCreditCards()) {
            boolean founded = false;
            for (CreditCard myCreditCard : myCreditCards) {
                //Nos aseguramos de que no haya repetidas
                if(creditCard.getBankName().equals(myCreditCard.getBankName()) &&
                        creditCard.getCardType().equals(myCreditCard.getCardType()) &&
                        creditCard.getCardNumber() == myCreditCard.getCardNumber() &&
                        creditCard.getUserName().equals(myCreditCard.getUserName()) &&
                        creditCard.getCCV() == myCreditCard.getCCV() &&
                        creditCard.getPIN() == myCreditCard.getPIN()){
                    founded = true;  //Repetido
                    break;
                }
            }
            if(!founded){
                creditCard.setId(myCreditCards.size() + 1); //Cambiamos su ID al ultimo.
                myCreditCards.add(creditCard); //Si no esta repetida la añadimos.
            }
        }
        //Importamos cryptowallets
        for (CryptoWallet cryptoWallet : backUpAccounts.getCryptoWallets()) {
            boolean founded = false;
            for (CryptoWallet myCryptoWallet : myCryptoWallets) {
                //Nos aseguramos de que no haya repetidas
                if(cryptoWallet.getWalletName().equals(myCryptoWallet.getWalletName()) &&
                        cryptoWallet.getWalletType().equals(myCryptoWallet.getWalletType()) &&
                        cryptoWallet.getEmailAsotiated().equals(myCryptoWallet.getEmailAsotiated()) &&
                        cryptoWallet.getPassword().equals(myCryptoWallet.getPassword()) &&
                        cryptoWallet.getPublicKey().equals(myCryptoWallet.getPublicKey()) &&
                        cryptoWallet.getPrivateKey().equals(myCryptoWallet.getPrivateKey())){
                    founded = true;
                    break;
                }
            }
            if(!founded){
                cryptoWallet.setId(myCryptoWallets.size() + 1); //Cambiamos su ID al ultimo.
                myCryptoWallets.add(cryptoWallet); //Si no esta repetida la añadimos.
            }
        }
        //Secret box actualizado.
    }

    //Getters
    public File getSecretFile() {
        return secretFile;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public boolean isNewUser() {
        return isNewUser;
    }
}
