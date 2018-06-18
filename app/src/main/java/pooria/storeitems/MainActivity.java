package pooria.storeitems;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

import pooria.storeitems.data.HomeListAdapter;
import pooria.storeitems.data.ItemsContract;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getName();
    CursorAdapter itemsAdapter;

     BottomNavigationView navigation;
    private List<android.support.v4.app.Fragment> fragments = new ArrayList<>(5);
    private static final String TAG_HISTORY = "tag_history";
    private static final String TAG_HOMEPAGE = "tag_home";
    private static final String TAG_ADDITEM = "tag_add_item";
    private static final String TAG_PROFILE = "tag_profile";
    private static final String TAG_BASKET = "tag_basket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //  itemsDbHelper = new ItemsDbHelper(this);
        itemsAdapter = new HomeListAdapter(this, null);
        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);



//    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//      @Override
//      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
//        Uri uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI, id);
//        intent.setData(uri);
//        startActivity(intent);
//
//      }
//    });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int Id = item.getItemId();

                switch (Id) {
                    case R.id.title_Home_List:
                        setTitle("Main List");

                        switchFragment(0, TAG_HOMEPAGE);
                        return true;
                    case R.id.title_addItem:

                        //     Toast.makeText(MainActivity.this, "Add Item Clicked", Toast.LENGTH_SHORT).show();          return true;
                        //switchFragment(1, TAG_ADDITEM);
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        startActivity(intent);

                        return true;
                    case R.id.title_history:
                        setTitle("History");

                        //   Toast.makeText(MainActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();          return true;
                        switchFragment(2, TAG_HISTORY);
                        return true;
                    case R.id.title_Basket:
                        // Toast.makeText(MainActivity.this, "setting Clicked", Toast.LENGTH_SHORT).show();          return true;
                        setTitle("Basket");
                        switchFragment(3, TAG_BASKET);

                        return true;
                    case R.id.title_profile:
                        setTitle("Profile");

                        switchFragment(4, TAG_PROFILE);
                        //Toast.makeText(MainActivity.this, "profile Clicked", Toast.LENGTH_SHORT).show();          return true;

                        return true;
                }
                return false;
            }
        });

        buildFragmentsList();

        switchFragment(0, TAG_HOMEPAGE);
    }


    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, fragments.get(pos), tag)
                .commit();
    }


    private void buildFragmentsList() {
        HomeListFragment homeListFragment = new HomeListFragment();
        ShopListFragment shopListFragment = new ShopListFragment();
        HistoryListFragment HistoryFragment = new HistoryListFragment();
        emptyFragment ProfileFragment = buildFragment("PROFILE");

        fragments.add(homeListFragment);
        fragments.add(new AddItemFragment());
        fragments.add(HistoryFragment);
        fragments.add(shopListFragment);
        fragments.add(ProfileFragment);


    }

    private emptyFragment buildFragment(String title) {
        return emptyFragment.newInstance(title);

    }


    //delete list
    private void deleteList() {
        getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI, null, null);


    }

    @Override
    protected void onResume() {
        super.onResume();

            navigation.setSelectedItemId(R.id.title_Home_List);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"OnPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    Log.i(LOG_TAG,"OnDestroy");
    }
}

