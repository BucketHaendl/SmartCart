package com.buckethaendl.smartcart.objects.shoppinglist;

import java.io.Serializable;

/**
 * An object representing a single item from a shopping list of SmartCart.
 * The private field checked determines if the item was already bought and is thus ticked in the shopping list.
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingListItem implements Serializable {

    public static final String TAG = ShoppingListItem.class.getName();

    public transient static final String PRE_REGEX = ",?\\d+[A-Z,a-z]*,?"; //to identify pre-string (200g, 3x)
    public transient static final long serialVersionUID = 1L;

    private String displayName;
    private boolean checked;
    private boolean unknown; //if the item is not known

    /**
     * Create a new item for a shopping list
     * @param displayName of the item (e.g. bread)
     */
    public ShoppingListItem(String displayName) {

        this(displayName, false);

    }

    /**
     * Create a new item for a shopping list
     * @param displayName of the item (e.g. bread)
     * @param checked true if the item was already bought and should appear ticked
     */
    public ShoppingListItem(String displayName, boolean checked) {

        this(displayName, checked, false);

    }

    /**
     * Create a new item for a shopping list
     * @param displayName of the item (e.g. bread)
     * @param checked true if the item was already bought and should appear ticked
     * @param unknown true if the item should appear as not known
     */
    public ShoppingListItem(String displayName, boolean checked, boolean unknown) {

        this.displayName = displayName;
        this.checked = checked;
        this.unknown = unknown;

    }

    //Getters & Setters
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns a search string, formated in a readable way to insert into WaSa
     * @return a format string
     */
    public String getFormatedName() {

        //Log.v(TAG, "Formating name '" + this.displayName + "'");

        String name = this.displayName;
        name = name.trim();

        String[] split = name.split(" "); //split on spaces
        StringBuilder builder = new StringBuilder();

        for(String part : split) {

            part = part.trim();

            if(part.matches(PRE_REGEX)) {

                //Log.v(TAG, "Found PRE_REGEX: '" + part + "'");
                //ignore

            }

            else {

                //Log.v(TAG, "Found product name: '" + part + "'");
                builder.append(part);
                builder.append(" ");

            }

        }

        String ret = builder.toString().trim();
        return ret;

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

        return this.getDisplayName();

    }


}
