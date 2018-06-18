package pooria.storeitems;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pooria.storeitems.data.ItemsContract;
import pooria.storeitems.data.ItemsDbHelper;

public class AddItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final String LOG_TAG = EditorActivity.class.getSimpleName();
  private static final int IMAGE_LOAD_RESULT = 200;
  private static final int TAKE_PHOTO_RESULT = 400;
  //list of global variables
  private int mShopperNumber;
  private int mCategoryNumber;
  EditText nameBox;
  EditText discriptionBox;
  EditText priceBox;
  EditText quantityBox;
  Button takePhoto;
  Button choosePhotoFromGallery;
  ImageView imageViewFrame;
  Uri uri;
  ItemsDbHelper dbHelper;
  private Spinner mCategorySpinner;
  PackageManager packageManager;
  byte imageInByte[];

  ArrayAdapter categorySpinnerAdapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dbHelper = new ItemsDbHelper(getContext());
    categorySpinnerAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.Category_list, android.R.layout.simple_spinner_item);

    categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


    setHasOptionsMenu(true);

    packageManager = getActivity().getBaseContext().getPackageManager();


  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.edit_item, container, false);
    nameBox = (EditText) view.findViewById(R.id.namebox);
    discriptionBox = (EditText) view.findViewById(R.id.discriptionbox);
    priceBox = (EditText) view.findViewById(R.id.pricebox);
    quantityBox = (EditText) view.findViewById(R.id.quantitybox);
    mCategorySpinner = (Spinner) view.findViewById(R.id.category_list_item);
    imageViewFrame = (ImageView) view.findViewById(R.id.image_item_frame);

    takePhoto = (Button) view.findViewById(R.id.take_photo);

    choosePhotoFromGallery = (Button) view.findViewById(R.id.choose_from_gallery);

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);

        if (selection.equals(getString(R.string.tech_List))) {

          mCategoryNumber = 0;

        } else if (selection.equals(getString(R.string.clothes_List))) {

          mCategoryNumber = 1;
        } else if (selection.equals(getString(R.string.home_List))) {

          mCategoryNumber = 2;
        }

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    mCategorySpinner.setAdapter(categorySpinnerAdapter);

    takePhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
          startActivityForResult(takePictureIntent, TAKE_PHOTO_RESULT);
        }
      }
    });


    choosePhotoFromGallery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_LOAD_RESULT);
          Log.i(LOG_TAG, "we need permission :");

        } else {
          Intent intent = new Intent(Intent.ACTION_PICK);
          intent.setType("image/*");
          startActivityForResult(intent, IMAGE_LOAD_RESULT);
        }
      }
    });

//with this method we can checked that we have a Bundle or not?
    //if our Argument is null means we have been clicked on Add item Icon and now we need to going to insert new item

    if (this.getArguments() == null) {

      Toast.makeText(getContext(), "Insert Your Details and Submit Save Button", Toast.LENGTH_SHORT).show();
      //change title
      getActivity().setTitle("Insert new Item");

      // if we have a Bundle means we are going to upadte a item and we came from on ItemClickListener
    } else {
      //get bundle to access item Id
      Bundle bundle = this.getArguments();
      long id = bundle.getLong("ItemId");
      //make itemUrl with id that came from Bundle
      uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI, id);
      //call loader to load data with this Uri
      LoaderManager loaderManager = getLoaderManager();
      loaderManager.initLoader(0, null, this);

      //change title
      getActivity().setTitle("Update Item");


    }

  }


   @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

    String[] projection = {
      ItemsContract.ItemsEntry.ID,
      ItemsContract.ItemsEntry.COLUMN_NAME,
      ItemsContract.ItemsEntry.COLUMN_DESCRIPTION,
      ItemsContract.ItemsEntry.COLUMN_PRICE,
      ItemsContract.ItemsEntry.COLUMN_QUANTITY,
      // ItemsContract.ItemsEntry.COLUMN_SUPPLIER,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,
      ItemsContract.ItemsEntry.COLUMN_IMAGE};

    return new CursorLoader(getContext(), uri, projection, null, null, null);
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

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


        // int supplier = data.getInt(supplierIndex);

        int category = data.getInt(categoryIndex);


        mCategoryNumber = category;

        mCategorySpinner.setSelection(mCategoryNumber);

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
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {

  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    //if permission granted go to Actvity result

    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.setType("image/*");
      Log.i(LOG_TAG, "we get permission :");

      startActivityForResult(intent, IMAGE_LOAD_RESULT);
    } else {
      Toast.makeText(getActivity().getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


  /**
   * @param requestCode white this code we known which request we must check
   * @param resultCode: result of checking data
   * @param data:data   that comefrom with request
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == -1) {

      //check request code
      switch (requestCode) {


        //if is equal to image load result get data and try to save as bitmap
        case IMAGE_LOAD_RESULT:

          //get uri (Adress image ) from data
          Uri uri = data.getData();
          //make input stream
          InputStream inputStream = null;
          try {
            //save input steram from uri
            inputStream = getActivity().getContentResolver().openInputStream(uri);
            //decode stream and save make bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


            imageViewFrame.setImageBitmap(bitmap);


            //save bitmap to byte array for saving in database
            saveBitmapInByteArray(bitmap);

            break;
          } catch (FileNotFoundException e) {
            e.printStackTrace();
            break;
          }


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
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    getActivity().getMenuInflater().inflate(R.menu.edit_menu, menu);

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.submit_button) {
      //insert();
      saveItem();
    }
    if (item.getItemId() == R.id.delete_item) {

      deleteItem();
    }
    return true;
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
    int rowDeleted = getActivity().getContentResolver().delete(uri, null, null);

    Toast.makeText(getContext(), "row Deleted: " + rowDeleted, Toast.LENGTH_SHORT).show();
  }


  private void saveItem() {
    //get input items
    String name = nameBox.getText().toString().trim();
    String discription = discriptionBox.getText().toString().trim();
    int price = Integer.parseInt(priceBox.getText().toString().trim());
    int quantity = Integer.valueOf(quantityBox.getText().toString().trim());
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

    contentValue.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, mCategoryNumber);

    contentValue.put(ItemsContract.ItemsEntry.COLUMN_IMAGE, imageInByte);


    //if uri is null means we have new item and must to add into database
    if (uri == null) {
      //insert item into database
      uri= getActivity().getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI, contentValue);

      Toast.makeText(getContext(), "Your ITem Saved", Toast.LENGTH_SHORT).show();
      backToHomeFragment();


    }
    //uri exist and we should to update this pat
    else {
      //do update method and getback number of row that updated
      int rowUpdated = getActivity().getContentResolver().update(uri, contentValue, null, null);
      //if row update successful Toast a massage
      if (rowUpdated > 0) {
        Toast.makeText(getContext(), "Row Updated Successfully :" + rowUpdated, Toast.LENGTH_SHORT).show();
        backToHomeFragment();
      }
      // Toast a massage to show update failed
      else {

        Toast.makeText(getContext(), "Updated Operation Was Failed", Toast.LENGTH_SHORT).show();
      }
    }
  }


  private void backToHomeFragment() {

    nameBox.setText("");
    discriptionBox.setText("");
    priceBox.setText("");
    quantityBox.setText("");

    getActivity().getSupportFragmentManager()
      .beginTransaction()
      .replace(R.id.fragment_frame, new HomeListFragment())
      .commitNow();


  }
}
