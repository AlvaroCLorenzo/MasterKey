package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.OpenCryptoWalletActivityController;
import com.example.masterkey.R;

public class OpenCryptoWalletActivity extends AppCompatActivity {

    private EditText walletNameEditText, walletTypeEditText, emailAsociatedEditText, passwordEditText, publicKeyEditText, privateKeyEditText;
    private Button editButton, deleteButton;

    private OpenCryptoWalletActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_crypto_wallet);

        this.walletNameEditText = findViewById(R.id.walletNameEditText);
        this.walletTypeEditText = findViewById(R.id.walletTypeEditText);
        this.emailAsociatedEditText = findViewById(R.id.emailAsociatedEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.publicKeyEditText = findViewById(R.id.publicKeyEditText);
        this.privateKeyEditText = findViewById(R.id.privateKeyEditText);
        this.editButton = findViewById(R.id.editButton);
        this.deleteButton = findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        String pass = bundle.getString("pass");
        int position = bundle.getInt("position");

        this.controller = new OpenCryptoWalletActivityController(this, pass, position);
        this.editButton.setOnClickListener(this.controller);
        this.deleteButton.setOnClickListener(this.controller);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Getters
    public String getWalletNameEditText() {
        return walletNameEditText.getText().toString();
    }

    public String getWalletTypeEditText() {
        return walletTypeEditText.getText().toString();
    }

    public String getEmailAsociatedEditText() {
        return emailAsociatedEditText.getText().toString();
    }

    public String getPasswordEditText() {
        return passwordEditText.getText().toString();
    }

    public String getPublicKeyEditText() {
        return publicKeyEditText.getText().toString();
    }

    public String getPrivateKeyEditText() {
        return privateKeyEditText.getText().toString();
    }

    //Setters
    public void setWalletNameEditText(String walletNameEditText) {
        this.walletNameEditText.setText(walletNameEditText);
    }

    public void setWalletTypeEditText(String walletTypeEditText) {
        this.walletTypeEditText.setText(walletTypeEditText);
    }

    public void setEmailAsociatedEditText(String emailAsociatedEditText) {
        this.emailAsociatedEditText.setText(emailAsociatedEditText);
    }

    public void setPasswordEditText(String passwordEditText) {
        this.passwordEditText.setText(passwordEditText);
    }

    public void setPublicKeyEditText(String publicKeyEditText) {
        this.publicKeyEditText.setText(publicKeyEditText);
    }

    public void setPrivateKeyEditText(String privateKeyEditText) {
        this.privateKeyEditText.setText(privateKeyEditText);
    }
}