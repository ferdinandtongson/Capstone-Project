package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.AppointmentColumns;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_APPOINTMENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;

/**************************************************************************************************/
/*
 * AppointmentLoader loads appointment data from database
 */
/**************************************************************************************************/

public class AppointmentLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mAppointmentDate - appointment date value
 *      String mClientKey - client key value
 *
 *      OnAppointmentLoadListener mListener - listens for when appointment data is loaded
 *      interface OnAppointmentLoadListener:
 *          onAppointmentLoadFinished(Cursor) - notifies listener appointment data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mAppointmentDate - appointment date value
    private String mAppointmentDate;

    //mClientKey - client key value
    private String mClientKey;

    //mListener - listens for when appointment data is loaded
    private OnAppointmentLoadListener mListener;
    public interface OnAppointmentLoadListener{
        //notifies listener appointment data has finished loading
        public void onAppointmentLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public AppointmentLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadAppointment(...) - start loader to load appointment data from database
 *      void loadAppointmentByDay(...) - start loader to load appointment data from database by date
 *      void loadAppointmentByClientKey(...) - start loader to load appointment data from database by clientKey
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadAppointment(...) - start loader to load appointment data from database
     */
    public void loadAppointment(OnAppointmentLoadListener listener){
        //load appointments using default loader id
        loadAppointment(LOADER_APPOINTMENT, listener);
    }

    /*
     * void loadAppointment(...) - start loader to load appointment data from database
     */
    public void loadAppointment(int loaderId, OnAppointmentLoadListener listener){
        //get listener
        mListener = listener;

        // Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = Contractor.AppointmentEntry.buildAppointmentByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                AppointmentColumns.PROJECTION,
                                null,
                                null,
                                Contractor.AppointmentEntry.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that data has finished loading
                            mListener.onAppointmentLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadAppointmentByDay(...) - start loader to load appointment data from database by date
     */
    public void loadAppointmentByDate(String appointmentDate, OnAppointmentLoadListener listener){
        //load appointment using default loader id
        loadAppointmentByDate(appointmentDate, LOADER_APPOINTMENT, listener);
    }

    /*
     * void loadAppointmentByDay(...) - start loader to load appointment data from database by date
     */
    public void loadAppointmentByDate(String appointmentDate, int loaderId, OnAppointmentLoadListener listener){
        //get date
        mAppointmentDate = appointmentDate;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = Contractor.AppointmentEntry.buildAppointmentByDate(mUserId, mAppointmentDate);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                AppointmentColumns.PROJECTION,
                                null,
                                null,
                                Contractor.AppointmentEntry.SORT_ORDER_TIME_CLIENT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onAppointmentLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadAppointmentByClientKey(...) - start loader to load appointment data from database by clientKey
     */
    public void loadAppointmentByClientKey(String clientKey, OnAppointmentLoadListener listener){
        //load appointment using default loader id
        loadAppointmentByClientKey(clientKey, LOADER_APPOINTMENT, listener);
    }


    /*
     * void loadAppointmentByClientKey(...) - start loader to load appointment data from database by clientKey
     */
    public void loadAppointmentByClientKey(String clientKey, int loaderId, OnAppointmentLoadListener listener){
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
                        Uri uri = Contractor.AppointmentEntry.buildAppointmentByClientKey(mUserId, mClientKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                AppointmentColumns.PROJECTION,
                                null,
                                null,
                                Contractor.AppointmentEntry.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onAppointmentLoadFinished(cursor);
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
