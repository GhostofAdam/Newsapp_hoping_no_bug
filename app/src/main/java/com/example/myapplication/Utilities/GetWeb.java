package com.example.myapplication.Utilities;

import com.example.myapplication.SQLite.serverAvail;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetWeb
{
    public DataList newsList = new DataList();
    public void senRequest(final String interFace)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(3, TimeUnit.SECONDS).build();
                    Request request = new Request.Builder().url(interFace).build();
                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();
                    String responseData = null;
                    if(responseBody != null)
                    {
                        responseData = responseBody.string();
                    }
                    newsList = parseJSON(responseData);
                } catch (IOException | NullPointerException e) {
                    newsList.setData();
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private DataList parseJSON(String jsonData)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, DataList.class);
    }

}
