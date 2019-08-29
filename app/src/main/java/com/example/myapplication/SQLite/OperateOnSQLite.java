package com.example.myapplication.SQLite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import com.example.myapplication.Utilities.News;

public class OperateOnSQLite
{
    /* insert news */
    public void insertNews(SQLiteDatabase db, News news, String identity)
    {
        db.insert(SQLiteDbHelper.TABLE_COLLECTION, null, news2ContentValues(news, identity));
    }

    private ContentValues news2ContentValues(News news, String identity)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("newsID", news.getNewsID());
        contentValues.put("title", news.getTitle());
        contentValues.put("content", news.getContent());
        contentValues.put("publisher", news.getPublisher());
        contentValues.put("publishTime", news.getPublishTime());
        contentValues.put("identity", identity);
        return contentValues;
    }

    /* delete news */
    public void deleteNews(SQLiteDatabase db, News news, String identity)
    {
        db.delete(SQLiteDbHelper.TABLE_COLLECTION, "newsID=? and identity=?", new String[] {news.getNewsID(), identity});
    }

    /* return all news in this identity */
    public Vector<News> allNews(SQLiteDatabase db, String identity)
    {
        Vector<News> newsList = new Vector<> ();
        News news = new News();
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_COLLECTION, null, "identity=?", new String[] {identity}, null, null, null);
        while(cursor.moveToNext())
        {
            news.setNews(cursor.getString(cursor.getColumnIndex("newsID")),
                         cursor.getString(cursor.getColumnIndex("title")),
                         cursor.getString(cursor.getColumnIndex("content")),
                         cursor.getString(cursor.getColumnIndex("publisher")),
                         cursor.getString(cursor.getColumnIndex("publishTime")));
            newsList.add(news);
        }
        cursor.close();
        return newsList;
    }

    public boolean findNews(SQLiteDatabase db, String newsID, String identity)
    {
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_COLLECTION, null, "newsID=? and identity=?", new String[] {newsID, identity}, null, null, null);
        if(cursor.moveToNext())
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /* insert new account, return true if succeed, false if identity has existed */
    public boolean insertAccount(SQLiteDatabase db, String identity, String password)
    {
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_ACCOUNT, null, "identity=?", new String[] {identity}, null, null, null);
        if(cursor.moveToNext())
        {
            cursor.close();
            return false;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(identity, password);
        db.insert(SQLiteDbHelper.TABLE_ACCOUNT, null, values);
        return true;
    }

    public void deleteAccount(SQLiteDatabase db, String identity)
    {
        db.delete(SQLiteDbHelper.TABLE_ACCOUNT, "identity=?", new String[] {identity});
    }

}
