package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import me.makeachoice.gymratpta.model.contract.client.ClientContract;
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
 *      mFKey - client fkey value
 *
 *      OnClientLoadListener mListener - listens for when the client data is loaded
 *      interface OnClientLoadListener:
 *          onClientLoadFinished(Cursor) - notifies listener client data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mStatus - client status value
    private String mStatus;

    //mFKey - client fkey value
    private String mFKey;

    //mListener - listens for when client data is loaded
    private OnClientLoadListener mListener;
    public interface OnClientLoadListener{
        //notifies listener client data has finished loading
        public void onClientLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

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
    public void loadClients(OnClientLoadListener listener){
        //load exercise using default loader exercise id
        loadClients(LOADER_CLIENT, listener);
    }

    /*
     * void loadClients(...) - start loader to load client data from database
     */
    public void loadClients(int loaderId, OnClientLoadListener listener){
        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = ClientContract.buildClientByUID(mUserId);
                        Log.d("Choice", "ClientLoader: " + uri.toString());

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientContract.PROJECTION,
                                null,
                                null,
                                ClientContract.SORT_ORDER_DEFAULT);
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
    public void loadClientsByStatus(String status, OnClientLoadListener listener){
        //load exercise using default loader exercise id
        loadClientsByStatus(status, LOADER_CLIENT, listener);
    }

    /*
     * void loadClientsByStatus(...) - start loader to load client data from database by status
     */
    public void loadClientsByStatus(String status, int loaderId, OnClientLoadListener listener){
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
                        Uri uri = ClientContract.buildClientByStatus(mUserId, mStatus);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientContract.PROJECTION,
                                null,
                                null,
                                ClientContract.SORT_ORDER_DEFAULT);
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
     * void loadClientsByFKey(...) - start loader to load client data from database by fkey
     */
    public void loadClientsByFKey(String fkey, OnClientLoadListener listener){
        //load exercise using default loader exercise id
        loadClientsByFKey(fkey, LOADER_CLIENT, listener);
    }


    /*
     * void loadClientsByFKey(...) - start loader to load client data from database by fkey
     */
    public void loadClientsByFKey(String fkey, int loaderId, OnClientLoadListener listener){
        //get fkey
        mFKey = fkey;

        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = ClientContract.buildClientByFKey(mUserId, mFKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ClientContract.PROJECTION,
                                null,
                                null,
                                ClientContract.SORT_ORDER_DEFAULT);
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
