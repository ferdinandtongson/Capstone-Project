package me.makeachoice.gymratpta.model.item.exercise;


import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns;

/**************************************************************************************************/
/*
 * RoutineItem used with routine database table
 */
/**************************************************************************************************/

public class RoutineItem extends RoutineFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/
    
    public String uid;
    public String routineName;
    public String orderNumber;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public RoutineItem(){}

    public RoutineItem(RoutineFBItem item){
        exercise = item.exercise;
        category = item.category;
        numOfSets = item.numOfSets;
    }

    public RoutineItem(Cursor cursor){
        uid = cursor.getString(RoutineColumns.INDEX_UID);
        routineName = cursor.getString(RoutineColumns.INDEX_ROUTINE_NAME);
        orderNumber = cursor.getString(RoutineColumns.INDEX_ORDER_NUMBER);
        exercise = cursor.getString(RoutineColumns.INDEX_EXERCISE);
        category = cursor.getString(RoutineColumns.INDEX_CATEGORY);
        numOfSets = cursor.getString(RoutineColumns.INDEX_NUM_SETS);
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
        values.put(Contractor.RoutineEntry.COLUMN_UID, uid);
        values.put(Contractor.RoutineEntry.COLUMN_ROUTINE_NAME, routineName);
        values.put(Contractor.RoutineEntry.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE, exercise);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY, category);
        values.put(Contractor.RoutineEntry.COLUMN_NUM_SETS, numOfSets);

        return values;
    }

    /**************************************************************************************************/
    
}
