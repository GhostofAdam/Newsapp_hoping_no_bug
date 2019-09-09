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
import com.example.myapplication.Adapter.SectionAdapter;
import com.example.myapplication.Entity.MySearchSuggest;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.OperateOnServer;
import com.example.myapplication.SQLite.SQLiteDbHelper;

import com.example.myapplication.SQLite.serverAvail;
import com.example.myapplication.Service.SQLservice;
import com.example.myapplication.Service.UpdateService;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;


import android.util.Log;
import android.view.View;

import android.view.WindowManager;

import android.widget.ImageButton;



import androidx.annotation.Nullable;

import androidx.core.view.GravityCompat;


import android.view.MenuItem;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.stephentuso.welcome.WelcomeHelper;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

import static cn.bingoogolapple.badgeview.BGAExplosionAnimator.ANIM_DURATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SectionAdapter sectionAdapter;
    private ViewPager viewPager;
    private SmartTabLayout tabs;
    private FloatingSearchView mSearchView;
    private ImageButton channelTags;
    private String mLastQuery="";
    private ColorDrawable mDimDrawable;
    private View mDimSearchViewBackground;
    private WelcomeHelper welcomeScreen;
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton actionEnable;
    private FloatingActionButton action_a;
    private FloatingActionButton action_b;
    private FloatingActionButton action_c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* DEBUG */
        SQLiteDbHelper helper = new SQLiteDbHelper(getApplicationContext());
        //new OperateOnServer().uploadNews(helper.getWritableDatabase(), "1", "1");
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null) {
            welcomeScreen = new WelcomeHelper(this, myWelcomeActivity.class);
            welcomeScreen.forceShow();
        }

        //welcomeScreen.forceShow();

        final User user = (User)getApplication();
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
        isConnectIsNomarl();
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setEnabled(false);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverAvail server = new serverAvail();
                if(!server.test()){
                    Toast.makeText(getApplicationContext(), "联网失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = (User)getApplication();
                if(user.getUsername()==null) {
                    Intent intent = new Intent(MainActivity.this, SignInorOutActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        sectionAdapter = new SectionAdapter(this,getSupportFragmentManager(),getResources().getStringArray(R.array.chanles));
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionAdapter);
        viewPager.setOffscreenPageLimit(7);
        tabs = findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
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

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
               return;
        }
        });
//        mSearchView.setMenuItemIconColor(R.attr.colorText);

        action_a = (FloatingActionButton)findViewById(R.id.action_a);
        action_b = (FloatingActionButton)findViewById(R.id.action_b);
        action_c = (FloatingActionButton)findViewById(R.id.action_c);
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        action_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.settheme(0);
                setTheme(R.style.AppTheme);
                refresh();
            }
        });
        action_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.settheme(1);
                setTheme(R.style.DayTheme);
                refresh();
            }
        });
        action_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.settheme(2);
                setTheme(R.style.NightTheme);
                refresh();
            }
        });
//        menuMultipleActions.addButton(action_a);
//        menuMultipleActions.addButton(action_b);
//        menuMultipleActions.addButton(action_c);


    }

    @Override
    protected void onResume() {
        super.onResume();
        sectionAdapter.notifyAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case -1:
                return;
            case 1:
                ArrayList<String> titles = data.getStringArrayListExtra("result");
                sectionAdapter.refreshTabPage(titles);
                tabs.setViewPager(viewPager);
                break;
            case 2:
                String []strings = data.getStringArrayExtra("result");
                User user = (User)getApplication();
                user.setUsername(strings[0]);
                user.setPassword(strings[1]);
                initUserData();
                //TextView textView=(TextView)navigationView.getHeaderView(1);
                View headView = navigationView.getHeaderView(0);
                TextView textView = headView.findViewById(R.id.user_name_show);

                textView.setText(user.getUsername());

                navigationView.getMenu().getItem(3).setEnabled(true);
                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                startService(intent);
                SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
                break;
            case 3:
                User user1 = (User)getApplication();
                sectionAdapter.updateFliter(user1.getKeyswords());
                break;
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

        if (id == R.id.nav_gallery) {
            final User user = (User)getApplication();
            if(user.getUsername()==null){
                Toast.makeText(getApplicationContext(), "请登录",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(this,CollectionsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            final User user = (User)getApplication();
            if(user.getUsername()==null){
                Toast.makeText(getApplicationContext(), "请登录",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(this,HistoryActivity.class);
            startActivity(intent);

        }
        else if(id == R.id.nav_keys){
            final User user = (User)getApplication();
            if(user.getUsername()==null){
                Toast.makeText(getApplicationContext(), "请登录",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(this,KeysWordsFilterActivity.class);
            startActivityForResult(intent,0);
        }
        else if(id==R.id.exit){
            User user = (User)getApplication();

            navigationView.getMenu().getItem(3).setEnabled(false);
            Intent intent = new Intent(MainActivity.this,UpdateService.class);
            stopService(intent);
            View headView = navigationView.getHeaderView(0);
            TextView textView = headView.findViewById(R.id.user_name_show);
            textView.setText("请登录");
            SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
            OperateOnSQLite op = new OperateOnSQLite();
            if(user.net) {
                op.insertState(helper.getWritableDatabase(), 1, user.getUsername());
                System.out.println("________________________________connected  server");
            }
            else {
                op.insertState(helper.getWritableDatabase(), 0, user.getUsername());
                System.out.println("________________________________not connected  server");
            }
            user.clear();
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
        Vector<String> strings = user.getSearch();
        for (String s:strings){
            suggestionList.add(new MySearchSuggest(s));
        }
        mSearchView.swapSuggestions(suggestionList);
    }
    private void addSearch(String s){
        User user = (User)getApplication();
        if(user.getUsername()!=null) {
            user.addSearch(s);
            Intent intent1 = new Intent(MainActivity.this, SQLservice.class);
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
    private void initUserData(){
        User user = (User)getApplication();
        SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
        OperateOnSQLite op = new OperateOnSQLite();
        OperateOnServer os = new OperateOnServer();
        if(op.getState(helper.getWritableDatabase(), user.getUsername())){
            os.downloadNews(helper.getWritableDatabase(),user.getUsername());
        }
        else{
            os.uploadNews(helper.getWritableDatabase(),user.getUsername(),user.getPassword());
        }
        op.insertState(helper.getWritableDatabase(),1,user.getUsername());
        user.initCollections(op.allNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,user.getUsername()));
        user.initHistory(op.allNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,user.getUsername()));
        user.initSearch(op.findSearch(helper.getWritableDatabase(),user.getUsername()));
        user.initFliter(op.allShields(helper.getWritableDatabase(),user.getUsername()));
    }

    private void refresh() {
        ArrayList<String>vector = new ArrayList<>();
        sectionAdapter.refreshTabPage(vector);
        recreate();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //welcomeScreen.onSaveInstanceState(outState);
        outState.putSerializable("","");
    }

    @Override
    protected void onDestroy() {
        User user = (User)getApplication();
        if(user.getUsername()!=null) {
            SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
            OperateOnSQLite op = new OperateOnSQLite();
            serverAvail serve = new serverAvail();
            if(user.net) {
                op.insertState(helper.getWritableDatabase(), 1, user.getUsername());
                System.out.println("________________________________connected  server");
            }
            else {
                op.insertState(helper.getWritableDatabase(), 0, user.getUsername());
                System.out.println("________________________________not connected  server");
            }
        }
        super.onDestroy();
    }
}
