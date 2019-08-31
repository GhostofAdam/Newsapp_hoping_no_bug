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
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;

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
        Vector<News> newslist = new Vector<News>();
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
                ItemTouchHelper(new  ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
                OperateOnSQLite op  = new OperateOnSQLite();
                User user = (User)getApplication();
                op.deleteNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,mAdapter.Dataset.get(position),user.getUsername());
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
        OperateOnSQLite op = new OperateOnSQLite();
        SQLiteDbHelper help = SQLiteDbHelper.getInstance(getApplicationContext());
        Vector<News> newsList = op.allNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,user.getUsername());
        mAdapter.notifyAdapter(newsList,false);
    }
}
