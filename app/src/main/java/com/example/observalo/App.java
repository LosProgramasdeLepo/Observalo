package com.example.observalo;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static List<Context> context = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context.add(0, getApplicationContext());
    }

    public static Context getAppContext() {
        return context.get(0);
    }
}