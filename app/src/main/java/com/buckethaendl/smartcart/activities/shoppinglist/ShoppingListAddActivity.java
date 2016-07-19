package com.buckethaendl.smartcart.activities.shoppinglist;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ColorChooserAdapter;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListRecyclerViewAdapter;
import com.buckethaendl.smartcart.objects.shoppingList.Library;
import com.buckethaendl.smartcart.objects.shoppingList.Material;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListItem;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListLibrary;
import com.buckethaendl.smartcart.util.DialogBuildingSite;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingListAddActivity extends AppCompatActivity {

    public static final int[] COLORS = {
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_red_dark,
            android.R.color.white,
            android.R.color.black,
            //Ab hier wiederholt es sich (nur zum test)
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_red_dark,
            android.R.color.white,
            android.R.color.black,
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_red_dark,
            android.R.color.white,
            android.R.color.black,
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_red_dark,
            android.R.color.white,
            android.R.color.black,
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_red_dark,
            android.R.color.white,
            android.R.color.black,
    };

    private ShoppingList list;

    private RecyclerView contentRecyclerView;
    private ShoppingListRecyclerViewAdapter shoppingListAdapter;
    private View addItemView;

    private ColorChoseListener colorChooserListener;
    private ChooseMaterialClickListener materialChooseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_add);

        //Plan: We need 2 recycler view adapters (whew...to much work): For the small colorchooser and then implement the one from the big content recycler view

        //Retrieve a new ShoppingList object
        this.list = ShoppingListLibrary.getLibrary(getResources()).createNewShoppingList(getResources());

        //Set the color chooser recyclerview and use adapter + onClickListener
        RecyclerView colorChooser = (RecyclerView) this.findViewById(R.id.activity_shopping_list_add_color_chooser);

        ColorChooserAdapter colorChooserAdapter = new ColorChooserAdapter(ShoppingListAddActivity.COLORS);
        colorChooser.setAdapter(colorChooserAdapter);

        LinearLayoutManager colorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        colorChooser.setLayoutManager(colorLayoutManager);

        this.colorChooserListener = new ColorChoseListener();
        colorChooserAdapter.setListener(this.colorChooserListener);

        //Set the content recyclerview and use adapter + onClickListener
        this.contentRecyclerView = (RecyclerView) this.findViewById(R.id.activity_shopping_list_add_content_recyclerview);

        this.shoppingListAdapter = new ShoppingListRecyclerViewAdapter(list);
        this.contentRecyclerView.setAdapter(this.shoppingListAdapter);

        LinearLayoutManager contentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.contentRecyclerView.setLayoutManager(contentLayoutManager);

        shoppingListAdapter.setShoppingItemClickListener(new ShoppingItemClickListener());

        //Set the add item logic
        this.addItemView = this.findViewById(R.id.activity_shopping_list_add_new_item_include);

        ImageButton addButton = (ImageButton) this.addItemView.findViewById(R.id.shopping_list_new_item_add_button);
        addButton.setOnClickListener(new AddItemClickListener());

        final EditText addItemEditText = (EditText) this.addItemView.findViewById(R.id.shopping_list_new_item_edittext);
        addItemEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN) {

                    if(keyCode == KeyEvent.KEYCODE_ENTER) {

                        addNewItemClicked();
                        return true;

                    }

                }

                return false;

            }

        });

        ImageButton materialButton = (ImageButton) this.addItemView.findViewById(R.id.shopping_list_new_item_category_imagebutton);

        this.materialChooseListener = new ChooseMaterialClickListener();
        materialButton.setOnClickListener(this.materialChooseListener);

        //Set the toolbar & Up-Navigation
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_shopping_list_add_toolbar);
        this.setSupportActionBar(toolbar);

        if(this.getSupportActionBar() != null) this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //Beginning of implementing save logic
    //There are three valid ways to save the list: done button, up-button & back button

    public void onDoneButtonClick(View view) {

        if(this.worthSaving()) this.saveCurrentListState(true);
        this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) { //when the home button is pressed

            if(this.worthSaving()) this.saveCurrentListState(true);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(this.worthSaving()) this.saveCurrentListState(true);
        super.onBackPressed();

    }

    //End of implementing save logic

    public boolean worthSaving() {

        EditText titleEditText = (EditText) this.findViewById(R.id.activity_shopping_list_add_name_edittext);

        if(titleEditText.getText().toString().isEmpty()) {

            return (this.list.getItemsCount() > 0);

        }

        else return true;

    }

    public void saveCurrentListState() {

        this.saveCurrentListState(false);

    }

    public void saveCurrentListState(boolean forceDiskSave) {

        //Add the add item, that might not have been added fully by now (just to make sure, we don't lose this last item)
        addNewItemClicked();

        //Save the title and color of the list
        EditText titleEditText = (EditText) this.findViewById(R.id.activity_shopping_list_add_name_edittext);

        if(!titleEditText.getText().toString().isEmpty()) this.getList().setName(titleEditText.getText().toString());

        if(this.colorChooserListener.getLatestColor() != -1) {

            this.getList().setColorId(this.colorChooserListener.getLatestColor());

        }

        ShoppingListLibrary.getLibrary(getResources()).addNewShoppingList(this.list);

        if(forceDiskSave) ShoppingListLibrary.getLibrary(getResources()).executeSaveComponents(this, ShoppingListHubActivity.getAppDirectory());

    }

    public ShoppingList getList() {

        return this.list;

    }

    public void addNewItemClicked() {

        //called when an item should be added
        EditText addItemEditText = (EditText) addItemView.findViewById(R.id.shopping_list_new_item_edittext);

        String name = addItemEditText.getText().toString();
        Material material = materialChooseListener.getCurrentMaterial();

        if(!name.isEmpty()) {

            addNewItem(name, material);
            Log.v("ShoppingListAddActivity", "Added new item " + name);

        }

    }

    public void addNewItem(String name, Material material) {

        ShoppingListItem item = new ShoppingListItem(name, material, false);
        this.list.addItem(item);

        this.shoppingListAdapter.notifyItemInserted(this.list.getItemsCount());

        resetNewItemFields();

    }

    public void resetNewItemFields() {

        EditText addItemEditText = (EditText) addItemView.findViewById(R.id.shopping_list_new_item_edittext);
        addItemEditText.setText("");

        ImageButton imageButton = (ImageButton) addItemView.findViewById(R.id.shopping_list_new_item_category_imagebutton);
        imageButton.setImageResource(android.R.drawable.sym_call_incoming);

        this.materialChooseListener = new ChooseMaterialClickListener();
        imageButton.setOnClickListener(this.materialChooseListener);

    }

    private class ShoppingItemClickListener implements ShoppingListRecyclerViewAdapter.ShoppingItemClickListener {

        @Override
        public void onClickShoppingItem(ShoppingListItem item, int position) {

            //called when a shopping item in a list is clicked
            Snackbar snackbar = Snackbar.make(ShoppingListAddActivity.this.findViewById(R.id.activity_shopping_list_add_root), "Item clicked: " + item.getName(), Snackbar.LENGTH_SHORT);
            snackbar.show();

        }

    }


    private class AddItemClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View view) {

            addNewItemClicked();

        }

    }

    //für den material / category chooser
    private class ChooseMaterialClickListener implements AdapterView.OnClickListener, MaterialChooser.MaterialChosenListener {

        private Material currentMaterial = Material.BAKED_GOODS; //TODO remove! only for testing
        private MaterialChooser chooser;
        private ImageButton button;

        @Override
        public void onClick(View view) {

            button = (ImageButton) view; //maybe set the drawable resource to something else

            List<Material> materialList = Arrays.asList(Material.values());
            this.chooser = new MaterialChooser(ShoppingListAddActivity.this, view, materialList, this);

            this.chooser.show();

        }

        @Override
        public void onMaterialChosen(Material material) {

            Log.v("ShoppingListAddActivity", "Material " + material.getName() + " chosen");
            this.currentMaterial = material;

            this.button.setImageResource(material.getIconResourceId());

        }

        public Material getCurrentMaterial() {
            return this.currentMaterial;
        }

    }

    private class ColorChoseListener implements ColorChooserAdapter.ColorChooserListener {

        private int color = -1;

        @Override
        public void onColorChosen(int color) {

            //called when a color is chosen
            this.color = color;

            Snackbar snackbar = Snackbar.make(ShoppingListAddActivity.this.findViewById(R.id.activity_shopping_list_add_root), "Color chosen: " + color, Snackbar.LENGTH_SHORT);
            snackbar.show();

            Toolbar toolbar = (Toolbar) findViewById(R.id.activity_shopping_list_add_toolbar);
            toolbar.setBackgroundResource(color); //TODO also change the color dark color set!

        }

        public int getLatestColor() {

            return this.color;

        }

    }

    //TODO duplicate code from ListHubActivity!


}
