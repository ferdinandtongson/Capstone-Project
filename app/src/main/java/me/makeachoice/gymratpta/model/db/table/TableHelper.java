package me.makeachoice.gymratpta.model.db.table;

import me.makeachoice.gymratpta.model.contract.Contractor;

/**************************************************************************************************/
/*
 *  Tables used in sqlite database
 */
/**************************************************************************************************/

public class TableHelper {

    public final static String SQL_CREATE_USER_TABLE = "CREATE TABLE " + Contractor.UserEntry.TABLE_NAME + " (" +
            Contractor.UserEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.UserEntry.COLUMN_UID + " TEXT UNIQUE NOT NULL, " +
            Contractor.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
            Contractor.UserEntry.COLUMN_USER_EMAIL + " TEXT, " +
            Contractor.UserEntry.COLUMN_USER_PHOTO+ " TEXT, " +
            Contractor.UserEntry.COLUMN_USER_STATUS + " TEXT NOT NULL);";

    public final static String SQL_CREATE_CLIENT_TABLE = "CREATE TABLE " + Contractor.ClientEntry.TABLE_NAME + " (" +
            Contractor.ClientEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.ClientEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.ClientEntry.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            Contractor.ClientEntry.COLUMN_CONTACTS_ID + " INTEGER NOT NULL, " +
            Contractor.ClientEntry.COLUMN_CLIENT_NAME + " TEXT NOT NULL, " +
            Contractor.ClientEntry.COLUMN_CLIENT_EMAIL + " TEXT, " +
            Contractor.ClientEntry.COLUMN_CLIENT_PHONE + " TEXT, " +
            Contractor.ClientEntry.COLUMN_FIRST_SESSION + " TEXT, " +
            Contractor.ClientEntry.COLUMN_CLIENT_GOALS + " TEXT, " +
            Contractor.ClientEntry.COLUMN_CLIENT_STATUS + " TEXT NOT NULL, " +
            Contractor.ClientEntry.COLUMN_PROFILE_PIC + " TEXT);";

    public final static String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + Contractor.CategoryEntry.TABLE_NAME + " (" +
            Contractor.CategoryEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.CategoryEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.CategoryEntry.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            Contractor.CategoryEntry.COLUMN_CATEGORY_NAME + " TEXT NOT NULL);";

    public final static String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " + Contractor.ExerciseEntry.TABLE_NAME + " (" +
            Contractor.ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.ExerciseEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.ExerciseEntry.COLUMN_CATEGORY_KEY + " TEXT NOT NULL, " +
            Contractor.ExerciseEntry.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            Contractor.ExerciseEntry.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
            Contractor.ExerciseEntry.COLUMN_EXERCISE_CATEGORY + " TEXT NOT NULL, " +
            Contractor.ExerciseEntry.COLUMN_RECORD_PRIMARY + " TEXT, " +
            Contractor.ExerciseEntry.COLUMN_RECORD_SECONDARY + " TEXT);";

    public final static String SQL_CREATE_ROUTINE_TABLE = "CREATE TABLE " + Contractor.RoutineEntry.TABLE_NAME + " (" +
            Contractor.RoutineEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.RoutineEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.RoutineEntry.COLUMN_ROUTINE_NAME+ " TEXT NOT NULL, " +
            Contractor.RoutineEntry.COLUMN_ORDER_NUMBER + " INTEGER NOT NULL, " +
            Contractor.RoutineEntry.COLUMN_EXERCISE + " TEXT NOT NULL, " +
            Contractor.RoutineEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
            Contractor.RoutineEntry.COLUMN_NUM_SETS + " INTEGER NOT NULL);";

    public final static String SQL_CREATE_ROUTINE_NAME_TABLE = "CREATE TABLE " + Contractor.RoutineNameEntry.TABLE_NAME + " (" +
            Contractor.RoutineNameEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.RoutineNameEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.RoutineNameEntry.COLUMN_ROUTINE_NAME+ " TEXT NOT NULL);";
}
