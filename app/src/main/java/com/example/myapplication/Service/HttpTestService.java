package com.example.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HttpTestService extends Service {
    public HttpTestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
