package me.makeachoice.gymratpta.model.contract.user;

/**
 * Created by Usuario on 2/1/2017.
 */

public class UserColumns {

    // Table name
    public static final String TABLE_NAME = "user";

    //Firebase key, used as userId
    public static final String COLUMN_FIREBASE_KEY = "firebase_key";

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
