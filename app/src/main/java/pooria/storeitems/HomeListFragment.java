package pooria.storeitems;


import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import pooria.storeitems.data.HomeListAdapter;
import pooria.storeitems.data.ItemsContract;
import pooria.storeitems.data.ItemsDbHelper;

public class HomeListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String LOG_TAG = MainActivity.class.getName();
  TextView textView;
  ItemsDbHelper itemsDbHelper;
  CursorAdapter itemsAdapter;
ImageView emptyView;
  ListView listView;
@Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Log.i("on ","Create");
    super.onCreate(savedInstanceState);
    itemsAdapter = new HomeListAdapter(getContext(), null);
setHasOptionsMenu(true);


  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    Log.i("on ","CreateView");
    View view = inflater.inflate(R.layout.activity_main, container, false);
    itemsDbHelper = new ItemsDbHelper(getContext());
     listView = view.findViewById(R.id.items_list_view);
    emptyView =view.findViewById(R.id.emptyView);
    listView.setEmptyView(emptyView);

    listView.setAdapter(itemsAdapter);

    return view;
  }




   @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    Log.i("on ","ActivityCreated");

    super.onActivityCreated(savedInstanceState);
    LoaderManager loaderManager = getLoaderManager();
    loaderManager.initLoader(0, null, this);



    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Fragment fragment=new AddItemFragment();

        Bundle bundle=new Bundle();
        bundle.putLong("ItemId",id);
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_frame,fragment)
          .commit();
      }
    });


   }



  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
  getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
//check if menu item is delete button then delete list;
    if (item.getItemId() == R.id.delete_button) {
       //read again database to update list
//displayDatabaseInfo();


    }

    if (item.getItemId() == R.id.basket) {

Intent intent=new Intent(getActivity(),ShopListActivity.class);
startActivity(intent);


    }
    return super.onOptionsItemSelected(item);
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {


    String[] projection = new String[]{ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,
      ItemsContract.ItemsEntry.COLUMN_IMAGE};
    Log.i("we are", "in onCreate Loader");


    return new CursorLoader(this.getContext(), ItemsContract.ItemsEntry.CONTENT_URI, projection, null, null, null);

  }
  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    if (data!=null){

      emptyView.setVisibility(View.GONE);

    }
    Log.i("we are", "On Load Finish");

    data.moveToFirst();
    data.getCount();
     itemsAdapter.swapCursor(data);
    Log.i("name is ", "" + data.getCount());

  }
  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    itemsAdapter.swapCursor(null);

  }

}
