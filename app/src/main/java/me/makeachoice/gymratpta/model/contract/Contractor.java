package me.makeachoice.gymratpta.model.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import me.makeachoice.gymratpta.model.contract.client.ClientColumns;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryColumns;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseColumns;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns;
import me.makeachoice.gymratpta.model.contract.user.UserColumns;

import static android.content.ContentUris.withAppendedId;


/**************************************************************************************************/
/*
 *  Contractor defines table and column names for the gym rat PTA database.
 */
/**************************************************************************************************/

public class Contractor {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "me.makeachoice.gymratpta.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_USER = "user";
    public static final String PATH_APPOINTMENT = "appointment";
    public static final String PATH_CLIENT = "client";
    public static final String PATH_CLIENT_STATS = "clientStats";
    public static final String PATH_CLIENT_NOTES = "clientNotes";
    public static final String PATH_CLIENT_EXERCISE = "clientExercise";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_EXERCISE = "exercise";
    public static final String PATH_ROUTINE = "routine";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  User / Client Inner Classes
 */
/**************************************************************************************************/
    /*
     * UserEntry - GymRat app users
     */
    public static final class UserEntry extends UserColumns implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        //"content://CONTENT_AUTHORITY/user/[_id]
        public static Uri buildUserUri(long id) {
            return withAppendedId(CONTENT_URI, id);
        }

        //"content://CONTENT_AUTHORITY/user/[uid]
        public static Uri buildUserByUID(String uid) {
            return CONTENT_URI.buildUpon().appendPath(uid).build();
        }

        //"content://CONTENT_AUTHORITY/user/[uid]
        public static String getUIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /*
     * ClientEntry - GymRat app client
     */
    public static final class ClientEntry extends ClientColumns implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLIENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT;

        //"content://CONTENT_AUTHORITY/client/[_id]
        public static Uri buildClientUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        //"content://CONTENT_AUTHORITY/client/[uid]
        public static Uri buildClientByUID(String uid) {
            return CONTENT_URI.buildUpon().appendPath(uid).build();
        }

        //"content://CONTENT_AUTHORITY/client/[uid]/fkey/[fKey]
        public static Uri buildClientByFirebaseKey(String uid, String fKey) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_FKEY).appendPath(fKey).build();
        }

        //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
        public static Uri buildClientByStatus(String uid, String status) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CLIENT_STATUS).appendPath(status).build();
        }

        //"content://CONTENT_AUTHORITY/client/[uid]/....
        public static String getUIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        //"content://CONTENT_AUTHORITY/client/[uid]/fkey/[fKey]
        public static String getFirebaseKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
        public static String getStatusFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Exercise Inner Classes
 *          CategoryEntry - user defined exercise categories
 *          ExerciseEntry - user defined exercises
 *          RoutineEntry - user defined exercise routines
 */
/**************************************************************************************************/
    /*
     * CategoryEntry - user defined exercise categories
     */
    public static final class CategoryEntry extends CategoryColumns implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;


        //"content://CONTENT_AUTHORITY/category/[_id]
        public static Uri buildCategoryUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        //"content://CONTENT_AUTHORITY/category/[uid]
        public static Uri buildCategoryByUID(String uid) {
            return CONTENT_URI.buildUpon().appendPath(uid).build();
        }

        //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fKey]
        public static Uri buildCategoryByFirebaseKey(String uid, String fKey) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_FKEY).appendPath(fKey).build();
        }

        //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
        public static Uri buildCategoryByName(String uid, String name) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CATEGORY_NAME).appendPath(name).build();
        }

        //"content://CONTENT_AUTHORITY/category/[uid]/....
        public static String getUIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fKey]
        public static String getFirebaseKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
        public static String getCategoryNameFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

    }

    /*
     * ExerciseEntry - user defined exercises
     */
    public static final class ExerciseEntry extends ExerciseColumns implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;


        //"content://CONTENT_AUTHORITY/exercise/[_id]
        public static Uri buildExerciseUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]
        public static Uri buildExerciseByUID(String uid) {
            return CONTENT_URI.buildUpon().appendPath(uid).build();
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/category_key/[categoryKey]
        public static Uri buildExerciseByCategoryKey(String uid, String categoryKey) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_CATEGORY_KEY).appendPath(categoryKey).build();
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/fkey/[fKey]
        public static Uri buildExerciseByFirebaseKey(String uid, String fKey) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_FKEY).appendPath(fKey).build();
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/[categoryKey]/exercise_name/[exerciseName]
        public static Uri buildExerciseByName(String uid, String categoryKey, String name) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(categoryKey).
                    appendPath(COLUMN_EXERCISE_NAME).appendPath(name).build();
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/....
        public static String getUIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/category_key/[categoryKey]
        public static String getCategoryKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/fkey/[fKey]
        public static String getFirebaseKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        //"content://CONTENT_AUTHORITY/exercise/[uid]/[categoryKey]/exercise_name/[exerciseName]
        public static String getCategoryKeyFromExerciseNameUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
        //"content://CONTENT_AUTHORITY/exercise/[uid]/[categoryKey]/exercise_name/[exerciseName]
        public static String getNameFromExerciseNameUri(Uri uri) {
            return uri.getPathSegments().get(4);
        }
    }

    /*
     * RoutineEntry - user defined exercise routines
     */
    public static final class RoutineEntry extends RoutineColumns implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTINE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE;


        public static Uri buildRoutineUri(long id) {
            return withAppendedId(CONTENT_URI, id);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Exercise Inner Classes
 */
/**************************************************************************************************/

}
