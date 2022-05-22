package Views;

import Controllers.CreditCardsViewController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import StaticData.Colors;
import StaticData.Fonts;
import StaticData.Texts;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * @autor: Alvaro
 */
public class CreditCardsView extends JDialog {

    //Objects needed
    private RoundedPanel panel;
    private RoundedLabel title;
    private RoundedButton buttonOpen, buttonCreateNew;
    private List creditCardsList;

    private Image imageCreditCard, imageOpen70, imageCreateNew;

    private CreditCardsViewController controller;

    public CreditCardsView(YourAcountsView vistaPadre, String humanPrivateKey) {
        super(vistaPadre, true); //Para crear cuadro modal que bloquee la ventana anterior hasta que esta se cierre.
        initObjects();
        objectsDesing();
        joinObjects();
        this.controller = new CreditCardsViewController(this, humanPrivateKey);
        addListenerToObjects();
        this.setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(650, 835, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.CREDIT_CARDS_TITLE, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.creditCardsList = new List();
        this.buttonOpen = new RoundedButton(Texts.OPEN_BUTTON, 350, 80, 25, Colors.SECONDARY_ORANGE, Colors.BLACK);
        this.buttonCreateNew = new RoundedButton(Texts.CREATE_NEW_BUTTON, 350, 80, 25, Colors.TERTIARY_GREEN, Colors.BLACK);
        try {
            this.imageCreditCard = ImageIO.read(new File(Texts.IMAGE_CREDIT_CARD_64));
            this.imageOpen70 = ImageIO.read(new File(Texts.IMAGE_OPEN_70));
            this.imageCreateNew = ImageIO.read(new File(Texts.IMAGE_CREATE_NEW_64));
        } catch (IOException e) {
        }
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.CREDIT_CARDS_TITLE);
        this.setLayout(null);
        this.getContentPane().setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(710, 930);
        this.setResizable(false);
        this.setIconImage(this.imageCreditCard); //Añadimos un icono
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Para cerrar la ventana en la X
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 30, 650, 835);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(25, 30, 600, 50);
        //LIST
        this.creditCardsList.setBounds(30, 200, 590, 300);
        this.creditCardsList.setFont(Fonts.MY_FONT5);
        //OPEN BUTTON
        this.buttonOpen.setFont(Fonts.MY_FONT1);
        this.buttonOpen.setFocusable(false);
        this.buttonOpen.setBounds(148, 580, 350, 80);
        //CREATE NEW BUTTON
        this.buttonCreateNew.setFont(Fonts.MY_FONT1);
        this.buttonCreateNew.setFocusable(false);
        this.buttonCreateNew.setBounds(148, 700, 350, 80);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.creditCardsList);
        this.panel.add(this.buttonOpen);
        this.panel.add(this.buttonCreateNew);
    }

    private void addListenerToObjects() {
        this.buttonOpen.addActionListener(this.controller);
        this.buttonCreateNew.addActionListener(this.controller);
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
        g2d.drawImage(this.imageCreditCard, 323, 170, 64, 64, this);
        g2d.setStroke(new BasicStroke(2)); //Damos un ancho a la linea.
        g2d.drawLine(70, 600, 640, 600);
        g2d.drawImage(this.imageOpen70, 555, 650, 64, 64, this);
        g2d.drawImage(this.imageCreateNew, 560, 770, 64, 64, this);
    }

    //Metodo utilizado para refrescar la informacion de la lista de credit cards.
    public void refreshList(String humanPrivateKey) {
        this.controller.getCreditCards(humanPrivateKey);
        this.controller.listCreditCards();
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

    //Error no Credit Cards
    public void showErrorNoCreditCards() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_NO_CREDIT_CARDS, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    public List getCreditCardList() {
        return creditCardsList;
    }

}
