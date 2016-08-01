package com.buckethaendl.smartcart.activities.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.Refreshable;
import com.buckethaendl.smartcart.activities.choosestore.ChooseStoreActivity;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListRecyclerViewAdapter;
import com.buckethaendl.smartcart.activities.shoppinglist.listeners.ShoppingItemClickListener;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;
import com.buckethaendl.smartcart.util.DialogBuildingSite;
import com.buckethaendl.smartcart.util.KeyboardUtil;

public class ShoppingListDetailsActivity extends AppCompatActivity implements Refreshable {

    public static final int RQ_EDIT_MODE = 5615;

    public static final String TAG = ShoppingListDetailsActivity.class.getName();
    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";

    protected ShoppingList list;

    protected RecyclerView recycler;
    protected ShoppingListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_shopping_list_details);

        if(savedInstanceState != null) {

            //Get information about the selected shopping list
            try {

                int id = savedInstanceState.getInt(EXTRA_SHOPPING_LIST_ID);
                this.list = ShoppingListLibrary.getInstance().getShoppingList(id);

            }

            catch (IndexOutOfBoundsException e) {
                finish(); //no such list, finish activity
            }

        }

        else {

            //Get information about the selected shopping list
            try {

                int id = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);
                this.list = ShoppingListLibrary.getInstance().getShoppingList(id);

            }

            catch (IndexOutOfBoundsException e) {
                finish(); //no such list, finish activity
            }

        }

        //Set the recycler view and listener (adapter is set later)
        this.recycler = (RecyclerView) this.findViewById(R.id.activity_shopping_list_details_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        this.recycler.setLayoutManager(manager);

        //this.adapter.setShoppingItemCheckedListener(new ShoppingCheckedListener());
        //TODO implement the onClick listener for the individual items
        //TODO idea for this: the clicked item should expand to a view with an edittext (to set the name) and a category chooser...or the categories are in a swipable recycler view just below that

        //Set up the FAB
        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.activity_shopping_list_details_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShoppingListDetailsActivity.this, ChooseStoreActivity.class);
                intent.putExtra(ChooseStoreActivity.EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(list));

                startActivity(intent);

            }

        });

        //Set up the SupportActionBar
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_shopping_list_details_toolbar);
        this.setSupportActionBar(toolbar);

        //Set up the Up-Navigation
        if(this.getSupportActionBar() != null) {

            this.getSupportActionBar().setTitle(this.list.getName());
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        this.refreshView(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RQ_EDIT_MODE) {

            //refresh value displaying fields
            this.refreshView();

        }

        else super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStop() {

        super.onStop();
        KeyboardUtil.hideKeyboard(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.shopping_list_details_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.shopping_list_details_edit_menuitem: //todo müsste man das hier eigentlich mit result machen?

                Intent intent = new Intent(this, ShoppingListNewActivity.class);

                intent.putExtra(ShoppingListNewActivity.EXTRA_EDIT_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(this.list));
                startActivityForResult(intent, RQ_EDIT_MODE);

                return true;

            case R.id.shopping_list_details_delete_menuitem:

                final AlertDialog dialog = DialogBuildingSite.buildErrorDialog(this, getString(R.string.confirm_delete_shopping_list, list.getName()));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ShoppingListLibrary.getInstance().removeList(list);
                        finish();

                    }

                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialog.dismiss();

                    }

                });

                dialog.setCancelable(true);
                dialog.show();

                return true;

            default: return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void refreshView() {

        this.refreshView(false);

    }

    @Override
    public void refreshView(boolean forceCreate) {

        //update title
        if(this.getSupportActionBar() != null) this.getSupportActionBar().setTitle(this.list.getName());

        //update recycler
        if(this.adapter == null || forceCreate) {

            this.adapter = new ShoppingListRecyclerViewAdapter(this.list);
            this.adapter.setShoppingItemClickListener(new ShoppingItemClickListener() {

                @Override
                public void onClickShoppingItem(ShoppingListItem item, int position) {
                    Log.v(TAG, "Result: '" + item.getFormatedName() + "'");
                }

            });

            this.recycler.setAdapter(this.adapter);

        }

        else this.adapter.notifyDataSetChanged();

        Log.v(TAG, "Refreshed activity");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putInt(EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(this.list));

    }

}
