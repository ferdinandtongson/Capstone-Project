package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE_NAME;

/**************************************************************************************************/
/*
 * RoutineNameLoader is a cursor loader that loads data from exercise routine name table
 */
/**************************************************************************************************/

public class RoutineNameLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      OnRoutineNameLoadListener mListener - listens for when the routine name data is loaded
 *
 *      interface OnRoutineNameLoadListener:
 *          onRoutineNameLoadFinished(Cursor) - notifies listener routine name data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mListener - listens for when the routine name data is loaded
    private OnRoutineNameLoadListener mListener;
    public interface OnRoutineNameLoadListener{
        //notify listener that routine name data has finished loading
        public void onRoutineNameLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public RoutineNameLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Public Methods
 *      void loadRoutineNames(...) - start loader to load routine name data from database
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadRoutineNames(...) - start loader to load routine name data from database
     */
    public void loadRoutineNames(OnRoutineNameLoadListener listener){
        //load routine names using default loader routine id
        loadRoutineNames(LOADER_ROUTINE_NAME, listener);
    }

    /*
     * void loadRoutineNames(...) - start loader to load routine name data from database
     */
    public void loadRoutineNames(int loaderId, OnRoutineNameLoadListener listener){
        //get listener
        mListener = listener;

        //initializes routine name loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = RoutineNameContract.buildRoutineNameByUID(mUserId);
                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                RoutineNameContract.PROJECTION_ROUTINE_NAME,
                                null,
                                null,
                                RoutineNameContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not null
                        if(mListener != null){
                            //notify listener that routine name data has finished loading
                            mListener.onRoutineNameLoadFinished(cursor);
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
        //destroy loader using default routine name loader id
        mActivity.getSupportLoaderManager().destroyLoader(LOADER_ROUTINE_NAME);
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
