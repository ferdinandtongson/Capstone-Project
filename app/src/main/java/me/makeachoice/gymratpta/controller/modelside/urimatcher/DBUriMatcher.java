package me.makeachoice.gymratpta.controller.modelside.urimatcher;

import android.content.UriMatcher;

import me.makeachoice.gymratpta.model.contract.Contractor;

/**
 * Created by Usuario on 2/2/2017.
 */

public class DBUriMatcher {

    //uri matcher id numbers
    public static final int USER = 100;
    public static final int USER_WITH_KEY = 101;

    public static final int CLIENT = 200;
    public static final int CLIENT_WITH_UID = 201;
    public static final int CLIENT_WITH_FKEY = 202;
    public static final int CLIENT_WITH_STATUS = 203;

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
        matcher.addURI(authority, fkeyPath, CLIENT_WITH_STATUS);

        return matcher;
    }

}
