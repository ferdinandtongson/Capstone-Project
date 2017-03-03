package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;

/**************************************************************************************************/
/*
 * ClientExerciseItem, extends ClientExerciseFBItem, used to interface with local database
 */
/**************************************************************************************************/

public class ClientExerciseItem extends ClientExerciseFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String clientKey;
    public String timestamp;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientExerciseItem(){}

    public ClientExerciseItem(ClientExerciseFBItem item){
        category = item.category;
        exercise = item.exercise;
        orderNumber = item.orderNumber;
        setNumber = item.setNumber;
        primaryLabel = item.primaryLabel;
        primaryValue = item.primaryValue;
        secondaryLabel = item.secondaryLabel;
        secondaryValue = item.secondaryValue;
    }

    public ClientExerciseItem(Cursor cursor){
        uid = cursor.getString(ClientExerciseContract.INDEX_UID);
        clientKey = cursor.getString(ClientExerciseContract.INDEX_CLIENT_KEY);
        timestamp = cursor.getString(ClientExerciseContract.INDEX_TIMESTAMP);
        category = cursor.getString(ClientExerciseContract.INDEX_CATEGORY);
        exercise = cursor.getString(ClientExerciseContract.INDEX_EXERCISE);
        orderNumber = cursor.getString(ClientExerciseContract.INDEX_ORDER_NUMBER);
        setNumber = cursor.getString(ClientExerciseContract.INDEX_SET_NUMBER);
        primaryLabel = cursor.getString(ClientExerciseContract.INDEX_PRIMARY_LABEL);
        primaryValue = cursor.getString(ClientExerciseContract.INDEX_PRIMARY_VALUE);
        secondaryLabel = cursor.getString(ClientExerciseContract.INDEX_SECONDARY_LABEL);
        secondaryValue = cursor.getString(ClientExerciseContract.INDEX_SECONDARY_VALUE);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      ContentValues getContentValues() - convert item data to a ContentValues object
 */
/**************************************************************************************************/
    /*
     * ContentValues getContentValues() - convert item data to a ContentValues object
     */
    public ContentValues getContentValues(){
        //content value of client exercise record
        ContentValues values = new ContentValues();
        values.put(ClientExerciseContract.COLUMN_UID, uid);
        values.put(ClientExerciseContract.COLUMN_CLIENT_KEY, clientKey);
        values.put(ClientExerciseContract.COLUMN_TIMESTAMP, timestamp);
        values.put(ClientExerciseContract.COLUMN_CATEGORY, category);
        values.put(ClientExerciseContract.COLUMN_EXERCISE, exercise);
        values.put(ClientExerciseContract.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(ClientExerciseContract.COLUMN_SET_NUMBER, setNumber);
        values.put(ClientExerciseContract.COLUMN_PRIMARY_LABEL, primaryLabel);
        values.put(ClientExerciseContract.COLUMN_PRIMARY_VALUE, primaryValue);
        values.put(ClientExerciseContract.COLUMN_SECONDARY_LABEL, secondaryLabel);
        values.put(ClientExerciseContract.COLUMN_SECONDARY_VALUE, secondaryValue);

        return values;
    }

/**************************************************************************************************/

}
