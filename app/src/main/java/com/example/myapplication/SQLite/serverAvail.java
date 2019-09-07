package com.example.myapplication.SQLite;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class serverAvail
{
    private int avail;
    private OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.SECONDS).readTimeout(1, TimeUnit.SECONDS).build();

    private void _test()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Request request = new Request.Builder().url(URL.url_avail).get().build();
                    Response response = client.newCall(request).execute();
                    System.out.println(response.body());
                    if(response.isSuccessful()) {
                        avail = response.code() == 200 ? 1 : 0;
                    }
                    else{
                        avail = 0;
                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    avail = 0;
                    System.out.println("ass");
                }
            }
        }).start();
    }
    public boolean test()
    {
        avail = -1;
        _test();
        while(avail == -1)  {
            System.out.println("asshole");
        }
        System.out.println("fuck");
        return avail == 1;
    }
}
