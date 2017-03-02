package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  ClientExerciseQueryHelper helps build queries to client exercise table
 */
/**************************************************************************************************/

public class ClientExerciseQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //clientExerciseQueryBuilder - sqlite query build used to access client exercise table
    private static final SQLiteQueryBuilder clientExerciseQueryBuilder;

    static{
        //initialize clientExerciseQueryBuilder
        clientExerciseQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query clientExercise table
        clientExerciseQueryBuilder.setTables(ClientExerciseContract.TABLE_NAME);
    }

    //query selection - clientExercise.uid = ?
    public static final String uidSelection =
            ClientExerciseContract.TABLE_NAME + "." + ClientExerciseContract.COLUMN_UID + " = ? ";

    //query selection - clientExercise.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            ClientExerciseContract.TABLE_NAME+
                    "." + ClientExerciseContract.COLUMN_UID + " = ? AND " +
                    ClientExerciseContract.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - clientExercise.uid = ? AND client_key = ? AND timestamp = ?
    public static final String clientKeyTimestampSelection =
            ClientExerciseContract.TABLE_NAME+
                    "." + ClientExerciseContract.COLUMN_UID + " = ? AND " +
                    ClientExerciseContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ClientExerciseContract.COLUMN_TIMESTAMP + " = ? ";

    //query selection - clientExercise.uid = ? AND client_key = ? AND exercise = ?
    public static final String clientKeyExerciseSelection =
            ClientExerciseContract.TABLE_NAME+
                    "." + ClientExerciseContract.COLUMN_UID + " = ? AND " +
                    ClientExerciseContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ClientExerciseContract.COLUMN_EXERCISE + " = ? ";

    //query selection - clientExercise.uid = ? AND client_key = ? AND timestamp = ? AND exercise = ?
    public static final String clientKeyTimestampExerciseSelection =
            ClientExerciseContract.TABLE_NAME+
                    "." + ClientExerciseContract.COLUMN_UID + " = ? AND " +
                    ClientExerciseContract.COLUMN_CLIENT_KEY + " = ? AND " +
                    ClientExerciseContract.COLUMN_TIMESTAMP + " = ? AND " +
                    ClientExerciseContract.COLUMN_EXERCISE + " = ? ";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getClientExerciseByUId(...) - get client exercise of user
 *      Cursor getClientExerciseByClientKey(...) - get client exercise by client key
 *      Cursor getClientExerciseByDateTime(...) - get client exercise by date and time
 */
/**************************************************************************************************/
    /*
     * Cursor getClientExerciseByUId(...) - get client exercise of user
     */
    public static Cursor getClientExerciseByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientExercise/[uid]
        String uid = ClientExerciseContract.getUIdFromUri(uri);

        //query from client routine table
        return ClientExerciseQueryHelper.clientExerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientExercise.uid = ?
                ClientExerciseQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientExerciseByClientKey(...) - get client exercise by client key
     */
    public static Cursor getClientExerciseByClientKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientExercise/[uid]/client_key/[clientKey]
        String uid = ClientExerciseContract.getUIdFromUri(uri);
        String clientKey = ClientExerciseContract.getClientKeyFromUri(uri);

        //query from table
        return ClientExerciseQueryHelper.clientExerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientExercise.uid = ? AND client_key = ?
                ClientExerciseQueryHelper.clientKeySelection,
                new String[]{uid, clientKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientExerciseByTimestamp(...) - get client exercises by date and time
     */
    public static Cursor getClientExerciseByTimestamp(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/timestamp/[timestamp]
        String uid = ClientExerciseContract.getUIdFromUri(uri);
        String clientKey = ClientExerciseContract.getClientKeyFromTimestampUri(uri);
        String timestamp = ClientExerciseContract.getTimestampFromTimestampUri(uri);

        //query from table
        return ClientExerciseQueryHelper.clientExerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientExercise.uid = ? AND client_key = ? AND timestamp = ?
                ClientExerciseQueryHelper.clientKeyTimestampSelection,
                new String[]{uid, clientKey, timestamp},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientExerciseByExercise(...) - get client exercises by exercise
     */
    public static Cursor getClientExerciseByExercise(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientExercise/[uid]/[clientKey]/exercise/[exercise]
        String uid = ClientExerciseContract.getUIdFromUri(uri);
        String clientKey = ClientExerciseContract.getClientKeyFromExerciseUri(uri);
        String exercise = ClientExerciseContract.getExerciseFromExerciseUri(uri);

        //query from table
        return ClientExerciseQueryHelper.clientExerciseQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - clientExercise.uid = ? AND client_key = ? AND exercise = ?
                ClientExerciseQueryHelper.clientKeyExerciseSelection,
                new String[]{uid, clientKey, exercise},
                null,
                null,
                sortOrder
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertClientExercise(...) - insert client exercise into database
 *      int deleteClientExercise(...) - delete client exercise from database
 *      int updateClientExercise(...) - update client exercise in database
 */
/**************************************************************************************************/
    /*
     * Uri insertClientExercise(...) - insert client exercise into database
     */
    public static Uri insertClientExercise(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(ClientExerciseContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return ClientExerciseContract.buildClientExerciseUri(_id);
    }

    /*
     * int deleteClientExercise(...) - delete client exercise from database
     */
    public static int deleteClientExercise(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(ClientExerciseContract.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateClientExercise(...) - update client exercise in database
     */
    public static int updateClientExercise(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                  String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(ClientExerciseContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/

}
