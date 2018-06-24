package pooria.storeitems.Category;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pooria.storeitems.EditorActivity;
import pooria.storeitems.R;
import pooria.storeitems.data.ItemsContract;

public class InsertNewCategory extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_category);

        editText = findViewById(R.id.insert_new_category);

    }


    //if user Clicked on  saveNewCategory Button we will send a intent to EditorActivity with editor OutPut
    public void onSaveCategoryClicked(View view) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {

            String newCategory = editText.getText().toString().trim();

            Log.i("category","is: "+newCategory);
            ContentValues newCategoryValue = new ContentValues();
            newCategoryValue.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, newCategory);

            Uri uri = getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI_CATEGORY_LIST, newCategoryValue);

            Log.i("NewCategory", "" + newCategory + "Uri Is : " + uri);

            //send intent to Editor Activity
            //Intent intent = new Intent(InsertNewCategory.this, EditorActivity.class);
            //get edittext.getText and put them into PutExtra for The intent
            //intent.putExtra("title", newCategory);
            //set Result to Ok and pass The Intent to that
            //setResult(Activity.RESULT_OK, intent);
            //finish activity
            finish();

        } else {

            //if editText is empty make a Toast
            Toast.makeText(InsertNewCategory.this, "Insert Your Category", Toast.LENGTH_SHORT).show();
        }
    }
}
