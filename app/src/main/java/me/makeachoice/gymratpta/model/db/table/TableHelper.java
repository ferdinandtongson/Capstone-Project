package me.makeachoice.gymratpta.model.db.table;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientContract;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryContract;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameContract;
import me.makeachoice.gymratpta.model.contract.user.UserContract;

/**************************************************************************************************/
/*
 *  Tables used in sqlite database
 */
/**************************************************************************************************/

public class TableHelper {

    public final static String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserContract.TABLE_NAME + " (" +
            UserContract._ID + " INTEGER PRIMARY KEY," +
            UserContract.COLUMN_UID + " TEXT UNIQUE NOT NULL, " +
            UserContract.COLUMN_USER_NAME + " TEXT NOT NULL, " +
            UserContract.COLUMN_USER_EMAIL + " TEXT, " +
            UserContract.COLUMN_USER_STATUS + " TEXT NOT NULL);";

    public final static String SQL_CREATE_CLIENT_TABLE = "CREATE TABLE " + ClientContract.TABLE_NAME + " (" +
            ClientContract._ID + " INTEGER PRIMARY KEY," +
            ClientContract.COLUMN_UID + " TEXT NOT NULL, " +
            ClientContract.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            ClientContract.COLUMN_CONTACTS_ID + " INTEGER NOT NULL, " +
            ClientContract.COLUMN_CLIENT_NAME + " TEXT NOT NULL, " +
            ClientContract.COLUMN_CLIENT_EMAIL + " TEXT, " +
            ClientContract.COLUMN_CLIENT_PHONE + " TEXT, " +
            ClientContract.COLUMN_FIRST_SESSION + " TEXT, " +
            ClientContract.COLUMN_CLIENT_GOALS + " TEXT, " +
            ClientContract.COLUMN_CLIENT_STATUS + " TEXT NOT NULL, " +
            ClientContract.COLUMN_PROFILE_PIC + " TEXT);";

    public final static String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryContract.TABLE_NAME + " (" +
            CategoryContract._ID + " INTEGER PRIMARY KEY," +
            CategoryContract.COLUMN_UID + " TEXT NOT NULL, " +
            CategoryContract.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            CategoryContract.COLUMN_CATEGORY_NAME + " TEXT NOT NULL);";

    public final static String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseContract.TABLE_NAME + " (" +
            ExerciseContract._ID + " INTEGER PRIMARY KEY," +
            ExerciseContract.COLUMN_UID + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_CATEGORY_KEY + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_FKEY + " TEXT, " +
            ExerciseContract.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_EXERCISE_CATEGORY + " TEXT NOT NULL, " +
            ExerciseContract.COLUMN_RECORD_PRIMARY + " TEXT, " +
            ExerciseContract.COLUMN_RECORD_SECONDARY + " TEXT);";

    public final static String SQL_CREATE_ROUTINE_TABLE = "CREATE TABLE " + RoutineContract.TABLE_NAME + " (" +
            RoutineContract._ID + " INTEGER PRIMARY KEY," +
            RoutineContract.COLUMN_UID + " TEXT NOT NULL, " +
            RoutineContract.COLUMN_ROUTINE_NAME+ " TEXT NOT NULL, " +
            RoutineContract.COLUMN_ORDER_NUMBER + " TEXT NOT NULL, " +
            RoutineContract.COLUMN_EXERCISE + " TEXT NOT NULL, " +
            RoutineContract.COLUMN_CATEGORY + " TEXT NOT NULL, " +
            RoutineContract.COLUMN_NUM_SETS + " TEXT NOT NULL);";

    public final static String SQL_CREATE_ROUTINE_NAME_TABLE = "CREATE TABLE " + RoutineNameContract.TABLE_NAME + " (" +
            RoutineNameContract._ID + " INTEGER PRIMARY KEY," +
            RoutineNameContract.COLUMN_UID + " TEXT NOT NULL, " +
            RoutineNameContract.COLUMN_ROUTINE_NAME+ " TEXT NOT NULL);";

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
            ClientRoutineContract.COLUMN_ORDER_NUMBER + " TEXT, " +
            ClientRoutineContract.COLUMN_NUM_OF_SETS + " TEXT);";

    public final static String SQL_CREATE_CLIENT_EXERCISE_TABLE = "CREATE TABLE " + ClientExerciseContract.TABLE_NAME + " (" +
            ClientExerciseContract._ID + " INTEGER PRIMARY KEY," +
            ClientExerciseContract.COLUMN_UID + " TEXT NOT NULL, " +
            ClientExerciseContract.COLUMN_CLIENT_KEY + " TEXT, " +
            ClientExerciseContract.COLUMN_TIMESTAMP + " TEXT, " +
            ClientExerciseContract.COLUMN_CATEGORY + " TEXT, " +
            ClientExerciseContract.COLUMN_EXERCISE + " TEXT, " +
            ClientExerciseContract.COLUMN_ORDER_NUMBER + " TEXT, " +
            ClientExerciseContract.COLUMN_SET_NUMBER + " TEXT, " +
            ClientExerciseContract.COLUMN_PRIMARY_LABEL + " TEXT, " +
            ClientExerciseContract.COLUMN_PRIMARY_VALUE + " TEXT, " +
            ClientExerciseContract.COLUMN_SECONDARY_LABEL + " TEXT, " +
            ClientExerciseContract.COLUMN_SECONDARY_VALUE + " TEXT);";

}
