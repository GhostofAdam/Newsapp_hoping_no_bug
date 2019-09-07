package com.example.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.OperateOnServer;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.SQLite.serverAvail;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;

public class SQLservice extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent _intent, int flags, int startId) {
        final int flag = (Integer)_intent.getSerializableExtra("flag");
        final Intent intent = _intent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDbHelper helper = SQLiteDbHelper.getInstance(getApplicationContext());
                OperateOnSQLite op = new OperateOnSQLite();
                OperateOnServer os = new OperateOnServer();
                User user = (User)getApplication();
                serverAvail  server = new serverAvail();
            switch(flag) {
                case User.ADD_COLLECTION: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.insertNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername());
                   if(server.test())
                        os.insertNews(SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername(),user.getPassword());
                    break;
                }
                case User.ADD_HISTORY: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.insertNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,news,user.getUsername());
                    if(server.test())
                        os.insertNews(SQLiteDbHelper.TABLE_SEEN,news,user.getUsername(),user.getPassword());
                    break;
                }
                case User.DELETE_COLLECTION: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.deleteNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername());
                    if(server.test())
                        os.deleteNews(SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername());
                    break;
                }
                case User.DELETE_HISTORY: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.deleteNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,news,user.getUsername());
                    if(server.test())
                        os.deleteNews(SQLiteDbHelper.TABLE_SEEN,news,user.getUsername());
                    break;
                }

                case User.ADD_ACCOUNT:{
                    String[] strings = (String[])intent.getSerializableExtra("data");
                    os.insertAccount(strings[0],strings[1]);
                    break;
                }
                case User.ADD_FLITER:{
                    String tag = (String)intent.getSerializableExtra("data");
                    os.insertShield(tag,user.getUsername(),user.getPassword());
                    op.insertShield(helper.getWritableDatabase(),tag,user.getUsername());
                    break;
                }
                case User.DELETE_FLITER:{
                    String tag = (String)intent.getSerializableExtra("data");
                    os.deleteShield(tag,user.getUsername());
                    op.deleteShield(helper.getWritableDatabase(),tag,user.getUsername());
                    break;
                }
                case User.ADD_SEARCH:{
                    String s = (String)intent.getSerializableExtra("data");
                    op.insertSearch(helper.getWritableDatabase(),s,user.getUsername());
                    break;
                }
                default:
                    break;
        }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
