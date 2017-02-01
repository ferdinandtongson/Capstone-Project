package me.makeachoice.gymratpta.model.contract.exercise;

/**
 * Created by Usuario on 2/1/2017.
 */

public class ExerciseColumns {

    // Table name
    public static final String TABLE_NAME = "exercise";

    //Firebase key
    public static final String COLUMN_FIREBASE_KEY = "firebase_key";

    //user key
    public static final String COLUMN_USER_KEY = "user_key";

    //exercise name
    public static final String COLUMN_EXERCISE_NAME = "exercise_name";

    //exercise category
    public static final String COLUMN_EXERCISE_CATEGORY = "exercise_category";

    //primary record data
    public static final String COLUMN_PRIMARY_DATA = "primary_data";

    //secondary record data
    public static final String COLUMN_SECONDARY_DATA = "secondary_data";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_EXERCISE_NAME + " ASC";

}
