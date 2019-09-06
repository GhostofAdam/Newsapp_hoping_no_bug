package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.text.TextPaint;
import android.util.SparseArray;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Adapter.GlideImageLoader;

import com.example.myapplication.Fragment.ShareFragment;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.OperateOnServer;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Service.SQLservice;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;


public class NewsDetailActivity extends AppCompatActivity {
    private TextView contentView;
    private TextView titleView;
    private TextView subtitleView;
    private ShineButton collect;
    private ImageButton share;
    private News news;
    private Banner banner;
    private ImageButton back;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final User user = (User)getApplication();
        switch (user.gettheme()){
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.DayTheme);
                break;
            case 2:
                setTheme(R.style.NightTheme);
                break;
            default:
                break;

        }
        setContentView(R.layout.activity_news_detail);
        final Intent intent= getIntent();
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
        if(user.getUsername()==null)
            collect.setEnabled(false);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getUsername()==null)
                    Toast.makeText(getApplicationContext(), "请登录",
                            Toast.LENGTH_SHORT).show();
            }
        });
        collect.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                User user = (User)getApplication();
               if(checked){
                   user.addCollection(news);
                   Intent intent1 = new Intent(NewsDetailActivity.this, SQLservice.class);
                   intent1.putExtra("flag",User.ADD_COLLECTION);
                   intent1.putExtra("data",news);
                   startService(intent1);
                   Toast.makeText(getApplicationContext(), "收藏成功",
                           Toast.LENGTH_SHORT).show();
               }
               else{
                   user.deleteCollection(news);
                   Intent intent1 = new Intent(NewsDetailActivity.this, SQLservice.class);
                   intent1.putExtra("flag",User.DELETE_COLLECTION);
                   intent1.putExtra("data",news);
                   startService(intent1);
                   // Button is inactive
                   Toast.makeText(getApplicationContext(), "取消收藏",
                           Toast.LENGTH_SHORT).show();
               }
            }
        });

        back = findViewById(R.id.news_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                shareBottomSheetDialog = ShareFragment.newInstance(news);
//                shareBottomSheetDialog.show(getSupportFragmentManager(),"asd");
                showShareDialog();
            }
        });

        if(user.getUsername()!=null&&!user.getHistory().contains(news)){

            user.addHistory(news);
            Intent intent1 = new Intent(NewsDetailActivity.this, SQLservice.class);
            intent1.putExtra("flag",User.ADD_HISTORY);
            intent1.putExtra("data",news);
            startService(intent1);
            if(user.findCollection(news)){
                collect.setChecked(true);
            }
        }


        banner = findViewById(R.id.banner);

        banner.setImageLoader(new GlideImageLoader());
        ArrayList<String> urls =  news.getImageUrl();
        if(urls==null||urls.size()==0){
            ArrayList<Integer> path = new  ArrayList<Integer>();
            path.add(R.drawable.bg_people);
            banner.setImages(path);
        }
        else{
            banner.setImages(urls);
        }
        banner.start();
    }
    public void showShareDialog() {
        ShareEntity testBean = new ShareEntity(news.getTitle(), news.getPublisher()+news.getPublishTime());
        testBean.setShareBigImg(true);
        if(news.getImageUrl()!=null&&news.getImageUrl().size()>0)
            testBean.setImgUrl(news.getImageUrl().get(0));
        else {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bg_people );
            String filePath = ShareUtil.saveBitmapToSDCard(this,bmp);
            testBean.setImgUrl(filePath);
        }
        SparseArray<ShareEntity> sparseArray = new SparseArray<>();
        sparseArray.put(ShareConstant.SHARE_CHANNEL_QQ, testBean);
        //sparseArray.put(ShareConstant.SHARE_CHANNEL_QZONE, testBean);
        sparseArray.put(ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE, testBean);
        sparseArray.put(ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND, testBean);
        sparseArray.put(ShareConstant.SHARE_CHANNEL_SINA_WEIBO, testBean);
        sparseArray.put(ShareConstant.SHARE_CHANNEL_SYSTEM, testBean);

        ShareUtil.showShareDialog(this, testBean, ShareConstant.REQUEST_CODE);
    }


}
