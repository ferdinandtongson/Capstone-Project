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
 *  NotesQueryHelper helps build queries to client SOAP notes table
 */
/**************************************************************************************************/

public class NotesQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //notesQueryBuilder - sqlite query build used to access notes table
    private static final SQLiteQueryBuilder notesQueryBuilder;

    static{
        //initialize notesQueryBuilder
        notesQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query notes table
        notesQueryBuilder.setTables(Contractor.NotesEntry.TABLE_NAME);
    }

    //query selection - notes.uid = ?
    public static final String uidSelection =
            Contractor.NotesEntry.TABLE_NAME + "." + Contractor.NotesEntry.COLUMN_UID + " = ? ";

    //query selection - notes.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            Contractor.NotesEntry.TABLE_NAME+
                    "." + Contractor.NotesEntry.COLUMN_UID + " = ? AND " +
                    Contractor.NotesEntry.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - notes.uid = ? AND client_key = ? AND appointment_date = ?
    public static final String dateSelection =
            Contractor.NotesEntry.TABLE_NAME+
                    "." + Contractor.NotesEntry.COLUMN_UID + " = ? AND " +
                    Contractor.NotesEntry.COLUMN_CLIENT_KEY + " = ? AND " +
                    Contractor.NotesEntry.COLUMN_APPOINTMENT_DATE + " = ? ";

    //query selection - notes.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
    public static final String clientKeyDateTimeSelection =
            Contractor.NotesEntry.TABLE_NAME+
                    "." + Contractor.NotesEntry.COLUMN_UID + " = ? AND " +
                    Contractor.NotesEntry.COLUMN_CLIENT_KEY + " = ? AND " +
                    Contractor.NotesEntry.COLUMN_APPOINTMENT_DATE + " = ? AND " +
                    Contractor.NotesEntry.COLUMN_APPOINTMENT_TIME + " = ? ";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getNotesByUId(...) - get notes of user
 *      Cursor getNotesByClientKey(...) - get notes by client key
 *      Cursor getNotesByDate(...) - get notes by date
 *      Cursor getNotesByDateTime(...) - get notes by date and time
 */
/**************************************************************************************************/
    /*
     * Cursor getNotesByUId(...) - get notes of user
     */
    public static Cursor getNotesByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientNotes/[uid]
        String uid = Contractor.NotesEntry.getUIdFromUri(uri);

        //query from user table
        return NotesQueryHelper.notesQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - notes.uid = ?
                NotesQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getNotesByClientKey(...) - get notes by client key
     */
    public static Cursor getNotesByClientKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientNotes/[uid]/client_key/[clientKey]
        String uid = Contractor.NotesEntry.getUIdFromUri(uri);
        String clientKey = Contractor.NotesEntry.getClientKeyFromUri(uri);

        //query from table
        return NotesQueryHelper.notesQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - notes.uid = ? AND client_key = ?
                NotesQueryHelper.clientKeySelection,
                new String[]{uid, clientKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getNotesByDate(...) - get notes by date
     */
    public static Cursor getNotesByDate(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]
        String uid = Contractor.NotesEntry.getUIdFromUri(uri);
        String clientKey = Contractor.NotesEntry.getClientKeyFromDateUri(uri);
        String appointmentDate = Contractor.NotesEntry.getDateFromDateUri(uri);

        //query from table
        return NotesQueryHelper.notesQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - notes.uid = ? AND client_key = ? AND appointment_date = ?
                NotesQueryHelper.dateSelection,
                new String[]{uid, clientKey, appointmentDate},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getNotesByDateTime(...) - get notes by date and time
     */
    public static Cursor getNotesByDateTime(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/clientNotes/[uid]/[clientKey]/[appointmentDate]/[appointmentTime]
        String uid = Contractor.NotesEntry.getUIdFromUri(uri);
        String clientKey = Contractor.NotesEntry.getClientKeyFromDateTimeUri(uri);
        String appointmentDate = Contractor.NotesEntry.getDateFromDateTimeUri(uri);
        String appointmentTime = Contractor.NotesEntry.getTimeFromDateTimeUri(uri);

        //query from table
        return NotesQueryHelper.notesQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - notes.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
                NotesQueryHelper.clientKeyDateTimeSelection,
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
 *      Uri insertNotes(...) - insert notes into database
 *      int deleteNotes(...) - delete notes from database
 *      int updateNotes(...) - update notes in database
 */
    /**************************************************************************************************/
    /*
     * Uri insertNotes(...) - insert notes into database
     */
    public static Uri insertNotes(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(Contractor.NotesEntry.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return Contractor.NotesEntry.buildNotesUri(_id);
    }

    /*
     * int deleteNotes(...) - delete notes from database
     */
    public static int deleteNotes(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(Contractor.NotesEntry.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateNotes(...) - update notes in database
     */
    public static int updateNotes(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                        String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(Contractor.NotesEntry.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
