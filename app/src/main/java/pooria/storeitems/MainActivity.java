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
  private final static String LOG_TAG=MainActivity.class.getName();
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
          Log.i(LOG_TAG, "id is :" + id);
          Log.i(LOG_TAG, "id is :" + position);
          Intent intent = new Intent(MainActivity.this, EditorActivity.class);
          Uri uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI, id);
          intent.setData(uri);
          startActivity(intent);

        }
      });

  }






  @Override
  protected void onStart() {
    super.onStart();
   // InserPet();
    //displayDatabaseInfo();
//insert();
   // displayDatabaseInfo();
  }

  private void InserPet() {
    ContentValues contentValues=new ContentValues();

    contentValues.put(ItemsContract.ItemsEntry.COLUMN_NAME,"ZenBook");
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,"Is Very Nice Book");
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_PRICE,500);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY,52);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_SUPPLIER,1);



    Uri uri=getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI,contentValues);



  }

  public void displayDatabaseInfo(){
  //get access for write on database

  SQLiteDatabase sqLiteDatabase=itemsDbHelper.getReadableDatabase();

  //all we want to get back from cursor like name discription id and else ....
String[] projection={ItemsContract.ItemsEntry.ID,
  ItemsContract.ItemsEntry.COLUMN_NAME,
  ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
  ItemsContract.ItemsEntry.COLUMN_PRICE,
  ItemsContract.ItemsEntry.COLUMN_QUANTITY,
  ItemsContract.ItemsEntry.COLUMN_SUPPLIER};



  /**
   * this method is like this Select (projection=*) FROM TABLE_NAME_ITEMS
   * {@link cursor} give us a random read-write access to result of this query with cursor we can change location throughout our table
   */
 Cursor cursor = sqLiteDatabase.query(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS, projection, null, null, null, null, null);

  try {

//we set schema of table to help us how we should insert items value for table

textView.setText("number of rows is "+cursor.getCount() +"\n");
    textView.append(ItemsContract.ItemsEntry.COLUMN_NAME+"-"
      + ItemsContract.ItemsEntry.COLUMN_DESCRIPTION+"-"
      + ItemsContract.ItemsEntry.COLUMN_PRICE+"-"
      + ItemsContract.ItemsEntry.COLUMN_QUANTITY+"-"
      + ItemsContract.ItemsEntry.COLUMN_SUPPLIER+"\n");



    //get number of each column for getting inner value of that number
    int idIndex=cursor.getColumnIndex(ItemsContract.ItemsEntry.ID);
    int nameIndex=cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME);
    int discriptionIndex=cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION);
    int priceIndex=cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE);
    int quantityIndex=cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);
    int supplierIndex=cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_SUPPLIER);

//until next cursor is exist that mean (until we have another row to count) cursor must goes to that.
    while (cursor.moveToNext()){
//for each row add every column index to string
      int id=cursor.getInt(idIndex);
      String name=cursor.getString(nameIndex);
      String discription=cursor.getString(discriptionIndex);
      int price=cursor.getInt(priceIndex);
      int quantity=cursor.getInt(quantityIndex);
      int supplier=cursor.getInt(supplierIndex);
      String s="";
      if (supplier== ItemsContract.ItemsEntry.shopper1){
        s=getString(R.string.tech_List);
      }if (supplier== ItemsContract.ItemsEntry.shopper2){
        s=getString(R.string.clothes_List);

      }else if (supplier== ItemsContract.ItemsEntry.shopper3){
        s=getString(R.string.home_List);
      }




//add all string to textView
      textView.append(id+"-"+name+"-"+discription+"-"+price+"-"+quantity+"-"+s+"\n");


    }



  }finally {
    //when cursor finish close it to release memory
    cursor.close();
  }



}


//write new item into database
private void insert(){
//get access for writing on database
  SQLiteDatabase sqLiteDatabase=itemsDbHelper.getWritableDatabase();

  //for add new item to listItems we use content value with 2 params
  /*
   *  contentValues.put(ColumnKey,columnValue);
   */
  ContentValues contentValues=new ContentValues();
  contentValues.put(ItemsContract.ItemsEntry.COLUMN_NAME,"ZenBook");
  contentValues.put(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,"Is Very Nice Book");
  contentValues.put(ItemsContract.ItemsEntry.COLUMN_PRICE,500);
  contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY,52);
  contentValues.put(ItemsContract.ItemsEntry.COLUMN_SUPPLIER,1);

//send request for adding new row to database with tablename,null,and content
  Long rowUpdated=sqLiteDatabase.insert(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS,null,contentValues);

  Toast.makeText(this, "Updated: "+rowUpdated, Toast.LENGTH_SHORT).show();
}


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/menu_editor.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.main_menu,menu);
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
//check if menu item is delete button then delete list;
    if (item.getItemId()==R.id.delete_button){
      deleteList();
      //read again database to update list
//displayDatabaseInfo();
    }
    if (item.getItemId()==R.id.insert)
    {
      InserPet();
    }if (item.getItemId()==R.id.basket){

      Intent intent=new Intent(this,ShopListActivity.class);
      startActivity(intent);

    }

    return super.onOptionsItemSelected(item);
  }




  //delete list
  private void deleteList(){
int id=getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI,null,null);


  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    /*
    String[]projection=new String[]{ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      ItemsContract.ItemsEntry.COLUMN_SUPPLIER,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,
      ItemsContract.ItemsEntry.COLUMN_IMAGE};
    */

    String[]projection=new String[]{ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,
      ItemsContract.ItemsEntry.COLUMN_IMAGE};


    return new CursorLoader(this,ItemsContract.ItemsEntry.CONTENT_URI,projection,null,null,null);

  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    Log.e(LOG_TAG,"LoaderOnLoad Finish");
    itemsAdapter.swapCursor(data);

    //data.close();
//itemsAdapter.swapCursor(null);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    Log.e(LOG_TAG,"LoaderOnLoad Reset");
   itemsAdapter.swapCursor(null);

  }
}

