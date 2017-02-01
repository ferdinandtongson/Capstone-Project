/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.makeachoice.gymratpta.controller.modelside.contentprovider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  UserProvider content provider for user info
 */
/**************************************************************************************************/

public class UserProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    //uri matcher id numbers
    static final int USER = 100;
    static final int USER_WITH_KEY = 101;
    static final int USER_WITH_KEY_AND_NAME = 102;

    private static final SQLiteQueryBuilder sUserByUserNameQueryBuilder;

    static{
        sUserByUserNameQueryBuilder = new SQLiteQueryBuilder();
        sUserByUserNameQueryBuilder.setTables(Contractor.UserEntry.TABLE_NAME);

    }

        //selection - user.firebase_key = ?
        private static final String sFirbaseKeySelection =
                Contractor.UserEntry.TABLE_NAME +
                        "." + Contractor.UserEntry.COLUMN_FIREBASE_KEY + " = ? ";

        //user.firebase_key = ? AND user_name = ?
        private static final String sFirebaseKeyAndUserName =
                Contractor.UserEntry.TABLE_NAME +
                        "." + Contractor.UserEntry.COLUMN_FIREBASE_KEY + " = ? AND " +
                        Contractor.UserEntry.COLUMN_USER_NAME + " = ? ";



    private Cursor getUserByFirebaseKey(Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/user/[firebaseKey]/user_name=[userName],firebase_key=[firebaseKey],.....
        String firebaseKey = Contractor.UserEntry.getFirebaseKeyFromUri(uri);

        //query from User.Table
        return sUserByUserNameQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                //selection - user.firebase_key = ?
                sFirbaseKeySelection,
                new String[]{firebaseKey},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getUserByFirebaseKeyAndUserName(Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/user/[firebaseKey]/user_name=[userName],firebase_key=[firebaseKey],.....
        String firebaseKey = Contractor.UserEntry.getFirebaseKeyFromUri(uri);
        String userName = Contractor.UserEntry.getUserNameFromUri(uri);

        //query from User.Table
        return sUserByUserNameQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                //user.firebase_key = ? AND user_name = ?
                sFirebaseKeyAndUserName,
                new String[]{firebaseKey, userName},
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contractor.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, Contractor.PATH_USER, USER);
        matcher.addURI(authority, Contractor.PATH_USER + "/*", USER_WITH_KEY);
        matcher.addURI(authority, Contractor.PATH_USER + "/*/*", USER_WITH_KEY_AND_NAME);
        return matcher;
    }


    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case USER_WITH_KEY_AND_NAME:
                return Contractor.UserEntry.CONTENT_ITEM_TYPE;
            case USER_WITH_KEY:
                return Contractor.UserEntry.CONTENT_ITEM_TYPE;
            case USER:
                return Contractor.UserEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "user/*/*"
            case USER_WITH_KEY_AND_NAME:
            {
                retCursor = getUserByFirebaseKeyAndUserName(uri, projection, sortOrder);
                break;
            }
            // "user/*"
            case USER_WITH_KEY: {
                retCursor = getUserByFirebaseKey(uri, projection, sortOrder);
                break;
            }
            // "user"
            case USER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        Contractor.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case USER: {
                long _id = db.insert(Contractor.UserEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contractor.UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case USER:
                rowsDeleted = db.delete(Contractor.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USER:
                rowsUpdated = db.update(Contractor.UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Contractor.UserEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}