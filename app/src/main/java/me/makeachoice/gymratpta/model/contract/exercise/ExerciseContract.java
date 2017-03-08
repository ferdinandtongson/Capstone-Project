package me.makeachoice.gymratpta.model.contract.exercise;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.MyContractor;

/**************************************************************************************************/
/*
 *  ExerciseContract are columns used in exercise table in the sqlite database
 */
/**************************************************************************************************/

public class ExerciseContract extends MyContractor implements BaseColumns {

/**************************************************************************************************/
/*
 *  Table and Column Variables
 */
/**************************************************************************************************/

    // Table name
    public static String TABLE_NAME = "exercise";

    //user id
    public static String COLUMN_UID = "uid";

    //category key
    public static String COLUMN_CATEGORY_KEY = "category_key";

    //firebase key
    public static String COLUMN_FKEY = "fkey";

    //exercise name
    public static String COLUMN_EXERCISE_NAME = "exercise_name";

    //exercise category
    public static String COLUMN_EXERCISE_CATEGORY = "exercise_category";

    //primary record data
    public static String COLUMN_RECORD_PRIMARY = "record_primary";

    //secondary record data
    public static String COLUMN_RECORD_SECONDARY = "record_secondary";

    //default sort order
    public static String SORT_ORDER_DEFAULT = COLUMN_EXERCISE_NAME + " ASC";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Projection and Index
 */
/**************************************************************************************************/

    // A "projection" defines the columns that will be returned for each row
    public static String[] PROJECTION_EXERCISE =
            {
                    COLUMN_UID,
                    COLUMN_CATEGORY_KEY,
                    COLUMN_FKEY,
                    COLUMN_EXERCISE_NAME,
                    COLUMN_EXERCISE_CATEGORY,
                    COLUMN_RECORD_PRIMARY,
                    COLUMN_RECORD_SECONDARY
            };

    public static int INDEX_UID = 0;
    public static int INDEX_CATEGORY_KEY = 1;
    public static int INDEX_FKEY = 2;
    public static int INDEX_EXERCISE_NAME = 3;
    public static int INDEX_EXERCISE_CATEGORY = 4;
    public static int INDEX_RECORD_PRIMARY = 5;
    public static int INDEX_RECORD_SECONDARY = 6;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Contract Entries
 */
/**************************************************************************************************/

    public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();

    public static String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISE;
    public static String CONTENT_ITEM_TYPE =
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
    public static Uri buildExerciseByName(String uid, String categoryKey, String exercise) {
        return CONTENT_URI.buildUpon().appendPath(uid).appendPath(categoryKey).
                appendPath(COLUMN_EXERCISE_NAME).appendPath(exercise).build();
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
    public static String getExerciseNameFromExerciseNameUri(Uri uri) {
        return uri.getPathSegments().get(4);
    }

/**************************************************************************************************/

}
