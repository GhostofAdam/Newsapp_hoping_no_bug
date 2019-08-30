package com.example.myapplication.Utilities;

import android.app.Application;

public class User extends Application {
    private  String username;
    private  String password;
    public  void setUsername(String s){
        username = s;
    }
    public  void setPassword(String s){
        password = s;
    }
    public  String getUsername(){
        return username;
    }
    public  String getPassword(){
        return password;
    }
}
