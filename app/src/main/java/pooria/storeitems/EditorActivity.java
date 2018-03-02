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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

/**
 * Created by Po0RiA on 1/2/2018.
 * with this activity we can add new item or edit exist item
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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
  private Spinner mShopperSpinner;
  private Spinner mCategorySpinner;
  PackageManager packageManager;
  byte imageInByte[];
  ArrayAdapter techShopperList;
  ArrayAdapter clothesShopperList;
  ArrayAdapter homeShopperList;
  ArrayAdapter categorySpinnerAdapter;
private boolean isTouched=false;


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
    mShopperSpinner = (Spinner) findViewById(R.id.spinner_items);
    mCategorySpinner = (Spinner) findViewById(R.id.category_list_item);


    categorySpinnerAdapter = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Category_list, android.R.layout.simple_spinner_item);
    categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

    mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                 @Override
                                                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  String selection=(String) parent.getItemAtPosition(position);

                                                   if (selection.equals(getString(R.string.tech_List))){

                                                     mCategoryNumber=0;

                                                   }else if (selection.equals(getString(R.string.clothes_List))){

                                                     mCategoryNumber=1;
                                                   }else if (selection.equals(getString(R.string.home_List))){

                                                     mCategoryNumber=2;
                                                   }

                                                 }

                                                 @Override
                                                 public void onNothingSelected(AdapterView<?> parent) {

                                                 }
                                               });


    mCategorySpinner.setAdapter(categorySpinnerAdapter);


    techShopperList = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Tech_shoppers_List, android.R.layout.simple_spinner_item);
    techShopperList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


    clothesShopperList = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Clothes_shoppers_list, android.R.layout.simple_spinner_item);
    clothesShopperList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


    homeShopperList = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Home_shoppers_list, android.R.layout.simple_spinner_item);
    homeShopperList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


    //active package manager for enable notification of permissions
    packageManager = getBaseContext().getPackageManager();

    imageViewFrame = (ImageView) findViewById(R.id.image_item_frame);

    takePhoto = (Button) findViewById(R.id.take_photo);

    choosePhotoFromGallery = (Button) findViewById(R.id.choose_from_gallery);


    takePhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
          startActivityForResult(takePictureIntent, TAKE_PHOTO_RESULT);
        }
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


    /*
    mCategorySpinner.setOnTouchListener(new View.OnTouchListener() {
                                      @Override
                                      public boolean onTouch(View v, MotionEvent event) {
                                       isTouched=true;
                                        return false;
                                      }
                                    });

    mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


      if (isTouched){
      final String selection = (String) parent.getItemAtPosition(position);

      Log.i(LOG_TAG, "Category is :" + selection);

      if (!TextUtils.isEmpty(selection)) {

        if (selection.equals(getString(R.string.tech_List))) {
          mCategoryNumber = 0;
          mShopperSpinner.setAdapter(techShopperList);
          Log.i(LOG_TAG, "we are in Tech");
        }


        if (selection.equals(getString(R.string.clothes_List))) {

          mCategoryNumber = 1;
          mShopperSpinner.setAdapter(clothesShopperList);

          Log.i(LOG_TAG, "we are is Clothes");


        }

        if (selection.equals(getString(R.string.home_List))) {

          mCategoryNumber = 2;
          mShopperSpinner.setAdapter(homeShopperList);

        }
      }
    }}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
  });


    mShopperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        Log.i(LOG_TAG, "item selected is " + selection);
        shopperCheck(selection);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

*/

    uri = getIntent().getData();

    if (uri == null) {
      setTitle("Insert new Item");

    } else {
      setTitle("Update Item");
      isTouched=true;
      getSupportLoaderManager().initLoader(0, null, this);
    }


  }

  private void shopperCheck(String selectedShopper) {
    Log.i(LOG_TAG, "we are in shoppering Check");

    switch (mCategoryNumber) {

      case 0:
        Log.i(LOG_TAG, "we are at case 0 >> Tech");
        checkTechShop(selectedShopper);
        break;
      case 1:
        Log.i(LOG_TAG, "we are at case 1 >> Clothes");

        checkClothesShop(selectedShopper);
        break;
      case 2:
        Log.i(LOG_TAG, "we are at case 2 >> Home");

        checkHomeShop(selectedShopper);
        break;
    }

  }

  private void checkTechShop(String selectedShopper) {
    Log.i(LOG_TAG, "we are at checkTechShop");

    if (selectedShopper.equals(getString(R.string.tech_shop1))) {
      //if selectedShopper is shopper1 then mShopperNumber=1 !


      mShopperNumber = 0;
      Log.i(LOG_TAG, "Shopper Selected " + mShopperNumber);
    }

    if (selectedShopper.equals(getString(R.string.tech_shop2))) {
      //if selectedShopper is shopper2 then mShopperNumber=2 !


      mShopperNumber = 1;
      Log.i(LOG_TAG, "Shopper Selected " + mShopperNumber);

    } else if (selectedShopper.equals(getString(R.string.tech_shop3))) {
      //if selectedShopper is shopper3 then mShopperNumber=3 !

      mShopperNumber = 2;
      Log.i(LOG_TAG, "Shopper Selected" + mShopperNumber);

    } else if (selectedShopper.equals(getString(R.string.tech_shop4))) {
      //if selectedShopper is shopper3 then mShopperNumber=3 !

      mShopperNumber = 3;
      Log.i(LOG_TAG, "Shopper Selected" + mShopperNumber);

    } else if (selectedShopper.equals(getString(R.string.tech_shop5))) {
      //if selectedShopper is shopper3 then mShopperNumber=3 !

      mShopperNumber = 4;
      Log.i(LOG_TAG, "Shopper Selected" + mShopperNumber);

    }

  }

  private void checkClothesShop(String selectedShopper) {
    Log.i(LOG_TAG, "we are at checkClothesShop");

    if (selectedShopper.equals(getString(R.string.clothes_shop1))) {
      //if selectedShopper is shopper1 then mShopperNumber=1 !


      mShopperNumber = 0;
      Log.i(LOG_TAG, "Shopper Selected " + mShopperNumber);
    }

    if (selectedShopper.equals(getString(R.string.clothes_shop2))) {
      //if selectedShopper is shopper2 then mShopperNumber=2 !


      mShopperNumber = 1;
      Log.i(LOG_TAG, "Shopper Selected " + mShopperNumber);

    } else if (selectedShopper.equals(getString(R.string.clothes_shop3))) {
      //if selectedShopper is shopper3 then mShopperNumber=3 !

      mShopperNumber = 2;
      Log.i(LOG_TAG, "Shopper Selected" + mShopperNumber);

    }

  }

  private void checkHomeShop(String selectedShopper) {

    if (selectedShopper.equals(getString(R.string.Home_shop1))) {
      mShopperNumber = 0;
      Log.i(LOG_TAG, "shopper name  is :" + selectedShopper);
    }
    if (selectedShopper.equals(getString(R.string.Home_shop2))) {
      mShopperNumber = 1;
      Log.i(LOG_TAG, "shopper name  is :" + selectedShopper);

    }
    if (selectedShopper.equals(getString(R.string.Home_shop3))) {
      mShopperNumber = 2;
      Log.i(LOG_TAG, "shopper name  is :" + selectedShopper);

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
          Log.i(LOG_TAG, "data is :" + uri.getPath());
//make input stream
          InputStream inputStream = null;
          try {
            //save input steram from uri
            inputStream = getContentResolver().openInputStream(uri);
            Log.i(LOG_TAG, "data is :" + inputStream);
//decode stream and save make bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Log.i(LOG_TAG, "data is :" + bitmap.toString());


            imageViewFrame.setImageBitmap(bitmap);


            //save bitmap to byte array for saving in database
            saveBitmapInByteArray(bitmap);

            break;
          } catch (FileNotFoundException e) {
            e.printStackTrace();
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
    int price = Integer.valueOf(priceBox.getText().toString().trim());
    int quantity = Integer.valueOf(quantityBox.getText().toString().trim());
    if (imageInByte == null) {

      //if imageInByte is null means we dont change image from camera or gallery so we need to save image again to array

      Log.i(LOG_TAG, "we are here" + imageInByte);
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
    //Log.i(LOG_TAG, "shopper is going to save " + mShopperNumber);
    //contentValue.put(ItemsContract.ItemsEntry.COLUMN_SUPPLIER, mShopperNumber);
    contentValue.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, mCategoryNumber);

    contentValue.put(ItemsContract.ItemsEntry.COLUMN_IMAGE, imageInByte);

    //if uri is null means we have new item and must to add into database
    if (uri == null) {
//insert item into database
      Uri uri = getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI, contentValue);

      Log.i(LOG_TAG, "" + uri);
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

  //insert method
 /* private void insert() {
    //get access for writing on database
    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//get string or int data from input views
    String name = nameBox.getText().toString().trim();


    Log.e("name", "is:" + nameBox.getText().toString().trim());
    String discription = discriptionBox.getText().toString().trim();

    Log.e("name", "is:" + discriptionBox.getText().toString().trim());
    Log.e("name", "is:" + discription.isEmpty());

    int price = Integer.parseInt(priceBox.getText().toString().trim());
    Log.e("name", "is:" + priceBox.getText().toString().trim());

    int quantity = Integer.parseInt(quantityBox.getText().toString().trim());
    Log.e("name", "is:" + quantityBox.getText().toString().trim());

    //create new content value for add these variable for content
    ContentValues contentValues = new ContentValues();

    contentValues.put(ItemsContract.ItemsEntry.COLUMN_NAME, name);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION, discription);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_PRICE, price);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, quantity);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_SUPPLIER, mShopperNumber);

    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(String.valueOf(price)) || TextUtils.isEmpty(String.valueOf(quantity))) {

      Toast.makeText(this, "Please Complete all inputs", Toast.LENGTH_SHORT).show();

    } else {

      //do insert method on database
      Long rowUpdated = sqLiteDatabase.insert(ItemsContract.ItemsEntry.TABLE_NAME_ITEMS, null, contentValues);
      Toast.makeText(this, "Updated: " + rowUpdated, Toast.LENGTH_SHORT).show();
    }
  }
*/
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/edit_menu.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.edit_menu, menu);

    return true;

  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.e("item", "is: " + item.getItemId());
    super.onOptionsItemSelected(item);
//if menu item equal to submit button do insert method and finish activity
    if (item.getItemId() == R.id.submit_button) {
      //insert();
      saveItem();
      finish();
      Toast.makeText(this, "You clicked save button", Toast.LENGTH_SHORT).show();
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
     // ItemsContract.ItemsEntry.COLUMN_SUPPLIER,
      ItemsContract.ItemsEntry.COLUMN_CATEGORY,
      ItemsContract.ItemsEntry.COLUMN_IMAGE};

    return new CursorLoader(this, uri, projection, null, null, null);
  }


  //load data from cursor : we use this method for query at first time
  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    // Find the columns of pet attributes that we're interested in

    int idColumnIndex = data.getColumnIndex(ItemsContract.ItemsEntry.ID);
    int nameIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME);
    int discriptionIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION);
    int priceIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE);
    int quantityIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);
  //  int supplierIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_SUPPLIER);
    int categoryIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_CATEGORY);
    Log.i(LOG_TAG, "categoryIndexInCursor" + categoryIndex);

    int imageIndex = data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_IMAGE);
    Log.i(LOG_TAG, "name is :" + data.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_DESCRIPTION));

