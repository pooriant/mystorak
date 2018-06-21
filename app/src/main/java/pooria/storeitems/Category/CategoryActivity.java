package pooria.storeitems.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import pooria.storeitems.EditorActivity;
import pooria.storeitems.R;


public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterOnClickHandler {

    Toast toast;
    private static final String LOG_TAG = CategoryActivity.class.getSimpleName();
    private static final int NEW_CATEGORY_CODE = 500;
    private ArrayList<String> CategoryNames;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_activity);


        CategoryNames = new ArrayList<>();
        Log.i(LOG_TAG, "OnCreate STart");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new CategoryAdapter(CategoryNames, this);


        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerViewList);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        papolateCategory();

        recyclerView.setAdapter(adapter);


    }


    private void papolateCategory() {

        CategoryNames.add("Tech");
        CategoryNames.add("Samsung");
        CategoryNames.add("Apple");
        CategoryNames.add("LG");
        CategoryNames.add("ASUS");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String Title) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, "Your Category Is :" + Title, Toast.LENGTH_SHORT);

        toast.show();

        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("Category", Title);
        setResult(Activity.RESULT_OK, intent);
        finish();
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

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case NEW_CATEGORY_CODE:

String newCategory=data.getStringExtra("title");
CategoryNames.add(newCategory);
adapter.notifyDataSetChanged();

                    break;


                    default:

                    break;
            }
        }


        super.onActivityResult(requestCode, resultCode, data);


    }


}
