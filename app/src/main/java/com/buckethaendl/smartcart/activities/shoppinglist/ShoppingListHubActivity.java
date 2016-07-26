package com.buckethaendl.smartcart.activities.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListAdapter;
import com.buckethaendl.smartcart.data.library.LibraryRefreshListener;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;

public class ShoppingListHubActivity extends AppCompatActivity {

    public static final String TAG = ShoppingListHubActivity.class.getName();

    private ShoppingListLibrary library;

    private ListView listView;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_hub);

        //Setup the list view (without adapter!)
        this.listView = (ListView) findViewById(R.id.activity_shopping_list_hub_overview_list);

        ShoppingListClickListener clickListener = new ShoppingListClickListener();
        this.listView.setOnItemClickListener(clickListener);

        //Set the ActionBar properly
        Toolbar supportToolbar = (Toolbar) this.findViewById(R.id.activity_shopping_list_hub_toolbar);
        this.setSupportActionBar(supportToolbar);

        if (this.getSupportActionBar() != null) {

            this.getSupportActionBar().setTitle(getString(R.string.shopping_list_overview_activity_actionbar_title));
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        //set library
        this.library = ShoppingListLibrary.getInstance();
        this.library.addListener(new ShoppingListLibraryListener());

    }

    @Override
    protected void onStart() {

        super.onStart();

        //Load the library with values from the local serialization file NOTE: this must be after getting references to the fragments!
        //note was in onStart before - any problems?
        if(!library.isInitialized()) library.loadLibrary(this);
        else this.refreshList(true);

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

            case R.id.shopping_list_overview_new_list_menuitem:

                Log.v(TAG, "Adding new shopping list");

                Intent intent = new Intent(this, ShoppingListNewActivity.class);
                startActivity(intent);

                return true;

            case R.id.load_shoppinglists:

                Log.v(TAG, "Executed load of library contents...");

                this.library.loadLibrary(this);
                return true;

            case R.id.save_shoppinglists:

                Log.v(TAG, "Executed save of library contents...");

                this.library.saveLibrary(this);
                return true;

            case R.id.default_shoppinglists:

                Log.v(TAG, "Adding default lists...");
                ShoppingListLibrary.getInstance().createTestLists();
                return true;

            default: return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        this.library.saveLibrary(this);

    }

    /**
     * Updates the adapter to notify it, that the data set has changed and shopping lists were added or removed from the Library
     * This method doesn't reload the shopping lists from the local binary file, but only shows the ones currently known by the Library.
     * Call loadLocalShoppingLists() to deserialize the locally saved lists first
     */
    public void refreshList() {

        this.refreshList(false);

    }

    /**
     * Updates the adapter to notify it, that the data set has changed and shopping lists were added or removed from the Library
     * This method doesn't reload the shopping lists from the local binary file, but only shows the ones currently known by the Library.
     * Call loadLocalShoppingLists() to deserialize the locally saved lists first
     * @param forceRecreate Set to true if you want to force the ArrayAdapter to regenerate, no matter if it pre-existed
     */
    public void refreshList(boolean forceRecreate) {

        if(ShoppingListHubActivity.this.adapter == null || forceRecreate) {

            ShoppingListHubActivity.this.adapter = new ShoppingListAdapter(ShoppingListHubActivity.this, R.layout.shopping_list_overview_listitem);
            ShoppingListHubActivity.this.listView.setAdapter(adapter);

        }

        else ShoppingListHubActivity.this.adapter.notifyDataSetChanged();

    }

    public class ShoppingListClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            Intent intent = new Intent(ShoppingListHubActivity.this, ShoppingListDetailsActivity.class);
            intent.putExtra(ShoppingListDetailsActivity.EXTRA_SHOPPING_LIST_ID, (int) id);
            startActivity(intent);

        }

    }

    public class ShoppingListLibraryListener implements LibraryRefreshListener {

        @Override
        public void onRefresh() {

            ShoppingListHubActivity.this.refreshList();
            Log.v(TAG, "Refreshed shopping list");

        }

    }

}