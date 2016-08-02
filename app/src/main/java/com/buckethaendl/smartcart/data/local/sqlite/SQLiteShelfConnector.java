package com.buckethaendl.smartcart.data.local.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.data.service.WaSaFBBShelf;
import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.io.File;

public class SQLiteShelfConnector implements SQLiteShelfInteractable {

    public static final String TAG = SQLiteShelfConnector.class.getName();

    public static final String[] SELECTED_COLUMNS = {"shelf", "priority", "x", "y"}; //todo update to correct values!
    public static final String FBB_COLUMN_NAME = "fbb";

    public static final String DATABASE_NAME = "stores.db";

    public static final String TABLE_NAME = "DE_6250"; //todo make changable

    public static SQLiteShelfConnector instance;

    private SQLiteDatabase database;

    static {

        instance = new SQLiteShelfConnector();

    }

    private SQLiteShelfConnector() {

    }

    public static SQLiteShelfConnector getInstance() {

        return SQLiteShelfConnector.instance;

    }

    /**
     * Gets access to the store database
     * @return The underlying store database or null, if it could not be opened
     */
    public SQLiteDatabase openStoreDatabase() {

        if(database == null || !database.isOpen()) {

            try {

                //Log.d("DATABASE FILE CHECK", "Database " + DATABASE_NAME + " Path: (" + fullPath.getPath() + ") Existing: " + fullPath.exists());

                File fullPath = new File(App.EXTERNAL_DIRECTORY, DATABASE_NAME);
                this.database = SQLiteDatabase.openDatabase(fullPath.toString(), null, SQLiteDatabase.OPEN_READONLY);

                Log.v(TAG, "DB " + DATABASE_NAME + " opened from " + App.EXTERNAL_DIRECTORY);
                return database;

            }

            catch (SQLiteException ex) {

                Log.e(TAG, "DB " + DATABASE_NAME + " could not be opened from " + App.EXTERNAL_DIRECTORY);
                return null;

            }

        }

        else return this.database;

    }

    /**
     * Only close the database via this way to prevent closing it, while another task is accessing it
     */
    public void closeStoreDatabase() {

        if(database != null && database.isOpen()) database.close();

    }

    /**
     * Returns the shelves in a sync task to directly retrieve them (hackaround)
     */
    public Shelf loadShelfSync(WaSaFBBShelf rawShelf) {

        LoadSQLiteShelvesAsyncTask task = new LoadSQLiteShelvesAsyncTask(rawShelf);
        return task.doInBackground();

    }

    @Override
    public void loadShelfAsync(WaSaFBBShelf rawShelf, LibraryListener<Shelf> listener) {

        LoadSQLiteShelvesAsyncTask task = new LoadSQLiteShelvesAsyncTask(rawShelf, listener);
        task.execute();

    }

    private class LoadSQLiteShelvesAsyncTask extends AsyncTask<Void, Void, Shelf> {

        private WaSaFBBShelf rawShelf;
        private LibraryListener<Shelf> listener;

        public LoadSQLiteShelvesAsyncTask(WaSaFBBShelf rawShelf) {

            this(rawShelf, null);

        }

        public LoadSQLiteShelvesAsyncTask(WaSaFBBShelf rawShelf, LibraryListener<Shelf> listener) {

            this.rawShelf = rawShelf;
            this.listener = listener;

        }

        @Override
        protected void onPreExecute() {

            if(this.listener != null) this.listener.onOperationStarted();

        }

        @Override
        protected Shelf doInBackground(Void...voids) {

            Shelf resultShelf = this.loadShelf();
            if(this.listener != null) this.listener.onLoadResult(resultShelf);

            return resultShelf;

        }

        @Override
        protected void onPostExecute(Shelf loadedShelf) {

            if(this.listener != null) {

                this.listener.onOperationFinished();

            }

        }

        private synchronized Shelf loadShelf() {

            Shelf shelf = null;

            SQLiteDatabase db;
            Cursor cursor;

            db = SQLiteShelfConnector.this.openStoreDatabase();

            if(db != null) {

                cursor = db.query(TABLE_NAME, SELECTED_COLUMNS, FBB_COLUMN_NAME + " = ?", new String[] {this.rawShelf.getFbbNr()}, null, null, null);

                if(cursor.moveToFirst()) {

                    shelf = new Shelf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3));
                    Log.v(TAG, "[" + this.rawShelf.getFbbNr() + "] " + "Mapped to shelf " + shelf.getShelfId());

                }

                else {

                    Log.w(TAG, "[" + this.rawShelf.getFbbNr() + "] " + "Unmapped");

                }

                //Free Resources (not close database!)
                if(cursor != null) {

                    cursor.close();
                    cursor = null;

                }

                return shelf;

            }

            else {

                Looper.prepare();

                Toast toast = Toast.makeText(App.getGlobalContext(), R.string.error_07, Toast.LENGTH_SHORT);
                toast.show();

                return null; //database not present - returning null element

            }

        }

    }

    public class NoFBBFoundException extends RuntimeException {

        public NoFBBFoundException() {

            super("No FBB could be found in the database records - this is an unsupported FBB that should not be there");

        }

        public NoFBBFoundException(String msg) {

            super(msg);

        }

    }

}
