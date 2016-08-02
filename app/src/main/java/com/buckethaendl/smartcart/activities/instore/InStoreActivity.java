package com.buckethaendl.smartcart.activities.instore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.buckethaendl.smartcart.activities.instore.adapters.InStoreRecyclerViewAdapter;
import com.buckethaendl.smartcart.activities.instore.listeners.RouteClickListener;
import com.buckethaendl.smartcart.activities.shoppinglist.ShoppingListHubActivity;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.instore.Material;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;
import com.buckethaendl.smartcart.objects.shoppinglist.SortedShoppingListItem;

public class InStoreActivity extends AppCompatActivity implements Refreshable {

    public static final String TAG = InStoreActivity.class.getName();

    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";
    public static final String LEAVE_SHOPPING_CONFIRM_TAG = "leave_shopping_confirm_tag";

    private View mainView;

    protected ShoppingList list;

    protected RecyclerView recycler;
    protected InStoreRecyclerViewAdapter adapter;

    private SortedShoppingListItem lastClickedItem;
    private Material lastClickedMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store);

        this.mainView = findViewById(R.id.activity_in_store_coordinatorlayout);

        //todo hier muss noch für alle items geprüft werden, ob sie wirklich existieren (unknown value setzen)

        int listId;
        //restore list details
        if(savedInstanceState != null) {

            listId = savedInstanceState.getInt(EXTRA_SHOPPING_LIST_ID);

        }

        else {

            listId = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);

        }

        //get information about the selected shopping list
        this.list = ShoppingListLibrary.getInstance().getShoppingList(listId);

        //Set the recycler view and listener (adapter is set later)
        this.recycler = (RecyclerView) this.findViewById(R.id.activity_in_store_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        this.recycler.setLayoutManager(manager);

        //Set up the FAB
        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.activity_in_store_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //tick the most-uppest entry on the list
                for(ShoppingListItem item : list) {

                    if(!item.isChecked()) {

                        item.setChecked(true);
                        InStoreActivity.this.adapter.notifyDataSetChanged();
                        Log.v(TAG, "Quick checked item " + item.getDisplayName());
                        return;

                    }

                }

                Log.v(TAG, "All items already checked");

            }

        });

        //Set up the SupportActionBar
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_in_store_toolbar);
        this.setSupportActionBar(toolbar);

        //Set up the Up-Navigation
        if(this.getSupportActionBar() != null) {

            this.getSupportActionBar().setTitle(this.list.getName());
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        this.refreshView(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.in_store_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home:

                /* todo reimplement
                LeaveShoppingConfirmDialog dialog = new LeaveShoppingConfirmDialog();
                dialog.show(getFragmentManager(), LEAVE_SHOPPING_CONFIRM_TAG);*/

                return super.onOptionsItemSelected(item);

            case R.id.in_store_done_menuitem:

                //todo implement functionality: end shopping!, clear back stack, ...

                Intent intent = new Intent(this, ShoppingListHubActivity.class);
                startActivity(intent);

                finish();

                return true;

            default: return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

        /* todo reimplement
        LeaveShoppingConfirmDialog dialog = new LeaveShoppingConfirmDialog();
        dialog.show(getFragmentManager(), LEAVE_SHOPPING_CONFIRM_TAG);*/

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

            this.adapter = new InStoreRecyclerViewAdapter(this.list);
            this.adapter.setRouteClickListener(new RouteClickListener() {

                @Override
                public void onClickShowRoute(SortedShoppingListItem toItem, Material toMaterial) {

                    if(lastClickedItem != null && lastClickedMaterial != null) {

                        //calculate route
                        Material fromMaterial = lastClickedMaterial;
                        StringBuilder route = new StringBuilder("Go from " + fromMaterial);

                        //while smaller --> get next material
                        while(fromMaterial.getId() < (toMaterial.getId() - 1)) {

                            route.append(" via ");
                            fromMaterial = fromMaterial.getMaterialAbove();
                            route.append(fromMaterial);

                        }

                        //while smaller --> get next material
                        while(fromMaterial.getId() > (toMaterial.getId() + 1)) {

                            route.append(" via ");
                            fromMaterial = fromMaterial.getMaterialBelow();
                            route.append(fromMaterial);

                        }

                        route.append(" to ");
                        route.append(toMaterial);

                        //show snackbar
                        Snackbar snackbar = Snackbar.make(mainView, route.toString(), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }

                    else {

                        //show snackbar
                        Snackbar snackbar = Snackbar.make(mainView, "Please check your first item", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }

                }

                @Override
                public void onCheck(SortedShoppingListItem item, Material material) {

                    lastClickedItem = item;
                    lastClickedMaterial = material;

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

    public static class LeaveShoppingConfirmDialog extends DialogFragment {
        private Activity activity;

        @Override
        public void onAttach(Activity activity) {

            super.onAttach(activity);
            this.activity = activity;

        }

        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.in_store_leave_dialog_title)
                    .setMessage(R.string.in_store_leave_dialog_message)
                    .setIcon(android.R.drawable.ic_menu_send)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }

                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();


                        }

                    })
                    .create();

            return dialog;

        }

    }


}
