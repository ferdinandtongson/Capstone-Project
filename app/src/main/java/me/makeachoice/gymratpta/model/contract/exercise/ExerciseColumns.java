package me.makeachoice.gymratpta.model.contract.exercise;

import static me.makeachoice.gymratpta.model.contract.exercise.CategoryColumns.COLUMN_CATEGORY_NAME;

/**
 * Created by Usuario on 2/1/2017.
 */

public class ExerciseColumns {

    // Table name
    public static final String TABLE_NAME = "exercise";

    //user id
    public static final String COLUMN_UID = "uid";

    //category key
    public static final String COLUMN_CATEGORY_KEY = "category_key";

    //firebase key
    public static final String COLUMN_FKEY = "fkey";

    //exercise name
    public static final String COLUMN_EXERCISE_NAME = "exercise_name";

    //exercise category
    public static final String COLUMN_EXERCISE_CATEGORY = "exercise_category";

    //primary record data
    public static final String COLUMN_RECORD_PRIMARY = "record_primary";

    //secondary record data
    public static final String COLUMN_RECORD_SECONDARY = "record_secondary";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_EXERCISE_NAME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_CATEGORY_KEY,
                    COLUMN_FKEY,
                    COLUMN_EXERCISE_NAME,
                    COLUMN_EXERCISE_CATEGORY,
                    COLUMN_RECORD_PRIMARY,
                    COLUMN_RECORD_SECONDARY
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CATEGORY_KEY = 1;
    public static final int INDEX_FKEY = 2;
    public static final int INDEX_EXERCISE_NAME = 3;
    public static final int INDEX_EXERCISE_CATEGORY = 4;
    public static final int INDEX_RECORD_PRIMARY = 5;
    public static final int INDEX_RECORD_SECONDARY = 6;

}
