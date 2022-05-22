package Views;

/**
 * @autor: Alvaro
 */
import GraphicModels.RoundedButton;
import GraphicModels.RoundedPanel;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import Controllers.LoginViewController;
import GraphicModels.RoundedLabel;
import StaticData.Colors;
import StaticData.Fonts;
import StaticData.Texts;
import java.awt.Frame;

public class LoginView extends Frame {

    //Objects needed
    private RoundedPanel panel;
    private RoundedLabel title;
    private RoundedLabel nickNameLabel;
    private JTextField nickNameText;
    private RoundedLabel passLabel;
    private JPasswordField passText;
    private RoundedLabel adviseLabel;
    private RoundedButton showHideButton;
    private RoundedButton openButton;
    private RoundedLabel company;

    //Images
    private Image imageUser50;
    private Image imageKeys50;
    private Image imageOpen70;
    private Image imageLogoApp;

    private LoginViewController controller;

    /**
     * Constructor
     */
    public LoginView() {
        initObjects();
        objectsDesing();
        joinObjects();
        addListenerToObjects();
        setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(650, 750, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.APP_TITLE, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.nickNameLabel = new RoundedLabel(Texts.ENTER_NICKNAME_LOGIN, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.nickNameText = new JTextField(20);
        this.passLabel = new RoundedLabel(Texts.ENTER_MASTERPASS_LOGIN, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.passText = new JPasswordField(20);
        this.adviseLabel = new RoundedLabel(Texts.PASSWORD_ADVISE_LOGIN, 600, 35, 25, Colors.SECONDARY_ORANGE, Colors.BLACK);
        this.showHideButton = new RoundedButton(Texts.SHOWPASS_BUTTON_LOGIN, 250, 40, 25, Colors.PRIMARY_BLUE, Colors.BLACK);
        this.openButton = new RoundedButton(Texts.OPEN_BUTTON, 200, 60, 25, Colors.SECONDARY_ORANGE, Colors.BLACK);
        this.company = new RoundedLabel(Texts.COMPANY_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);

        try {
            this.imageUser50 = ImageIO.read(new File(Texts.IMAGE_USER_50));
            this.imageKeys50 = ImageIO.read(new File(Texts.IMAGE_KEY_50));
            this.imageOpen70 = ImageIO.read(new File(Texts.IMAGE_OPEN_70));
            this.imageLogoApp = ImageIO.read(new File(Texts.IMAGE_LOGO_APP));
        } catch (IOException e) {
        }
        this.controller = new LoginViewController(this);
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.APP_TITLE);
        this.setLayout(null);
        this.setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(710, 840);
        this.setResizable(false);
        this.setIconImage(this.imageLogoApp); //Añadimos un icono (LOGO DE APP)
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 60, 650, 750);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(25, 30, 600, 50);
        //USER INSTRUCTION
        this.nickNameLabel.setFont(Fonts.MY_FONT3);
        this.nickNameLabel.setBounds(150, 120, 350, 50);
        //USER TEXTFIELD
        this.nickNameText.setHorizontalAlignment(JTextField.CENTER);
        this.nickNameText.setFont(Fonts.MY_FONT6);
        this.nickNameText.setFocusable(true);
        this.nickNameText.setBounds(175, 195, 300, 40);
        //PASSWORD INSTRUCTION
        this.passLabel.setFont(Fonts.MY_FONT3);
        this.passLabel.setBounds(150, 275, 350, 50);
        //PASS TEXTFIELD
        this.passText.setHorizontalAlignment(JPasswordField.CENTER);
        this.passText.setFont(Fonts.MY_FONT6);
        this.passText.setFocusable(true);
        this.passText.setBounds(175, 350, 300, 40);
        //PASSWORD ADVISE
        this.adviseLabel.setFont(Fonts.MY_FONT4);
        this.adviseLabel.setBounds(25, 415, 600, 35);
        //BUTTON SHOW/HIDE PASSWORD
        this.showHideButton.setFont(Fonts.MY_FONT3);
        this.showHideButton.setFocusable(false);
        this.showHideButton.setBounds(200, 475, 250, 40);
        //BUTTON OPEN
        this.openButton.setFont(Fonts.MY_FONT1);
        this.openButton.setFocusable(false);
        this.openButton.setBounds(225, 570, 200, 60);
        //COMPANY
        this.company.setFont(Fonts.FONT_COMPANY);
        this.company.setBounds(25, 670, 600, 50);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.nickNameLabel);
        this.panel.add(this.nickNameText);
        this.panel.add(this.passLabel);
        this.panel.add(this.passText);
        this.panel.add(this.adviseLabel);
        this.panel.add(this.openButton);
        this.panel.add(this.showHideButton);
        this.panel.add(this.company);
    }

    private void addListenerToObjects() {

        this.showHideButton.addMouseListener(this.controller);
        this.openButton.addActionListener(this.controller);

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

    public void isPasswordVisible(boolean visible) {
        if (visible) {
            this.passText.setEchoChar(((char) 0)); //Para mostrar...
        } else {
            this.passText.setEchoChar('•');
        }
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.drawImage(this.imageUser50, 525, 250, 50, 50, this);
        g2d.drawImage(this.imageKeys50, 525, 402, 50, 50, this);
        g2d.setStroke(new BasicStroke(2)); //Damos un ancho a la linea.
        g2d.drawLine(70, 600, 640, 600);
        g2d.drawImage(this.imageOpen70, 480, 627, 65, 65, this);
    }

    public void showErrorLogin() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_LOGIN_FAILED, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    public void showErrorFields() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_EMPTY_FIELDS_LOGIN, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    public void showWelcomeUser(String user) {
        JOptionPane.showMessageDialog(this, Texts.WELCOME_USER_MESSAGE + user, Texts.WELCOME_USER_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageOpen70));
    }

    //GETTERS
    public String getNickNameText() {
        return this.nickNameText.getText();
    }

    public String getMasterPassText() {
        return String.valueOf(this.passText.getPassword()); //getText() deprecated -> actual = getPassword()
    }

    public void vaciarCampos() {
        this.nickNameText.setText("");
        this.passText.setText("");
    }

    //Metodo para saludar a un nuevo usuario que acaba de descargarse la app, y no hay contenido en su secret box.
    public void showNewUserMessage() {
        UIManager.put("OptionPane.messageFont", new FontUIResource(Fonts.MY_FONT3)); //Para dar una guente al dialog
        JOptionPane.showMessageDialog(this, Texts.NEW_USER_MESSAGE, Texts.NEW_USER_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageOpen70));
    }
}
