package pooria.storeitems.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Po0RiA on 1/5/2018.
 */

public class ItemsProvider extends ContentProvider {
  private final static String LOG_TAG = ItemsProvider.class.getName();
  ItemsDbHelper dbHelper;

  /*UriMatcher code for content list for a Items table  */
  public static final int ITEMS = 100;
  /*UriMatcher code for content Uri for a single Item at table */
  public static final int ITEM_ID = 101;


  public static final int SELL_ITEM_ID = 201;

  public static final int SELL_ITEMS = 200;


  public static final int ORDER_HISTORY_LIST_ID = 400;
  public static final int ORDER_HISTORY_LIST_ITEM_ID = 401;

  /**
   * use this code to match a content uri to a corresponding uri
   * the input passed into the constractor represent the code to return for this class
   * it is common to pass NO_MATCH as the input for this case.
   */


  private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


  // Static initializer. This is run the first time anything is called from this class.
  static {


    // The calls to addURI() go here, for all of the content URI patterns that the provider
    // should recognize. All paths added to the UriMatcher have a corresponding code to return
    // when a match is found.


    // The content URI of the form "content://pooria.storeitems/items" will map to the
    // integer code {@link #ITEMS}. This URI is used to provide access to MULTIPLE rows
    // of the pets table.
    sURI_MATCHER.addURI(ItemsContract.CONTENT_AUTHORITY, ItemsContract.PATH_ITEMS, ITEMS);
    // The content URI of the form "content://pooria.storeitems/items/#" will map to the
    // integer code {@link #ITEM_ID}. This URI is used to provide access to ONE single row
    // of the pets table.
    //
    // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
    // For example, "content://pooria.storeitems/items/3" matches, but
    // "content://pooria.storeitems/items" (without a number at the end) doesn't match.

    sURI_MATCHER.addURI(ItemsContract.CONTENT_AUTHORITY, ItemsContract.PATH_ITEMS + "/#", ITEM_ID);


    sURI_MATCHER.addURI(ItemsContract.CONTENT_AUTHORITY, ItemsContract.PATH_SELL, SELL_ITEMS);
    sURI_MATCHER.addURI(ItemsContract.CONTENT_AUTHORITY, ItemsContract.PATH_SELL + "/#", SELL_ITEM_ID);


    sURI_MATCHER.addURI(ItemsContract.CONTENT_AUTHORITY, ItemsContract.PATH_HISTORY_ITEMS, ORDER_HISTORY_LIST_ID);
    sURI_MATCHER.addURI(ItemsContract.CONTENT_AUTHORITY, ItemsContract.PATH_HISTORY_ITEMS + "/#", ORDER_HISTORY_LIST_ITEM_ID);

  }


  @Override
  public boolean onCreate() {
    dbHelper = new ItemsDbHelper(getContext());
    return true;
  }


  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    // get Readable access to database
    SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
Log.i("ItemProvider","Query");
    //make new cursor
    Cursor cursor;
//get number of uri matcher ! is ITEMS=100 Or ITEM_ID=101 ?
    int match = sURI_MATCHER.match(uri);


    switch (match) {
//is is 100 se get quary on database
      case ITEMS:
        Log.i("ItemProvider","Items Match");

        // For the ITEMS code, query the pets table directly with the given
        // projection, selection, selection arguments, and sort order. The cursor
        // could contain multiple rows of the pets table.
        cursor = sqLiteDatabase.query(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS,//table name >>ITEMS
          projection,//List of columns that we want
          selection,// >> WHERE ...(like ID,NAME,PRICE,....) = ?
          selectionArgs,// =? >> 5 , Apple , MAcBOOK, 2200$
          null,
          null,
          null,
          sortOrder);
        Log.i("ItemProvider","getCount"+cursor.getCount());

        break;
//if code is 101 get information of a specific Item
      case ITEM_ID:
        // For the ITEM_ID code, extract out the ID from the URI.
        // For an example URI such as "content://pooria.storeitems/items/3",
        // the selection will be "_id=?" and the selection argument will be a
        // String array containing the actual ID of 3 in this case.
        //
        // For every "?" in the selection, we need to have an element in the selection
        // arguments that will fill in the "?". Since we have 1 question mark in the
        // selection, we have 1 String in the selection arguments' String array.
        selection = ItemsContract.ItemsEntry.ID + "=?";//WHERE ID=?
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};// =? ... like 6 , 5 , 4


        //get quary for a item
        cursor = sqLiteDatabase.query(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS, projection, selection, selectionArgs, null, null, sortOrder);

        break;
//else throw in execption

      case SELL_ITEMS:

        cursor = sqLiteDatabase.query(ItemsContract.ItemsEntry.TABLE_NAME_SELL_LIST, projection, selection, selectionArgs, null, null, null, sortOrder);
        Log.i("ItemProvider","SELL_ITEM: "+cursor.getCount());

        break;

      case SELL_ITEM_ID:

        cursor = sqLiteDatabase.query(ItemsContract.ItemsEntry.TABLE_NAME_SELL_LIST, projection, selection, selectionArgs, null, null, sortOrder);
        Log.i("ItemProvider","SELL_ITEM_ID"+cursor.getCount());

