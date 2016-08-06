package com.buckethaendl.smartcart.activities.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.IconChooseAdapter;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListRecyclerViewAdapter;
import com.buckethaendl.smartcart.activities.shoppinglist.listeners.ShoppingItemClickListener;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;
import com.buckethaendl.smartcart.util.DialogBuildingSite;
import com.buckethaendl.smartcart.util.KeyboardUtil;

import java.util.Calendar;

/**
 * This activity is used for new shopping lists (empty), as well as editing existing ones!
 */
public class ShoppingListNewActivity extends AppCompatActivity {

    public static final String TAG = "ShoppingListNewAc";

    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";

    protected ShoppingList list;

    protected RecyclerView recycler;
    protected ShoppingListRecyclerViewAdapter adapter;

    private IconChooseListener iconChooseListener;

    private View addItemView;
    private TextView addItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_shopping_list_new);

        int listId = -1;

        //restore list details
        if(savedInstanceState != null) {

            listId = savedInstanceState.getInt(EXTRA_SHOPPING_LIST_ID);

        }

        else {

            if(getIntent().hasExtra(EXTRA_SHOPPING_LIST_ID)) listId = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);

        }

        if(listId != -1) {

            //get information about the selected shopping list
            this.list = ShoppingListLibrary.getInstance().getShoppingList(listId);

            //Set the loaded values
            TextView title = (TextView) this.findViewById(R.id.activity_shopping_list_add_name_edittext);
            title.setText(this.list.getName());

        }

        else {

            //Get information about the selected shopping list
            this.list = new ShoppingList(getString(R.string.shopping_list_add_activity_new_list_name), Calendar.getInstance());
            Log.v(TAG, "New list created");

        }

        //Set the color chooser recyclerview and use adapter + onClickListener
        RecyclerView colorChooser = (RecyclerView) this.findViewById(R.id.activity_shopping_list_add_color_chooser);

        IconChooseAdapter iconChooseAdapter = new IconChooseAdapter();
        colorChooser.setAdapter(iconChooseAdapter);

        LinearLayoutManager colorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        colorChooser.setLayoutManager(colorLayoutManager);

        this.iconChooseListener = new IconChooseListener(this.list.getIconId());
        iconChooseAdapter.setListener(this.iconChooseListener);

        //Set the recycler view and listener
        this.recycler = (RecyclerView) this.findViewById(R.id.activity_shopping_list_add_content_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        this.recycler.setLayoutManager(manager);

        //set the adapter
        this.adapter = new ShoppingListRecyclerViewAdapter(this.list);
        this.recycler.setAdapter(this.adapter);

        this.adapter.setShoppingItemClickListener(new ShoppingItemClickListener() {

            @Override
            public void onClickShoppingItem(ShoppingListItem item, int position) {

                //hide keyboard if clicked outside the add new item section
                KeyboardUtil.hideKeyboard(ShoppingListNewActivity.this);

                Log.v(TAG, "Closed keyboard now");

            }

        });

        //Set the add item logic
        this.addItemView = this.findViewById(R.id.activity_shopping_list_add_new_item_include);
        this.addItemText = (TextView) this.addItemView.findViewById(R.id.shopping_list_new_item_edittext);

        final ImageButton addButton = (ImageButton) this.addItemView.findViewById(R.id.shopping_list_new_item_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                addNewItem();

            }

        });

        this.addItemText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId == EditorInfo.IME_ACTION_DONE) {

                    addNewItem();
                    return true;

                }

                if(keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                    addNewItem();
                    return true;

                }

                return false;

            }

        });

        //Set the toolbar & Up-Navigation
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_shopping_list_add_toolbar);
        this.setSupportActionBar(toolbar);

        if(this.getSupportActionBar() != null) this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.shopping_list_new_menu, menu); TODO reimplement when working!
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home:

                if(this.isWorthSaving()) this.saveCurrentListState(true);
                return super.onOptionsItemSelected(item);

            case R.id.shopping_list_new_delete_menuitem:

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

        outState.putInt(EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(this.list));

    }

    @Override
    protected void onStop() {

        super.onStop();
        KeyboardUtil.hideKeyboard(this);

    }

    /**
     * Adds a new item with the given name to the shopping list
     */
    public void addNewItem() {

        //called when an item should be added
        //todo keine Überprüfung bisher
        EditText addItemEditText = (EditText) addItemView.findViewById(R.id.shopping_list_new_item_edittext);
        String name = addItemEditText.getText().toString();

        if(!name.isEmpty()) {

            ShoppingListItem item = new ShoppingListItem(name, false);
            this.list.add(item);

            this.adapter.notifyDataSetChanged();

            resetNewItem();

            Log.v("ShoppingListAddActivity", "Added new item " + name);

        }

    }

    /**
     * Resets the new item fields, so a new item can be added
     */
    public void resetNewItem() {

        EditText addItemEditText = (EditText) addItemView.findViewById(R.id.shopping_list_new_item_edittext);
        addItemEditText.setText("");

    }

    /*
    //Beginning of implementing save logic
    //There are three valid ways to save the list: done button, up-button (is implemented in onOptionsItemSelected()) & back button
     */
    public void onDoneButtonClick(View view) {

        EditText addItemEditText = (EditText) addItemView.findViewById(R.id.shopping_list_new_item_edittext);

        if(!addItemEditText.getText().toString().isEmpty()) {

            addNewItem();

        }

        else {

            if(this.isWorthSaving()) this.saveCurrentListState(true);
            this.finish();

        }


    }

    @Override
    public void onBackPressed() {

        Toast toast = Toast.makeText(this, "Toast", Toast.LENGTH_SHORT);
        toast.show();

        if(this.isWorthSaving()) this.saveCurrentListState(true);
        super.onBackPressed();

    }

    /**
     * Determines, if the list should be saved or if it is empty
     * @return true if the list should be saved, false if not
     */
    public boolean isWorthSaving() {

        EditText titleEditText = (EditText) this.findViewById(R.id.activity_shopping_list_add_name_edittext);

        if(titleEditText.getText().toString().isEmpty()) {

            return (this.list.size() > 0);

        }

        else return true;

    }

    /**
     * Adds the current list to the ShoppingListLibrary object in memory
     */
    public void saveCurrentListState() {

        this.saveCurrentListState(false);

    }

    /**
     * Adds the current list to the ShoppingListLibrary object in memory
     * @param saveImmediately defines if the library should be saved directly after adding the list to the library
     *                        set to true to force disk save
     *                        set to false to not force disk save
     */
    public void saveCurrentListState(boolean saveImmediately) {

        //Add the add item, that might not have been added fully by now (just to make sure, we don't lose this last item)
        addNewItem();

        //Save the title and icon of the list
        EditText titleEditText = (EditText) this.findViewById(R.id.activity_shopping_list_add_name_edittext);
        if(!titleEditText.getText().toString().isEmpty()) this.list.setName(titleEditText.getText().toString());
        this.list.setIconId(this.iconChooseListener.getIconId());

        //If the list is a new one
        if(!ShoppingListLibrary.getInstance().containsList(list)) ShoppingListLibrary.getInstance().addList(this.list);

        if(saveImmediately) ShoppingListLibrary.getInstance().saveLibrary(this);

    }
    /*
    //End of implementing save logic
    */

    /**
     * A listener that stores the currently selected icon
     * Can be used to retrieve the icon later on again (e.g. for saving the list)
     */
    private class IconChooseListener implements IconChooseAdapter.IconChooseListener {

        public static final int DEFAULT_ICON = R.drawable.list_apple_icon;
        private int iconId;

        public IconChooseListener() {

            iconId = DEFAULT_ICON;

        }

        public IconChooseListener(int iconId) {

            this.iconId = iconId;

        }

        /**
         * Called when an icon was chosen from the icon recycler
         * @param iconId The selected icon id
         */
        @Override
        public void onIconChosen(int iconId) {

            //called when a color is chosen
            this.iconId = iconId;

        }

        /**
         * Get back the latestly set icon id
         * @return an int representing the icon id of type drawable (R.drawable.<id>)
         */
        public int getIconId() {

            return this.iconId;

        }

    }

}
