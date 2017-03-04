package me.makeachoice.gymratpta.model.item.exercise;


import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;

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
        uid = cursor.getString(RoutineContract.INDEX_UID);
        routineName = cursor.getString(RoutineContract.INDEX_ROUTINE_NAME);
        orderNumber = cursor.getString(RoutineContract.INDEX_ORDER_NUMBER);
        exercise = cursor.getString(RoutineContract.INDEX_EXERCISE);
        category = cursor.getString(RoutineContract.INDEX_CATEGORY);
        numOfSets = cursor.getString(RoutineContract.INDEX_NUM_SETS);
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
        values.put(RoutineContract.COLUMN_UID, uid);
        values.put(RoutineContract.COLUMN_ROUTINE_NAME, routineName);
        values.put(RoutineContract.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(RoutineContract.COLUMN_EXERCISE, exercise);
        values.put(RoutineContract.COLUMN_CATEGORY, category);
        values.put(RoutineContract.COLUMN_NUM_SETS, numOfSets);

        return values;
    }

    /**************************************************************************************************/
    
}
