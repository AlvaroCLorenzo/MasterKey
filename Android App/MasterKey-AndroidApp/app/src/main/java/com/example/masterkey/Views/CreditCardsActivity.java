package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.masterkey.Controllers.CreditCardsActivityController;
import com.example.masterkey.R;

import java.util.ArrayList;

public class CreditCardsActivity extends AppCompatActivity {

    private ListView listViewCreditCards;
    private Button createNewCreditCardButton;

    private CreditCardsActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_cards);

        this.listViewCreditCards = findViewById(R.id.listViewCreditCards);
        this.createNewCreditCardButton = findViewById(R.id.createNewCreditCardButton);

        this.controller = new CreditCardsActivityController(this);
        this.listViewCreditCards.setOnItemClickListener(this.controller);
        this.createNewCreditCardButton.setOnClickListener(this.controller);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> creditCards = this.controller.listCreditCards(bundle.getString("pass"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, creditCards);
        this.listViewCreditCards.setAdapter(adapter);
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
    public ListView getListViewCreditCards() {
        return listViewCreditCards;
    }
}