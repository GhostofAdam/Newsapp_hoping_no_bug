package com.example.myapplication.Activity;
import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Utilities.UrlRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

public class SearchActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private NewsListAdapter adapter;
    private RecyclerView recyclerView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String query = (String) intent.getSerializableExtra("data");
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsListAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25",query,""),this,null);
        recyclerView.setAdapter(adapter);
        back = findViewById(R.id.search_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSearchView = findViewById(R.id.searchView2);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25",query,""),false);
                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){

                }else{

                }
                return false;
            }
        });
    }
}
