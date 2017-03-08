package me.makeachoice.gymratpta.model.item;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.user.UserContract;

public class UserItem extends UserFBItem{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public UserItem(){}

    public UserItem(UserFBItem item){
        uid = item.uid;
        userName = item.userName;
        email = item.email;
        status = item.status;
    }

    public UserItem(Cursor cursor){
        uid = cursor.getString(UserContract.INDEX_UID);
        userName = cursor.getString(UserContract.INDEX_USER_NAME);
        email = cursor.getString(UserContract.INDEX_USER_EMAIL);
        status = cursor.getString(UserContract.INDEX_USER_STATUS);
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
        values.put(UserContract.COLUMN_UID, uid);
        values.put(UserContract.COLUMN_USER_NAME, userName);
        values.put(UserContract.COLUMN_USER_EMAIL, email);
        values.put(UserContract.COLUMN_USER_STATUS, status);

        return values;
    }

    public UserFBItem getFBItem(){
        UserFBItem item = new UserFBItem();
        item.uid = uid;
        item.userName = userName;
        item.email = email;
        item.status = status;

        return item;
    }

/**************************************************************************************************/

}
