package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineColumns;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;

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
    public String appointmentDate;
    public String appointmentTime;
    public String exercise;
    public String category;
    public int orderNumber;
    public int numOfSets;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientRoutineItem(){}

    public ClientRoutineItem(AppointmentItem appItem, RoutineItem routineItem){
        uid = appItem.uid;
        clientKey = appItem.clientKey;
        appointmentDate = appItem.appointmentDate;
        appointmentTime = appItem.appointmentTime;
        category = routineItem.category;
        exercise = routineItem.exercise;
        orderNumber = routineItem.orderNumber;
        numOfSets = routineItem.numOfSets;

    }

    public ClientRoutineItem(Cursor cursor){
        uid = cursor.getString(ClientRoutineColumns.INDEX_UID);
        clientKey = cursor.getString(ClientRoutineColumns.INDEX_CLIENT_KEY);
        appointmentDate = cursor.getString(ClientRoutineColumns.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(ClientRoutineColumns.INDEX_APPOINTMENT_TIME);
        category = cursor.getString(ClientRoutineColumns.INDEX_CATEGORY);
        exercise = cursor.getString(ClientRoutineColumns.INDEX_EXERCISE);
        orderNumber = cursor.getInt(ClientRoutineColumns.INDEX_ORDER_NUMBER);
        numOfSets = cursor.getInt(ClientRoutineColumns.INDEX_NUM_OF_SETS);
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
        values.put(Contractor.ClientRoutineEntry.COLUMN_UID, uid);
        values.put(Contractor.ClientRoutineEntry.COLUMN_CLIENT_KEY, clientKey);
        values.put(Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(Contractor.ClientRoutineEntry.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(Contractor.ClientRoutineEntry.COLUMN_CATEGORY, category);
        values.put(Contractor.ClientRoutineEntry.COLUMN_EXERCISE, exercise);
        values.put(Contractor.ClientRoutineEntry.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(Contractor.ClientRoutineEntry.COLUMN_NUM_OF_SETS, numOfSets);

        return values;
    }

/**************************************************************************************************/

}
