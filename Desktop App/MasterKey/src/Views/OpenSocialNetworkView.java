package Views;

import Controllers.OpenSocialNetworkViewController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import Models.SocialNetwork;
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
public class OpenSocialNetworkView extends JDialog {

    //Objects needed
    private RoundedPanel panel;
    private RoundedLabel title;
    private RoundedLabel webNameLabel;
    private JTextField webNameText;
    private RoundedLabel urlLabel;
    private JTextField urlText;
    private RoundedLabel emailLabel;
    private JTextField emailText;
    private RoundedLabel userNameLabel;
    private JTextField userNameText;
    private RoundedLabel passwordLabel;
    private JTextField passwordText;
    private RoundedButton buttonEdit, buttonDelete;

    private Image imageEdit64, imageDelete64, imageOpen70;

    private SocialNetworksView vistaPadre;

    private OpenSocialNetworkViewController controller;

    public OpenSocialNetworkView(SocialNetworksView vistaPadre, SocialNetwork socialNetwork) {
        super(vistaPadre, true); //Para crear cuadro modal que bloquee la ventana anterior hasta que esta se cierre.
        this.vistaPadre = vistaPadre;
        initObjects();
        objectsDesing();
        joinObjects();
        this.controller = new OpenSocialNetworkViewController(this, socialNetwork);
        addListenerToObjects();
        this.setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(860, 790, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.YOUR_SECURE_SOCIAL_NETWORKS_TITLE, 790, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.webNameLabel = new RoundedLabel(Texts.WEB_NAME_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.webNameText = new JTextField(20);
        this.urlLabel = new RoundedLabel(Texts.URL_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.urlText = new JTextField(20);
        this.emailLabel = new RoundedLabel(Texts.EMAIL_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.emailText = new JTextField(20);
        this.userNameLabel = new RoundedLabel(Texts.USER_NAME_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.userNameText = new JTextField(20);
        this.passwordLabel = new RoundedLabel(Texts.PASSWORD_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.passwordText = new JTextField(20);
        this.buttonEdit = new RoundedButton(Texts.EDIT_BUTTON, 350, 80, 25, Colors.TERTIARY_GREEN, Colors.BLACK);
        this.buttonDelete = new RoundedButton(Texts.DELETE_BUTTON, 350, 80, 25, Colors.TERTIARY_GRAY, Colors.WHITE);
        try {
            this.imageEdit64 = ImageIO.read(new File(Texts.IMAGE_EDIT_64));
            this.imageOpen70 = ImageIO.read(new File(Texts.IMAGE_OPEN_70));
            this.imageDelete64 = ImageIO.read(new File(Texts.IMAGE_DELETE_64));
        } catch (IOException e) {
        }
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.YOUR_SECURE_SOCIAL_NETWORKS_TITLE);
        this.setLayout(null);
        this.getContentPane().setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(930, 890);
        this.setResizable(false);
        this.setIconImage(this.imageOpen70); //Añadimos un icono
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Para cerrar la ventana en la X
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 30, 860, 790);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(30, 30, 790, 50);
        //WEB NAME LABEL
        this.webNameLabel.setFont(Fonts.MY_FONT1);
        this.webNameLabel.setBounds(70, 110, 350, 50);
        //WEB NAME TEXTFIELD
        this.webNameText.setHorizontalAlignment(JTextField.CENTER);
        this.webNameText.setFont(Fonts.MY_FONT5);
        this.webNameText.setFocusable(true);
        this.webNameText.setBounds(95, 185, 300, 40);
        //URL LABEL
        this.urlLabel.setFont(Fonts.MY_FONT1);
        this.urlLabel.setBounds(440, 110, 350, 50);
        //URL TEXTFIELD
        this.urlText.setHorizontalAlignment(JTextField.CENTER);
        this.urlText.setFont(Fonts.MY_FONT5);
        this.urlText.setFocusable(true);
        this.urlText.setBounds(465, 185, 300, 40);
        //EMAIL LABEL
        this.emailLabel.setFont(Fonts.MY_FONT1);
        this.emailLabel.setBounds(70, 255, 350, 50);
        //EMAIL TEXTFIELD
        this.emailText.setHorizontalAlignment(JTextField.CENTER);
        this.emailText.setFont(Fonts.MY_FONT5);
        this.emailText.setFocusable(true);
        this.emailText.setBounds(95, 330, 300, 40);
        //USER NAME LABEL
        this.userNameLabel.setFont(Fonts.MY_FONT1);
        this.userNameLabel.setBounds(440, 255, 350, 50);
        //USER NAME TEXTFIELD
        this.userNameText.setHorizontalAlignment(JTextField.CENTER);
        this.userNameText.setFont(Fonts.MY_FONT5);
        this.userNameText.setFocusable(true);
        this.userNameText.setBounds(465, 330, 300, 40);
        //PASSWORD LABEL
        this.passwordLabel.setFont(Fonts.MY_FONT1);
        this.passwordLabel.setBounds(255, 405, 350, 50);
        //PASSWORD TEXTFIELD
        this.passwordText.setHorizontalAlignment(JTextField.CENTER);
        this.passwordText.setFont(Fonts.MY_FONT5);
        this.passwordText.setFocusable(true);
        this.passwordText.setBounds(280, 480, 300, 40);
        //BUTTON EDIT
        this.buttonEdit.setFont(Fonts.MY_FONT0);
        this.buttonEdit.setFocusable(false);
        this.buttonEdit.setBounds(255, 570, 350, 80);
        //BUTTON DELETE
        this.buttonDelete.setFont(Fonts.MY_FONT0);
        this.buttonDelete.setFocusable(false);
        this.buttonDelete.setBounds(255, 680, 350, 80);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.webNameLabel);
        this.panel.add(this.webNameText);
        this.panel.add(this.urlLabel);
        this.panel.add(this.urlText);
        this.panel.add(this.emailLabel);
        this.panel.add(this.emailText);
        this.panel.add(this.userNameLabel);
        this.panel.add(this.userNameText);
        this.panel.add(this.passwordLabel);
        this.panel.add(this.passwordText);
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
        g2d.drawImage(this.imageEdit64, 660, 640, 64, 64, this);
        g2d.drawImage(this.imageDelete64, 660, 747, 64, 64, this);
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

    //Error private key
    public void showErrorIncorrectPrivateKey() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_PRIVATE_KEY, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error empty fields
    public void showErrorFields() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_EMPTY_FIELDS, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Social Network Edited Succesful
    //Se ve en la vista padre.
    public void showSocialNetworkEdited() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.SOCIAL_NETWORK_SUCCESFUL_EDITION_MESSAGE, Texts.SUCCESFUL_EDITION_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageEdit64));
    }

    //Social Network Deleted Succesful
    //Se ve en la vista padre.
    public void showSocialNetworkDeleted() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.SOCIAL_NETWORK_SUCCESFUL_DELETION_MESSAGE, Texts.SUCCESFUL_DELETION_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageDelete64));
    }

    //Getters y Setters
    public String getWebNameText() {
        return webNameText.getText();
    }

    public void setWebNameText(String webNameText) {
        this.webNameText.setText(webNameText);
    }

    public String getUrlText() {
        return urlText.getText();
    }

    public void setUrlText(String urlText) {
        this.urlText.setText(urlText);
    }

    public String getEmailText() {
        return emailText.getText();
    }

    public void setEmailText(String emailText) {
        this.emailText.setText(emailText);
    }

    public String getUserNameText() {
        return userNameText.getText();
    }

    public void setUserNameText(String userNameText) {
        this.userNameText.setText(userNameText);
    }

    public String getPasswordText() {
        return passwordText.getText();
    }

    public void setPasswordText(String passwordText) {
        this.passwordText.setText(passwordText);
    }

    public SocialNetworksView getVistaPadre() {
        return vistaPadre;
    }

}
