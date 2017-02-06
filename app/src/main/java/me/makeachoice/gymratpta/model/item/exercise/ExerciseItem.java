package me.makeachoice.gymratpta.model.item.exercise;


import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseColumns;

/**************************************************************************************************/
/*
 * ExerciseItem extends ExerciseFBItem and used to interface with local database
 */
/**************************************************************************************************/

public class ExerciseItem extends ExerciseFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String categoryKey;
    public String fkey;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ExerciseItem(){}

    public ExerciseItem(ExerciseFBItem item){
        exerciseName = item.exerciseName;
        exerciseCategory = item.exerciseCategory;
        recordPrimary = item.recordPrimary;
        recordSecondary = item.recordSecondary;
    }

    public ExerciseItem(Cursor cursor){
        uid = cursor.getString(ExerciseColumns.INDEX_UID);
        categoryKey = cursor.getString(ExerciseColumns.INDEX_CATEGORY_KEY);
        fkey = cursor.getString(ExerciseColumns.INDEX_FKEY);
        exerciseName = cursor.getString(ExerciseColumns.INDEX_EXERCISE_NAME);
        exerciseCategory = cursor.getString(ExerciseColumns.INDEX_EXERCISE_CATEGORY);
        recordPrimary = cursor.getString(ExerciseColumns.INDEX_RECORD_PRIMARY);
        recordSecondary = cursor.getString(ExerciseColumns.INDEX_RECORD_SECONDARY);
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
        values.put(Contractor.ExerciseEntry.COLUMN_UID, uid);
        values.put(Contractor.ExerciseEntry.COLUMN_CATEGORY_KEY, categoryKey);
        values.put(Contractor.ExerciseEntry.COLUMN_FKEY, fkey);
        values.put(Contractor.ExerciseEntry.COLUMN_EXERCISE_NAME, exerciseName);
        values.put(Contractor.ExerciseEntry.COLUMN_EXERCISE_CATEGORY, exerciseCategory);
        values.put(Contractor.ExerciseEntry.COLUMN_RECORD_PRIMARY, recordPrimary);
        values.put(Contractor.ExerciseEntry.COLUMN_RECORD_SECONDARY, recordSecondary);

        return values;
    }

/**************************************************************************************************/



}
