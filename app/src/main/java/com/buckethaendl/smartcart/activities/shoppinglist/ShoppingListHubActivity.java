package com.buckethaendl.smartcart.activities.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.objects.shoppingList.Library;
import com.buckethaendl.smartcart.objects.shoppingList.MenuBarFragmentListener;
import com.buckethaendl.smartcart.objects.shoppingList.NotInitializedException;
import com.buckethaendl.smartcart.objects.shoppingList.RefreshAdapterListener;
import com.buckethaendl.smartcart.objects.shoppingList.SerializationException;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.shoppingList.WrongFragmentInstanceException;
import com.buckethaendl.smartcart.util.DialogBuildingSite;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ShoppingListHubActivity extends AppCompatActivity {

    public static final String APP_DIRECTORY_NAME = "local";

    public static final int NEW_LIST_RESULT_CODE = 123456;

    public static final int MAX_LISTENING_FRAGMENTS = 2; //Up to 2 fragments can listen for MenuBar events on this activity

    private RefreshAdapterListener adapterListener = null;
    //private MenuBarFragmentListener[] menuBarListeners = new MenuBarFragmentListener[MAX_LISTENING_FRAGMENTS]; currently not implemented!


    private static File appDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_hub);

        if(appDirectory == null) appDirectory = this.getDir(APP_DIRECTORY_NAME, Context.MODE_PRIVATE);

        //Get references to the fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment overviewListFragment = fragmentManager.findFragmentById(R.id.activity_shopping_list_hub_overview_fragment);

        if (overviewListFragment != null) {

            //this.menuBarListeners[0] = (MenuBarFragmentListener) overviewListFragment; //The listener[0] is always the overview fragment!
            this.adapterListener = (RefreshAdapterListener) overviewListFragment;

        } else Log.v("ShoppingListHubActivity", "No overview fragment recognized");

        Fragment detailsListFragment = fragmentManager.findFragmentById(R.id.activity_shopping_list_hub_details_fragment);

        if (detailsListFragment != null) {

            //this.menuBarListeners[1] = (MenuBarFragmentListener) overviewListFragment; //The listener[1] is always the details fragment (if it exists in this activity because the phone is in landscape mode)!

        } else Log.v("ShoppingListHubActivity", "No details fragment recognized");

        //Set the ActionBar properly
        Toolbar supportToolbar = (Toolbar) this.findViewById(R.id.activity_shopping_list_hub_toolbar);
        this.setSupportActionBar(supportToolbar);

        if (this.getSupportActionBar() != null) {

            this.getSupportActionBar().setTitle(getString(R.string.shopping_list_overview_activity_actionbar_title));
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public void onStart() {

        super.onStart();

        //Load the library with values from the local serialization file NOTE: this must be after getting references to the fragments!
        ShoppingListLibrary library = ShoppingListLibrary.getLibrary(getResources());

        if(!library.isInitialized()) library.executeLoadComponents(this, appDirectory, new LibraryLoadedListener());
        else this.refreshShoppingListsAdapter(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.getMenuInflater().inflate(R.menu.shopping_list_overview_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {

            case R.id.shopping_list_overview_new_list_menuitem: //TODO implement real working version

                Log.v("ShoppingListOverviewFr", "Adding new shopping list");

                Intent intent = new Intent(this, ShoppingListAddActivity.class);
                startActivity(intent);

                return true;

            /* TODO remove - only for testing
            case R.id.load_shoppinglists:

                Log.v("ShoppingListOverviewFr", "Executed load of library contents...");
                ShoppingListLibrary.getLibrary(getResources()).executeLoadComponents(this, getAppDirectory(), new LibraryLoadedListener());

                return true;

            case R.id.save_shoppinglists:

                Log.v("ShoppingListOverviewFr", "Executed save of library contents...");
                ShoppingListLibrary.getLibrary(getResources()).executeSaveComponents(this, getAppDirectory());

                return true;*/

            /* TODO remove - no longer used
            default: //The action can not be performed by the activity and the call should be passed on to the MenuItemBarListeners

                if(super.onOptionsItemSelected(item)) return true;

                else {

                    boolean result = false;

                    for (MenuBarFragmentListener listener : this.menuBarListeners) { //Pass the id on to all listeners for this fragment

                        if (listener == null) continue;

                        boolean localResult = listener.onClickMenuItem(id);
                        if (!result) result = localResult;

                    }

                    return result;

                } */

            default: return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        ShoppingListLibrary.getLibrary(getResources()).executeSaveComponents(this, getAppDirectory());

    }

    public static File getAppDirectory() {

        return appDirectory;

    }

    /**
     * Tries to revalidate the underlying adapter view in the list fragment ShoppinglistOverviewFragment or whichever was set as the adapterListener
     * @throws NotInitializedException if the adapterListener wasn't set up before or isn't working
     */
    public void refreshShoppingListsAdapter() throws NotInitializedException {

        this.refreshShoppingListsAdapter(false);

    }

    /**
     * Tries to revalidate the underlying adapter view in the list fragment ShoppinglistOverviewFragment or whichever was set as the adapterListener
     * @param force true to force a recreation of the list adapter with new values
     * @throws NotInitializedException if the adapterListener wasn't set up before or isn't working
     */
    public void refreshShoppingListsAdapter(boolean force) throws NotInitializedException {

        if(this.adapterListener == null) throw new NotInitializedException("The RefreshAdapterListener adapterListener wasn't set or is null and cannot call refresh");
        this.adapterListener.onRefreshListAdapter(force);

    }

    public class LibraryLoadedListener implements ShoppingListLibrary.TaskListener {

        @Override
        public void onDone() {

            refreshShoppingListsAdapter();

        }

    }

}



        /**

         TODO remove - old implementation from the activity that is now moved into the respective fragments

         switch (id) {

         case R.id.shopping_list_overview_new_list_menuitem:

         Log.v("ShoppingListOverviewAc", "Adding new shopping list...");

         Toast toast = Toast.makeText(this, "NewShoppingList Intent clicked", Toast.LENGTH_SHORT);
         toast.show();

         return true;

         case R.id.load_shoppinglists:

         Log.v("ShoppingListOverviewAc", "Executed load of library contents...");
         this.loadLocalShoppingLists();

         return true;

         case R.id.save_shoppinglists:

         Log.v("ShoppingListOverviewAc", "Executed save of library contents...");
         this.saveLocalShoppingLists();

         return true;

         default:
         return super.onOptionsItemSelected(item);

         }**/