package me.makeachoice.library.android.base.model.db;

/**************************************************************************************************/
/*
 *  MyDBTable contains final static string values used in creating, accessing and deleting a db
 */

import static android.R.attr.id;

/**************************************************************************************************/

public abstract class MyDBTable {

/**************************************************************************************************/
/*
 * Database Variables
 */
/**************************************************************************************************/

    protected static final String DB_TYPE_PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT";
    protected static final String DB_TYPE_TEXT = " TEXT";
    protected static final String DB_TYPE_NUMERIC = " NUM";
    protected static final String DB_TYPE_INTEGER = " INT";
    protected static final String DB_TYPE_REAL = " REAL";
    protected static final String DB_TYPE_BLOB = " BLOB";
    protected static final String DB_TYPE_NONE = "";

    protected static final String DB_COMMA_SEPARATOR = ",";
    protected static final String DB_PAREN_OPEN = "(";
    protected static final String DB_PAREN_CLOSE = ")";
    protected static final String DB_NOT_NULL = " NOT NULL";
    protected static final String DB_DEFAULT = " DEFAULT ";
    protected static final String DB_FOREIGN_KEY = "FOREIGN KEY";
    protected static final String DB_REFERENCES = " REFERENCES ";
    protected static final String DB_UNIQUE = " UNIQUE ";
    protected static final String DB_ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE ";

    protected static final String DB_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    protected static final String DB_DROP_TABLE = "DROP TABLE IF EXISTS ";
    protected static final String DB_SELECT_ALL = "SELECT * FROM ";
    protected static final String DB_WHERE = " WHERE ";
    protected static final String DB_UPDATE = "UPDATE ";
    protected static final String DB_REPLACE = "REPLACE INTO ";
    protected static final String DB_ORDER_BY = " ORDER BY ";
    protected static final String DB_ORDER_DESC = " DESC";
    protected static final String DB_ORDER_ASC = " ASC";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Create String Methods
 *      String createPrimaryKey(String) - creates "[id] INTEGER PRIMARY KEY AUTOINCREMENT,"
 *      String createTextColumn(String,boolean,boolean) - create Text type column
 *      String createTextColumn(String,String,boolean) - create Text type column
 *      String createIntegerColumn(String,boolean,boolean) - create Integer type column
 *      String createIntegerColumn(String,String,boolean) - create Integer type column
 *      String createRealColumn(String,boolean,boolean) - create Real type column
 *      String createRealColumn(String,String,boolean) - create Real type column
 *      String createForeignKey(String,String,String,boolean) - create foreign key reference in table
 *      String addCommaSeparator(String) - add comma at end of string value
 */
/**************************************************************************************************/
    /*
     * String createPrimaryKey(String) - creates "[id] INTEGER PRIMARY KEY AUTOINCREMENT,"
     * @param key - primary key string "_id"
     */
    protected static String createPrimaryKey(String key){
        return key + DB_TYPE_PRIMARY_KEY + DB_COMMA_SEPARATOR;
    }

    /*
     * String createTextColumn(String,boolean,boolean) - create Text type column
     * @param column - column name
     * @param notNull - flag used to determine if column can have null value
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createTextColumn(String column, boolean notNull, boolean commaSeparated){
        String defineColumn;

        if(notNull){
            defineColumn = column + DB_TYPE_TEXT + DB_NOT_NULL;
        }
        else{
            defineColumn = column + DB_TYPE_TEXT;
        }

        if(commaSeparated){
            return addCommaSeparator(defineColumn);
        }

        return defineColumn;
    }

    /*
     * String createTextColumn(String,String,boolean) - create Text type column
     * @param column - column name
     * @param extra - extra values to define column attributes
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createTextColumn(String column, String extra, boolean commaSeparated){
        String defineColumn = column + DB_TYPE_TEXT + extra;

        if(commaSeparated){
            return addCommaSeparator(defineColumn);
        }

        return defineColumn;
    }

    /*
     * String createIntegerColumn(String,boolean,boolean) - create Integer type column
     * @param column - column name
     * @param notNull - flag used to determine if column can have null value
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createIntegerColumn(String column, boolean notNull, boolean commaSeparated){
        String defineColumn;

        if(notNull){
            defineColumn = column + DB_TYPE_INTEGER + DB_NOT_NULL;
        }
        else{
            defineColumn = column + DB_TYPE_INTEGER;
        }

        if(commaSeparated){
            return addCommaSeparator(defineColumn);
        }

        return defineColumn;
    }

    /*
     * String createIntegerColumn(String,String,boolean) - create Integer type column
     * @param column - column name
     * @param extra - extra values to define column attributes
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createIntegerColumn(String column, String extra, boolean commaSeparated){
        String defineColumn = column + DB_TYPE_INTEGER + extra;

        if(commaSeparated){
            return addCommaSeparator(defineColumn);
        }

        return defineColumn;
    }

    /*
     * String createRealColumn(String,boolean,boolean) - create Real type column
     * @param column - column name
     * @param notNull - flag used to determine if column can have null value
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createRealColumn(String column, boolean notNull, boolean commaSeparated){
        String defineColumn;

        if(notNull){
            defineColumn = column + DB_TYPE_REAL + DB_NOT_NULL;
        }
        else{
            defineColumn = column + DB_TYPE_REAL;
        }

        if(commaSeparated){
            return addCommaSeparator(defineColumn);
        }

        return defineColumn;
    }

    /*
     * String createRealColumn(String,String,boolean) - create Real type column
     * @param column - column name
     * @param extra - extra values to define column attributes
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createRealColumn(String column, String extra, boolean commaSeparated){
        String defineColumn = column + DB_TYPE_REAL + extra;

        if(commaSeparated){
            return addCommaSeparator(defineColumn);
        }

        return defineColumn;
    }

    /*
     * String createForeignKey(String,String,String,boolean) - create foreign key reference in table
     * @param foreignKey - column defined as a foreign key
     * @param table - table which foreign key is referencing
     * @param column - column in foreign table that foreign key is referencing
     * @param commaSeparated - flag used to determine if comma separated
     */
    protected static String createForeignKey(String foreignKey, String table, String column, boolean commaSeparated){
        String defineForeignKey = DB_FOREIGN_KEY + "(" + foreignKey + ")" + DB_REFERENCES  +
                table + "(" + column + ")";

        if(commaSeparated){
            return addCommaSeparator(defineForeignKey);
        }

        return defineForeignKey;
    }

    /*
     * String addCommaSeparator(String) - add comma at end of string value
     * @param value - string value
     */
    protected static String addCommaSeparator(String value){
        return value + DB_COMMA_SEPARATOR;
    }

/**************************************************************************************************/

}
