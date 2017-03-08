package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameContract;
import me.makeachoice.gymratpta.model.db.DBHelper;


public class RoutineNameQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //routineNameQueryBuilder - sqlite query build used to access routine name table
    private static final SQLiteQueryBuilder routineNameQueryBuilder;

    static{
        //initialize routineNameQueryBuilder
        routineNameQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        routineNameQueryBuilder.setTables(RoutineNameContract.TABLE_NAME);
    }

    //query selection - routineName.uid = ?
    public static final String uidSelection =
            RoutineNameContract.TABLE_NAME + "." + RoutineNameContract.COLUMN_UID + " = ? ";

    //query selection - routineName.uid = ? AND routine_name = ?
    public static final String routineNameSelection =
            RoutineNameContract.TABLE_NAME+
                    "." + RoutineNameContract.COLUMN_UID + " = ? AND " +
                    RoutineNameContract.COLUMN_ROUTINE_NAME + " = ? ";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getRoutineName(...) - get all routine names from table
 *      Cursor getRoutineNameByUId(...) - get routine with order by
 *      Cursor getRoutineNameByName(...) - get routine by routine name
 *      Cursor getRoutineNameExerciseByOrderNumber(...) - get routine exercise by order number
 */
/**************************************************************************************************/
    /*
     * Cursor getRoutineName(...) - get all routine names from table
     */
    public static Cursor getRoutineName(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routineName/[uid]/....
        String uid = RoutineNameContract.getUIdFromUri(uri);

        //query routine table
        return RoutineNameQueryHelper.routineNameQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //selection - routineName.uid = ?
                RoutineNameQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getRoutineNameByUId(...) - get routine with order by
     */
    public static Cursor getRoutineNameByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routine/[uid]
        String uid = RoutineNameContract.getUIdFromUri(uri);

        //query from user table
        return RoutineNameQueryHelper.routineNameQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - routineName.uid = ?
                RoutineNameQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getRoutineNameByName(...) - get routine by routine name
     */
    public static Cursor getRoutineNameByName(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routineName/[uid]/routine_name/[name]
        String uid = RoutineNameContract.getUIdFromUri(uri);
        String routineName = RoutineNameContract.getRoutineNameFromUri(uri);

        //query from user table
        return RoutineNameQueryHelper.routineNameQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - routineName.uid = ? AND routine_name = ?
                RoutineNameQueryHelper.routineNameSelection,
                new String[]{uid, routineName},
                null,
                null,
                sortOrder
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertRoutineName(...) - insert routine name into database
 *      int deleteRoutineName(...) - delete routine name from database
 *      int updateRoutineName(...) - update routine name in database
 */
/**************************************************************************************************/
    /*
     * Uri insertRoutineName(...) - insert routine name into database
     */
    public static Uri insertRoutineName(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(RoutineNameContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return RoutineNameContract.buildRoutineNameUri(_id);
    }

    /*
     * int deleteRoutineName(...) - delete routine name from database
     */
    public static int deleteRoutineName(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(RoutineNameContract.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateRoutineName(...) - update user in database
     */
    public static int updateRoutineName(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                    String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(RoutineNameContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/

}
