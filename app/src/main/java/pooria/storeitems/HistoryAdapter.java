package pooria.storeitems;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import pooria.storeitems.DateUtil.DateConverter;
import pooria.storeitems.data.ItemsContract.ItemsEntry;

public class HistoryAdapter extends CursorAdapter {
  public HistoryAdapter(Context context, Cursor c) {
    super(context, c, 0);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    TextView nameView = (TextView) view.findViewById(R.id.title_sell);
    TextView priceView = (TextView) view.findViewById(R.id.price_sell);
    TextView quantityView = (TextView) view.findViewById(R.id.quantity_sell);
    TextView dateView = (TextView) view.findViewById(R.id.time_and_date);
    ImageView imageView = (ImageView) view.findViewById(R.id.image_frame_for_history);


    int nameIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_NAME);
    int priceIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_PRICE);
    int quantityIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_QUANTITY);
    int imageIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_IMAGE);
    int dateIndex = cursor.getColumnIndex(ItemsEntry.COLUMN_DATE);

    String nameValue = cursor.getString(nameIndex);
    String priceValue = cursor.getString(priceIndex);
    String quantityValue = cursor.getString(quantityIndex);
    byte[] imageValue = cursor.getBlob(imageIndex);


    Bitmap bitmap=getBitmapFromByte(imageValue);

    Long dateValue = cursor.getLong(dateIndex);
    String time = DateConverter.getShamsiDateFromLocalTimestamp(dateValue);


    nameView.setText(nameValue);
    priceView.setText(priceValue);
    quantityView.setText(quantityValue);
    imageView.setImageBitmap(bitmap);
    dateView.setText(time);


  }




private Bitmap getBitmapFromByte(byte[] imageInByte){

  ByteArrayInputStream stream = new ByteArrayInputStream(imageInByte);
  Bitmap bitmap = BitmapFactory.decodeStream(stream);
    return bitmap;
}

}
