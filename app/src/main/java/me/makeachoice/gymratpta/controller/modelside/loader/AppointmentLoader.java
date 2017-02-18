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
    private static MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private static String mUserId;

    //mAppointmentDate - appointment date value
    private static String mAppointmentDate;

    //mClientKey - client key value
    private static String mClientKey;

    //mListener - listens for when appointment data is loaded
    private static OnAppointmentLoadListener mListener;
    public interface OnAppointmentLoadListener{
        //notifies listener appointment data has finished loading
        public void onAppointmentLoadFinished(Cursor cursor);
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
    public static void loadAppointment(MyActivity activity, String userId, OnAppointmentLoadListener listener){
        //load appointments using default loader id
        loadAppointment(activity, userId, LOADER_APPOINTMENT, listener);
    }

    /*
     * void loadAppointment(...) - start loader to load appointment data from database
     */
    public static void loadAppointment(MyActivity activity, String userId, int loaderId, OnAppointmentLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

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
    public static void loadAppointmentByDate(MyActivity activity, String userId, String appointmentDate,
                                             OnAppointmentLoadListener listener){
        //load appointment using default loader id
        loadAppointmentByDate(activity, userId, appointmentDate, LOADER_APPOINTMENT, listener);
    }

    /*
     * void loadAppointmentByDay(...) - start loader to load appointment data from database by date
     */
    public static void loadAppointmentByDate(MyActivity activity, String userId, String appointmentDate,
                                           int loaderId, OnAppointmentLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

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
    public static void loadAppointmentByClientKey(MyActivity activity, String userId, String clientKey,
                                             OnAppointmentLoadListener listener){
        //load appointment using default loader id
        loadAppointmentByClientKey(activity, userId, clientKey, LOADER_APPOINTMENT, listener);
    }


    /*
     * void loadAppointmentByClientKey(...) - start loader to load appointment data from database by clientKey
     */
    public static void loadAppointmentByClientKey(MyActivity activity, String userId, String clientKey,
                                             int loaderId, OnAppointmentLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

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
    public static void destroyLoader(MyActivity activity){
        //destroy loader using default loader exercise id
        destroyLoader(activity, LOADER_CLIENT);
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
