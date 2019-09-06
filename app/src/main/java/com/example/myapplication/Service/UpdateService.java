package com.example.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.OperateOnServer;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.SQLite.serverAvail;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;

public class UpdateService extends Service {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            serverAvail serve = new serverAvail();
            @Override
            public void run() {
                boolean net = serve.test();
                while (true)
                {
                    while (net)
                    {
                        net = serve.test();
                        try
                        {
                            Thread.sleep(300);
                             }
                        catch (Exception e){ }
                    }
                    while(!net)
                    {
                        net = serve.test();
                        try
                        {
                            Thread.sleep(300);
                        }
                        catch (Exception e){ }
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    }
