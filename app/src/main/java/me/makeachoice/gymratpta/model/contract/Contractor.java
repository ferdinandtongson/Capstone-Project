package me.makeachoice.gymratpta.model.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameColumns;

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
    public static final String PATH_ROUTINE_NAME = "routineName";


/**************************************************************************************************/


/**************************************************************************************************/
/*
 *  Exercise Inner Classes
 *          CategoryEntry - user defined exercise categories
 *          ExerciseEntry - user defined exercises
 *          RoutineEntry - user defined exercise routines
 *          RoutineNameEntry - user defined routine names
 */
/**************************************************************************************************/

    /*
     * RoutineNameEntry - user defined routine names
     */
    public static class RoutineNameEntry extends RoutineNameColumns implements BaseColumns {

        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTINE_NAME).build();

        public static String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE_NAME;
        public static String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTINE_NAME;


        //"content://CONTENT_AUTHORITY/routineName/[_id]
        public static Uri buildRoutineNameUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        //"content://CONTENT_AUTHORITY/routineName/[uid]
        public static Uri buildRoutineNameByUID(String uid) {
            return CONTENT_URI.buildUpon().appendPath(uid).build();
        }

        //"content://CONTENT_AUTHORITY/routineName/[uid]/routine_name/[name]
        public static Uri buildRoutineNameByName(String uid, String name) {
            return CONTENT_URI.buildUpon().appendPath(uid).appendPath(COLUMN_ROUTINE_NAME).appendPath(name).build();
        }

        //"content://CONTENT_AUTHORITY/routineName/[uid]/....
        public static String getUIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        //"content://CONTENT_AUTHORITY/routineName/[uid]/routine_name/[name]
        public static String getRoutineNameFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }

    }

/**************************************************************************************************/


}
