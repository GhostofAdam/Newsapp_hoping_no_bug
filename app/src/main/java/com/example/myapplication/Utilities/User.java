package com.example.myapplication.Utilities;

import android.app.Application;

import com.example.myapplication.R;
import com.example.myapplication.SQLite.serverAvail;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.invoke.WrongMethodTypeException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
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
    private int theme=0;
    private IWXAPI mWxApi;
    private  String username=null;
    private  String password=null;
    private Vector<News>colloctionsNews;
    private Vector<News>historyNews;
    private Vector<String> searchSuggestios;
    private Vector<String> keyswords = new Vector<>();
    public final static int ADD_COLLECTION=0;
    public final static int ADD_HISTORY=1;
    public final static int DELETE_COLLECTION=2;
    public final static int DELETE_HISTORY=3;
    public final static int FIND_COLLECTION=4;
    public final static int FIND_HISTORY=5;
    public final static int ADD_SEARCH=6;
    public final static int ADD_ACCOUNT=7;
    public final static int ADD_FLITER=8;
    public final static int DELETE_FLITER=9;
    public void initCollections(Vector<News> n){
        colloctionsNews = n;
    }
    public void initHistory(Vector<News> n){
        historyNews = n;
    }
    public void initSearch(Vector<String> s){
        searchSuggestios.addAll(s);
    }
    public void initFliter(Vector<String> s){keyswords =s;}
    public void addCollection(News news){
        colloctionsNews.add(news);
    }

    public void onCreate() {
        super.onCreate();
        mWxApi = WXAPIFactory.createWXAPI(this, "wx8959869f468fec43", true);
        mWxApi.registerApp("wx8959869f468fec43");

    }
    public Vector<String>getKeyswords(){
        return keyswords;
    }
    public void deleteKeywords(String s){
        keyswords.remove(s);
    }
    public void addKeywords(String s){
        keyswords.add(s);
    }
    public IWXAPI getAPI(){
        return  mWxApi;
    }

    public void addHistory(News news){
        if(historyNews.contains(news))
            return;
        historyNews.add(news);
    }
    public void deleteCollection(News news){
        colloctionsNews.remove(news);
    }
    public void deleteHistory(News news){
        historyNews.remove(news);
    }
    public void addSearch(String string){
        if(searchSuggestios.contains(string))
            return;
        searchSuggestios.add(string);
    }
    public Vector<News> getCollections(){
        return colloctionsNews;
    }
    public Vector<News> getHistory(){
        return  historyNews;
    }
    public Vector<String> getSearch(){
        return  searchSuggestios;
    }
    public boolean findHistory(News news){
        return historyNews.contains(news);
    }
    public boolean findCollection(News news){
        return colloctionsNews.contains(news);
    }




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
        historyNews = new Vector<>();
        colloctionsNews = new Vector<>();
        searchSuggestios = new Vector<>();
//        String[] chanles = getResources().getStringArray(R.array.chanles);
//        for(int i=0;i<chanles.length;i++){
//            CGRecord r = new CGRecord();
//            r.label = chanles[i];
//            r.n=0;
//            r.records = new Vector<>();
//            records.add(r);
//        }
    }

    public Vector<News> getRecomendation(){
         Vector<News>recomendations = new Vector<>();
//         recomendations = new UrlRequest().urlRequest(15,"2019-07-01","2019-07-03","特朗普","科技");
        DateUtility dateUtility = new DateUtility();
        String today = dateUtility.getDateString(dateUtility.getCurrent());
        serverAvail server = new serverAvail();
        if(!server.test())
            return recomendations;
        if(username==null)
            recomendations = new UrlRequest().urlRequest(10,"","2019-09-05","海洋研究","");
        else{
            for(News news:colloctionsNews){
                if(recomendations.size()>10) break;
                if(Math.random()<0.5){
                    recomendations.addAll(new UrlRequest().urlRequest(2,"",today,news.getKeywords()[0].getWord(),news.getCategory()));
                }
            }
            for(News news:historyNews){
                if(recomendations.size()>15) break;
                if(Math.random()<0.5){
                    recomendations.addAll(new UrlRequest().urlRequest(2,"",today,news.getKeywords()[0].getWord(),news.getCategory()));
                }
            }

            recomendations.addAll(new UrlRequest().urlRequest(15-recomendations.size(),"",today,"",""));
        }
        return recomendations;
    }
    public void clear(){
        username = null;
        password = null;
        for (CGRecord r:records){
            r.records.clear();
        }
        colloctionsNews.clear();
        historyNews.clear();
        searchSuggestios.clear();
        keyswords.clear();
    }
    public void settheme(int i){
        theme = i;
    }
    public int gettheme(){
        return theme;
    }
}
