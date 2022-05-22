package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.CreateNewCreditCardActivityController;
import com.example.masterkey.R;

public class CreateNewCreditCardActivity extends AppCompatActivity {

    private EditText bankNameEditText, cardTypeEditText, cardNumberEditText, userNameEditText, endDateEditTex, CCVEditText, PINEditText;
    private Button createButton;

    private CreateNewCreditCardActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_credit_card);

        this.bankNameEditText = findViewById(R.id.bankNameEditText);
        this.cardTypeEditText = findViewById(R.id.cardTypeEditText);
        this.cardNumberEditText = findViewById(R.id.cardNumberEditText);
        this.userNameEditText = findViewById(R.id.userNameEditText);
        this.endDateEditTex = findViewById(R.id.endDateEditText);
        this.CCVEditText = findViewById(R.id.CCVEditText);
        this.PINEditText = findViewById(R.id.PINEditText);
        this.createButton = findViewById(R.id.createButton);

        this.controller = new CreateNewCreditCardActivityController(this);
        this.createButton.setOnClickListener(this.controller);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Getters
    public String getBankNameEditText() {
        return bankNameEditText.getText().toString();
    }

    public String getCardTypeEditText() {
        return cardTypeEditText.getText().toString();
    }

    public String getCardNumberEditText() {
        return cardNumberEditText.getText().toString();
    }

    public String getUserNameEditText() {
        return userNameEditText.getText().toString();
    }

    public String getEndDateEditText() {
        return endDateEditTex.getText().toString();
    }

    public String getCCVEditText() {
        return CCVEditText.getText().toString();
    }

    public String getPINEditText() {
        return PINEditText.getText().toString();
    }
}