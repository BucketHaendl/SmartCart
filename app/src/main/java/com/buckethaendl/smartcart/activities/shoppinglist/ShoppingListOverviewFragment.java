package com.buckethaendl.smartcart.activities.shoppinglist;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.objects.shoppingList.MenuBarFragmentListener;
import com.buckethaendl.smartcart.objects.shoppingList.RefreshAdapterListener;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListLibrary;

import java.util.List;

public class ShoppingListOverviewFragment extends ListFragment implements RefreshAdapterListener {

    private ArrayAdapter<ShoppingList> shoppingListArrayAdapter;

    private Context context;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.context = activity;

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //Set click listener
        ShoppingListClickListener shoppingListClickListener = new ShoppingListClickListener();
        getListView().setOnItemClickListener(shoppingListClickListener);

    }

    @Override
    public void onRefreshListAdapter() {

        this.onRefreshListAdapter(false);

    }

    @Override
    public void onRefreshListAdapter(boolean force) {

        this.showShoppingLists(force);

    }

    /**
     * Updates the arrayAdapter to notify it, that the data set has changed and shopping lists were added or removed from the Library
     * This method doesn't reload the shopping lists from the local binary file, but only shows the ones currently known by the Library.
     * Call loadLocalShoppingLists() to deserialize the locally saved lists first
     */
    public void showShoppingLists() {

        this.showShoppingLists(false);

    }

    /**
     * Updates the arrayAdapter to notify it, that the data set has changed and shopping lists were added or removed from the Library
     * This method doesn't reload the shopping lists from the local binary file, but only shows the ones currently known by the Library.
     * Call loadLocalShoppingLists() to deserialize the locally saved lists first
     * @param forceRecreate Set to true if you want to force the ArrayAdapter to regenerate, no matter if it pre-existed
     */
    public void showShoppingLists(boolean forceRecreate) {

        if(ShoppingListOverviewFragment.this.shoppingListArrayAdapter == null || forceRecreate) {

            ShoppingListOverviewFragment.this.shoppingListArrayAdapter = new ShoppingListArrayAdapter(ShoppingListOverviewFragment.this.getContext(), R.layout.shopping_list_overview_listitem, ShoppingListLibrary.getLibrary(getResources()).getShoppingLists());
            ShoppingListOverviewFragment.this.shoppingListArrayAdapter.setNotifyOnChange(true); //When entries etc. are removed / added to the list, the view is updated automatically :)

            ShoppingListOverviewFragment.this.setListAdapter(shoppingListArrayAdapter);

        }

        else ShoppingListOverviewFragment.this.shoppingListArrayAdapter.notifyDataSetChanged(); //Should work without this call too (as set to automaticallyUpdate!)

    }

    @Override
    public Context getContext() {

        return this.context;

    }

    public class ShoppingListArrayAdapter extends ArrayAdapter<ShoppingList> {

        private int resource;

        public ShoppingListArrayAdapter(Context context, int resource, List<ShoppingList> shoppingList) {

            super(context, resource, shoppingList);

            this.resource = resource;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ShoppingListLibrary library = ShoppingListLibrary.getLibrary(getResources());
            ShoppingList selectedList = library.getShoppingLists().get(position);

            if(selectedList != null) {

                if(convertView == null) {

                    LayoutInflater inflater = (LayoutInflater) ShoppingListOverviewFragment.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(this.resource, parent, false);

                }

                //Set shopping_list_overview_listitem_circle_imageview

                ImageView circleImageView = (ImageView) convertView.findViewById(R.id.shopping_list_overview_listitem_circle_imageview);
                circleImageView.setImageResource(selectedList.getListState().getIconResourceId()); //puts in a tick with transparent background if the list was done once
                circleImageView.setBackgroundResource(selectedList.getColorId());

                //Set shopping_list_overview_listitem_title_textview

                TextView titleTextView = (TextView) convertView.findViewById(R.id.shopping_list_overview_listitem_title_textview);
                titleTextView.setText(selectedList.getName());

                //Set shopping_list_overview_listitem_date_textview

                TextView dateTextView = (TextView) convertView.findViewById(R.id.shopping_list_overview_listitem_date_textview);
                dateTextView.setText(selectedList.getDateFormatted());

                //Set shopping_list_overview_listitem_info_textview

                TextView infoTextView = (TextView) convertView.findViewById(R.id.shopping_list_overview_listitem_info_textview);
                String itemCount = selectedList.getItemsCount() + " " + getString(R.string.shopping_list_overview_listitem_info_product);

                infoTextView.setText(itemCount);

                return convertView;

            }

            else return super.getView(position, convertView, parent); //Appears to be an item not in my list

        }

    }

    public class ShoppingListClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            //Get selected ShoppingList
            ShoppingListLibrary library = ShoppingListLibrary.getLibrary(getResources());
            ShoppingList selected = library.getShoppingList(id);

            Intent intent = new Intent(ShoppingListOverviewFragment.this.getActivity(), ShoppingListDetailsActivity.class);
            intent.putExtra(ShoppingListDetailsActivity.EXTRA_SHOPPING_LIST_ID, (int) id);
            startActivity(intent);

        }

    }

}
