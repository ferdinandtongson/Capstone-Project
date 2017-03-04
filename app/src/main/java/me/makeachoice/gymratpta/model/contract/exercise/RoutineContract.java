package me.makeachoice.gymratpta.model.contract.exercise;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 * RoutineContract define columns used in routine database table
 */
/**************************************************************************************************/

public class RoutineContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static String TABLE_NAME = "routine";

    //user id
    public static String COLUMN_UID = "uid";

    //routine name
    public static String COLUMN_ROUTINE_NAME = "routine_name";

    //order number
    public static String COLUMN_ORDER_NUMBER = "order_number";

    //exercise
    public static String COLUMN_EXERCISE = "exercise";

    //category
    public static String COLUMN_CATEGORY = "category";

    //number of sets
    public static String COLUMN_NUM_SETS = "numOfSets";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_ROUTINE_NAME + " ASC";

    public static String SORT_BY_ORDER_NUMBER = COLUMN_ORDER_NUMBER + " ASC";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Projection and Index
 */
/**************************************************************************************************/

    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_ROUTINE =
            {
                    COLUMN_UID,
                    COLUMN_ROUTINE_NAME,
                    COLUMN_ORDER_NUMBER,
                    COLUMN_EXERCISE,
                    COLUMN_CATEGORY,
                    COLUMN_NUM_SETS
            };

    public static int INDEX_UID = 0;
    public static int INDEX_ROUTINE_NAME = 1;
    public static int INDEX_ORDER_NUMBER = 2;
    public static int INDEX_EXERCISE = 3;
    public static int INDEX_CATEGORY = 4;
    public static int INDEX_NUM_SETS = 5;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTINE).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE;


    //"content://CONTENT_AUTHORITY/routine/[_id]
    public static Uri buildRoutineUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]
    public static Uri buildRoutineByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]
    public static Uri buildRoutineByName(String uid, String routineName) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(routineName).build();
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]/[orderNumber]
    public static Uri buildRoutineByOrderNumber(String uid, String routineName, String orderNumber) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(routineName).appendPath(orderNumber).build();
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]
    public static String getRoutineNameFromUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]/[orderNumber]
    public static String getOrderNumberFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]/[exerciseName]
    public static String getCategoryKeyFromExerciseNameUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

/**************************************************************************************************/

}
