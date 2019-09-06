package com.example.myapplication.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import com.example.myapplication.Utilities.News;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;
import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class OperateOnSQLite
{
    /* tableName: selected from SQLiteDbHelper.TABLE_ACCOUNT, SQLiteDbHelper.TABLE_COLLECTION, SQLiteDbHelper.TABLE_SEEN */
    public void clearTables(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_ACCOUNT);
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_COLLECTION);
        db.execSQL("DELETE FROM " + SQLiteDbHelper.TABLE_SEEN);
    }

    public void insertState(SQLiteDatabase db, int avail)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("avail", avail);
        db.insert(SQLiteDbHelper.TABLE_STATE, null, contentValues);
    }

    public boolean getState(SQLiteDatabase db)
    {
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_STATE, null, null, null, null, null, null, null);
        cursor.moveToLast();
        int judge = cursor.getInt(cursor.getColumnIndex("avail"));
        cursor.close();
        return judge == 1;
    }

    /* insert news */
    public void insertNews(SQLiteDatabase db, String tableName, News news, String identity)
    {
        if(tableName.equals(SQLiteDbHelper.TABLE_COLLECTION))
        {
            db.insert(tableName, null, news2ContentValues(news, identity));
        }
        else if(tableName.equals(SQLiteDbHelper.TABLE_SEEN))
        {
            db.insertWithOnConflict(tableName, null, news2ContentValues(news, identity), CONFLICT_IGNORE);
        }
    }

    private ContentValues news2ContentValues(News news, String identity)
    {
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

    /* delete news */
    public void deleteNews(SQLiteDatabase db, String tableName, News news, String identity)
    {
        db.delete(tableName, "sole=?", new String[] {identity + news.getNewsID()});
    }

    public void deleteNewsOfAccount(SQLiteDatabase db, String tableName, String identity)
    {
        db.delete(tableName, "identity=?", new String[] {identity});
    }

    /* return all news in this identity */
    public Vector<News> allNews(SQLiteDatabase db, String tableName, String identity)
    {
        Vector<News> newsList = new Vector<>();
        Cursor cursor = db.query(tableName, null, "identity=?", new String[] {identity}, null, null, null);
        while(cursor.moveToNext())
        {
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

    public boolean findNews(SQLiteDatabase db, String tableName, String newsID, String identity)
    {
        Cursor cursor = db.query(tableName, null, "sole=?", new String[] {identity + newsID}, null, null, null);
        if(cursor.moveToNext())
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void insertSearch(SQLiteDatabase db, String word, String identity)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("word", word);
        contentValues.put("identity", identity);
        db.insertWithOnConflict(SQLiteDbHelper.TABLE_SEARCH, null, contentValues, CONFLICT_REPLACE);
    }

    public void deleteSearch(SQLiteDatabase db, String word, String identity)
    {
        db.delete(SQLiteDbHelper.TABLE_SEARCH, "word=? and identity=?", new String[] {word, identity});
    }

    public Vector<String> findSearch(SQLiteDatabase db, String identity)
    {
        Vector<String> words = new Vector<>();
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_SEARCH, null, "identity=?", new String[] {identity}, null, null, "id desc");
        while(cursor.moveToNext())
        {
            words.add(cursor.getString(cursor.getColumnIndex("word")));
        }
        return words;
    }

    /* insert new account, return true if succeed, false if identity has existed */
    public void insertAccount(SQLiteDatabase db, String identity, String password)
    {
        ContentValues values = new ContentValues();
        values.put("identity", identity);
        values.put("password", password);
        db.insert(SQLiteDbHelper.TABLE_ACCOUNT, null, values);
    }

    public void deleteAccount(SQLiteDatabase db, String identity)
    {
        db.delete(SQLiteDbHelper.TABLE_ACCOUNT, "identity=?", new String[] {identity});
    }

    /* if true, the identity has existed; else, not */
    public boolean isAccount(SQLiteDatabase db, String identity)
    {
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_ACCOUNT, null, "identity=?", new String[] {identity}, null, null, null);
        if(cursor.moveToNext())
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /* if true, the password is right; else, not */
    public boolean isRightPassword(SQLiteDatabase db, String identity, String password)
    {
        Cursor cursor = db.query(SQLiteDbHelper.TABLE_ACCOUNT, null, "identity=? and password=?", new String[] {identity, password}, null, null, null);
        if(cursor.moveToNext())
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
