package pooria.storeitems;

import android.Manifest;
import android.content.ContentValues;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pooria.storeitems.Category.CategoryActivity;
import pooria.storeitems.data.ItemsContract;
import pooria.storeitems.data.ItemsDbHelper;

/**
 * Created by Po0RiA on 1/2/2018.
 * with this activity we can add new item or edit exist item
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private static final int IMAGE_LOAD_RESULT = 200;
    private static final int TAKE_PHOTO_RESULT = 400;
    private static final int GET_CATEGORY_RESULT = 300;
    private static final String TAG_HOMEPAGE = "tag_home";
    static final int REQUEST_TAKE_PHOTO = 1;

    //list of global variables
    EditText nameBox;
    EditText discriptionBox;
    EditText priceBox;
    EditText quantityBox;
    Button takePhoto;
    Button choosePhotoFromGallery;
    ImageView imageViewFrame;
    Uri uri;
    ItemsDbHelper dbHelper;
    PackageManager packageManager;
    byte imageInByte[];

    String mCurrentPhotoPath;

    TextView mMyCategory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        //make a instance of database
        dbHelper = new ItemsDbHelper(this);
        //initialize views like button Edittext and etc...
        nameBox = (EditText) findViewById(R.id.namebox);
        discriptionBox = (EditText) findViewById(R.id.discriptionbox);
        priceBox = (EditText) findViewById(R.id.pricebox);
        quantityBox = (EditText) findViewById(R.id.quantitybox);

        mMyCategory = (TextView) findViewById(R.id.my_category);

        mMyCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getCategoryIntent = new Intent(EditorActivity.this, CategoryActivity.class);
                startActivityForResult(getCategoryIntent, GET_CATEGORY_RESULT);
            }
        });

        //active package manager for enable notification of permissions
        packageManager = getBaseContext().getPackageManager();

        imageViewFrame = (ImageView) findViewById(R.id.image_item_frame);

        takePhoto = (Button) findViewById(R.id.take_photo);

        choosePhotoFromGallery = (Button) findViewById(R.id.choose_from_gallery);


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, TAKE_PHOTO_RESULT);
//                }
            }
        });


        choosePhotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(EditorActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_LOAD_RESULT);
                    Log.i(LOG_TAG, "we need permission :");

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_LOAD_RESULT);
                }
            }
        });


        uri = getIntent().getData();

        if (uri == null) {
            setTitle("Insert new Item");

        } else {
            setTitle("Update Item");
            getSupportLoaderManager().initLoader(0, null, this);
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    /**
     * @param requestCode: whit this code we known where this request confrom is
     * @param permissions: which permisson we want to check
     * @param grantResults : we get result code when we getting back from checking method
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //if permission granted go to Actvity result

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            Log.i(LOG_TAG, "we get permission :");

            startActivityForResult(intent, IMAGE_LOAD_RESULT);
        } else {
            Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * @param requestCode white this code we known which request we must check
     * @param resultCode: result of checking data
     * @param data:data   that comefrom with request
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            //check request code
            switch (requestCode) {


                //if is equal to image load result get data and try to save as bitmap
                case IMAGE_LOAD_RESULT:

                    //get uri (Adress image ) from data
                    Uri uri = data.getData();
                    Log.i(LOG_TAG,"Uri "+uri);

                    //  Glide.with(this).load(uri).into(imageViewFrame);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerInside();

                    Glide.with(this)
                            .load(uri)
                            .apply(requestOptions)
                            .into(imageViewFrame);


                    break;

                //if data equal to this get data and save as bitmap
                case TAKE_PHOTO_RESULT:
                    //get extras from data

                    Bundle extras = data.getExtras();
                    //make bitmap from extras
                    Bitmap bitmap = (Bitmap) extras.get("data");


                    imageViewFrame.setImageBitmap(bitmap);

                    //save bitmap to byte array
                    saveBitmapInByteArray(bitmap);
                    break;

                case REQUEST_TAKE_PHOTO:

                    requestOptions = new RequestOptions();
                    requestOptions.centerInside();

                    Glide.with(this)
                            .load(mCurrentPhotoPath)
                            .apply(requestOptions)
                            .into(imageViewFrame);
galleryAddPic();

                    break;
                //if data is equal to CategoryName
                case GET_CATEGORY_RESULT:
                    //save CategoryName as a String
                    String category = data.getStringExtra("Category");
                    //set that name in CategoryBox
                    mMyCategory.setText(category);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveBitmapInByteArray(Bitmap bitmap) {
//make new byte array OutPutStream for stream  and save image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

//  compress bitmap to ByteArrayoutPutStream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        //convert ByteArrayOupPutStream to ByteArray()
        imageInByte = bytes.toByteArray();
    }


    private void deleteItem() {
//remove this item with Uri
        int rowDeleted = getContentResolver().delete(uri, null, null);

        Toast.makeText(this, "row Deleted: " + rowDeleted, Toast.LENGTH_SHORT).show();
    }


    private void saveItem() {
        //get input items
        String name = nameBox.getText().toString().trim();
        String discription = discriptionBox.getText().toString().trim();
        int price = Integer.parseInt(priceBox.getText().toString().trim());
        int quantity = Integer.valueOf(quantityBox.getText().toString().trim());
        String mCategory = mMyCategory.getText().toString().trim();
        if (imageInByte == null) {

            //if imageInByte is null means we dont change image from camera or gallery so we need to save image again to array

            //get image from image View as drawable
            Drawable drawable = imageViewFrame.getDrawable();

            //convert drawable to Bitmap
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
            //get bitmap from drawable
            Bitmap bitmap = bitmapDrawable.getBitmap();
            //send to this method and save as ByteArray
            saveBitmapInByteArray(bitmap);

        }
        //make a new content value to get input data into variable
        ContentValues contentValue = new ContentValues();

        contentValue.put(ItemsContract.ItemsEntry.COLUMN_NAME, name);
        contentValue.put(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION, discription);
        contentValue.put(ItemsContract.ItemsEntry.COLUMN_PRICE, price);
        contentValue.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, quantity);

        contentValue.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, mCategory);

        contentValue.put(ItemsContract.ItemsEntry.COLUMN_IMAGE, imageInByte);


        //if uri is null means we have new item and must to add into database
        if (uri == null) {
            //insert item into database
            uri = getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI, contentValue);

            Toast.makeText(this, "Your ITem Saved", Toast.LENGTH_SHORT).show();


        }
        //uri exist and we should to update this pat
        else {
            //do update method and getback number of row that updated
            int rowUpdated = getContentResolver().update(uri, contentValue, null, null);
            //if row update successful Toast a massage
            if (rowUpdated > 0) {
                Toast.makeText(this, "Row Updated Successfully :" + rowUpdated, Toast.LENGTH_SHORT).show();

            }
            // Toast a massage to show update failed
            else {

                Toast.makeText(this, "Updated Operation Was Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/edit_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.edit_menu, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
//if menu item equal to submit button do insert method and finish activity
        if (item.getItemId() == R.id.submit_button) {


            //insert();
            saveItem();

            finish();

        }
        if (item.getItemId() == R.id.delete_item) {

            deleteItem();
            finish();
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ItemsContract.ItemsEntry.ID,
                ItemsContract.ItemsEntry.COLUMN_NAME,
                ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
                ItemsContract.ItemsEntry.COLUMN_PRICE,
                ItemsContract.ItemsEntry.COLUMN_QUANTITY,
                ItemsContract.ItemsEntry.COLUMN_CATEGORY,
                ItemsContract.ItemsEntry.COLUMN_IMAGE};

        return new CursorLoader(this, uri, projection, null, null, null);
    }


    //load data from cursor : we use this method for query at first time
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Find the columns of pet attributes that we're interested in

        int nameIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME);
        int discriptionIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION);
        int priceIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE);
        int quantityIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);
        int categoryIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_CATEGORY);

        int imageIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_IMAGE);

//means if cursor is not empty and it has at least a row as first row ,cursor can  do  this command ! cursor in default is -1 and we must send thair to 1 to start check data
        try {
            if (data.moveToFirst()) {
                // Extract out the value from the Cursor for the given column index

                String name = data.getString(nameIndex);
                String discription = data.getString(discriptionIndex);
                int price = data.getInt(priceIndex);
                int quantity = data.getInt(quantityIndex);

                byte imageInByte[] = data.getBlob(imageIndex);

                //read byte and stream them and save in ByteArrayOutPutStream
                ByteArrayInputStream outputStream = new ByteArrayInputStream(imageInByte);

                //decode stream to make new bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(outputStream);

                imageViewFrame.setImageBitmap(bitmap);


                String category = data.getString(categoryIndex);


                mMyCategory.setText(category);
                // Update the views on the screen with the values from the database

                nameBox.setText(name);
                discriptionBox.setText(discription);
                priceBox.setText(String.valueOf(price));
                quantityBox.setText(String.valueOf(quantity));


            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            //close corsur and release data
            data.close();

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(LOG_TAG,"CurrentPath"+mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "pooria.storeitems.fileprovider",
                        photoFile);
                Log.i(LOG_TAG,"URI PHOTO "+photoURI);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}


