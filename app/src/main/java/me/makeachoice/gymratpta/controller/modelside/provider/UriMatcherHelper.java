package me.makeachoice.gymratpta.controller.modelside.provider;

import android.content.UriMatcher;

import me.makeachoice.gymratpta.model.contract.Contractor;

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
        String fkeyPath = Contractor.PATH_CLIENT + "/*" + Contractor.ClientEntry.COLUMN_FKEY + "/*";
        matcher.addURI(authority, fkeyPath, CLIENT_WITH_FKEY);

        //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
        String statusPath = Contractor.PATH_CLIENT + "/*" + Contractor.ClientEntry.COLUMN_CLIENT_STATUS + "/*";
        matcher.addURI(authority, statusPath, CLIENT_WITH_STATUS);

        addUriCategory(matcher, authority);
        addUriExercise(matcher, authority);

        return matcher;
    }

    private static void addUriCategory(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/category/
        matcher.addURI(authority, Contractor.PATH_CATEGORY, CATEGORY);

        //"content://CONTENT_AUTHORITY/category/[uid]
        String uidPath = Contractor.PATH_CATEGORY + "/*";
        matcher.addURI(authority, uidPath, CATEGORY_WITH_UID);

        //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fkey]
        String fkeyPath = Contractor.PATH_CATEGORY + "/*" + Contractor.CategoryEntry.COLUMN_FKEY + "/*";
        matcher.addURI(authority, fkeyPath, CATEGORY_WITH_FKEY);

        //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
        String namePath = Contractor.PATH_CATEGORY + "/*" + Contractor.CategoryEntry.COLUMN_CATEGORY_NAME + "/*";
        matcher.addURI(authority, namePath, CATEGORY_WITH_NAME);

    }

    private static void addUriExercise(UriMatcher matcher, String authority){
        //"content://CONTENT_AUTHORITY/exercise/
        matcher.addURI(authority, Contractor.PATH_EXERCISE, EXERCISE);

        //"content://CONTENT_AUTHORITY/exercise/[uid]
        String uidPath = Contractor.PATH_EXERCISE + "/*";
        matcher.addURI(authority, uidPath, EXERCISE_WITH_UID);

        //"content://CONTENT_AUTHORITY/exercise/[uid]/category_key/[categoryKey]
        String categoryKeyPath = Contractor.PATH_EXERCISE + "/*" + Contractor.ExerciseEntry.COLUMN_CATEGORY_KEY + "/*";
        matcher.addURI(authority, categoryKeyPath, EXERCISE_WITH_CATEGORY_KEY);

        //"content://CONTENT_AUTHORITY/exercise/[uid]/fkey/[fkey]
        String fkeyPath = Contractor.PATH_EXERCISE + "/*" + Contractor.ExerciseEntry.COLUMN_FKEY + "/*";
        matcher.addURI(authority, fkeyPath, EXERCISE_WITH_FKEY);

        //"content://CONTENT_AUTHORITY/exercise/[uid]/[categoryKey]/exercise_name/[exerciseName]
        String namePath = Contractor.PATH_EXERCISE + "/*/*" + Contractor.ExerciseEntry.COLUMN_EXERCISE_NAME + "/*";
        matcher.addURI(authority, namePath, EXERCISE_WITH_NAME);

    }


}