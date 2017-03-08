package me.makeachoice.gymratpta.model.contract.user;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

import static android.content.ContentUris.withAppendedId;

/**************************************************************************************************/
/*
 *  UserContract are columns used in user table in the sqlite database
 */
/**************************************************************************************************/

public class UserContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static final String TABLE_NAME = "user";

    //user id
    public static final String COLUMN_UID = "uid";

    //user name
    public static final String COLUMN_USER_NAME = "user_name";

    //user email
    public static final String COLUMN_USER_EMAIL = "user_email";

    //user status (free, paid)
    public static final String COLUMN_USER_STATUS = "user_status";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_USER_NAME + " ASC";


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
            COLUMN_USER_NAME,
            COLUMN_USER_EMAIL,
            COLUMN_USER_STATUS
    };

    public static final int INDEX_UID = 0;
    public static final int INDEX_USER_NAME = 1;
    public static final int INDEX_USER_EMAIL = 2;
    public static final int INDEX_USER_STATUS = 3;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
    public static String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

    //"content://CONTENT_AUTHORITY/user/[_id]
    public static Uri buildUserUri(long id) {
        return withAppendedId(CONTENT_URI, id);
    }

    //"content://CONTENT_AUTHORITY/user/[uid]
    public static Uri buildUserByUID(String uid) {
        return CONTENT_URI.buildUpon().appendPath(uid).build();
    }

    //"content://CONTENT_AUTHORITY/user/[uid]
    public static String getUIdFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

/**************************************************************************************************/

}
