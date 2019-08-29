//package com.example.myapplication.Utilities;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class DownloadImageTask{
//    public Bitmap mIcon11 = null;
//    public String[] urls;
//    public Bitmap download(String... _urls) {
//
//    }
//
//    protected Bitmap doInBackground(String... _urls) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String urldisplay;
//                int i = 0;
//                while (i < urls.length && urls[i].equals("")) {
//                    i++;
//                }
//                urldisplay = urls[i];
//                urldisplay = "http://www.freeimageslive.com/galleries/objects/general/pics/woodenbox0482.jpg";
//                try {
//                    InputStream input = new java.net.URL(urldisplay).openStream();
//                    mIcon11 = BitmapFactory.decodeStream(input);
//                } catch (Exception e) {
//                    Log.i("error","failed downloading image");
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//        return mIcon11;
//    }
//}

