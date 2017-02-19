package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.AppointmentColumns;

/**************************************************************************************************/
/*
 * AppointmentItem, extends AppointmentFBItem, used to interface with local database
 */
/**************************************************************************************************/

public class AppointmentItem extends AppointmentFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String fkey;
    public String appointmentDate;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public AppointmentItem(){}

    public AppointmentItem(AppointmentFBItem item){
        appointmentTime = item.appointmentTime;
        clientKey = item.clientKey;
        clientName = item.clientName;
        routineName = item.routineName;
        status = item.status;
    }

    public AppointmentItem(ClientAppFBItem item){
        appointmentDate = item.appointmentDate;
        appointmentTime = item.appointmentTime;
        clientName = item.clientName;
        routineName = item.routineName;
        status = item.status;
    }

    public AppointmentItem(Cursor cursor){
        uid = cursor.getString(AppointmentColumns.INDEX_UID);
        fkey = cursor.getString(AppointmentColumns.INDEX_FKEY);
        appointmentDate = cursor.getString(AppointmentColumns.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(AppointmentColumns.INDEX_APPOINTMENT_TIME);
        clientKey = cursor.getString(AppointmentColumns.INDEX_CLIENT_KEY);
        clientName = cursor.getString(AppointmentColumns.INDEX_CLIENT_NAME);
        routineName = cursor.getString(AppointmentColumns.INDEX_ROUTINE_NAME);
        status = cursor.getString(AppointmentColumns.INDEX_APPOINTMENT_STATUS);
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
        values.put(Contractor.AppointmentEntry.COLUMN_UID, uid);
        values.put(Contractor.AppointmentEntry.COLUMN_FKEY, fkey);
        values.put(Contractor.AppointmentEntry.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(Contractor.AppointmentEntry.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(Contractor.AppointmentEntry.COLUMN_CLIENT_KEY, clientKey);
        values.put(Contractor.AppointmentEntry.COLUMN_CLIENT_NAME, clientName);
        values.put(Contractor.AppointmentEntry.COLUMN_ROUTINE_NAME, routineName);
        values.put(Contractor.AppointmentEntry.COLUMN_APPOINTMENT_STATUS, status);

        return values;
    }

/**************************************************************************************************/

}
