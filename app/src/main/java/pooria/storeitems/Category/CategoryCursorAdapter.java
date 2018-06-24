package pooria.storeitems.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import pooria.storeitems.EditorActivity;
import pooria.storeitems.R;
import pooria.storeitems.data.ItemsContract;

public class CategoryCursorAdapter extends CursorAdapter {
    private Activity mCurrentActivity;

    CategoryCursorAdapter(Activity currentActivity, Cursor c) {
        super(currentActivity, c, 0);
        mCurrentActivity = currentActivity;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.category_recyceler_item, parent, false);


    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textView = (TextView) view.findViewById(R.id.Category_name_View);

        int categoryIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_CATEGORY);

        final String categoryName = cursor.getString(categoryIndex);

        textView.setText(categoryName);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCurrentActivity, EditorActivity.class);
                intent.putExtra("Category", categoryName);
                mCurrentActivity.setResult(Activity.RESULT_OK, intent);
                mCurrentActivity.finish();


            }
        });

    }


}
