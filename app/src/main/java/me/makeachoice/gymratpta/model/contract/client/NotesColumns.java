package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  NotesColumns are columns used in notes table in the sqlite database
 */
/**************************************************************************************************/

public class NotesColumns {

    // Table name
    public static final String TABLE_NAME = "client_notes";

    //user id
    public static final String COLUMN_UID = "uid";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //appointment date
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    //appointment time
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    //modified date
    public static final String COLUMN_MODIFIED_DATE = "modified_date";

    //subjective notes
    public static final String COLUMN_SUBJECTIVE_NOTES = "subjective_notes";

    //objective notes
    public static final String COLUMN_OBJECTIVE_NOTES = "objective_notes";

    //assessment notes
    public static final String COLUMN_ASSESSMENT_NOTES = "assessment_notes";

    //plan notes
    public static final String COLUMN_PLAN_NOTES = "plan_notes";

    //default sort order
    public static final String SORT_ORDER_DATE_TIME = COLUMN_APPOINTMENT_DATE + " ASC, " + COLUMN_APPOINTMENT_TIME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_CLIENT_KEY,
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_MODIFIED_DATE,
                    COLUMN_SUBJECTIVE_NOTES,
                    COLUMN_OBJECTIVE_NOTES,
                    COLUMN_ASSESSMENT_NOTES,
                    COLUMN_PLAN_NOTES
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_APPOINTMENT_DATE = 2;
    public static final int INDEX_APPOINTMENT_TIME = 3;
    public static final int INDEX_MODIFIED_DATE = 4;
    public static final int INDEX_SUBJECTIVE_NOTES = 5;
    public static final int INDEX_OBJECTIVE_NOTES = 6;
    public static final int INDEX_ASSESSMENT_NOTES = 7;
    public static final int INDEX_PLAN_NOTES = 8;

}
