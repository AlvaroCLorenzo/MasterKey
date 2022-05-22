package Controllers;

import EncryptionModels.AES;
import Models.Accounts;
import Models.CreditCard;
import Models.CryptoWallet;
import EncryptionModels.SHA256;
import Models.SecretBox;
import Models.SocialNetwork;
import Models.User;
import StaticData.Texts;
import Views.MyProfileView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @autor: Alvaro
 */
public class FileManager {

    private final String SECRET_FILE_PATH = "./files/secret.masterkey";

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
    public void getSecretFileFromDisk() {

        this.secretFile = new File(this.SECRET_FILE_PATH); //Buscamos el archivo

        //Si no existe..
        if (!this.secretFile.exists()) {
            try {
                //NO EXISTE UN FICHERO, SE CREARÁ.
                this.secretFile.createNewFile(); //Creamos el archivo secreto.
                this.isNewUser = true; //Nuevo usuario...
            } catch (IOException ex) {
            }
        } else {
            //EXISTE UN FICHERO.
            this.isNewUser = false; //Ya existe un usuario (quizas)...

            String content = readEncryptedFile(this.secretFile);
            //Si el archivo tiene contenido...
            if (content != null) {
                this.encryptedContent = content; //Obtenemos el contenido encryptado
                //Si esta vacio, es un nuevo usuario...     
            } else {
                this.isNewUser = true; //No hay contenido en el fichero, no hay Secretbox (nuevo usuario/o backup)
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
                return content; //Retornamos el contenido del fichero.
            }
        } catch (IOException e) {
        }
        return null; //Retornamos null si el fichero no tiene contenido
    }

    //METODO QUE VERIFICA SI EL USUARIO QUE ACCEDE ES EL PROPIETARIO DEL FICHERO ENCRIPTADO...
    public boolean validateLogin(String userNickName, String userPassword) {
        //Recojo el contenido JSON desencriptado del fichero
        //Convierto el JSON desencriptado del usuario guardado en un SecretBox con Gson.
        SecretBox secretBox = this.getMySecretBox(userPassword);
        //Si todo fue bien...
        if (secretBox != null) {
            //Recojo las credenciales del usuario guardado.
            String realNickName = secretBox.getUser().getNickName();
            String realMasterPass = secretBox.getUser().getMasterPassword();

            //Realizamos el hash con SHA256 para comparar las cadenas
            String encryptedName = SHA256.encrypt(userNickName);
            String encryptedPass = SHA256.encrypt(userPassword);

            //Si coinciden los hashes de los nicknames y password, además de haber descifrado el fichero con su clave...retorna true
            //Si algo fue mal en el proceso y no coindicen...Error de login
            return (encryptedName.equals(realNickName) && encryptedPass.equals(realMasterPass));
        } else {
            return false; //Error de login...    
        }
    }

    //METODO QUE PERMITE ESCOGER UN PATH CON UN ARCHIVO .masterkey PARA IMPORTAR UN ARCHIVO DE BACK-UP
    public String getImportBackUpFilePath() {
        //Se crea el JFileChooser. Se le indica que la ventana se abra en el directorio actual                    
        JFileChooser fileChooser = new JFileChooser("/");
        fileChooser.setDialogTitle(Texts.SELECT_YOUR_BACKUP_TO_IMPORT_MESSAGE);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //Se crea el Filtro. El primer parámetro es el mensaje que se muestra,
        //El segundo parámetro es la extensión de los ficheros que se van a mostrar      
        FileFilter filtro = new FileNameExtensionFilter(Texts.MASTER_KEY_TYPE_FILE, Texts.MASTER_KEY_EXTENSION);
        //Se le asigna al JFileChooser el filtro
        fileChooser.setFileFilter(filtro);

        //Se muestra la ventana de eleccion...
        int valor = fileChooser.showOpenDialog(fileChooser);
        //Si el usuario escogió un path...
        if (valor == JFileChooser.APPROVE_OPTION) {
            String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
            //Volvemos a realizar comprobacion de que se trata de un archivo .masterkey
            if (absolutePath.substring(absolutePath.length() - Texts.MASTER_KEY_DOT_EXTENSION.length(), absolutePath.length()).equals(Texts.MASTER_KEY_DOT_EXTENSION)) {
                System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
                return fileChooser.getSelectedFile().getAbsolutePath(); //Retornamos el absolute path  seleccionado
            }
        }
        //Si no escogió un archivo de backup...
        return null; //Retornamos null
    }

