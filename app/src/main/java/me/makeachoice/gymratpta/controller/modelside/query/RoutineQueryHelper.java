package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  RoutineQueryHelper helps build queries to the routine table
 */
/**************************************************************************************************/

public class RoutineQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //routineQueryBuilder - sqlite query build used to access routine table
    private static final SQLiteQueryBuilder routineQueryBuilder;

    static{
        //initialize routineQueryBuilder
        routineQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        routineQueryBuilder.setTables(Contractor.RoutineEntry.TABLE_NAME);
    }

    //query selection - routine.uid = ?
    public static final String uidSelection =
            Contractor.RoutineEntry.TABLE_NAME + "." + Contractor.RoutineEntry.COLUMN_UID + " = ? ";

    //query selection - routine.uid = ? AND routine_name = ?
    public static final String routineNameSelection =
            Contractor.RoutineEntry.TABLE_NAME+
                    "." + Contractor.RoutineEntry.COLUMN_UID + " = ? AND " +
                    Contractor.RoutineEntry.COLUMN_ROUTINE_NAME + " = ? ";

    //query selection - routine.uid = ? AND routine_name = ? AND order_number = ?
    public static final String orderNumberSelection =
            Contractor.RoutineEntry.TABLE_NAME+
                    "." + Contractor.RoutineEntry.COLUMN_UID + " = ? AND " +
                    Contractor.RoutineEntry.COLUMN_ROUTINE_NAME + " = ? AND " +
                    Contractor.RoutineEntry.COLUMN_ORDER_NUMBER + " = ? ";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getRoutine(...) - get all routine from table
 *      Cursor getRoutineByUId(...) - get routine with order by
 *      Cursor getRoutineByRoutineName(...) - get routine by routine name
 *      Cursor getRoutineExerciseByOrderNumber(...) - get routine exercise by order number
 */
/**************************************************************************************************/
    /*
     * Cursor getRoutine(...) - get all routine of user from routine table
     */
    public static Cursor getRoutine(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routine/[uid]/....
        String uid = Contractor.RoutineEntry.getUIdFromUri(uri);

        //query routine table
        return RoutineQueryHelper.routineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //selection - routine.uid = ?
                RoutineQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getRoutineByUId(...) - get routine with order by
     */
    public static Cursor getRoutineByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routine/[uid]
        String uid = Contractor.RoutineEntry.getUIdFromUri(uri);

        //query from user table
        return RoutineQueryHelper.routineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - routine.uid = ?
                RoutineQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getRoutineByRoutineName(...) - get routine by routine name
     */
    public static Cursor getRoutineByRoutineName(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]
        String uid = Contractor.RoutineEntry.getUIdFromUri(uri);
        String routineName = Contractor.RoutineEntry.getRoutineNameFromUri(uri);

        //query from user table
        return RoutineQueryHelper.routineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - routine.uid = ? AND routine_name = ?
                RoutineQueryHelper.routineNameSelection,
                new String[]{uid, routineName},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getRoutineExerciseByOrderNumber(...) - get routine exercise by order number
     */
    public static Cursor getRoutineExerciseByOrderNumber(DBHelper dbHelper, Uri uri, String[] projection) {
        //"content://CONTENT_AUTHORITY/routine/[uid]/[routineName]/[orderNumber]
        String uid = Contractor.RoutineEntry.getUIdFromUri(uri);
        String routineName = Contractor.RoutineEntry.getRoutineNameFromUri(uri);
        String orderNumber = Contractor.RoutineEntry.getOrderNumberFromUri(uri);

        //query from user table
        return RoutineQueryHelper.routineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - routine.uid = ? AND routine_name = ? AND order_number = ?
                RoutineQueryHelper.orderNumberSelection,
                new String[]{uid, routineName, orderNumber},
                null,
                null,
                null
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertRoutine(...) - insert routine into database
 *      int deleteRoutine(...) - delete routine from database
 *      int updateRoutine(...) - update routine in database
 */
/**************************************************************************************************/
    /*
     * Uri insertRoutine(...) - insert routine into database
     */
    public static Uri insertRoutine(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(Contractor.RoutineEntry.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            Log.d("Choice", "     exception: " + mSQLException.toString());
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
                Log.d("Choice", "     constraint exception");
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
                Log.d("Choice", "     mismatch exception");
            }
            throw mSQLException;
        }

        return Contractor.RoutineEntry.buildRoutineUri(_id);
    }

    /*
     * int deleteRoutine(...) - delete routine from database
     */
    public static int deleteRoutine(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(Contractor.RoutineEntry.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateRoutine(...) - update user in database
     */
    public static int updateRoutine(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                     String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(Contractor.RoutineEntry.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
