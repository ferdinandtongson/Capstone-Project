package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientColumns;

/**************************************************************************************************/
/*
 * ClientItem, extends ClientFBItem, used to interface with local database
 */
/**************************************************************************************************/

public class ClientItem extends ClientFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String fkey;
    public long contactId;
    public String profilePic;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientItem(){}

    public ClientItem(ClientFBItem item){
        clientName = item.clientName;
        email = item.email;
        phone = item.phone;
        firstSession = item.firstSession;
        goals = item.goals;
        status = item.status;
    }

    public ClientItem(Cursor cursor){
        uid = cursor.getString(ClientColumns.INDEX_UID);
        fkey = cursor.getString(ClientColumns.INDEX_FKEY);
        contactId = cursor.getLong(ClientColumns.INDEX_CONTACTS_ID);
        clientName = cursor.getString(ClientColumns.INDEX_CLIENT_NAME);
        email = cursor.getString(ClientColumns.INDEX_CLIENT_EMAIL);
        phone = cursor.getString(ClientColumns.INDEX_CLIENT_PHONE);
        firstSession = cursor.getString(ClientColumns.INDEX_FIRST_SESSION);
        goals = cursor.getString(ClientColumns.INDEX_CLIENT_GOALS);
        status = cursor.getString(ClientColumns.INDEX_CLIENT_STATUS);
        profilePic = cursor.getString(ClientColumns.INDEX_PROFILE_PIC);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      ContentValues getContentValues() - convert item data to a ContentValues object
 *      ClientItem addData(Cursor) - add cursor data to item
 */
/**************************************************************************************************/
    /*
     * ContentValues getContentValues() - convert item data to a ContentValues object
     */
    public ContentValues getContentValues(){
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(Contractor.ClientEntry.COLUMN_UID, uid);
        values.put(Contractor.ClientEntry.COLUMN_FKEY, fkey);
        values.put(Contractor.ClientEntry.COLUMN_CONTACTS_ID, contactId);
        values.put(Contractor.ClientEntry.COLUMN_CLIENT_NAME, clientName);
        values.put(Contractor.ClientEntry.COLUMN_CLIENT_EMAIL, email);
        values.put(Contractor.ClientEntry.COLUMN_CLIENT_PHONE, phone);
        values.put(Contractor.ClientEntry.COLUMN_FIRST_SESSION, firstSession);
        values.put(Contractor.ClientEntry.COLUMN_CLIENT_GOALS, goals);
        values.put(Contractor.ClientEntry.COLUMN_CLIENT_STATUS, status);
        values.put(Contractor.ClientEntry.COLUMN_PROFILE_PIC, profilePic);

        return values;
    }

/**************************************************************************************************/

}
