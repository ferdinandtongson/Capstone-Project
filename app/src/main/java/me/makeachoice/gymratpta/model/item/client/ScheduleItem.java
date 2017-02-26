package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;

/**************************************************************************************************/
/*
 * ScheduleItem appointment schedule item used for local database
 */
/**************************************************************************************************/

public class ScheduleItem extends ScheduleFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String datestamp;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ScheduleItem(){}

    public ScheduleItem(ScheduleFBItem item){
        appointmentDate = item.appointmentDate;
        appointmentTime = item.appointmentTime;
        clientKey = item.clientKey;
        clientName = item.clientName;
        routineName = item.routineName;
        status = item.status;
    }

    public ScheduleItem(ClientAppFBItem item){
        appointmentDate = item.appointmentDate;
        appointmentTime = item.appointmentTime;
        clientName = item.clientName;
        routineName = item.routineName;
        status = item.status;
    }

    public ScheduleItem(Cursor cursor){
        uid = cursor.getString(ScheduleContract.INDEX_UID);
        datestamp = cursor.getString(ScheduleContract.INDEX_TIMESTAMP);
        appointmentDate = cursor.getString(ScheduleContract.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(ScheduleContract.INDEX_APPOINTMENT_TIME);
        clientKey = cursor.getString(ScheduleContract.INDEX_CLIENT_KEY);
        clientName = cursor.getString(ScheduleContract.INDEX_CLIENT_NAME);
        routineName = cursor.getString(ScheduleContract.INDEX_ROUTINE_NAME);
        status = cursor.getString(ScheduleContract.INDEX_APPOINTMENT_STATUS);
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
        //create content value
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.COLUMN_UID, uid);
        values.put(ScheduleContract.COLUMN_TIMESTAMP, datestamp);
        values.put(ScheduleContract.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(ScheduleContract.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(ScheduleContract.COLUMN_CLIENT_KEY, clientKey);
        values.put(ScheduleContract.COLUMN_CLIENT_NAME, clientName);
        values.put(ScheduleContract.COLUMN_ROUTINE_NAME, routineName);
        values.put(ScheduleContract.COLUMN_APPOINTMENT_STATUS, status);

        return values;
    }

/**************************************************************************************************/

}
