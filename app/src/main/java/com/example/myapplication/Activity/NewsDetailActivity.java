package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Utilities.News;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.awt.font.TextAttribute;

public class NewsDetailActivity extends AppCompatActivity {
    TextView contentView;
    TextView titleView;
    TextView subtitleView;
    SparkButton collect;
    private ImageButton back;
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
        collect = findViewById(R.id.spark_button);
        collect.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    // Button is active
                } else {
                    // Button is inactive
                }
            }
            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }
        });
        back = findViewById(R.id.news_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }
}
