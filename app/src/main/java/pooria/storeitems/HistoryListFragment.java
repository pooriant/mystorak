package pooria.storeitems;

import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import pooria.storeitems.data.ItemsContract;

public class HistoryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
  CursorAdapter adapter;
  private static final String LOG_TAG=HistoryActivity.class.getSimpleName();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter=new HistoryAdapter(getContext(),null);

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view=inflater.inflate(R.layout.history_fragment,container,false);

    ListView listView=view.findViewById(R.id.history_fragment_list_view);

listView.setAdapter(adapter);

return view;  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    LoaderManager loaderManager=getLoaderManager();
    loaderManager.initLoader(0,null,this);

  }


  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

    String [] projection={
      ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      ItemsContract.ItemsEntry.COLUMN_DATE,
      ItemsContract.ItemsEntry.COLUMN_IMAGE,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,

    };


    return new CursorLoader(getContext(), ItemsContract.ItemsEntry.CONTENT_URI_ORDER_HISTORY_LIST,projection,null,null,null);

  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);

  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }



}
