package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  ClientExerciseColumns are columns used in the client exercise table in the sqlite database
 */
/**************************************************************************************************/

public class ClientExerciseColumns {

    // Table name
    public static final String TABLE_NAME = "clientExercise";

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

    //category
    public static final String COLUMN_CATEGORY= "category";

    //exercise
    public static final String COLUMN_EXERCISE = "exercise";

    //order number
    public static final String COLUMN_ORDER_NUMBER = "order_number";

    //set number
    public static final String COLUMN_SET_NUMBER = "set_number";

    //primary type
    public static final String COLUMN_PRIMARY_LABEL = "primary_label";

    //primary value
    public static final String COLUMN_PRIMARY_VALUE = "primary_value";

    //secondary type
    public static final String COLUMN_SECONDARY_LABEL = "secondary_label";

    //secondary value
    public static final String COLUMN_SECONDARY_VALUE = "secondary_value";

    //sort order by date, time, order number and set number
    public static final String DEFAULT_SORT_ORDER = COLUMN_TIMESTAMP + " ASC, " +
            COLUMN_ORDER_NUMBER + " ASC, " + COLUMN_SET_NUMBER + " ASC";

    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_CLIENT_KEY,
                    COLUMN_TIMESTAMP,
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_CATEGORY,
                    COLUMN_EXERCISE,
                    COLUMN_ORDER_NUMBER,
                    COLUMN_SET_NUMBER,
                    COLUMN_PRIMARY_LABEL,
                    COLUMN_PRIMARY_VALUE,
                    COLUMN_SECONDARY_LABEL,
                    COLUMN_SECONDARY_VALUE
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_TIMESTAMP = 2;
    public static final int INDEX_APPOINTMENT_DATE = 3;
    public static final int INDEX_APPOINTMENT_TIME = 4;
    public static final int INDEX_CATEGORY = 5;
    public static final int INDEX_EXERCISE = 6;
    public static final int INDEX_ORDER_NUMBER = 7;
    public static final int INDEX_SET_NUMBER = 8;
    public static final int INDEX_PRIMARY_LABEL = 9;
    public static final int INDEX_PRIMARY_VALUE = 10;
    public static final int INDEX_SECONDARY_LABEL = 11;
    public static final int INDEX_SECONDARY_VALUE= 12;

}
