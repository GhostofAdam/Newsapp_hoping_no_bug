package com.example.myapplication.Utilities;

import android.app.Application;

import com.example.myapplication.R;

import java.lang.invoke.WrongMethodTypeException;
import java.util.Vector;
class CGRecord{
    public int n=0;
    public String label="";
    public Vector<WRecord>records;
}
class WRecord{
    public int n=0;
    public String label="";
}
public class User extends Application {
    private  String username=null;
    private  String password=null;
    private Vector<CGRecord>records=null;
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
    public User(){
        records=new Vector<CGRecord>();
//        String[] chanles = getResources().getStringArray(R.array.chanles);
//        for(int i=0;i<chanles.length;i++){
//            CGRecord r = new CGRecord();
//            r.label = chanles[i];
//            r.n=0;
//            r.records = new Vector<>();
//            records.add(r);
//        }
    }
    public void initRecords(Vector<News>collections,Vector<News>histories){

    }
    public void addCollection(News news){

    }
    public void addHistory(News news){

    }
    public void deleteCollection(News news){
    }
    public Vector<News> getRecomendation(){
         Vector<News>recomendations = new Vector<>();
         recomendations = new UrlRequest().urlRequest(15,"2019-07-01","2019-07-03","特朗普","科技");
         return recomendations;
    }
    public void clear(){
        username = null;
        password = null;
        for (CGRecord r:records){
            r.records.clear();
        }
    }
}
