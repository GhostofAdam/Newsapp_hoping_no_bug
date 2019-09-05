package com.example.myapplication.SQLite;

import com.example.myapplication.Utilities.News;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class URL
{
    static String url = "http://166.111.5.239:8001/app/";
}

public class OperateOnServer
{
    static private OkHttpClient client = new OkHttpClient();
    public Vector<News> allnews;
    public boolean isFindNews;
    public boolean isaccount;
    public boolean isright;

    private FormBody.Builder createBuilder(String id, String table, String doing)
    {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", id);
        builder.add("table", table);
        builder.add("doing", doing);
        return builder;
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
                    isaccount = true;
                }
                else if(result.equals("no"))
                {
                    isaccount = false;
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
                    isright = true;
                }
                else if(result.equals("no"))
                {
                    isright = false;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                _isAccount(identity);
            }
        }).start();
    }

    public void isRightPassword(final String identity, final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _isRightPassword(identity, password);
            }
        }).start();
    }

}

class myNewsList
{
    News[] list = null;
}


