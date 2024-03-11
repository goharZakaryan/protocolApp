package com.example.protocolapp.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    private String email;
    private String password;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
