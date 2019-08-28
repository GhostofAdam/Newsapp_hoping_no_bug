package com.example.myapplication.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    public Bitmap mIcon11 = null;
    public String[] urls;
    public Bitmap download(String... _urls) {
        urls = _urls;
        return doInBackground(_urls);
    }

    protected Bitmap doInBackground(String... _urls) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urldisplay;
                int i = 0;
                while (i < urls.length && urls[i].equals("")) {
                    i++;
                }
                urldisplay = urls[i];

                try {
                    URL imageUrl = new URL(urldisplay);
                    mIcon11 = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return mIcon11;
    }
}
