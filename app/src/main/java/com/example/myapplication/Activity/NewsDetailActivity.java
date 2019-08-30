package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.ImageAboutAdapter;
import com.example.myapplication.Fragment.Pageholder;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class NewsDetailActivity extends AppCompatActivity {
    TextView contentView;
    TextView titleView;
    TextView subtitleView;
    SparkButton collect;
    News news;
    private static int PAGER_TIOME = 5000;//间隔时间
    private List<View> mDots;//小点
    private Thread autoPlay;
    private ViewPager mViewPager;
    private ImageButton back;
    private int previousPosition = 0;//前一个被选中的position
    private List<ImageView> mImageList;//轮播的图片集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent= getIntent();
        news = (News) intent.getSerializableExtra("news");
        ArrayList<String>urls = news.getImageUrl();
        contentView = findViewById(R.id.news_content);
        contentView.setText(news.getContent());
        titleView = findViewById(R.id.news_title);
        titleView.setText(news.getTitle());
        titleView.setTextScaleX(2);
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
        setImages();

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
    }
    private void setImages(){
        mViewPager = findViewById(R.id.imageAbout);
        mImageList = new ArrayList<>();

        ArrayList<String> urls = news.getImageUrl();
        if(urls.size()==0){
            ImageView iv;
            iv = new ImageView(getApplicationContext());
            Glide.with(this).load(R.drawable.bg_people).into(iv);
            iv.setId(0);
            mImageList.add(iv);
        }
        else {
            int i = 0;
            for (String url : urls) {
                ImageView iv;
                iv = new ImageView(getApplicationContext());
                Glide.with(this).load(url).into(iv);
                iv.setId(i);
                mImageList.add(iv);
            }
        }

        //添加轮播点
        LinearLayout linearLayoutDots = findViewById(R.id.lineLayout_dot);
        mDots = addDots(linearLayoutDots,fromResToDrawable(getApplicationContext(),R.drawable.is_dot_normal),mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable
        ImageAboutAdapter viewPagerAdapter = new ImageAboutAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;

                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setFirstLocation() {
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }
    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(getApplicationContext());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4,0,4,0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }
    public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number){
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout,backgount);
            dots.add(findViewById(dotId));
        }
        return dots;
    }
    private void autoPlayView() {
        autoPlay = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    NewsDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        });
        autoPlay.start();
    }
}
