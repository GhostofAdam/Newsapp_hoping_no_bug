package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.Adapter.DeletableNewsListAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Utilities.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;


public class HistoryActivity extends AppCompatActivity {
    private DeletableNewsListAdapter mAdapter;
    private RecyclerView recyclerView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.history_list);
        mAdapter = new DeletableNewsListAdapter(10);
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

}
