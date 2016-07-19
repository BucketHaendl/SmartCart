package com.buckethaendl.smartcart.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLInput;

/**
 * Created by Cedric on 18.03.2016.
 */
public class SmartCartSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "smartcart_database";
    public static final int DATABASE_VERSION = 1;

    public static final String SHOPPING_LISTS_TABLE_NAME = "shopping_lists";

    public SmartCartSQLiteHelper(Context context) {

        super(context, SmartCartSQLiteHelper.DATABASE_NAME, null, SmartCartSQLiteHelper.DATABASE_VERSION);

        Log.v("SmartCartSQLiteHelper", "Constructor called");

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        this.onUpgrade(database, 0, DATABASE_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        //If the database is created fully new
        if(oldVersion <= 0) {

            String columns = "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date NUMERIC, items TEXT, )";
            String nonQuery = SQLiteQueryBuilder.buildCreateTableQuery(SmartCartSQLiteHelper.SHOPPING_LISTS_TABLE_NAME, columns);

            database.execSQL(nonQuery);

        }

    }

    /**
     * A utility class to help building (non-)query strings for SQLite execution
     */
    public static class SQLiteQueryBuilder {

        private SQLiteQueryBuilder() {

        }

        /**
         * Builds a non-query to create an empty table with this name without any columns
         * @param tablename of the table to be created
         * @return a ready-to-execute non-query
         */
        public static String buildCreateTableQuery(String tablename) {

            return "TABLE CREATE " + tablename;

        }

        /**
         * Builds a non-query to create an empty table with this name with the specified columns in parentheses
         * @param tablename of the table to be created
         * @param columns the table should contain. Format: (_id INT INDEX, name TEXT, ...)
         * @return a ready-to-execute non-query
         */
        public static String buildCreateTableQuery(String tablename, String columns) {

            return "TABLE CREATE " + tablename + " " + columns;

        }

    }

}
