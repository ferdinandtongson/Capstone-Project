package me.makeachoice.gymratpta.model.contract.user;

/**************************************************************************************************/
/*
 *  UserColumns are columns used in user table in the sqlite database
 */
/**************************************************************************************************/

public class UserColumns {

    // Table name
    public static final String TABLE_NAME = "user";

    //user id
    public static final String COLUMN_UID = "uid";

    //user name
    public static final String COLUMN_USER_NAME = "user_name";

    //user email
    public static final String COLUMN_USER_EMAIL = "user_email";

    //user photo
    public static final String COLUMN_USER_PHOTO = "user_photo";

    //user status (free, paid)
    public static final String COLUMN_USER_STATUS = "user_status";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_USER_NAME + " ASC";

}
