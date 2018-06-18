package pooria.storeitems;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;

import pooria.storeitems.data.ItemsContract;
import pooria.storeitems.data.ItemsDbHelper;

/**
 * Created by Po0RiA on 2/11/2018.
 */

class ShoppingListAdapter extends CursorAdapter {


  public ShoppingListAdapter(Context context, Cursor c) {
    super(context, c, 0);

  }


  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {

Log.i("make","new view");
    return LayoutInflater.from(context).inflate(R.layout.shoping_item, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    Log.i("cursor","count" +cursor.getCount());

    TextView nameView = (TextView) view.findViewById(R.id.title_sell);
    TextView priceView = (TextView) view.findViewById(R.id.price_sell);
    TextView quantityView = (TextView) view.findViewById(R.id.quantity_sell);
    ImageView imageView = (ImageView) view.findViewById(R.id.image_frame_for_sell_page);

    int nameId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_NAME);
    int priceId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_PRICE);
    int quantityId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_QUANTITY);
    int imageId = cursor.getColumnIndex(ItemsContract.ItemsEntry.COLUMN_IMAGE);

    String name = cursor.getString(nameId);
    int price = cursor.getInt(priceId);
    int quantity = cursor.getInt(quantityId);
if (cursor.getBlob(imageId)!=null){
   byte imageinByte[] = cursor.getBlob(imageId);

      if(imageinByte.length>0) {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageinByte);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        imageView.setImageBitmap(bitmap);

      }}
    nameView.setText(name);
    priceView.setText(String.valueOf(price));
    quantityView.setText(String.valueOf(quantity));


  }
}
