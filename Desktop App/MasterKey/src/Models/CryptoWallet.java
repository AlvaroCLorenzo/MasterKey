package Models;

/**
 * @autor: Alvaro
 */
public class CryptoWallet {

    private int id;
    private String walletName;
    private String walletType;
    private String emailAsociated;
    private String password;
    private String publicKey;
    private String privateKey;

    public CryptoWallet(int id, String walletName, String walletType, String email, String password, String publicKey, String privateKey) {
        this.id = id;
        this.walletName = walletName;
        this.walletType = walletType;
        this.emailAsociated = email;
        this.password = password;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWalletName() {
        return walletName;
    }

    public String getWalletType() {
        return walletType;
    }

    public String getEmailAsotiated() {
        return emailAsociated;
    }

    public String getPassword() {
        return password;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    //Setters
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public void setEmailAsotiated(String email) {
        this.emailAsociated = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
