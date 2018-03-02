package pooria.storeitems;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import pooria.storeitems.data.ItemsAdapter;
import pooria.storeitems.data.ItemsContract;
import pooria.storeitems.data.ItemsDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String LOG_TAG = MainActivity.class.getName();
  TextView textView;
  ItemsDbHelper itemsDbHelper;
  CursorAdapter itemsAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    itemsDbHelper = new ItemsDbHelper(this);

    getLoaderManager().initLoader(0, null, this);

//set empty view when user doesnt select any item in list
    ListView listView = (ListView) findViewById(R.id.items_list_view);
    listView.setEmptyView(findViewById(R.id.emptyView));
    itemsAdapter = new ItemsAdapter(this, null);
    listView.setAdapter(itemsAdapter);
    // displayDatabaseInfo();

    FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    actionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), EditorActivity.class);

        startActivity(intent);

      }
    });


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        Uri uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI, id);
        intent.setData(uri);
        startActivity(intent);

      }
    });

  }




  private void InserPet() {
    ContentValues contentValues = new ContentValues();

    contentValues.put(ItemsContract.ItemsEntry.COLUMN_NAME, "ZenBook");
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION, "Is Very Nice Book");
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_PRICE, 500);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, 52);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_SUPPLIER, 1);


    Uri uri = getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI, contentValues);


  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/menu_editor.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
//check if menu item is delete button then delete list;
    if (item.getItemId() == R.id.delete_button) {
      deleteList();
      //read again database to update list
//displayDatabaseInfo();
    }
    if (item.getItemId() == R.id.insert) {
      InserPet();
    }
    if (item.getItemId() == R.id.basket) {

      Intent intent = new Intent(this, ShopListActivity.class);
      startActivity(intent);

    }

    return super.onOptionsItemSelected(item);
  }


  //delete list
  private void deleteList() {
    int id = getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI, null, null);


  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {


    String[] projection = new String[]{ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,
      ItemsContract.ItemsEntry.COLUMN_IMAGE};


    return new CursorLoader(this, ItemsContract.ItemsEntry.CONTENT_URI, projection, null, null, null);

  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    itemsAdapter.swapCursor(data);


  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    itemsAdapter.swapCursor(null);

  }
}

