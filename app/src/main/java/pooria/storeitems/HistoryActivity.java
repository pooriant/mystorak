package pooria.storeitems;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import pooria.storeitems.data.ItemsContract;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    CursorAdapter adapter;
private static final String LOG_TAG=HistoryActivity.class.getSimpleName();
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  setContentView(R.layout.history_activity);
      ListView history_listView = findViewById(R.id.history_list_view);
adapter=new HistoryAdapter(this,null);


history_listView.setAdapter(adapter);

getLoaderManager().initLoader(100,null,this);

  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    String [] projection={
      ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      ItemsContract.ItemsEntry.COLUMN_DATE,
      ItemsContract.ItemsEntry.COLUMN_IMAGE,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,

    };


   return new CursorLoader(this, ItemsContract.ItemsEntry.CONTENT_URI_ORDER_HISTORY_LIST,projection,null,null,null);

  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
adapter.swapCursor(null);
  }


  public void clearHistoryClicked (View view){
if (adapter.getCount()>0) {
  int rowDeleted = getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI_ORDER_HISTORY_LIST, null, null);

  Log.i(LOG_TAG, "Total items deleted : " + rowDeleted);
}else {

  Toast.makeText(this, "there is Nothing To delete", Toast.LENGTH_SHORT).show();
}


  }
}
