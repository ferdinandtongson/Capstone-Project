package me.makeachoice.gymratpta.model.contract;

import android.net.Uri;

/**
 * Created by Usuario on 2/26/2017.
 */

public class MyContractor {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    protected static final String CONTENT_AUTHORITY = "me.makeachoice.gymratpta.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    protected static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_SCHEDULE = "userSchedule";
    public static final String PATH_CLIENT = "client";
    public static final String PATH_STATS = "clientStats";
    public static final String PATH_NOTES = "clientNotes";
    public static final String PATH_CLIENT_ROUTINE = "clientRoutine";
    public static final String PATH_CLIENT_EXERCISE = "clientExercise";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_EXERCISE = "exercise";
    public static final String PATH_ROUTINE = "routine";
    public static final String PATH_ROUTINE_NAME = "routineName";

}
