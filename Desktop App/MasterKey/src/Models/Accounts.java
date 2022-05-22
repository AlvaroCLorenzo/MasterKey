package Models;

import java.util.ArrayList;

/**
 * @autor: Alvaro
 */
public class Accounts {

    private ArrayList<SocialNetwork> socialNetworks;
    private ArrayList<CreditCard> creditCards;
    private ArrayList<CryptoWallet> cryptoWallets;

    public Accounts() {
        this.socialNetworks = new ArrayList<>();
        this.creditCards = new ArrayList<>();
        this.cryptoWallets = new ArrayList<>();
    }

    //Getters
    public ArrayList<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public ArrayList<CreditCard> getCreditCards() {
        return creditCards;
    }

    public ArrayList<CryptoWallet> getCryptoWallets() {
        return cryptoWallets;
    }

    //Setters
    public void setSocialNetworks(ArrayList<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }

    public void setCreditCards(ArrayList<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public void setCryptoWallets(ArrayList<CryptoWallet> cryptoWallets) {
        this.cryptoWallets = cryptoWallets;
    }

}
