package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  UserColumns are columns used in user table in the sqlite database
 */
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
    public static final String SORT_ORDER_DEFAULT = COLUMN_CLIENT_NAME + " ASC";


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

}
