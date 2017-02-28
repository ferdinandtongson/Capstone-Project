package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_STATS;

/**************************************************************************************************/
/*
 *  StatsLoader load data from stats table
 */
/**************************************************************************************************/

public class StatsLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mClientKey - client key value
 *      String mAppointmentDate - appointment date value
 *      mAppointmentTime - appointment time value
 *
 *      OnStatsLoadListener mListener - listens for when stats data is loaded
 *      interface OnStatsLoadListener:
 *          onStatsLoadFinished(Cursor) - notifies listener stats data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mClientKey - client key value
    private String mClientKey;

    //mAppointmentDate - appointment date value
    private String mAppointmentDate;

    //mAppointmentTime - appointment time value
    private String mAppointmentTime;

    //mListener - listens for when stats data is loaded
    private OnStatsLoadListener mListener;
    public interface OnStatsLoadListener{
        //notifies listener stats data has finished loading
        public void onStatsLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public StatsLoader(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;
        mClientKey = clientKey;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadStats(...) - start loader to load stats data from database
 *      void loadStatsByClientKey(...) - start loader to load stats data from database by clientKey
 *      void loadStatsByDateTime(...) - start loader to load stats data from database by date and time
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadStats(...) - start loader to load stats data from database
     */
    public void loadStats(OnStatsLoadListener listener){
        //load stats using default loader id
        loadStats(LOADER_STATS, listener);
    }

    /*
     * void loadStats(...) - start loader to load stats data from database
     */
    public void loadStats(int loaderId, OnStatsLoadListener listener){
        //get listener
        mListener = listener;

        // Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = StatsContract.buildStatsByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                StatsContract.PROJECTION,
                                null,
                                null,
                                StatsContract.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that data has finished loading
                            mListener.onStatsLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadStatsByClientKey(...) - start loader to load stats data from database by clientKey
     */
    public void loadStatsByClientKey(OnStatsLoadListener listener){
        //load appointment using default loader id
        loadStatsByClientKey(LOADER_STATS, listener);
    }


    /*
     * void loadStatsByClientKey(...) - start loader to load stats data from database by clientKey
     */
    public void loadStatsByClientKey(int loaderId, OnStatsLoadListener listener){
        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = StatsContract.buildStatsByClientKey(mUserId, mClientKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                StatsContract.PROJECTION,
                                null,
                                null,
                                StatsContract.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onStatsLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadStatsByDateTime(...) - start loader to load stats data from database by date and time
     */
    public void loadStatsByDateTime(String appointmentDate, String appointmentTime, OnStatsLoadListener listener){

        //load stats using default loader id
        loadStatsByDateTime(appointmentDate, appointmentTime, LOADER_STATS, listener);
    }

    /*
     * void loadStatsByDateTime(...) - start loader to load stats data from database by date and time
     */
    public void loadStatsByDateTime(String appointmentDate, String appointmentTime,
                                    int loaderId, OnStatsLoadListener listener){
        //get date
        mAppointmentDate = appointmentDate;

        //get time
        mAppointmentTime = appointmentTime;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = StatsContract.buildStatsByDateTime(mUserId, mClientKey,
                                mAppointmentDate, mAppointmentTime);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                StatsContract.PROJECTION,
                                null,
                                null,
                                StatsContract.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onStatsLoadFinished(cursor);
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
        destroyLoader(LOADER_STATS);
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