        break;

      case ORDER_HISTORY_LIST_ID:
        cursor=sqLiteDatabase.query(ItemsContract.ItemsEntry.TABLE_NAME_ORDER_HISTORY_LIST,projection,selection,selectionArgs,null,null,null,sortOrder);
        Log.i("ItemProvider","ORDER_HISTORY_LIST_ID"+cursor.getCount());

        break;
      default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);

    }
    // Set notification URI on the Cursor,
    // so we know what content URI the Cursor was created for.
    // If the data at this URI changes, then we know we need to update the Cursor.
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    // Return the cursor

    return cursor;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    final int match = sURI_MATCHER.match(uri);

    switch (match) {

      case ITEMS:
        return ItemsContract.ItemsEntry.CONTENT_LIST_TYPE;
      case ITEM_ID:

        return ItemsContract.ItemsEntry.CONTENT_ITEM_TYPE;

      case SELL_ITEMS:
        return ItemsContract.ItemsEntry.CONTENT_LIST_TYPE_FOR_SELL;

      case SELL_ITEM_ID:
        return ItemsContract.ItemsEntry.CONTENT_ITEM_TYPE_FOR_SELL;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
    }
  }


  @Override
  public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
    int MATCH = sURI_MATCHER.match(uri);
    SQLiteDatabase database = dbHelper.getWritableDatabase();


    //total row we are going to insert in history list mean : 4 items>>1)galaxyNote5 2)T-shirt 3)ball 4)laptop
    int totalRowInserted = 0;

    // total item counts >> means : 1)galaxynote5:2pcs  2)T-shirt : 4pcs    3) ball:2psc  4)laptop:3psc   >>>>>total is:11pcs
    int totalSellsItemCount = 0;
    switch (MATCH) {
      case ORDER_HISTORY_LIST_ID:
        try {
          database.beginTransaction();
          for (ContentValues value : values) {

            long rowInsertNumber = database.insert(ItemsContract.ItemsEntry.TABLE_NAME_ORDER_HISTORY_LIST, null, value);
            Log.i("Row", "Inserted: " + rowInsertNumber);

            if (rowInsertNumber > 0) {
              totalSellsItemCount += value.getAsInteger(ItemsContract.ItemsEntry.COLUMN_QUANTITY);

              totalRowInserted++;
            }
          }

          Log.i("item", "Counted : " + totalSellsItemCount);
          database.setTransactionSuccessful();
        } catch (Exception e) {
          e.printStackTrace();

        } finally {
          database.endTransaction();

        }

        if (totalRowInserted > 0) {

          getContext().getContentResolver().notifyChange(uri, null);
        }
        return totalRowInserted;



      default:
        return super.bulkInsert(uri,values);

    }

  }




  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    //check uri is valid or not
    int match = sURI_MATCHER.match(uri);

    switch (match) {

      case ITEMS:
        //if is uri in for of Items Table go to Insert new Items
        return insertItem(uri, values, ITEMS);
      //else Uri is invalid throw in exception

      case SELL_ITEMS:

        return insertItem(uri, values, SELL_ITEMS);


      default:
        throw new IllegalArgumentException("Uri is Invalid");
    }

  }


  private void itemChecker(ContentValues contentValues) {

    //check name validation
    if (contentValues.containsKey(ItemsContract.ItemsEntry.COLUMN_NAME)) {
      String name = contentValues.getAsString(ItemsContract.ItemsEntry.COLUMN_NAME);
      if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("Item requared a name ");
      }
    }

    //check price validation
    if (contentValues.containsKey(ItemsContract.ItemsEntry.COLUMN_PRICE)) {
      int price = contentValues.getAsInteger(ItemsContract.ItemsEntry.COLUMN_PRICE);
      if (price <= 0) {

        throw new IllegalArgumentException("Items Number is Invalid");
      }
    }


    //check quantity validation
    if (contentValues.containsKey(ItemsContract.ItemsEntry.COLUMN_QUANTITY)) {
      int quantity = contentValues.getAsInteger(ItemsContract.ItemsEntry.COLUMN_QUANTITY);
      if (quantity < 0) {

        throw new IllegalArgumentException("Items quantity is Invalid");
      }
    }

    //check shopperNumber validation
    if (contentValues.containsKey(ItemsContract.ItemsEntry.COLUMN_SUPPLIER)) {
      int shoperNumber = contentValues.getAsInteger(ItemsContract.ItemsEntry.COLUMN_SUPPLIER);

      if (shoperNumber < 0 || !ItemsContract.ItemsEntry.isValidShoper(shoperNumber)) {

        throw new IllegalArgumentException("shopper is invalid");
      }
    }
    //we don't need to check Discription because every input is valid such as Null

    if (contentValues.containsKey(ItemsContract.ItemsEntry.COLUMN_CATEGORY)) {

      int category = contentValues.getAsInteger(ItemsContract.ItemsEntry.COLUMN_CATEGORY);

    }
  }

  private Uri insertItem(Uri uri, ContentValues contentValues, int URI_ID) {

//check input items before save to database
    itemChecker(contentValues);
    Long id = 2L;
    //get write access to database
    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
    //insert a new item to Items table and get back row's number

    if (URI_ID == ITEMS) {
      id = sqLiteDatabase.insert(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS, null, contentValues);

      if (id == -1) {
        Toast.makeText(getContext(), "Error In Adding Item To DB" + id, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getContext(), "Added was successful" + id, Toast.LENGTH_SHORT).show();
      }
    }

    if (URI_ID == SELL_ITEMS) {

      id = sqLiteDatabase.insert(ItemsContract.ItemsEntry.TABLE_NAME_SELL_LIST, null, contentValues);
      if (id == -1) {
        Toast.makeText(getContext(), "Error In Adding Item To DB" + id, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getContext(), "Added was successful" + id, Toast.LENGTH_SHORT).show();
      }

    }


    if (URI_ID == ORDER_HISTORY_LIST_ID) {


      id = sqLiteDatabase.insert(ItemsContract.ItemsEntry.TABLE_NAME_ORDER_HISTORY_LIST, null, contentValues);
      if (id == -1) {
        Toast.makeText(getContext(), "Error In Adding Item To ORDER HISTORY LIST" + id, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(getContext(), "Added was successful TO ORDER HISTORY" + id, Toast.LENGTH_SHORT).show();

      }

    }
    // Notify all listeners that the data has changed for the item content URI
    getContext().getContentResolver().notifyChange(uri, null);

    // Once we know the ID of the new row in the table,
    // return the new URI with the ID appended to the end of it
    return ContentUris.withAppendedId(uri, id);

  }

  private int deleteITem(Uri uri, String selection, String[] selectionArgs, int URI_ID) {

    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
    int rowDeleted = 0;
    if (URI_ID == ITEMS || URI_ID == ITEM_ID) {

      rowDeleted = sqLiteDatabase.delete(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS, selection, selectionArgs);
    }

    if (URI_ID == SELL_ITEMS || URI_ID == SELL_ITEM_ID) {
      rowDeleted = sqLiteDatabase.delete(ItemsContract.ItemsEntry.TABLE_NAME_SELL_LIST, selection, selectionArgs);
    }
    if (URI_ID==ORDER_HISTORY_LIST_ID){

      rowDeleted=sqLiteDatabase.delete(ItemsContract.ItemsEntry.TABLE_NAME_ORDER_HISTORY_LIST,selection,selectionArgs);
    }

    getContext().getContentResolver().notifyChange(uri, null);

    return rowDeleted;

  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {


    int match = sURI_MATCHER.match(uri);

    switch (match) {
      case ITEMS:

        return deleteITem(uri, selection, selectionArgs, ITEMS);

      case ITEM_ID:

        selection = ItemsContract.ItemsEntry.ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};


        return deleteITem(uri, selection, selectionArgs, ITEM_ID);

      case SELL_ITEMS:
        return deleteITem(uri, selection, selectionArgs, SELL_ITEMS);


      case SELL_ITEM_ID:

        selection = ItemsContract.ItemsEntry.ID + "=?";

        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        return deleteITem(uri, selection, selectionArgs, SELL_ITEM_ID);

      case ORDER_HISTORY_LIST_ID:
        return deleteITem(uri,selection,selectionArgs,ORDER_HISTORY_LIST_ID);

      default:
        throw new IllegalArgumentException("delete was failed");
    }


  }


  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//check uri
    int match = sURI_MATCHER.match(uri);

    switch (match) {

      case ITEMS:
        return updateItem(uri, values, selection, selectionArgs, ITEMS);


      case ITEM_ID:
        selection = ItemsContract.ItemsEntry.ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return updateItem(uri, values, selection, selectionArgs, ITEM_ID);

      case SELL_ITEMS:


        return updateItem(uri, values, selection, selectionArgs, SELL_ITEMS);

      case SELL_ITEM_ID:
        selection = ItemsContract.ItemsEntry.ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return updateItem(uri, values, selection, selectionArgs, SELL_ITEM_ID);

      default:
        throw new IllegalArgumentException("Update Failed");
    }


  }


  private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs, int URI_ID) {
    //check input item before update a row
    itemChecker(values);


    // If there are no values to update, then don't try to update the database
    if (values.size() == 0) {
      return 0;
    }
    //get writing access to database
    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
    int rowUpdated = 0;
    if (URI_ID == ITEMS || URI_ID == ITEM_ID) {
      //get number of row updated from database with update method ;
      rowUpdated = sqLiteDatabase.update(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS, values, selection, selectionArgs);
    }

    if (URI_ID == SELL_ITEMS || URI_ID == SELL_ITEM_ID) {
      rowUpdated = sqLiteDatabase.update(ItemsContract.ItemsEntry.TABLE_NAME_SELL_LIST, values, selection, selectionArgs);

    }
    //if row update set notify data changer
    if (rowUpdated != 0 && getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }


    return rowUpdated;
  }


}
