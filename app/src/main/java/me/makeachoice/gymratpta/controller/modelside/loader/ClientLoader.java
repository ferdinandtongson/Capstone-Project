package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientColumns;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;

/**************************************************************************************************/
/*
 * ClientLoader is a cursor loader that loads data from client table
 */
/**************************************************************************************************/

public class ClientLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      mStatus - client status value
 *
 *      OnClientLoadListener mListener - listens for when the client data is loaded
 *      interface OnClientLoadListener:
 *          onClientLoadFinished(Cursor) - notifies listener client data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private static MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private static String mUserId;

    //mStatus - client status value
    private static String mStatus;

    //mListener - listens for when client data is loaded
    private static OnClientLoadListener mListener;
    public interface OnClientLoadListener{
        //notifies listener client data has finished loading
        public void onClientLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadClient(...) - start loader to load client data from database
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/

    /*
     * void loadClient(...) - start loader to load client data from database
     */
    public static void loadClients(MyActivity activity, String userId, OnClientLoadListener listener){
        //load exercise using default loader exercise id
        loadClients(activity, userId, LOADER_CLIENT, listener);
    }

    /*
     * void loadClients(...) - start loader to load client data from database
     */
    public static void loadClients(MyActivity activity, String userId, int loaderId, OnClientLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.ClientEntry.buildClientByUID(mUserId);
                        Log.d("Choice", "ClientLoader: " + uri.toString());

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientColumns.PROJECTION,
                                null,
                                null,
                                Contractor.ClientEntry.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onClientLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadClientsByStatus(...) - start loader to load client data from database by status
     */
    public static void loadClientsByStatus(MyActivity activity, String userId, String status,
                                           OnClientLoadListener listener){
        //load exercise using default loader exercise id
        loadClientsByStatus(activity, userId, status, LOADER_CLIENT, listener);
    }

    /*
     * void loadClientsByStatus(...) - start loader to load client data from database by status
     */
    public static void loadClientsByStatus(MyActivity activity, String userId, String status,
                                           int loaderId, OnClientLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

        //get status
        mStatus = status;

        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.ClientEntry.buildClientByStatus(mUserId, mStatus);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientColumns.PROJECTION,
                                null,
                                null,
                                Contractor.ClientEntry.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that client data has finished loading
                            mListener.onClientLoadFinished(cursor);
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
