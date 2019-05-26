package com.bytebala.foodrecipes;

/*
Make it easier to fire a background thread
Use singleton. Retrofit requests needs to be done in background thread.
Produce those objects to run those tasks on background .
Executors are responsible for executing runnable tasks
 */

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    private static AppExecutors instance;

    public static AppExecutors getInstance() {
        if (instance == null)
            instance = new AppExecutors();
        return instance;
    }

    //It allows you to add extra features to regular Executors
    //Executor is essentaily thing to execute runnable tasks. You can use them in main and background. You can customize them to do whatever whanted
    //SES is executor service that can schedule commands to run after a given delay
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

    /*
    //Single thread to execute a task
    private Executor mBackGroundExecutor = Executors.newSingleThreadExecutor(); */

    public ScheduledExecutorService mNetWorkIO(){
        return mNetworkIO;
    }
}
