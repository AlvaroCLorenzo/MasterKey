package com.example.masterkey.Controllers;

import android.view.View;
import android.widget.CompoundButton;

import com.example.masterkey.EncryptionModels.AES;
import com.example.masterkey.R;
import com.example.masterkey.Views.EncrypterDecrypterActivity;

public class EncrypterDecrypterActivityController implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EncrypterDecrypterActivity view;
    private boolean encryptMode = true;

    public EncrypterDecrypterActivityController(EncrypterDecrypterActivity view) {
        this.view = view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.encryptDecryptButton:
                String humanPassword = this.view.getPrivateKeyEditText();
                if(!humanPassword.isEmpty()){
                    //ENCRYPT.
                    if (encryptMode) {
                        String textToEncrypt = this.view.getTextArea1();
                        String encryptedText = AES.encrypt(textToEncrypt, humanPassword);
                        this.view.setTextArea2(encryptedText);
                    //DECRYPT.
                    }else{
                        String textToDecrypt = this.view.getTextArea1();
                        String encryptedText = AES.decrypt(textToDecrypt, humanPassword);
                        this.view.setTextArea2(encryptedText);
                    }
                }else{
                    this.view.showToast(this.view.getString(R.string.error_private_key));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            this.encryptMode = true;
            this.view.setSwitchState(true);
        }else{
            this.encryptMode = false;
            this.view.setSwitchState(false);
        }
    }
}
