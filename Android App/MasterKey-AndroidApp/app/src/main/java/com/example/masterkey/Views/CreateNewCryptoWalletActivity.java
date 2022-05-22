package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.CreateNewCryptoWalletActivityController;
import com.example.masterkey.R;

public class CreateNewCryptoWalletActivity extends AppCompatActivity {

    private EditText walletNameEditText, walletTypeEditText, emailAsociatedEditText, passwordEditTex, publicKeyEditText, privateKeyEditText;
    private Button createButton;

    private CreateNewCryptoWalletActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_crypto_wallet);

        this.walletNameEditText = findViewById(R.id.walletNameEditText);
        this.walletTypeEditText = findViewById(R.id.walletTypeEditText);
        this.emailAsociatedEditText = findViewById(R.id.emailAsociatedEditText);
        this.passwordEditTex = findViewById(R.id.passwordEditText);
        this.publicKeyEditText = findViewById(R.id.publicKeyEditText);
        this.privateKeyEditText = findViewById(R.id.privateKeyEditText);
        this.createButton = findViewById(R.id.createButton);

        this.controller = new CreateNewCryptoWalletActivityController(this);
        this.createButton.setOnClickListener(this.controller);
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
        return passwordEditTex.getText().toString();
    }

    public String getPublicKeyEditText() {
        return publicKeyEditText.getText().toString();
    }

    public String getPrivateKeyEditText() {
        return privateKeyEditText.getText().toString();
    }
}