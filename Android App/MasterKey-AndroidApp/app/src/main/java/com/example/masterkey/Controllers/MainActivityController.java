package com.example.masterkey.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.service.autofill.OnClickAction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.masterkey.R;
import com.example.masterkey.Views.MainActivity;
import com.example.masterkey.Views.YourAccountsActivity;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class MainActivityController implements View.OnClickListener, View.OnTouchListener {

    private MainActivity view;
    private FileManager fileManager;

    public MainActivityController(MainActivity view) {
        this.view = view;
        this.fileManager = FileManager.getInstance();
        this.fileManager.getSecretFileFromDisk(this.view);
        this.fileManager.readEncryptedFile(this.fileManager.getSecretFile());
        //Si no hay un fichero se saludará al nuevo usuario indicandole el uso de la app
        if(this.fileManager.isNewUser()){
            //Creamos un dialog con un mensaje para el nuevo usuario.
            this.createRegisterDialog().show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openButtonLogin:
                this.openLogin();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.view.setMasterPasswordEditTextState(false);
                break;
            case MotionEvent.ACTION_UP:
                this.view.setMasterPasswordEditTextState(true);
                break;
            default:
                break;
        }
        return true;
    }

    //Metodo encargado de recoger las credenciales del usuario y realizar la comprobacion para dar o no paso al la vista de usuario.
    public void openLogin(){
        //Recojo las credenciales introducidas por el usuario.
        String userNickName = this.view.getNickNameEditText();
        String userPassword = this.view.getMasterPasswordEditText();
        //Si los campos no son vacios...
        if (!userNickName.isEmpty() && !userPassword.isEmpty()){
            //Recojo el contenido encriptado del fichero.
            String encryptedContent = this.fileManager.getEncryptedContent();
            //Exite un SecretBox (LOGIN de un Usuario)
            if(encryptedContent!=null){
                //Validamos que el fichero pertenece al usuario que esta usando la app actualmente.
                //Si es correcto...
                if(this.fileManager.validateLogin(userNickName, userPassword)){
                    //Nos desplazamos a la vista de usuario.
                    this.showYourAccountsView(userNickName);
                //Si es incorrecto...
                }else{
                    this.view.showToast(this.view.getString(R.string.error_login_failed)); //Lanzamos mensaje de error de login
                    this.view.resetFields();
                }
            }//No existe un SecretBox (REGISTRO de un Nuevo Usuario).
            else if(this.fileManager.isNewUser()){
                //Generamos un Nuevo Usuario y SecretBox, y escribimos el contenido en el fichero encriptado
                this.fileManager.writeNewUserAndSecretBoxInFile(userNickName, userPassword);
                //Nos desplazamos a la vista de usuario.
                this.showYourAccountsView(userNickName);
            //Login incorrecto...
            }else{
                this.view.showToast(this.view.getString(R.string.error_login_failed)); //Lanzamos mensaje de error de login
            }
        }else{
            this.view.showToast(this.view.getString(R.string.error_empty_fields_login)); //Lanzamos mensaje de error de campos vacios
        }
    }

    //Metodo que desplaza al usuario a la vista Your Accounts.
    private void showYourAccountsView(String nickName){
        Intent intent = new Intent(this.view, YourAccountsActivity.class);
        this.view.startActivity(intent);
        //Mostramos mensaje de bienvenido con el nickname.
        this.view.showToast(this.view.getString(R.string.welcome_user)+nickName); //Lanzamos mensaje de error de campos vacios
    }

    //Metodo que muestra un dialog cuando el usuario utiliza por primera vez la app
    public AlertDialog createRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.view); //Se mostrara en la vista...
        builder.setTitle(this.view.getString(R.string.masterkey_new_user_title)); //Mensaje en el registro de nuevo usuario
        builder.setMessage(this.view.getString(R.string.masterkey_new_user_message));
        builder.setPositiveButton(this.view.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                view.showToast(view.getString(R.string.company_name));
            }
        });
        return builder.create();
    }

}
