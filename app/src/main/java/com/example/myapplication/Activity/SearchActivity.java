package com.example.myapplication.Activity;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.myapplication.Adapter.NewsListAdapter;
import com.example.myapplication.Entity.MySearchSuggest;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.SQLite.serverAvail;
import com.example.myapplication.Service.SQLservice;
import com.example.myapplication.Utilities.DateUtility;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.UrlRequest;
import com.example.myapplication.Utilities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import static cn.bingoogolapple.badgeview.BGAExplosionAnimator.ANIM_DURATION;

public class SearchActivity extends AppCompatActivity {
    private FloatingSearchView mSearchView;
    private NewsListAdapter adapter;
    private RecyclerView recyclerView;
    private String mLastQuery="";
    private ColorDrawable mDimDrawable;
    private View mDimSearchViewBackground;
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
        Intent intent = getIntent();
        String query = (String) intent.getSerializableExtra("data");
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DateUtility dateUtility = new DateUtility();
        String today = dateUtility.getDateString(dateUtility.getCurrent());
        serverAvail server = new serverAvail();
        if(server.test())
            adapter = new NewsListAdapter(new UrlRequest().urlRequest(10,"",today,query,""),this,null);
        else
            adapter = new NewsListAdapter(new Vector<News>(),this,null);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, NewsDetailActivity.class);
                intent.putExtra("news",adapter.Dataset.get(position));
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new NewsListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, NewsDetailActivity.class);
                intent.putExtra("news",adapter.Dataset.get(position));
                startActivity(intent);
            }
        });
        mSearchView = findViewById(R.id.floating_search_view_2);
        mDimSearchViewBackground = findViewById(R.id.dim_background_2);
        setmSearchView();
        setSearchSuggestions();
        mSearchView.setTranslationZ(4);
        mSearchView.invalidate();
    }

    private void setSearchSuggestions(){
        User user = (User)getApplication();
        if(user.getUsername()==null){
            return;
        }
        Vector<SearchSuggestion> suggestionList = new Vector<>();
        Vector<String>strings = user.getSearch();
        for (String s:strings){
            suggestionList.add(new MySearchSuggest(s));
        }
        mSearchView.swapSuggestions(suggestionList);
    }
    private void addSearch(String s){
        User user = (User)getApplication();
        if(user.getUsername()!=null) {
            user.addSearch(s);
            Intent intent1 = new Intent(SearchActivity.this, SQLservice.class);
            intent1.putExtra("flag",User.ADD_SEARCH);
            intent1.putExtra("data",s);
            startService(intent1);
        }
    }
    private void setmSearchView(){
        mDimDrawable = new ColorDrawable(Color.BLACK);
        mDimDrawable.setAlpha(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDimSearchViewBackground.setBackground(mDimDrawable);
        }else {
            mDimSearchViewBackground.setBackgroundDrawable(mDimDrawable);
        }
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                if(searchSuggestion.getBody().equals(""))
                    return;
                mLastQuery = searchSuggestion.getBody();
                DateUtility dateUtility = new DateUtility();
                String today = dateUtility.getDateString(dateUtility.getCurrent());
                serverAvail server = new serverAvail();
                if(server.test())
                    adapter.notifyAdapter(new UrlRequest().urlRequest(10,"",today,searchSuggestion.getBody(),""),false);
                setSearchSuggestions();
                addSearch(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                if(currentQuery.equals(""))
                    return;
                mLastQuery = currentQuery;
                DateUtility dateUtility = new DateUtility();
                String today = dateUtility.getDateString(dateUtility.getCurrent());
                serverAvail server = new serverAvail();
                if(server.test())
                    adapter.notifyAdapter(new UrlRequest().urlRequest(10,"",today,currentQuery,""),false);
                setSearchSuggestions();
                addSearch(currentQuery);
            }
        });


        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

            }
        });
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                int headerHeight = getResources().getDimensionPixelOffset(R.dimen.sliding_search_view_header_height);
                ObjectAnimator anim = ObjectAnimator.ofFloat(mSearchView, "translationY",
                        headerHeight, 0);
                anim.setDuration(350);
                fadeDimBackground(0, 150, null);
                anim.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //show suggestions when search bar gains focus (typically history suggestions)
                        setSearchSuggestions();
                    }
                });
                anim.start();
            }

            @Override
            public void onFocusCleared() {
                int headerHeight = getResources().getDimensionPixelOffset(R.dimen.sliding_search_view_header_height);
                ObjectAnimator anim = ObjectAnimator.ofFloat(mSearchView, "translationY",
                        0, headerHeight);
                anim.setDuration(350);
                anim.start();
                fadeDimBackground(150, 0, null);
                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);
                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());
            }
        });
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                onBackPressed();
            }
        });
    }


    private void fadeDimBackground(int from, int to, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                mDimDrawable.setAlpha(value);
            }
        });
        if(listener != null) {
            anim.addListener(listener);
        }
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }
}
