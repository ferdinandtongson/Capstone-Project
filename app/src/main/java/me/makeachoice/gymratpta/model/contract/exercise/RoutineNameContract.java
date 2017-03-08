package me.makeachoice.gymratpta.model.contract.exercise;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 * RoutineNameContract are columns used in exercise table in the sqlite database
 */
/**************************************************************************************************/


public class RoutineNameContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static String TABLE_NAME = "routineName";

    //user id
    public static String COLUMN_UID = "uid";

    //routine name
    public static String COLUMN_ROUTINE_NAME = "routine_name";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_ROUTINE_NAME + " ASC";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Projection and Index
 */
/**************************************************************************************************/


    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_ROUTINE_NAME =
            {
                    COLUMN_UID,
                    COLUMN_ROUTINE_NAME
            };

    public static int INDEX_UID = 0;
    public static int INDEX_ROUTINE_NAME = 1;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTINE_NAME).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE_NAME;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE_NAME;


    //"content://CONTENT_AUTHORITY/routineName/[_id]
    public static Uri buildRoutineNameUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/routineName/[uid]
    public static Uri buildRoutineNameByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/routineName/[uid]/routine_name/[name]
    public static Uri buildRoutineNameByName(String uid, String name) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_ROUTINE_NAME).appendPath(name).build();
    }

    //"content://CONTENT_AUTHORITY/routineName/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/routineName/[uid]/routine_name/[name]
    public static String getRoutineNameFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

/**************************************************************************************************/

}
