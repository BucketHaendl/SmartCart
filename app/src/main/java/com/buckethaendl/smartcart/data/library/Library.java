package com.buckethaendl.smartcart.data.library;

import android.content.Context;

/**
 * An interface to enforce standardized loading and saving behavior for classes that handle loading and saving to local files
 *
 * Created by Cedric on 18.03.2016.
 */
public interface Library {

    /**
     * GENERAL INFO:
     * Load / Save cycle in an activity working with underlying data from a Library implementation object
     * In onCreate(), the loadLibrary() method of the respective library is called in an AsyncTask. After loading, the data is shown to the user in onPostExecute()
     * When new entries are added or old ones are removed from the underlying library data, these changes are only held in memory and are NOT immediately stored.
     * In onDestroy(), the sacveComponents() method of the respective library is called again in an AsyncTask. Saving should only be made in this method of all field values stored in memory by the library at this point of time.
     *
     */

    /**
     * Loads the components this library can load from a local file and initializes all it's values
     * This method MUST be called prior to all work with a Library object to ensure that all memory binary files etc. were tried to load.
     */
    void loadLibrary(Context context);
    void loadLibrary(Context context, LibraryRefreshListener listener);

    void saveLibrary(Context context);
    void saveLibrary(Context context, LibraryRefreshListener listener);
    /**
     * Before any methods with serialized objects can be used in the Library object, the loadLibrary() method must have been called at least once.
     * The initialized field enforces this
     * Note that loadLibrary should only be called on the first app start! otherwise, data might be removed! :(
     * @return if the fields were initialized by the loadLibrary() method
     */
    boolean isInitialized();

    /**
     * Forces the library to set it's initialization value to the given argument.
     * NOTE that this method is unsafe to use because it tricks out the controlling logic that before any operation in a library, the loadLibrary() method should have ran.
     * This should only be used for specific exception handling
     * @param initialized if the value of the field should be set to true or false
     */
    void setInitialized(boolean initialized);



}
