//package com.example.myapplication.SQLite;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.example.myapplication.Utilities.News;
//
//public class OperateOnSQLite
//{
//    private SQLiteDbHelper helper;
//    private SQLiteDatabase database;
//
//    public OperateOnSQLite()
//    {
//        helper = new SQLiteDbHelper(getContext());
//        database = helper.getWritableDatabase();
//    }
//
//    public void insertNews(News news)
//    {
//        ContentValues values = news2ContentValues(news);
//        database.insert(SQLiteDbHelper.TABLE_COLLECTION, null, values);
//    }
//
//    private ContentValues news2ContentValues(News news)
//    {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("newsID", news.getNewsID());
//        contentValues.put("title", news.getTitle());
//        contentValues.put("content", news.getContent());
//        return contentValues;
//    }
//
//}
