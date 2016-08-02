package com.buckethaendl.smartcart.activities.instore.adapters;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.buckethaendl.smartcart.activities.instore.listeners.RouteClickListener;
import com.buckethaendl.smartcart.activities.shoppinglist.adapters.ShoppingListRecyclerViewAdapter;
import com.buckethaendl.smartcart.objects.instore.Material;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;
import com.buckethaendl.smartcart.objects.shoppinglist.SortedShoppingListItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cedric on 29.07.16.
 */
public class InStoreRecyclerViewAdapter extends ShoppingListRecyclerViewAdapter {

    public static final String TAG = InStoreRecyclerViewAdapter.class.getName();

    private Map<ShoppingListItem, Material> shownMaterials = new HashMap<ShoppingListItem, Material>();
    private RouteClickListener listener;

    public InStoreRecyclerViewAdapter(ShoppingList list) {

        super(list);

    }

    @Override
    public void onBindViewHolder(final ShoppingItemViewHolder holder, final int position) {

        //set all default / normal values
        super.onBindViewHolder(holder, position);

        //set the category icon
        final ShoppingListItem item = this.list.get(position);

        if(item instanceof SortedShoppingListItem) {

            final SortedShoppingListItem sortedItem = ((SortedShoppingListItem) item);
            ImageButton categoryButton = holder.getImageButton();
            int priority = sortedItem.getShelf().getPriority();

            final Material mat = Material.getMaterial(Material.getMaterialId(priority));

            if(mat != null) {

                //if this material was never seen by now (first item with this category)
                if(!shownMaterials.containsValue(mat)) {

                    shownMaterials.put(item, mat);

                }

                //check if this item has an associated material to display
                if(shownMaterials.containsKey(item)) {

                    categoryButton.setImageDrawable(ContextCompat.getDrawable(categoryButton.getContext(), mat.getIconId()));
                    categoryButton.setVisibility(View.VISIBLE);

                    Log.v(TAG, "Showing material " + mat.getId());

                }

                //else, set to invisible
                else categoryButton.setVisibility(View.INVISIBLE);

                //trigger listener
                holder.getView().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(listener != null) listener.onClickShowRoute(sortedItem, mat);

                    }

                });

                holder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                        //only if the item was checked
                        if(checked) {

                            if(listener != null) listener.onCheck(sortedItem, mat);

                        }

                    }

                });

            }

            else {

                categoryButton.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Material of priority " + priority + " not found");

            }


        }

        else Log.e(TAG, "Item " + item.getFormatedName() + " was not sorted in!"); //todo maybe show error icon (!) next to item

    }

    public RouteClickListener getRouteClickListener() {
        return this.listener;
    }

    public void setRouteClickListener(RouteClickListener listener) {
        this.listener = listener;
    }

}
