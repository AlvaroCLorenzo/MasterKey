package Models;

/**
 * @autor: Alvaro
 */
public class CreditCard {

    private int id;
    private String bankName;
    private String cardType;
    private String userName;
    private String cardNumber;
    private String endDate;
    private int CCV;
    private int PIN;

    public CreditCard(int id, String bankName, String cardType, String userName, String cardNumber, String endDate, int CCV, int PIN) {
        this.id = id;
        this.bankName = bankName;
        this.cardType = cardType;
        this.userName = userName;
        this.cardNumber = cardNumber;
        this.endDate = endDate;
        this.CCV = CCV;
        this.PIN = PIN;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public String getUserName() {
        return userName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getCCV() {
        return CCV;
    }

    public int getPIN() {
        return PIN;
    }

    //Setters
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCCV(int CCV) {
        this.CCV = CCV;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

}
