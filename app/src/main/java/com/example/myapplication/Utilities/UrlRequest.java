package com.example.myapplication.Utilities;

import java.util.Vector;

public class UrlRequest {
    public Vector<News> urlRequest(int size,String startDate,String endDate,String words,String categories){
        String url =  "https://api2.newsminer.net/svc/news/queryNewsList?size="+size+"&startDate="+startDate+"&endDate="+endDate+"&words="+words+"&categories="+categories;
        //String url = "https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate=2019-07-01&endDate=2019-07-03&words=特朗普&categories=科技";
        GetWeb getWeb = new GetWeb();
        getWeb.senRequest(url);
        Vector<News> data = new Vector<>();
        while (getWeb.newsList.getData() == null)   {}
        for (int i = 0; i < getWeb.newsList.getData().length; i++)
        {
            data.add(getWeb.newsList.getData()[i]);
        }
        return data;
    }
}
