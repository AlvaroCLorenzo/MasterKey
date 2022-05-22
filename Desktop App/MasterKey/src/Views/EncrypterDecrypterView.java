package Views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import Controllers.EncrypterDecrypterController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import StaticData.Colors;
import StaticData.Fonts;
import StaticData.Texts;

/**
 * @autor: Alvaro
 */
public class EncrypterDecrypterView extends JDialog {

    //Objects needed
    private RoundedPanel panel;
    private RoundedLabel title;
    private RoundedLabel privateKeyLabel;
    private JPasswordField privateKeyTextField;
    private RoundedLabel textToBeEncryptLabel;
    private RoundedLabel textEncryptedLabel;
    private TextArea textArea1, textArea2;
    private Panel panelGO;
    private CheckboxGroup checkGroup;
    private Checkbox encryptCheck, decryptCheck;
    private RoundedButton encryptDecryptButton;
    private RoundedLabel company;

    private Image imageLLave50;
    private Image imageEncrypter64;

    private EncrypterDecrypterController controller;

    public EncrypterDecrypterView(YourAcountsView vistaPadre) {
        super(vistaPadre, true); //Para crear cuadro modal que bloquee la ventana anterior hasta que esta se cierre.
        initObjects();
        objectsDesing();
        joinObjects();
        addListenerToObjects();
        this.setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(650, 835, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.ENCRYPTER_DECRYPTER_TITLE, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);

        this.privateKeyLabel = new RoundedLabel(Texts.INSERT_A_PRIVATE_KEY, 400, 40, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.privateKeyTextField = new JPasswordField(20);

        this.textToBeEncryptLabel = new RoundedLabel(Texts.INSERT_TEXT_TOBE_ENCRYPTED_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.textArea1 = new TextArea();

        this.textEncryptedLabel = new RoundedLabel(Texts.YOUR_TEXT__ENCRYPTED_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.textArea2 = new TextArea();

        this.panelGO = new Panel();
        this.checkGroup = new CheckboxGroup();
        this.encryptCheck = new Checkbox(Texts.ENCRYPT_CHECK, this.checkGroup, true);
        this.decryptCheck = new Checkbox(Texts.DECRYPT_CHECK, this.checkGroup, false);

        this.encryptDecryptButton = new RoundedButton(Texts.ENCRYPTDECRYPT_BUTTON, 420, 60, 25, Colors.SECONDARY_LIGHT_GRAY, Colors.BLACK);
        this.company = new RoundedLabel(Texts.COMPANY_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);

        try {
            this.imageLLave50 = ImageIO.read(new File(Texts.IMAGE_KEY_50));
            this.imageEncrypter64 = ImageIO.read(new File(Texts.IMAGE_ENCRYPTER_64));
        } catch (IOException e) {
        }

        this.controller = new EncrypterDecrypterController(this);
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.ENCRYPTER_DECRYPTER_TITLE);
        this.setLayout(null);
        this.getContentPane().setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(710, 930);
        this.setResizable(false);
        this.setIconImage(this.imageEncrypter64); //Añadimos un icono
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Para cerrar la ventana en la X
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 30, 650, 835);
        //TITLE LABEL
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(25, 30, 600, 50);
        //PRIVATE KEY LABEL
        this.privateKeyLabel.setFont(Fonts.MY_FONT2);
        this.privateKeyLabel.setBounds(120, 95, 400, 40);
        //PRIVATE KEY TEXT FIELD
        this.privateKeyTextField.setFont(Fonts.MY_FONT2);
        this.privateKeyTextField.setForeground(Colors.BLACK);
        this.privateKeyTextField.setHorizontalAlignment(JPasswordField.CENTER);
        this.privateKeyTextField.setBounds(148, 150, 350, 50);
        //TEXT TO ENCRYPT LABEL 
        this.textToBeEncryptLabel.setFont(Fonts.MY_FONT2);
        this.textToBeEncryptLabel.setBounds(25, 225, 600, 50);
        //TEXT AREA 1
        this.textArea1.setFont(Fonts.MY_FONT6);
        this.textArea1.setForeground(Colors.BLACK);
        this.textArea1.setBounds(25, 290, 600, 120);
        //TEXT ENCRYPTED LABEL 
        this.textEncryptedLabel.setFont(Fonts.MY_FONT2);
        this.textEncryptedLabel.setBounds(25, 435, 600, 50);
        //TEXT AREA 2
        this.textArea2.setFont(Fonts.MY_FONT6);
        this.textArea2.setForeground(Colors.BLACK);
        this.textArea2.setEditable(false);
        this.textArea2.setBounds(25, 500, 600, 120);
        //PANEL GO
        this.panelGO.setLayout(null);
        this.panelGO.setBackground(Colors.PRIMARY_GRAY);
        this.panelGO.setBounds(0, 610, 650, 60);
        //CHECKBOX ENCRYPT
        this.encryptCheck.setFont(Fonts.MY_FONT2);
        this.encryptCheck.setForeground(Colors.PRIMARY_ORANGE);
        this.encryptCheck.setFocusable(false);
        this.encryptCheck.setBounds(210, 10, 100, 60);
        //CHECKBOX DECRYPT
        this.decryptCheck.setFont(Fonts.MY_FONT2);
        this.decryptCheck.setForeground(Colors.BLACK);
        this.decryptCheck.setFocusable(false);
        this.decryptCheck.setBounds(340, 10, 100, 60);
        //BOTON ENCRYPT / DECRYPT
        this.encryptDecryptButton.setFont(Fonts.MY_FONT1);
        this.encryptDecryptButton.setFocusable(false);
        this.encryptDecryptButton.setBounds(115, 680, 420, 60);
        //COMPANY LABEL
        this.company.setFont(Fonts.FONT_COMPANY);
        this.company.setBounds(25, 760, 600, 50);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.privateKeyLabel);
        this.panel.add(this.privateKeyTextField);
        this.panel.add(this.textToBeEncryptLabel);
        this.panel.add(this.textArea1);
        this.panel.add(this.textEncryptedLabel);
        this.panel.add(this.textArea2);
        this.panelGO.add(this.encryptCheck, BorderLayout.NORTH);
        this.panelGO.add(this.decryptCheck, BorderLayout.NORTH);
        this.panel.add(this.panelGO);
        this.panel.add(this.encryptDecryptButton);
        this.panel.add(this.company);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.drawImage(this.imageLLave50, 550, 210, 50, 50, this);
        g2d.setStroke(new BasicStroke(2)); //Damos un ancho a la linea.
        g2d.drawLine(70, 420, 640, 420);
        g2d.drawImage(this.imageEncrypter64, 600, 740, 62, 62, this);
    }

