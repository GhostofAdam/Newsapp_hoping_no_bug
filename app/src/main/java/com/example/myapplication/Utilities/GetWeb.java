package com.example.myapplication.Utilities;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.myapplication.Utilities.DataList;
public class GetWeb
{
    public DataList newsList = new DataList();
    public void senRequest(final String interFace)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(interFace).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    newsList = parseJSON(responseData);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public DataList parseJSON(String jsonData)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, DataList.class);
    }

}
