package me.makeachoice.gymratpta.model.contract.client;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  ClientExerciseContract are columns used in the client exercise table in the sqlite database
 */
/**************************************************************************************************/

public class ClientExerciseContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "clientExercise";

    //user id
    public static final String COLUMN_UID = "uid";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //timestamp
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //category
    public static final String COLUMN_CATEGORY= "category";

    //exercise
    public static final String COLUMN_EXERCISE = "exercise";

    //order number
    public static final String COLUMN_ORDER_NUMBER = "order_number";

    //set number
    public static final String COLUMN_SET_NUMBER = "set_number";

    //primary type
    public static final String COLUMN_PRIMARY_LABEL = "primary_label";

    //primary value
    public static final String COLUMN_PRIMARY_VALUE = "primary_value";

    //secondary type
    public static final String COLUMN_SECONDARY_LABEL = "secondary_label";

    //secondary value
    public static final String COLUMN_SECONDARY_VALUE = "secondary_value";

    //sort order by date, time, order number and set number
    public static final String DEFAULT_SORT_ORDER = COLUMN_TIMESTAMP + " DESC, " +
            COLUMN_ORDER_NUMBER + " ASC, " + COLUMN_SET_NUMBER + " ASC";

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
                    COLUMN_SET_NUMBER,
                    COLUMN_PRIMARY_LABEL,
                    COLUMN_PRIMARY_VALUE,
                    COLUMN_SECONDARY_LABEL,
                    COLUMN_SECONDARY_VALUE
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_TIMESTAMP = 2;
    public static final int INDEX_CATEGORY = 3;
    public static final int INDEX_EXERCISE = 4;
    public static final int INDEX_ORDER_NUMBER = 5;
    public static final int INDEX_SET_NUMBER = 6;
    public static final int INDEX_PRIMARY_LABEL = 7;
    public static final int INDEX_PRIMARY_VALUE = 8;
    public static final int INDEX_SECONDARY_LABEL = 9;
    public static final int INDEX_SECONDARY_VALUE= 10;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLIENT_EXERCISE).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT_EXERCISE;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT_EXERCISE;

    //"content://CONTENT_AUTHORITY/clientExercise/[_id]
    public static Uri buildClientExerciseUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]
    public static Uri buildClientExerciseByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/client_key/[clientKey]
    public static Uri buildClientExerciseByClientKey(String uid, String clientKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_KEY).appendPath(clientKey).build();
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/exercise/[exercise]
    public static Uri buildClientExerciseByExercise(String uid, String clientKey, String exercise) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).
                appendPath(COLUMN_EXERCISE).appendPath(exercise).build();
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/timestamp/[timestamp]
    public static Uri buildClientExerciseByTimestamp(String uid, String clientKey, String timestamp) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).
                appendPath(COLUMN_TIMESTAMP).appendPath(timestamp).build();
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static Uri buildClientExerciseByTimestampExercise(String uid, String clientKey,
                                                             String timestamp, String exercise) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).appendPath(timestamp).
                appendPath(COLUMN_EXERCISE).appendPath(exercise).build();
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/client_key/[clientKey]
    public static String getClientKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/exercise/[exercise]
    public static String getClientKeyFromExerciseUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/exercise/[exercise]
    public static String getExerciseFromExerciseUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/timestamp/[timestamp]
    public static String getClientKeyFromTimestampUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/timestamp/[timestamp]
    public static String getTimestampFromTimestampUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static String getClientKeyFromTimestampExerciseUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static String getTimestampFromTimestampExerciseUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
    public static String getExerciseFromTimestampExerciseUri(Uri uri) {
        return uri.getPathSegments().get(5);
    }


/**************************************************************************************************/

}
