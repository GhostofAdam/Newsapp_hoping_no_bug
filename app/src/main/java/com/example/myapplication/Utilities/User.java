package com.example.myapplication.Utilities;

import android.app.Application;

public class User extends Application {
    private  String username=null;
    private  String password=null;
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
