package com.buckethaendl.smartcart.activities.choosestore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.Refreshable;
import com.buckethaendl.smartcart.activities.choosestore.adapters.ChooseStoreAdapter;
import com.buckethaendl.smartcart.activities.choosestore.listeners.ChooseStoreClickListener;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.data.service.MarketListConnector;
import com.buckethaendl.smartcart.objects.choosestore.Market;
import com.buckethaendl.smartcart.objects.choosestore.MarketDistance;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 01.08.16.
 */
public class ChooseStoreActivity extends AppCompatActivity implements Refreshable {

    public static final String TAG = ChooseStoreActivity.class.getName();

    private List<MarketDistance> marketsNearby = new ArrayList<MarketDistance>();

    private RecyclerView recycler;
    private ChooseStoreAdapter adapter;

    public static final String EXTRA_SHOPPING_LIST_ID = "extra_shopping_list_id";
    private ShoppingList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_store);

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
        this.recycler = (RecyclerView) this.findViewById(R.id.activity_choose_store_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        this.recycler.setLayoutManager(manager);

        //Set up the SupportActionBar
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.activity_choose_store_toolbar);
        this.setSupportActionBar(toolbar);

        //Set up the Up-Navigation
        if(this.getSupportActionBar() != null) {

            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        //initiate loading the list (will be displayed & refreshed automatically
        this.initiateLoadMarkets();

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);

        bundle.putInt(EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(list));

    }

    @Override
    public void refreshView() {

        this.refreshView(false);

    }

    @Override
    public void refreshView(boolean forceCreate) {

        //update recycler
        if(this.adapter == null || forceCreate) {

            this.adapter = new ChooseStoreAdapter(this.marketsNearby);
            this.adapter.setClickListener(new ChooseStoreClickListener() {

                @Override
                public void onClick(Market market, int position) {

                    Log.v(TAG, "Selected id " + position + " with market " + market.toString());

                    Intent intent = new Intent(ChooseStoreActivity.this, DetailStoreActivity.class);

                    intent.putExtra(DetailStoreActivity.EXTRA_SHOPPING_LIST_ID, ShoppingListLibrary.getInstance().indexOf(list));
                    intent.putExtra(DetailStoreActivity.EXTRA_MARKET_SERIALIZABLE, market);

                    startActivity(intent);

                }

            });

            this.recycler.setAdapter(this.adapter);

        }

        else this.adapter.notifyDataSetChanged();

        Log.v(TAG, "Refreshed activity");

    }

    private void initiateLoadMarkets() {

        double userLongitude = 28734;
        double userLatitude = 91843;
        int maxResults = 30;

        //get the location data of the user
        int check = PermissionChecker.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");

        if(check == PermissionChecker.PERMISSION_GRANTED) {

            Log.w(TAG, "Permission for location granted :)");

            LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

            String provider = manager.getBestProvider(criteria, false);

            Location location = manager.getLastKnownLocation(provider);

            if(location != null) {

                userLongitude = location.getLongitude();
                userLatitude = location.getLatitude();

                Log.v(TAG, "Using accurate location (" + userLongitude + "|" + userLatitude + ")");

            }

        }

        else Log.w(TAG, "Permission for location denied! :(");

        //get the markets nearby
        MarketListConnector connector = MarketListConnector.getInstance();
        connector.loadMarketDistancesAsync(userLongitude, userLatitude, maxResults, new LibraryListener<List<MarketDistance>>() {

            private ProgressDialog dialog;

            @Override
            public void onOperationStarted() {

                Log.v(TAG, "Started loading all markets");

                dialog = new ProgressDialog(ChooseStoreActivity.this);
                dialog.setTitle(R.string.loading_stores_dialog_title);
                dialog.setMessage(getString(R.string.loading_stores_dialog_message));
                dialog.setCancelable(false);
                dialog.show();

            }

            @Override
            public void onOperationFinished() {

                dialog.dismiss();
                refreshView(true);

                Log.v(TAG, "Finished loading all markets");

            }

            @Override
            public void onLoadResult(List<MarketDistance> results) {

                marketsNearby.clear();

                if(results != null) {

                    marketsNearby.addAll(results); //if not sorted correctly here, sort again

                    for(MarketDistance distance : marketsNearby) {

                        Log.v(TAG, distance.getMarket().toString() + " | Distance (km): " + distance.getDistance());

                    }

                }


            }

            @Override
            public void onSetInitialized(boolean initialized) {

            }

        });

    }

}
