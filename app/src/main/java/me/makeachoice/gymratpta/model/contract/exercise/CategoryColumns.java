package me.makeachoice.gymratpta.model.contract.exercise;


/**************************************************************************************************/
/*
 *  CategoryColumns are columns used in category table in the sqlite database
 */
/**************************************************************************************************/

public class CategoryColumns {

    // Table name
    public static final String TABLE_NAME = "category";

    //user id
    public static final String COLUMN_UID = "uid";

    //firebase key
    public static final String COLUMN_FKEY = "fkey";

    //contacts id
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    //default sort order
    public static final String SORT_ORDER_DEFAULT = COLUMN_CATEGORY_NAME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_FKEY,
                    COLUMN_CATEGORY_NAME
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_FKEY = 1;
    public static final int INDEX_CATEGORY_NAME = 2;


}
