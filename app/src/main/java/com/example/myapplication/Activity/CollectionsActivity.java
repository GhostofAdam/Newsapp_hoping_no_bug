package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
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
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Service.SQLservice;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.SpacesItemDecoration;
import com.example.myapplication.Utilities.User;

import java.util.ArrayList;
import java.util.Vector;


public class CollectionsActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_collections);
        Vector<News> newslist = new Vector<News>();
        mAdapter = new DeletableNewsListAdapter(newslist,this,null);

        recyclerView = findViewById(R.id.collections_list);
        int space = 8;
        float rad = 20;
        recyclerView.addItemDecoration(new SpacesItemDecoration(space,rad));
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
        mAdapter.setNoImage();
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
                ItemTouchHelper(new  ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                User user = (User)getApplication();
                user.deleteCollection(mAdapter.Dataset.get(position));
                Intent intent1 = new Intent(CollectionsActivity.this, SQLservice.class);
                intent1.putExtra("flag",User.DELETE_COLLECTION);
                intent1.putExtra("data",mAdapter.Dataset.get(position));
                startService(intent1);
                mAdapter.deleteItem(position);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

        });




        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final User user = (User)getApplication();
        Vector<News> newsList =  user.getCollections();
        mAdapter.notifyAdapter(newsList,false);
    }
}
