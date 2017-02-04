package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  UserColumns are columns used in user table in the sqlite database
 */

import android.provider.ContactsContract;

/**************************************************************************************************/

public class ClientColumns {

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

    //client status (active, retired)
    public static final String COLUMN_CLIENT_STATUS = "client_status";

    //client profile pic
    public static final String COLUMN_PROFILE_PIC = "profile_pic";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_CLIENT_NAME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_FKEY,
                    COLUMN_CONTACTS_ID,
                    COLUMN_CLIENT_NAME,
                    COLUMN_CLIENT_STATUS,
                    COLUMN_PROFILE_PIC
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_FKEY = 1;
    public static final int INDEX_CONTACTS_ID = 2;
    public static final int INDEX_CLIENT_NAME = 3;
    public static final int INDEX_CLIENT_STATUS = 4;
    public static final int INDEX_PROFILE_PIC = 5;

}
