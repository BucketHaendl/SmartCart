package com.buckethaendl.smartcart.objects.shoppinglist;

import java.io.Serializable;

/**
 * An object representing a single item from a shopping list of SmartCart.
 * The private field checked determines if the item was already bought and is thus ticked in the shopping list.
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingListItem implements Serializable {

    public transient static final long serialVersionUID = 1L;

    private String name;
    private boolean checked;
    private boolean unknown; //if the item is not known

    /**
     * Create a new item for a shopping list
     * @param name of the item (e.g. bread)
     */
    public ShoppingListItem(String name) {

        this(name, false);

    }

    /**
     * Create a new item for a shopping list
     * @param name of the item (e.g. bread)
     * @param checked true if the item was already bought and should appear ticked
     */
    public ShoppingListItem(String name, boolean checked) {

        this(name, checked, false);

    }

    /**
     * Create a new item for a shopping list
     * @param name of the item (e.g. bread)
     * @param checked true if the item was already bought and should appear ticked
     * @param unknown true if the item should appear as not known
     */
    public ShoppingListItem(String name, boolean checked, boolean unknown) {

        this.name = name;
        this.checked = checked;
        this.unknown = unknown;

    }

    //Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public void setUnknown(boolean unknown) {
        this.unknown = unknown;
    }

    @Override
    public String toString() {

        return this.getName();

    }


}
