package com.example.foodrecipes;

// to do the retrofit in background thread

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    // singleton pattern
    private static AppExecutors instance;

    public static AppExecutors getInstance(){
        if (instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

    // to retrieving an executor services
    public ScheduledExecutorService networkIO(){
        return mNetworkIO;
    }

}
