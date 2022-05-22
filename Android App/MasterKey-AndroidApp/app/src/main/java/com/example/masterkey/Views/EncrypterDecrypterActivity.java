package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.masterkey.Controllers.EncrypterDecrypterActivityController;
import com.example.masterkey.R;

public class EncrypterDecrypterActivity extends AppCompatActivity {

    private EditText privateKeyEditText, textArea1, textArea2;
    private Switch encryptDecryptSwitch;
    private Button encryptDecryptButton;

    private EncrypterDecrypterActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypter_decrypter);

        this.privateKeyEditText = findViewById(R.id.privateKeyEditTextEncrypterDecrypter);
        this.textArea1 = findViewById(R.id.inserTyourTextEncryptedMultiLineEncrypterDecrypter);
        this.textArea2 = findViewById(R.id.yourTextEncryptedMultiLineEncrypterDecrypter);
        //Para evitar que se escriba en ese text area, pero que se pueda seleccionar el texto.
        this.textArea2.setKeyListener(null);
        this.textArea2.setTextIsSelectable(true);
        this.encryptDecryptSwitch = findViewById(R.id.switchEncryptDecrypt);
        this.encryptDecryptButton = findViewById(R.id.encryptDecryptButton);

        this.controller = new EncrypterDecrypterActivityController(this);

        this.encryptDecryptSwitch.setOnCheckedChangeListener(this.controller);
        this.encryptDecryptButton.setOnClickListener(this.controller);
    }

    //Metodo para cambiar el texto del switch cuando se chequea.
    public void setSwitchState(boolean checked){
        if(checked){
            this.encryptDecryptSwitch.setText(getString(R.string.encrypt_switch));
        }else{
            this.encryptDecryptSwitch.setText(getString(R.string.decrypt_switch));
        }
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Getters
    public String getPrivateKeyEditText() {
        return privateKeyEditText.getText().toString();
    }

    public String getTextArea1() {
        return textArea1.getText().toString();
    }

    //Setters
    public void setTextArea2(String textArea2) {
        this.textArea2.setText(textArea2);
    }
}