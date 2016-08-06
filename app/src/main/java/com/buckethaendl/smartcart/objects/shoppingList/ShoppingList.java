package com.buckethaendl.smartcart.objects.shoppinglist;

import android.os.AsyncTask;
import android.util.Log;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.data.library.WaSaOfflineLibrary;
import com.buckethaendl.smartcart.data.local.sqlite.SQLiteShelfConnector;
import com.buckethaendl.smartcart.data.service.FBBShelf;
import com.buckethaendl.smartcart.data.service.WaSaConnector;
import com.buckethaendl.smartcart.data.service.WaSaFBBShelf;
import com.buckethaendl.smartcart.objects.instore.Shelf;
import com.buckethaendl.smartcart.util.Importancer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * An object representing a shopping list for SmartCart
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingList extends ArrayList<ShoppingListItem> implements Serializable {

    public transient static final String TAG = ShoppingList.class.getName();
    public transient static final long serialVersionUID = 1L;

    private String name;
    private Calendar date;
    private int iconId;
    private boolean done;

    public ShoppingList(String name, Calendar date) {

        this(name, date, Icons.getRandomIcon());

    }

    public ShoppingList(String name, int hour, int minute) {

        this(name, null, Icons.getRandomIcon());
        this.setDate(hour, minute);

    }

    public ShoppingList(String name, Calendar date, int iconId) {

        this(name, date, iconId, null);

    }

    public ShoppingList(String name, Calendar date, int iconId, List<ShoppingListItem> items) {

        this(name, date, iconId, items, false);

    }

    public ShoppingList(String name, Calendar date, int iconId, List<ShoppingListItem> items, boolean done) {

        super();

        this.name = name;
        this.date = date;
        this.done = done;
        this.iconId = iconId;

        if(items != null) this.addAll(items);

    }

    //Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDate() {
        return date;
    }

    /**
     * Gets the date of the list in a readable form of one of the following:
     * > today
     * > yesterday
     * > XX.YY.
     * > XX.YY.ZZZZ
     * Can be used for the shopping_list_overview_listitem_date_textview
     * @return a formatted string showing date information
     */
    public String getDateFormatted() {

        Calendar current = Calendar.getInstance();

        if(current.get(Calendar.DAY_OF_YEAR) == this.date.get(Calendar.DAY_OF_YEAR)) { //when created on the same day (today)

            return App.getGlobalResources().getString(R.string.shopping_list_overview_listitem_date_today);

        }

        else if((current.get(Calendar.DAY_OF_YEAR) - this.date.get(Calendar.DAY_OF_YEAR)) == 1) { //when created one day back (yesterday)

            return App.getGlobalResources().getString(R.string.shopping_list_overview_listitem_date_yesterday);

        }

        else {

            int dayOfMonth = this.date.get(Calendar.DAY_OF_MONTH);
            int month = this.date.get(Calendar.MONTH);
            int year = this.date.get(Calendar.YEAR);

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy", Locale.GERMANY); //todo set locale depending on location (US, ...)

            return format.format(current.getTime());

        }

    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDate(int hour, int minute) {

        this.date = Calendar.getInstance();

        this.date.set(Calendar.HOUR_OF_DAY, hour);
        this.date.set(Calendar.MINUTE, minute);

    }

    public boolean isDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    /**
     * Returns whether this shopping list is currently fully sorted or not
     * @return true if all entries sorted, false if not
     */
    public boolean isSorted() {

        for(ShoppingListItem item : this) {

            //if at least one item is not a sorted one
            if(!(item instanceof SortedShoppingListItem)) return false;

        }

        return true;

    }

    /**
     * Sorts the shopping list according to the perfect shelf sequence in the given market number
     * NOTE that this action requires internet and can have a quite heavy workload!
     * todo maybe only one central background task to do everything in (just one Thread with Listener, etc.) and no multi-threading in itself
     */
    public void sortList(final String country, final int market, final SortingListListener listener, final boolean forceOffline) {

        Log.v(TAG, "Starting sorting list " + this.getName() + " for country " + country + " in market " + market + " offline " + forceOffline);

        final List<SortedShoppingListItem> sortedItems = new ArrayList<SortedShoppingListItem>();
        final SQLiteShelfConnector connector = SQLiteShelfConnector.getInstance();

        AsyncTask<Void, ShoppingListItem, Boolean> sortTask = new AsyncTask<Void, ShoppingListItem, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {

                for (ShoppingListItem item : ShoppingList.this) {

                    Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "Start sorting");
                    this.publishProgress(item);

                    List<? extends FBBShelf> fbbShelves = null;

                    //the corresponding shelves are loaded via the WaSa online service
                    if(!forceOffline) {

                        Log.v(TAG, "Loading online WaSa shelves for " + item.getFormatedName());
                        fbbShelves = WaSaConnector.getInstance().loadShelvesSync(country, market, item.getFormatedName());

                    }

                    else Log.v(TAG, "Forcing offline FBB searching");

                    //loading offline FBBs as well every time! (in case of water, only water is the right shelf, not Obstwasser!)
                    Log.v(TAG, "Loading offline shelves for " + item.getFormatedName());
                    List<? extends FBBShelf> offlineShelves = WaSaOfflineLibrary.getInstance().loadShelvesOffline(country, market, item.getFormatedName());

                    //if shelves found offline
                    if(offlineShelves != null && !offlineShelves.isEmpty()) {

                        //override existing entries when offline results were found
                        fbbShelves = offlineShelves;

                    }

                    Shelf shelf = null;

                    //if still no FBBShelves found
                    if(fbbShelves == null || fbbShelves.isEmpty()) {

                        Log.v(TAG, "No results found for " + item.getDisplayName() + ", set to unknown");

                    }

                    else {

                        //maps found shelves to occurrances and articles
                        final List<Shelf> realShelves = new ArrayList<Shelf>();
                        for (FBBShelf fbbShelf : fbbShelves) {

                            //try to find a shelf for this fbb in the database
                            Shelf correspondingShelf = connector.loadShelfSync(fbbShelf);

                            //only add non-null shelves
                            if(correspondingShelf != null) {

                                realShelves.add(correspondingShelf);
                                Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "Found FBB " + fbbShelf.getFbbNr() + " with article count " + (fbbShelf instanceof WaSaFBBShelf ? ((WaSaFBBShelf) fbbShelf).getArtikel().size() : "1"));

                            }

                        }

                        //todo remove fake - this is just for testing
                        //todo wenn zwei shelves gleich werden sie nicht hinzugeft
                        /*
                        realShelves.add(new Shelf(1202, 1202, 1, 1, null));
                        realShelves.add(new Shelf(200, 200, 1, 1, null));
                        realShelves.add(new Shelf(200, 200, 1, 1, null));
                        realShelves.add(new Shelf(1203, 1203, 1, 1, null));
                        realShelves.add(new Shelf(102, 102, 1, 1, null));
                        realShelves.add(new Shelf(200, 200, 1, 1, null));*/

                        shelf = Importancer.findMostImportant(realShelves);

                    }

                    //check if the shelf could be found
                    if(shelf != null) {

                        Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "Most important shelf: " + shelf.getShelfId() + " (priority: " + shelf.getPriority() + ")");

                    }

                    else {

                        shelf = Shelf.UNKNOWN; //set to unknown
                        Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "No shelf found");

                    }

                    SortedShoppingListItem sortedItem = new SortedShoppingListItem(item, shelf);
                    sortedItems.add(sortedItem);

                }

                if(sortedItems.size() >= ShoppingList.this.size()) {

                    //after all were sorted in
                    Log.v(TAG, "BEFORE: " + ShoppingList.super.toString());

                    Collections.sort(sortedItems);

                    Log.v(TAG, "MIDDLE: " + sortedItems.toString());

                    //reset all values to the sorted form and with
                    ShoppingList.this.clear();
                    ShoppingList.this.addAll(sortedItems);

                    Log.v(TAG, "AFTER: " + ShoppingList.super.toString());

                    Log.v(TAG, "Fully sorted? " + isSorted());

                    return true;

                }

                else {

                    return false;

                }

            }

            @Override
            protected void onProgressUpdate(ShoppingListItem...items) {

                if(listener != null) listener.updateProgress(sortedItems.size(), items[0]);

            }

            @Override
            protected void onPostExecute(Boolean sortedAll) {

                if(sortedAll) Log.v(TAG, "Finished sorting list " + getName() + " for country " + country + " in market " + market);
                else Log.e(TAG, "Could not sort in all items in " + ShoppingList.this.getName());


                //inform listener about finished sorting
                listener.finishedSorting(ShoppingList.this);

            }

        };

        sortTask.execute();

    }

    @Override
    public String toString() {

        return String.format(Locale.GERMANY, "[List Info] Name: %s, Date: %s, Color: %d, Items: %s", name, getDateFormatted(), this.iconId, this.toArray().toString());

    }

    public interface SortingListListener {

        void updateProgress(int finishedItems, ShoppingListItem currentItem);
        void finishedSorting(ShoppingList list);

    }

            /*
        Async Method
        Formerly used
        Uses multi-threading, but bit more complicated
        for(final ShoppingListItem item : this) {

            Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "Start sorting");

            //for every item, the corresponding shelves are loaded
            WaSaConnector.getInstance().loadShelvesAsync(country, market, item.getFormatedName(), new LibraryListener<List<WaSaFBBShelf>>() {

                @Override
                public void onOperationStarted() {

                }

                @Override
                public void onLoadResult(final List<WaSaFBBShelf> result) {

                    final List<Shelf> realShelves = new ArrayList<Shelf>(); //maps found shelves to occurrances

                    for(final WaSaFBBShelf fbbShelf : result) {

                        Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "Found FBB " + fbbShelf.getFbbNr());

                        //todo kann es hier zu sync problemen kommen? beim reinen auslesen und schreiben?
                        connector.loadShelfAsync(fbbShelf, new LibraryListener<Shelf>() {

                            @Override
                            public void onOperationStarted() {

                            }

                            @Override
                            public void onLoadResult(Shelf result) {

                                realShelves.add(result); //todo was passiert hier, wenn null hinzugefüt wird?

                            }

                            @Override
                            public void onSetInitialized(boolean initialized) {

                            }

                            @Override
                            public void onOperationFinished() {

                                //wait for all shelves to be retrieved
                                if(realShelves.size() >= result.size()) {

                                    //Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "All shelves: " + realShelves.toString());
                                    Shelf shelf = Importancer.findMostImportant(realShelves);

                                    Log.v(TAG, "[" + item.getFormatedName().toUpperCase() + "] " + "MOST IMPORTANT SHELF: " + shelf.getShelfId() + " (priority: " + shelf.getPriority() + ")");

                                    SortedShoppingListItem sortedItem = new SortedShoppingListItem(item, shelf);
                                    sortedItems.add(sortedItem);

                                }

                                //after all were sorted in
                                if(sortedItems.size() >= ShoppingList.this.size()) {

                                    Log.v(TAG, "BEFORE: " + ShoppingList.super.toString());

                                    Collections.sort(sortedItems);

                                    //reset all values to the sorted form and with
                                    ShoppingList.this.clear();
                                    ShoppingList.this.addAll(sortedItems);

                                    Log.v(TAG, "AFTER: " + ShoppingList.super.toString());

                                    Log.v(TAG, "Is sorted? " + isSorted());

                                    //inform listener about finished sorting
                                    listener.finishedSorting(ShoppingList.this);
                                    Log.v(TAG, "Finished sorting list " + getName() + " for country " + country + " in market " + market);

                                }

                            }

                        });


                    }


                }

                @Override
                public void onSetInitialized(boolean initialized) {

                }

                @Override
                public void onOperationFinished() {

                }

            });

        }

        Log.v(TAG, "Finished calling all threads...");*/

}
