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
import com.example.myapplication.R;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.Vector;


public class CollectionsActivity extends AppCompatActivity {
    private DeletableNewsListAdapter mAdapter;
    private RecyclerView recyclerView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Vector<News> data = (Vector<News>)intent.getSerializableExtra("data");
        setContentView(R.layout.activity_collections);
        recyclerView = findViewById(R.id.collections_list);
        mAdapter = new DeletableNewsListAdapter(data,this,null);
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
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
