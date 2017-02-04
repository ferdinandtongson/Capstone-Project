package me.makeachoice.gymratpta.model.contract.contacts;

import android.provider.ContactsContract;

public class ContactsColumns {

    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION_CONTACTS = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_PRIMARY_NAME = 1;

    public static final String[] PROJECTION_PHONE = {
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    public static final int INDEX_PHONE = 0;

    public static final String[] PROJECTION_EMAIL = {
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Email.TYPE
    };

    public static final int INDEX_EMAIL_DATA = 0;
    public static final int INDEX_EMAIL_TYPE = 1;

}
