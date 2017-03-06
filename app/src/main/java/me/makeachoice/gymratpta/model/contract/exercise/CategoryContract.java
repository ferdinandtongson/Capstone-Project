package me.makeachoice.gymratpta.model.contract.exercise;



import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  CategoryContract are columns used in category table in the sqlite database
 */
/**************************************************************************************************/

public class CategoryContract extends MyContractor implements BaseColumns {

    // Table name
    public static String TABLE_NAME = "category";

    //user id
    public static String COLUMN_UID = "uid";

    //firebase key
    public static String COLUMN_FKEY = "fkey";

    //contacts id
    public static String COLUMN_CATEGORY_NAME = "category_name";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_CATEGORY_NAME + " ASC";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Projection and Index
 */
/**************************************************************************************************/

    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_CATEGORY =
            {
                    COLUMN_UID,
                    COLUMN_FKEY,
                    COLUMN_CATEGORY_NAME
            };

    public static int INDEX_UID = 0;
    public static int INDEX_FKEY = 1;
    public static int INDEX_CATEGORY_NAME = 2;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;


    //"content://CONTENT_AUTHORITY/category/[_id]
    public static Uri buildCategoryUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/category/[uid]
    public static Uri buildCategoryByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fKey]
    public static Uri buildCategoryByFirebaseKey(String uid, String fKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_FKEY).appendPath(fKey).build();
    }

    //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
    public static Uri buildCategoryByName(String uid, String name) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CATEGORY_NAME).appendPath(name).build();
    }

    //"content://CONTENT_AUTHORITY/category/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fKey]
    public static String getFirebaseKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
    public static String getCategoryNameFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }


/**************************************************************************************************/


}
