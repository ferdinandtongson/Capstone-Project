package me.makeachoice.gymratpta.model.contract.contacts;

import android.provider.ContactsContract;

import static me.makeachoice.gymratpta.model.contract.user.UserColumns.COLUMN_USER_EMAIL;
import static me.makeachoice.gymratpta.model.contract.user.UserColumns.COLUMN_USER_NAME;
import static me.makeachoice.gymratpta.model.contract.user.UserColumns.COLUMN_USER_PHOTO;
import static me.makeachoice.gymratpta.model.contract.user.UserColumns.COLUMN_USER_STATUS;

/**
 * Created by Usuario on 2/3/2017.
 */

public class ContactsColumns {

    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION_CONTACTS =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            };

    public static final int INDEX_ID = 0;
    public static final int INDEX_PRIMARY_NAME = 1;

}
