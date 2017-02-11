package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryColumns;
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
    private static MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private static String mUserId;

    //mListener - listens for when the category data is loaded
    private static OnCategoryLoadListener mListener;
    public interface OnCategoryLoadListener{
        //notify listener that category data has finished loading
        public void onCategoryLoadFinished(Cursor cursor);
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
    public static void loadCategories(MyActivity activity, String userId, OnCategoryLoadListener listener){
        //load category using default loader category id
        loadCategories(activity, userId, LOADER_CATEGORY, listener);
    }

    /*
     * void loadCategories(...) - start loader to load category data from database
     */
    public static void loadCategories(MyActivity activity, String userId, int loaderId,
                                      OnCategoryLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

        //get listener
        mListener = listener;

        // Initializes category loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.CategoryEntry.buildCategoryByUID(mUserId);
                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                CategoryColumns.PROJECTION_CATEGORY,
                                null,
                                null,
                                Contractor.CategoryEntry.SORT_ORDER_DEFAULT);
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
    public static void destroyLoader(MyActivity activity){
        //destroy loader using default category loader id
        activity.getSupportLoaderManager().destroyLoader(LOADER_CATEGORY);
    }

    /*
     * void destroyLoader(...) - destroy loader and any data managed by the loader
     */
    public static void destroyLoader(MyActivity activity, int loaderId){
        //destroy loader
        activity.getSupportLoaderManager().destroyLoader(loaderId);
    }

/**************************************************************************************************/

}
