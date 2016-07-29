package com.buckethaendl.smartcart.activities.choosestore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.instore.InStoreActivity;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;

/**
 * todo just a placeholder activity by now for the store chooser & confirmation screen
 */
public class ChooseStoreActivity extends AppCompatActivity {

    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";
    private ShoppingList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store);

        //Get information about the selected shopping list
        try {

            int id = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);
            this.list = ShoppingListLibrary.getInstance().getShoppingList(id);

        }

        catch (IndexOutOfBoundsException e) {
        }

        Button button = (Button) this.findViewById(R.id.activity_choose_store_start_button);
        button.setText(String.format("Start Shopping with %s", list.getName()));

    }

    public void onStartShoppingButtonClick(View view) {

        if(this.list != null) {

            Log.v("ChooseStoreActivity", "Clicked sorting button");

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.in_store_calc_dialog_title);
            dialog.setMessage(this.getString(R.string.in_store_calc_dialog_message));
            dialog.setIcon(R.drawable.garlic_icon);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(this.list.size());

            dialog.show();

            this.list.sortList("DE", 3000, new ShoppingList.SortingListListener() {

                @Override
                public void updateProgress(int finishedItems, ShoppingListItem currentItem) {

                    dialog.setProgress(finishedItems);
                    dialog.setMessage(getString(R.string.in_store_calc_dialog_message, currentItem.getFormatedName()));

                }

                @Override
                public void finishedSorting(ShoppingList list) {

                    dialog.dismiss();

                    Log.v("ChooseStoreActivity", "Start shopping");

                    Intent intent = new Intent(ChooseStoreActivity.this, InStoreActivity.class);
                    intent.putExtra(InStoreActivity.EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(list));

                    startActivity(intent);


                }

            });

        }

    }

}
