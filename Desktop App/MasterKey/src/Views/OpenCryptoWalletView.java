package Views;

import Controllers.OpenCryptoWalletViewController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import Models.CryptoWallet;
import StaticData.Colors;
import StaticData.Fonts;
import StaticData.Texts;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @autor: Alvaro
 */
public class OpenCryptoWalletView extends JDialog {

    //Objects needed
    private RoundedPanel panel;
    private RoundedLabel title;
    private RoundedLabel walletNameLabel;
    private JTextField walletNameText;
    private RoundedLabel walletTypeLabel;
    private JTextField walletTypeText;
    private RoundedLabel emailAsociatedLabel;
    private JTextField emailAsociatedText;
    private RoundedLabel passwordLabel;
    private JTextField passwordText;
    private RoundedLabel publicKeyLabel;
    private JTextField publicKeyText;
    private RoundedLabel privateKeyLabel;
    private JTextField privateKeyText;
    private RoundedButton buttonEdit, buttonDelete;

    private Image imageEdit64, imageDelete64, imageOpen70;

    private CryptoWalletsView vistaPadre;

    private OpenCryptoWalletViewController controller;

    public OpenCryptoWalletView(CryptoWalletsView vistaPadre, CryptoWallet cryptoWallet) {
        super(vistaPadre, true); //Para crear cuadro modal que bloquee la ventana anterior hasta que esta se cierre.
        this.vistaPadre = vistaPadre;
        initObjects();
        objectsDesing();
        joinObjects();
        this.controller = new OpenCryptoWalletViewController(this, cryptoWallet);
        addListenerToObjects();
        this.setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(860, 680, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.YOUR_SECURE_CRYPTO_WALLET_TITLE, 790, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.walletNameLabel = new RoundedLabel(Texts.WALLET_NAME_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.walletNameText = new JTextField(20);
        this.walletTypeLabel = new RoundedLabel(Texts.WALLET_TYPE_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.walletTypeText = new JTextField(20);
        this.emailAsociatedLabel = new RoundedLabel(Texts.EMAIL_ASOCIATED_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.emailAsociatedText = new JTextField(20);
        this.passwordLabel = new RoundedLabel(Texts.PASSWORD_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.passwordText = new JTextField(20);
        this.publicKeyLabel = new RoundedLabel(Texts.PUBLIC_KEY_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.publicKeyText = new JTextField(20);
        this.privateKeyLabel = new RoundedLabel(Texts.PRIVATE_KEY_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.privateKeyText = new JTextField(20);
        this.buttonEdit = new RoundedButton(Texts.EDIT_BUTTON, 285, 80, 25, Colors.SECONDARY_GREEN, Colors.BLACK);
        this.buttonDelete = new RoundedButton(Texts.DELETE_BUTTON, 280, 80, 25, Colors.TERTIARY_GRAY, Colors.WHITE);
        try {
            this.imageOpen70 = ImageIO.read(new File(Texts.IMAGE_OPEN_70));
            this.imageEdit64 = ImageIO.read(new File(Texts.IMAGE_EDIT_64));
            this.imageDelete64 = ImageIO.read(new File(Texts.IMAGE_DELETE_64));
        } catch (IOException e) {
        }
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.YOUR_SECURE_CRYPTO_WALLET_TITLE);
        this.setLayout(null);
        this.getContentPane().setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(930, 780);
        this.setResizable(false);
        this.setIconImage(this.imageOpen70); //Añadimos un icono
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Para cerrar la ventana en la X
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 30, 860, 680);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(30, 30, 790, 50);
        //WALLET NAME LABEL
        this.walletNameLabel.setFont(Fonts.MY_FONT1);
        this.walletNameLabel.setBounds(70, 110, 350, 50);
        //WALLET NAME TEXTFIELD
        this.walletNameText.setHorizontalAlignment(JTextField.CENTER);
        this.walletNameText.setFont(Fonts.MY_FONT5);
        this.walletNameText.setFocusable(true);
        this.walletNameText.setBounds(95, 185, 300, 40);
        //WALLET TYPE LABEL
        this.walletTypeLabel.setFont(Fonts.MY_FONT1);
        this.walletTypeLabel.setBounds(440, 110, 350, 50);
        //WALLET TYPE TEXTFIELD
        this.walletTypeText.setHorizontalAlignment(JTextField.CENTER);
        this.walletTypeText.setFont(Fonts.MY_FONT5);
        this.walletTypeText.setFocusable(true);
        this.walletTypeText.setBounds(465, 185, 300, 40);
        //EMAIL LABEL
        this.emailAsociatedLabel.setFont(Fonts.MY_FONT1);
        this.emailAsociatedLabel.setBounds(70, 255, 350, 50);
        //EMAIL TEXTFIELD
        this.emailAsociatedText.setHorizontalAlignment(JTextField.CENTER);
        this.emailAsociatedText.setFont(Fonts.MY_FONT5);
        this.emailAsociatedText.setFocusable(true);
        this.emailAsociatedText.setBounds(95, 330, 300, 40);
        //PASSWORD LABEL
        this.passwordLabel.setFont(Fonts.MY_FONT1);
        this.passwordLabel.setBounds(440, 255, 350, 50);
        //PASSWORD TEXTFIELD
        this.passwordText.setHorizontalAlignment(JTextField.CENTER);
        this.passwordText.setFont(Fonts.MY_FONT5);
        this.passwordText.setFocusable(true);
        this.passwordText.setBounds(465, 330, 300, 40);
        //PUBLIC KEY LABEL
        this.publicKeyLabel.setFont(Fonts.MY_FONT1);
        this.publicKeyLabel.setBounds(70, 400, 350, 50);
        //PUBLIC KEY TEXTFIELD
        this.publicKeyText.setHorizontalAlignment(JTextField.CENTER);
        this.publicKeyText.setFont(Fonts.MY_FONT5);
        this.publicKeyText.setFocusable(true);
        this.publicKeyText.setBounds(95, 475, 300, 40);
        //PRIVATE KEY LABEL
        this.privateKeyLabel.setFont(Fonts.MY_FONT1);
        this.privateKeyLabel.setBounds(440, 400, 350, 50);
        //PRIVATE KEY TEXTFIELD
        this.privateKeyText.setHorizontalAlignment(JTextField.CENTER);
        this.privateKeyText.setFont(Fonts.MY_FONT5);
        this.privateKeyText.setFocusable(true);
        this.privateKeyText.setBounds(465, 475, 300, 40);
        //BUTTON EDIT
        this.buttonEdit.setFont(Fonts.MY_FONT0);
        this.buttonEdit.setFocusable(false);
        this.buttonEdit.setBounds(70, 570, 285, 80);
        //BUTTON DELETE
        this.buttonDelete.setFont(Fonts.MY_FONT0);
        this.buttonDelete.setFocusable(false);
        this.buttonDelete.setBounds(455, 570, 280, 80);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.walletNameLabel);
        this.panel.add(this.walletNameText);
        this.panel.add(this.walletTypeLabel);
        this.panel.add(this.walletTypeText);
        this.panel.add(this.emailAsociatedLabel);
        this.panel.add(this.emailAsociatedText);
        this.panel.add(this.passwordLabel);
        this.panel.add(this.passwordText);
        this.panel.add(this.publicKeyLabel);
        this.panel.add(this.publicKeyText);
        this.panel.add(this.privateKeyLabel);
        this.panel.add(this.privateKeyText);
        this.panel.add(this.buttonEdit);
        this.panel.add(this.buttonDelete);
    }

    private void addListenerToObjects() {
        this.buttonEdit.addActionListener(this.controller);
        this.buttonDelete.addActionListener(this.controller);
    }

    public static void centrarEnPantalla(Window window) {
        int width, height;
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        width = (pantalla.width / 2) - (window.getSize().width / 2);
        height = (pantalla.height / 2) - (window.getSize().height / 2);

        window.setLocation(width, height);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setStroke(new BasicStroke(2)); //Damos un ancho a la linea.
        g2d.drawLine(70, 605, 850, 605);
        g2d.drawImage(this.imageEdit64, 395, 640, 64, 64, this);
        g2d.drawImage(this.imageDelete64, 780, 640, 64, 64, this);
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO. DEVUELVE LA PASSWORD INTRODUCIDA POR EL USUARIO.
    public String askForMasterPassword() {
        //Usamos JPasswordField para ocultar la password.
        JPasswordField pf = new JPasswordField();
        pf.setFont(Fonts.MY_FONT3);
        pf.setHorizontalAlignment(JPasswordField.CENTER);
        int valor = JOptionPane.showConfirmDialog(this, pf, Texts.SECURITY_STEP_INSERT_MASTERPASS, JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION);

        if (valor == JOptionPane.OK_OPTION) {
            return new String(pf.getPassword()); //Cuando el usuario acepte...retornamos la password introducida...
        } else {
            return null; //Si no...retornamos null.
        }
    }

    //Crypto wallet Edited Succesful
    //Se ve en la vista padre.
    public void showCryptoWalletEdited() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.CRYPTO_WALLET_EDITION_SUCCESS_MESSAGE, Texts.SUCCESFUL_EDITION_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageEdit64));
    }

    //Crypto wallet Deleted Succesful
    //Se ve en la vista padre.
    public void showCryptoWalletDeleted() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.CRYPTO_WALLET_DELETION_SUCCESS_MESSAGE, Texts.SUCCESFUL_DELETION_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageDelete64));
    }

    //Error private key
    public void showErrorIncorrectPrivateKey() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_PRIVATE_KEY, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error empty fields
    public void showErrorFields() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_EMPTY_FIELDS, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Getters y Setters
    public String getWalletNameText() {
        return walletNameText.getText();
    }

    public void setWalletNameText(String walletNameText) {
        this.walletNameText.setText(walletNameText);
    }

    public String getWalletTypeText() {
        return walletTypeText.getText();
    }

    public void setWalletTypeText(String walletTypeText) {
        this.walletTypeText.setText(walletTypeText);
    }

    public String getEmailAsotiatedText() {
        return emailAsociatedText.getText();
    }

    public void setEmailAsotiatedText(String emailAsotiatedText) {
        this.emailAsociatedText.setText(emailAsotiatedText);
    }

    public String getPasswordText() {
        return passwordText.getText();
    }

    public void setPasswordText(String passwordText) {
        this.passwordText.setText(passwordText);
    }

    public String getPublicKeyText() {
        return publicKeyText.getText();
    }

    public void setPublicKeyText(String publicKeyText) {
        this.publicKeyText.setText(publicKeyText);
    }

    public String getPrivateKeyText() {
        return privateKeyText.getText();
    }

    public void setPrivateKeyText(String privateKeyText) {
        this.privateKeyText.setText(privateKeyText);
    }

    public CryptoWalletsView getVistaPadre() {
        return vistaPadre;
    }

}
