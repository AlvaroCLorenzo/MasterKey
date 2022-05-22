package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.MainActivityController;
import com.example.masterkey.R;

public class MainActivity extends AppCompatActivity {

    private EditText nickNameEditText, masterPasswordEditText;
    private Button showPasswordButton, openButton;
    private MainActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nickNameEditText = findViewById(R.id.nickNameEditTextLogin);
        this.masterPasswordEditText = findViewById(R.id.masterPassEditTextLogin);
        this.masterPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        this.showPasswordButton = findViewById(R.id.showPasswordButtonLogin);
        this.openButton = findViewById(R.id.openButtonLogin);

        this.controller = new MainActivityController(this);
        this.showPasswordButton.setOnTouchListener(this.controller);
        this.openButton.setOnClickListener(this.controller);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resetFields(); //Borramos los campos al aparecer la vista,por si se vuelve hacia atras.
    }

    //Getters
    public String getNickNameEditText() {
        return nickNameEditText.getText().toString();
    }

    public String getMasterPasswordEditText() {
        return masterPasswordEditText.getEditableText().toString();
    }

    //Setter del campo de la master password para ser visualizado o no...
    public void setMasterPasswordEditTextState(boolean statePassword){
        if(statePassword){
            this.showPasswordButton.setText(getString(R.string.show_password_button_login)); //Seteamos el contenido del boton. (SHOW)
            this.masterPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            this.showPasswordButton.setText(getString(R.string.hide_password_button_login)); //Seteamos el contenido del boton.(HIDE)
            this.masterPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    //Metodo que vacia los campos de introduccion de credenciales.
    public void resetFields() {
        this.nickNameEditText.setText("");
        this.masterPasswordEditText.setText("");
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}