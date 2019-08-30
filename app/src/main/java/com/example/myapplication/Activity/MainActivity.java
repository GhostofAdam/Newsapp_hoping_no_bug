package com.example.myapplication.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.Adapter.SectionAdapter;
import com.example.myapplication.Entity.MySearchSuggest;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.GetWeb;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import redis.clients.jedis.Jedis;

import static cn.bingoogolapple.badgeview.BGAExplosionAnimator.ANIM_DURATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SectionAdapter sectionAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;
    private FloatingSearchView mSearchView;
    private Button channelTags;
    private String mLastQuery="";
    private ColorDrawable mDimDrawable;
    private View mDimSearchViewBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Jedis jedis = new Jedis();
        super.onCreate(savedInstanceState);
        isConnectIsNomarl();
        setContentView(R.layout.activity_main);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sectionAdapter = new SectionAdapter(this,getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        channelTags = findViewById(R.id.tab_button);
        channelTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.tab_button){
                    Intent intent = new Intent(MainActivity.this,ChannelTagsActivity.class);
                    intent.putExtra("data",sectionAdapter.getTabTitles());

                    startActivityForResult(intent,0);
            }
        }});
        mDimSearchViewBackground = findViewById(R.id.dim_background);
        mSearchView = findViewById(R.id.floating_search_view);

        setmSearchView();
        mSearchView.attachNavigationDrawerToMenuButton(drawer);
        setSearchSuggestions();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case -1:
                return;
            case 1:
                ArrayList<String> titles = data.getStringArrayListExtra("result");
                this.sectionAdapter.refreshTabPage(titles);
                break;
            case 2:
                String []strings = data.getStringArrayExtra("result");
                final User user = (User)getApplication();
                user.setUsername(strings[0]);
                user.setPassword(strings[1]);
                default:
                    break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(this,SignInorOutActivity.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_gallery) {
            final User user = (User)getApplication();
            if(user.getUsername()==null){
                Toast.makeText(getApplicationContext(), "请登陆",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(this,CollectionsActivity.class);
            OperateOnSQLite op = new OperateOnSQLite();
            SQLiteDbHelper help = SQLiteDbHelper.getInstance(getApplicationContext());
            Vector<News> newsList = op.allNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,user.getUsername());
            intent.putExtra("data",newsList);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            final User user = (User)getApplication();
            if(user.getUsername()==null){
                Toast.makeText(getApplicationContext(), "请登陆",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(this,HistoryActivity.class);
            OperateOnSQLite op = new OperateOnSQLite();
            SQLiteDbHelper help = SQLiteDbHelper.getInstance(getApplicationContext());
            Vector<News> newsList = op.allNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,user.getUsername());
            intent.putExtra("data",newsList);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String intentName = info.getTypeName();
            Log.i("通了没！", "当前网络名称：" + intentName);
            return true;
        } else {
            Log.i("通了没！", "没有可用网络");
            return false;
        }
    }
    private void setSearchSuggestions(){
        User user = (User)getApplication();
        if(user.getUsername()==null){
            return;
        }
        Vector<SearchSuggestion>suggestionList = new Vector<>();
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
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("data",searchSuggestion.getBody());
                addSearch(searchSuggestion.getBody());
                startActivity(intent);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                if(currentQuery.equals(""))
                    return;
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("data",currentQuery);
                addSearch(currentQuery);
                startActivity(intent);
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
