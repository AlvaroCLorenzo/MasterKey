package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.OpenSocialNetworkActivityController;
import com.example.masterkey.R;

public class OpenSocialNetworkActivity extends AppCompatActivity {

    private EditText webNameEditText, urlEditText, emailEditText, userNameEditText, passwordEditText;
    private Button editButton, deleteButton;

    private OpenSocialNetworkActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_social_network);

        this.webNameEditText = findViewById(R.id.webNameEditText);
        this.urlEditText = findViewById(R.id.urlEditText);
        this.emailEditText = findViewById(R.id.emailEditText);
        this.userNameEditText = findViewById(R.id.userNameEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.editButton = findViewById(R.id.editButton);
        this.deleteButton = findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        String pass = bundle.getString("pass");
        int position = bundle.getInt("position");

        this.controller = new OpenSocialNetworkActivityController(this, pass, position);
        this.editButton.setOnClickListener(this.controller);
        this.deleteButton.setOnClickListener(this.controller);
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

    //Setters
    public void setWebNameEditText(String webNameEditText) {
        this.webNameEditText.setText(webNameEditText);
    }

    public void setUrlEditText(String urlEditText) {
        this.urlEditText.setText(urlEditText);
    }

    public void setEmailEditText(String emailEditText) {
        this.emailEditText.setText(emailEditText);
    }

    public void setUserNameEditText(String userNameEditText) {
        this.userNameEditText.setText(userNameEditText);
    }

    public void setPasswordEditText(String passwordEditText) {
        this.passwordEditText.setText(passwordEditText);
    }
}