package com.buckethaendl.smartcart;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * This class provides a global access to the application context and resources.
 * Please note that this class should only be used for non-Android classes to gain access to Android-specific resources.
 * Using this class is no good practice.
 *
 * Created by Cedric on 25.07.16.
 */
public class App extends Application {

    private static Context globalContext;

    @Override
    public void onCreate() {

        super.onCreate();
        App.globalContext = this;

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
