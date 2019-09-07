package com.example.myapplication.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.Utilities.News;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class URL
{
    static String port = "8006";
    static String url = "http://166.111.5.239:" + port + "/app/";
    static String url_avail = "http://166.111.5.239:" + port + "/test/";
    static String url_us = "http://166.111.5.239:" + port + "/uploadseen/";
    static String url_uc = "http://166.111.5.239:" + port + "/uploadcollection/";
    static String url_ds = "http://166.111.5.239:" + port + "/downloadseen/";
    static String url_dc = "http://166.111.5.239:" + port + "/downloadcollection/";
}

public class OperateOnServer
{
    private static final String TAG = "OperateOnServer";
    
    static private OkHttpClient client = new OkHttpClient();
    private int isaccount_num;
    private int isright_num;

    private FormBody.Builder createBuilder(String id, String table, String doing)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", id);
        builder.add("table", table);
        builder.add("doing", doing);
        return builder;
    }

    private void _downloadNews(SQLiteDatabase db, String tableName, String identity)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("identity", identity);
        RequestBody formBody = builder.build();
        Request request = tableName.equals(SQLiteDbHelper.TABLE_COLLECTION) ? new Request.Builder().url(URL.url_dc).post(formBody).build()
                                                                            : new Request.Builder().url(URL.url_ds).post(formBody).build();
        News[] newsList = null;
        Response response;
        try
        {
            response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                String result = response.body().string();
                if(!result.equals("[]"))
                {
                    newsList = new Gson().fromJson(result, myNewsList.class).list;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db.beginTransaction();
        if(newsList != null)
        {
            try
            {
                for (News a : newsList)
                {
                    ContentValues values = new ContentValues();
                    String sole = a.getSole();
                    String id = a.getNewsID();
                    values.put("sole", sole);
                    values.put("newsID", id);
                    values.put("title", a.getTitle());
                    values.put("content", a.getContent());
                    values.put("publisher", a.getPublisher());
                    values.put("publishTime", a.getPublishTime());
                    values.put("identity", sole.replaceAll(id, ""));
                    db.insert(tableName, null, values);
                }
                db.setTransactionSuccessful();
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            finally
            {
                db.endTransaction();
            }
        }
    }

    private void _uploadNews(SQLiteDatabase db, String tableName, String identity, String password)
    {
        JSONArray array = new JSONArray();
        Cursor cursor = db.query(tableName, null, "identity=?", new String[] {identity}, null, null, null);
        while(cursor.moveToNext())
        {
            JSONObject object = new JSONObject();
            try
            {
                object.put("sole", cursor.getString(cursor.getColumnIndex("sole")));
                object.put("newsID", cursor.getString(cursor.getColumnIndex("newsID")));
                object.put("title", cursor.getString(cursor.getColumnIndex("title")));
                object.put("content", cursor.getString(cursor.getColumnIndex("content")));
                object.put("publisher", cursor.getString(cursor.getColumnIndex("publisher")));
                object.put("publishTime", cursor.getString(cursor.getColumnIndex("publishTime")));
                object.put("identity", identity);
                object.put("password", password);
            }
            catch(org.json.JSONException e)
            {
                e.printStackTrace();
            }
            array.put(object);
        }
        cursor.close();
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), array.toString());
        Request request = tableName.equals(SQLiteDbHelper.TABLE_COLLECTION) ? new Request.Builder().url(URL.url_uc).post(body).build() : new Request.Builder().url(URL.url_us).post(body).build();
        try
        {
            Response response = client.newCall(request).execute();
            response.body().close();
        }
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void _insertNews(String tableName, News news, String identity, String password)
    {
        FormBody.Builder builder = newsToBuilder(createBuilder("0", tableName, "add"), news, identity, password);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        try
        {
            Response response = client.newCall(request).execute();
            response.body().close();
        }
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private FormBody.Builder newsToBuilder(FormBody.Builder builder, News news, String identity, String password)
    {
        builder.add("sole", identity + news.getNewsID());
        builder.add("newsID", news.getNewsID());
        builder.add("title", news.getTitle());
        builder.add("content", news.getContent());
        builder.add("publisher", news.getPublisher());
        builder.add("publishTime", news.getPublishTime());
        builder.add("identity", identity);
        builder.add("password", password);
        return builder;
    }

    private void _deleteNews(String tableName, News news, String identity)
    {
        FormBody.Builder builder = createBuilder("0", tableName, "delete");
        builder.add("sole", identity + news.getNewsID());
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        try
        {
            Response response = client.newCall(request).execute();
            response.body().close();
        }
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void _insertAccount(String identity, String password)
    {
        FormBody.Builder builder = createBuilder("0", "account", "add");
        builder.add("identity", identity);
        builder.add("password", password);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        try
        {
            Response response = client.newCall(request).execute();
            response.body().close();
        }
        catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void _isAccount(String identity)
    {
        FormBody.Builder builder = createBuilder("1", "account", "is");
        builder.add("identity", identity);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                String result = response.body().string();
                if(result.equals("yes"))
                {
                    isaccount_num = 1;
                }
                else if(result.equals("no"))
                {
                    isaccount_num = 0;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(response != null)
            {
                response.body().close();
            }
        }
    }

    private void _isRightPassword(String identity, String password)
    {
        FormBody.Builder builder = createBuilder("1","account", "right");
        builder.add("identity", identity);
        builder.add("password", password);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            Thread.sleep(1000);
            Log.d(TAG, "_isRightPassword: " + response);
            if(response.code() == 200)
            {
                String result = response.body().string();
                Log.d(TAG, "_isRightPassword: " + result);
                if(result.equals("yes"))
                {
                    isright_num = 1;
                }
                else if(result.equals("no"))
                {
                    isright_num = 0;
                }
            } else {
                Log.d(TAG, "_isRightPassword: ");
            }
        }
        catch(IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(response != null)
            {
                response.body().close();
            }
        }
    }

    public void downloadNews(final SQLiteDatabase db, final String identity)
    {
        OperateOnSQLite op = new OperateOnSQLite();
        op.deleteNewsOfAccount(db, SQLiteDbHelper.TABLE_COLLECTION, identity);
        op.deleteNewsOfAccount(db, SQLiteDbHelper.TABLE_SEEN, identity);
        new Thread(new Runnable() {
            @Override
            public void run() {
                _downloadNews(db, SQLiteDbHelper.TABLE_COLLECTION, identity);
                _downloadNews(db, SQLiteDbHelper.TABLE_SEEN, identity);
            }
        }).start();
    }

    public void uploadNews(final SQLiteDatabase db, final String identity, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _uploadNews(db, SQLiteDbHelper.TABLE_COLLECTION, identity, password);
                _uploadNews(db, SQLiteDbHelper.TABLE_SEEN, identity, password);
            }
        }).start();
    }

    public void insertNews(final String tableName, final News news, final String identity, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _insertNews(tableName, news, identity, password);
            }
        }).start();
    }

    public void deleteNews(final String tableName, final News news, final String identity)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _deleteNews(tableName, news, identity);
            }
        }).start();
    }

    public void insertAccount(final String identity, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _insertAccount(identity, password);
            }
        }).start();
    }

    public boolean isAccount(final String identity)
    {
        isaccount_num = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                _isAccount(identity);
            }
        }).start();
        while (isaccount_num == -1)
        {
            System.out.println("fuck");
        }
        return isaccount_num == 1;
    }

    public boolean isRightPassword(final String identity, final String password)
    {
        isright_num = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                _isRightPassword(identity, password);
            }
        }).start();
        while (isright_num == -1)
        {
            System.out.println("fuck");
        }
        return isright_num == 1;
    }

}

class myNewsList
{
    News[] list = null;
}