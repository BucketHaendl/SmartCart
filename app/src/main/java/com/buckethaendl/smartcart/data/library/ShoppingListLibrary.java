package com.buckethaendl.smartcart.data.library;

import android.content.Context;
import android.util.Log;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.data.local.FileInteractable;
import com.buckethaendl.smartcart.data.local.FileLibrary;
import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.objects.exceptions.NotInitializedException;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Holds references to all loaded shopping lists and organizes all IO handling around this topic
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingListLibrary implements ListenerLibrary {

    public static final String TAG = ShoppingListLibrary.class.getName();

    private static ShoppingListLibrary instance;

    private FileInteractable file;
    private boolean listsInitialized = false;
    private ArrayList<ShoppingList> shoppingLists = new ArrayList<ShoppingList>(); //todo vielleicht sollte Library eine (Abstrakte?, welche methoden sind immer anders?) Klasse mit Generics sein...dann muss man nicht jede library neu schreiben in neuer klasse//Will be (de-)serialized by the Library interface methods. The list is only assigned to an ArrayList reference once in the constructor so when changed, any Android ListAdapters easily update.

    private List<LibraryRefreshListener> listeners = new ArrayList<LibraryRefreshListener>(); //listeners, listening on refreshes or changes in the underlying library

    static {

        new ShoppingListLibrary();

    }

    private ShoppingListLibrary() {

        ShoppingListLibrary.instance = this;
        this.file = FileLibrary.getInstance().getEKZFileManager();

    }

    /**
     * Gets the only instance of this library
     * @return An object of this library
     */
    public static ShoppingListLibrary getInstance() {

        return ShoppingListLibrary.instance;

    }

    public void createTestLists() {

        //TODO remove when working serialisation is implemented
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 77);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DAY_OF_YEAR, 10);
        cal2.set(Calendar.MONTH, 3);

        ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();
        items.add(new ShoppingListItem("Butter"));
        items.add(new ShoppingListItem("Bread"));
        items.add(new ShoppingListItem("5 Apples", true, true));
        items.add(new ShoppingListItem("3 Bananas"));
        items.add(new ShoppingListItem("Kiwi"));
        items.add(new ShoppingListItem("Cereal"));
        items.add(new ShoppingListItem("7 Up"));
        items.add(new ShoppingListItem("Cucumber"));
        items.add(new ShoppingListItem("Butter"));
        items.add(new ShoppingListItem("Bread"));
        items.add(new ShoppingListItem("5 Apples"));
        items.add(new ShoppingListItem("3 Bananas"));
        items.add(new ShoppingListItem("Kiwi"));
        items.add(new ShoppingListItem("Cereal"));
        items.add(new ShoppingListItem("7 Up"));
        items.add(new ShoppingListItem("Cucumber"));
        items.add(new ShoppingListItem("Butter"));
        items.add(new ShoppingListItem("Bread"));
        items.add(new ShoppingListItem("5 Apples"));
        items.add(new ShoppingListItem("3 Bananas"));
        items.add(new ShoppingListItem("Kiwi"));
        items.add(new ShoppingListItem("Cereal"));
        items.add(new ShoppingListItem("7 Up"));
        items.add(new ShoppingListItem("Cucumber"));

        ShoppingList list1 = new ShoppingList("Abendessen", cal, R.drawable.apple_icon, items);

        items.add(new ShoppingListItem("4x Pasta a la Mama", true));
        items.add(new ShoppingListItem("4x Pasta a la Mama", true, true));

        ShoppingList list2 = new ShoppingList("Wocheneinkauf", cal, R.drawable.carrot_icon, items);
        ShoppingList list3 = new ShoppingList("Für Mum", cal2, R.drawable.sweet_icon);

        this.addList(list1);
        this.addList(list2);
        this.addList(list3);

    }

    /**
     * Instantiates a new, empty shopping list object
     * @return the newly created list
     */
    public ShoppingList createNewList() { //TODO this is the only method able to create new lists for the AddActivity

        String newListName = App.getGlobalResources().getString(R.string.shopping_list_add_activity_new_list_name);
        return new ShoppingList(newListName, Calendar.getInstance());

    }

    public void addList(ShoppingList list) {

        this.shoppingLists.add(list);
        this.notifyListeners();

    }

    public boolean containsList(ShoppingList list) {

        return this.shoppingLists.contains(list);

    }

    public void removeList(ShoppingList list) {

        if(this.shoppingLists.contains(list)) {

            this.shoppingLists.remove(list);
            this.notifyListeners();

        }

    }

    /**
     * Getter for all shopping lists that are currently loaded
     * @return The list of currently existing shopping lists
     * @throws NotInitializedException if the method loadLibrary() has never been executed and maybe existing objects were not deserialized
     */
    public List<ShoppingList> getShoppingLists() throws NotInitializedException {

        if(!this.isInitialized()) throw new NotInitializedException("The List<ShoppingList> in ShoppingListLibrary wasn't loaded yet! Call loadLibrary() first!");

        else return this.shoppingLists;

    }

    /**
     * Getter for a specific shopping lists out of the currently loaded ones
     * @param id the ID of the list in the ArrayList
     * @return The shopping list with this ID
     * @throws NotInitializedException if the method loadLibrary() has never been executed and maybe existing objects were not deserialized
     * @throws IndexOutOfBoundsException if id is out of the bounds of the shopping lists array list
     */
    public ShoppingList getShoppingList(long id) throws NotInitializedException, IndexOutOfBoundsException {

        if(!this.isInitialized()) throw new NotInitializedException("The List<ShoppingList> in ShoppingListLibrary wasn't loaded yet! Call loadLibrary() first!");

        if(this.shoppingLists.size() <= id) throw new IndexOutOfBoundsException("Index " + id + " is out of bounds of ArrayList<ShoppingList> with size " + this.shoppingLists.size());

        else return this.shoppingLists.get((int) id);

    }

    /**
     * Replaces all existing shopping lists with the new ones
     * @param lists The lists to set to the shoppingLists ArrayList
     * @throws NullPointerException if the passed list is null
     */
    private void setShoppingLists(List<ShoppingList> lists) throws NullPointerException {

        this.shoppingLists.clear();
        this.shoppingLists.addAll(lists);

        this.notifyListeners();

    }

    //Library IO methods (to load & save the library)

    @Override
    public synchronized void loadLibrary(Context context) {

        this.loadLibrary(context, null);

    }

    @Override
    public void loadLibrary(Context context, final LibraryRefreshListener listener) {

        this.file.loadLibrary(context, new LibraryListener<ArrayList<ShoppingList>>() {

            @Override
            public void onOperationStarted() {

                Log.v(TAG, "[STATUS] Load operation started");

            }

            @Override
            public void onLoadResult(ArrayList<ShoppingList> result) {

                Log.v(TAG, "[STATUS] Load operation set result: " + result.toString() + result.size());
                shoppingLists = result;

            }

            @Override
            public void onSetInitialized(boolean initialized) {

                setInitialized(initialized);
                Log.v(TAG, "[STATUS] Load operation set initialized " + initialized);

            }

            @Override
            public void onOperationFinished() {

                if(listener != null) listener.onRefresh();

                //notify all listening elements whenever the contents are loaded anew
                notifyListeners();
                Log.v(TAG, "[STATUS] Load operation finished");

            }

        });

    }

    public synchronized void saveLibrary(Context context) {

        this.saveLibrary(context, null);

    }

    @Override
    public void saveLibrary(Context context, final LibraryRefreshListener listener) {

        this.file.saveLibrary(context, this.shoppingLists, new LibraryListener<ArrayList<ShoppingList>>() {

            @Override
            public void onOperationStarted() {

                Log.d(TAG, "[STATUS] Save operation started");

            }

            @Override
            public void onOperationFinished() {

                if(listener != null) listener.onRefresh();
                Log.d(TAG, "[STATUS] Save operation finished");

            }

            @Override
            public void onLoadResult(ArrayList<ShoppingList> result) {

            }

            @Override
            public void onSetInitialized(boolean initialized) {

            }

        });

    }

    @Override
    public boolean isInitialized() {

        return this.listsInitialized;

    }

    @Override
    public void setInitialized(boolean initialized) {

        this.listsInitialized = initialized;

    }

    //Listener methods

    @Override
    public void addListener(LibraryRefreshListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(LibraryRefreshListener listener) {
        if(this.listeners.contains(listener)) this.listeners.remove(listener);
    }

    @Override
    public List<LibraryRefreshListener> getListeners() {
        return this.listeners;
    }

    @Override
    public void setListeners(List<LibraryRefreshListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void notifyListeners() {

        for(LibraryRefreshListener listener : listeners) {

            listener.onRefresh();
            Log.v(TAG, "Notified listeners");

        }

    }

}
