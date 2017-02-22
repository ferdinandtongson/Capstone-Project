package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.NotesColumns;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_NOTES;

/**************************************************************************************************/
/*
 *  NotesLoader load data from notes table
 */
/**************************************************************************************************/

public class NotesLoader {

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

    //mListener - listens for when notes data is loaded
    private OnNotesLoadListener mListener;
    public interface OnNotesLoadListener{
        //notifies listener notes data has finished loading
        public void onNotesLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public NotesLoader(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;
        mClientKey = clientKey;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadNotes(...) - start loader to load notes data from database
 *      void loadNotesByClientKey(...) - start loader to load notes data from database by clientKey
 *      void loadNotesByDateTime(...) - start loader to load notes data from database by date and time
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadNotes(...) - start loader to load notes data from database
     */
    public void loadNotes(OnNotesLoadListener listener){
        //load notes using default loader id
        loadNotes(LOADER_NOTES, listener);
    }

    /*
     * void loadNotes(...) - start loader to load notes data from database
     */
    public void loadNotes(int loaderId, OnNotesLoadListener listener){
        //get listener
        mListener = listener;

        // Initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = Contractor.NotesEntry.buildNotesByUID(mUserId);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                NotesColumns.PROJECTION,
                                null,
                                null,
                                Contractor.NotesEntry.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that data has finished loading
                            mListener.onNotesLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadNotesByClientKey(...) - start loader to load notes data from database by clientKey
     */
    public void loadNotesByClientKey(OnNotesLoadListener listener){
        //load appointment using default loader id
        loadNotesByClientKey(LOADER_NOTES, listener);
    }


    /*
     * void loadNotesByClientKey(...) - start loader to load notes data from database by clientKey
     */
    public void loadNotesByClientKey(int loaderId, OnNotesLoadListener listener){
        //get listener
        mListener = listener;

        //initializes loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request cursor from local database
                        Uri uri = Contractor.NotesEntry.buildNotesByClientKey(mUserId, mClientKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                NotesColumns.PROJECTION,
                                null,
                                null,
                                Contractor.NotesEntry.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onNotesLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadNotesByDateTime(...) - start loader to load notes data from database by date and time
     */
    public void loadNotesByDateTime(String appointmentDate, String appointmentTime, OnNotesLoadListener listener){

        //load appointment using default loader id
        loadNotesByDateTime(appointmentDate, appointmentTime, LOADER_NOTES, listener);
    }

    /*
     * void loadNotesByDateTime(...) - start loader to load notes data from database by date and time
     */
    public void loadNotesByDateTime(String appointmentDate, String appointmentTime,
                                    int loaderId, OnNotesLoadListener listener){
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
                        Uri uri = Contractor.NotesEntry.buildNotesByDateTime(mUserId, mClientKey,
                                mAppointmentDate, mAppointmentTime);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                NotesColumns.PROJECTION,
                                null,
                                null,
                                Contractor.NotesEntry.SORT_ORDER_DATE_TIME);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onNotesLoadFinished(cursor);
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
