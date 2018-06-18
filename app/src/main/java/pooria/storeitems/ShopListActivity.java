package pooria.storeitems;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pooria.storeitems.data.ItemsContract;
import pooria.storeitems.data.ItemsDbHelper;

/**
 * Created by Po0RiA on 2/9/2018.
 */

public class ShopListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
 // ItemsDbHelper itemsDbHelper = new ItemsDbHelper(this);
  private ListView mListView;
  CursorAdapter shopListAdapter;
  ContentValues[] contentValues;

  private final static String LOG_TAG = ShopListActivity.class.getName();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.shoplist_main);

    mListView = (ListView) findViewById(R.id.sell_list_view);
    mListView.setEmptyView(findViewById(R.id.storeitems));

    shopListAdapter = new ShoppingListAdapter(this, null);

  //  sqLiteDatabase = itemsDbHelper.getWritableDatabase();
    mListView.setAdapter(shopListAdapter);

    getLoaderManager().initLoader(0, null, this);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.shop_list_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.delete_basket) {

      int rowDeleted = getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, null, null);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String projection[] = new String[]{
      ItemsContract.ItemsEntry._ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_IMAGE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY};


    return new CursorLoader(this, ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, projection, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    shopListAdapter.swapCursor(data);

    readyToSendOrderHistory(data);

  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
shopListAdapter.swapCursor(null);
  }

  private void readyToSendOrderHistory(Cursor cursor) {


    int totalCount = cursor.getCount();
    if (totalCount <= 0) return;


    contentValues = new ContentValues[totalCount];
    try {
      cursor.moveToFirst();
      for (int i = 0; totalCount > i; i++) {
        Log.i("item","number"+i);

        int nameId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME);
        int priceId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE);
        int quantityId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);
        int imageId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_IMAGE);

        String name = cursor.getString(nameId);
        int price = cursor.getInt(priceId);
        int quantity = cursor.getInt(quantityId);

        byte imageinByte[] = cursor.getBlob(imageId);

        Long mCurrentTime = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(ItemsContract.ItemsEntry.COLUMN_NAME, name);
        values.put(ItemsContract.ItemsEntry.COLUMN_PRICE, price);
        values.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, quantity);
        values.put(ItemsContract.ItemsEntry.COLUMN_IMAGE, imageinByte);
        values.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, "tech");

        values.put(ItemsContract.ItemsEntry.COLUMN_DATE, mCurrentTime);

        contentValues[i] = values;
if (!cursor.isLast()){cursor.moveToNext();}
      }
    }catch (RuntimeException e){
      e.printStackTrace();
Log.i("print","stackTrace"+e);
    }

  }

}
