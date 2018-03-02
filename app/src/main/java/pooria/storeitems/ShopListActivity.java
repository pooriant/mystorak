package pooria.storeitems;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;

import pooria.storeitems.data.ItemsContract;

/**
 * Created by Po0RiA on 2/9/2018.
 */

public class ShopListActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {
private ListView mListView;
  CursorAdapter shopListAdapter;
  private final static String LOG_TAG=ShopListActivity.class.getName();
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  setContentView(R.layout.shoplist_main);

mListView=(ListView)findViewById(R.id.sell_list_view);
    mListView.setEmptyView(findViewById(R.id.storeitems));

    shopListAdapter=new ShoppingListAdapter(this,null);


    mListView.setAdapter(shopListAdapter);

    getLoaderManager().initLoader(0, null, this);




  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.shop_list_menu,menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()==R.id.delete_basket)
    {

     int rowDeleted= getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST,null,null);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String projection[]=new String[]{
      ItemsContract.ItemsEntry._ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_IMAGE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY};





    return new CursorLoader(this,ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST,projection,null,null,null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    shopListAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

  }
}
