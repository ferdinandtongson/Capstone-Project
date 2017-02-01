package me.makeachoice.gymratpta.model.contract.exercise;

/**
 * Created by Usuario on 2/1/2017.
 */

public class RoutineColumns {

    // Table name
    public static final String TABLE_NAME = "routine";

    //Firebase key
    public static final String COLUMN_FIREBASE_KEY = "firebase_key";

    //user key
    public static final String COLUMN_USER_KEY = "user_key";

    //routine name
    public static final String COLUMN_ROUTINE_NAME = "routine_name";

    //exercise names; ie push ups, bench press, .....
    public static final String COLUMN_EXERCISE_NAMES = "exercise_names";

    //exerise sets; ie 3, 4, .....
    public static final String COLUMN_EXERCISE_SETS = "exercise_sets";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_ROUTINE_NAME + " ASC";

}
