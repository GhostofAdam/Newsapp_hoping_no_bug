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
import com.example.myapplication.Adapter.ImageAboutAdapter;
import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.Activity.NewsDetailActivity;
import com.example.myapplication.Utilities.UrlRequest;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

public class Pageholder extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public String Label;
    private View root;
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private List<ImageView> mImageList;//轮播的图片集合
    private Vector<String> mImageTitles;//标题集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//小点
    private Thread autoPlay;
    private Vector<Bitmap> imageRess;
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 5000;//间隔时间
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
        fragment.newsListAdapter = new NewsListAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",label),null,fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recyclerView = (RecyclerView) getView().findViewById(R.id.myRecycle);
        //NewsListAdapter myAdapter = new NewsListAdapter();
        //recyclerView.setAdapter(myAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pageholder, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
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

        mViewPager = (ViewPager) root.findViewById(R.id.imageAbout);
        mTvPagerTitle = (TextView) root.findViewById(R.id.tv_pager_title);
        initData();//初始化数据
        initView();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放

        return root;
    }
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }
    public void initData() {
        mImageTitles = new Vector<>();
        for(int i=0;i<4&&i<newsListAdapter.Dataset.size();i++)
            mImageTitles.add(newsListAdapter.Dataset.get(i).getTitle());

        mImageList = new ArrayList<>();
        ImageView iv;
        int j=0;
        for (int i = 0; i < mImageTitles.size(); i++) {
            iv = new ImageView(getContext());
            while(newsListAdapter.Dataset.get(j).getImageUrl().size()==0)
                j++;
            Glide.with(getActivity()).load(newsListAdapter.Dataset.get(j).getImageUrl().get(0)).into(iv);
            iv.setId(j);
            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            mImageList.add(iv);
            j++;
        }

        //添加轮播点
        LinearLayout linearLayoutDots = (LinearLayout) root.findViewById(R.id.lineLayout_dot);
        mDots = addDots(linearLayoutDots,fromResToDrawable(getContext(),R.drawable.is_dot_normal),mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable
    }
    private void RefreshData(){
        newsListAdapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",Label),false);
        int j=0;
        for (int i = 0; i < mImageTitles.size(); i++) {
            ImageView iv =  mImageList.get(i);
            while(newsListAdapter.Dataset.get(j).getImageUrl().size()==0)
                j++;
            Glide.with(getActivity()).load(newsListAdapter.Dataset.get(j).getImageUrl().get(0)).into(iv);
            iv.setId(j);
            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            j++;
        }
    }
    private void LoadMoreData(){
        newsListAdapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25","",Label),true);
    }
    private class pagerImageOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news",newsListAdapter.Dataset.get(v.getId()));
            getActivity().startActivity(intent);
        }
    }

    private void setFirstLocation() {
        mTvPagerTitle.setText(mImageTitles.get(previousPosition));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(getContext());
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
            dots.add(root.findViewById(dotId));
        }
        return dots;
    }

    private void initView(){
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
                // 把当前选中的点给切换了, 还有描述信息也切换
                mTvPagerTitle.setText(mImageTitles.get(newPosition));//图片下面设置显示文本
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
        setFirstLocation();
    }
    private void autoPlayView() {
        isStop = false;
        autoPlay = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop){
                    getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void onResume() {
        super.onResume();
        autoPlayView();
    }


    @Override
    public  void onPause() {
        super.onPause();
        isStop = true;
    }
}
