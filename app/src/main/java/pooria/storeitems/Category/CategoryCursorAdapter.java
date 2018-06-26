package pooria.storeitems.Category;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import pooria.storeitems.EditorActivity;
import pooria.storeitems.MyDialogFragment;
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
    public void bindView(View view, final Context context, Cursor cursor) {


        TextView textView = (TextView) view.findViewById(R.id.Category_name_View);

        int categoryIndex = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_CATEGORY);

        final int id = cursor.getColumnIndex(ItemsContract.ItemsEntry.ID);

        final long idValue = cursor.getLong(id);

        final Uri uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI_CATEGORY_LIST, idValue);


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


        ImageButton imageButton = view.findViewById(R.id.menu_dot);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View imagePopupView) {

                showPopupMenu(imagePopupView,uri ,categoryName);


            }
        });



    }

private void showPopupMenu(View v , final Uri uri, final String categoryName){
    PopupMenu popup = new PopupMenu(mCurrentActivity, v);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.edit_category_menu, popup.getMenu());
    popup.show();
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.delete_a_category:

                    mCurrentActivity.getContentResolver().delete(uri, null, null);


                    Toast.makeText(mCurrentActivity, "That is ", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.Edit_a_category:

                    FragmentManager manager = mCurrentActivity.getFragmentManager();
                    MyDialogFragment myDialogFragment = new MyDialogFragment();

                    myDialogFragment.CategoryValuesForUpdate(uri, categoryName);

                    myDialogFragment.show(manager, "editorFragment");

                    break;
            }


            return true;
        }
    });
}
}

