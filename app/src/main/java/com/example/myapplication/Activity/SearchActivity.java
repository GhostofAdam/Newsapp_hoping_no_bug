package com.example.myapplication.Activity;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.Entity.MySearchSuggest;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.UrlRequest;
import com.example.myapplication.Utilities.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.Vector;

public class SearchActivity extends AppCompatActivity {
    private FloatingSearchView mSearchView;
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
        mSearchView = findViewById(R.id.floating_search_view_2);
        setSearchSuggestions();
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                if(searchSuggestion.getBody().equals(""))
                    return;
                adapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25",searchSuggestion.getBody(),""),false);
                setSearchSuggestions();
                addSearch(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                if(currentQuery.equals(""))
                    return;
                adapter.notifyAdapter(new UrlRequest().urlRequest(10,"2019-08-01","2019-08-25",currentQuery,""),false);
                setSearchSuggestions();
                addSearch(currentQuery);
            }
        });
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
            }
        }});

    }

    private void setSearchSuggestions(){
        User user = (User)getApplication();
        if(user.getUsername()==null){
            return;
        }
        Vector<SearchSuggestion> suggestionList = new Vector<>();
        SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
        OperateOnSQLite op = new OperateOnSQLite();

        Vector<String> strings = op.findSearch(helper.getWritableDatabase(),user.getUsername());
        for (String s:strings){
            suggestionList.add(new MySearchSuggest(s));
        }
        mSearchView.swapSuggestions(suggestionList);
    }
    private void addSearch(String s){
        User user = (User)getApplication();
        if(user.getUsername()!=null) {
            SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
            OperateOnSQLite op = new OperateOnSQLite();
            op.insertSearch(helper.getWritableDatabase(),s,user.getUsername());
        }
    }
}
