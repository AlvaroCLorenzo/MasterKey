package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.FileManager;
import com.example.masterkey.Controllers.YourAccountsActivityController;
import com.example.masterkey.R;

public class YourAccountsActivity extends AppCompatActivity {

    private Button socialNetworksButton, creditCardsButton, cryptoWalletsButton, encrypterDecrypterButton, myProfileButton;
    private YourAccountsActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_accounts);

        this.socialNetworksButton = findViewById(R.id.socialNetworksButton);
        this.creditCardsButton = findViewById(R.id.creditCardsButton);
        this.cryptoWalletsButton = findViewById(R.id.cryptoWalletsButton);
        this.encrypterDecrypterButton = findViewById(R.id.encrypterDecryperButton);
        this.myProfileButton = findViewById(R.id.myProfileButton);

        this.controller = new YourAccountsActivityController(this);

        this.socialNetworksButton.setOnClickListener(this.controller);
        this.creditCardsButton.setOnClickListener(this.controller);
        this.cryptoWalletsButton.setOnClickListener(this.controller);
        this.encrypterDecrypterButton.setOnClickListener(this.controller);
        this.myProfileButton.setOnClickListener(this.controller);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class); //Para que vuelva a este activity desde cualquier otro.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
        this.finish();
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}