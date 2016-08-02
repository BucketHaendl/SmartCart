package com.buckethaendl.smartcart;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.buckethaendl.smartcart.data.local.file.FileLibrary;

import java.io.File;

/**
 * This class provides a global access to the application context and resources.
 * Please note that this class should only be used for non-Android classes to gain access to Android-specific resources.
 * Using this class is no good practice.
 *
 * Created by Cedric on 25.07.16.
 */
public class App extends Application {

    public static final String TAG = App.class.getName();

    private static Context globalContext;

    public static final String DIRECTORY_NAME = "smartcart";
    public static File EXTERNAL_DIRECTORY;

    @Override
    public void onCreate() {

        super.onCreate();
        App.globalContext = this;

        //EXTERNAL_DIRECTORY = this.getDir(APP_DIRECTORY_NAME, Context.MODE_PRIVATE);
        EXTERNAL_DIRECTORY = this.getDir(DIRECTORY_NAME, MODE_PRIVATE); //TODO or use this.getDir(mode public)
        Log.e("App", "File directory: " + EXTERNAL_DIRECTORY);

        //Create basic objects
        Log.d(TAG, "Loading basic objects");

        FileLibrary fileLibrary = new FileLibrary(App.EXTERNAL_DIRECTORY);
        fileLibrary.createManagers();

    }

    /**
     * Returns an instance of the applications context, created on the first app start
     * @return Instance of the context
     */
    public static Context getGlobalContext() {

        return globalContext;

    }

    /**
     * Returns an instance of the applications resources, created on the first app start
     * @return Instance of the resources accessor object
     */
    public static Resources getGlobalResources() {

        return globalContext.getResources();

    }

}
