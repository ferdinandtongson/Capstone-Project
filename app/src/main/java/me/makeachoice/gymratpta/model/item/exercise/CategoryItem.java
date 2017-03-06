package me.makeachoice.gymratpta.model.item.exercise;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.exercise.CategoryContract;

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
        uid = cursor.getString(CategoryContract.INDEX_UID);
        fkey = cursor.getString(CategoryContract.INDEX_FKEY);
        categoryName = cursor.getString(CategoryContract.INDEX_CATEGORY_NAME);
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
        values.put(CategoryContract.COLUMN_UID, uid);
        values.put(CategoryContract.COLUMN_FKEY, fkey);
        values.put(CategoryContract.COLUMN_CATEGORY_NAME, categoryName);

        return values;
    }

/**************************************************************************************************/

}
