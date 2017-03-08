package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  ExerciseQueryHelper helps in building queries to exercise table
 */
/**************************************************************************************************/

public class ExerciseQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //exerciseQueryBuilder - sqlite query build used to access exercise table
    private static final SQLiteQueryBuilder exerciseQueryBuilder;

    static{
        //initialize exerciseQueryBuilder
        exerciseQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        exerciseQueryBuilder.setTables(ExerciseContract.TABLE_NAME);
    }

    //query selection - exercise.uid = ?
    public static final String uidSelection =
            ExerciseContract.TABLE_NAME + "." + ExerciseContract.COLUMN_UID + " = ? ";

    //query selection - exercise.uid = ? AND category_key = ?
    public static final String categorySelection =
            ExerciseContract.TABLE_NAME+
                    "." + ExerciseContract.COLUMN_UID + " = ? AND " +
                    ExerciseContract.COLUMN_CATEGORY_KEY + " = ? ";

    //query selection - exercise.uid = ? AND fkey = ?
    public static final String fkeySelection =
            ExerciseContract.TABLE_NAME+
                    "." + ExerciseContract.COLUMN_UID + " = ? AND " +
                    ExerciseContract.COLUMN_FKEY + " = ? ";

    //query selection - exercise.uid = ? AND category_key = ? AND exercise_name = ?
    public static final String categoryKeyExerciseSelection =
            ExerciseContract.TABLE_NAME+
                    "." + ExerciseContract.COLUMN_UID + " = ? AND " +
                    ExerciseContract.COLUMN_CATEGORY_KEY + " = ? AND " +
                    ExerciseContract.COLUMN_EXERCISE_NAME + " = ? ";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getExercises(...) - get all exercises of user from exercise table
 *      Cursor getExerciseByCategory(...) - get exercise by category
 *      Cursor getExerciseByFKey(...) - get exercise by firebase key
 *      Cursor getExerciseByName(...) - get exercises by name
 */
/**************************************************************************************************/
    /*
     * Cursor getExercises(...) - get all exercises of user from exercise table
     */
    public static Cursor getExercises(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/exercise/[uid]/....
        String uid = ExerciseContract.getUIdFromUri(uri);

        //query exercise table
        return ExerciseQueryHelper.exerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //selection - exercise.uid = ?
                ExerciseQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getExerciseByUId(...) - get exercise by user id
     */
    public static Cursor getExerciseByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/exercise/[uid]
        String uid = ExerciseContract.getUIdFromUri(uri);

        //query from user table
        return ExerciseQueryHelper.exerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - exercise.uid = ?
                ExerciseQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getExerciseByCategory(...) - get exercise by user id
     */
    public static Cursor getExerciseByCategory(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/exercise/[uid]/category_key/[categoryKey]
        String uid = ExerciseContract.getUIdFromUri(uri);
        String categoryKey = ExerciseContract.getCategoryKeyFromUri(uri);

        //query from user table
        return ExerciseQueryHelper.exerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - exercise.uid = ? AND category_key = ?
                ExerciseQueryHelper.categorySelection,
                new String[]{uid, categoryKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getExerciseByFKey(...) - get exercise by firebase key
     */
    public static Cursor getExerciseByFKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/exercise/[uid]/fkey/[fKey]
        String uid = ExerciseContract.getUIdFromUri(uri);
        String fkey = ExerciseContract.getFirebaseKeyFromUri(uri);

        //query from user table
        return ExerciseQueryHelper.exerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - exercise.uid = ? AND fkey = ?
                ExerciseQueryHelper.fkeySelection,
                new String[]{uid, fkey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getExerciseByName(...) - get exercises by category and name
     */
    public static Cursor getExerciseByName(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/exercise/[uid]/exercise_name/[exerciseName]
        String uid = ExerciseContract.getUIdFromUri(uri);
        String categoryKey = ExerciseContract.getCategoryKeyFromExerciseNameUri(uri);
        String exerciseName = ExerciseContract.getExerciseNameFromExerciseNameUri(uri);

        //query from user table
        return ExerciseQueryHelper.exerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - exercise.uid = ? AND category_key = ? AND exercise_name = ?
                ExerciseQueryHelper.categoryKeyExerciseSelection,
                new String[]{uid, categoryKey, exerciseName},
                null,
                null,
                sortOrder
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertExercise(...) - insert exercise into database
 *      int deleteExercise(...) - delete exercise from database
 *      int updateExercise(...) - update exercise in database
 */
/**************************************************************************************************/
    /*
     * Uri insertExercise(...) - insert exercise into database
     */
    public static Uri insertExercise(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(ExerciseContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return ExerciseContract.buildExerciseUri(_id);
    }

    /*
     * int deleteExercise(...) - delete exercise from database
     */
    public static int deleteExercise(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(ExerciseContract.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateExercise(...) - update user in database
     */
    public static int updateExercise(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                   String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(ExerciseContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
