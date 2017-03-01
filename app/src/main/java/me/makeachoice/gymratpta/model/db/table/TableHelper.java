package me.makeachoice.gymratpta.model.db.table;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;

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

    public final static String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseContract.TABLE_NAME + " (" +
            ExerciseContract._ID + " INTEGER PRIMARY KEY," +
            ExerciseContract.COLUMN_UID + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_CATEGORY_KEY + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            ExerciseContract.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_EXERCISE_CATEGORY + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_RECORD_PRIMARY + " TEXT, " +
            ExerciseContract.COLUMN_RECORD_SECONDARY + " TEXT);";

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

    public final static String SQL_CREATE_SCHEDULE_TABLE = "CREATE TABLE " + ScheduleContract.TABLE_NAME + " (" +
            ScheduleContract._ID + " INTEGER PRIMARY KEY," +
            ScheduleContract.COLUMN_UID + " TEXT NOT NULL, " +
            ScheduleContract.COLUMN_TIMESTAMP + " TEXT, " +
            ScheduleContract.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
            ScheduleContract.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
            ScheduleContract.COLUMN_CLIENT_KEY + " TEXT NOT NULL, " +
            ScheduleContract.COLUMN_CLIENT_NAME + " TEXT, " +
            ScheduleContract.COLUMN_ROUTINE_NAME + " TEXT, " +
            ScheduleContract.COLUMN_APPOINTMENT_STATUS + " TEXT);";

    public final static String SQL_CREATE_NOTES_TABLE = "CREATE TABLE " + NotesContract.TABLE_NAME + " (" +
            NotesContract._ID + " INTEGER PRIMARY KEY," +
            NotesContract.COLUMN_UID + " TEXT NOT NULL, " +
            NotesContract.COLUMN_CLIENT_KEY + " TEXT, " +
            NotesContract.COLUMN_TIMESTAMP + " TEXT, " +
            NotesContract.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
            NotesContract.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
            NotesContract.COLUMN_MODIFIED_DATE + " TEXT, " +
            NotesContract.COLUMN_SUBJECTIVE_NOTES + " TEXT, " +
            NotesContract.COLUMN_OBJECTIVE_NOTES + " TEXT, " +
            NotesContract.COLUMN_ASSESSMENT_NOTES+ " TEXT, " +
            NotesContract.COLUMN_PLAN_NOTES + " TEXT);";

    public final static String SQL_CREATE_STATS_TABLE = "CREATE TABLE " + StatsContract.TABLE_NAME + " (" +
            StatsContract._ID + " INTEGER PRIMARY KEY," +
            StatsContract.COLUMN_UID + " TEXT NOT NULL, " +
            StatsContract.COLUMN_CLIENT_KEY + " TEXT, " +
            StatsContract.COLUMN_TIMESTAMP + " TEXT, " +
            StatsContract.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
            StatsContract.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
            StatsContract.COLUMN_MODIFIED_DATE + " TEXT, " +
            StatsContract.COLUMN_STAT_WEIGHT + " INTEGER, " +
            StatsContract.COLUMN_STAT_BODY_FAT + " INTEGER, " +
            StatsContract.COLUMN_STAT_BMI + " INTEGER, " +
            StatsContract.COLUMN_STAT_NECK + " INTEGER, " +
            StatsContract.COLUMN_STAT_CHEST + " INTEGER, " +
            StatsContract.COLUMN_STAT_RBICEP + " INTEGER, " +
            StatsContract.COLUMN_STAT_LBICEP + " INTEGER, " +
            StatsContract.COLUMN_STAT_WAIST + " INTEGER, " +
            StatsContract.COLUMN_STAT_NAVEL + " INTEGER, " +
            StatsContract.COLUMN_STAT_HIPS + " INTEGER, " +
            StatsContract.COLUMN_STAT_RTHIGH + " INTEGER, " +
            StatsContract.COLUMN_STAT_LTHIGH + " INTEGER, " +
            StatsContract.COLUMN_STAT_RCALF + " INTEGER, " +
            StatsContract.COLUMN_STAT_LCALF + " INTEGER);";

    public final static String SQL_CREATE_CLIENT_ROUTINE_TABLE = "CREATE TABLE " + ClientRoutineContract.TABLE_NAME + " (" +
            ClientRoutineContract._ID + " INTEGER PRIMARY KEY," +
            ClientRoutineContract.COLUMN_UID + " TEXT NOT NULL, " +
            ClientRoutineContract.COLUMN_CLIENT_KEY + " TEXT, " +
            ClientRoutineContract.COLUMN_TIMESTAMP + " TEXT, " +
            ClientRoutineContract.COLUMN_CATEGORY + " TEXT, " +
            ClientRoutineContract.COLUMN_EXERCISE + " TEXT, " +
            ClientRoutineContract.COLUMN_ORDER_NUMBER + " INTEGER, " +
            ClientRoutineContract.COLUMN_NUM_OF_SETS + " INTEGER);";

    public final static String SQL_CREATE_CLIENT_EXERCISE_TABLE = "CREATE TABLE " + ClientExerciseContract.TABLE_NAME + " (" +
            ClientExerciseContract._ID + " INTEGER PRIMARY KEY," +
            ClientExerciseContract.COLUMN_UID + " TEXT NOT NULL, " +
            ClientExerciseContract.COLUMN_CLIENT_KEY + " TEXT, " +
            ClientExerciseContract.COLUMN_TIMESTAMP + " TEXT, " +
            ClientExerciseContract.COLUMN_CATEGORY + " TEXT, " +
            ClientExerciseContract.COLUMN_EXERCISE + " TEXT, " +
            ClientExerciseContract.COLUMN_ORDER_NUMBER + " INTEGER, " +
            ClientExerciseContract.COLUMN_SET_NUMBER + " INTEGER, " +
            ClientExerciseContract.COLUMN_PRIMARY_LABEL + " TEXT, " +
            ClientExerciseContract.COLUMN_PRIMARY_VALUE + " TEXT, " +
            ClientExerciseContract.COLUMN_SECONDARY_LABEL + " TEXT, " +
            ClientExerciseContract.COLUMN_SECONDARY_VALUE + " TEXT);";

}
