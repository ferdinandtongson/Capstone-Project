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
 *  AppointmentQueryHelper helps build queries to the appointment table
 */
/**************************************************************************************************/


public class AppointmentQueryHelper {

/**************************************************************************************************/
/*
 *  Query builder
 */
/**************************************************************************************************/

    //appointmentQueryBuilder - sqlite query build used to access appointment table
    private static final SQLiteQueryBuilder appointmentQueryBuilder;

    static{
        //initialize appointmentQueryBuilder
        appointmentQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        appointmentQueryBuilder.setTables(Contractor.AppointmentEntry.TABLE_NAME);
    }

    //query selection - appointment.uid = ?
    public static final String uidSelection =
            Contractor.AppointmentEntry.TABLE_NAME + "." + Contractor.AppointmentEntry.COLUMN_UID + " = ? ";

    //query selection - appointment.uid = ? AND appointment_date = ?
    public static final String dateSelection =
            Contractor.AppointmentEntry.TABLE_NAME+
                    "." + Contractor.AppointmentEntry.COLUMN_UID + " = ? AND " +
                    Contractor.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " = ? ";

    //query selection - appointment.uid = ? AND client_key = ?
    public static final String clientKeySelection =
            Contractor.AppointmentEntry.TABLE_NAME+
                    "." + Contractor.AppointmentEntry.COLUMN_UID + " = ? AND " +
                    Contractor.AppointmentEntry.COLUMN_CLIENT_KEY + " = ? ";

    //query selection - appointment.uid = ? AND fkey = ?
    public static final String fkeySelection =
            Contractor.AppointmentEntry.TABLE_NAME+
                    "." + Contractor.AppointmentEntry.COLUMN_UID + " = ? AND " +
                    Contractor.AppointmentEntry.COLUMN_FKEY+ " = ? ";

    //query selection - appointment.uid = ? AND client_key = ? AND appointment_date = ? AND appointment_time = ?
    public static final String clientKeyDateTimeSelection =
            Contractor.AppointmentEntry.TABLE_NAME+
                    "." + Contractor.AppointmentEntry.COLUMN_UID + " = ? AND " +
                    Contractor.AppointmentEntry.COLUMN_CLIENT_KEY + " = ? AND " +
                    Contractor.AppointmentEntry.COLUMN_APPOINTMENT_DATE + " = ? AND " +
                    Contractor.AppointmentEntry.COLUMN_APPOINTMENT_TIME + " = ? ";


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getAppointmentByUId(...) - get appointments of user
 *      Cursor getAppointmentByDate(...) - get appointments by date
 *      Cursor getAppointmentByClientKey(...) - get appointments by client key
 *      Cursor getAppointmentByFKey(...) - get appointments by fkey
 */
/**************************************************************************************************/
    /*
     * Cursor getAppointmentByUId(...) - get appointments of user
     */
    public static Cursor getAppointmentByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/routine/[uid]
        String uid = Contractor.AppointmentEntry.getUIdFromUri(uri);

        //query from user table
        return AppointmentQueryHelper.appointmentQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - appointment.uid = ?
                AppointmentQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getAppointmentByDate(...) - get appointments by date
     */
    public static Cursor getAppointmentByDate(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/appointment/[uid]/appointment_date/[appointmentDay]
        String uid = Contractor.AppointmentEntry.getUIdFromUri(uri);
        String appointmentDate = Contractor.AppointmentEntry.getDateFromUri(uri);

        //query from table
        return AppointmentQueryHelper.appointmentQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - appointment.uid = ? AND appointment_date = ?
                AppointmentQueryHelper.dateSelection,
                new String[]{uid, appointmentDate},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getAppointmentByClientKey(...) - get appointments by client key
     */
    public static Cursor getAppointmentByClientKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/appointment/[uid]/client_key/[clientKey]
        String uid = Contractor.AppointmentEntry.getUIdFromUri(uri);
        String clientKey = Contractor.AppointmentEntry.getClientKeyFromUri(uri);

        //query from table
        return AppointmentQueryHelper.appointmentQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - appointment.uid = ? AND client_key = ?
                AppointmentQueryHelper.clientKeySelection,
                new String[]{uid, clientKey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getAppointmentByFKey(...) - get appointments by fkey
     */
    public static Cursor getAppointmentByFKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/appointment/[uid]/fkey/[fkey]
        String uid = Contractor.AppointmentEntry.getUIdFromUri(uri);
        String fkey = Contractor.AppointmentEntry.getFKeyUri(uri);

        //query from table
        return AppointmentQueryHelper.appointmentQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - appointment.uid = ? AND fkey = ?
                AppointmentQueryHelper.fkeySelection,
                new String[]{uid, fkey},
                null,
                null,
                sortOrder
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertAppointment(...) - insert appointment into database
 *      int deleteAppointment(...) - delete appointment from database
 *      int updateAppointment(...) - update appointment in database
 */
/**************************************************************************************************/
    /*
     * Uri insertAppointment(...) - insert appointment into database
     */
    public static Uri insertAppointment(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(Contractor.AppointmentEntry.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return Contractor.AppointmentEntry.buildAppointmentUri(_id);
    }

    /*
     * int deleteAppointment(...) - delete appointment from database
     */
    public static int deleteAppointment(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(Contractor.AppointmentEntry.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateAppointment(...) - update appointment in database
     */
    public static int updateAppointment(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                    String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(Contractor.AppointmentEntry.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
