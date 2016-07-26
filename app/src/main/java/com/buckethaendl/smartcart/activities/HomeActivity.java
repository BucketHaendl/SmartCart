package com.buckethaendl.smartcart.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.ShoppingListHubActivity;
import com.buckethaendl.smartcart.data.local.FileLibrary;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = HomeActivity.class.getName();

    public static final String APP_DIRECTORY_NAME = "files";
    public static File APP_DIRECTORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //create basic objects
        Log.d(TAG, "Loading basic objects");
        APP_DIRECTORY = this.getDir(APP_DIRECTORY_NAME, Context.MODE_PRIVATE);

        FileLibrary fileLibrary = new FileLibrary(APP_DIRECTORY);
        fileLibrary.createManagers();

        //setup UI
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_home_toolbar);
        this.setSupportActionBar(toolbar);

        /*
        //Set the ActionBar properly
        if(this.getActionBar() != null) {

            this.getActionBar().setTitle(getString(R.string.home_activity_actionbar_title));

        }*/

    }

    /**
     * Executed when the activity_home_write_shopping_list_button Button is clicked
     * Loads and shows the ShoppingListOverview activity
     * @param view The clicked button
     */
    public void onWriteShoppingListButtonClick(View view) {

        Intent intent = new Intent(this, ShoppingListHubActivity.class);
        startActivity(intent);

    }

    /**
     * Executed when the activity_home_go_shopping_button Button is clicked
     * Loads and shows the LaunchShopping activity
     * @param view The clicked button
     */
    public void onGoShoppingButtonClick(View view) {



    }

}
