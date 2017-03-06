package me.makeachoice.gymratpta.controller.modelside.query;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.exercise.CategoryContract;
import me.makeachoice.gymratpta.model.db.DBHelper;

/**************************************************************************************************/
/*
 *  CategoryQueryHelper helps build queries to access category table
 */
/**************************************************************************************************/

public class CategoryQueryHelper {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //categoryQueryBuilder - sqlite query build used to access category table
    private static final SQLiteQueryBuilder categoryQueryBuilder;

    static{
        //initialize categoryQueryBuilder
        categoryQueryBuilder = new SQLiteQueryBuilder();
        //set builder to query user table
        categoryQueryBuilder.setTables(CategoryContract.TABLE_NAME);
    }

    //query selection - category.uid = ?
    public static final String uidSelection =
            CategoryContract.TABLE_NAME + "." + CategoryContract.COLUMN_UID + " = ? ";

    //query selection - category.uid = ? AND fkey = ?
    public static final String fkeySelection =
            CategoryContract.TABLE_NAME+
                    "." + CategoryContract.COLUMN_UID + " = ? AND " +
                    CategoryContract.COLUMN_FKEY + " = ? ";

    //query selection - category.uid = ? AND category_name = ?
    public static final String statusSelection =
            CategoryContract.TABLE_NAME+
                    "." + CategoryContract.COLUMN_UID + " = ? AND " +
                    CategoryContract.COLUMN_CATEGORY_NAME + " = ? ";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Cursor getCategories(...) - get all categories from table
 *      Cursor getCategoryByFKey(...) - get category by firebase key
 *      Cursor getCategoryByName(...) - get category by name
 */
/**************************************************************************************************/
    /*
     * Cursor getCategories(...) - get all categories from table
     */
    public static Cursor getCategories(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/category/[uid]/....
        String uid = CategoryContract.getUIdFromUri(uri);

        //query category table
        return CategoryQueryHelper.categoryQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //selection - category.uid = ?
                CategoryQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getCategoryByUId(...) - get categories by userId
     */
    public static Cursor getCategoryByUId(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/category/[uid]
        String uid = CategoryContract.getUIdFromUri(uri);

        //query from category table
        return CategoryQueryHelper.categoryQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - category.uid = ?
                CategoryQueryHelper.uidSelection,
                new String[]{uid},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getCategoryByFKey(...) - get category by firebase key
     */
    public static Cursor getCategoryByFKey(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/category/[uid]/fkey/[fKey]
        String uid = CategoryContract.getUIdFromUri(uri);
        String fkey = CategoryContract.getFirebaseKeyFromUri(uri);

        //query from category table
        return CategoryQueryHelper.categoryQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - category.uid = ? AND fkey = ?
                CategoryQueryHelper.fkeySelection,
                new String[]{uid, fkey},
                null,
                null,
                sortOrder
        );
    }

    /*
     * Cursor getCategoryByName(...) - get category by name
     */
    public static Cursor getCategoryByName(DBHelper dbHelper, Uri uri, String[] projection, String sortOrder) {
        //"content://CONTENT_AUTHORITY/category/[uid]/category_name/[name]
        String uid = CategoryContract.getUIdFromUri(uri);
        String categoryName = CategoryContract.getCategoryNameFromUri(uri);

        //query from category table
        return CategoryQueryHelper.categoryQueryBuilder.query(
                dbHelper.getReadableDatabase(),
                projection,
                //query selection - category.uid = ? AND category_name = ?
                CategoryQueryHelper.statusSelection,
                new String[]{uid, categoryName},
                null,
                null,
                sortOrder
        );
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Cursor Methods
 *      Uri insertCategory(...) - insert category into database
 *      int deleteCategory(...) - delete category from database
 *      int updateCategory(...) - update category in database
 */
/**************************************************************************************************/
    /*
     * Uri insertCategory(...) - insert category into database
     */
    public static Uri insertCategory(SQLiteDatabase db, ContentValues values){
        long _id = -1;
        try{
            _id = db.insert(CategoryContract.TABLE_NAME, null, values);
        }
        catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                //some toast message to user.
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                //some toast message to user.
            }
            throw mSQLException;
        }

        return CategoryContract.buildCategoryUri(_id);
    }

    /*
     * int deleteCategory(...) - delete category from database
     */
    public static int deleteCategory(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        //number or rows deleted
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( selection == null ) selection = "1";

        try{
            rowsDeleted = db.delete(CategoryContract.TABLE_NAME, selection, selectionArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsDeleted;
    }

    /*
     * int updateCategory(...) - update category in database
     */
    public static int updateCategory(SQLiteDatabase db, Uri uri, ContentValues values, String whereClause,
                                   String[] whereArgs){
        int rowsUpdated;
        try{
            rowsUpdated = db.update(CategoryContract.TABLE_NAME, values, whereClause, whereArgs);
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        return rowsUpdated;
    }

/**************************************************************************************************/


}
