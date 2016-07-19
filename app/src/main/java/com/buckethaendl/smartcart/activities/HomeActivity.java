package com.buckethaendl.smartcart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.ShoppingListHubActivity;
import com.buckethaendl.smartcart.activities.shoppinglist.ShoppingListOverviewFragment;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListLibrary;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
