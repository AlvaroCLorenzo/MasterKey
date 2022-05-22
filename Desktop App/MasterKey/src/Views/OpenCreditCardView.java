package Views;

import Controllers.OpenCreditCardViewController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import Models.CreditCard;
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
public class OpenCreditCardView extends JDialog {

    //Objects needed
    private RoundedPanel panel;
    private RoundedLabel title;
    private RoundedLabel bankNameLabel;
    private JTextField bankNameText;
    private RoundedLabel cardTypeLabel;
    private JTextField cardTypeText;
    private RoundedLabel cardNumberLabel;
    private JTextField cardNumberText;
    private RoundedLabel userNameLabel;
    private JTextField userNameText;
    private RoundedLabel endDateLabel;
    private JTextField endDateText;
    private RoundedLabel CCVLabel;
    private JTextField CCVText;
    private RoundedLabel PINLabel;
    private JTextField PINText;
    private RoundedButton buttonEdit, buttonDelete;

    private Image imageEdit64, imageDelete64, imageOpen70;

    private CreditCardsView vistaPadre;

    private OpenCreditCardViewController controller;

    public OpenCreditCardView(CreditCardsView vistaPadre, CreditCard creditCard) {
        super(vistaPadre, true); //Para crear cuadro modal que bloquee la ventana anterior hasta que esta se cierre.
        this.vistaPadre = vistaPadre;
        initObjects();
        objectsDesing();
        joinObjects();
        this.controller = new OpenCreditCardViewController(this, creditCard);
        addListenerToObjects();
        this.setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(860, 835, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.YOUR_SECURE_CREDIT_CARD_TITLE, 790, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.bankNameLabel = new RoundedLabel(Texts.BANK_NAME_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.bankNameText = new JTextField(20);
        this.cardTypeLabel = new RoundedLabel(Texts.CARD_TYPE_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.cardTypeText = new JTextField(20);
        this.cardNumberLabel = new RoundedLabel(Texts.CARD_NUMBER_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.cardNumberText = new JTextField(20);
        this.userNameLabel = new RoundedLabel(Texts.USER_NAME_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.userNameText = new JTextField(20);
        this.endDateLabel = new RoundedLabel(Texts.END_DATE_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.endDateText = new JTextField(20);
        this.CCVLabel = new RoundedLabel(Texts.CCV_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.CCVText = new JTextField(20);
        this.PINLabel = new RoundedLabel(Texts.PIN_LABEL, 350, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.PINText = new JTextField(20);
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
        this.setTitle(Texts.YOUR_SECURE_CREDIT_CARD_TITLE);
        this.setLayout(null);
        this.getContentPane().setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(930, 930);
        this.setResizable(false);
        this.setIconImage(this.imageOpen70); //Añadimos un icono
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Para cerrar la ventana en la X
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 30, 860, 835);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(30, 30, 790, 50);
        //BANK NAME LABEL
        this.bankNameLabel.setFont(Fonts.MY_FONT1);
        this.bankNameLabel.setBounds(70, 110, 350, 50);
        //BANK NAME TEXTFIELD
        this.bankNameText.setHorizontalAlignment(JTextField.CENTER);
        this.bankNameText.setFont(Fonts.MY_FONT5);
        this.bankNameText.setFocusable(true);
        this.bankNameText.setBounds(95, 185, 300, 40);
        //CARD TYPE LABEL
        this.cardTypeLabel.setFont(Fonts.MY_FONT1);
        this.cardTypeLabel.setBounds(440, 110, 350, 50);
        //CARD TYPE TEXTFIELD
        this.cardTypeText.setHorizontalAlignment(JTextField.CENTER);
        this.cardTypeText.setFont(Fonts.MY_FONT5);
        this.cardTypeText.setFocusable(true);
        this.cardTypeText.setBounds(465, 185, 300, 40);
        //CARD NUMBER LABEL
        this.cardNumberLabel.setFont(Fonts.MY_FONT1);
        this.cardNumberLabel.setBounds(70, 255, 350, 50);
        //CARD NUMBER TEXTFIELD
        this.cardNumberText.setHorizontalAlignment(JTextField.CENTER);
        this.cardNumberText.setFont(Fonts.MY_FONT5);
        this.cardNumberText.setFocusable(true);
        this.cardNumberText.setBounds(95, 330, 300, 40);
        //USER NAME LABEL
        this.userNameLabel.setFont(Fonts.MY_FONT1);
        this.userNameLabel.setBounds(440, 255, 350, 50);
        //USER NAME TEXTFIELD
        this.userNameText.setHorizontalAlignment(JTextField.CENTER);
        this.userNameText.setFont(Fonts.MY_FONT5);
        this.userNameText.setFocusable(true);
        this.userNameText.setBounds(465, 330, 300, 40);
        //END DATE  LABEL
        this.endDateLabel.setFont(Fonts.MY_FONT1);
        this.endDateLabel.setBounds(70, 400, 350, 50);
        //END DATE TEXTFIELD
        this.endDateText.setHorizontalAlignment(JTextField.CENTER);
        this.endDateText.setFont(Fonts.MY_FONT5);
        this.endDateText.setFocusable(true);
        this.endDateText.setBounds(95, 475, 300, 40);
        //CCV LABEL
        this.CCVLabel.setFont(Fonts.MY_FONT1);
        this.CCVLabel.setBounds(440, 400, 350, 50);
        //CCV TEXTFIELD
        this.CCVText.setHorizontalAlignment(JTextField.CENTER);
        this.CCVText.setFont(Fonts.MY_FONT5);
        this.CCVText.setFocusable(true);
        this.CCVText.setBounds(465, 475, 300, 40);
        //PIN LABEL
        this.PINLabel.setFont(Fonts.MY_FONT1);
        this.PINLabel.setBounds(255, 550, 350, 50);
        //PIN TEXTFIELD
        this.PINText.setHorizontalAlignment(JTextField.CENTER);
        this.PINText.setFont(Fonts.MY_FONT5);
        this.PINText.setFocusable(true);
        this.PINText.setBounds(280, 625, 300, 40);
        //BUTTON EDIT
        this.buttonEdit.setFont(Fonts.MY_FONT0);
        this.buttonEdit.setFocusable(false);
        this.buttonEdit.setBounds(70, 720, 285, 80);
        //BUTTON DELETE
        this.buttonDelete.setFont(Fonts.MY_FONT0);
        this.buttonDelete.setFocusable(false);
        this.buttonDelete.setBounds(455, 720, 280, 80);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.bankNameLabel);
        this.panel.add(this.bankNameText);
        this.panel.add(this.cardTypeLabel);
        this.panel.add(this.cardTypeText);
        this.panel.add(this.cardNumberLabel);
        this.panel.add(this.cardNumberText);
        this.panel.add(this.userNameLabel);
        this.panel.add(this.userNameText);
        this.panel.add(this.endDateLabel);
        this.panel.add(this.endDateText);
        this.panel.add(this.CCVLabel);
        this.panel.add(this.CCVText);
        this.panel.add(this.PINLabel);
        this.panel.add(this.PINText);
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
        g2d.drawLine(70, 750, 850, 750);
        g2d.drawImage(this.imageEdit64, 395, 790, 64, 64, this);
        g2d.drawImage(this.imageDelete64, 780, 790, 64, 64, this);
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

    //Error numeric 
    public void showNumericFieldError() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_NUMERICAL_FIELD, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error empty fields
    public void showErrorFields() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_EMPTY_FIELDS, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Credit Card Edited Succesful
    //Se ve en la vista padre.
    public void showCreditCardEdited() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.CREDIT_CARD_SUCCESFUL_EDITION_MESSAGE, Texts.SUCCESFUL_EDITION_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageEdit64));
    }

    //Credit Card Deleted Succesful
    //Se ve en la vista padre.
    public void showCreditCardDeleted() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.CREDIT_CARD_SUCCESFUL_DELETION_MESSAGE, Texts.SUCCESFUL_DELETION_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageDelete64));
    }

    //Getters y Setters
    public String getBankNameText() {
        return bankNameText.getText();
    }

    public void setBankNameText(String bankNameText) {
        this.bankNameText.setText(bankNameText);
    }

    public String getCardTypeText() {
        return cardTypeText.getText();
    }

    public void setCardTypeText(String cardTypeText) {
        this.cardTypeText.setText(cardTypeText);
    }

    public String getCardNumberText() {
        return cardNumberText.getText();
    }

    public void setCardNumberText(String cardNumberText) {
        this.cardNumberText.setText(cardNumberText);
    }

    public String getUserNameText() {
        return userNameText.getText();
    }

    public void setUserNameText(String userNameText) {
        this.userNameText.setText(userNameText);
    }

    public String getEndDateText() {
        return endDateText.getText();
    }

    public void setEndDateText(String endDateText) {
        this.endDateText.setText(endDateText);
    }

    public String getCCVText() {
        return CCVText.getText();
    }

    public void setCCVText(String CCVText) {
        this.CCVText.setText(CCVText);
    }

    public String getPINText() {
        return PINText.getText();
    }

    public void setPINText(String PINText) {
        this.PINText.setText(PINText);
    }

    public CreditCardsView getVistaPadre() {
        return vistaPadre;
    }

}
