package me.makeachoice.gymratpta.model.item.exercise;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryColumns;

/**************************************************************************************************/
/*
 * CategoryItem extends CategoryFBItem and used to interface with local database storing category data
 */
/**************************************************************************************************/

public class CategoryItem extends CategoryFBItem{

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

    public CategoryItem(){}

    public CategoryItem(CategoryFBItem item){
        categoryName = item.categoryName;
    }

    public CategoryItem(Cursor cursor){
        uid = cursor.getString(CategoryColumns.INDEX_UID);
        fkey = cursor.getString(CategoryColumns.INDEX_FKEY);
        categoryName = cursor.getString(CategoryColumns.INDEX_CATEGORY_NAME);
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
        values.put(Contractor.CategoryEntry.COLUMN_UID, uid);
        values.put(Contractor.CategoryEntry.COLUMN_FKEY, fkey);
        values.put(Contractor.CategoryEntry.COLUMN_CATEGORY_NAME, categoryName);

        return values;
    }

/**************************************************************************************************/




}
