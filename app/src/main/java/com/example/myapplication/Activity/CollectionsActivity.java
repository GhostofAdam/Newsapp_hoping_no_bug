package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Adapter.DeletableNewsListAdapter;
import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Vector;


public class CollectionsActivity extends AppCompatActivity {
    private DeletableNewsListAdapter mAdapter;
    private RecyclerView recyclerView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        Intent intent = getIntent();
        ArrayList<News> data = (ArrayList<News>)intent.getSerializableExtra("data");
        Vector<News> newslist = new Vector<News>();
        newslist.addAll(data);
        mAdapter = new DeletableNewsListAdapter(newslist,this,null);

        recyclerView = findViewById(R.id.collections_list);
        mAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CollectionsActivity.this, NewsDetailActivity.class);
                intent.putExtra("news",mAdapter.Dataset.get(position));
                startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new NewsListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(CollectionsActivity.this, NewsDetailActivity.class);
                intent.putExtra("news",mAdapter.Dataset.get(position));
                startActivity(intent);
            }
        });
        setUpRecyclerView();
        back = findViewById(R.id.collections_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
