package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_EXERCISE;

/**************************************************************************************************/
/*
 * ClientExerciseLoader loads client exercise table data
 */
/**************************************************************************************************/

public class ClientExerciseLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mClientKey - client key value
 *      String mAppointmentDate - appointment date value
 *      mAppointmentTime - appointment time value
 *
 *      OnClientExerciseLoadListener mListener - listens for when client exercise data is loaded
 *      interface OnClientExerciseLoadListener:
 *          onClientExerciseLoadFinished(Cursor) - notifies listener client exercise data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mClientKey - client key value
    private String mClientKey;

    //mTimestamp - appointment timestamp value
    private String mTimestamp;

    //mExercise - exercise name value
    private String mExercise;

    //mListener - listens for when client exercise data is loaded
    private OnClientExerciseLoadListener mListener;
    public interface OnClientExerciseLoadListener{
        //notifies listener client exercise data has finished loading
        void onClientExerciseLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientExerciseLoader(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;
        mClientKey = clientKey;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadClientExercise(...) - start loader to load client exercise data from database
 *      void loadClientExerciseByClientKey(...) - start loader to load client exercise data from database by clientKey
 *      void loadClientExerciseByDateTime(...) - start loader to load client exercise data from database by date and time
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadClientExercise(...) - start loader to load client exercise data from database
     */
    public void loadClientExercise(OnClientExerciseLoadListener listener){
        //load client exercise using default loader id
        loadClientExercise(LOADER_CLIENT_EXERCISE, listener);
    }

    /*
     * void loadClientExercise(...) - start loader to load client exercise data from database
     */
    public void loadClientExercise(int loaderId, OnClientExerciseLoadListener listener){
        //get listener
        mListener = listener;

        //Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ClientExerciseContract.buildClientExerciseByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientExerciseContract.PROJECTION,
                                null,
                                null,
                                ClientExerciseContract.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener data has finished loading
                            mListener.onClientExerciseLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadClientExerciseByClientKey(...) - start loader to load client exercise data from database by clientKey
     */
    public void loadClientExerciseByClientKey(ClientExerciseLoader.OnClientExerciseLoadListener listener){
        //load client exercise using default loader id
        loadClientExerciseByClientKey(LOADER_CLIENT_EXERCISE, listener);
    }


    /*
     * void loadClientExerciseByClientKey(...) - start loader to load client exercise data from database by clientKey
     */
    public void loadClientExerciseByClientKey(int loaderId, OnClientExerciseLoadListener listener){
        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ClientExerciseContract.buildClientExerciseByClientKey(mUserId, mClientKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientExerciseContract.PROJECTION,
                                null,
                                null,
                                ClientExerciseContract.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client exercise data has finished loading
                            mListener.onClientExerciseLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadClientExerciseByTimestamp(...) - start loader to load client exercise data from database by timestamp
     */
    public void loadClientExerciseByTimestamp(String timestamp, OnClientExerciseLoadListener listener){

        //load client exercise using default loader id
        loadClientExerciseByTimestamp(timestamp, LOADER_CLIENT_EXERCISE, listener);
    }

    /*
     * void loadClientExerciseByTimestamp(...) - start loader to load client exercise data from database by date and time
     */
    public void loadClientExerciseByTimestamp(final String timestamp, int loaderId, OnClientExerciseLoadListener listener){
        //get timestamp
        mTimestamp = timestamp;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ClientExerciseContract.buildClientExerciseByTimestamp(mUserId, mClientKey,
                                mTimestamp);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientExerciseContract.PROJECTION,
                                null,
                                null,
                                ClientExerciseContract.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client exercise data has finished loading
                            mListener.onClientExerciseLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadClientExerciseByTimestampExercise(...) - start loader to load client exercise data from database by exercise
     */
    public void loadClientExerciseByExercise(String exercise, OnClientExerciseLoadListener listener){

        //load client exercise
        loadClientExerciseByExercise(exercise, LOADER_CLIENT_EXERCISE, listener);
    }

    /*
     * void loadClientExerciseByExercise(...) - start loader to load client exercise data by exercise
     */
    public void loadClientExerciseByExercise(String exercise, int loaderId,
                                                      OnClientExerciseLoadListener listener){
        //get exercise
        mExercise = exercise;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ClientExerciseContract.buildClientExerciseByExercise(mUserId,
                                mClientKey, mExercise);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientExerciseContract.PROJECTION,
                                null,
                                null,
                                ClientExerciseContract.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client exercise data has finished loading
                            mListener.onClientExerciseLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }


    /*
     * void loadClientExerciseByTimestampExercise(...) - start loader to load client exercise data from database by exercise
     */
    public void loadClientExerciseByTimestampExercise(String timestamp, String exercise, OnClientExerciseLoadListener listener){

        //load client exercise
        loadClientExerciseByTimestampExercise(timestamp, exercise, LOADER_CLIENT_EXERCISE, listener);
    }

    /*
     * void loadClientExerciseByTimestampExercise(...) - start loader to load client exercise data by exercise
     */
    public void loadClientExerciseByTimestampExercise(String timestamp, String exercise, int loaderId,
                                             OnClientExerciseLoadListener listener){
        //get exercise
        mExercise = exercise;
        mTimestamp = timestamp;

        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = ClientExerciseContract.buildClientExerciseByTimestampExercise(mUserId,
                                mClientKey, mTimestamp, mExercise);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientExerciseContract.PROJECTION,
                                null,
                                null,
                                ClientExerciseContract.DEFAULT_SORT_ORDER);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client exercise data has finished loading
                            mListener.onClientExerciseLoadFinished(cursor);
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
        destroyLoader(LOADER_CLIENT_EXERCISE);
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
