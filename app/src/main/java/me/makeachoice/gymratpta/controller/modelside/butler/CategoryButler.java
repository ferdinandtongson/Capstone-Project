package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.CategoryFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.CategoryLoader;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryContract;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  CategoryButler assists in loading, saving and deleting categories to database and firebase
 */
/**************************************************************************************************/

public class CategoryButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mCategoryList - data loaded from database
    private ArrayList<CategoryItem> mCategoryList;

    private CategoryItem mSaveItem;

    //mLoaderId - loader id number
    private int mLoaderId;

    //mCategoryLoader - loader class used to load data
    private CategoryLoader mCategoryLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<CategoryItem> categoryList);
    }

    //mSaveListener - used to listen for save events
    private OnSavedListener mSaveListener;
    public interface OnSavedListener{
        public void onSaved();
    }

    //mDeleteListener - used to listen for delete events
    private OnDeletedListener mDeleteListener;
    public interface OnDeletedListener{
        public void onDeleted();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public CategoryButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        mCategoryList = new ArrayList<>();
        mCategoryLoader = new CategoryLoader(mActivity, mUserId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load
 */
/**************************************************************************************************/
    /*
     * void loadCategories() - load data for given day from database
     */
    public void loadCategories(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mCategoryLoader.loadCategories(mLoaderId, new CategoryLoader.OnCategoryLoadListener() {
            @Override
            public void onCategoryLoadFinished(Cursor cursor) {
                onCategoriesLoaded(cursor);
            }
        });
    }

    public void loadCategoryByName(String categoryName, int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mCategoryLoader.loadCategoryByName(categoryName, loaderId, new CategoryLoader.OnCategoryLoadListener() {
            @Override
            public void onCategoryLoadFinished(Cursor cursor) {
                onCategoriesLoaded(cursor);
            }
        });
    }

    /*
     * void onCategoriesLoaded(Cursor) - data from database has been loaded
     */
    private void onCategoriesLoaded(Cursor cursor){
        //clear list array
        mCategoryList.clear();

        //get number of scheduled appointments loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            CategoryItem item = new CategoryItem(cursor);

            //add item to list
            mCategoryList.add(item);
        }

        //destroy loader
        mCategoryLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(mCategoryList);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save
 */
/**************************************************************************************************/

    public void saveCategory(CategoryItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

    }

    /*
     * void saveToFB(StatsItem) - save to firebase
     */
    private void saveToFirebase(CategoryItem saveItem){
        mSaveItem = saveItem;

        //get firebase helper instance
        CategoryFirebaseHelper fbHelper = CategoryFirebaseHelper.getInstance();

        //save to firebase
        fbHelper.addCategory(mUserId, saveItem.getFBItem(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    CategoryItem item = postSnapshot.getValue(CategoryItem.class);

                    if(item.categoryName.equals(mSaveItem.categoryName)){
                        mSaveItem.fkey = postSnapshot.getKey();
                    }
                }
                //save to local database
                saveToDatabase(mSaveItem);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(CategoryItem saveItem){
        //get uri value for table
        Uri uriValue = CategoryContract.CONTENT_URI;

        //add to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/


}
