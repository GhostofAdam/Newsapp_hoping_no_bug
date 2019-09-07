package com.example.myapplication.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import com.example.myapplication.Utilities.News;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;
import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class OperateOnSQLite {

    public void clearTables(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_COLLECTION);
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_SEEN);
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_STATE);
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_SHIELD);
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_SEARCH);
    }

    public void insertState(SQLiteDatabase db, int avail, String identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("avail", avail);
        contentValues.put("identity", identity);
        db.insert(SQLiteDbHelper.TABLE_STATE, null, contentValues);
    }

    public boolean getState(SQLiteDatabase db) {
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_STATE, null, null, null, null, null, null, null);
        if(cursor.getCount() > 0)
        {
            cursor.moveToLast();
            int judge = cursor.getInt(cursor.getColumnIndex("avail"));
            cursor.close();
            return judge == 1;
        }
        return true;
    }

    public void insertShield(SQLiteDatabase db, String word, String identity)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sole", identity + word);
        contentValues.put("word", word);
        contentValues.put("identity", identity);
        db.insertWithOnConflict(SQLiteDbHelper.TABLE_SHIELD, null, contentValues, CONFLICT_IGNORE);
    }

    public void deleteShield(SQLiteDatabase db, String word, String identity)
    {
        db.delete(SQLiteDbHelper.TABLE_SHIELD, "sole=?", new String[] {identity + word});
    }

    public Vector<String> allShields(SQLiteDatabase db, String identity)
    {
        Vector<String> list = new Vector<>();
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_SHIELD, null, "identity=?", new String[] {identity}, null, null, null);
        while (cursor.moveToNext())
        {
            list.add(cursor.getString(cursor.getColumnIndex("word")));
        }
        cursor.close();
        return list;
    }

    public void insertNews(SQLiteDatabase db, String tableName, News news, String identity) {
        if (tableName.equals(SQLiteDbHelper.TABLE_COLLECTION))
        {
            db.insert(tableName, null, news2ContentValues(news, identity));
        }
        else if (tableName.equals(SQLiteDbHelper.TABLE_SEEN))
        {
            db.insertWithOnConflict(tableName, null, news2ContentValues(news, identity), CONFLICT_IGNORE);
        }
    }

    private ContentValues news2ContentValues(News news, String identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sole", identity + news.getNewsID());
        contentValues.put("newsID", news.getNewsID());
        contentValues.put("title", news.getTitle());
        contentValues.put("content", news.getContent());
        contentValues.put("publisher", news.getPublisher());
        contentValues.put("publishTime", news.getPublishTime());
        contentValues.put("identity", identity);
        return contentValues;
    }

    public void deleteNews(SQLiteDatabase db, String tableName, News news, String identity) {
        db.delete(tableName, "sole=?", new String[]{identity + news.getNewsID()});
    }

    void deleteNewsOfAccount(SQLiteDatabase db, String tableName, String identity) {
        db.delete(tableName, "identity=?", new String[]{identity});
    }

    void deleteShieldOfAccount(SQLiteDatabase db, String identity)
    {
        db.delete(SQLiteDbHelper.TABLE_SHIELD, "identity=?", new String[] {identity});
    }

    public Vector<News> allNews(SQLiteDatabase db, String tableName, String identity) {
        Vector<News> newsList = new Vector<>();
        Cursor cursor = db.query(tableName, null, "identity=?", new String[]{identity}, null, null, null);
        while (cursor.moveToNext()) {
            News news = new News();
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

    public void insertSearch(SQLiteDatabase db, String word, String identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("word", word);
        contentValues.put("identity", identity);
        db.insertWithOnConflict(SQLiteDbHelper.TABLE_SEARCH, null, contentValues, CONFLICT_REPLACE);
    }

    public Vector<String> findSearch(SQLiteDatabase db, String identity) {
        Vector<String> words = new Vector<>();
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_SEARCH, null, "identity=?", new String[]{identity}, null, null, "id desc");
        while (cursor.moveToNext()) {
            words.add(cursor.getString(cursor.getColumnIndex("word")));
        }
        cursor.close();
        return words;
    }
}

