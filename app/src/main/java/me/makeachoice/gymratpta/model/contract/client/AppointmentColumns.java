package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  AppointmentColumns are columns used in the appointment table in the sqlite database
 */
/**************************************************************************************************/

public class AppointmentColumns {

    // Table name
    public static final String TABLE_NAME = "appointment";

    //user id
    public static final String COLUMN_UID = "uid";

    //firebase key
    public static final String COLUMN_FKEY = "fkey";

    //appointment date
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    //appointment time
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //client name
    public static final String COLUMN_CLIENT_NAME = "client_name";

    //appointment status
    public static final String COLUMN_APPOINTMENT_STATUS = "appointment_status";

    //sort order by date and time
    public static final String SORT_ORDER_DATE_TIME = COLUMN_APPOINTMENT_DATE + " ASC, " + COLUMN_APPOINTMENT_TIME + " ASC";

    //sort order by date, time and client
    public static final String SORT_ORDER_TIME_CLIENT = SORT_ORDER_DATE_TIME + ", " + COLUMN_CLIENT_NAME;


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_FKEY,
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_CLIENT_KEY,
                    COLUMN_CLIENT_NAME,
                    COLUMN_APPOINTMENT_STATUS
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_FKEY = 1;
    public static final int INDEX_APPOINTMENT_DATE = 2;
    public static final int INDEX_APPOINTMENT_TIME = 3;
    public static final int INDEX_CLIENT_KEY = 4;
    public static final int INDEX_CLIENT_NAME = 5;
    public static final int INDEX_APPOINTMENT_STATUS = 6;

}
