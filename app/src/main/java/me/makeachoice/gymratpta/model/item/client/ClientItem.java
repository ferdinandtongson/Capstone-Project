package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.client.ClientContract;

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
        uid = cursor.getString(ClientContract.INDEX_UID);
        fkey = cursor.getString(ClientContract.INDEX_FKEY);
        contactId = cursor.getLong(ClientContract.INDEX_CONTACTS_ID);
        clientName = cursor.getString(ClientContract.INDEX_CLIENT_NAME);
        email = cursor.getString(ClientContract.INDEX_CLIENT_EMAIL);
        phone = cursor.getString(ClientContract.INDEX_CLIENT_PHONE);
        firstSession = cursor.getString(ClientContract.INDEX_FIRST_SESSION);
        goals = cursor.getString(ClientContract.INDEX_CLIENT_GOALS);
        status = cursor.getString(ClientContract.INDEX_CLIENT_STATUS);
        profilePic = cursor.getString(ClientContract.INDEX_PROFILE_PIC);
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
        values.put(ClientContract.COLUMN_UID, uid);
        values.put(ClientContract.COLUMN_FKEY, fkey);
        values.put(ClientContract.COLUMN_CONTACTS_ID, contactId);
        values.put(ClientContract.COLUMN_CLIENT_NAME, clientName);
        values.put(ClientContract.COLUMN_CLIENT_EMAIL, email);
        values.put(ClientContract.COLUMN_CLIENT_PHONE, phone);
        values.put(ClientContract.COLUMN_FIRST_SESSION, firstSession);
        values.put(ClientContract.COLUMN_CLIENT_GOALS, goals);
        values.put(ClientContract.COLUMN_CLIENT_STATUS, status);
        values.put(ClientContract.COLUMN_PROFILE_PIC, profilePic);

        return values;
    }

/**************************************************************************************************/

}
