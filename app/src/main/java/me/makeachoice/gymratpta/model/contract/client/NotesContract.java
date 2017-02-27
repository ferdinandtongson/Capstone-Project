package me.makeachoice.gymratpta.model.contract.client;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  NotesContract are columns used in notes table in the sqlite database
 */
/**************************************************************************************************/

public class NotesContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "client_notes";

    //user id
    public static final String COLUMN_UID = "uid";

    //timestamp
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //appointment date
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    //appointment time
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    //modified date
    public static final String COLUMN_MODIFIED_DATE = "modified_date";

    //subjective notes
    public static final String COLUMN_SUBJECTIVE_NOTES = "subjective_notes";

    //objective notes
    public static final String COLUMN_OBJECTIVE_NOTES = "objective_notes";

    //assessment notes
    public static final String COLUMN_ASSESSMENT_NOTES = "assessment_notes";

    //plan notes
    public static final String COLUMN_PLAN_NOTES = "plan_notes";

    //default sort order
    public static final String SORT_ORDER_DATE_TIME = COLUMN_TIMESTAMP + " ASC, " + COLUMN_APPOINTMENT_TIME + " ASC";

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
                    COLUMN_SUBJECTIVE_NOTES,
                    COLUMN_OBJECTIVE_NOTES,
                    COLUMN_ASSESSMENT_NOTES,
                    COLUMN_PLAN_NOTES
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_TIMESTAMP = 2;
    public static final int INDEX_APPOINTMENT_DATE = 3;
    public static final int INDEX_APPOINTMENT_TIME = 4;
    public static final int INDEX_MODIFIED_DATE = 5;
    public static final int INDEX_SUBJECTIVE_NOTES = 6;
    public static final int INDEX_OBJECTIVE_NOTES = 7;
    public static final int INDEX_ASSESSMENT_NOTES = 8;
    public static final int INDEX_PLAN_NOTES = 9;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

    //"content://CONTENT_AUTHORITY/clientNotes/[_id]
    public static Uri buildNotesUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]
    public static Uri buildNotesByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/client_key/[clientKey]
    public static Uri buildNotesByClientKey(String uid, String clientKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_KEY).appendPath(clientKey).build();
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/appointment_date/[appointmentDate]
    public static Uri buildNotesByDate(String uid, String clientKey, String appointmentDate) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).
                appendPath(uid).appendPath(COLUMN_APPOINTMENT_DATE).appendPath(appointmentDate).build();
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static Uri buildNotesByDateTime(String uid, String clientKey, String appointmentDate, String appointmentTime) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(clientKey).appendPath(appointmentDate).
                appendPath(COLUMN_APPOINTMENT_TIME).appendPath(appointmentTime).build();
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/client_key/[clientKey]
    public static String getClientKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/appointment_date/[appointmentDate]
    public static String getClientKeyFromDateUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/appointment_date/[appointmentDate]
    public static String getDateFromDateUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getClientKeyFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(2);
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getDateFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
    public static String getTimeFromDateTimeUri(Uri uri) {
        return uri.getPathSegments().get(5);
    }

/**************************************************************************************************/
}
