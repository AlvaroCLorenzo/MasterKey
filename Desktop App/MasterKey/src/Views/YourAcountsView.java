package Views;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import Controllers.YourAccountsViewController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import StaticData.Colors;
import StaticData.Fonts;
import StaticData.Texts;
import java.awt.Frame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * @autor: Alvaro
 */
public class YourAcountsView extends Frame {

    //Objects needed
    private RoundedPanel panel;

    private RoundedLabel title;
    private RoundedLabel yourAccountsLabel;
    private RoundedButton creditCardsButton;
    private RoundedButton socialMediaButton;
    private RoundedButton cryptoWalletsButton;
    private RoundedButton myProfileButton;
    private RoundedButton encrypterButton;
    private RoundedLabel company;

    private Image imageSocialNetwork;
    private Image imageCreditCard;
    private Image imageCryptoWallet;
    private Image imageEncrypter;
    private Image imageMyProfile;
    private Image imageOpen128;

    private YourAccountsViewController controller;

    /**
     * Constructor
     */
    public YourAcountsView() {
        initObjects();
        objectsDesing();
        joinObjects();
        addListenerToObjects();
        setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(650, 750, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.APP_TITLE, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.yourAccountsLabel = new RoundedLabel(Texts.YOUR_SECURE_ACCOUNTS_TITLE, 400, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.socialMediaButton = new RoundedButton(Texts.SOCIAL_NETWORKS_BUTTON, 350, 50, 50, Colors.SECONDARY_BLUE, Colors.BLACK);
        this.creditCardsButton = new RoundedButton(Texts.CREDIT_CARDS_BUTTON, 350, 50, 50, Colors.SECONDARY_GREEN, Colors.BLACK);
        this.cryptoWalletsButton = new RoundedButton(Texts.CRYPTO_WALLETS_BUTTON, 350, 50, 50, Colors.SECONDARY_ORANGE, Colors.BLACK);
        this.encrypterButton = new RoundedButton(Texts.ENCRYPTER_DECRYPTER_BUTTON, 350, 65, 25, Colors.SECONDARY_DARK_GRAY, Colors.WHITE);
        this.myProfileButton = new RoundedButton(Texts.MY_PROFILE_BUTTON, 350, 60, 25, Colors.SECONDARY_LIGHT_GRAY, Colors.BLACK);
        this.company = new RoundedLabel(Texts.COMPANY_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);

        try {
            this.imageSocialNetwork = ImageIO.read(new File(Texts.IMAGE_SOCIAL_NETWORKS_64));
            this.imageCreditCard = ImageIO.read(new File(Texts.IMAGE_CREDIT_CARD_64));
            this.imageCryptoWallet = ImageIO.read(new File(Texts.IMAGE_CRYPTO_WALLET_70));
            this.imageEncrypter = ImageIO.read(new File(Texts.IMAGE_ENCRYPTER_64));
            this.imageMyProfile = ImageIO.read(new File(Texts.IMAGE_USER_50));
            this.imageOpen128 = ImageIO.read(new File(Texts.IMAGE_OPEN_128));
        } catch (IOException ex) {
        }
        this.controller = new YourAccountsViewController(this);
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.APP_TITLE);
        this.setLayout(null);
        this.setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(710, 840);
        this.setResizable(false);
        this.setIconImage(this.imageOpen128); //Añadimos un icono
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 60, 650, 750);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(25, 30, 600, 50);
        //YOUR ACCOUNTS LABEL
        this.yourAccountsLabel.setFont(Fonts.MY_FONT2);
        this.yourAccountsLabel.setBounds(125, 110, 400, 50);
        //SOCIAL MEDIA BUTTON
        this.socialMediaButton.setFont(Fonts.MY_FONT2);
        this.socialMediaButton.setFocusable(false);
        this.socialMediaButton.setBounds(148, 200, 350, 50);
        //CREDIT CARDS BUTTON
        this.creditCardsButton.setFont(Fonts.MY_FONT2);
        this.creditCardsButton.setFocusable(false);
        this.creditCardsButton.setBounds(148, 280, 350, 50);
        //CRYPTO WALLET BUTTON
        this.cryptoWalletsButton.setFont(Fonts.MY_FONT2);
        this.cryptoWalletsButton.setFocusable(false);
        this.cryptoWalletsButton.setBounds(148, 360, 350, 50);
        //ENCRYPTER/HIDE PASSWORD BUTTON 
        this.encrypterButton.setFont(Fonts.MY_FONT2);
        this.encrypterButton.setFocusable(false);
        this.encrypterButton.setBounds(148, 478, 350, 65);
        //MY PROFILE BUTTON 
        this.myProfileButton.setFont(Fonts.MY_FONT1);
        this.myProfileButton.setFocusable(false);
        this.myProfileButton.setBounds(148, 575, 350, 60);
        //COMPANY
        this.company.setFont(Fonts.FONT_COMPANY);
        this.company.setBounds(25, 670, 600, 50);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.yourAccountsLabel);
        this.panel.add(this.socialMediaButton);
        this.panel.add(this.creditCardsButton);
        this.panel.add(this.cryptoWalletsButton);
        this.panel.add(this.myProfileButton);
        this.panel.add(this.encrypterButton);
        this.panel.add(this.company);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.drawImage(this.imageSocialNetwork, 550, 252, 64, 64, this);
        g2d.drawImage(this.imageCreditCard, 550, 333, 64, 64, this);
        g2d.drawImage(this.imageCryptoWallet, 548, 408, 70, 70, this);
        g2d.setStroke(new BasicStroke(2)); //Damos un ancho a la linea.
        g2d.drawLine(70, 505, 640, 505);
        g2d.drawImage(this.imageEncrypter, 555, 540, 62, 62, this);
        g2d.drawImage(this.imageMyProfile, 555, 640, 50, 50, this);
    }

    private void addListenerToObjects() {
        this.socialMediaButton.addActionListener(this.controller);
        this.creditCardsButton.addActionListener(this.controller);
        this.cryptoWalletsButton.addActionListener(this.controller);
        this.encrypterButton.addActionListener(this.controller);
        this.myProfileButton.addActionListener(this.controller);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
    }

    public static void centrarEnPantalla(Window window) {
        int width, height;
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        width = (pantalla.width / 2) - (window.getSize().width / 2);
        height = (pantalla.height / 2) - (window.getSize().height / 2);

        window.setLocation(width, height);
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
}
