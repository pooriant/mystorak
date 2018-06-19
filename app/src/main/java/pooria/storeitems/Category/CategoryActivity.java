package pooria.storeitems.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import pooria.storeitems.EditorActivity;
import pooria.storeitems.R;



public class CategoryActivity  extends Activity implements CategoryAdapter.CategoryAdapterOnClickHandler {

Toast toast;
    private static final String LOG_TAG=CategoryActivity.class.getSimpleName();
private ArrayList<String> CategoryNames;
    CategoryAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    setContentView(R.layout.category_activity);


    CategoryNames=new ArrayList<>();
        Log.i(LOG_TAG,"OnCreate STart");
        RecyclerView recyclerView= findViewById(R.id.categoryRecyclerViewList);

        adapter=new CategoryAdapter( CategoryNames,this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);



        papolateCategory();
    }






    private void papolateCategory(){

        CategoryNames.add("Tech");
        CategoryNames.add("Samsung");
        CategoryNames.add("Apple");
        CategoryNames.add("LG");
        CategoryNames.add("ASUS");
adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String Title) {
        if (toast!=null)
        {
            toast.cancel();
        }
        toast=Toast.makeText(this, "Your Category Is :"+Title, Toast.LENGTH_SHORT);

        toast.show();

        Intent intent=new Intent(this, EditorActivity.class);
        intent.putExtra("category",Title);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
