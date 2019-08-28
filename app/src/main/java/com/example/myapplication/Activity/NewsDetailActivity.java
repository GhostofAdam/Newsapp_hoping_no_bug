package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Utilities.News;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.awt.font.TextAttribute;

public class NewsDetailActivity extends AppCompatActivity {
    TextView contentView;
    TextView titleView;
    TextView subtitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent= getIntent();
        News news = (News) intent.getSerializableExtra("news");
        contentView = findViewById(R.id.news_content);
        contentView.setText(news.getContent());
        titleView = findViewById(R.id.news_title);
        titleView.setText(news.getTitle());
        titleView.setTextScaleX(2);
        TextPaint tp = titleView.getPaint();
        tp.setFakeBoldText(true);
        subtitleView = findViewById(R.id.news_sub_title);
        subtitleView.setText(news.getPublisher()+"\n"+news.getPublishTime()+"\n"+news.getOrganizations()+"\n"+news.getLocations());
        tp = subtitleView.getPaint();
        tp.setFakeBoldText(true);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    }
}