    //METODO QUE RECOGE LA MASTER PASSWORD INTRODUCIDA POR EL USUARIO Y VALIDA SI ES LA MISMA QUE LA DEL SECRET BOX.
    public boolean validateMasterPassword(String humanPassword) {
        //La clave privada ha coincidido y hay que convertirlo en SecretBox con GSON.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String mySecretBoxJSON = AES.decrypt(this.encryptedContent, humanPassword);
        //Si no consigue desencriptar es falso...
        if (mySecretBoxJSON != null) {
            SecretBox mySecretBox = gson.fromJson(mySecretBoxJSON, SecretBox.class);
            //Volvemos a realizar otra comprobación y retorna true si coinciden los hashes, false si no coinciden.
            return mySecretBox.getUser().getMasterPassword().equals(SHA256.encrypt(humanPassword));
        } else {
            return false; //Falso si no logró desencriptar en la (primera comprobacion).
        }
    }

    //METODO QUE RECOGE UN PATH DE UN DIRECTORIO ESCOGIDO POR EL USUARIO PARA EXPORTAR EL ARCHIVO DE BACK-UP.
    public String getExportBackUpFilePath() {
        //Se crea el JFileChooser. Se le indica que la ventana se abra en el directorio actual                    
        JFileChooser fileChooser = new JFileChooser("/");
        fileChooser.setDialogTitle(Texts.SELECT_DIRECTORY_TO_EXPORT_MESSAGE);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //El usuario solo puede elegir directorios...
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        //Se muestra la ventana
        int valor = fileChooser.showSaveDialog(fileChooser);
        //Si el usuario eligio una ubicación...
        if (valor == JFileChooser.APPROVE_OPTION) {
            System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
            return fileChooser.getSelectedFile().getAbsolutePath(); //Retornamos el absolute path seleccionado
        }
        //Si no escogió nada...
        return null; //Retornamos null
    }

    //METODO QUE RECOGE UN PATH DE UN ARCHIVO DE BACKUP A IMPORTAR Y LO LEE PARA ASÍ ACTUALIZAR LOS DATOS DEL USUARIO.
    public boolean getImportBackUp(String path, String humanPrivateKey, MyProfileView view) {
        try {
            File file = new File(path);
            //Leo el contenido cifrado del backup y descifro para actualizar redes sociales, credit cards y cryptowalets.
            String encryptedBackupContent = this.readEncryptedFile(file);
            //Si el fichero de backup tiene contenido...
            if (encryptedBackupContent != null) {
                //1. Desencripto el json con la private key. (Pedir private key)
                String encryptedSecretBoxJSON = AES.decrypt(encryptedBackupContent, humanPrivateKey);
                //Si pudo desencriptar y hay contenido...
                if (encryptedSecretBoxJSON != null && !encryptedSecretBoxJSON.isEmpty()) {
                    //3. Si es una instancia de secretbox en JSON...
                    try {
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
                        if (writeFileInit(this.secretFile, this.encryptedContent)) {
                            return true; //Retornamos true, si el proceso se completó con exito...
                        }

                    } catch (JsonSyntaxException ex) {
                        view.showErrorIncorrectBackUpFile();
                    }
                } else {
                    //La clave privada no coindice para desencriptar el fichero de backup.
                    view.showErrorIncorrectPrivateKey();
                }
                //Si el fichero de back up no tiene contenido...
            } else {
                view.showErrorBackupFileEmpty();
            }
        } catch (Exception ex) {
        }

        return false; //Si algo falló... se devuelve false.
    }

