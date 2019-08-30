package com.example.myapplication.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class News implements Serializable
{
    private String image;
    private String publishTime;
    private Keyword[] keywords;
    private String language;
    private String video;
    private String title;
    private When[] when;
    private String content;
    private Person[] persons;
    private String newsID;
    private String crawlTime;
    private Organization[] organizations;
    private String publisher;
    private Location[] locations;
    private Where[] where;
    private String category;
    private Who[] who;

    public void setNews(String newsID, String title, String content, String publisher, String publishTime)
    {
        this.newsID = newsID;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.publishTime = publishTime;
    }

    public ArrayList<String> getImageUrl() {
        if(image==null)
            return null;
        String[] strings = image.split("\\[|\\]|,| ");
        ArrayList<String> list = new  ArrayList<String>();
        for(int i=0;i<strings.length;i++){
            if(strings[i].length()>3)
                list.add(strings[i]);
        }
        return list;
    }
    public String[] getVideoUrl() {
        if(video==null||video.length()==0)
            return null;
        String[] strings = video.split("\\[|\\]|,");
        return strings;
    }
    public String getPublishTime() { return this.publishTime; }
    public Keyword[] getKeywords() { return this.keywords; }
    public String getLanguage() { return this.language; }
    public String getVideo() { return this.video; }
    public String getTitle() { return this.title; }
    public When[] getWhen() { return this.when; }
    public String getContent() { return this.content; }
    public Person[] getPersons() { return this.persons; }
    public String getNewsID() { return this.newsID; }
    public String getCrawlTime() { return this.crawlTime; }
    public Organization[] getOrganizations() { return this.organizations; }
    public String getPublisher() { return this.publisher; }
    public Location[] getLocations() { return this.locations; }
    public Where[] getWhere() { return this.where; }
    public String getCategory() { return this.category; }
    public Who[] getWho() { return this.who; }
}

class Keyword implements Serializable
{
    private String word;
    private Double score;

    public String getWord()
    {
        return this.word;
    }
    public Double getScore()
    {
        return this.score;
    }
}

class When implements Serializable
{
    private String word;
    private Double score;

    public String getWord()
    {
        return this.word;
    }
    public Double getScore()
    {
        return this.score;
    }
}

class Where implements Serializable
{
    private String word;
    private Double score;

    public String getWord()
    {
        return this.word;
    }
    public Double getScore()
    {
        return this.score;
    }
}

class Who implements Serializable
{
    private String word;
    private Double score;

    public String getWord()
    {
        return this.word;
    }
    public Double getScore()
    {
        return this.score;
    }
}

class Person implements Serializable
{
    private String mention;
    private String linkedURL;
    private int count;

    public String getMention()
    {
        return this.mention;
    }
    public String getLinkedURL()
    {
        return this.linkedURL;
    }
    public int getCount()
    {
        return this.count;
    }
}

class Organization implements Serializable
{
    private String mention;
    private String linkedURL;
    private int count;

    public String getMention()
    {
        return this.mention;
    }
    public String getLinkedURL()
    {
        return this.linkedURL;
    }
    public int getCount()
    {
        return this.count;
    }
}

class Location implements Serializable
{
    private String mention;
    private Double lat;
    private String linkedURL;
    private int count;
    private Double lng;

    public String getMention() { return this.mention; }
    public Double getLat()
    {
        return this.lat;
    }
    public String getLinkedURL() { return this.linkedURL; }
    public int getCount() { return this.count; }
    public Double getLng()
    {
        return this.lng;
    }
}


