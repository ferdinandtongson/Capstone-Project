package me.makeachoice.gymratpta.model.db.table;

import me.makeachoice.gymratpta.model.contract.Contractor;

/**************************************************************************************************/
/*
 *  Tables used in sqlite database
 */
/**************************************************************************************************/

public class TableHelper {

    public final static String SQL_CREATE_USER_TABLE = "CREATE TABLE " + Contractor.UserEntry.TABLE_NAME + " (" +
            Contractor.UserEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.UserEntry.COLUMN_UID + " TEXT UNIQUE NOT NULL, " +
            Contractor.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
            Contractor.UserEntry.COLUMN_USER_EMAIL + " TEXT, " +
            Contractor.UserEntry.COLUMN_USER_PHOTO+ " TEXT, " +
            Contractor.UserEntry.COLUMN_USER_STATUS + " TEXT NOT NULL);";

    public final static String SQL_CREATE_CLIENT_TABLE = "CREATE TABLE " + Contractor.ClientEntry.TABLE_NAME + " (" +
            Contractor.ClientEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.ClientEntry.COLUMN_UID + " TEXT NOT NULL, " +
            Contractor.ClientEntry.COLUMN_FKEY + " TEXT UNIQUE NOT NULL, " +
            Contractor.ClientEntry.COLUMN_CONTACTS_ID + " TEXT NOT NULL, " +
            Contractor.ClientEntry.COLUMN_CLIENT_NAME+ " TEXT NOT NULL, " +
            Contractor.ClientEntry.COLUMN_CLIENT_STATUS + " TEXT NOT NULL);";


}
