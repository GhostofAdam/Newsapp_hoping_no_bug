package com.example.myapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


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

    private Vector<String> mImageTitles;//标题集合

    private NewsListAdapter newsListAdapter;
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    public Pageholder(String label){
        Label = label;
    }
    public static Pageholder newInstance(int index, String label) {
        Pageholder fragment = new Pageholder(label);
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        if(label.equals("推荐")){
            //User user= (User) fragment.getActivity().getApplication();

            fragment.newsListAdapter = new NewsListAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",label),null,fragment);
        }
        else
            fragment.newsListAdapter = new NewsListAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",label),null,fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news",newsListAdapter.Dataset.get(position));
                getActivity().startActivity(intent);
            }
        });
        newsListAdapter.setOnItemLongClickListener(new NewsListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news",newsListAdapter.Dataset.get(position));
                getActivity().startActivity(intent);
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
        newsListAdapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",Label),false);

    }
    private void LoadMoreData(){
        newsListAdapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",Label),true);
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
}
