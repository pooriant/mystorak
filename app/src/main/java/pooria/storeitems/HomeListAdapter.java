package pooria.storeitems.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

import pooria.storeitems.R;

/**
 * Created by Po0RiA on 1/5/2018.
 */


public class HomeListAdapter extends CursorAdapter {
  private ItemsDbHelper dbHelper;
  private String mNameValue;
  private int mCategoryValue;
  private int mPriceValue;
  private byte imageInByte[];
  private int mQuantityValue;

  private final String LOG_TAG = HomeListAdapter.class.getName();

  public HomeListAdapter(Context context, Cursor c) {
    super(context, c, 0/*flag*/);
    dbHelper = new ItemsDbHelper(context);

  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    //make new view for items until either display be full of data or data for new view has been finished
    Log.i(LOG_TAG, "in Adapter");

    return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

  }


  //full view by value in spcefic row
  @Override
  public void bindView(View view, final Context context, final Cursor cursor) {

    final int mCursorPosition = cursor.getPosition();


    //initialize Views for title and description;
    View sellItemView = view.findViewById(R.id.sell_one_item);
    TextView titleView = (TextView) view.findViewById(R.id.title_item);
    TextView sellerView = (TextView) view.findViewById(R.id.seller_name);
    ImageView imageView = (ImageView) view.findViewById(R.id.image_frame_for_item);
    TextView priceView = (TextView) view.findViewById(R.id.price_item);
    TextView quantityView = (TextView) view.findViewById(R.id.quantity_item);


    //get number of spesfic column
    int nameId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME);
    //int sellerName = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_SUPPLIER);
    int categoryName = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_CATEGORY);


    int imageId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_IMAGE);

    int priceId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE);
    final int quantityId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);


//    //get image id
    imageInByte = cursor.getBlob(imageId);
//    //get byteInputStream of imageByteArray
    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageInByte);
//    //decode stream from to ake bitmap
    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//    //set image as bitmap
    imageView.setImageBitmap(bitmap);


    //get values of each row
    mNameValue = cursor.getString(nameId);

    mCategoryValue = cursor.getInt(categoryName);

    if (mCategoryValue == 0) {

      sellerView.setText(R.string.tech_List);
    }
    if (mCategoryValue == 1) {

      sellerView.setText(R.string.clothes_List);
    }
    if (mCategoryValue == 2) {
      sellerView.setText(R.string.home_List);
    }

    Log.i("name", "is" + mNameValue);
    titleView.setText(mNameValue);

    mPriceValue = cursor.getInt(priceId);
    String Price = "$" + String.valueOf(mPriceValue);
    priceView.setText(Price);

    mQuantityValue = cursor.getInt(quantityId);

    quantityView.setText(String.valueOf(mQuantityValue));


    sellItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //check item on list and prepare for update or add on both list
        checkItem(context, cursor, mCursorPosition);


      }
    });

  }


  private void checkItem(Context context, Cursor Cursor_OnMAinList, int tag) {

    try {

      Cursor_OnMAinList.moveToPosition(tag);
      //item details on Main list is like below
      mNameValue = Cursor_OnMAinList.getString(Cursor_OnMAinList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME));
      mCategoryValue = Cursor_OnMAinList.getInt(Cursor_OnMAinList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_CATEGORY));
      imageInByte = Cursor_OnMAinList.getBlob(Cursor_OnMAinList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_IMAGE));
      mPriceValue = Cursor_OnMAinList.getInt(Cursor_OnMAinList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE));
      mQuantityValue = Cursor_OnMAinList.getInt(Cursor_OnMAinList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY));


      if (mQuantityValue > 0) {

        //check item for update or add to Sell List and Main List
        addOrUpdateItem_MainList_SellList(context, Cursor_OnMAinList);

      } else {
// quantity of item is 0 so we cant add to sell
        Toast.makeText(context, "you cant do this", Toast.LENGTH_SHORT).show();

      }

    } catch (IllegalArgumentException exception) {
      Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }


  private void addOrUpdateItem_MainList_SellList(Context context, Cursor Cursor_OnMAinList) {

    //result of search on Sell list, if we have this item on list result is 1 or above then is -1 because we dont have this item on list and should to add on our list
    Cursor Cursor_SearchReslut_SellList = searchItem_SellList(context, mNameValue);

    //if result count is more then 0 means we have this item and must to update that
    if (Cursor_SearchReslut_SellList.getCount() > 0) {

      //update that item on SellList too;
      update_SellList(Cursor_SearchReslut_SellList, context);

      //update that item on Main list with new quantity (LastQuantity-1)
      update_MainList(Cursor_OnMAinList, context);


      //we dont have this item then add to list
    } else {
      //add item to Sell list
      add_SellList(context);

      //update item on main list
      update_MainList(Cursor_OnMAinList, context);

    }
  }

  private void update_MainList(Cursor cursor_OnMainList, Context context) {

    //get Id and Quantity of item for Update with new Quantity (Quantity-1)
    int itemId = cursor_OnMainList.getColumnIndex(ItemsContract.ItemsEntry._ID);

    int quantityValueId = cursor_OnMainList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);

    int ID = cursor_OnMainList.getInt(itemId);

    int quantityValue = cursor_OnMainList.getInt(quantityValueId);


    quantityValue--;

