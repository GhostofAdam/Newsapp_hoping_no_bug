package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.text.TextPaint;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Adapter.GlideImageLoader;

import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.youth.banner.Banner;

import java.util.ArrayList;


public class NewsDetailActivity extends AppCompatActivity {
    private TextView contentView;
    private TextView titleView;
    private TextView subtitleView;
    private SparkButton collect;
    private News news;
    private Banner banner;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent= getIntent();
        news = (News) intent.getSerializableExtra("news");
        contentView = findViewById(R.id.news_content);
        contentView.setText(news.getContent());
        titleView = findViewById(R.id.news_title);
        titleView.setText(news.getTitle());
        TextPaint tp = titleView.getPaint();
        tp.setFakeBoldText(true);
        subtitleView = findViewById(R.id.news_sub_title);
        subtitleView.setText(news.getPublisher()+"\n"+news.getPublishTime()+"\n");
        tp = subtitleView.getPaint();
        tp.setFakeBoldText(true);
        collect = findViewById(R.id.spark_button);

        collect.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                OperateOnSQLite op = new OperateOnSQLite();
                SQLiteDbHelper help = SQLiteDbHelper.getInstance(getApplicationContext());
                User user = (User)getApplication();
                if (buttonState) {
                    // Button is active
                    if(user.getUsername()==null){
                        Toast.makeText(getApplicationContext(), "请登陆",
                                Toast.LENGTH_SHORT).show();
                        collect.setChecked(true);
                        return;
                    }
                    op.insertNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION, news,user.getUsername());
                    Toast.makeText(getApplicationContext(), "收藏成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if(user.getUsername()==null){
                        Toast.makeText(getApplicationContext(), "请登陆",
                                Toast.LENGTH_SHORT).show();
                        collect.setChecked(true);
                        return;
                    }
                    op.deleteNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION, news,user.getUsername());
                    // Button is inactive
                    Toast.makeText(getApplicationContext(), "取消收藏",
                            Toast.LENGTH_SHORT).show();
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

        User user = (User)getApplication();
        if(user.getUsername()!=null){
            OperateOnSQLite op = new OperateOnSQLite();
            SQLiteDbHelper help = SQLiteDbHelper.getInstance(getApplicationContext());
            op.insertNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN, news,user.getUsername());
            if(op.findNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,news.getNewsID(),user.getUsername())){
                collect.setChecked(true);
            }
        }
       else
           collect.setChecked(true);

        banner = findViewById(R.id.banner);

        banner.setImageLoader(new GlideImageLoader());
        ArrayList<String> urls =  news.getImageUrl();
        if(urls.size()==0){
            ArrayList<Integer> path = new  ArrayList<Integer>();
            path.add(R.drawable.bg_people);
            banner.setImages(path);
        }
        else{
            banner.setImages(urls);
        }
        banner.start();
    }

}
