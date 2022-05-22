package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.masterkey.Controllers.OpenCreditCardActivityController;
import com.example.masterkey.R;

public class OpenCreditCardActivity extends AppCompatActivity {

    private EditText bankNameEditText, cardTypeEditText, cardNumberEditText, userNameEditText, endDateEditText, CCVEditText, PINEditText;
    private Button editButton, deleteButton;

    private OpenCreditCardActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_credit_card);

        this.bankNameEditText = findViewById(R.id.bankNameEditText);
        this.cardTypeEditText = findViewById(R.id.cardTypeEditText);
        this.cardNumberEditText = findViewById(R.id.cardNumberEditText);
        this.userNameEditText = findViewById(R.id.userNameEditText);
        this.endDateEditText = findViewById(R.id.endDateEditText);
        this.CCVEditText = findViewById(R.id.CCVEditText);
        this.PINEditText = findViewById(R.id.PINEditText);
        this.editButton = findViewById(R.id.editButton);
        this.deleteButton = findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        String pass = bundle.getString("pass");
        int position = bundle.getInt("position");

        this.controller = new OpenCreditCardActivityController(this, pass, position);
        this.editButton.setOnClickListener(this.controller);
        this.deleteButton.setOnClickListener(this.controller);
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
        return endDateEditText.getText().toString();
    }

    public String getCCVEditText() {
        return CCVEditText.getText().toString();
    }

    public String getPINEditText() {
        return PINEditText.getText().toString();
    }

    //Setters
    public void setBankNameEditText(String bankNameEditText) {
        this.bankNameEditText.setText(bankNameEditText);
    }

    public void setCardTypeEditText(String cardTypeEditText) {
        this.cardTypeEditText.setText(cardTypeEditText);
    }

    public void setCardNumberEditText(String cardNumberEditText) {
        this.cardNumberEditText.setText(cardNumberEditText);
    }

    public void setUserNameEditText(String userNameEditText) {
        this.userNameEditText.setText(userNameEditText);
    }

    public void setEndDateEditText(String endDateEditText) {
        this.endDateEditText.setText(endDateEditText);
    }

    public void setCCVEditText(String CCVEditText) {
        this.CCVEditText.setText(CCVEditText);
    }

    public void setPINEditText(String PINEditText) {
        this.PINEditText.setText(PINEditText);
    }
}