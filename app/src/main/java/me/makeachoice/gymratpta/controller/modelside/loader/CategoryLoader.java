package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.exercise.CategoryContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CATEGORY;

/**************************************************************************************************/
/*
 * CategoryLoader is a cursor loader that loads data from exercise category table
 */
/**************************************************************************************************/

public class CategoryLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      OnCategoryLoadListener mListener - listens for when the category data is loaded
 *
 *      interface OnCategoryLoadListener:
 *          onCategoryLoadFinished(Cursor) - notifies listener category data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mCategoryName - name of category
    private String mCategoryName;

    //mListener - listens for when the category data is loaded
    private OnCategoryLoadListener mListener;
    public interface OnCategoryLoadListener{
        //notify listener that category data has finished loading
        public void onCategoryLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public CategoryLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadCategories(...) - start loader to load category data from database
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadCategories(...) - start loader to load category data from database
     */
    public void loadCategories(OnCategoryLoadListener listener){
        //load category using default loader category id
        loadCategories(LOADER_CATEGORY, listener);
    }

    /*
     * void loadCategories(...) - start loader to load category data from database
     */
    public void loadCategories(int loaderId, OnCategoryLoadListener listener){

        //get listener
        mListener = listener;

        // Initializes category loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = CategoryContract.buildCategoryByUID(mUserId);
                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                CategoryContract.PROJECTION_CATEGORY,
                                null,
                                null,
                                CategoryContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not null
                        if(mListener != null){
                            //notify listener that category data has finished loading
                            mListener.onCategoryLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadCategoryByName(...) - start loader to load category data from database
     */
    public void loadCategoryByName(String categoryName, int loaderId, OnCategoryLoadListener listener){
        //get category name
        mCategoryName = categoryName;

        //get listener
        mListener = listener;

        // Initializes category loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = CategoryContract.buildCategoryByName(mUserId, mCategoryName);
                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                CategoryContract.PROJECTION_CATEGORY,
                                null,
                                null,
                                CategoryContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not null
                        if(mListener != null){
                            //notify listener that category data has finished loading
                            mListener.onCategoryLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void destroyLoader(...) - destroy loader and any data managed by the loader
     */
    public void destroyLoader(){
        //destroy loader using default category loader id
        mActivity.getSupportLoaderManager().destroyLoader(LOADER_CATEGORY);
    }

    /*
     * void destroyLoader(...) - destroy loader and any data managed by the loader
     */
    public void destroyLoader(int loaderId){
        //destroy loader
        mActivity.getSupportLoaderManager().destroyLoader(loaderId);
    }

/**************************************************************************************************/

}
