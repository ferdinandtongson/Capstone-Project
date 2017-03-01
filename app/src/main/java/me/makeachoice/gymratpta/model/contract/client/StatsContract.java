package me.makeachoice.gymratpta.model.contract.client;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  StatsContract are columns used in stats table in the sqlite database
 */
/**************************************************************************************************/

public class StatsContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "client_stats";

    //user id
    public static final String COLUMN_UID = "uid";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //timestamp
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //appointment date
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    //appointment time
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    //modified date
    public static final String COLUMN_MODIFIED_DATE = "modified_date";

    //stat weight
    public static final String COLUMN_STAT_WEIGHT = "stat_weight";

    //stat body fat
    public static final String COLUMN_STAT_BODY_FAT = "stat_body_fat";

    //stat bmi
    public static final String COLUMN_STAT_BMI = "stat_bmi";

    //stat neck
    public static final String COLUMN_STAT_NECK = "stat_neck";

    //stat chest
    public static final String COLUMN_STAT_CHEST = "stat_chest";

    //stat rbicep
    public static final String COLUMN_STAT_RBICEP = "stat_rbicep";

    //stat lbicep
    public static final String COLUMN_STAT_LBICEP = "stat_lbicep";

    //stat waist
    public static final String COLUMN_STAT_WAIST = "stat_waist";

    //stat naval
    public static final String COLUMN_STAT_NAVEL = "stat_navel";

    //stat hips
    public static final String COLUMN_STAT_HIPS = "stat_hips";

    //stat rthigh
    public static final String COLUMN_STAT_RTHIGH = "stat_rthigh";

    //stat lthigh
    public static final String COLUMN_STAT_LTHIGH = "stat_lthigh";

    //stat rcalf
    public static final String COLUMN_STAT_RCALF = "stat_rcalf";

    //stat lcalf
    public static final String COLUMN_STAT_LCALF = "stat_lcalf";

    //sort by date and time
    public static final String SORT_ORDER_DATE_TIME = COLUMN_TIMESTAMP + " DESC";

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
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_MODIFIED_DATE,
                    COLUMN_STAT_WEIGHT,
                    COLUMN_STAT_BODY_FAT,
                    COLUMN_STAT_BMI,
                    COLUMN_STAT_NECK,
                    COLUMN_STAT_CHEST,
                    COLUMN_STAT_RBICEP,
                    COLUMN_STAT_LBICEP,
                    COLUMN_STAT_WAIST,
                    COLUMN_STAT_NAVEL,
                    COLUMN_STAT_HIPS,
                    COLUMN_STAT_RTHIGH,
                    COLUMN_STAT_LTHIGH,
                    COLUMN_STAT_RCALF,
                    COLUMN_STAT_LCALF
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_TIMESTAMP = 2;
    public static final int INDEX_APPOINTMENT_DATE = 3;
    public static final int INDEX_APPOINTMENT_TIME = 4;
    public static final int INDEX_MODIFIED_DATE = 5;
    public static final int INDEX_STAT_WEIGHT = 6;
    public static final int INDEX_STAT_BODY_FAT = 7;
    public static final int INDEX_STAT_BMI = 8;
    public static final int INDEX_STAT_NECK = 9;
    public static final int INDEX_STAT_CHEST = 10;
    public static final int INDEX_STAT_RBICEP = 11;
    public static final int INDEX_STAT_LBICEP = 12;
    public static final int INDEX_STAT_WAIST = 13;
    public static final int INDEX_STAT_NAVEL = 14;
    public static final int INDEX_STAT_HIPS = 15;
    public static final int INDEX_STAT_RTHIGH = 16;
    public static final int INDEX_STAT_LTHIGH = 17;
    public static final int INDEX_STAT_RCALF = 18;
    public static final int INDEX_STAT_LCALF = 19;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STATS).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STATS;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STATS;

    //"content://CONTENT_AUTHORITY/clientStats/[_id]
    public static Uri buildStatsUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]
    public static Uri buildStatsByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/client_key/[clientKey]
    public static Uri buildStatsByClientKey(String uid, String clientKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_KEY).appendPath(clientKey).build();
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/timestamp/[timestamp]
    public static Uri buildStatsByTimestamp(String uid, String clientKey, String timestamp) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).
                appendPath(COLUMN_TIMESTAMP).appendPath(timestamp).build();
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static Uri buildStatsByDateTime(String uid, String clientKey, String appointmentDate, String appointmentTime) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).appendPath(appointmentDate).
                appendPath(COLUMN_APPOINTMENT_TIME).appendPath(appointmentTime).build();
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/client_key/[clientKey]
    public static String getClientKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/timestamp/[timestamp]
    public static String getClientKeyFromTimestampUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/timestamp/[timestamp]
    public static String getTimestampFromTimestampUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getClientKeyFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getDateFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getTimeFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(5);
    }

/**************************************************************************************************/

}
