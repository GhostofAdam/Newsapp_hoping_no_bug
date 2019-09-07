package com.example.myapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.VideoNewsActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import com.example.myapplication.SQLite.serverAvail;
import com.example.myapplication.Utilities.DateUtility;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.Activity.NewsDetailActivity;
import com.example.myapplication.Utilities.UrlRequest;
import com.example.myapplication.Utilities.User;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

public class Pageholder extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public String Label;
    private View root;
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private User user;
    private Vector<String> mImageTitles;//标题集合
    private Date lastDate;
    public NewsListAdapter newsListAdapter;
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    public static Pageholder newInstance(int index, String label) {
        Pageholder fragment = new Pageholder();
        Bundle bundle = new Bundle();
        fragment.Label=label;
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User)getActivity().getApplication();
        DateUtility dateUtility = new DateUtility();
        lastDate = dateUtility.getCurrent();
        String now = dateUtility.getDateString(lastDate);
        String before = dateUtility.getDateString(dateUtility.backAWeek(lastDate));
        if(savedInstanceState!=null){
            Vector<News> data = ( Vector<News>) savedInstanceState.getSerializable("data");
            serverAvail server = new serverAvail();

            newsListAdapter = new NewsListAdapter(data,null,this);
            Label = (String)savedInstanceState.getSerializable("label");
        }
        else{
            serverAvail server = new serverAvail();
            if(!server.test()){
                newsListAdapter = new NewsListAdapter(new Vector<News>(),null,this);
            }
            else {
                if (Label.equals("推荐")) {
                    newsListAdapter = new NewsListAdapter(user.getRecomendation(), null, this);
                } else
                    newsListAdapter = new NewsListAdapter(new UrlRequest().urlRequest(10, before, now, "", Label), null, this);
            }
        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pageholder, container, false);

        refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                RefreshData();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                LoadMoreData();
                refreshLayout.finishLoadmore();
            }
        });

        recyclerView = root.findViewById(R.id.myRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                News news = newsListAdapter.Dataset.get(position);
                if(news.getVideoUrl()==null) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("news", news);
                    getActivity().startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getActivity(), VideoNewsActivity.class);
                    intent.putExtra("news", news);
                    getActivity().startActivity(intent);
                }
            }
        });
        newsListAdapter.setOnItemLongClickListener(new NewsListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                News news = newsListAdapter.Dataset.get(position);
                if(news.getVideoUrl()==null) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("news", news);
                    getActivity().startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getActivity(), VideoNewsActivity.class);
                    intent.putExtra("news", news);
                    getActivity().startActivity(intent);
                }
            }
        });
        initData();//初始化数据
        return root;
    }

    public void initData() {
        mImageTitles = new Vector<>();
        for(int i=0;i<4&&i<newsListAdapter.Dataset.size();i++)
            mImageTitles.add(newsListAdapter.Dataset.get(i).getTitle());
    }
    private void RefreshData(){
        serverAvail server = new serverAvail();
        if(!server.test()){
            return;
        }
        if(Label.equals("推荐")){
            newsListAdapter.notifyAdapter(user.getRecomendation(),false);
        }
        else {
            DateUtility dateUtility = new DateUtility();
            lastDate = dateUtility.getCurrent();
            String now = dateUtility.getDateString(lastDate);
            String before = dateUtility.getDateString(dateUtility.backAWeek(lastDate));
            newsListAdapter.notifyAdapter(new UrlRequest().urlRequest(10, before, now, "", Label), false);
        }

    }
    private void LoadMoreData(){
        serverAvail server = new serverAvail();
        if(!server.test()){
            return;
        }
        if(Label.equals("推荐")){
            newsListAdapter.notifyAdapter(user.getRecomendation(),true);
        }
        else {
            DateUtility dateUtility = new DateUtility();
            lastDate = dateUtility.backAWeek(lastDate);
            String now = dateUtility.getDateString(lastDate);
            String before = dateUtility.getDateString(dateUtility.backAWeek(lastDate));
            newsListAdapter.notifyAdapter(new UrlRequest().urlRequest(10, before, now, "", Label), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //autoPlayView();
    }


    @Override
    public  void onPause() {
        super.onPause();
        //isStop = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data",newsListAdapter.Dataset);
        outState.putSerializable("lable",Label);
    }
}
