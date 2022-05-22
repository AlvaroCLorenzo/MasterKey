package com.example.masterkey.Models;

public class SocialNetwork {
    private int id;
    private String webName;
    private String url;
    private String email;
    private String userName;
    private String password;

    public SocialNetwork(int id, String webName, String url, String email, String userName, String password) {
        this.id = id;
        this.webName = webName;
        this.url = url;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebName() {
        return webName;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    //Setters
    public void setWebName(String webName) {
        this.webName = webName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
