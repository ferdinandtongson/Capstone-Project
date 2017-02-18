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
 *  ClientQueryHelper helps build queries to the client table
 */
/**************************************************************************************************/

public class ClientQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //clientQueryBuilder - sqlite query build used to access client table
    private static final SQLiteQueryBuilder clientQueryBuilder;

    static{
        //initialize clientQueryBuilder
        clientQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        clientQueryBuilder.setTables(Contractor.ClientEntry.TABLE_NAME);
    }

    //query selection - client.uid = ?
    public static final String uidSelection =
            Contractor.ClientEntry.TABLE_NAME + "." + Contractor.ClientEntry.COLUMN_UID + " = ? ";

    //query selection - client.uid = ? AND fkey = ?
    public static final String fkeySelection =
            Contractor.ClientEntry.TABLE_NAME+
                    "." + Contractor.ClientEntry.COLUMN_UID + " = ? AND " +
                    Contractor.ClientEntry.COLUMN_FKEY + " = ? ";

    //query selection - client.uid = ? AND client_status = ?
    public static final String statusSelection =
            Contractor.ClientEntry.TABLE_NAME+
                    "." + Contractor.ClientEntry.COLUMN_UID + " = ? AND " +
                    Contractor.ClientEntry.COLUMN_CLIENT_STATUS + " = ? ";
    
/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getClients(...) - get all clients of user from client table
 *      Cursor getClientByFKey(...) - get client by firebase key
 *      Cursor getClientByStatus(...) - get clients by status
 */
 /**************************************************************************************************/
    /*
     * Cursor getClients(...) - get all clients of user from client table
     */
     public static Cursor getClients(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
         //"content://CONTENT_AUTHORITY/client/[uid]/....
         String uid = Contractor.ClientEntry.getUIdFromUri(uri);
    
         //query client table
         return ClientQueryHelper.clientQueryBuilder.query(
                 dbHelper.getReadableDatabase(),
                 projection,
                 //selection - client.uid = ?
                 ClientQueryHelper.uidSelection,
                 new String[]{uid},
                 null,
                 null,
                 sortOrder
         );
     }

    /*
     * Cursor getClientByUId(...) - get client by user id
     */
    public static Cursor getClientByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/client/[uid]
        String uid = Contractor.ClientEntry.getUIdFromUri(uri);

        //query from user table
        return ClientQueryHelper.clientQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - client.uid = ?
                ClientQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientByFKey(...) - get client by firebase key
     */
    public static Cursor getClientByFKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/client/[uid]/fkey/[fKey]
        String uid = Contractor.ClientEntry.getUIdFromUri(uri);
        String fkey = Contractor.ClientEntry.getFKeyFromUri(uri);

        //query from user table
        return ClientQueryHelper.clientQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - client.uid = ? AND fkey = ?
                ClientQueryHelper.fkeySelection,
                new String[]{uid, fkey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getClientByStatus(...) - get clients by status
     */
    public static Cursor getClientByStatus(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/client/[uid]/client_status/[status]
        String uid = Contractor.ClientEntry.getUIdFromUri(uri);
        String status = Contractor.ClientEntry.getStatusFromUri(uri);

        //query from user table
        return ClientQueryHelper.clientQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - client.uid = ? AND status = ?
                ClientQueryHelper.statusSelection,
                new String[]{uid, status},
                null,
                null,
                sortOrder
        );
    }
    
/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertClient(...) - insert client into database
 *      int deleteClient(...) - delete client from database
 *      int updateClient(...) - update client in database
 */
    /**************************************************************************************************/
    /*
     * Uri insertClient(...) - insert client into database
     */
    public static Uri insertClient(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(Contractor.ClientEntry.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return Contractor.ClientEntry.buildClientUri(_id);
    }

    /*
     * int deleteClient(...) - delete client from database
     */
    public static int deleteClient(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(Contractor.ClientEntry.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateClient(...) - update user in database
     */
    public static int updateClient(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                 String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(Contractor.ClientEntry.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
