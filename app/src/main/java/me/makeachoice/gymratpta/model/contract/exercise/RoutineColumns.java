package me.makeachoice.gymratpta.model.contract.exercise;

/**************************************************************************************************/
/*
 * RoutineColumns define columns used in routine database table
 */
/**************************************************************************************************/

public class RoutineColumns {

    // Table name
    public static String TABLE_NAME = "routine";

    //user id
    public static String COLUMN_UID = "uid";

    //routine name
    public static String COLUMN_ROUTINE_NAME = "routine_name";

    //order number
    public static String COLUMN_ORDER_NUMBER = "order_number";

    //exercise
    public static String COLUMN_EXERCISE = "exercise";

    //category
    public static String COLUMN_CATEGORY = "category";

    //number of sets
    public static String COLUMN_NUM_SETS = "numOfSets";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_ROUTINE_NAME + " ASC";

    public static String SORT_BY_ORDER_NUMBER = COLUMN_ORDER_NUMBER + " ASC";

    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_ROUTINE =
            {
                    COLUMN_UID,
                    COLUMN_ROUTINE_NAME,
                    COLUMN_ORDER_NUMBER,
                    COLUMN_EXERCISE,
                    COLUMN_CATEGORY,
                    COLUMN_NUM_SETS
            };

    public static int INDEX_UID = 0;
    public static int INDEX_ROUTINE_NAME = 1;
    public static int INDEX_ORDER_NUMBER = 2;
    public static int INDEX_EXERCISE = 3;
    public static int INDEX_CATEGORY = 4;
    public static int INDEX_NUM_SETS = 5;

}
