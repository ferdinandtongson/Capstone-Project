package me.makeachoice.gymratpta.model.contract.exercise;


/**
 * Created by Usuario on 2/1/2017.
 */

public class ExerciseColumns {

    // Table name
    public static String TABLE_NAME = "exercise";

    //user id
    public static String COLUMN_UID = "uid";

    //category key
    public static String COLUMN_CATEGORY_KEY = "category_key";

    //firebase key
    public static String COLUMN_FKEY = "fkey";

    //exercise name
    public static String COLUMN_EXERCISE_NAME = "exercise_name";

    //exercise category
    public static String COLUMN_EXERCISE_CATEGORY = "exercise_category";

    //primary record data
    public static String COLUMN_RECORD_PRIMARY = "record_primary";

    //secondary record data
    public static String COLUMN_RECORD_SECONDARY = "record_secondary";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_EXERCISE_NAME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_EXERCISE =
            {
                    COLUMN_UID,
                    COLUMN_CATEGORY_KEY,
                    COLUMN_FKEY,
                    COLUMN_EXERCISE_NAME,
                    COLUMN_EXERCISE_CATEGORY,
                    COLUMN_RECORD_PRIMARY,
                    COLUMN_RECORD_SECONDARY
            };

    public static int INDEX_UID = 0;
    public static int INDEX_CATEGORY_KEY = 1;
    public static int INDEX_FKEY = 2;
    public static int INDEX_EXERCISE_NAME = 3;
    public static int INDEX_EXERCISE_CATEGORY = 4;
    public static int INDEX_RECORD_PRIMARY = 5;
    public static int INDEX_RECORD_SECONDARY = 6;

}
