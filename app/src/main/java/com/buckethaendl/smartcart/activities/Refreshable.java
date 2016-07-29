package com.buckethaendl.smartcart.activities;

/**
 * An interface to make an activity refresh all it's underlying data to the current state
 *
 * Created by Cedric on 27.07.16.
 */
public interface Refreshable {


    /**
     * Updates the adapter to notify it, that the data set has changed and shopping lists were added or removed from the Library
     * This method doesn't reload the shopping lists from the local binary file, but only shows the ones currently known by the Library.
     * Call loadLocalShoppingLists() to deserialize the locally saved lists first
     */
    void refreshView();

    /**
     * Updates the adapter to notify it, that the data set has changed and shopping lists were added or removed from the Library
     * This method doesn't reload the shopping lists from the local binary file, but only shows the ones currently known by the Library.
     * Call loadLocalShoppingLists() to deserialize the locally saved lists first
     * @param forceCreate Set to true if you want to force the ArrayAdapter to regenerate, no matter if it pre-existed
     */
    void refreshView(boolean forceCreate);

}
