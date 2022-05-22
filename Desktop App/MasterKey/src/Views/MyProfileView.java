package Views;

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
import Controllers.MyProfileViewController;
import GraphicModels.RoundedButton;
import GraphicModels.RoundedLabel;
import GraphicModels.RoundedPanel;
import StaticData.Colors;
import StaticData.Fonts;
import StaticData.Texts;

/**
 * @autor: Alvaro
 */
public class MyProfileView extends JDialog {

    //Objects needed
    private RoundedPanel panel;

    private RoundedLabel title;
    private RoundedLabel privateKeyLabel, nicknameLabel;
    private JTextField nicknameTextField;
    private JPasswordField privateKeyTextField;
    private RoundedButton buttonUpdateProfile;
    private RoundedLabel importBackupLabel;
    private RoundedLabel exportBackupLabel;

    private RoundedButton buttonImport, buttonExport;
    private RoundedLabel company;

    private Image imageLLave50;
    private Image imageImportBackup64;
    private Image imageExportBackup64;
    private Image imageUser50;

    private MyProfileViewController controller;

    private final YourAcountsView vistaPadre;

    public MyProfileView(YourAcountsView vistaPadre) {
        super(vistaPadre, true); //Para crear cuadro modal que bloquee la ventana anterior hasta que esta se cierre.
        this.vistaPadre = vistaPadre;
        initObjects();
        objectsDesing();
        joinObjects();
        addListenerToObjects();
        this.setVisible(true);
    }

