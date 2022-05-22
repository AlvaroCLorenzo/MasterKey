package Controllers;

import Views.EncrypterDecrypterView;
import EncryptionModels.AES;
import StaticData.Texts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JOptionPane;

/**
 * @autor: Alvaro
 */
public class EncrypterDecrypterController implements ActionListener, ItemListener {

    private final EncrypterDecrypterView vista;

    public EncrypterDecrypterController(EncrypterDecrypterView view) {
        this.vista = view;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case Texts.ENCRYPTDECRYPT_BUTTON:

                String humanPassword = this.vista.getPassTextField();

                if (!humanPassword.isEmpty()) {
                    //ENCRYPT.
                    if (this.vista.isOnEncryptMode()) {
                        String textToEncrypt = this.vista.getTextOfTextArea();
                        String encryptedText = AES.encrypt(textToEncrypt, humanPassword);
                        this.vista.setTextArea2(encryptedText);

                        //DECRYPT.    
                    } else {
                        String textToDecrypt = this.vista.getTextOfTextArea();
                        String decryptedText = AES.decrypt(textToDecrypt, humanPassword);
                        this.vista.setTextArea2(decryptedText);
                    }
                } else {
                    JOptionPane.showMessageDialog(this.vista, Texts.ERROR_NO_KEY_TO_ENCRYPT, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        switch (String.valueOf(ie.getItem())) {
            case Texts.ENCRYPT_CHECK:
                this.vista.setColorOnCheckChange(true);
                break;
            case Texts.DECRYPT_CHECK:
                this.vista.setColorOnCheckChange(false);
                break;
            default:
                break;
        }
    }

}
