package me.makeachoice.gymratpta.controller.modelside.provider;

import android.content.UriMatcher;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.MyContractor;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;

/**
 * Created by Usuario on 2/2/2017.
 */

public class UriMatcherHelper {

    //uri matcher id numbers
    public static final int USER = 100;
    public static final int USER_WITH_KEY = 101;

    public static final int CLIENT = 200;
    public static final int CLIENT_WITH_UID = 201;
    public static final int CLIENT_WITH_FKEY = 202;
    public static final int CLIENT_WITH_STATUS = 203;

    public static final int CATEGORY = 300;
    public static final int CATEGORY_WITH_UID = 301;
    public static final int CATEGORY_WITH_FKEY = 302;
    public static final int CATEGORY_WITH_NAME = 303;

    public static final int EXERCISE = 400;
    public static final int EXERCISE_WITH_UID = 401;
    public static final int EXERCISE_WITH_CATEGORY_KEY = 402;
    public static final int EXERCISE_WITH_FKEY = 403;
    public static final int EXERCISE_WITH_NAME = 404;

    public static final int ROUTINE = 500;
    public static final int ROUTINE_WITH_UID = 501;
    public static final int ROUTINE_WITH_ROUTINE_NAME = 502;
    public static final int ROUTINE_EXERCISE_WITH_ORDER_NUMBER = 503;

    public static final int ROUTINE_NAME = 600;
    public static final int ROUTINE_NAME_WITH_UID = 601;
    public static final int ROUTINE_NAME_WITH_NAME = 602;

    public static final int SCHEDULE = 700;
    public static final int SCHEDULE_WITH_UID = 701;
    public static final int SCHEDULE_WITH_TIMESTAMP = 702;
    public static final int SCHEDULE_WITH_CLIENT_KEY = 703;
    public static final int SCHEDULE_WITH_RANGE = 704;

    public static final int NOTES = 800;
    public static final int NOTES_WITH_UID = 801;
    public static final int NOTES_WITH_CLIENT_KEY = 802;
    public static final int NOTES_WITH_TIMESTAMP = 803;
    public static final int NOTES_WITH_DATE_TIME = 804;

    public static final int STATS = 900;
    public static final int STATS_WITH_UID = 901;
    public static final int STATS_WITH_CLIENT_KEY = 902;
    public static final int STATS_WITH_TIMESTAMP = 903;
    public static final int STATS_WITH_DATE_TIME = 904;

    public static final int CLIENT_ROUTINE = 1000;
    public static final int CLIENT_ROUTINE_WITH_UID = 1001;
    public static final int CLIENT_ROUTINE_WITH_CLIENT_KEY = 1002;
    public static final int CLIENT_ROUTINE_WITH_TIMESTAMP = 1003;
    public static final int CLIENT_ROUTINE_WITH_EXERCISE = 1004;

    public static final int CLIENT_EXERCISE = 1100;
    public static final int CLIENT_EXERCISE_WITH_UID = 1101;
    public static final int CLIENT_EXERCISE_WITH_CLIENT_KEY = 1102;
    public static final int CLIENT_EXERCISE_WITH_EXERCISE = 1103;
    public static final int CLIENT_EXERCISE_WITH_TIMESTAMP = 1104;
    public static final int CLIENT_EXERCISE_WITH_TIMESTAMP_EXERCISE = 1105;

    public static final UriMatcher dbUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contractor.CONTENT_AUTHORITY;

        //"content://CONTENT_AUTHORITY/user/
        matcher.addURI(authority, Contractor.PATH_USER, USER);
        //"content://CONTENT_AUTHORITY/user/[_id]
        matcher.addURI(authority, Contractor.PATH_USER + "/*", USER_WITH_KEY);

        //"content://CONTENT_AUTHORITY/client/
        matcher.addURI(authority, Contractor.PATH_CLIENT, CLIENT);

