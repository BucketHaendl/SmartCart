package com.buckethaendl.smartcart.activities.choosestore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.instore.InStoreActivity;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.choosestore.Market;
import com.buckethaendl.smartcart.objects.choosestore.Opening;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;

import java.util.Calendar;

public class DetailStoreActivity extends AppCompatActivity {

    public static final String TAG = DetailStoreActivity.class.getName();
    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";
    public static final String EXTRA_MARKET_SERIALIZABLE = "extra_market_serializable";

    private ShoppingList list;
    private Market market;

    private ImageView logoView;
    private TextView nameText;
    private TextView addressText;
    private TextView openingText;
    private TextView shoppingListText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_store);

        int listId;

        //restore list details
        if(savedInstanceState != null) {

            listId = savedInstanceState.getInt(EXTRA_SHOPPING_LIST_ID);
            this.market = (Market) savedInstanceState.getSerializable(EXTRA_MARKET_SERIALIZABLE);

        }

        else {

            listId = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);
            this.market = (Market) getIntent().getSerializableExtra(EXTRA_MARKET_SERIALIZABLE);

        }

        //get information about the selected shopping list
        this.list = ShoppingListLibrary.getInstance().getShoppingList(listId);

        //set toolbar
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_detail_store_toolbar);
        this.setSupportActionBar(toolbar);

        if(this.getSupportActionBar() != null) this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set FAB
        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.activity_detail_store_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                initiateSortShoppingList();

            }

        });

        this.fillViews();

    }

    private void fillViews() {

        //assign view variables
        this.logoView = (ImageView) this.findViewById(R.id.activity_detail_store_logo_imageview);
        this.nameText = (TextView) this.findViewById(R.id.activity_detail_store_name_textview);
        this.addressText = (TextView) this.findViewById(R.id.activity_detail_store_address_textview);
        this.openingText = (TextView) this.findViewById(R.id.activity_detail_store_opening_textview);
        this.shoppingListText = (TextView) this.findViewById(R.id.activity_detail_store_shopping_list_textview);

        //set view contents with variables
        Drawable logo = ContextCompat.getDrawable(this, this.market.getDrawableId());
        this.logoView.setImageDrawable(logo);
        this.logoView.setContentDescription(this.market.getCompany().getName());

        this.nameText.setText(this.market.getCompany().getName());
        this.addressText.setText(getString(R.string.detail_store_activity_store_address_format, market.getStreet(), market.getZipcode(), market.getCity()));

        StringBuilder openingData = new StringBuilder();

        for(Opening opening : this.market.getOpeningHours()) {

            Calendar open = getCalendarFromInt(opening.getOpen());
            Calendar close = getCalendarFromInt(opening.getClose());

            String string = getString(R.string.detail_store_activity_standard_opening_format, opening.getWeekday(), open.get(Calendar.HOUR_OF_DAY), open.get(Calendar.MINUTE), close.get(Calendar.HOUR_OF_DAY), close.get(Calendar.MINUTE));
            openingData.append(string);
            openingData.append("\n");

        }

        openingData.append("\n");

        for(Opening opening : this.market.getSpecialOpeningHours()) {

            Calendar open = getCalendarFromInt(opening.getOpen());
            Calendar close = getCalendarFromInt(opening.getClose());

            String string = getString(R.string.detail_store_activity_standard_opening_format, opening.getWeekday(), open.get(Calendar.HOUR_OF_DAY), open.get(Calendar.MINUTE), close.get(Calendar.HOUR_OF_DAY), close.get(Calendar.MINUTE));
            openingData.append(string);
            openingData.append("\n");

        }

        this.openingText.setText(openingData.toString());
        this.shoppingListText.setText(getString(R.string.detail_store_activity_shopping_list_format, this.list.getName()));

    }

    public Calendar getCalendarFromInt(int rawTime) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, rawTime % 60);
        cal.set(Calendar.HOUR_OF_DAY, rawTime / 100);

        return cal;

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);

        bundle.putInt(EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(list));
        bundle.putSerializable(EXTRA_MARKET_SERIALIZABLE, this.market);

    }

    private void initiateSortShoppingList() {

        if(this.list != null) {

            Log.v(TAG, "Starting to sort shopping list");

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.in_store_calc_dialog_title);
            dialog.setMessage(this.getString(R.string.in_store_calc_dialog_message));
            dialog.setIcon(R.drawable.garlic_icon);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(this.list.size());

            dialog.show();

            String rawStoreId = this.market.getStoreId().replaceAll("[A-Z,a-z]*", "");
            final int storeId = Integer.parseInt(rawStoreId);

            this.list.sortList(this.market.getCountry(), storeId, new ShoppingList.SortingListListener() {

                @Override
                public void updateProgress(int finishedItems, ShoppingListItem currentItem) {

                    dialog.setProgress(finishedItems);
                    dialog.setMessage(getString(R.string.in_store_calc_dialog_message, currentItem.getFormatedName()));

                }

                @Override
                public void finishedSorting(ShoppingList list) {

                    dialog.dismiss();

                    Log.v(TAG, "Start shopping");

                    Intent intent = new Intent(DetailStoreActivity.this, InStoreActivity.class);
                    intent.putExtra(InStoreActivity.EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(list));

                    startActivity(intent);


                }

            });

        }

    }

}