//make Item Uri for update
    Uri uri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI, ID);


    //make content value with new QuantityValue for Update
    ContentValues contentValues = new ContentValues();

    contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, quantityValue);

//Update Item On Main List
    context.getContentResolver().update(uri, contentValues, null, null);


  }


  private void update_SellList(Cursor Cursor_SearchResult_SellList, Context context) {

    //move Cursor to first position
    Cursor_SearchResult_SellList.moveToFirst();

    //get Id and Quantity of Item On Sell List For Update With new Quantity(Quantity+1)
    int itemId_OnSellList_ColumnIndex = Cursor_SearchResult_SellList.getColumnIndex(ItemsContract.ItemsEntry.ID);

    int itemId_OnSellList = Cursor_SearchResult_SellList.getInt(itemId_OnSellList_ColumnIndex);

    int ItemQuantity_OnSellList_ColumnIndex = Cursor_SearchResult_SellList.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);

    int ItemQuantity_OnSellList = Cursor_SearchResult_SellList.getInt(ItemQuantity_OnSellList_ColumnIndex);


    //add quantity +1
    ItemQuantity_OnSellList++;
    //make ContentValue with new Quantity Value
    ContentValues contentValues = new ContentValues();
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, ItemQuantity_OnSellList);
    //make uri For Update On Sell List
    Uri itemUri = ContentUris.withAppendedId(ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, itemId_OnSellList);
//update Item On Sell List
      context.getContentResolver().update(itemUri, contentValues, null, null);


  }

  private void add_SellList(Context context) {


    //make contentValue For Add Item On SEll List
    ContentValues contentValues = new ContentValues();


    contentValues.put(ItemsContract.ItemsEntry.COLUMN_NAME, mNameValue);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_CATEGORY, mCategoryValue);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_QUANTITY, 1);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_IMAGE, imageInByte);
    contentValues.put(ItemsContract.ItemsEntry.COLUMN_PRICE, mPriceValue);
    //Add Item To Sell List

    context.getContentResolver().insert(ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, contentValues);


  }

  private Cursor searchItem_SellList(Context context, String itemForSearch) {

    //search ITem On Sell List Where NAME is = (itemFor search)>>that item's name we want to search
    String selection = ItemsContract.ItemsEntry.COLUMN_NAME + "=?";

    String[] selectionArgs = new String[]{itemForSearch};

    //make a query on Sell List for finding item with name above
    Cursor resultOfSearch = context.getContentResolver().query(ItemsContract.ItemsEntry.CONTENT_URI_SELL_LIST, null, selection, selectionArgs, null);


    return resultOfSearch;
  }


}


