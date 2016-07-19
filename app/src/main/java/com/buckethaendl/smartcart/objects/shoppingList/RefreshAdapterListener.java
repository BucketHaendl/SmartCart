package com.buckethaendl.smartcart.objects.shoppingList;

/**
 * An interface used as a link to enable activity-fragment comunication.
 * In the ideal case, the fragment retrieves it's values from an underlying 3rd source like a Library object and knows how to handle a refresh method call
 *
 * Created by Cedric on 22.03.2016.
 */
public interface RefreshAdapterListener {

    /**
     * Updates or refreshes the list adapter of this list to show the latest version of entries.
     */
    void onRefreshListAdapter();

    /**
     * Updates or refreshes the list adapter of this list to show the latest version of entries.
     * @param force true to force a recreation of the list adapter with new values
     */
    void onRefreshListAdapter(boolean force);

}
