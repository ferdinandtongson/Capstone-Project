package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
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
        clientRoutineQueryBuilder.setTables(ClientRoutineContract.TABLE_NAME);
    }

    //query selection - clientRoutine.uid = ?
    public static final String uidSelection =
            ClientRoutineContract.TABLE_NAME + "." + ClientRoutineContract.COLUMN_UID + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            ClientRoutineContract.TABLE_NAME+
                    "." + ClientRoutineContract.COLUMN_UID + " = ? AND " +
                    ClientRoutineContract.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ? AND timestamp = ?
    public static final String timestampSelection =
            ClientRoutineContract.TABLE_NAME+
                    "." + ClientRoutineContract.COLUMN_UID + " = ? AND " +
                    ClientRoutineContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ClientRoutineContract.COLUMN_TIMESTAMP + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ? AND exercise = ? AND timestamp = ?
    public static final String exerciseSelection =
            ClientRoutineContract.TABLE_NAME+
                    "." + ClientRoutineContract.COLUMN_UID + " = ? AND " +
                    ClientRoutineContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ClientRoutineContract.COLUMN_EXERCISE + " = ? AND " +
                    ClientRoutineContract.COLUMN_TIMESTAMP + " = ? ";

    //query selection - clientRoutine.uid = ? AND client_key = ? AND order_number = ? AND timestamp = ?
    public static final String orderNumberSelection =
            ClientRoutineContract.TABLE_NAME+
                    "." + ClientRoutineContract.COLUMN_UID + " = ? AND " +
                    ClientRoutineContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ClientRoutineContract.COLUMN_ORDER_NUMBER + " = ? AND " +
                    ClientRoutineContract.COLUMN_TIMESTAMP + " = ? ";

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
        String uid = ClientRoutineContract.getUIdFromUri(uri);

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
        String uid = ClientRoutineContract.getUIdFromUri(uri);
        String clientKey = ClientRoutineContract.getClientKeyFromUri(uri);

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
     * Cursor getClientRoutineByTimestamp(...) - get by timestamp
     */
    public static Cursor getClientRoutineByTimestamp(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/timestamp/[timestamp]
        String uid = ClientRoutineContract.getUIdFromUri(uri);
        String clientKey = ClientRoutineContract.getClientKeyFromTimestampUri(uri);
        String timestamp = ClientRoutineContract.getTimestampFromTimestampUri(uri);

        //query from table
        return ClientRoutineQueryHelper.clientRoutineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientRoutine.uid = ? AND client_key = ? AND timestamp = ?
                ClientRoutineQueryHelper.timestampSelection,
                new String[]{uid, clientKey, timestamp},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientRoutineByTimestampExercise(...) - get by timestamp and exercise
     */
    public static Cursor getClientRoutineByTimestampExercise(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/exercise/[exercise]
        String uid = ClientRoutineContract.getUIdFromUri(uri);
        String clientKey = ClientRoutineContract.getClientKeyFromExerciseUri(uri);
        String timestamp = ClientRoutineContract.getTimestampFromExerciseUri(uri);
        String exercise = ClientRoutineContract.getExerciseFromExerciseUri(uri);

        //query from table
        return ClientRoutineQueryHelper.clientRoutineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientRoutine.uid = ? AND client_key = ? AND exercise = ? AND timestamp = ?
                ClientRoutineQueryHelper.exerciseSelection,
                new String[]{uid, clientKey, exercise, timestamp},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientRoutineByTimestampOrderNumber(...) - get by timestamp and order number
     */
    public static Cursor getClientRoutineByTimestampOrderNumber(DBHelper dbHelper, Uri uri,
                                                                String[] projection, String sortOrder) {
        //content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/order_number/[orderNumber]
        String uid = ClientRoutineContract.getUIdFromUri(uri);
        String clientKey = ClientRoutineContract.getClientKeyFromOrderNumberUri(uri);
        String timestamp = ClientRoutineContract.getTimestampFromOrderNumberUri(uri);
        String orderNumber = ClientRoutineContract.getOrderNumberFromOrderNumberUri(uri);

        //query from table
        return ClientRoutineQueryHelper.clientRoutineQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientRoutine.uid = ? AND client_key = ? AND order_number = ? AND timestamp = ?
                ClientRoutineQueryHelper.orderNumberSelection,
                new String[]{uid, clientKey, orderNumber, timestamp},
                null,
                null,
                sortOrder
        );
    }

//content://CONTENT_AUTHORITY/clientRoutine/[uid]/[clientKey]/[timestamp]/order_number/[orderNumber]
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
            _id = db.insert(ClientRoutineContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return ClientRoutineContract.buildClientRoutineUri(_id);
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
            rowsDeleted = db.delete(ClientRoutineContract.TABLE_NAME, selection, selectionArgs);
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
            rowsUpdated = db.update(ClientRoutineContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/

}
