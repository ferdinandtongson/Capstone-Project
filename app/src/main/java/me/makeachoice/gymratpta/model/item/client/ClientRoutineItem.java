package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;

/**************************************************************************************************/
/*
 * ClientRoutineItem holds client routine data used by the local database
 */
/**************************************************************************************************/

public class ClientRoutineItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String clientKey;
    public String timestamp;
    public String exercise;
    public String category;
    public String orderNumber;
    public String numOfSets;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientRoutineItem(){}

    public ClientRoutineItem(ScheduleItem appItem, RoutineItem routineItem){
        uid = appItem.uid;
        clientKey = appItem.clientKey;
        timestamp = DateTimeHelper.getTimestamp(appItem.appointmentDate, appItem.appointmentTime);
        category = routineItem.category;
        exercise = routineItem.exercise;
        orderNumber = routineItem.orderNumber;
        numOfSets = routineItem.numOfSets;

    }

    public ClientRoutineItem(Cursor cursor){
        uid = cursor.getString(ClientRoutineContract.INDEX_UID);
        clientKey = cursor.getString(ClientRoutineContract.INDEX_CLIENT_KEY);
        timestamp = cursor.getString(ClientRoutineContract.INDEX_TIMESTAMP);
        category = cursor.getString(ClientRoutineContract.INDEX_CATEGORY);
        exercise = cursor.getString(ClientRoutineContract.INDEX_EXERCISE);
        orderNumber = cursor.getString(ClientRoutineContract.INDEX_ORDER_NUMBER);
        numOfSets = cursor.getString(ClientRoutineContract.INDEX_NUM_OF_SETS);
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
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(ClientRoutineContract.COLUMN_UID, uid);
        values.put(ClientRoutineContract.COLUMN_CLIENT_KEY, clientKey);
        values.put(ClientRoutineContract.COLUMN_TIMESTAMP, timestamp);
        values.put(ClientRoutineContract.COLUMN_CATEGORY, category);
        values.put(ClientRoutineContract.COLUMN_EXERCISE, exercise);
        values.put(ClientRoutineContract.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(ClientRoutineContract.COLUMN_NUM_OF_SETS, numOfSets);

        return values;
    }

/**************************************************************************************************/

}
