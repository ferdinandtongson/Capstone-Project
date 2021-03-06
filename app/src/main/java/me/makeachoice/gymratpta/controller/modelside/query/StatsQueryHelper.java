package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  StatsQueryHelper helps build queries to stats table
 */
/**************************************************************************************************/

public class StatsQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //statsQueryBuilder - sqlite query build used to access stats table
    private static final SQLiteQueryBuilder statsQueryBuilder;

    static{
        //initialize statsQueryBuilder
        statsQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query stats table
        statsQueryBuilder.setTables(StatsContract.TABLE_NAME);
    }

    //query selection - stats.uid = ?
    public static final String uidSelection =
            StatsContract.TABLE_NAME + "." + StatsContract.COLUMN_UID + " = ? ";

    //query selection - stats.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            StatsContract.TABLE_NAME+
                    "." + StatsContract.COLUMN_UID + " = ? AND " +
                    StatsContract.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - stats.uid = ? AND client_key = ? AND timestamp = ?
    public static final String timestampSelection =
            StatsContract.TABLE_NAME+
                    "." + StatsContract.COLUMN_UID + " = ? AND " +
                    StatsContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    StatsContract.COLUMN_TIMESTAMP + " = ? ";

    //query selection - stats.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
    public static final String clientKeyDateTimeSelection =
            StatsContract.TABLE_NAME+
                    "." + StatsContract.COLUMN_UID + " = ? AND " +
                    StatsContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    StatsContract.COLUMN_APPOINTMENT_DATE + " = ? AND " +
                    StatsContract.COLUMN_APPOINTMENT_TIME + " = ? ";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getStatsByUId(...) - get stats of user
 *      Cursor getStatsByClientKey(...) - get stats by client key
 *      Cursor getStatsByDate(...) - get stats by date
 *      Cursor getStatsByDateTime(...) - get stats by date and time
 */
/**************************************************************************************************/
    /*
     * Cursor getStatsByUId(...) - get stats of user
     */
    public static Cursor getStatsByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientStats/[uid]
        String uid = StatsContract.getUIdFromUri(uri);

        //query from user table
        return StatsQueryHelper.statsQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientStats.uid = ?
                StatsQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getStatsByClientKey(...) - get stats by client key
     */
    public static Cursor getStatsByClientKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientStats/[uid]/client_key/[clientKey]
        String uid = StatsContract.getUIdFromUri(uri);
        String clientKey = StatsContract.getClientKeyFromUri(uri);

        //query from table
        return StatsQueryHelper.statsQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientStats.uid = ? AND client_key = ?
                StatsQueryHelper.clientKeySelection,
                new String[]{uid, clientKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getStatsByTimestamp(...) - get stats by timestamp
     */
    public static Cursor getStatsByTimestamp(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/timestamp/[timestamp]
        String uid = StatsContract.getUIdFromUri(uri);
        String clientKey = StatsContract.getClientKeyFromTimestampUri(uri);
        String appointmentDate = StatsContract.getTimestampFromTimestampUri(uri);

        //query from table
        return StatsQueryHelper.statsQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientStats.uid = ? AND client_key = ? AND timestamp = ?
                StatsQueryHelper.timestampSelection,
                new String[]{uid, clientKey, appointmentDate},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getStatsByDateTime(...) - get stats by date and time
     */
    public static Cursor getStatsByDateTime(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientStats/[uid]/[clientKey]/[appointmentDate]/[appointmentTime]
        String uid = StatsContract.getUIdFromUri(uri);
        String clientKey = StatsContract.getClientKeyFromDateTimeUri(uri);
        String appointmentDate = StatsContract.getDateFromDateTimeUri(uri);
        String appointmentTime = StatsContract.getTimeFromDateTimeUri(uri);

        //query from table
        return StatsQueryHelper.statsQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientStats.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
                StatsQueryHelper.clientKeyDateTimeSelection,
                new String[]{uid, clientKey, appointmentDate, appointmentTime},
                null,
                null,
                sortOrder
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertStats(...) - insert stats into database
 *      int deleteStats(...) - delete stats from database
 *      int updateStats(...) - update stats in database
 */
/**************************************************************************************************/
    /*
     * Uri insertStats(...) - insert stats into database
     */
    public static Uri insertStats(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(StatsContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return StatsContract.buildStatsUri(_id);
    }

    /*
     * int deleteStats(...) - delete stats from database
     */
    public static int deleteStats(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(StatsContract.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateStats(...) - update stats in database
     */
    public static int updateStats(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                  String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(StatsContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/
}
