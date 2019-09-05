package com.example.myapplication.SQLite;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class serverAvail
{
    public static boolean avail;
    static private OkHttpClient client = new OkHttpClient();

    public static void test()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Request request = new Request.Builder().url(URL.url).get().build();
                    Response response = client.newCall(request).execute();
                    System.out.print(response.code());
                    avail = response.code() == 200;
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
