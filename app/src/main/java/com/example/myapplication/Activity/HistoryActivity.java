package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.SwipeToDeleteCallback;

import com.example.myapplication.Utilities.User;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.Vector;


public class HistoryActivity extends AppCompatActivity {
    private NewsListAdapter mAdapter;
    private RecyclerView recyclerView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = (User)getApplication();
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
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.history_list);
        Vector<News> newslist = new Vector<News>();
        mAdapter = new NewsListAdapter(newslist,this,null);
        mAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HistoryActivity.this, NewsDetailActivity.class);
                intent.putExtra("news",mAdapter.Dataset.get(position));
                startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new NewsListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(HistoryActivity.this, NewsDetailActivity.class);
                intent.putExtra("news",mAdapter.Dataset.get(position));
                startActivity(intent);
            }
        });
        mAdapter.setNoImage();

        setUpRecyclerView();
        back = findViewById(R.id.history_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setUpRecyclerView() {
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final User user = (User)getApplication();
        Vector<News> newsList = user.getHistory();
        mAdapter.notifyAdapter(newsList,false);
    }
}