    private void addListenerToObjects() {
        this.encryptCheck.addItemListener(this.controller);
        this.decryptCheck.addItemListener(this.controller);
        this.encryptDecryptButton.addActionListener(this.controller);
    }

    public static void centrarEnPantalla(Window window) {
        int width, height;
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        width = (pantalla.width / 2) - (window.getSize().width / 2);
        height = (pantalla.height / 2) - (window.getSize().height / 2);

        window.setLocation(width, height);
    }

    public String getPassTextField() {
        return this.privateKeyTextField.getText();
    }

    public String getTextOfTextArea() {
        return this.textArea1.getText();
    }

    public void setTextArea2(String text) {
        this.textArea2.setText(text);
    }

    public Boolean isOnEncryptMode() {
        return this.encryptCheck.getState();
    }

    //Metodo utilizado cuando el usuario hace clic en los checkbox y que cambia de color las labels indicativas
    //true = encrypt / false = decrypt
    public void setColorOnCheckChange(boolean encrypt) {
        if (encrypt) {
            this.encryptCheck.setForeground(Colors.PRIMARY_ORANGE);
            this.decryptCheck.setForeground(Colors.BLACK);
        } else {
            this.encryptCheck.setForeground(Colors.BLACK);
            this.decryptCheck.setForeground(Colors.PRIMARY_ORANGE);
        }
    }

}
