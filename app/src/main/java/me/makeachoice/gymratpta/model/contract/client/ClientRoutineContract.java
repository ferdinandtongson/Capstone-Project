package me.makeachoice.gymratpta.model.contract.client;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  ClientRoutineContract are columns used in the client routine table in the sqlite database
 */
/**************************************************************************************************/

public class ClientRoutineContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "clientRoutine";

    //user id
    public static final String COLUMN_UID = "uid";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //appointment date
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //category
    public static final String COLUMN_CATEGORY= "category";

    //exercise
    public static final String COLUMN_EXERCISE = "exercise";

    //order number
    public static final String COLUMN_ORDER_NUMBER = "order_number";

    //number of sets
    public static final String COLUMN_NUM_OF_SETS = "num_of_sets";

    //sort order by date, time, and order number
    public static final String DEFAULT_SORT_ORDER = COLUMN_TIMESTAMP + " ASC, " +
            COLUMN_ORDER_NUMBER + " ASC, " + COLUMN_NUM_OF_SETS + " ASC";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Projection and Index
 */
/**************************************************************************************************/

    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_CLIENT_KEY,
                    COLUMN_TIMESTAMP,
                    COLUMN_CATEGORY,
                    COLUMN_EXERCISE,
                    COLUMN_ORDER_NUMBER,
                    COLUMN_NUM_OF_SETS
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_TIMESTAMP = 2;
    public static final int INDEX_CATEGORY = 3;
    public static final int INDEX_EXERCISE = 4;
    public static final int INDEX_ORDER_NUMBER = 5;
    public static final int INDEX_NUM_OF_SETS = 6;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLIENT_ROUTINE).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT_ROUTINE;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT_ROUTINE;

    //"content://CONTENT_AUTHORITY/clientRoutine/[_id]
    public static Uri buildClientRoutineUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]
    public static Uri buildClientRoutineByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/client_key/[clientKey]
    public static Uri buildClientRoutineByClientKey(String uid, String clientKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_KEY).appendPath(clientKey).build();
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/timestamp/[timestamp]
    public static Uri buildClientRoutineByTimestamp(String uid, String clientKey, String timestamp) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).
                appendPath(COLUMN_TIMESTAMP).appendPath(timestamp).build();
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static Uri buildClientRoutineByExercise(String uid, String clientKey, String timestamp, String exercise) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).appendPath(timestamp).
                appendPath(COLUMN_EXERCISE).appendPath(exercise).build();
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/order_number/[orderNumber]
    public static Uri buildClientRoutineByOrderNumber(String uid, String clientKey, String timestamp, String orderNumber) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).appendPath(timestamp).
                appendPath(COLUMN_ORDER_NUMBER).appendPath(orderNumber).build();
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/client_key/[clientKey]
    public static String getClientKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getClientKeyFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/timestamp/[timestamp]
    public static String getClientKeyFromTimestampUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/timestamp/[timestamp]
    public static String getTimestampFromTimestampUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static String getClientKeyFromExerciseUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static String getTimestampFromExerciseUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static String getExerciseFromExerciseUri(Uri uri) {
        return uri.getPathSegments().get(5);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/order_number/[orderNumber]
    public static String getClientKeyFromOrderNumberUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/order_number/[orderNumber]
    public static String getTimestampFromOrderNumberUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/order_number/[orderNumber]
    public static String getOrderNumberFromOrderNumberUri(Uri uri) {
        return uri.getPathSegments().get(5);
    }


/**************************************************************************************************/

}
