package pooria.storeitems.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.net.URL;

/**
 * Created by Po0RiA on 1/1/2018.
 * we make a contract class with constance like schema
 */

public class ItemsContract {


    /*
    to prevent someone that accidentally instantiating the contract class
    give it a empty constructor
     */
    private ItemsContract() {
    }


    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    final static String CONTENT_AUTHORITY = "pooria.storeitems";


    /**
     * use content_authority to create base of all uri which apps will  use for connect to content provider
     */
    final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    /**
     * possible PATH  appends to base content uri for each uri which app use to connect to database
     * <p>
     * for instance content://pooria.storeitems/items is a valid Path for looking for items on database
     * but content://pooria.storeItems/data/Icons is not a valid Path because contnt provider has not been given any information for waht to do with Icons !
     */


    final static String PATH_ITEMS = "items";

    final static String PATH_SELL = "sell_basket";
    final static String PATH_HISTORY_ITEMS = "total_history";

    final static String PATH_CATEGORY_LIST="category_list";

    // inner class that create to define constant of tables in it, each entry in it represents a single item


    public final static class ItemsEntry implements BaseColumns {

        // content uri for access items data in provider
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        // content://pooria.storeitems/items

        // content uri for access sell items data in provider
        public final static Uri CONTENT_URI_SELL_LIST = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SELL);
        // content://pooria.storeitems/sell_basket


        public final static Uri CONTENT_URI_ORDER_HISTORY_LIST = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HISTORY_ITEMS);


        // content Uri For Access Category List Items
        public final static Uri CONTENT_URI_CATEGORY_LIST=Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORY_LIST);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a List of Items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI;
        //       vnd.android.cursor.dir/pooria.storeitems.data/items


        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI;
        //       vnd.android.cursor.item/pooria.storeitems.data/items


        //content list types for sell activity


        public static final String CONTENT_LIST_TYPE_FOR_SELL =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI_SELL_LIST;
        //       vnd.android.cursor.dir/pooria.storeitems.data/sell_basket


        /**
         * The MIME type of the {@link #CONTENT_URI} for a single sell item.
         */
        public static final String CONTENT_ITEM_TYPE_FOR_SELL =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI_SELL_LIST;
        //       vnd.android.cursor.item/pooria.storeitems.data/sell_basket


        //content list types for sell activity


        public static final String CONTENT_LIST_TYPE_FOR_ORDER_HISTORY =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI_ORDER_HISTORY_LIST;
        //       vnd.android.cursor.dir/pooria.storeitems.data/history_list


        /**
         * The MIME type of the {@link #CONTENT_URI} for a single sell item.
         */
        public static final String CONTENT_ITEM_TYPE_FOR_ORDER_HISTORY_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI_ORDER_HISTORY_LIST;
        //       vnd.android.cursor.item/pooria.storeitems.data/history_list


        public static final String CONTENT_LIST_TYPE_FOR_CATEGORY_LIST =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI_CATEGORY_LIST;
        //       vnd.android.cursor.item/pooria.storeitems.data/category_list


        public static final String CONTENT_ITEM_TYPE_FOR_CATEGORY_LIST_ITEM=
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_URI_CATEGORY_LIST;
        //       vnd.android.cursor.item/pooria.storeitems.data/category_list






        /* names of database Table for Items */

        public final static String TABLE_NAME_ITEMS = "items";
        public final static String TABLE_NAME_SELL_LIST = "sellitems";
        public final static String TABLE_NAME_ORDER_HISTORY_LIST = "orderhistory";
        public final static String TABLE_NAME_CATEGORY_LIST = "category";

        /**
         * Unique ID number for item in table (only for use in database table);
         * Type: INTEGER
         */

        public static final String ID = BaseColumns._ID;

        /**
         * Name of Item
         * Type:Text
         */
        public final static String COLUMN_NAME = "name";

        /**
         * Description of Item
         * Type:Text
         */
        public final static String COLUMN_DESCRIPTION = "description";

        /**
         * Price of Item
         * Type:INTEGER
         */
        public final static String COLUMN_PRICE = "price";
        /**
         * Quantity of Item
         * Type : INTEGER
         */

        /**
         * Image of item
         * Type : BLOB
         */
        public final static String COLUMN_IMAGE = "image";
        public final static String COLUMN_QUANTITY = "quantity";

        public static final String COLUMN_DATE = "date";

        /**
         * Supplier of item
         * only Values possible are {@link #shopper1},{@link #shopper2} or {@link #shopper3};
         * Type:INTEGER
         */
        public final static String COLUMN_SUPPLIER = "supplier";

        /*only possible values for supplier are these */
        public static final int shopper1 = 0;

        public static final int shopper2 = 1;

        public static final int shopper3 = 2;


        public static boolean isValidShoper(int shoperNumber) {
            if (shoperNumber == shopper1 || shoperNumber == shopper2 || shoperNumber == shopper3 || shoperNumber == 3 || shoperNumber == 4) {
                return true;
            }
            return false;
        }


        public static final String COLUMN_CATEGORY = "category";

    }


}
