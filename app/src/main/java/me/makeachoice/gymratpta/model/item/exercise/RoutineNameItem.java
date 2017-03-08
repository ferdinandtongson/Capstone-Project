package me.makeachoice.gymratpta.model.item.exercise;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameContract;

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

    public RoutineNameItem(String name){
        routineName = name;
    }

    public RoutineNameItem(Cursor cursor){
        uid = cursor.getString(RoutineNameContract.INDEX_UID);
        routineName = cursor.getString(RoutineNameContract.INDEX_ROUTINE_NAME);
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
        values.put(RoutineNameContract.COLUMN_UID, uid);
        values.put(RoutineNameContract.COLUMN_ROUTINE_NAME, routineName);

        return values;
    }

    public RoutineNameFBItem getFBItem(){
        RoutineNameFBItem fbItem = new RoutineNameFBItem();
        fbItem.routineName = routineName;

        return fbItem;
    }

/**************************************************************************************************/


}
