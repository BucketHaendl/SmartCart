package com.buckethaendl.smartcart.activities.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListRecyclerViewAdapter;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.util.DialogBuildingSite;

public class ShoppingListDetailsActivity extends AppCompatActivity {

    public static final String TAG = ShoppingListDetailsActivity.class.getName();
    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";

    protected int listId;
    protected ShoppingList list;

    protected RecyclerView recyclerView;
    protected ShoppingListRecyclerViewAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_shopping_list_details);

        //todo hier muss noch für alle items geprüft werden, ob sie wirklich existieren (unknown value setzen)
        if(savedInstanceState != null) {

            this.list = (ShoppingList) savedInstanceState.getSerializable(EXTRA_SHOPPING_LIST_ID);

        }

        else {

            //Get information about the selected shopping list
            try {

                this.listId = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);
                this.list = ShoppingListLibrary.getInstance().getShoppingList(this.listId);

            }

            catch (IndexOutOfBoundsException e) {
                finish(); //no such list, finish activity
            }

        }

        //Set the recycler view and listener
        this.recyclerView = (RecyclerView) this.findViewById(R.id.activity_shopping_list_details_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(manager);

        //set the recyclerAdapter
        this.recyclerAdapter = new ShoppingListRecyclerViewAdapter(this.list);
        this.recyclerView.setAdapter(this.recyclerAdapter);

        //this.recyclerAdapter.setShoppingItemClickListener(new ShoppingClickListener()); not used atm
        //this.recyclerAdapter.setShoppingItemCheckedListener(new ShoppingCheckedListener());
        //TODO implement the onClick listener for the individual items
        //TODO idea for this: the clicked item should expand to a view with an edittext (to set the name) and a category chooser...or the categories are in a swipable recycler view just below that

        //Set up the FAB
        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.activity_shopping_list_details_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TODO implement real functionality (sobald man Einkaufen gehen kann)
                Snackbar snackbar = Snackbar.make(recyclerView, "Starting your shopping...", Snackbar.LENGTH_SHORT);
                snackbar.show();

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

                intent.putExtra(ShoppingListNewActivity.EXTRA_EDIT_SHOPPING_LIST_ID, this.listId);
                startActivity(intent);

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
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putSerializable(EXTRA_SHOPPING_LIST_ID, this.list);

    }

}
