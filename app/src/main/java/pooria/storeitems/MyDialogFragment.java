package pooria.storeitems;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;

import pooria.storeitems.data.ItemsContract;

public class MyDialogFragment extends android.app.DialogFragment {
    EditText editText;
    View view;
    private Uri mUri;
    private String mCategoryName;


    public  void CategoryValuesForUpdate(Uri uri, String CategoryName){
        this.mUri=uri;
        this.mCategoryName=CategoryName;


    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_category_dialog_layout, null);

        editText = view.findViewById(R.id.edit_this_category);
      //  Bundle bundle = getArguments();

//        String mCategory = bundle.getString("CategoryName");
//        final int mId = bundle.getInt("CategoryId");
//        final Uri uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI_CATEGORY_LIST, mId);

        editText.setText(mCategoryName);


        builder.setMessage("This is a FRagment")
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(view)

                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     mCategoryName = editText.getText().toString().trim();

                        updateCategoryName(mCategoryName, mUri);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {


                        MyDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void updateCategoryName(String name, Uri uri) {
        ContentValues values = new ContentValues();
        values.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, name);


        int rowUpdated = getActivity().getContentResolver().update(uri, values, null, null);

        Toast.makeText(getActivity(), "THis Row Updated: " + rowUpdated, Toast.LENGTH_SHORT).show();
    }

}
