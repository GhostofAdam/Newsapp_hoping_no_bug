package com.example.myapplication.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Utilities.News;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class URL
{
    static String url = "http://166.111.5.239:8001/app/";
    static String _url = "http://166.111.5.239:8001/data/";
}

///* DEBUG */
//SQLiteDbHelper helper = new SQLiteDbHelper(getApplicationContext());
//    new OperateOnSQLite().clearTables(helper.getWritableDatabase());
//            new OperateOnServer().downloadAll(helper.getWritableDatabase());

public class OperateOnServer
{
    static private OkHttpClient client = new OkHttpClient();
    public Vector<News> allnews;
    public boolean isFindNews;
    public boolean isaccount;
    private int isaccount_num;
    public boolean isright;
    private int isright_num;

    private FormBody.Builder createBuilder(String id, String table, String doing)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", id);
        builder.add("table", table);
        builder.add("doing", doing);
        return builder;
    }

    private void _downloadAccount(SQLiteDatabase db) {
        FormBody.Builder builder = createBuilder("1", SQLiteDbHelper.TABLE_ACCOUNT, "download");
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        Accounts[] myaccountList = null;
        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                String result = response.body().string();
                if(!result.equals("[]"))
                {
                    myaccountList = new Gson().fromJson(result, myAccountList.class).list;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db.beginTransaction();
        if(myaccountList != null)
        {
            try
            {
                for (Accounts a : myaccountList)
                {
                    ContentValues values = new ContentValues();
                    values.put("identity", a.identity);
                    values.put("password", a.password);
                    db.insert(SQLiteDbHelper.TABLE_ACCOUNT, null, values);
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

    private void _downloadNews(SQLiteDatabase db, String tableName, String identity)
    {
        FormBody.Builder builder = createBuilder("1", tableName, "download");
        builder.add("identity", identity);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        News[] mynewsList = null;
        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                String result = response.body().string();
                if(!result.equals("[]"))
                {
                    mynewsList = new Gson().fromJson(result, myNewsList.class).list;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        db.beginTransaction();
        if(mynewsList != null)
        {
            try
            {
                for (News a : mynewsList)
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
        Request request = new Request.Builder().url(URL._url).post(body).build();
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

    private void _allNews(String tableName, String identity)
    {
        FormBody.Builder builder = createBuilder("1", tableName, "all");
        builder.add("identity", identity);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(URL.url).post(formBody).build();
        myNewsList mynewsList;
        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            if(response.code() == 200)
            {
                String result = response.body().string();
                if(!result.equals("[]"))
                {
                    mynewsList = new Gson().fromJson(result, myNewsList.class);
                    allnews = new Vector<>(Arrays.asList(mynewsList.list));
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

    private void _findNews(String tableName, String newsID, String identity)
    {
        FormBody.Builder builder = createBuilder("1", tableName, "find");
        builder.add("sole", identity + newsID);
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
                    isFindNews = true;
                }
                else if(result.equals("no"))
                {
                    isFindNews = false;
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

    private void _deleteAccount(String identity)
    {
        FormBody.Builder builder = createBuilder("0", "account", "delete");
        builder.add("identity", identity);
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
            if(response.code() == 200)
            {
                String result = response.body().string();
                if(result.equals("yes"))
                {
                    isright_num = 1;
                }
                else if(result.equals("no"))
                {
                    isright_num = 0;
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

    public void downloadAll(final SQLiteDatabase db, final String identity)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _downloadAccount(db);
                _downloadNews(db, SQLiteDbHelper.TABLE_COLLECTION, identity);
                _downloadNews(db, SQLiteDbHelper.TABLE_SEEN, identity);
            }
        }).start();
    }

    public void uploadNews(final SQLiteDatabase db, final String tableName, final String identity, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _uploadNews(db, tableName, identity, password);
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

    public void allNews(final String tableName, final String identity)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _allNews(tableName, identity);
            }
        }).start();
    }

    public void findNews(final String tableName, final String newsID, final String identity)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _findNews(tableName, newsID, identity);
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

    public void deleteAccount(final String identity)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _deleteAccount(identity);
            }
        }).start();
    }

    public void isAccount(final String identity)
    {
        isaccount_num = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                _isAccount(identity);
            }
        }).start();
        while(isaccount_num == -1)  {}
        isaccount = isaccount_num == 1;
    }

    public void isRightPassword(final String identity, final String password)
    {
        isright_num = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                _isRightPassword(identity, password);
            }
        }).start();
        while(isright_num == -1)   {}
        isright = isright_num == 1;
    }

}

class myNewsList
{
    News[] list = null;
}

class Accounts
{
    String identity;
    String password;
}

class myAccountList
{
    Accounts[] list = null;
}


