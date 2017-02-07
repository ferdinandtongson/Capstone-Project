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
    public String fkey;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public RoutineItem(){}

    public RoutineItem(RoutineFBItem item){
        routineName = item.routineName;
        exercise01 = item.exercise01;
        exercise02 = item.exercise02;
        exercise03 = item.exercise03;
        exercise04 = item.exercise04;
        exercise05 = item.exercise05;
        exercise06 = item.exercise06;
        exercise07 = item.exercise07;
        exercise08 = item.exercise08;
        exercise09 = item.exercise09;
        exercise10 = item.exercise10;
        category01 = item.category01;
        category02 = item.category02;
        category03 = item.category03;
        category04 = item.category04;
        category05 = item.category05;
        category06 = item.category06;
        category07 = item.category07;
        category08 = item.category08;
        category09 = item.category09;
        category10 = item.category10;
        set01 = item.set01;
        set02 = item.set02;
        set03 = item.set03;
        set04 = item.set04;
        set05 = item.set05;
        set06 = item.set06;
        set07 = item.set07;
        set08 = item.set08;
        set09 = item.set09;
        set10 = item.set10;
    }

    public RoutineItem(Cursor cursor){
        uid = cursor.getString(RoutineColumns.INDEX_UID);
        fkey = cursor.getString(RoutineColumns.INDEX_FKEY);
        routineName = cursor.getString(RoutineColumns.INDEX_ROUTINE_NAME);
        exercise01 = cursor.getString(RoutineColumns.INDEX_EXERCISE01);
        exercise02 = cursor.getString(RoutineColumns.INDEX_EXERCISE02);
        exercise03 = cursor.getString(RoutineColumns.INDEX_EXERCISE03);
        exercise04 = cursor.getString(RoutineColumns.INDEX_EXERCISE04);
        exercise05 = cursor.getString(RoutineColumns.INDEX_EXERCISE05);
        exercise06 = cursor.getString(RoutineColumns.INDEX_EXERCISE06);
        exercise07 = cursor.getString(RoutineColumns.INDEX_EXERCISE07);
        exercise08 = cursor.getString(RoutineColumns.INDEX_EXERCISE08);
        exercise09 = cursor.getString(RoutineColumns.INDEX_EXERCISE09);
        exercise10 = cursor.getString(RoutineColumns.INDEX_EXERCISE10);
        category01 = cursor.getString(RoutineColumns.INDEX_CATEGORY01);
        category02 = cursor.getString(RoutineColumns.INDEX_CATEGORY02);
        category03 = cursor.getString(RoutineColumns.INDEX_CATEGORY03);
        category04 = cursor.getString(RoutineColumns.INDEX_CATEGORY04);
        category05 = cursor.getString(RoutineColumns.INDEX_CATEGORY05);
        category06 = cursor.getString(RoutineColumns.INDEX_CATEGORY06);
        category07 = cursor.getString(RoutineColumns.INDEX_CATEGORY07);
        category08 = cursor.getString(RoutineColumns.INDEX_CATEGORY08);
        category09 = cursor.getString(RoutineColumns.INDEX_CATEGORY09);
        category10 = cursor.getString(RoutineColumns.INDEX_CATEGORY10);
        set01 = cursor.getInt(RoutineColumns.INDEX_SET01);
        set02 = cursor.getInt(RoutineColumns.INDEX_SET02);
        set03 = cursor.getInt(RoutineColumns.INDEX_SET03);
        set04 = cursor.getInt(RoutineColumns.INDEX_SET04);
        set05 = cursor.getInt(RoutineColumns.INDEX_SET05);
        set06 = cursor.getInt(RoutineColumns.INDEX_SET06);
        set07 = cursor.getInt(RoutineColumns.INDEX_SET07);
        set08 = cursor.getInt(RoutineColumns.INDEX_SET08);
        set09 = cursor.getInt(RoutineColumns.INDEX_SET09);
        set10 = cursor.getInt(RoutineColumns.INDEX_SET10);
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
        values.put(Contractor.RoutineEntry.COLUMN_FKEY, fkey);
        values.put(Contractor.RoutineEntry.COLUMN_ROUTINE_NAME, routineName);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE01, exercise01);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE02, exercise02);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE03, exercise03);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE04, exercise04);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE05, exercise05);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE06, exercise06);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE07, exercise07);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE08, exercise08);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE09, exercise09);
        values.put(Contractor.RoutineEntry.COLUMN_EXERCISE10, exercise10);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY01, category01);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY02, category02);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY03, category03);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY04, category04);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY05, category05);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY06, category06);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY07, category07);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY08, category08);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY09, category09);
        values.put(Contractor.RoutineEntry.COLUMN_CATEGORY10, category10);
        values.put(Contractor.RoutineEntry.COLUMN_SET01, set01);
        values.put(Contractor.RoutineEntry.COLUMN_SET02, set02);
        values.put(Contractor.RoutineEntry.COLUMN_SET03, set03);
        values.put(Contractor.RoutineEntry.COLUMN_SET04, set04);
        values.put(Contractor.RoutineEntry.COLUMN_SET05, set05);
        values.put(Contractor.RoutineEntry.COLUMN_SET06, set06);
        values.put(Contractor.RoutineEntry.COLUMN_SET07, set07);
        values.put(Contractor.RoutineEntry.COLUMN_SET08, set08);
        values.put(Contractor.RoutineEntry.COLUMN_SET09, set09);
        values.put(Contractor.RoutineEntry.COLUMN_SET10, set10);

        return values;
    }

    /**************************************************************************************************/
    
}
