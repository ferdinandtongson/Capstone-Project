package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE;

/**************************************************************************************************/
/*
 * RoutineLoader is a cursor loader that loads data from exercise routine table
 */
/**************************************************************************************************/

public class RoutineLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      mRoutineName - name of specific routine
 *      OnRoutineLoadListener mListener - listens for when the routine data is loaded
 *
 *      interface OnRoutineLoadListener:
 *          onRoutineLoadFinished(Cursor) - notifies listener routine data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mRoutineName - name of specific routine
    private String mRoutineName;

    //mListener - listens for when the routine data is loaded
    private OnRoutineLoadListener mListener;
    public interface OnRoutineLoadListener{
        //notify listener that routine data has finished loading
        public void onRoutineLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public RoutineLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadRoutines(...) - start loader to load routine data from database
 *      void loadRoutineByName(...) - start loader to load specified routine from database
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadRoutines(...) - start loader to load routine data from database
     */
    public void loadRoutines(OnRoutineLoadListener listener){
        //load routines using default loader routine id
        loadRoutines(LOADER_ROUTINE, listener);
    }

    /*
     * void loadRoutines(...) - start loader to load routine data from database
     */
    public void loadRoutines(int loaderId, OnRoutineLoadListener listener){

        //get listener
        mListener = listener;

        //initializes routine loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = RoutineContract.buildRoutineByUID(mUserId);
                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                RoutineContract.PROJECTION_ROUTINE,
                                null,
                                null,
                                RoutineContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not null
                        if(mListener != null){
                            //notify listener that routine data has finished loading
                            mListener.onRoutineLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadRoutineByName(...) - start loader to load specified routine from database
     */
    public void loadRoutineByName(String routineName, OnRoutineLoadListener listener){
        //load routines using default loader routine id
        loadRoutineByName(routineName, LOADER_ROUTINE, listener);
    }

    /*
     * void loadRoutineByName(...) - start loader to load specified routine from database
     */
    public void loadRoutineByName(String routineName, int loaderId, OnRoutineLoadListener listener){

        //get routine name
        mRoutineName = routineName;

        //get listener
        mListener = listener;

        //initializes routine loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = RoutineContract.buildRoutineByName(mUserId, mRoutineName);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                RoutineContract.PROJECTION_ROUTINE,
                                null,
                                null,
                                RoutineContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not null
                        if(mListener != null){
                            //notify listener that routine data has finished loading
                            mListener.onRoutineLoadFinished(cursor);
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
        //destroy loader using default routine loader id
        mActivity.getSupportLoaderManager().destroyLoader(LOADER_ROUTINE);
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
