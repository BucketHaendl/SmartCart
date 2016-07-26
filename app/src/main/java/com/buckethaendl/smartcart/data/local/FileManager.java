package com.buckethaendl.smartcart.data.local;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.objects.exceptions.SerializationException;
import com.buckethaendl.smartcart.util.DialogBuildingSite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Cedric on 20.07.16.
 */
 class FileManager implements FileInteractable {

    public static final String TAG = FileManager.class.getName();

    private File file;

    public FileManager(File appDirectory, String fileName) {

        File file = new File(appDirectory, fileName);
        this.file = file;

    }

    public FileManager(File file) {

        this.file = file;

    }

    @Override
    public <E extends Serializable> void loadLibrary(Context context) {
        this.loadLibrary(context, null);
    }

    @Override
    public <E extends Serializable> void saveLibrary(Context context, E content) {
        this.saveLibrary(context, content, null);
    }

    @Override
    public synchronized <E extends Serializable> void loadLibrary(Context context, LibraryListener<E> listener) {

        LoadShoppingListsAsyncTask<E> task = new LoadShoppingListsAsyncTask<E>(context, listener);
        task.execute();

    }

    @Override
    public synchronized <E extends Serializable> void saveLibrary(Context context, E content, LibraryListener<E> listener) {

        SaveShoppingListsAsyncTask<E> task = new SaveShoppingListsAsyncTask<E>(context, content, listener);
        task.execute();

    }

    private class LoadShoppingListsAsyncTask<E extends Serializable> extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private Handler uiHandler;
        private LibraryListener<E> listener;

        public LoadShoppingListsAsyncTask(Context context) {

            this.context = context;

        }

        public LoadShoppingListsAsyncTask(Context context, LibraryListener<E> listener) {

            this.context = context;
            this.listener = listener;

        }

        @Override
        protected void onPreExecute() {

            this.uiHandler = new Handler(Looper.getMainLooper());
            if(this.listener != null) this.listener.onOperationStarted();

        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            try {

                E result = this.loadFromFile(); //Loads the List<ShoppingList> for the ShoppingListLibrary
                Log.v(TAG, file.getName() + " loaded without any error");

                return true;

            }

            catch (IOException e) {

                this.uiHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        Resources resources = context.getResources();
                        AlertDialog dialog = DialogBuildingSite.buildErrorDialog(context, resources.getString(R.string.error_01));

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.error_dialog_retry), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LoadShoppingListsAsyncTask.this.cancel(true);
                                FileManager.this.loadLibrary(context, listener);

                            }

                        }); //Retry Button

                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.error_dialog_ignore), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }

                        }); //Ignore Button

                        dialog.show();
                    }

                });

                Log.e(TAG, file.getName() + " could not be loaded (IOException): " + e.getMessage());
                e.printStackTrace();

            }

            catch (SerializationException e) {

                this.uiHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        Resources resources = context.getResources();
                        AlertDialog dialog = DialogBuildingSite.buildErrorDialog(context, resources.getString(R.string.error_02));

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.error_dialog_retry), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LoadShoppingListsAsyncTask.this.cancel(true);
                                FileManager.this.loadLibrary(context, listener);

                            }

                        }); //Retry Button

                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.error_dialog_ignore), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //ShoppingListLibrary.this.setInitialized(true); todo set initialization...könnte das nicht über den anderen intiialized variable laufen?
                                dialog.dismiss();

                            }

                        }); //Ignore Button

                        dialog.show();
                    }

                });

                Log.e(TAG, file.getName() + " could not be loaded (SerializationException): " + e.getMessage());

            }

            return false;

        }

        @Override
        protected void onPostExecute(Boolean loadedLibrary) {

            if(this.listener != null) {

                this.listener.onSetInitialized(loadedLibrary); //TODO implement! (bisher wird einfach auf true gesetzt, sobald ein mal versucht wurde zu laden (auch mit Fehler!)
                this.listener.onOperationFinished();

            }

            //ShoppingListLibrary.this.setInitialized(loadedLibrary); todo talk to library! interface

            //END OF METHOD (the rest is only for debugging and can be removed!)

            /*
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

            }*/

            //Remove up to here

        }

        private synchronized E loadFromFile() throws IOException, SerializationException {

            if(file.getParentFile().mkdirs() || file.getParentFile().isDirectory()) { //Create the parent directory and check, that it was created

                if(!file.exists()) {

                    Log.v(TAG, "No file " + file.getName() + " existing - this is your first app start up");

                }

                else {

                    FileInputStream binaryStream = null;
                    ObjectInputStream objectStream = null;

                    try {

                        binaryStream = new FileInputStream(file);
                        objectStream = new ObjectInputStream(binaryStream);

                        try {

                            E result = (E) objectStream.readObject(); //throws ClassCastException if not right
                            if(result != null) {

                                this.listener.onLoadResult(result);

                            }

                            Log.v("ShoppingListLibrary", "The file " + file.getName() + " successfully deserialized and the lists are loaded");
                            return result;

                        }

                        catch (IOException e) {

                            //IOExceptions will be passed just as they are and are not a failure in the serialized object. The lists could be reloaded at a later time.
                            throw e;

                        }

                        catch (Exception e) {

                            //When an exception occurs during these steps, there is a problem with the serialized object. No lists should be loaded.
                            throw new SerializationException("The ArrayList<ShoppingList> for ShoppingListLibrary could not be deserialized: " + e.getMessage());

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

            else throw new IOException("The file path " + file.getAbsolutePath() + " could not be created! Is there a problem with the internal memory / SD card?");

            return null;

        }

    }

    private class SaveShoppingListsAsyncTask<E extends Serializable> extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private Handler uiHandler;
        private E content;
        private LibraryListener<E> listener;

        public SaveShoppingListsAsyncTask(Context context, E content) {

            this.context = context;
            this.content = content;

        }

        public SaveShoppingListsAsyncTask(Context context, E content, LibraryListener<E> listener) {

            this.context = context;
            this.content = content;
            this.listener = listener;

        }

        @Override
        protected void onPreExecute() {

            this.uiHandler = new Handler(Looper.getMainLooper());
            if(this.listener != null) this.listener.onOperationStarted();

        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            try {

                this.saveToFile(content);

                Log.v("ShoppingListOverviewFr", "ShoppingListLibrary successfully saved");

            }

            catch (IOException e) {

                this.uiHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        Resources resources = context.getResources();
                        AlertDialog dialog = DialogBuildingSite.buildErrorDialog(context, resources.getString(R.string.error_03));

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.error_dialog_retry), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SaveShoppingListsAsyncTask.this.cancel(true);
                                FileManager.this.saveLibrary(context, content, listener);

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

            if(this.listener != null) {

                this.listener.onOperationFinished();

            }

        }

        private synchronized void saveToFile(E content) throws IOException {

            if(file.getParentFile().mkdirs() || file.getParentFile().isDirectory()) { //Create the parent directory and check, that it was created

                if (!file.exists()) Log.v("ShoppingListLibrary", "No file " + file.getName() + " existing - this is the first time shoppingLists are saved"); //Nothing special happens

                FileOutputStream binaryStream = null;
                ObjectOutputStream objectStream = null;

                try {

                    binaryStream = new FileOutputStream(file);
                    objectStream = new ObjectOutputStream(binaryStream);

                    objectStream.writeObject(content);

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

            else throw new IOException("The file path " + file.getAbsolutePath() + " could not be created! Is there a problem with the internal memory / SD card?");

        }

    }


}
