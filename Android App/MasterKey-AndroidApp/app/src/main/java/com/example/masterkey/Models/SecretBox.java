package com.example.masterkey.Models;

public class SecretBox {
    private User user;
    private Accounts accounts;

    public SecretBox(User user) {
        this.user = user;
        this.accounts = new Accounts();
    }

    //Getters
    public User getUser() {
        return user;
    }

    public Accounts getAccounts() {
        return accounts;
    }
}
