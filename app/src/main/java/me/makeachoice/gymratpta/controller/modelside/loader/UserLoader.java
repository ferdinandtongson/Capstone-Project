package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.user.UserContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_NOTES;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_USER;

/**************************************************************************************************/
/*
 *  UserLoader load data from user table
 */
/**************************************************************************************************/

public class UserLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mClientKey - client key value
 *      String mAppointmentDate - appointment date value
 *      mAppointmentTime - appointment time value
 *
 *      OnNotesLoadListener mListener - listens for when notes data is loaded
 *      interface OnNotesLoadListener:
 *          onNotesLoadFinished(Cursor) - notifies listener notes data has finished loading
 */
/**************************************************************************************************/

    private MyActivity mActivity;
    private String mUserId;

    //mListener - listens for when notes data is loaded
    private OnUserLoadListener mListener;
    public interface OnUserLoadListener{
        //notifies listener notes data has finished loading
        void onUserLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public UserLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 */
/**************************************************************************************************/
    /*
     * void loadUser(...) - start loader to load user data from database
     */
    public void loadUser(OnUserLoadListener listener){
        //load notes using default loader id
        loadUser(LOADER_USER, listener);
    }

    /*
     * void loadUser(...) - start loader to load user data from database
     */
    public void loadUser(int loaderId, OnUserLoadListener listener){
        //get listener
        mListener = listener;

        // Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = UserContract.buildUserByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                UserContract.PROJECTION,
                                null,
                                null,
                                UserContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that data has finished loading
                            mListener.onUserLoadFinished(cursor);
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
        destroyLoader(LOADER_NOTES);
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
