package me.makeachoice.gymratpta.model.contract.client;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  ScheduleContract are columns used in the appointment table in the sqlite database
 */
/**************************************************************************************************/

public class ScheduleContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "user_schedule";

    //user id
    public static final String COLUMN_UID = "uid";

    //timestamp
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //appointment date
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    //appointment time
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //client name
    public static final String COLUMN_CLIENT_NAME = "client_name";

    //routine name
    public static final String COLUMN_ROUTINE_NAME = "routine_name";

    //appointment status
    public static final String COLUMN_APPOINTMENT_STATUS = "appointment_status";

    //sort order by date and time
    public static final String SORT_ORDER_TIMESTAMP = COLUMN_TIMESTAMP + " ASC";

    public static final String SORT_ORDER_DATE_TIME = COLUMN_TIMESTAMP + " ASC, " +  COLUMN_APPOINTMENT_TIME + " ASC";

    //sort order by date, time and client
    public static final String SORT_ORDER_TIME_CLIENT = SORT_ORDER_DATE_TIME + ", " + COLUMN_CLIENT_NAME + " ASC";

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
                    COLUMN_TIMESTAMP,
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_CLIENT_KEY,
                    COLUMN_CLIENT_NAME,
                    COLUMN_ROUTINE_NAME,
                    COLUMN_APPOINTMENT_STATUS
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_TIMESTAMP = 1;
    public static final int INDEX_APPOINTMENT_DATE = 2;
    public static final int INDEX_APPOINTMENT_TIME = 3;
    public static final int INDEX_CLIENT_KEY = 4;
    public static final int INDEX_CLIENT_NAME = 5;
    public static final int INDEX_ROUTINE_NAME = 6;
    public static final int INDEX_APPOINTMENT_STATUS = 7;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Projection and Index
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCHEDULE).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;

    //"content://CONTENT_AUTHORITY/userSchedule/[_id]
    public static Uri buildScheduleUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]
    public static Uri buildScheduleByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[timestamp]
    public static Uri buildScheduleByTimestamp(String uid, String timestamp) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_TIMESTAMP).appendPath(timestamp).build();
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/client_key/[clientKey]
    public static Uri buildScheduleByClientKey(String uid, String clientKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_KEY).appendPath(clientKey).build();
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[startDate]/[endDate]
    public static Uri buildScheduleByRange(String uid, String startDate, String endDate) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_TIMESTAMP).
                appendPath(startDate).appendPath(endDate).build();
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[timestamp]
    public static String getTimestampFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/client_key/[clientKey]
    public static String getClientKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[startDate]/[endDate]
    public static String getStarDateFromRangeUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[startDate]/[endDate]
    public static String getEndDateFromRangeUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }


/**************************************************************************************************/


}
