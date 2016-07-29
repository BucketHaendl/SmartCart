package com.buckethaendl.smartcart.unused;

import android.content.Context;
import android.util.Log;

import com.buckethaendl.smartcart.data.library.Library;
import com.buckethaendl.smartcart.data.library.LibraryRefreshListener;
import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.objects.exceptions.NotInitializedException;
import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 27.07.16.
 */
public class ShelfLibrary implements Library {

    public static final String TAG = ShelfLibrary.class.getName();

    private static ShelfLibrary instance;
    private LibrarySQLiteShelfInteractable interactable;

    private boolean initialized;

    private List<Shelf> shelves = new ArrayList<Shelf>();

    static {

        new ShelfLibrary();

    }

    private ShelfLibrary() {

        ShelfLibrary.instance = this;

    }

    /**
     * Gets the only instance of this library
     * @return An object of this library
     */
    public static ShelfLibrary getInstance() {

        return ShelfLibrary.instance;

    }

    public boolean containsShelf(Shelf shelf) {

        return this.shelves.contains(shelf);

    }

    /**
     * Getter for all shopping lists that are currently loaded
     * @return The list of currently existing shopping lists
     * @throws NotInitializedException if the method loadLibrary() has never been executed and maybe existing objects were not deserialized
     */
    public List<Shelf> getShelves() throws NotInitializedException {

        this.proveInitialized();
        return this.shelves;

    }

    /**
     * Getter for a specific shopping lists out of the currently loaded ones
     * @param id the ID of the list in the ArrayList
     * @return The shopping list with this ID
     * @throws NotInitializedException if the method loadLibrary() has never been executed and maybe existing objects were not deserialized
     * @throws IndexOutOfBoundsException if id is out of the bounds of the shopping lists array list
     */
    public Shelf getShelf(long id) throws NotInitializedException, IndexOutOfBoundsException {

        this.proveInitialized();
        if(this.shelves.size() <= id) throw new IndexOutOfBoundsException("Index " + id + " is out of bounds of List<Shelf> with size " + this.shelves.size());

        else return this.shelves.get((int) id);

    }

    public void proveInitialized() {

        if(!this.isInitialized()) {

            throw new NotInitializedException("The List<Shelf> in ShelfLibrary wasn't loaded yet! Call loadLibrary() first!");

        }

    }

    @Override
    public void loadLibrary(Context context) {

        this.loadLibrary(context, null);

    }

    @Override
    public void loadLibrary(Context context, final LibraryRefreshListener listener) {

        if(this.interactable == null) this.interactable = new LibraryLibrarySQLiteShelfConnector(context);

        this.interactable.loadLibrary(context, new LibraryListener<List<Shelf>>() {

            @Override
            public void onOperationStarted() {

                Log.v(TAG, "[STATUS] Load operation started");

            }

            @Override
            public void onLoadResult(List<Shelf> result) {

                Log.v(TAG, "[STATUS] Load operation set result: " + result.toString() + result.size());
                shelves = result;

            }

            @Override
            public void onSetInitialized(boolean initialized) {

                setInitialized(initialized);
                Log.v(TAG, "[STATUS] Load operation set initialized " + initialized);

            }

            @Override
            public void onOperationFinished() {

                if(listener != null) listener.onRefresh();
                Log.v(TAG, "[STATUS] Load operation finished");

            }

        });

    }

    @Override
    public void saveLibrary(Context context) {
        //not implemented
    }

    @Override
    public void saveLibrary(Context context, LibraryRefreshListener listener) {
        //not implemented
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

}
