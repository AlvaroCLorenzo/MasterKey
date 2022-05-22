package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.masterkey.Controllers.CryptoWalletsActivityController;
import com.example.masterkey.R;

import java.util.ArrayList;

public class CryptoWalletsActivity extends AppCompatActivity {

    private ListView listViewCryptoWallets;
    private Button createNewCryptoWalletButton;

    private CryptoWalletsActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_wallets);

        this.listViewCryptoWallets = findViewById(R.id.listViewCryptoWallets);
        this.createNewCryptoWalletButton = findViewById(R.id.createNewCryptoWalletButton);

        this.controller = new CryptoWalletsActivityController(this);
        this.listViewCryptoWallets.setOnItemClickListener(this.controller);
        this.createNewCryptoWalletButton.setOnClickListener(this.controller);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> cryptoWallets = this.controller.listCryptoWallets(bundle.getString("pass"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cryptoWallets);
        this.listViewCryptoWallets.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, YourAccountsActivity.class); //Para que vuelva a este activity desde cualquier otro.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
        this.finish();
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Getters
    public ListView getListViewCryptoWallets() {
        return listViewCryptoWallets;
    }
}