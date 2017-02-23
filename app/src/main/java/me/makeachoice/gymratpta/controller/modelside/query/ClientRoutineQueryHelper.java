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
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  ClientRoutineQueryHelper helps build queries to client routine table
 */
/**************************************************************************************************/

public class ClientRoutineQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //clientRoutineQueryBuilder - sqlite query build used to access client routine table
    private static final SQLiteQueryBuilder clientRoutineQueryBuilder;

    static{
        //initialize clientRoutineQueryBuilder
        clientRoutineQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query clientRoutine table
        clientRoutineQueryBuilder.setTables(Contractor.ClientRoutineEntry.TABLE_NAME);
    }

    //query selection - clientRoutine.uid = ?
    public static final String uidSelection =
            Contractor.ClientRoutineEntry.TABLE_NAME + "." + Contractor.ClientRoutineEntry.COLUMN_UID + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            Contractor.ClientRoutineEntry.TABLE_NAME+
                    "." + Contractor.ClientRoutineEntry.COLUMN_UID + " = ? AND " +
                    Contractor.ClientRoutineEntry.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ? AND appointment_date = ?
    public static final String dateSelection =
            Contractor.ClientRoutineEntry.TABLE_NAME+
                    "." + Contractor.ClientRoutineEntry.COLUMN_UID + " = ? AND " +
                    Contractor.ClientRoutineEntry.COLUMN_CLIENT_KEY + " = ? AND " +
                    Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_DATE + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
    public static final String clientKeyDateTimeSelection =
            Contractor.ClientRoutineEntry.TABLE_NAME+
                    "." + Contractor.ClientRoutineEntry.COLUMN_UID + " = ? AND " +
                    Contractor.ClientRoutineEntry.COLUMN_CLIENT_KEY + " = ? AND " +
                    Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_DATE + " = ? AND " +
                    Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_TIME + " = ? ";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getClientRoutineByUId(...) - get stats of user
 *      Cursor getClientRoutineByClientKey(...) - get stats by client key
 *      Cursor getClientRoutineByDate(...) - get stats by date
 *      Cursor getClientRoutineByDateTime(...) - get stats by date and time
 */
/**************************************************************************************************/
    /*
     * Cursor getClientRoutineByUId(...) - get client routines of user
     */
    public static Cursor getClientRoutineByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]
        String uid = Contractor.ClientRoutineEntry.getUIdFromUri(uri);

        //query from client routine table
        return ClientRoutineQueryHelper.clientRoutineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientRoutine.uid = ?
                ClientRoutineQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientRoutineByClientKey(...) - get notes by client key
     */
    public static Cursor getClientRoutineByClientKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/client_key/[clientKey]
        String uid = Contractor.ClientRoutineEntry.getUIdFromUri(uri);
        String clientKey = Contractor.ClientRoutineEntry.getClientKeyFromUri(uri);

        //query from table
        return ClientRoutineQueryHelper.clientRoutineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientRoutine.uid = ? AND client_key = ?
                ClientRoutineQueryHelper.clientKeySelection,
                new String[]{uid, clientKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientRoutineByDateTime(...) - get notes by date and time
     */
    public static Cursor getClientRoutineByDateTime(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[appointmentDate]/appointment_time/[appointmentTime]
        String uid = Contractor.ClientRoutineEntry.getUIdFromUri(uri);
        String clientKey = Contractor.ClientRoutineEntry.getClientKeyFromDateTimeUri(uri);
        String appointmentDate = Contractor.ClientRoutineEntry.getDateFromDateTimeUri(uri);
        String appointmentTime = Contractor.ClientRoutineEntry.getTimeFromDateTimeUri(uri);

        //query from table
        return ClientRoutineQueryHelper.clientRoutineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientRoutine.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
                ClientRoutineQueryHelper.clientKeyDateTimeSelection,
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
 *      Uri insertClientRoutine(...) - insert stats into database
 *      int deleteClientRoutine(...) - delete stats from database
 *      int updateClientRoutine(...) - update stats in database
 */
/**************************************************************************************************/
    /*
     * Uri insertClientRoutine(...) - insert stats into database
     */
    public static Uri insertClientRoutine(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(Contractor.ClientRoutineEntry.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return Contractor.ClientRoutineEntry.buildClientRoutineUri(_id);
    }

    /*
     * int deleteClientRoutine(...) - delete stats from database
     */
    public static int deleteClientRoutine(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(Contractor.ClientRoutineEntry.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateClientRoutine(...) - update stats in database
     */
    public static int updateClientRoutine(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                  String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(Contractor.ClientRoutineEntry.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/

}
