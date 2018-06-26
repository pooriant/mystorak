package pooria.storeitems.Category;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pooria.storeitems.R;
import pooria.storeitems.data.ItemsContract;


public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Toast toast;
    private static final String LOG_TAG = CategoryActivity.class.getSimpleName();
    private static final int NEW_CATEGORY_CODE = 500;
    ListView listView;
     CursorAdapter adapterCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_activity);


         Log.i(LOG_TAG, "OnCreate STart");

        adapterCursor = new CategoryCursorAdapter(this, null);
        listView = findViewById(R.id.categoryRecyclerViewList);

        listView.setAdapter(adapterCursor);



         getSupportLoaderManager().initLoader(0, null, this);

     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Insert_new_category) {

            Intent intent = new Intent(CategoryActivity.this, InsertNewCategory.class);
            startActivityForResult(intent, NEW_CATEGORY_CODE);

        }if(item.getItemId()==R.id.delete_category){

            int rowDetelet=getContentResolver().delete(ItemsContract.ItemsEntry.CONTENT_URI_CATEGORY_LIST,null,null);

            if (rowDetelet>0){
                Toast.makeText(this, "Your List Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {


        return new CursorLoader(this, ItemsContract.ItemsEntry.CONTENT_URI_CATEGORY_LIST, null, null, null, null);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        adapterCursor.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {

        super.onResume();


    }

    @Override
    protected void onRestart() {
         super.onRestart();
    }
}
