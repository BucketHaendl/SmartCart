package com.buckethaendl.smartcart.objects.shoppinglist;

import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.io.Serializable;

/**
 * Created by Cedric on 27.07.16.
 */
public class SortedShoppingListItem extends ShoppingListItem implements Comparable<SortedShoppingListItem>, Serializable {

    private transient Shelf shelf;

    public SortedShoppingListItem(ShoppingListItem item, Shelf shelf) {

        super(item.getDisplayName(), item.isChecked(), item.isUnknown());

        this.shelf = shelf;

    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    /**
     * Compares this shopping list item to another sorted item
     */
    @Override
    public int compareTo(SortedShoppingListItem item) {

        if(this.getShelf().getPriority() > item.getShelf().getPriority()) return 1;

        else if(this.getShelf().getPriority() == item.getShelf().getPriority()) return 0;

        else return -1;

    }

}