    //METODO UTILIZADO PARA ACTUALIZAR LAS CUENTAS DEL USUARIO CUANDO IMPORTA UN BACKUP
    private void updateAccountsInSecretBox(SecretBox mySecretBox, SecretBox importSecretBox) {
        //Recogemos nuestros accounts.
        Accounts myAccounts = mySecretBox.getAccounts();
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
                if (socialNetwork.getWebName().equals(mySocialNetwork.getWebName())
                        && socialNetwork.getUrl().equals(mySocialNetwork.getUrl())
                        && socialNetwork.getEmail().equals(mySocialNetwork.getEmail())
                        && socialNetwork.getUserName().equals(mySocialNetwork.getUserName())
                        && socialNetwork.getPassword().equals(mySocialNetwork.getPassword())) {
                    founded = true; //Repetido, no lo añadimos
                    break;
                }
            }
            if (!founded) {
                socialNetwork.setId(mySocialNetworks.size() + 1); //Cambiamos su ID al ultimo.
                mySocialNetworks.add(socialNetwork); //Si no esta repetida la añadimos.
            }
        }
        //Importamos creditcards
        for (CreditCard creditCard : backUpAccounts.getCreditCards()) {
            boolean founded = false;
            for (CreditCard myCreditCard : myCreditCards) {
                //Nos aseguramos de que no haya repetidas
                if (creditCard.getBankName().equals(myCreditCard.getBankName())
                        && creditCard.getCardType().equals(myCreditCard.getCardType())
                        && creditCard.getCardNumber() == myCreditCard.getCardNumber()
                        && creditCard.getUserName().equals(myCreditCard.getUserName())
                        && creditCard.getCCV() == myCreditCard.getCCV()
                        && creditCard.getPIN() == myCreditCard.getPIN()) {
                    founded = true;  //Repetido, no lo añadimos
                    break;
                }
            }
            if (!founded) {
                creditCard.setId(myCreditCards.size() + 1); //Cambiamos su ID al ultimo.
                myCreditCards.add(creditCard); //Si no esta repetida la añadimos.
            }
        }
        //Importamos cryptowallets
        for (CryptoWallet cryptoWallet : backUpAccounts.getCryptoWallets()) {
            boolean founded = false;
            for (CryptoWallet myCryptoWallet : myCryptoWallets) {
                //Nos aseguramos de que no haya repetidas
                if (cryptoWallet.getWalletName().equals(myCryptoWallet.getWalletName())
                        && cryptoWallet.getWalletType().equals(myCryptoWallet.getWalletType())
                        && cryptoWallet.getEmailAsotiated().equals(myCryptoWallet.getEmailAsotiated())
                        && cryptoWallet.getPassword().equals(myCryptoWallet.getPassword())
                        && cryptoWallet.getPublicKey().equals(myCryptoWallet.getPublicKey())
                        && cryptoWallet.getPrivateKey().equals(myCryptoWallet.getPrivateKey())) {
                    founded = true;  //Repetido, no lo añadimos
                    break;
                }
            }
            if (!founded) {
                cryptoWallet.setId(myCryptoWallets.size() + 1); //Cambiamos su ID al ultimo.
                myCryptoWallets.add(cryptoWallet); //Si no esta repetida la añadimos.
            }
        }
        //Secret box actualizado.
    }

    //METODO QUE ACTUALIZA LAS CREDENCIALES DEL USUARIO EN SU SECRETBOX.
    public boolean updateCredentialsInSecretBox(String nickname, String humanPassword, String oldPassword) {
        //Recojo el Secret Box antes de actualizarse
        SecretBox mySecretBox = this.getMySecretBox(oldPassword); //Con la clave privada actual

        //Actualizo el nickname.
        mySecretBox.getUser().setNickName(SHA256.encrypt(nickname)); //Nuevo nickname
        //Actualizo la master password.
        mySecretBox.getUser().setMasterPassword(SHA256.encrypt(humanPassword)); //Nueva master password

        //Actualizamos el encrypted content(Con la nueva masterPassword)
        this.setMySecretBox(mySecretBox, humanPassword);

        //Actualizamos el contenido encritado en el fichero borrando el contenido anterior.
        if (writeFileInit(this.getSecretFile(), this.encryptedContent)) {
            //Secret box actualizado.
            return true; //Si todo fue bien, devuelve true. 
        }

        return false; //Si algo falló, devuelve false. 
    }

    //METODO ESCRIBIR BORRANDO TODO LO ANTERIOR:
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

    public boolean exportBackUpFile(String path) {
        try {
            //Creamos un archivo de backup, con una fecha orientativa en el nombre
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
            //String timeStamp = Calendar.getInstance().getTime().toString();
            System.out.println(timeStamp);
            timeStamp = "-DATE-" + timeStamp.split(" ")[0] + "-TIME-" + timeStamp.split(" ")[1];
            File backUp = new File(path + "/backup" + timeStamp + Texts.MASTER_KEY_DOT_EXTENSION);
            System.out.println(path + "/backup" + timeStamp + Texts.MASTER_KEY_DOT_EXTENSION);
            if (!backUp.exists()) {
                backUp.createNewFile();
            }
            //Escribimos el contenido encriptado en el backup
            if (this.writeFileInit(backUp, this.encryptedContent)) {
                return true; //Si todo fue bien...retornamos true.
            }
        } catch (IOException ex) {
        }
        return false; //Si algo falló... retornamos false.
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

    //MÉTODO QUE RECOGE LA CLAVE PRIVADA DEL USUARIO Y RETORNA SU SECRET BOX.
    public SecretBox getMySecretBox(String humanPassword) {
        //Recojo el secret box desde el encryptedContent
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String mySecretBoxJSON = AES.decrypt(this.encryptedContent, humanPassword);
        if (mySecretBoxJSON != null && !mySecretBoxJSON.isEmpty()) {
            try {
                SecretBox mySecretBox = gson.fromJson(mySecretBoxJSON, SecretBox.class);
                return mySecretBox; //Retorna el secret box del usuario si todo fue correctamente.
            } catch (JsonSyntaxException ex) {
            }
        }
        return null; //Null si algo falló...
    }

    //MÉTODO QUE RECOGE LA CLAVE PRIVADA DEL USUARIO Y ACTUALIZA EL VALOR DE SU ENCRIPTED CONTENT A TRAVES DE SU SECRET BOX.
    public void setMySecretBox(SecretBox mySecretBox, String humanPassword) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String updatedSecretBoxJSON = gson.toJson(mySecretBox, SecretBox.class);
        this.encryptedContent = AES.encrypt(updatedSecretBoxJSON, humanPassword); //Encryptamos con la masterpassword.
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
