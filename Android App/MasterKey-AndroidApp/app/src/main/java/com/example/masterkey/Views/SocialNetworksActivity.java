package com.example.masterkey.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.masterkey.Controllers.SocialNetworksActivityController;
import com.example.masterkey.R;

import java.util.ArrayList;

public class SocialNetworksActivity extends AppCompatActivity {

    private ListView listViewSocialNetworks;
    private Button createNewSocialNetworkButton;

    private SocialNetworksActivityController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_networks);

        this.listViewSocialNetworks = findViewById(R.id.listViewSocialNetworks);
        this.createNewSocialNetworkButton = findViewById(R.id.createNewSocialNetworkButton);

        this.controller = new SocialNetworksActivityController(this);
        this.listViewSocialNetworks.setOnItemClickListener(this.controller);
        this.createNewSocialNetworkButton.setOnClickListener(this.controller);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> socialNetworks = this.controller.listSocialNetworks(bundle.getString("pass"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, socialNetworks);
        this.listViewSocialNetworks.setAdapter(adapter);

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
    public ListView getListViewSocialNetworks() {
        return listViewSocialNetworks;
    }
}