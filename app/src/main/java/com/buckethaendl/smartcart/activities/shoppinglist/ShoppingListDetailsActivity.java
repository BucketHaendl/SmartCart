package com.buckethaendl.smartcart.activities.shoppinglist;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListRecyclerViewAdapter;
import com.buckethaendl.smartcart.objects.shoppingList.Material;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListItem;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListLibrary;

import java.util.Arrays;
import java.util.List;

public class ShoppingListDetailsActivity extends AppCompatActivity implements ShoppingListRecyclerViewAdapter.ShoppingItemClickListener {

    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";

    private ShoppingList selectedList;

    private RecyclerView recyclerView;
    private ShoppingListRecyclerViewAdapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_shopping_list_details);

        //Get information about the selected shopping list
        int id = this.getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);

        if(id != -1) this.selectedList = ShoppingListLibrary.getLibrary(getResources()).getShoppingList(id);

        else {

            //No shopping list was passed - finishing activity (it's never shown)
            this.finish();

        }

        //Load the recycler view with the items of the selected list
        this.recyclerView = (RecyclerView) this.findViewById(R.id.activity_shopping_list_details_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(manager);

        //Set the adapter
        this.adapter = new ShoppingListRecyclerViewAdapter(this.selectedList);
        this.recyclerView.setAdapter(this.adapter);

        this.adapter.setShoppingItemClickListener(this);

        //TODO implement the onClick listener for the individual items
        //TODO set a load animation for the views
        //TODO idea for this: the clicked item should expand to a view with an edittext (to set the name) and a category chooser...or the categories are in a swipable recycler view just below that

        //Set up the FAB
        this.fab = (FloatingActionButton) this.findViewById(R.id.activity_shopping_list_details_fab);
        this.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TODO implement real functionality (sobald man Einkaufen gehen kann)
                final Snackbar snackbar = Snackbar.make(recyclerView, "Starting your shopping...", Snackbar.LENGTH_SHORT);

                snackbar.setAction("TEST", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        snackbar.dismiss();

                    }

                });

                snackbar.show();

            }

        });

        //Set up the SupportActionBar
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_shopping_list_details_toolbar);
        this.setSupportActionBar(toolbar);

        //Set up the Up-Navigation
        if(this.getSupportActionBar() != null) {

            this.getSupportActionBar().setTitle(this.selectedList.getName());
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public void onClickShoppingItem(ShoppingListItem item, int position) {

        Snackbar snackbar = Snackbar.make(ShoppingListDetailsActivity.this.findViewById(R.id.activity_shopping_list_details_coordinatorlayout), "Clicked: " + item.getName(), Snackbar.LENGTH_SHORT);
        snackbar.show();
        
    }

    //für den material / category chooser
    //TODO only coppy-pasted from ShoppingListAddActivity -> should be a better way!
    private class ChooseMaterialClickListener implements AdapterView.OnClickListener, MaterialChooser.MaterialChosenListener {

        private Material currentMaterial = Material.BAKED_GOODS; //TODO remove! only for testing
        private MaterialChooser chooser;

        @Override
        public void onClick(View view) {

            ImageView button = (ImageView) view; //maybe set the drawable resource to something else

            List<Material> materialList = Arrays.asList(Material.values());
            this.chooser = new MaterialChooser(ShoppingListDetailsActivity.this, view, materialList, this);

            this.chooser.show();

        }

        @Override
        public void onMaterialChosen(Material material) {

            this.currentMaterial = material;

            Log.v("ShoppingListAddActivity", "Material " + material.getName() + " chosen");

        }

        public Material getCurrentMaterial() {
            return this.currentMaterial;
        }

    }

}
