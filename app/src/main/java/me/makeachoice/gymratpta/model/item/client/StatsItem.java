package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.StatsColumns;

/**
 * Created by Usuario on 2/21/2017.
 */

public class StatsItem extends StatsFBItem{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String clientKey;
    public String appointmentDate;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public StatsItem(){}

    public StatsItem(StatsFBItem item){
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
        statNaval = item.statNaval;
        statHips = item.statHips;
        statRThigh = item.statRThigh;
        statLThigh = item.statLThigh;
        statRCalf = item.statRCalf;
        statLCalf = item.statLCalf;
    }

    public StatsItem(Cursor cursor){
        uid = cursor.getString(StatsColumns.INDEX_UID);
        clientKey = cursor.getString(StatsColumns.INDEX_CLIENT_KEY);
        appointmentDate = cursor.getString(StatsColumns.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(StatsColumns.INDEX_APPOINTMENT_TIME);
        modifiedDate = cursor.getString(StatsColumns.INDEX_MODIFIED_DATE);
        statWeight = cursor.getDouble(StatsColumns.INDEX_STAT_WEIGHT);
        statBodyFat = cursor.getDouble(StatsColumns.INDEX_STAT_BODY_FAT);
        statBMI = cursor.getDouble(StatsColumns.INDEX_STAT_BMI);
        statNeck = cursor.getDouble(StatsColumns.INDEX_STAT_NECK);
        statChest = cursor.getDouble(StatsColumns.INDEX_STAT_CHEST);
        statRBicep = cursor.getDouble(StatsColumns.INDEX_STAT_RBICEP);
        statLBicep = cursor.getDouble(StatsColumns.INDEX_STAT_LBICEP);
        statWaist = cursor.getDouble(StatsColumns.INDEX_STAT_WAIST);
        statNaval = cursor.getDouble(StatsColumns.INDEX_STAT_NAVAL);
        statHips = cursor.getDouble(StatsColumns.INDEX_STAT_HIPS);
        statRThigh = cursor.getDouble(StatsColumns.INDEX_STAT_RTHIGH);
        statLThigh = cursor.getDouble(StatsColumns.INDEX_STAT_LTHIGH);
        statRCalf = cursor.getDouble(StatsColumns.INDEX_STAT_RCALF);
        statLCalf = cursor.getDouble(StatsColumns.INDEX_STAT_LCALF);
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
        values.put(Contractor.StatsEntry.COLUMN_UID, uid);
        values.put(Contractor.StatsEntry.COLUMN_CLIENT_KEY, clientKey);
        values.put(Contractor.StatsEntry.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(Contractor.StatsEntry.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(Contractor.StatsEntry.COLUMN_MODIFIED_DATE, modifiedDate);
        values.put(Contractor.StatsEntry.COLUMN_STAT_WEIGHT, statWeight);
        values.put(Contractor.StatsEntry.COLUMN_STAT_BODY_FAT, statBodyFat);
        values.put(Contractor.StatsEntry.COLUMN_STAT_BMI, statBMI);
        values.put(Contractor.StatsEntry.COLUMN_STAT_NECK, statNeck);
        values.put(Contractor.StatsEntry.COLUMN_STAT_CHEST, statChest);
        values.put(Contractor.StatsEntry.COLUMN_STAT_RBICEP, statRBicep);
        values.put(Contractor.StatsEntry.COLUMN_STAT_LBICEP, statLBicep);
        values.put(Contractor.StatsEntry.COLUMN_STAT_WAIST, statWaist);
        values.put(Contractor.StatsEntry.COLUMN_STAT_NAVAL, statNaval);
        values.put(Contractor.StatsEntry.COLUMN_STAT_HIPS, statHips);
        values.put(Contractor.StatsEntry.COLUMN_STAT_RTHIGH, statRThigh);
        values.put(Contractor.StatsEntry.COLUMN_STAT_LTHIGH, statLThigh);
        values.put(Contractor.StatsEntry.COLUMN_STAT_RCALF, statRCalf);
        values.put(Contractor.StatsEntry.COLUMN_STAT_LCALF, statLCalf);

        return values;
    }

/**************************************************************************************************/


}
