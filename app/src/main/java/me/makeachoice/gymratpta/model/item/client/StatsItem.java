package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.client.StatsContract;

/**************************************************************************************************/
/*
 * StatsItem
 */
/**************************************************************************************************/

public class StatsItem extends StatsFBItem{

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

    public StatsItem(){}

    public StatsItem(StatsFBItem item){
        appointmentDate = item.appointmentDate;
        appointmentTime = item.appointmentTime;
        modifiedDate = item.modifiedDate;
        statWeight = item.statWeight;
        statBodyFat = item.statBodyFat;
        statBMI = item.statBMI;
        statNeck = item.statNeck;
        statChest = item.statChest;
        statRBicep = item.statRBicep;
        statLBicep = item.statLBicep;
        statWaist = item.statWaist;
        statNavel = item.statNavel;
        statHips = item.statHips;
        statRThigh = item.statRThigh;
        statLThigh = item.statLThigh;
        statRCalf = item.statRCalf;
        statLCalf = item.statLCalf;
    }

    public StatsItem(Cursor cursor){
        uid = cursor.getString(StatsContract.INDEX_UID);
        clientKey = cursor.getString(StatsContract.INDEX_CLIENT_KEY);
        timestamp = cursor.getString(StatsContract.INDEX_TIMESTAMP);
        appointmentDate = cursor.getString(StatsContract.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(StatsContract.INDEX_APPOINTMENT_TIME);
        modifiedDate = cursor.getString(StatsContract.INDEX_MODIFIED_DATE);
        statWeight = cursor.getDouble(StatsContract.INDEX_STAT_WEIGHT);
        statBodyFat = cursor.getDouble(StatsContract.INDEX_STAT_BODY_FAT);
        statBMI = cursor.getDouble(StatsContract.INDEX_STAT_BMI);
        statNeck = cursor.getDouble(StatsContract.INDEX_STAT_NECK);
        statChest = cursor.getDouble(StatsContract.INDEX_STAT_CHEST);
        statRBicep = cursor.getDouble(StatsContract.INDEX_STAT_RBICEP);
        statLBicep = cursor.getDouble(StatsContract.INDEX_STAT_LBICEP);
        statWaist = cursor.getDouble(StatsContract.INDEX_STAT_WAIST);
        statNavel = cursor.getDouble(StatsContract.INDEX_STAT_NAVEL);
        statHips = cursor.getDouble(StatsContract.INDEX_STAT_HIPS);
        statRThigh = cursor.getDouble(StatsContract.INDEX_STAT_RTHIGH);
        statLThigh = cursor.getDouble(StatsContract.INDEX_STAT_LTHIGH);
        statRCalf = cursor.getDouble(StatsContract.INDEX_STAT_RCALF);
        statLCalf = cursor.getDouble(StatsContract.INDEX_STAT_LCALF);
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
        values.put(StatsContract.COLUMN_UID, uid);
        values.put(StatsContract.COLUMN_CLIENT_KEY, clientKey);
        values.put(StatsContract.COLUMN_TIMESTAMP, timestamp);
        values.put(StatsContract.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(StatsContract.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(StatsContract.COLUMN_MODIFIED_DATE, modifiedDate);
        values.put(StatsContract.COLUMN_STAT_WEIGHT, statWeight);
        values.put(StatsContract.COLUMN_STAT_BODY_FAT, statBodyFat);
        values.put(StatsContract.COLUMN_STAT_BMI, statBMI);
        values.put(StatsContract.COLUMN_STAT_NECK, statNeck);
        values.put(StatsContract.COLUMN_STAT_CHEST, statChest);
        values.put(StatsContract.COLUMN_STAT_RBICEP, statRBicep);
        values.put(StatsContract.COLUMN_STAT_LBICEP, statLBicep);
        values.put(StatsContract.COLUMN_STAT_WAIST, statWaist);
        values.put(StatsContract.COLUMN_STAT_NAVEL, statNavel);
        values.put(StatsContract.COLUMN_STAT_HIPS, statHips);
        values.put(StatsContract.COLUMN_STAT_RTHIGH, statRThigh);
        values.put(StatsContract.COLUMN_STAT_LTHIGH, statLThigh);
        values.put(StatsContract.COLUMN_STAT_RCALF, statRCalf);
        values.put(StatsContract.COLUMN_STAT_LCALF, statLCalf);

        return values;
    }

/**************************************************************************************************/


}
