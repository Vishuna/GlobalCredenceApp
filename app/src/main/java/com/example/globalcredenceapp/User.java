package com.example.globalcredenceapp;


public class User {
    private String token;

    public User(String token) {

        this.token = token;
    }


    public String getToken(){
        return token;
    }
}
