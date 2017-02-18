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
package me.makeachoice.gymratpta.controller.modelside.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import me.makeachoice.gymratpta.controller.modelside.query.AppointmentQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.CategoryQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.ClientQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.ExerciseQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineNameQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.UserQueryHelper;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.db.DBHelper;

import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_DAY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_WITH_STATUS;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_CATEGORY_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_EXERCISE_WITH_ORDER_NUMBER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_WITH_ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.USER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.USER_WITH_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.dbUriMatcher;

/**************************************************************************************************/
/*
 *  GymRatProvider content provider for user info
 */
/**************************************************************************************************/

public class GymRatProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private DBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return UriTypeHelper.getType(uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (dbUriMatcher.match(uri)) {
            case USER:
                retCursor = UserQueryHelper.getUsers(mOpenHelper, uri, projection, sortOrder);
                break;
            case USER_WITH_KEY:
                retCursor = UserQueryHelper.getUserByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case CLIENT:
                retCursor = ClientQueryHelper.getClients(mOpenHelper, uri, projection, sortOrder);
                break;
            case CLIENT_WITH_UID:
                retCursor = ClientQueryHelper.getClientByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case CLIENT_WITH_FKEY:
                retCursor = ClientQueryHelper.getClientByFKey(mOpenHelper, uri, projection, sortOrder);
                break;
            case CLIENT_WITH_STATUS:
                retCursor = ClientQueryHelper.getClientByStatus(mOpenHelper, uri, projection, sortOrder);
                break;
            case CATEGORY:
                retCursor = CategoryQueryHelper.getCategories(mOpenHelper, uri, projection, sortOrder);
                break;
            case CATEGORY_WITH_UID:
                retCursor = CategoryQueryHelper.getCategoryByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case CATEGORY_WITH_FKEY:
                retCursor = CategoryQueryHelper.getCategoryByFKey(mOpenHelper, uri, projection, sortOrder);
                break;
            case CATEGORY_WITH_NAME:
                retCursor = CategoryQueryHelper.getCategoryByName(mOpenHelper, uri, projection, sortOrder);
                break;
            case EXERCISE:
                retCursor = ExerciseQueryHelper.getExercises(mOpenHelper, uri, projection, sortOrder);
                break;
            case EXERCISE_WITH_UID:
                retCursor = ExerciseQueryHelper.getExerciseByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case EXERCISE_WITH_CATEGORY_KEY:
                retCursor = ExerciseQueryHelper.getExerciseByCategory(mOpenHelper, uri, projection, sortOrder);
                break;
            case EXERCISE_WITH_FKEY:
                retCursor = ExerciseQueryHelper.getExerciseByFKey(mOpenHelper, uri, projection, sortOrder);
                break;
            case EXERCISE_WITH_NAME:
                retCursor = ExerciseQueryHelper.getExerciseByName(mOpenHelper, uri, projection, sortOrder);
                break;
            case ROUTINE:
                retCursor = RoutineQueryHelper.getRoutine(mOpenHelper, uri, projection, sortOrder);
                break;
            case ROUTINE_WITH_UID:
                retCursor = RoutineQueryHelper.getRoutineByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case ROUTINE_WITH_ROUTINE_NAME:
                retCursor = RoutineQueryHelper.getRoutineByRoutineName(mOpenHelper, uri, projection, sortOrder);
                break;
            case ROUTINE_EXERCISE_WITH_ORDER_NUMBER:
                retCursor = RoutineQueryHelper.getRoutineExerciseByOrderNumber(mOpenHelper, uri, projection);
                break;
            case ROUTINE_NAME:
                retCursor = RoutineNameQueryHelper.getRoutineName(mOpenHelper, uri, projection, sortOrder);
                break;
            case ROUTINE_NAME_WITH_UID:
                retCursor = RoutineNameQueryHelper.getRoutineNameByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case ROUTINE_NAME_WITH_NAME:
                retCursor = RoutineNameQueryHelper.getRoutineNameByName(mOpenHelper, uri, projection, sortOrder);
                break;
            case APPOINTMENT_WITH_UID:
                retCursor = AppointmentQueryHelper.getAppointmentByUId(mOpenHelper, uri, projection, sortOrder);
                break;
            case APPOINTMENT_WITH_DAY:
                retCursor = AppointmentQueryHelper.getAppointmentByDate(mOpenHelper, uri, projection, sortOrder);
                break;
            case APPOINTMENT_WITH_CLIENT_KEY:
                retCursor = AppointmentQueryHelper.getAppointmentByClientKey(mOpenHelper, uri, projection, sortOrder);
                break;
            case APPOINTMENT_WITH_FKEY:
                retCursor = AppointmentQueryHelper.getAppointmentByFKey(mOpenHelper, uri, projection, sortOrder);
                break;
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
        //open sqlite database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        //get uri matcher
        final int match = dbUriMatcher.match(uri);

        //uri to be returned for validation
        Uri returnUri = null;

        switch (match) {
            case USER:
                returnUri = UserQueryHelper.insertUser(db, values);
                break;
            case CLIENT:
                returnUri = ClientQueryHelper.insertClient(db, values);
                break;
            case CATEGORY:
                returnUri = CategoryQueryHelper.insertCategory(db, values);
                break;
            case EXERCISE:
                returnUri = ExerciseQueryHelper.insertExercise(db, values);
                break;
            case ROUTINE:
                returnUri = RoutineQueryHelper.insertRoutine(db, values);
                break;
            case ROUTINE_NAME:
                returnUri = RoutineNameQueryHelper.insertRoutineName(db, values);
                break;
            case APPOINTMENT:
                returnUri = AppointmentQueryHelper.insertAppointment(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(returnUri != null){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    public void insertFirebase(Uri uri, ContentValues values){

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //open database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        //get uri matcher
        final int match = dbUriMatcher.match(uri);

        //number of rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case USER:
                rowsDeleted = UserQueryHelper.deleteUser(db, uri, selection, selectionArgs);
                break;
            case CLIENT:
                rowsDeleted = ClientQueryHelper.deleteClient(db, uri, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = CategoryQueryHelper.deleteCategory(db, uri, selection, selectionArgs);
                break;
            case EXERCISE:
                rowsDeleted = ExerciseQueryHelper.deleteExercise(db, uri, selection, selectionArgs);
                break;
            case ROUTINE:
                rowsDeleted = RoutineQueryHelper.deleteRoutine(db, uri, selection, selectionArgs);
                break;
            case ROUTINE_NAME:
                rowsDeleted = RoutineNameQueryHelper.deleteRoutineName(db, uri, selection, selectionArgs);
                break;
            case APPOINTMENT:
                rowsDeleted = AppointmentQueryHelper.deleteAppointment(db, uri, selection, selectionArgs);
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
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = dbUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USER:
                rowsUpdated = UserQueryHelper.updateUser(db, uri, values, selection, selectionArgs);
                break;
            case CLIENT:
                rowsUpdated = ClientQueryHelper.updateClient(db, uri, values, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsUpdated = CategoryQueryHelper.updateCategory(db, uri, values, selection, selectionArgs);
                break;
            case EXERCISE:
                rowsUpdated = ExerciseQueryHelper.updateExercise(db, uri, values, selection, selectionArgs);
                break;
            case ROUTINE:
                rowsUpdated = RoutineQueryHelper.updateRoutine(db, uri, values, selection, selectionArgs);
                break;
            case ROUTINE_NAME:
                rowsUpdated = RoutineNameQueryHelper.updateRoutineName(db, uri, values, selection, selectionArgs);
                break;
            case APPOINTMENT:
                rowsUpdated = AppointmentQueryHelper.updateAppointment(db, uri, values, selection, selectionArgs);
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
        final int match = dbUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case USER:
                db.beginTransaction();
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
            case CLIENT:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Contractor.ClientEntry.TABLE_NAME, null, value);
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
            case CATEGORY:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Contractor.CategoryEntry.TABLE_NAME, null, value);
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
            case EXERCISE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Contractor.ExerciseEntry.TABLE_NAME, null, value);
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