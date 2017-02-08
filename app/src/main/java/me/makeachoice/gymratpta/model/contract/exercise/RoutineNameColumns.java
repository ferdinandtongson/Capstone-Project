package me.makeachoice.gymratpta.model.contract.exercise;

import static me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns.COLUMN_CATEGORY;
import static me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns.COLUMN_EXERCISE;
import static me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns.COLUMN_NUM_SETS;
import static me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns.COLUMN_ORDER_NUMBER;

/**************************************************************************************************/
/*
 * RoutineNameColumns define columns used in routineName database table
 */
/**************************************************************************************************/


public class RoutineNameColumns {

    // Table name
    public static String TABLE_NAME = "routineName";

    //user id
    public static String COLUMN_UID = "uid";

    //routine name
    public static String COLUMN_ROUTINE_NAME = "routine_name";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_ROUTINE_NAME + " ASC";

    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_ROUTINE_NAME =
            {
                    COLUMN_UID,
                    COLUMN_ROUTINE_NAME
            };

    public static int INDEX_UID = 0;
    public static int INDEX_ROUTINE_NAME = 1;

}
