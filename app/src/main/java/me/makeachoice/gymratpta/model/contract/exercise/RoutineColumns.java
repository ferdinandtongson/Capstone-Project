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

    //firebase key
    public static String COLUMN_FKEY = "fkey";

    //routine name
    public static String COLUMN_ROUTINE_NAME = "routine_name";

    //exercise name 01
    public static String COLUMN_EXERCISE01 = "exercise01";

    //exercise name 02
    public static String COLUMN_EXERCISE02 = "exercise02";

    //exercise name 03
    public static String COLUMN_EXERCISE03 = "exercise03";

    //exercise name 04
    public static String COLUMN_EXERCISE04 = "exercise04";

    //exercise name 05
    public static String COLUMN_EXERCISE05 = "exercise05";

    //exercise name 06
    public static String COLUMN_EXERCISE06 = "exercise06";

    //exercise name 07
    public static String COLUMN_EXERCISE07 = "exercise07";

    //exercise name 08
    public static String COLUMN_EXERCISE08 = "exercise08";

    //exercise name 09
    public static String COLUMN_EXERCISE09 = "exercise09";

    //exercise name 10
    public static String COLUMN_EXERCISE10 = "exercise10";

    //category name 01
    public static String COLUMN_CATEGORY01 = "category01";

    //category name 02
    public static String COLUMN_CATEGORY02 = "category02";

    //category name 03
    public static String COLUMN_CATEGORY03 = "category03";

    //category name 04
    public static String COLUMN_CATEGORY04 = "category04";

    //category name 05
    public static String COLUMN_CATEGORY05 = "category05";

    //category name 06
    public static String COLUMN_CATEGORY06 = "category06";

    //category name 07
    public static String COLUMN_CATEGORY07 = "category07";

    //category name 08
    public static String COLUMN_CATEGORY08 = "category08";

    //category name 09
    public static String COLUMN_CATEGORY09 = "category09";

    //category name 10
    public static String COLUMN_CATEGORY10 = "category10";

    //set name 01
    public static String COLUMN_SET01 = "set01";

    //set name 02
    public static String COLUMN_SET02 = "set02";

    //set name 03
    public static String COLUMN_SET03 = "set03";

    //set name 04
    public static String COLUMN_SET04 = "set04";

    //set name 05
    public static String COLUMN_SET05 = "set05";

    //set name 06
    public static String COLUMN_SET06 = "set06";

    //set name 07
    public static String COLUMN_SET07 = "set07";

    //set name 08
    public static String COLUMN_SET08 = "set08";

    //set name 09
    public static String COLUMN_SET09 = "set09";

    //set name 10
    public static String COLUMN_SET10 = "set10";


    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_ROUTINE_NAME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_ROUTINE =
            {
                    COLUMN_UID,
                    COLUMN_FKEY,
                    COLUMN_ROUTINE_NAME,
                    COLUMN_EXERCISE01,
                    COLUMN_EXERCISE02,
                    COLUMN_EXERCISE03,
                    COLUMN_EXERCISE04,
                    COLUMN_EXERCISE05,
                    COLUMN_EXERCISE06,
                    COLUMN_EXERCISE07,
                    COLUMN_EXERCISE08,
                    COLUMN_EXERCISE09,
                    COLUMN_EXERCISE10,
                    COLUMN_CATEGORY01,
                    COLUMN_CATEGORY02,
                    COLUMN_CATEGORY03,
                    COLUMN_CATEGORY04,
                    COLUMN_CATEGORY05,
                    COLUMN_CATEGORY06,
                    COLUMN_CATEGORY07,
                    COLUMN_CATEGORY08,
                    COLUMN_CATEGORY09,
                    COLUMN_CATEGORY10,
                    COLUMN_SET01,
                    COLUMN_SET02,
                    COLUMN_SET03,
                    COLUMN_SET04,
                    COLUMN_SET05,
                    COLUMN_SET06,
                    COLUMN_SET07,
                    COLUMN_SET08,
                    COLUMN_SET09,
                    COLUMN_SET10
            };

    public static int INDEX_UID = 0;
    public static int INDEX_FKEY = 1;
    public static int INDEX_ROUTINE_NAME = 2;
    public static int INDEX_EXERCISE01 = 3;
    public static int INDEX_EXERCISE02 = 4;
    public static int INDEX_EXERCISE03 = 5;
    public static int INDEX_EXERCISE04 = 6;
    public static int INDEX_EXERCISE05 = 7;
    public static int INDEX_EXERCISE06 = 8;
    public static int INDEX_EXERCISE07 = 9;
    public static int INDEX_EXERCISE08 = 10;
    public static int INDEX_EXERCISE09 = 11;
    public static int INDEX_EXERCISE10 = 12;
    public static int INDEX_CATEGORY01 = 13;
    public static int INDEX_CATEGORY02 = 14;
    public static int INDEX_CATEGORY03 = 15;
    public static int INDEX_CATEGORY04 = 16;
    public static int INDEX_CATEGORY05 = 17;
    public static int INDEX_CATEGORY06 = 18;
    public static int INDEX_CATEGORY07 = 19;
    public static int INDEX_CATEGORY08 = 20;
    public static int INDEX_CATEGORY09 = 21;
    public static int INDEX_CATEGORY10 = 22;
    public static int INDEX_SET01 = 23;
    public static int INDEX_SET02 = 24;
    public static int INDEX_SET03 = 25;
    public static int INDEX_SET04 = 26;
    public static int INDEX_SET05 = 27;
    public static int INDEX_SET06 = 28;
    public static int INDEX_SET07 = 29;
    public static int INDEX_SET08 = 30;
    public static int INDEX_SET09 = 31;
    public static int INDEX_SET10 = 32;


}
