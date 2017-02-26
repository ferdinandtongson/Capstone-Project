package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  ScheduleQueryHelper helps build queries to the user schedule table
 */
/**************************************************************************************************/


public class ScheduleQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //scheduleQueryBuilder - sqlite query build used to access schedule table
    private static final SQLiteQueryBuilder scheduleQueryBuilder;

    static{
        //initialize scheduleQueryBuilder
        scheduleQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query userSchedule table
        scheduleQueryBuilder.setTables(ScheduleContract.TABLE_NAME);
    }

    //query selection - userSchedule.uid = ?
    public static final String uidSelection =
            ScheduleContract.TABLE_NAME + "." + ScheduleContract.COLUMN_UID + " = ? ";

    //query selection - userSchedule.uid = ? AND timestamp = ?
    public static final String timestampSelection =
            ScheduleContract.TABLE_NAME+
                    "." + ScheduleContract.COLUMN_UID + " = ? AND " +
                    ScheduleContract.COLUMN_TIMESTAMP + " = ? ";

    //query selection - userSchedule.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            ScheduleContract.TABLE_NAME+
                    "." + ScheduleContract.COLUMN_UID + " = ? AND " +
                    ScheduleContract.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - userSchedule.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
    public static final String clientKeyDateTimeSelection =
            ScheduleContract.TABLE_NAME+
                    "." + ScheduleContract.COLUMN_UID + " = ? AND " +
                    ScheduleContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ScheduleContract.COLUMN_APPOINTMENT_DATE + " = ? AND " +
                    ScheduleContract.COLUMN_APPOINTMENT_TIME + " = ? ";

    //"query selection - userSchedule.uid AND between ? and ?";
    public static final String dateRangeSelection =
            ScheduleContract.TABLE_NAME+
                    "." + ScheduleContract.COLUMN_UID + " = ? AND BETWEEN ? AND ?";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getScheduleByUId(...) - get schedule of user
 *      Cursor getScheduleByDate(...) - get schedule by date
 *      Cursor getScheduleByClientKey(...) - get schedule by client key
 */
/**************************************************************************************************/
    /*
     * Cursor getScheduleByUId(...) - get schedule of user
     */
    public static Cursor getScheduleByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/userSchedule/[uid]
        String uid = ScheduleContract.getUIdFromUri(uri);

        //query from userSchedule table
        return ScheduleQueryHelper.scheduleQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - userSchedule.uid = ?
                ScheduleQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getScheduleByTimestamp(...) - get schedule by timestamp
     */
    public static Cursor getScheduleByTimestamp(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/userSchedule/[uid]/timestamp/[timestamp]
        String uid = ScheduleContract.getUIdFromUri(uri);
        String timestamp = ScheduleContract.getTimestampFromUri(uri);

        //query from table
        return ScheduleQueryHelper.scheduleQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - userSchedule.uid = ? AND appointment_date = ?
                ScheduleQueryHelper.timestampSelection,
                new String[]{uid, timestamp},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getScheduleByClientKey(...) - get schedule by client key
     */
    public static Cursor getScheduleByClientKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/userSchedule/[uid]/client_key/[clientKey]
        String uid = ScheduleContract.getUIdFromUri(uri);
        String clientKey = ScheduleContract.getClientKeyFromUri(uri);

        //query from table
        return ScheduleQueryHelper.scheduleQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - userSchedule.uid = ? AND client_key = ?
                ScheduleQueryHelper.clientKeySelection,
                new String[]{uid, clientKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getScheduleByRange(...) - get schedule by range
     */
    public static Cursor getScheduleByRange(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/userSchedule/[uid]/datestamp/[date1]/[date2]
        String uid = ScheduleContract.getUIdFromUri(uri);
        String date1 = ScheduleContract.getClientKeyFromUri(uri);
        String date2 = "";

        //query from table
        return ScheduleQueryHelper.scheduleQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - userSchedule.uid AND between ? and ?";
                ScheduleQueryHelper.clientKeySelection,
                new String[]{uid, date1, date2},
                null,
                null,
                sortOrder
        );
    }



/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertSchedule(...) - insert schedule into database
 *      int deleteSchedule(...) - delete schedule from database
 *      int updateSchedule(...) - update schedule in database
 */
/**************************************************************************************************/
    /*
     * Uri insertSchedule(...) - insert schedule into database
     */
    public static Uri insertSchedule(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(ScheduleContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return ScheduleContract.buildScheduleUri(_id);
    }

    /*
     * int deleteSchedule(...) - delete schedule from database
     */
    public static int deleteSchedule(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(ScheduleContract.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateSchedule(...) - update schedule in database
     */
    public static int updateSchedule(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                    String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(ScheduleContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
