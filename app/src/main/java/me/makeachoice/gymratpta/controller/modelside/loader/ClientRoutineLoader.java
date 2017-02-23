package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineColumns;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_ROUTINE;

/**************************************************************************************************/
/*
 * ClientRoutineLoader loads client routine table data
 */
/**************************************************************************************************/

public class ClientRoutineLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mClientKey - client key value
 *      String mAppointmentDate - appointment date value
 *      mAppointmentTime - appointment time value
 *
 *      OnClientRoutineLoadListener mListener - listens for when stats data is loaded
 *      interface OnClientRoutineLoadListener:
 *          onClientRoutineLoadFinished(Cursor) - notifies listener stats data has finished loading
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

    //mListener - listens for when client routine data is loaded
    private OnClientRoutineLoadListener mListener;
    public interface OnClientRoutineLoadListener{
        //notifies listener client routine data has finished loading
        public void onClientRoutineLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientRoutineLoader(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;
        mClientKey = clientKey;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadClientRoutine(...) - start loader to load clien routine data from database
 *      void loadClientRoutineByClientKey(...) - start loader to load clien routine data from database by clientKey
 *      void loadClientRoutineByDateTime(...) - start loader to load clien routine data from database by date and time
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadClientRoutine(...) - start loader to load clien routine data from database
     */
    public void loadClientRoutine(OnClientRoutineLoadListener listener){
        //load client routine using default loader id
        loadClientRoutine(LOADER_CLIENT_ROUTINE, listener);
    }

    /*
     * void loadClientRoutine(...) - start loader to load client routine data from database
     */
    public void loadClientRoutine(int loaderId, ClientRoutineLoader.OnClientRoutineLoadListener listener){
        //get listener
        mListener = listener;

        //Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = Contractor.ClientRoutineEntry.buildClientRoutineByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientRoutineColumns.PROJECTION,
                                null,
                                null,
                                Contractor.ClientRoutineEntry.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener data has finished loading
                            mListener.onClientRoutineLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadClientRoutineByClientKey(...) - start loader to load client routine data from database by clientKey
     */
    public void loadClientRoutineByClientKey(ClientRoutineLoader.OnClientRoutineLoadListener listener){
        //load client routine using default loader id
        loadClientRoutineByClientKey(LOADER_CLIENT_ROUTINE, listener);
    }


    /*
     * void loadClientRoutineByClientKey(...) - start loader to load client routine data from database by clientKey
     */
    public void loadClientRoutineByClientKey(int loaderId, ClientRoutineLoader.OnClientRoutineLoadListener listener){
        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = Contractor.ClientRoutineEntry.buildClientRoutineByClientKey(mUserId, mClientKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientRoutineColumns.PROJECTION,
                                null,
                                null,
                                Contractor.ClientRoutineEntry.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onClientRoutineLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadClientRoutineByDateTime(...) - start loader to load client routine data from database by date and time
     */
    public void loadClientRoutineByDateTime(String appointmentDate, String appointmentTime, ClientRoutineLoader.OnClientRoutineLoadListener listener){

        //load client routine using default loader id
        loadClientRoutineByDateTime(appointmentDate, appointmentTime, LOADER_CLIENT_ROUTINE, listener);
    }

    /*
     * void loadClientRoutineByDateTime(...) - start loader to load client routine data from database by date and time
     */
    public void loadClientRoutineByDateTime(String appointmentDate, String appointmentTime,
                                    int loaderId, ClientRoutineLoader.OnClientRoutineLoadListener listener){
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
                        Uri uri = Contractor.ClientRoutineEntry.buildClientRoutineByDateTime(mUserId, mClientKey,
                                mAppointmentDate, mAppointmentTime);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientRoutineColumns.PROJECTION,
                                null,
                                null,
                                Contractor.ClientRoutineEntry.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onClientRoutineLoadFinished(cursor);
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
        destroyLoader(LOADER_CLIENT_ROUTINE);
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