    private void initObjects() {
        this.panel = new RoundedPanel(650, 835, 25, Colors.PRIMARY_GRAY);
        this.title = new RoundedLabel(Texts.MY_PROFILE_TITLE, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);

        this.nicknameLabel = new RoundedLabel(Texts.EDIT_YOUR_NICKNAME_MYPROFILE, 400, 40, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.nicknameTextField = new JTextField(20);

        this.privateKeyLabel = new RoundedLabel(Texts.EDIT_YOUR_MASTERPASS_MYPROFILE, 400, 40, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.privateKeyTextField = new JPasswordField(20);

        this.buttonUpdateProfile = new RoundedButton(Texts.UPDATE_PROFILE_BUTTON, 350, 50, 25, Colors.SECONDARY_ORANGE, Colors.BLACK);

        this.importBackupLabel = new RoundedLabel(Texts.IMPORT_YOUR_BACKUP_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.buttonImport = new RoundedButton(Texts.IMPORT_BACKUP_BUTTON, 350, 80, 25, Colors.SECONDARY_PINK, Colors.BLACK);

        this.exportBackupLabel = new RoundedLabel(Texts.EXPORT_YOUR_BACKUP_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);
        this.buttonExport = new RoundedButton(Texts.EXPORT_BACKUP_BUTTON, 350, 80, 25, Colors.PRIMARY_BLUE, Colors.BLACK);

        this.company = new RoundedLabel(Texts.COMPANY_LABEL, 600, 50, 25, Colors.PRIMARY_GREEN, Colors.BLACK);

        try {
            this.imageUser50 = ImageIO.read(new File(Texts.IMAGE_USER_50));
            this.imageLLave50 = ImageIO.read(new File(Texts.IMAGE_KEY_50));
            this.imageImportBackup64 = ImageIO.read(new File(Texts.IMAGE_IMPORT_BACKUP_64));
            this.imageExportBackup64 = ImageIO.read(new File(Texts.IMAGE_EXPORT_BACKUP_64));
        } catch (IOException e) {
        }
        this.controller = new MyProfileViewController(this);
    }

    private void objectsDesing() {
        //FRAME
        this.setTitle(Texts.MY_PROFILE_TITLE);
        this.setLayout(null);
        this.getContentPane().setBackground(Colors.DARK_GRAY_BACKGROUND);
        this.setSize(710, 930);
        this.setResizable(false);
        this.setIconImage(this.imageUser50); //Añadimos un icono
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Para cerrar la ventana en la X
        centrarEnPantalla(this);
        //Panel
        this.panel.setLayout(null);
        this.panel.setBounds(30, 30, 650, 835);
        //TITLE
        this.title.setFont(Fonts.MY_FONT1);
        this.title.setBounds(25, 30, 600, 50);
        //NICKNAME
        this.nicknameLabel.setFont(Fonts.MY_FONT2);
        this.nicknameLabel.setBounds(120, 95, 400, 40);
        //NICKNAME TEXT FIELD
        this.nicknameTextField.setFont(Fonts.MY_FONT5);
        this.nicknameTextField.setForeground(Colors.BLACK);
        this.nicknameTextField.setHorizontalAlignment(JPasswordField.CENTER);
        this.nicknameTextField.setBounds(148, 150, 350, 50);
        //PRIVATE KEY 
        this.privateKeyLabel.setFont(Fonts.MY_FONT2);
        this.privateKeyLabel.setBounds(120, 222, 400, 40);
        //PRIVATE KEY TEXT FIELD
        this.privateKeyTextField.setFont(Fonts.MY_FONT5);
        this.privateKeyTextField.setForeground(Colors.BLACK);
        this.privateKeyTextField.setHorizontalAlignment(JPasswordField.CENTER);
        this.privateKeyTextField.setBounds(148, 277, 350, 50);
        //UPDATE PROFILE BUTTON
        this.buttonUpdateProfile.setFont(Fonts.MY_FONT2);
        this.buttonUpdateProfile.setFocusable(false);
        this.buttonUpdateProfile.setBounds(148, 347, 350, 50);
        //IMPORT BACKUP LABEL
        this.importBackupLabel.setFont(Fonts.MY_FONT3);
        this.importBackupLabel.setBounds(25, 430, 600, 50);
        //IMPORT BUTTON
        this.buttonImport.setFont(Fonts.MY_FONT1);
        this.buttonImport.setFocusable(false);
        this.buttonImport.setBounds(148, 495, 350, 80);
        //EXPORT BACKUP LABEL
        this.exportBackupLabel.setFont(Fonts.MY_FONT3);
        this.exportBackupLabel.setBounds(25, 600, 600, 50);
        //EXPORT BUTTON
        this.buttonExport.setFont(Fonts.MY_FONT1);
        this.buttonExport.setFocusable(false);
        this.buttonExport.setBounds(148, 665, 350, 80);
        //COMPANY LABEL
        this.company.setFont(Fonts.FONT_COMPANY);
        this.company.setBounds(25, 760, 600, 50);
    }

    private void joinObjects() {
        this.add(this.panel);
        this.panel.add(this.title);
        this.panel.add(this.nicknameLabel);
        this.panel.add(this.nicknameTextField);
        this.panel.add(this.privateKeyLabel);
        this.panel.add(this.privateKeyTextField);
        this.panel.add(this.buttonUpdateProfile);
        this.panel.add(this.importBackupLabel);
        this.panel.add(this.buttonImport);
        this.panel.add(this.exportBackupLabel);
        this.panel.add(this.buttonExport);
        this.panel.add(this.company);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.drawImage(this.imageUser50, 550, 210, 50, 50, this);
        g2d.drawImage(this.imageLLave50, 550, 337, 50, 50, this);
        g2d.setStroke(new BasicStroke(2)); //Damos un ancho a la linea.
        g2d.drawLine(70, 475, 640, 475);
        g2d.drawImage(this.imageImportBackup64, 545, 565, 64, 64, this);
        g2d.drawImage(this.imageExportBackup64, 545, 735, 64, 64, this);
    }

    private void addListenerToObjects() {
        this.buttonUpdateProfile.addActionListener(this.controller);
        this.buttonImport.addActionListener(this.controller);
        this.buttonExport.addActionListener(this.controller);
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

    //Update Profile succesfull
    public void showUpdateProfileSuccesful() {
        JOptionPane.showMessageDialog(this, Texts.UPDATE_SUCCESFUL_MESSAGE, Texts.UPDATE_SUCCESFUL_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageUser50));
    }

    //Export Backup succesfull
    public void showExportBackupSuccesful() {
        JOptionPane.showMessageDialog(this.vistaPadre, Texts.EXPORT_SUCCESFUL_MESSAGE, Texts.EXPORT_SUCCESFUL_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageExportBackup64));
    }

    //Import Backup succesfull
    public void showImportBackupSuccesful() {
        JOptionPane.showMessageDialog(this, Texts.IMPORT_SUCCESFUL_MESSAGE, Texts.IMPORT_SUCCESFUL_TITLE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.imageImportBackup64));
    }

    //METODO DE SEGURIDAD QUE PREGUNTA LA MASTER PASSWORD AL USUARIO ANTES DE UN PASO CRITICO. DEVUELVE LA PASSWORD INTRODUCIDA POR EL USUARIO.
    public String askForMasterPassword(String message) {
        //Usamos JPasswordField para ocultar la password.
        JPasswordField pf = new JPasswordField();
        pf.setFont(Fonts.MY_FONT3);
        pf.setHorizontalAlignment(JPasswordField.CENTER);
        int valor = JOptionPane.showConfirmDialog(this, pf, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION);

        if (valor == JOptionPane.OK_OPTION) {
            return new String(pf.getPassword()); //Cuando el usuario acepte...retornamos la password introducida...
        } else {
            return null; //Si no...retornamos null.
        }
    }

    //Error empty fields update profile
    public void showErrorFields() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_EMPTY_FIELDS_LOGIN, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error updating profile credentials
    public void showErrorUpdatingProgile() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_UPDATING_PROFILE, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);

    }

    //Error no back up file selected
    public void showErrorNoFileSelected() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_NO_BACKUP_FILE_SELECTED, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error no back up directory selected
    public void showErrorNoDirectorySelected() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_NO_BACKUP_DIRECTORY_SELECTED, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error backup file empty
    public void showErrorBackupFileEmpty() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_BACKUP_NO_CONTENT, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error private key
    public void showErrorIncorrectPrivateKey() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_PRIVATE_KEY, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error at exporting backup
    public void showErrorExportingBackUp() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_EXPORTING_BACKUP, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    //Error incorrect type of backup file
    public void showErrorIncorrectBackUpFile() {
        JOptionPane.showMessageDialog(this, Texts.ERROR_BACKUP_TYPE, Texts.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    public String getNicknameTextField() {
        return nicknameTextField.getText();
    }

    public String getPrivateKeyTextField() {
        return String.valueOf(privateKeyTextField.getPassword());
    }

    public YourAcountsView getVistaPadre() {
        return vistaPadre;
    }

}
