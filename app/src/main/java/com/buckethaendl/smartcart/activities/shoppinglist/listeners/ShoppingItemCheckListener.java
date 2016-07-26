package com.buckethaendl.smartcart.activities.shoppinglist.listeners;

import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;

/**
 * Created by Cedric on 26.07.16.
 */
public interface ShoppingItemCheckListener {

    void onCheckShoppingItem(ShoppingListItem item, int position, boolean checked);

}
