package me.makeachoice.gymratpta.model.item.exercise;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameColumns;

/**************************************************************************************************/
/*
 * RoutineNameItem holds routine names created by the user
 */
/**************************************************************************************************/

public class RoutineNameItem extends RoutineNameFBItem{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public RoutineNameItem(){}

    public RoutineNameItem(RoutineNameFBItem item){
        routineName = item.routineName;
    }

    public RoutineNameItem(Cursor cursor){
        uid = cursor.getString(RoutineNameColumns.INDEX_UID);
        routineName = cursor.getString(RoutineNameColumns.INDEX_ROUTINE_NAME);
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
        values.put(Contractor.RoutineNameEntry.COLUMN_UID, uid);
        values.put(Contractor.RoutineNameEntry.COLUMN_ROUTINE_NAME, routineName);

        return values;
    }

/**************************************************************************************************/


}
