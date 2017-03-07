package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  ClientContract are columns used in client table in the sqlite database
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/

public class ClientContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "client";

    //user id
    public static final String COLUMN_UID = "uid";

    //firebase key
    public static final String COLUMN_FKEY = "fkey";

    //contacts id
    public static final String COLUMN_CONTACTS_ID = "contacts_id";

    //client name
    public static final String COLUMN_CLIENT_NAME = "client_name";

    //client email
    public static final String COLUMN_CLIENT_EMAIL = "client_email";

    //client phone
    public static final String COLUMN_CLIENT_PHONE = "client_phone";

    //date of first client session
    public static final String COLUMN_FIRST_SESSION = "first_session";

    //client goals
    public static final String COLUMN_CLIENT_GOALS = "client_goals";

    //client status (active, retired)
    public static final String COLUMN_CLIENT_STATUS = "client_status";

    //client profile pic
    public static final String COLUMN_PROFILE_PIC = "profile_pic";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_CLIENT_NAME + " COLLATE NOCASE ASC";

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
                    COLUMN_FKEY,
                    COLUMN_CONTACTS_ID,
                    COLUMN_CLIENT_NAME,
                    COLUMN_CLIENT_EMAIL,
                    COLUMN_CLIENT_PHONE,
                    COLUMN_FIRST_SESSION,
                    COLUMN_CLIENT_GOALS,
                    COLUMN_CLIENT_STATUS,
                    COLUMN_PROFILE_PIC,

            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_FKEY = 1;
    public static final int INDEX_CONTACTS_ID = 2;
    public static final int INDEX_CLIENT_NAME = 3;
    public static final int INDEX_CLIENT_EMAIL = 4;
    public static final int INDEX_CLIENT_PHONE = 5;
    public static final int INDEX_FIRST_SESSION = 6;
    public static final int INDEX_CLIENT_GOALS = 7;
    public static final int INDEX_CLIENT_STATUS = 8;
    public static final int INDEX_PROFILE_PIC = 9;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLIENT).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT;

    //"content://CONTENT_AUTHORITY/client/[_id]
    public static Uri buildClientUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    //"content://CONTENT_AUTHORITY/client/[uid]
    public static Uri buildClientByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/client/[uid]/fkey/[fKey]
    public static Uri buildClientByFKey(String uid, String fKey) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_FKEY).appendPath(fKey).build();
    }

    //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
    public static Uri buildClientByStatus(String uid, String status) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_STATUS).appendPath(status).build();
    }

    //"content://CONTENT_AUTHORITY/client/[uid]/....
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    //"content://CONTENT_AUTHORITY/client/[uid]/fkey/[fKey]
    public static String getFKeyFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

    //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
    public static String getStatusFromUri(Uri uri) {
        return uri.getPathSegments().get(3);
    }

/**************************************************************************************************/

}
