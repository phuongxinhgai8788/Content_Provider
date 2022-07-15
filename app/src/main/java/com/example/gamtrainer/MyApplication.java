package com.example.gamtrainer;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.initialize(this);
    }
}