        //"content://CONTENT_AUTHORITY/client/[uid]
        String uidPath = Contractor.PATH_CLIENT + "/*";
        matcher.addURI(authority, uidPath, CLIENT_WITH_UID);

        //"content://CONTENT_AUTHORITY/client/[uid]/fkey/[fkey]
        String fkeyPath = Contractor.PATH_CLIENT + "/*/" + Contractor.ClientEntry.COLUMN_FKEY + "/*";
        matcher.addURI(authority, fkeyPath, CLIENT_WITH_FKEY);

        //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
        String statusPath = Contractor.PATH_CLIENT + "/*/" + Contractor.ClientEntry.COLUMN_CLIENT_STATUS + "/*";
        matcher.addURI(authority, statusPath, CLIENT_WITH_STATUS);

        addUriCategory(matcher, authority);
        addUriExercise(matcher, authority);
        addUriRoutine(matcher, authority);
        addUriRoutineName(matcher, authority);
        addUriSchedule(matcher, authority);
        addUriNotes(matcher, authority);
        addUriStats(matcher, authority);
        addUriClientRoutine(matcher, authority);
        addUriClientExercise(matcher,authority);

        return matcher;
    }

    private static void addUriCategory(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/category/
        matcher.addURI(authority, Contractor.PATH_CATEGORY, CATEGORY);

        //"content://CONTENT_AUTHORITY/category/[uid]
        String uidPath = Contractor.PATH_CATEGORY + "/*";
        matcher.addURI(authority, uidPath, CATEGORY_WITH_UID);

        //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fkey]
        String fkeyPath = Contractor.PATH_CATEGORY + "/*/" + Contractor.CategoryEntry.COLUMN_FKEY + "/*";
        matcher.addURI(authority, fkeyPath, CATEGORY_WITH_FKEY);

        //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
        String namePath = Contractor.PATH_CATEGORY + "/*/" + Contractor.CategoryEntry.COLUMN_CATEGORY_NAME + "/*";
        matcher.addURI(authority, namePath, CATEGORY_WITH_NAME);

    }

    private static void addUriExercise(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/exercise/
        matcher.addURI(authority, Contractor.PATH_EXERCISE, EXERCISE);

        //"content://CONTENT_AUTHORITY/exercise/[uid]
        String uidPath = Contractor.PATH_EXERCISE + "/*";
        matcher.addURI(authority, uidPath, EXERCISE_WITH_UID);

        //"content://CONTENT_AUTHORITY/exercise/[uid]/category_key/[categoryKey]
        String exerciseKeyPath = Contractor.PATH_EXERCISE + "/*/" + ExerciseContract.COLUMN_CATEGORY_KEY + "/*";
        matcher.addURI(authority, exerciseKeyPath, EXERCISE_WITH_CATEGORY_KEY);

        //"content://CONTENT_AUTHORITY/exercise/[uid]/fkey/[fkey]
        String fkeyPath = Contractor.PATH_EXERCISE + "/*/" + ExerciseContract.COLUMN_FKEY + "/*";
        matcher.addURI(authority, fkeyPath, EXERCISE_WITH_FKEY);

        //"content://CONTENT_AUTHORITY/exercise/[uid]/[categoryKey]/exercise_name/[exerciseName]
        String namePath = Contractor.PATH_EXERCISE + "/*/*/" + ExerciseContract.COLUMN_EXERCISE_NAME + "/*";
        matcher.addURI(authority, namePath, EXERCISE_WITH_NAME);

    }

    private static void addUriRoutine(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/routine/
        matcher.addURI(authority, Contractor.PATH_ROUTINE, ROUTINE);

        //"content://CONTENT_AUTHORITY/routine/[uid]
        String uidPath = Contractor.PATH_ROUTINE + "/*";
        matcher.addURI(authority, uidPath, ROUTINE_WITH_UID);

        //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]
        String exerciseKeyPath = Contractor.PATH_ROUTINE + "/*/*";
        matcher.addURI(authority, exerciseKeyPath, ROUTINE_WITH_ROUTINE_NAME);

        //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]/[orderNumber]
        String fkeyPath = Contractor.PATH_ROUTINE + "/*/*/*";
        matcher.addURI(authority, fkeyPath, ROUTINE_EXERCISE_WITH_ORDER_NUMBER);

    }

    private static void addUriRoutineName(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/routineName/
        matcher.addURI(authority, Contractor.PATH_ROUTINE_NAME, ROUTINE_NAME);

        //"content://CONTENT_AUTHORITY/routineName/[uid]
        String uidPath = Contractor.PATH_ROUTINE_NAME + "/*";
        matcher.addURI(authority, uidPath, ROUTINE_NAME_WITH_UID);

        //"content://CONTENT_AUTHORITY/routineName/[uid]/[routineName]
        String routiNENamePath = Contractor.PATH_ROUTINE_NAME + "/*/*";
        matcher.addURI(authority, routiNENamePath, ROUTINE_NAME_WITH_NAME);

    }

    private static void addUriSchedule(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/userSchedule/
        matcher.addURI(authority, MyContractor.PATH_SCHEDULE, SCHEDULE);

        //"content://CONTENT_AUTHORITY/userSchedule/[uid]
        String uidPath = MyContractor.PATH_SCHEDULE + "/*";
        matcher.addURI(authority, uidPath, SCHEDULE_WITH_UID);

        //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[timestamp]
        String dayPath = MyContractor.PATH_SCHEDULE + "/*/" + ScheduleContract.COLUMN_TIMESTAMP + "/*";
        matcher.addURI(authority, dayPath, SCHEDULE_WITH_TIMESTAMP);

        //"content://CONTENT_AUTHORITY/userSchedule/[uid]/client_key/[clientKey]
        String clientKeyPath = MyContractor.PATH_SCHEDULE + "/*/" + ScheduleContract.COLUMN_CLIENT_KEY + "/*";
        matcher.addURI(authority, clientKeyPath, SCHEDULE_WITH_CLIENT_KEY);

        //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[startDate]/[endDate]
        String rangePath = MyContractor.PATH_SCHEDULE + "/*/" + ScheduleContract.COLUMN_TIMESTAMP + "/*/*";
        matcher.addURI(authority, rangePath, SCHEDULE_WITH_RANGE);
    }

    private static void addUriNotes(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/clientNotes/
        matcher.addURI(authority, NotesContract.PATH_NOTES, NOTES);

        //"content://CONTENT_AUTHORITY/clientNotes/[uid]
        String uidPath = NotesContract.PATH_NOTES + "/*";
        matcher.addURI(authority, uidPath, NOTES_WITH_UID);

        //"content://CONTENT_AUTHORITY/clientNotes/[uid]/client_key/[clientKey]
        String clientKeyPath = NotesContract.PATH_NOTES + "/*/" + NotesContract.COLUMN_CLIENT_KEY + "/*";
        matcher.addURI(authority, clientKeyPath, NOTES_WITH_CLIENT_KEY);

        //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/timestamp/[timestamp]
        String datePath = NotesContract.PATH_NOTES + "/*/*/"  + NotesContract.COLUMN_TIMESTAMP + "/*";
        matcher.addURI(authority, datePath, NOTES_WITH_TIMESTAMP);

        //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
        String timePath = NotesContract.PATH_NOTES + "/*/*/*/"  + NotesContract.COLUMN_APPOINTMENT_TIME + "/*";
        matcher.addURI(authority, timePath, NOTES_WITH_DATE_TIME);

    }

    private static void addUriStats(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/clientStats/
        matcher.addURI(authority, StatsContract.PATH_STATS, STATS);

        //"content://CONTENT_AUTHORITY/clientStats/[uid]
        String uidPath = StatsContract.PATH_STATS + "/*";
        matcher.addURI(authority, uidPath, STATS_WITH_UID);

        //"content://CONTENT_AUTHORITY/clientStats/[uid]/client_key/[clientKey]
        String clientKeyPath = StatsContract.PATH_STATS + "/*/" + StatsContract.COLUMN_CLIENT_KEY + "/*";
        matcher.addURI(authority, clientKeyPath, STATS_WITH_CLIENT_KEY);

        //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/timestamp/[timestamp]
        String datePath = StatsContract.PATH_STATS + "/*/*/"  + StatsContract.COLUMN_TIMESTAMP + "/*";
        matcher.addURI(authority, datePath, STATS_WITH_TIMESTAMP);

        //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
        String timePath = StatsContract.PATH_STATS + "/*/*/*/"  + StatsContract.COLUMN_APPOINTMENT_TIME + "/*";
        matcher.addURI(authority, timePath, STATS_WITH_DATE_TIME);

    }

    private static void addUriClientRoutine(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/clientRoutine/
        matcher.addURI(authority, Contractor.PATH_CLIENT_ROUTINE, CLIENT_ROUTINE);

        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]
        String uidPath = Contractor.PATH_CLIENT_ROUTINE + "/*";
        matcher.addURI(authority, uidPath, CLIENT_ROUTINE_WITH_UID);

        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/client_key/[clientKey]
        String clientKeyPath = Contractor.PATH_CLIENT_ROUTINE + "/*/" + ClientRoutineContract.COLUMN_CLIENT_KEY + "/*";
        matcher.addURI(authority, clientKeyPath, CLIENT_ROUTINE_WITH_CLIENT_KEY);

        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/timestamp/[timestamp]
        String timePath = Contractor.PATH_CLIENT_ROUTINE + "/*/*/"  + ClientRoutineContract.COLUMN_TIMESTAMP + "/*";
        matcher.addURI(authority, timePath, CLIENT_ROUTINE_WITH_TIMESTAMP);

        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
        String exercisePath = Contractor.PATH_CLIENT_ROUTINE + "/*/*/*"  + ClientRoutineContract.COLUMN_EXERCISE + "/*";
        matcher.addURI(authority, exercisePath, CLIENT_ROUTINE_WITH_EXERCISE);

    }

    private static void addUriClientExercise(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/clientExercise/
        matcher.addURI(authority, Contractor.PATH_CLIENT_EXERCISE, CLIENT_EXERCISE);

        //"content://CONTENT_AUTHORITY/clientExercise/[uid]
        String uidPath = Contractor.PATH_CLIENT_EXERCISE + "/*";
        matcher.addURI(authority, uidPath, CLIENT_EXERCISE_WITH_UID);

        //"content://CONTENT_AUTHORITY/clientExercise/[uid]/client_key/[clientKey]
        String clientKeyPath = Contractor.PATH_CLIENT_EXERCISE + "/*/" + ClientExerciseContract.COLUMN_CLIENT_KEY + "/*";
        matcher.addURI(authority, clientKeyPath, CLIENT_EXERCISE_WITH_CLIENT_KEY);

        //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/exercise/[exercise]
        String exercisePath = Contractor.PATH_CLIENT_EXERCISE + "/*/*/"  + ClientExerciseContract.COLUMN_EXERCISE + "/*";
        matcher.addURI(authority, exercisePath, CLIENT_EXERCISE_WITH_EXERCISE);

        //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/timestamp/[timestamp]
        String timePath = Contractor.PATH_CLIENT_EXERCISE + "/*/*/"  + ClientExerciseContract.COLUMN_TIMESTAMP + "/*";
        matcher.addURI(authority, timePath, CLIENT_EXERCISE_WITH_TIMESTAMP);

        //content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
        String timeExercisePath = Contractor.PATH_CLIENT_EXERCISE + "/*/*/*/"  + ClientExerciseContract.COLUMN_EXERCISE + "/*";
        matcher.addURI(authority, timeExercisePath, CLIENT_EXERCISE_WITH_TIMESTAMP_EXERCISE);

    }
}
