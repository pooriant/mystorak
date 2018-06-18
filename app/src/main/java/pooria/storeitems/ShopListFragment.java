package pooria.storeitems;

import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import pooria.storeitems.data.ItemsContract;

public class ShopListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

  private ListView mListView;
  CursorAdapter shopListAdapter;
  ContentValues[] contentValues;
  private final static String LOG_TAG = ShopListFragment.class.getName();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    shopListAdapter = new ShoppingListAdapter(getContext(), null);

    //  sqLiteDatabase = itemsDbHelper.getWritableDatabase();

  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.shoplist_main,container,false);

    mListView = view.findViewById(R.id.sell_list_view);
    mListView.setEmptyView(view.findViewById(R.id.storeitems_empty));
    mListView.setAdapter(shopListAdapter);
    Button button=view.findViewById(R.id.btn_order);

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(getContext(), "Toast", Toast.LENGTH_SHORT).show();
        int insertRowNumber = getActivity().getContentResolver().bulkInsert(ItemsContract.ItemsEntry.CONTENT_URI_ORDER_HISTORY_LIST, contentValues);


        Toast.makeText(getContext(), "Insert Sucssesssfull" + insertRowNumber, Toast.LENGTH_SHORT).show();


        int rowDeleted = getActivity().getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, null, null);
        Log.i(LOG_TAG, "row deleted: " + rowDeleted);


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new HistoryListFragment()).commit();
      }
    });
    return view;
  }



  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    LoaderManager loaderManager=getLoaderManager();
    loaderManager.initLoader(0,null,this);

  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    String projection[] = new String[]{
      ItemsContract.ItemsEntry._ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_IMAGE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY};


    return new CursorLoader(this.getContext(), ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, projection, null, null, null);
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    Log.i("onLoadFinish","data:" +data.getCount());

    shopListAdapter.swapCursor(data);
    readyToSendOrderHistory(data);

  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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
