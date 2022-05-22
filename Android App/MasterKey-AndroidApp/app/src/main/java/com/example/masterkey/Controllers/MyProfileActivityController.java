package com.example.masterkey.Controllers;

import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.masterkey.R;
import com.example.masterkey.Views.MainActivity;
import com.example.masterkey.Views.MyProfileActivity;

import java.io.File;
import java.time.LocalDateTime;

public class MyProfileActivityController implements View.OnClickListener {

    private MyProfileActivity view;

    private String newNickname, newMasterPassword;

    public MyProfileActivityController(MyProfileActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateProfileButtonMyProfile:
                this.updateProfile();
                break;
            case R.id.importBackUpButtonMyProfile:
                this.importBackUp();
                break;
            case R.id.exportBackUpButtonMyProfile:
                this.askForMasterPassword(3, null); //TYPE 3 = EXPORT BACKUP
                break;
            default:
                break;
        }
    }

    private void updateProfile() {
        //Recojo los campos de actualizacion de perfil...
        this.newNickname = this.view.getNickNameEditText();
        this.newMasterPassword = this.view.getMasterPasswordEditText();
        //Si los campos están completados...
        if(!this.newNickname.isEmpty() && !this.newMasterPassword.isEmpty()){
            //Pido clave privada y realizo la comprobacion para actualizar los datos.
            this.askForMasterPassword(1, null); //TYPE 1 = UPDATE PROFILE
        }else {
            //this.vista.showErrorFields();
            this.view.showToast(this.view.getString(R.string.error_empty_fields_login));
        }
    }

    //METODO DE SEGURIDAD PARA LOS DIFERENTES TIPOS DE ACCIONES.
    //TYPE 1 = UPDATE PROFILE; TYPE 2 = IMPORT BACKUP; TYPE 3 = EXPORT BACKUP.
    public void askForMasterPassword(int type, @Nullable String importPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.view);
        builder.setTitle(this.view.getString(R.string.security_step_title));
        if(type == 1){
            builder.setMessage(this.view.getString(R.string.security_step_introduce_oldpass_message)); //Pido clave privada antigua.
        }else{
            builder.setMessage(R.string.security_step_introduce_pass_message);
        }
        final EditText input = new EditText(this.view);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(input);

        builder.setPositiveButton(this.view.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = input.getText().toString();
                dialog.cancel();
                FileManager fm = FileManager.getInstance();
                //Si fue correcto...
                if(fm.validateMasterPassword(pass)){
                    switch (type){
                        //UPDATE
                        case 1:
                            updatingProfileProcess(pass);
                            break;
                        //IMPORT BACKUP
                        case 2:
                            System.out.println("HA LLEGADO: "+importPath);
                            if(fm.getImportBackUp(importPath, pass, view)){
                                //Import exitoso... volvemos al login, se debe resetear la app.
                                goIntent(MainActivity.class, view.getString(R.string.import_succesful_toast));
                            }
                            break;
                        //EXPORT BACKUP
                        case 3:
                            //Para comprobar si nos dio los permisos de escritura en el dispositivo y pedirlos si no...
                            if(ActivityCompat.checkSelfPermission(view, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                                //Volvemos a pedir los permisos si no los tiene.
                                ActivityCompat.requestPermissions(view, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                            }
                            //Si hay permisos... realizamos el proceso de export...
                            if(ActivityCompat.checkSelfPermission(view, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                //Exportamos el backup.
                                exportBackUpProcess();
                                view.showToast(view.getString(R.string.export_succesful_toast)); //Mensaje de exito
                                goToDownloadsFile(); //Mostramos el directorio downloads
                            }
                            break;
                        default:
                            break;
                    }
                }else{
                    view.showToast(view.getString(R.string.error_private_key)); //Error clave master password.
                }
            }
        });
        builder.setNegativeButton(this.view.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Metodo que traslada al usuario al la carpeta Downloads para que observe su documento de backup exportado.
    private void goToDownloadsFile() {
        Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath().toString());
        chooser.addCategory(Intent.CATEGORY_OPENABLE);
        chooser.setDataAndType(uri, "*/*");
        try {
            view.startActivityForResult(chooser, 1);
        }
        catch (android.content.ActivityNotFoundException ex){
        }
    }


    //METODO QUE REALIZA EL PROCESO DE ACTUALIZAR LAS CREDENCIALES DEL USUARIO UNA VEZ SE A CONFIRMADO EL PASO DE SEGURIDAD.
    private void updatingProfileProcess(String oldPass){
        //Actualizo las credenciales del Secretbox del usuario por las que acaba de introducir.
        if(FileManager.getInstance().updateCredentialsInSecretBox(newNickname, newMasterPassword, oldPass)){
            //Si fue correctamente...
            //Reiniciamos la app. Volvemos al login
            goIntent(MainActivity.class, this.view.getString(R.string.update_profile_succesful_toast)); //Mensaje actualizacion exitosa
        }else{
            view.showToast(this.view.getString(R.string.error_updating)); //Mensaje error actualizando
        }
    }

    //METODO QUE REALIZA EL PROCESO DE IMPORTACION DE UN ARCHIVO ENCRIPTADO DEL SISTEMA DEL USUARIO, UNA VEZ SE A CONFIRMADO EL PASO DE SEGURIDAD.
    private void importBackUp(){
        //Comprobamos si nos dio los permisos de escritura en el dispositivo y los pedimos si no...
        if(ActivityCompat.checkSelfPermission(view, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            //Volvemos a pedir los permisos si no los tiene.
            ActivityCompat.requestPermissions(view, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
        //Si hay permisos... realizamos el proceso de export...
        if(ActivityCompat.checkSelfPermission(view, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //Metodo que acciona al file chooser donde el usuario podra escoger su archivo...
            showFileChooser();
        }
    }

    //METODO QUE DESPLIEGA UNA VISTA DE SELECCION DE ARCHIVOS PARA ESCOGER UN BACKUP.
    private void showFileChooser() {
        //Para llamar el "File Chooser/File picker" Nativo de Android, lo podemos realizar mediante un intent.createChooser
        // y obtener el resultado en onActivityResult
        int VALOR_RETORNO = 1;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        this.view.startActivityForResult(Intent.createChooser(intent, this.view.getString(R.string.choose_file_title)), VALOR_RETORNO);
    }

    //METODO QUE REALIZA EL PROCESO DE EXPORTACION DE UNA COPIA DEL ARCHIVO ENCRIPTADO AL SISTEMA DEL USUARIO, UNA VEZ SE A CONFIRMADO EL PASO DE SEGURIDAD.
    private void exportBackUpProcess(){
        //Creamos un archivo de backup
        try{
            //Lo descargamos en el directorio de Download
            //Al tener la extension .masterkey nos aseguramos que no se pueda abrir de manera facil desde el dispositivo.
            File backUpFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/backup"+LocalDateTime.now()+".masterkey");
            //File backUpFile = new File("/storage/emulated/0/Download/backup"+LocalDateTime.now()+".masterkey");
            if(!backUpFile.exists()){
                backUpFile.createNewFile();
            }
            //Escribitmos el contenido encritpado en el fichero de backup
            FileManager.getInstance().writeFileInit(backUpFile, FileManager.getInstance().getEncryptedContent());
        }catch (Exception ex){
            this.view.showToast(this.view.getString(R.string.error_exporting)); //Error exportando
        }
    }

    public void goIntent(Class activityClass, String message){
        Intent intent = new Intent(this.view, activityClass);
        this.view.startActivity(intent);
        this.view.showToast(message);
    }

}
