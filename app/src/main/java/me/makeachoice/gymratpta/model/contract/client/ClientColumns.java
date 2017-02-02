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

    //client status (active, retired)
    public static final String COLUMN_CLIENT_STATUS = "client_status";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_CLIENT_NAME + " ASC";

}
