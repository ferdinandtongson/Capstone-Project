package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseColumns;

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
    public long timestamp;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientExerciseItem(){}

    public ClientExerciseItem(ClientExerciseFBItem item){
        appointmentDate = item.appointmentDate;
        appointmentTime = item.appointmentTime;
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
        uid = cursor.getString(ClientExerciseColumns.INDEX_UID);
        clientKey = cursor.getString(ClientExerciseColumns.INDEX_CLIENT_KEY);
        timestamp = cursor.getLong(ClientExerciseColumns.INDEX_TIMESTAMP);
        appointmentDate = cursor.getString(ClientExerciseColumns.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(ClientExerciseColumns.INDEX_APPOINTMENT_TIME);
        category = cursor.getString(ClientExerciseColumns.INDEX_CATEGORY);
        exercise = cursor.getString(ClientExerciseColumns.INDEX_EXERCISE);
        orderNumber = cursor.getInt(ClientExerciseColumns.INDEX_ORDER_NUMBER);
        setNumber = cursor.getInt(ClientExerciseColumns.INDEX_SET_NUMBER);
        primaryLabel = cursor.getString(ClientExerciseColumns.INDEX_PRIMARY_LABEL);
        primaryValue = cursor.getString(ClientExerciseColumns.INDEX_PRIMARY_VALUE);
        secondaryLabel = cursor.getString(ClientExerciseColumns.INDEX_SECONDARY_LABEL);
        secondaryValue = cursor.getString(ClientExerciseColumns.INDEX_SECONDARY_VALUE);
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
        values.put(Contractor.ClientExerciseEntry.COLUMN_UID, uid);
        values.put(Contractor.ClientExerciseEntry.COLUMN_CLIENT_KEY, clientKey);
        values.put(Contractor.ClientExerciseEntry.COLUMN_TIMESTAMP, timestamp);
        values.put(Contractor.ClientExerciseEntry.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(Contractor.ClientExerciseEntry.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(Contractor.ClientExerciseEntry.COLUMN_CATEGORY, category);
        values.put(Contractor.ClientExerciseEntry.COLUMN_EXERCISE, exercise);
        values.put(Contractor.ClientExerciseEntry.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(Contractor.ClientExerciseEntry.COLUMN_SET_NUMBER, setNumber);
        values.put(Contractor.ClientExerciseEntry.COLUMN_PRIMARY_LABEL, primaryLabel);
        values.put(Contractor.ClientExerciseEntry.COLUMN_PRIMARY_VALUE, primaryValue);
        values.put(Contractor.ClientExerciseEntry.COLUMN_SECONDARY_LABEL, secondaryLabel);
        values.put(Contractor.ClientExerciseEntry.COLUMN_SECONDARY_VALUE, secondaryValue);

        return values;
    }

/**************************************************************************************************/

}
