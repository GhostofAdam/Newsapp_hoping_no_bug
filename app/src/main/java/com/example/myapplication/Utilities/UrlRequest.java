package com.example.myapplication.Utilities;

public class UrlRequest {
    String urlRequest(int size,String startDate,String endDate,String words,String categories){
        return "https://api2.newsminer.net/svc/news/queryNewsList?size="+size+"&startDate="+startDate+"&endDate="+endDate+"&words="+words+"&categories="+categories;
    }
}
