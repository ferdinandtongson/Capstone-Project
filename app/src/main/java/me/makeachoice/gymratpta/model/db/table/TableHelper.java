package me.makeachoice.gymratpta.model.db.table;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;

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

    public final static String SQL_CREATE_STATS_TABLE = "CREATE TABLE " + Contractor.StatsEntry.TABLE_NAME + " (" +
            Contractor.StatsEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.StatsEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.StatsEntry.COLUMN_CLIENT_KEY + " TEXT, " +
            Contractor.StatsEntry.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
            Contractor.StatsEntry.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
            Contractor.StatsEntry.COLUMN_MODIFIED_DATE + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_WEIGHT + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_BODY_FAT + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_BMI + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_NECK + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_CHEST + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_RBICEP + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_LBICEP + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_WAIST + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_NAVAL + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_HIPS + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_RTHIGH + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_LTHIGH + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_RCALF + " TEXT, " +
            Contractor.StatsEntry.COLUMN_STAT_LCALF + " TEXT);";

    public final static String SQL_CREATE_CLIENT_ROUTINE_TABLE = "CREATE TABLE " + Contractor.ClientRoutineEntry.TABLE_NAME + " (" +
            Contractor.ClientRoutineEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.ClientRoutineEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.ClientRoutineEntry.COLUMN_CLIENT_KEY + " TEXT, " +
            Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
            Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
            Contractor.ClientRoutineEntry.COLUMN_CATEGORY + " TEXT, " +
            Contractor.ClientRoutineEntry.COLUMN_EXERCISE + " TEXT, " +
            Contractor.ClientRoutineEntry.COLUMN_ORDER_NUMBER + " INTEGER, " +
            Contractor.ClientRoutineEntry.COLUMN_NUM_OF_SETS + " INTEGER);";

    public final static String SQL_CREATE_CLIENT_EXERCISE_TABLE = "CREATE TABLE " + Contractor.ClientExerciseEntry.TABLE_NAME + " (" +
            Contractor.ClientExerciseEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.ClientExerciseEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.ClientExerciseEntry.COLUMN_CLIENT_KEY + " TEXT, " +
            Contractor.ClientExerciseEntry.COLUMN_TIMESTAMP + " INTEGER, " +
            Contractor.ClientExerciseEntry.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, " +
            Contractor.ClientExerciseEntry.COLUMN_APPOINTMENT_TIME + " TEXT NOT NULL, " +
            Contractor.ClientExerciseEntry.COLUMN_CATEGORY + " TEXT, " +
            Contractor.ClientExerciseEntry.COLUMN_EXERCISE + " TEXT, " +
            Contractor.ClientExerciseEntry.COLUMN_ORDER_NUMBER + " INTEGER, " +
            Contractor.ClientExerciseEntry.COLUMN_SET_NUMBER + " INTEGER, " +
            Contractor.ClientExerciseEntry.COLUMN_PRIMARY_LABEL + " TEXT, " +
            Contractor.ClientExerciseEntry.COLUMN_PRIMARY_VALUE + " TEXT, " +
            Contractor.ClientExerciseEntry.COLUMN_SECONDARY_LABEL + " TEXT, " +
            Contractor.ClientExerciseEntry.COLUMN_SECONDARY_VALUE + " TEXT);";

}
