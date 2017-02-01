package me.makeachoice.gymratpta.model.db.user;

import me.makeachoice.gymratpta.model.contract.Contractor;

/**
 * Created by Usuario on 2/1/2017.
 */

public class UserTable {

    public final static String SQL_CREATE_USER_TABLE = "CREATE TABLE " + Contractor.UserEntry.TABLE_NAME + " (" +
            Contractor.UserEntry._ID + " INTEGER PRIMARY KEY," +
            Contractor.UserEntry.COLUMN_FIREBASE_KEY + " TEXT UNIQUE NOT NULL, " +
            Contractor.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
            Contractor.UserEntry.COLUMN_USER_EMAIL + " TEXT, " +
            Contractor.UserEntry.COLUMN_USER_PHOTO+ " TEXT, " +
            Contractor.UserEntry.COLUMN_USER_STATUS + " TEXT NOT NULL, " +
            " );";

}
