package com.example.myapplication.SQLite;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class serverAvail
{
    private static int avail;
    static private OkHttpClient client = new OkHttpClient();

    private static void _test()
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
                    avail = response.code() == 200 ? 1 : 0;
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static boolean test()
    {
        avail = -1;
        _test();
        while(avail == -1)  {}
        return avail == 1;
    }
}
