package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.myapplication.Adapter.SectionAdapter;
import com.example.myapplication.R;
import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.GetWeb;
import com.example.myapplication.Utilities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
    private SearchView mSearchView;
    private Button channelTags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isConnectIsNomarl();
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals(""))
                    return false;
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("data",query);
                startActivity(intent);
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

            intent.putExtra("data",op.allNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,user.getUsername()));
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
            intent.putExtra("data",op.allNews(help.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,user.getUsername()));
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
}
