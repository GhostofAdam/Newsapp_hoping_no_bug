package com.example.myapplication.SQLite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import com.example.myapplication.Utilities.News;

public class OperateOnSQLite
{
    private SQLiteDbHelper helper;
    private SQLiteDatabase database;

    public OperateOnSQLite(Activity activity)
    {
        helper = new SQLiteDbHelper(activity.getApplicationContext());
        database = helper.getWritableDatabase();
    }

    public void insertNews(News news, String account)
    {
        ContentValues values = news2ContentValues(news, account);
        database.insert(SQLiteDbHelper.TABLE_COLLECTION, null, values);
    }

    public void deleteNews(News news, String account)
    {
        database.delete(SQLiteDbHelper.TABLE_COLLECTION, "newsID=? and account=?", new String[] {news.getNewsID(), account});
    }

    public Vector<News> allNews(String account)
    {
        Vector<News> newsList = new Vector<> ();
        News news = new News();
        Cursor cursor = database.query(SQLiteDbHelper.TABLE_COLLECTION, null, "account=?", new String[] {account}, null, null, null);
        while(cursor.moveToNext())
        {
            news.setNews(cursor.getString(cursor.getColumnIndex("newsID")),
                         cursor.getString(cursor.getColumnIndex("title")),
                         cursor.getString(cursor.getColumnIndex("content")));
            newsList.add(news);
        }
        cursor.close();
        return newsList;
    }

    public boolean insertAccount(String identity, String password)
    {
        Cursor cursor = database.query(SQLiteDbHelper.TABLE_ACCOUNT, null, "identity=?", new String[] {identity}, null, null, null);
        if(cursor.moveToNext())
        {
            cursor.close();
            return false;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(identity, password);
        database.insert(SQLiteDbHelper.TABLE_ACCOUNT, null, values);
        return true;
    }

    public void deleteNews(String identity)
    {
        database.delete(SQLiteDbHelper.TABLE_ACCOUNT, "identity=?", new String[] {identity});
    }

    private ContentValues news2ContentValues(News news, String account)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("newsID", news.getNewsID());
        contentValues.put("title", news.getTitle());
        contentValues.put("content", news.getContent());
        contentValues.put("account", account);
        return contentValues;
    }

}
