package com.buckethaendl.smartcart.unused;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.util.ArrayList;
import java.util.List;

public class LibraryLibrarySQLiteShelfConnector extends SQLiteOpenHelper implements LibrarySQLiteShelfInteractable {

    public static final String TAG = LibraryLibrarySQLiteShelfConnector.class.getName();

    public static final String[] SELECTED_COLUMNS = {"shelf_no", "priority", "x", "y", "fbb"}; //todo update to correct values!

    public static final String DATABASE_NAME = "stores";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "DE_6250"; //todo make changable

    public LibraryLibrarySQLiteShelfConnector(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //no implementation
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //no implementation
    }

    @Override
    public void loadLibrary(Context context) {

        this.loadLibrary(context, null);

    }

    @Override
    public void loadLibrary(Context context, LibraryListener<List<Shelf>> listener) {

        LoadSQLiteShelvesAsyncTask task = new LoadSQLiteShelvesAsyncTask(context, listener);
        task.execute();

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        throw new RuntimeException("ShelfConnector is only readable and may not be modified!");

    }

    private class LoadSQLiteShelvesAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private Handler uiHandler;
        private LibraryListener<List<Shelf>> listener;

        public LoadSQLiteShelvesAsyncTask(Context context) {

            this.context = context;

        }

        public LoadSQLiteShelvesAsyncTask(Context context, LibraryListener<List<Shelf>> listener) {

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

            List<Shelf> shelves = this.loadShelves(); //Loads the List<Shelf> for the SQLiteDatabase
            Log.v(TAG, DATABASE_NAME + " loaded without any error");

            return true;

        }

        @Override
        protected void onPostExecute(Boolean loadedLibrary) {

            if(this.listener != null) {

                this.listener.onSetInitialized(loadedLibrary);
                this.listener.onOperationFinished();

            }

        }

        private synchronized List<Shelf> loadShelves() {

            List<Shelf> shelves = new ArrayList<>();
            SQLiteDatabase database;
            Cursor cursor;

            database = LibraryLibrarySQLiteShelfConnector.this.getReadableDatabase();
            cursor = database.query(TABLE_NAME, SELECTED_COLUMNS, null, null, null, null, null);

            if(cursor.moveToFirst()) {

                do {

                    //retrieve values
                    //create shelf object
                    Shelf shelf = new Shelf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), null);
                    shelves.add(shelf);

                } while(cursor.moveToNext());

                this.listener.onLoadResult(shelves);

                Log.v(TAG, "The table " + TABLE_NAME + " was successfully loaded");

            }

            else {

                Log.e(TAG, "No shelves could be loaded");

            }

            //Free Resources
            if(cursor != null) {

                cursor.close();
                cursor = null;

            }

            if(database != null) {

                database.close();
                database = null;

            }

            return shelves;

        }

    }

    /*
    private List<Shelf> mapShelvesToGroups(List<WaSaFBBShelf> foundShelves, ResultSet resultSet) throws SQLException {

        List<Shelf> regalgruppen = new ArrayList<>();
        while ( resultSet.next() ) {
            String fbb = resultSet.getString("fbb");
            for(WaSaFBBShelf regal : foundShelves){
                if(regal.getFbbNr().equals(fbb)){
                    Shelf gruppe = new Shelf(resultSet.getInt("_id"),
                            resultSet.getInt("shelf"),
                            resultSet.getInt("priority"),
                            resultSet.getInt("x"),
                            resultSet.getInt("y")
                    );
                    gruppe.getRegale().add(regal);
                    regalgruppen.add(gruppe);
                }
            }
        }
        return regalgruppen;
    }

    private List<Shelf> mergeGroups(List<Shelf> regalgruppen) {

        Map<Integer, Shelf> groups = new HashMap<>();

        for (Shelf group : regalgruppen){
            if(groups.get(group.getShelf())!=null){
                groups.get(group).getRegale().addAll(group.getRegale());
            }else{
                groups.put(group.getShelf(), group);
            }
        }

        return groups.keySet().stream().map(groups::get).collect(Collectors.toList());
    }*/


}
