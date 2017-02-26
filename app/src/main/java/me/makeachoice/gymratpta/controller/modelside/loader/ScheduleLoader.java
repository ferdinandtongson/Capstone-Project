package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;

/**************************************************************************************************/
/*
 * ScheduleLoader loads appointment data from database
 */
/**************************************************************************************************/

public class ScheduleLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mAppointmentDate - appointment date value
 *      String mClientKey - client key value
 *
 *      OnScheduleLoadListener mListener - listens for when schedule data is loaded
 *      interface OnScheduleLoadListener:
 *          onScheduleLoadFinished(Cursor) - notifies listener schedule data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mTimestamp - appointment timestamp value
    private String mTimestamp;

    //mClientKey - client key value
    private String mClientKey;

    //mListener - listens for when schedule data is loaded
    private OnScheduleLoadListener mListener;
    public interface OnScheduleLoadListener{
        //notifies listener schedule data has finished loading
        public void onScheduleLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ScheduleLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadSchedule(...) - start loader to load schedule data from database
 *      void loadScheduleByTimestamp(...) - start loader to load schedule data from database by timestamp
 *      void loadScheduleByClientKey(...) - start loader to load schedule data from database by clientKey
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadSchedule(...) - start loader to load schedule data from database
     */
    public void loadSchedule(OnScheduleLoadListener listener){
        //load schedule using default loader id
        loadSchedule(LOADER_SCHEDULE, listener);
    }

    /*
     * void loadSchedule(...) - start loader to load schedule data from database
     */
    public void loadSchedule(int loaderId, OnScheduleLoadListener listener){
        //get listener
        mListener = listener;

        // Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ScheduleContract.buildScheduleByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                me.makeachoice.gymratpta.model.contract.client.ScheduleContract.PROJECTION,
                                null,
                                null,
                                ScheduleContract.SORT_ORDER_TIMESTAMP);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that data has finished loading
                            mListener.onScheduleLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadScheduleByTimestamp(...) - start loader to load schedule data from database by timestamp
     */
    public void loadScheduleByTimestamp(String timestamp, OnScheduleLoadListener listener){
        //load schedule using default loader id
        loadScheduleByTimestamp(timestamp, LOADER_SCHEDULE, listener);
    }

    /*
     * void loadScheduleByTimestamp(...) - start loader to load schedule data from database by timestamp
     */
    public void loadScheduleByTimestamp(String timestamp, int loaderId, OnScheduleLoadListener listener){
        //get date
        mTimestamp = timestamp;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ScheduleContract.buildScheduleByTimestamp(mUserId, mTimestamp);
                        Log.d("Choice", "     uri: " + uri.toString());

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ScheduleContract.PROJECTION,
                                null,
                                null,
                                ScheduleContract.SORT_ORDER_TIME_CLIENT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onScheduleLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadScheduleByClientKey(...) - start loader to load schedule data from database by clientKey
     */
    public void loadScheduleByClientKey(String clientKey, OnScheduleLoadListener listener){
        //load schedule using default loader id
        loadScheduleByClientKey(clientKey, LOADER_SCHEDULE, listener);
    }


    /*
     * void loadScheduleByClientKey(...) - start loader to load schedule data from database by clientKey
     */
    public void loadScheduleByClientKey(String clientKey, int loaderId, OnScheduleLoadListener listener){
        //get client key
        mClientKey = clientKey;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ScheduleContract.buildScheduleByClientKey(mUserId, mClientKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                me.makeachoice.gymratpta.model.contract.client.ScheduleContract.PROJECTION,
                                null,
                                null,
                                ScheduleContract.SORT_ORDER_TIMESTAMP);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onScheduleLoadFinished(cursor);
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
        //destroy loader using default loader exercise id
        destroyLoader(LOADER_CLIENT);
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
