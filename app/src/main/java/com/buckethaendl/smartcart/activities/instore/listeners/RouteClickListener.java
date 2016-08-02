package com.buckethaendl.smartcart.activities.instore.listeners;

import com.buckethaendl.smartcart.objects.instore.Material;
import com.buckethaendl.smartcart.objects.shoppinglist.SortedShoppingListItem;

/**
 * Created by Cedric on 02.08.16.
 */
public interface RouteClickListener {

    void onClickShowRoute(SortedShoppingListItem toItem, Material toMaterial);
    void onCheck(SortedShoppingListItem item, Material material);

}
