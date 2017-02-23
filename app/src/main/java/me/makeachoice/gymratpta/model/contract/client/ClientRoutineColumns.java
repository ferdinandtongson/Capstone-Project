package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  ClientRoutineColumns are columns used in the client routine table in the sqlite database
 */
/**************************************************************************************************/

public class ClientRoutineColumns {

    // Table name
    public static final String TABLE_NAME = "clientRoutine";

    //user id
    public static final String COLUMN_UID = "uid";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

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

    //number of sets
    public static final String COLUMN_NUM_OF_SETS = "num_of_sets";

    //sort order by date, time, and order number
    public static final String DEFAULT_SORT_ORDER = COLUMN_APPOINTMENT_DATE + " ASC, " +
            COLUMN_APPOINTMENT_TIME + " ASC, " + COLUMN_ORDER_NUMBER + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_CLIENT_KEY,
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_CATEGORY,
                    COLUMN_EXERCISE,
                    COLUMN_ORDER_NUMBER,
                    COLUMN_NUM_OF_SETS
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_APPOINTMENT_DATE = 2;
    public static final int INDEX_APPOINTMENT_TIME = 3;
    public static final int INDEX_CATEGORY = 4;
    public static final int INDEX_EXERCISE = 5;
    public static final int INDEX_ORDER_NUMBER = 6;
    public static final int INDEX_NUM_OF_SETS = 7;


}
