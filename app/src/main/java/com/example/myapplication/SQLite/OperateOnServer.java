package com.example.myapplication.SQLite;

import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.R;
import com.example.myapplication.Utilities.News;

import java.io.IOException;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class URL
{
    static String url = "http://192.168.0.164:8000/";
}

public class OperateOnServer
{
    static private OkHttpClient client = new OkHttpClient();

    public void inseartNews(final String tableName, final News news, final String identity, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                inseartNews(tableName, news, identity, password);
            }
        }).start();
    }

    public void _inseartNews(String tableName, News news, String identity, String password)
    {

        FormBody.Builder builder = newsToBuilder(news, identity, password);
        if(tableName.equals(SQLiteDbHelper.TABLE_COLLECTION))
        {
            builder.add("type", "collection");
        }
        else if(tableName.equals(SQLiteDbHelper.TABLE_SEEN))
        {
            builder.add("type", "seen");
        }
        builder.add("doing", "add");
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

    private FormBody.Builder newsToBuilder(News news, String identity, String password)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("sole", news.getNewsID() + news.getContent());
        builder.add("newsID", news.getNewsID());
        builder.add("title", news.getTitle());
        builder.add("content", news.getContent());
        builder.add("publisher", news.getPublisher());
        builder.add("publishTime", news.getPublishTime());
        builder.add("identity", identity);
        builder.add("password", password);
        return builder;
    }

    public void deleteNews(String tableName, News news, String identity)
    {
        FormBody.Builder builder = new FormBody.Builder();
        if(tableName.equals(SQLiteDbHelper.TABLE_COLLECTION))
        {
            builder.add("type", "collection");
        }
        else if(tableName.equals(SQLiteDbHelper.TABLE_SEEN))
        {
            builder.add("type", "seen");
        }
        builder.add("doing", "delete");
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

    public Vector<News> allNews(String tableName, String identity)
    {
        FormBody.Builder builder = new FormBody.Builder();
        if(tableName.equals(SQLiteDbHelper.TABLE_COLLECTION))
        {
            builder.add("type", "collection");
        }
        else if(tableName.equals(SQLiteDbHelper.TABLE_SEEN))
        {
            builder.add("type", "seen");
        }
        builder.add("doing", "all");
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
        return null;
    }

    public boolean findNews(String tableName, String newsID, String identity)
    {
        FormBody.Builder builder = new FormBody.Builder();
        if(tableName.equals(SQLiteDbHelper.TABLE_COLLECTION))
        {
            builder.add("type", "collection");
        }
        else if(tableName.equals(SQLiteDbHelper.TABLE_SEEN))
        {
            builder.add("type", "seen");
        }
        builder.add("doing", "find");
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
        return true;
    }

    public void insertAccount(String identity, String password)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("type", "account");
        builder.add("doing", "add");
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

    public void deleteAccount(String identity)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("type", "account");
        builder.add("doing", "delete");
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

    public boolean isAccount(String identity)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("type", "account");
        builder.add("doing", "is");
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
        return true;
    }

    public boolean isRightPassword(String identity, String password)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("type", "account");
        builder.add("doing", "right");
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
        return true;
    }
}

