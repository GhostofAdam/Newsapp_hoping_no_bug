package com.example.myapplication.Utilities;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class GetWeb
{
    public DataList newsList = new DataList();
    public void senRequest(final String interFace)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                synchronized (newsList) {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(interFace).build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        newsList = parseJSON(responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    newsList.notify();
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
