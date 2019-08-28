package com.example.myapplication.Utilities;

import java.util.List;

public class News
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

    public String[] getImage() {
        if(image.length()==2)
            return null;
        String[] strings = image.split("\\[|\\]|\\,");
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

class Keyword
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

class When
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

class Where
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

class Who
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

class Person
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

class Organization
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

class Location
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


