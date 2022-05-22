package com.example.masterkey.Views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.FileManager;
import com.example.masterkey.Controllers.MyProfileActivityController;
import com.example.masterkey.R;

public class MyProfileActivity extends AppCompatActivity {

    private EditText nickNameEditText, masterPasswordEditText;
    private Button updateProfileButton, importBackUpButton, exportBackUpButton;
    private MyProfileActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        this.nickNameEditText = findViewById(R.id.nickNameEditTextMyProfile);
        this.masterPasswordEditText = findViewById(R.id.masterPassEditTextMyProfile);
        this.updateProfileButton = findViewById(R.id.updateProfileButtonMyProfile);
        this.importBackUpButton = findViewById(R.id.importBackUpButtonMyProfile);
        this.exportBackUpButton = findViewById(R.id.exportBackUpButtonMyProfile);

        this.controller = new MyProfileActivityController(this);
        this.updateProfileButton.setOnClickListener(this.controller);
        this.importBackUpButton.setOnClickListener(this.controller);
        this.exportBackUpButton.setOnClickListener(this.controller);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Metodo que devuelve el archivo de backup escogido desde el dispositivo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }
        if ((resultCode == RESULT_OK) && (requestCode == 1)) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
            //Importamos el backup seleccionado por el usuario.
            this.controller.askForMasterPassword(2, uri.getPath().substring(14)); //TYPE 2 = IMPORT BACKUP
        }
    }

    //Getters
    public String getNickNameEditText() {
        return nickNameEditText.getText().toString();
    }

    public String getMasterPasswordEditText() {
        return masterPasswordEditText.getText().toString();
    }
}