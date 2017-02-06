package me.makeachoice.gymratpta.model.contract.exercise;

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
    public static final String COLUMN_RECORD_SECONDARY = "record_primary";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_EXERCISE_NAME + " ASC";

}