//means if cursor is not empty and it has at least a row as first row ,cursor can  do  this command ! cursor in default is -1 and we must send thair to 1 to start check data
    try {
      if (data.moveToFirst()) {
        Log.e(LOG_TAG, "" + data.getPosition());
        // Extract out the value from the Cursor for the given column index

        int id = data.getInt(idColumnIndex);
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
        //mShopperNumber = supplier;

       // mCategorySpinner.setAdapter(categorySpinnerAdapter);
        mCategorySpinner.setSelection(mCategoryNumber);

//mShopperSpinner.setAdapter(techShopperList);
//Log.i(LOG_TAG,"LoadData0: "+supplier);
//Log.i(LOG_TAG,"LoadData1: "+mShopperNumber);
     //   mShopperSpinner.setSelection(3);

        // Update the views on the screen with the values from the database

        nameBox.setText(name);
        discriptionBox.setText(discription);
        priceBox.setText(String.valueOf(price));
        quantityBox.setText(String.valueOf(quantity));

        //mCategorySpinner.setSelection(0);
        //mShopperSpinner.setSelection(supplier);


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

/*
private void testList(){
  ArrayAdapter shopperAdapter = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Tech_shoppers_List, android.R.layout.simple_spinner_item);
  shopperAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

  mShopperSpinner.setAdapter(shopperAdapter);

  mShopperSpinner.setSelection(mShopperNumber);

  mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      final String selection = (String) parent.getItemAtPosition(position);

      Log.i(LOG_TAG, "Category is :" + selection);

      if (!TextUtils.isEmpty(selection)) {

        if (selection.equals(getString(R.string.tech_List))) {
          mCategoryNumber = 0;

          ArrayAdapter techShopperList = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Tech_shoppers_List, android.R.layout.simple_spinner_item);
          techShopperList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
          mShopperSpinner.setAdapter(techShopperList);


          Log.i(LOG_TAG, "we are in Tech");

        }


        if (selection.equals(getString(R.string.clothes_List))) {
          /**
           * @param2 array list that we want to use for mShopperSpinner
           * @param3 use a simple list vertical for item without any effect for showing better style
           */


   /*

          ArrayAdapter clothesShopperList = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Clothes_shoppers_list, android.R.layout.simple_spinner_item);
          // Specify dropdown layout style - simple list view with 1 item per line
          clothesShopperList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

          mShopperSpinner.setAdapter(clothesShopperList);
          mCategoryNumber = 1;
          Log.i(LOG_TAG, "we are is Clothes");


        }
        if (selection.equals(getString(R.string.home_List))) {


          ArrayAdapter homeShopperList = ArrayAdapter.createFromResource(EditorActivity.this, R.array.Clothes_shoppers_list, android.R.layout.simple_spinner_item);


          // Specify dropdown layout style - simple list view with 1 item per line
          homeShopperList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

          mShopperSpinner.setAdapter(homeShopperList);

          mCategoryNumber = 2;

        }
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
  });
  mShopperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String selection = (String) parent.getItemAtPosition(position);
      Log.i(LOG_TAG, "item selected is " + selection);
      shopperCheck(selection);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
  });


}






*/
}

