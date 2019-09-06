package com.example.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.OperateOnServer;
import com.example.myapplication.SQLite.SQLiteDbHelper;
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
            switch(flag) {
                case User.ADD_COLLECTION: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.insertNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername());
                    os.insertNews(SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername(),user.getPassword());
                }
                case User.ADD_HISTORY: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.insertNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,news,user.getUsername());
                    os.insertNews(SQLiteDbHelper.TABLE_SEEN,news,user.getUsername(),user.getPassword());
                }
                case User.DELETE_COLLECTION: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.deleteNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername());
                    os.deleteNews(SQLiteDbHelper.TABLE_COLLECTION,news,user.getUsername());
                }
                case User.DELETE_HISTORY: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.deleteNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,news,user.getUsername());
                    os.deleteNews(SQLiteDbHelper.TABLE_SEEN,news,user.getUsername());
                }
                case User.FIND_COLLECTION: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.findNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_COLLECTION,news.getNewsID(),user.getUsername());

                }
                case User.FIND_HISTORY: {
                    News news = (News)intent.getSerializableExtra("data");
                    op.findNews(helper.getWritableDatabase(),SQLiteDbHelper.TABLE_SEEN,news.getNewsID(),user.getUsername());
                }
                case User.ADD_SEARCH: {
                    String word = (String)intent.getSerializableExtra("data");
                    op.insertSearch(helper.getWritableDatabase(),word,user.getUsername());
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
