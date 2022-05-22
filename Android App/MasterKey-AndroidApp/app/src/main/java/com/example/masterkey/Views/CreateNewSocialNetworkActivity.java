package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.CreateNewSocialNetworkActivityController;
import com.example.masterkey.Controllers.FileManager;
import com.example.masterkey.R;

public class CreateNewSocialNetworkActivity extends AppCompatActivity {

    private EditText webNameEditText, urlEditText, emailEditText, userNameEditText, passwordEditText;
    private Button createButton;

    private CreateNewSocialNetworkActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_social_network);

        this.webNameEditText = findViewById(R.id.webNameEditText);
        this.urlEditText = findViewById(R.id.urlEditText);
        this.emailEditText = findViewById(R.id.emailEditText);
        this.userNameEditText = findViewById(R.id.userNameEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.createButton = findViewById(R.id.createButton);

        this.controller = new CreateNewSocialNetworkActivityController(this);
        this.createButton.setOnClickListener(this.controller);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Getters
    public String getWebNameEditText() {
        return webNameEditText.getText().toString();
    }

    public String getUrlEditText() {
        return urlEditText.getText().toString();
    }

    public String getEmailEditText() {
        return emailEditText.getText().toString();
    }

    public String getUserNameEditText() {
        return userNameEditText.getText().toString();
    }

    public String getPasswordEditText() {
        return passwordEditText.getText().toString();
    }
}