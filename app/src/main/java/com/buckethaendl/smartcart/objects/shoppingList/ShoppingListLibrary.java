package com.buckethaendl.smartcart.objects.shoppingList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.ShoppingListHubActivity;
import com.buckethaendl.smartcart.util.DialogBuildingSite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Holds references to all loaded shopping lists and organizes all IO handling around this topic
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingListLibrary implements Library {

    public static final String SHOPPING_LIST_FILE_PATH = "shopping_lists/lists.bin";

    private static ShoppingListLibrary instance;

    private boolean listsInitialized = false; //Set to true after the executeLoadComponents() method was executed
    private List<ShoppingList> shoppingLists; //Will be (de-)serialized by the Library interface methods. The list is only assigned to an ArrayList reference once in the constructor so when changed, any Android ListAdapters easily update.

    static {

        new ShoppingListLibrary();

    }

    private ShoppingListLibrary() {

        ShoppingListLibrary.instance = this;
        this.shoppingLists = new ArrayList<ShoppingList>();

    }

    /**
     * Gets the only instance of this library
     * @return An object of this library
     */
    public static ShoppingListLibrary getLibrary(Resources resources) {

        ShoppingList.setStaticResources(resources);
        return ShoppingListLibrary.instance;

    }

    public void createTestLists(Resources resources) {

        //TODO remove when working serialisation is implemented

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 77);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DAY_OF_YEAR, 10);
        cal2.set(Calendar.MONTH, 3);

        ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();
        items.add(new ShoppingListItem("Butter",  Material.BAKED_GOODS));
        items.add(new ShoppingListItem("Bread", Material.FRESH_FRUITS));
        items.add(new ShoppingListItem("5 Apples", Material.FRESH_VEGETABLES));
        items.add(new ShoppingListItem("Butter",  Material.BAKED_GOODS));
        items.add(new ShoppingListItem("Bread", Material.FRESH_FRUITS));
        items.add(new ShoppingListItem("5 Apples", Material.FRESH_VEGETABLES));
        items.add(new ShoppingListItem("Butter",  Material.BAKED_GOODS));
        items.add(new ShoppingListItem("Bread", Material.FRESH_FRUITS));
        items.add(new ShoppingListItem("5 Apples", Material.FRESH_VEGETABLES));
        items.add(new ShoppingListItem("Butter",  Material.BAKED_GOODS));
        items.add(new ShoppingListItem("Bread", Material.FRESH_FRUITS));
        items.add(new ShoppingListItem("5 Apples", Material.FRESH_VEGETABLES));
        items.add(new ShoppingListItem("Butter",  Material.BAKED_GOODS));
        items.add(new ShoppingListItem("Bread", Material.FRESH_FRUITS));
        items.add(new ShoppingListItem("5 Apples", Material.FRESH_VEGETABLES));
        items.add(new ShoppingListItem("Butter",  Material.BAKED_GOODS));
        items.add(new ShoppingListItem("Bread", Material.FRESH_FRUITS));
        items.add(new ShoppingListItem("5 Apples", Material.FRESH_VEGETABLES));

        shoppingLists.add(new ShoppingList("Abendessen", cal, android.R.color.holo_green_light, items));

        items.add(new ShoppingListItem("4x Pasta a la Mama", Material.BAKED_GOODS, true));

        shoppingLists.add(new ShoppingList("Wocheneinkauf", cal, android.R.color.holo_blue_bright, items));

        shoppingLists.add(new ShoppingList("Für Mum", cal2, android.R.color.holo_red_dark));

    }

    public ShoppingList createNewShoppingList(Resources resources) { //TODO this is the only method able to create new lists for the AddActivity

        String newListName = resources.getString(R.string.shopping_list_add_activity_new_list_name);

        return new ShoppingList(newListName, Calendar.getInstance());

    }

    public void addNewShoppingList(ShoppingList list) {

        this.shoppingLists.add(list);

    }

    @Override
    public synchronized void executeLoadComponents(Context context, File appDirectory) {

        LoadShoppingListsAsyncTask task = new LoadShoppingListsAsyncTask(context, appDirectory);
        task.execute();

    }

    @Override
    public synchronized void executeLoadComponents(Context context, File appDirectory, TaskListener listener) {

        LoadShoppingListsAsyncTask task = new LoadShoppingListsAsyncTask(context, appDirectory, listener);
        task.execute();

    }

    public synchronized void executeSaveComponents(Context context, File appDirectory) {

        SaveShoppingListsAsyncTask task = new SaveShoppingListsAsyncTask(context, appDirectory);
        task.execute();

    }

    public synchronized void executeSaveComponents(Context context, File appDirectory, TaskListener listener) {

        SaveShoppingListsAsyncTask task = new SaveShoppingListsAsyncTask(context, appDirectory, listener);
        task.execute();

    }

    @Override
    public boolean isInitialized() {

        return this.listsInitialized;

    }

    @Override
    public void setInitialized(boolean initialized) {

        this.listsInitialized = initialized;

    }

    /**
     * Getter for all shopping lists that are currently loaded
     * @return The list of currently existing shopping lists
     * @throws NotInitializedException if the method executeLoadComponents() has never been executed and maybe existing objects were not deserialized
     */
    public List<ShoppingList> getShoppingLists() throws NotInitializedException {

        if(!this.listsInitialized) throw new NotInitializedException("The List<ShoppingList> in ShoppingListLibrary wasn't loaded yet! Call executeLoadComponents() first!");

        else return this.shoppingLists;

    }

    /**
     * Getter for a specific shopping lists out of the currently loaded ones
     * @param id the ID of the list in the ArrayList
     * @return The shopping list with this ID
     * @throws NotInitializedException if the method executeLoadComponents() has never been executed and maybe existing objects were not deserialized
     * @throws IndexOutOfBoundsException if id is out of the bounds of the shopping lists array list
     */
    public ShoppingList getShoppingList(long id) throws NotInitializedException, IndexOutOfBoundsException {

        if(!this.listsInitialized) throw new NotInitializedException("The List<ShoppingList> in ShoppingListLibrary wasn't loaded yet! Call executeLoadComponents() first!");

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

    }

    private abstract static class FileHandlingAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private File appDirectory;
        private TaskListener listener;

        private Handler uiHandler;

        public FileHandlingAsyncTask(Context context, File appDirectory) {

            this.context = context;
            this.appDirectory = appDirectory;

        }

        public FileHandlingAsyncTask(Context context, File appDirectory, TaskListener listener) {

            this.context = context;
            this.appDirectory = appDirectory;
            this.listener = listener;

        }

        @Override
        protected void onPreExecute() {

            this.uiHandler = new Handler(Looper.getMainLooper());

        }

        protected Context getContext() {
            return this.context;
        }

        protected File getAppDirectory() {
            return this.appDirectory;
        }

        protected Handler getUiHandler() {
            return this.uiHandler;
        }

        protected TaskListener getListener() {
            return listener;
        }

    }

    private class LoadShoppingListsAsyncTask extends FileHandlingAsyncTask {

        public LoadShoppingListsAsyncTask(Context context, File appDirectory) {

            super(context, appDirectory);

        }

        public LoadShoppingListsAsyncTask(Context context, File appDirectory, TaskListener listener) {

            super(context, appDirectory, listener);

        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            boolean loadedLibrary = false;

            try {

                loadedLibrary = this.loadFromFile(getAppDirectory()); //Loads the List<ShoppingList> for the ShoppingListLibrary
                Log.v("ShoppingListOverviewFr", "ShoppingListLibrary successfully loaded");

            }

            catch (IOException e) {

                this.getUiHandler().post(new Runnable() {

                    @Override
                    public void run() {

                        Resources resources = getContext().getResources();
                        AlertDialog dialog = DialogBuildingSite.buildErrorDialog(getContext(), resources.getString(R.string.error_01));

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.error_dialog_retry), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LoadShoppingListsAsyncTask.this.cancel(true);

                                ShoppingListLibrary.this.executeLoadComponents(getContext(), getAppDirectory(), getListener());

                            }

                        }); //Retry Button

                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.error_dialog_ignore), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ShoppingListLibrary.this.setInitialized(true);
                                dialog.dismiss();

                            }

                        }); //Ignore Button

                        dialog.show();
                    }

                });

                Log.e("ShoppingListOverviewFr", "ShoppingListLibrary could not be loaded (IOException): " + e.getMessage());

            }

            catch (SerializationException e) {

                this.getUiHandler().post(new Runnable() {

                    @Override
                    public void run() {

                        Resources resources = getContext().getResources();
                        AlertDialog dialog = DialogBuildingSite.buildErrorDialog(getContext(), resources.getString(R.string.error_02));

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.error_dialog_retry), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LoadShoppingListsAsyncTask.this.cancel(true);

                                ShoppingListLibrary.this.executeLoadComponents(getContext(), getAppDirectory(), getListener());

                            }

                        }); //Retry Button

                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.error_dialog_ignore), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ShoppingListLibrary.this.setInitialized(true);
                                dialog.dismiss();

                            }

                        }); //Ignore Button

                        dialog.show();
                    }

                });

                Log.e("ShoppingListOverviewFr", "ShoppingListLibrary could not be loaded (SerializationException): " + e.getMessage());

            }

            return loadedLibrary;

        }

        @Override
        protected void onPostExecute(Boolean loadedLibrary) {

            ShoppingListLibrary.this.setInitialized(loadedLibrary);

            if(this.getListener() != null) {

                this.getListener().onDone();

            }

            //END OF METHOD (the rest is only for debugging and can be removed!)

            if (loadedLibrary) {

                Log.v("ShoppingListOverviewFr", "Some data was successfully loaded from shopping lists file");

                List<ShoppingList> shoppingLists = ShoppingListLibrary.this.getShoppingLists();

                StringBuilder loadedLists = new StringBuilder();

                for(ShoppingList list : shoppingLists) {

                    loadedLists.append(list.toString());
                    loadedLists.append("\n");

                }

                Log.v("ShoppingListOverviewFr", "These are all loaded shopping lists: \n" + loadedLists);

            }

            else {

                Log.v("ShoppingListOverviewFr", "No data was loaded from shopping lists file");

            }

            //Remove up to here

        }

        private synchronized boolean loadFromFile(File appDirectory) throws IOException, SerializationException {

            boolean initialized = false;

            File shoppingListsFile = new File(appDirectory, SHOPPING_LIST_FILE_PATH);

            if(shoppingListsFile.getParentFile().mkdirs() || shoppingListsFile.getParentFile().isDirectory()) { //Create the parent directory and check, that it was created

                if(!shoppingListsFile.exists()) {

                    Log.v("ShoppingListLibrary", "No shoppingListsFile existing - this is your first app start up");

                    initialized = true;

                }

                else {

                    FileInputStream binaryStream = null;
                    ObjectInputStream objectStream = null;

                    try {

                        binaryStream = new FileInputStream(shoppingListsFile);
                        objectStream = new ObjectInputStream(binaryStream);

                        try {

                            List<ShoppingList> lists = (List<ShoppingList>) objectStream.readObject();

                            if(lists != null || lists.size() > 0) {

                                ShoppingListLibrary.this.setShoppingLists(lists);
                                initialized = true; //At least one list was loaded from the file

                            }

                            ShoppingListLibrary.this.listsInitialized = true; //Even if no lists were loaded, the lists were still initialized and are fully working!

                            Log.v("ShoppingListLibrary", "The shoppingListsFile was successfully deserialized and the lists are loaded");

                        }

                        catch (IOException e) {

                            //IOExceptions will be passed just as they are and are not a failure in the serialized object. The lists could be reloaded at a later time.
                            throw e;

                        }

                        catch (Exception e) {

                            //When an exception occurs during these steps, there is a problem with the serialized object. No lists should be loaded.
                            throw new SerializationException("The ArrayList<ShoppingList> for ShoppingListLibrary could not be deserialized");

                        }

                    }

                    finally {

                        //Free Resources

                        if(binaryStream != null) {

                            try {

                                binaryStream.close();

                            } catch (IOException e) {
                                Log.w("ShoppingListLibrary", "Could not close FileInputStream properly: " + e.getMessage());
                            }

                            binaryStream = null;

                        }

                        if(objectStream != null) {

                            try {

                                objectStream.close();

                            } catch (IOException e) {
                                Log.w("ShoppingListLibrary", "Could not close ObjectInputStream properly: " + e.getMessage());
                            }

                            objectStream = null;

                        }

                    }

                }

            }

            else throw new IOException("The file path " + shoppingListsFile.getAbsolutePath() + " could not be created! Is there a problem with the internal memory / SD card?");

            return initialized;

        }

    }

    private class SaveShoppingListsAsyncTask extends FileHandlingAsyncTask {

        public SaveShoppingListsAsyncTask(Context context, File appDirectory) {

            super(context, appDirectory);

        }

        public SaveShoppingListsAsyncTask(Context context, File appDirectory, TaskListener listener) {

            super(context, appDirectory, listener);

        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            Library library = ShoppingListLibrary.this;

            try {

                SaveShoppingListsAsyncTask.this.saveToFile(getAppDirectory());
                Log.v("ShoppingListOverviewFr", "ShoppingListLibrary successfully saved");

            }

            catch (IOException e) {

                this.getUiHandler().post(new Runnable() {

                    @Override
                    public void run() {

                        Resources resources = getContext().getResources();
                        AlertDialog dialog = DialogBuildingSite.buildErrorDialog(getContext(), resources.getString(R.string.error_03));

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.error_dialog_retry), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SaveShoppingListsAsyncTask.this.cancel(true);

                                ShoppingListLibrary.this.executeSaveComponents(getContext(), getAppDirectory(), getListener());

                            }

                        }); //Retry Button

                        dialog.show();
                    }

                });

                Log.e("ShoppingListOverviewFr", "ShoppingListLibrary could not be saved (IOException): " + e.getMessage());

            }

            return true;

        }

        @Override
        protected void onPostExecute(Boolean done) {

            if(this.getListener() != null) {

                this.getListener().onDone();

            }

        }

        private synchronized void saveToFile(File appDirectory) throws IOException {

            File shoppingListsFile = new File(appDirectory, SHOPPING_LIST_FILE_PATH);

            if(shoppingListsFile.getParentFile().mkdirs() || shoppingListsFile.getParentFile().isDirectory()) { //Create the parent directory and check, that it was created

                if (!shoppingListsFile.exists()) Log.v("ShoppingListLibrary", "No shoppingListsFile existing - this is the first time shoppingLists are loaded"); //Nothing special happens

                FileOutputStream binaryStream = null;
                ObjectOutputStream objectStream = null;

                try {

                    binaryStream = new FileOutputStream(shoppingListsFile);
                    objectStream = new ObjectOutputStream(binaryStream);

                    objectStream.writeObject(shoppingLists);

                    Log.v("ShoppingListLibrary", "The field containing all shoppingLists was successfully serialized and saved as a local file");

                }

                finally {

                    //Free Resources

                    if(binaryStream != null) {

                        try {

                            binaryStream.close();

                        } catch (IOException e) {
                            Log.w("ShoppingListLibrary", "Could not close FileOutputStream properly: " + e.getMessage());
                        }

                        binaryStream = null;

                    }

                    if(objectStream != null) {

                        try {
                            objectStream.close();
                        } catch (IOException e) {
                            Log.w("ShoppingListLibrary", "Could not close ObjectOutputStream properly: " + e.getMessage());
                        }

                        objectStream = null;

                    }

                }

            }

            else throw new IOException("The file path " + shoppingListsFile.getAbsolutePath() + " could not be created! Is there a problem with the internal memory / SD card?");

        }

    }

    public interface TaskListener {

        void onDone();

    }

}
