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
 *  UserQueryHelper helps query user data from the database
 */
/**************************************************************************************************/

public class UserQueryHelper {

/**************************************************************************************************/
/*
 *  UserProvider content provider for user info
 */
/**************************************************************************************************/

    //userQueryBuild - sqlite query build used to access user table
    private static final SQLiteQueryBuilder userQueryBuilder;

    static{
        //initialize userQueryBuilder
        userQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        userQueryBuilder.setTables(Contractor.UserEntry.TABLE_NAME);
    }

    //query selection - user.uid = ?
    private static final String uidSelection =
            Contractor.UserEntry.TABLE_NAME + "." + Contractor.UserEntry.COLUMN_UID + " = ? ";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getUserByUID(...) - get user from user table with UId value
 *      Cursor getUsers(...) - get all users from user table
 */
/**************************************************************************************************/
    /*
     * Cursor getUserByUID(...) - get user with UId value
     */
    public static Cursor getUserByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/user/[uid]/user_name=[userName],uid=[uid],.....
        String uid = Contractor.UserEntry.getUIdFromUri(uri);

        //query from user table
        return UserQueryHelper.userQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //selection - user.uid = ?
                UserQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getUsers(...) - get all users from user table
     */
    public static Cursor getUsers(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {

        //query user table
        return dbHelper.getReadableDatabase().query(
                Contractor.UserEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertUser(...) - insert user into database
 *      int deleteUser(...) - delete user from database
 *      int updateUser(...) - update user in database
 */
/**************************************************************************************************/
    /*
     * Uri insertUser(...) - insert user into database
     */
    public static Uri insertUser(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(Contractor.UserEntry.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            Log.d("Choice", "     exception: " + mSQLException.toString());
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
                Log.d("Choice", "     constraint exception");
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
                Log.d("Choice", "     mismatch exception");
            }else{
                throw mSQLException;
            }
        }

        return Contractor.UserEntry.buildUserUri(_id);
    }

    /*
     * int deleteUser(...) - delete user from database
     */
    public static int deleteUser(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(Contractor.UserEntry.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateUser(...) - update user in database
     */
    public static int updateUser(SQLiteDatabase db, Uri uri, ContentValues values, String selection,
                                 String[] selectionArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(Contractor.UserEntry.TABLE_NAME, values, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/

}
