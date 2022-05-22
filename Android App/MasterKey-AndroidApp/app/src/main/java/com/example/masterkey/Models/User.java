package com.example.masterkey.Models;

public class User {
    private String nickName;
    private String masterPassword;

    public User(String nickName, String masterPassword) {
        this.nickName = nickName;
        this.masterPassword = masterPassword;
    }

    //Getters
    public String getNickName() {
        return nickName;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    //Setters
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }
}
