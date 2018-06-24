package pooria.storeitems.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import pooria.storeitems.data.ItemsContract.ItemsEntry;

/**
 * Created by Po0RiA on 1/1/2018.
 * we must use this class for connecting to database
 */

public class ItemsDbHelper extends SQLiteOpenHelper {

    /**
     * name of database file
     */

    public static final String DATABASE_NAME = "store.db";

    /**
     * Version code of database,if we change the schema, we must increment the database version
     */

    public static final Integer VERSION_CODE = 3;

    /**
     * make a new instance of @param ItemsDbHelper
     * Constructs a new instance of {@link ItemsDbHelper}.
     *
     * @param context of app
     */
    public ItemsDbHelper(Context context) {

        super(context, DATABASE_NAME, null, VERSION_CODE);
    }


    /**
     * This is called when the database is created for the first time.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        Log.i("DB", "OnCreate");

        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME_ITEMS + "("
                + ItemsEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_DESCRIPTION + " TEXT, "
                + ItemsEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_IMAGE + " BLOB );";


        String SQL_CREATE_SELLS_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME_SELL_LIST + "("
                + ItemsEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_DESCRIPTION + " TEXT, "
                + ItemsEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_IMAGE + " BLOB );";

        String SQL_CREATE_ORDER_HISTORY_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME_ORDER_HISTORY_LIST + "("
                + ItemsEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + ItemsEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + ItemsEntry.COLUMN_IMAGE + " BLOB, "
                + ItemsEntry.COLUMN_DATE + " LONG NOT NULL );";

        String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME_CATEGORY_LIST + "("
                + ItemsEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemsEntry.COLUMN_CATEGORY + " TEXT NOT NULL );";


        db.execSQL(SQL_CREATE_ORDER_HISTORY_TABLE);

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);

        db.execSQL(SQL_CREATE_SELLS_TABLE);

        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("DB", "ONUpgrade" + db + "oldVersion: " + "newVersion: " + newVersion);
        // The database is still at version 1, so there's nothing to do be done here.

    }
}
