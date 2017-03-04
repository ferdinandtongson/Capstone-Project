package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientRoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ClientRoutineLoader;
import me.makeachoice.gymratpta.controller.modelside.query.ClientRoutineQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineFBItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  ClientRoutineButler assists in loading, saving and deleting client routine to database and firebase
 */
/**************************************************************************************************/

public class ClientRoutineButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mRoutineList - list loaded from database
    private ArrayList<ClientRoutineItem> mRoutineList;

    private ClientRoutineItem mDeleteItem;

    //mLoaderId - loader id number
    private int mLoaderId;

    //ClientRoutineLoader - loader class used to load data
    private ClientRoutineLoader mRoutineLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<ClientRoutineItem> routineList);
    }

    //mSaveListener - used to listen for save events
    private OnSavedListener mSaveListener;
    public interface OnSavedListener{
        public void onSaved();
    }

    //mDeleteListener - used to listen for delete events
    private OnDeletedListener mDeleteListener;
    public interface OnDeletedListener{
        public void onDeleted();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientRoutineButler(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;

        mRoutineList = new ArrayList<>();
        mRoutineLoader = new ClientRoutineLoader(mActivity, mUserId, clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load
 */
/**************************************************************************************************/
    /*
     * void loadClientRoutinesByClientKey() - load data from database
     */
    public void loadClientRoutinesByClientKey(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mRoutineLoader.loadClientRoutineByClientKey(mLoaderId, new ClientRoutineLoader.OnClientRoutineLoadListener() {
            @Override
            public void onClientRoutineLoadFinished(Cursor cursor) {
                onClientRoutineLoaded(cursor);
            }
        });
    }

    /*
     * void loadClientRoutinesByTimestamp() - load data from database
     */
    public void loadClientRoutinesByTimestamp(String timestamp, int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mRoutineLoader.loadClientRoutineByTimestamp(timestamp, mLoaderId, new ClientRoutineLoader.OnClientRoutineLoadListener() {
            @Override
            public void onClientRoutineLoadFinished(Cursor cursor) {
                onClientRoutineLoaded(cursor);
            }
        });
    }

    /*
     * void onClientRoutineLoaded(Cursor) - data from database has been loaded
     */
    private void onClientRoutineLoaded(Cursor cursor){
        //clear list array
        mRoutineList.clear();

        //get number loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            ClientRoutineItem item = new ClientRoutineItem(cursor);

            //add item to list
            mRoutineList.add(item);
        }

        //destroy loader
        mRoutineLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(mRoutineList);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save
 */
/**************************************************************************************************/

    public void saveClientRoutine(ClientRoutineItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

        //save to local database
        saveToDatabase(saveItem);

    }

    /*
     * void saveToFB(StatsItem) - save to firebase
     */
    private void saveToFirebase(ClientRoutineItem saveItem){
        //get firebase helper instance
        ClientRoutineFirebaseHelper fbHelper = ClientRoutineFirebaseHelper.getInstance();

        //create firebase item
        RoutineFBItem fbItem = new RoutineFBItem();
        fbItem.exercise = saveItem.exercise;
        fbItem.category = saveItem.category;
        fbItem.numOfSets = saveItem.numOfSets;

        //save to firebase
        fbHelper.addRoutineByTimestamp(mUserId, saveItem.clientKey, saveItem.timestamp, saveItem.orderNumber, fbItem);

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(ClientRoutineItem saveItem){
        //get uri value for table
        Uri uriValue = ClientRoutineContract.CONTENT_URI;

        //add to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete
 */
/**************************************************************************************************/
    /*
     * void deleteClientRoutine(...) - delete from firebase
     */
    public void deleteClientRoutine(ClientRoutineItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;

        //get firebase helper instance
        ClientRoutineFirebaseHelper fbHelper = ClientRoutineFirebaseHelper.getInstance();

        //delete notes from firebase
        fbHelper.deleteClientRoutine(mUserId, clientKey, timestamp, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
                deleteFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

    }

    /*
     * void deleteClientRoutineAtOrderNumber(...) - delete from firebase
     */
    public void deleteClientRoutineAtOrderNumber(ClientRoutineItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;
        String orderNumber = String.valueOf(deleteItem.orderNumber);

        //get firebase helper instance
        ClientRoutineFirebaseHelper fbHelper = ClientRoutineFirebaseHelper.getInstance();

        //delete notes from firebase
        fbHelper.deleteClientRoutineAtOrderNumber(mUserId, clientKey, timestamp, orderNumber,
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        deleteOrderNumberFromDatabase(mDeleteItem);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //does nothing
                    }
                });

    }

    /*
     * void deleteFromDatabase(...) - delete data from database
     */
    private void deleteOrderNumberFromDatabase(ClientRoutineItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;
        String orderNumber = String.valueOf(deleteItem.orderNumber);

        //get uri value for table
        Uri uri = ClientRoutineContract.CONTENT_URI;

        //remove notes from database
        int delete = mActivity.getContentResolver().delete(uri,
                ClientRoutineQueryHelper.orderNumberSelection,
                new String[]{mUserId, clientKey, orderNumber, timestamp});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

    /*
     * void deleteFromDatabase(...) - delete data from database
     */
    private void deleteFromDatabase(ClientRoutineItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;
        String exercise = deleteItem.exercise;

        //get uri value for table
        Uri uri = ClientRoutineContract.CONTENT_URI;

        //remove notes from database
        mActivity.getContentResolver().delete(uri,
                ClientRoutineQueryHelper.timestampSelection,
                new String[]{mUserId, clientKey, timestamp});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/


}
