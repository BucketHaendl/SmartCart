package com.buckethaendl.smartcart.objects.shoppingList;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * An interface to enforce standardized loading and saving behavior for classes that handle loading and saving to local files
 *
 * Created by Cedric on 18.03.2016.
 */
public interface Library {

    /**
     * GENERAL INFO:
     * Load / Save cycle in an activity working with underlying data from a Library implementation object
     * In onCreate(), the executeLoadComponents() method of the respective library is called in an AsyncTask. After loading, the data is shown to the user in onPostExecute()
     * When new entries are added or old ones are removed from the underlying library data, these changes are only held in memory and are NOT immediately stored.
     * In onDestroy(), the sacveComponents() method of the respective library is called again in an AsyncTask. Saving should only be made in this method of all field values stored in memory by the library at this point of time.
     *
     */

    /**
     * Loads the components this library can load from a local file and initializes all it's values
     * This method MUST be called prior to all work with a Library object to ensure that all memory binary files etc. were tried to load.
     * @param appDirectory The directory file of this app or program (ONLY the root directory)
     */
    void executeLoadComponents(Context context, File appDirectory);

    /**
     * Loads the components this library can load from a local file and initializes all it's values
     * This method MUST be called prior to all work with a Library object to ensure that all memory binary files etc. were tried to load.
     * @param appDirectory The directory file of this app or program (ONLY the root directory)
     */
    void executeLoadComponents(Context context, File appDirectory, ShoppingListLibrary.TaskListener listener);

    /**
     * Saves the components this library has in it's instance variables / global fields to a local file
     * @param appDirectory The directory file of this app or program (ONLY the root directory)
     */
    void executeSaveComponents(Context context, File appDirectory);

    /**
     * Saves the components this library has in it's instance variables / global fields to a local file
     * @param appDirectory The directory file of this app or program (ONLY the root directory)
     */
    void executeSaveComponents(Context context, File appDirectory, ShoppingListLibrary.TaskListener listener);

    /**
     * Before any methods with serialized objects can be used in the Library object, the executeLoadComponents() method must have been called at least once.
     * The initialized field enforces this
     * Note that executeLoadComponents should only be called on the first app start! otherwise, data might be removed! :(
     * @return if the fields were initialized by the executeLoadComponents() method
     */
    boolean isInitialized();

    /**
     * Forces the library to set it's initialization value to the given argument.
     * NOTE that this method is unsafe to use because it tricks out the controlling logic that before any operation in a library, the executeLoadComponents() method should have ran.
     * This should only be used for specific exception handling
     * @param initialized if the value of the field should be set to true or false
     */
    void setInitialized(boolean initialized);

}
