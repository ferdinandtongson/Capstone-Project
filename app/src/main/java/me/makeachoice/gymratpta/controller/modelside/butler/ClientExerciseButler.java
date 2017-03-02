package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientExerciseFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ClientExerciseLoader;
import me.makeachoice.gymratpta.controller.modelside.query.ClientExerciseQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.item.client.ClientExerciseFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientExerciseItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  ClientExerciseButler assists in loading, saving and deleting client exercises to database and firebase
 */
/**************************************************************************************************/

public class ClientExerciseButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mStatsList - list of stats loaded from database
    private ArrayList<ClientExerciseItem> mExerciseList;

    //mLoaderId - loader id number
    private int mLoaderId;

    //ClientExerciseLoader - loader class used to load client exercise data
    private ClientExerciseLoader mExerciseLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<ClientExerciseItem> exerciseList);
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

    public ClientExerciseButler(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;

        mExerciseList = new ArrayList<>();
        mExerciseLoader = new ClientExerciseLoader(mActivity, mUserId, clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load
 */
/**************************************************************************************************/
    /*
     * void loadClientExercisesByClientKey() - load client exercise data from database
     */
    public void loadClientExercisesByClientKey(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mExerciseLoader.loadClientExerciseByClientKey(mLoaderId, new ClientExerciseLoader.OnClientExerciseLoadListener() {
            @Override
            public void onClientExerciseLoadFinished(Cursor cursor) {
                onClientExerciseLoaded(cursor);
            }
        });
    }

    /*
     * void loadClientExercisesByTimestamp() - load client exercise data from database
     */
    public void loadClientExercisesByTimestamp(String timestamp, int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mExerciseLoader.loadClientExerciseByTimestamp(timestamp, mLoaderId, new ClientExerciseLoader.OnClientExerciseLoadListener() {
            @Override
            public void onClientExerciseLoadFinished(Cursor cursor) {
                onClientExerciseLoaded(cursor);
            }
        });
    }

    /*
     * void loadClientExercisesByExercise() - load client exercise data from database
     */
    public void loadClientExercisesByExercise(String exercise, int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mExerciseLoader.loadClientExerciseByExercise(exercise, mLoaderId, new ClientExerciseLoader.OnClientExerciseLoadListener() {
            @Override
            public void onClientExerciseLoadFinished(Cursor cursor) {
                onClientExerciseLoaded(cursor);
            }
        });
    }

    /*
     * void loadClientExercisesByTimestampExercise() - load client exercise data from database
     */
    public void loadClientExercisesByTimestampExercise(String timestamp, String exercise,
                                                       int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mExerciseLoader.loadClientExerciseByTimestampExercise(timestamp, exercise, mLoaderId,
                new ClientExerciseLoader.OnClientExerciseLoadListener() {
                    @Override
                    public void onClientExerciseLoadFinished(Cursor cursor) {
                        onClientExerciseLoaded(cursor);
                    }
                });
    }

    /*
     * void onClientExerciseLoaded(Cursor) - data from database has been loaded
     */
    private void onClientExerciseLoaded(Cursor cursor){
        //clear list array
        mExerciseList.clear();

        //get number loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            ClientExerciseItem item = new ClientExerciseItem(cursor);

            //add item to list
            mExerciseList.add(item);
        }

        //destroy loader
        mExerciseLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(mExerciseList);
        }

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save
 */
/**************************************************************************************************/

    public void saveClientExercise(ClientExerciseItem saveItem, OnSavedListener listener){
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
    private void saveToFirebase(ClientExerciseItem saveItem){
        //get firebase helper instance
        ClientExerciseFirebaseHelper fbHelper = ClientExerciseFirebaseHelper.getInstance();

        //create firebase item
        ClientExerciseFBItem fbItem = new ClientExerciseFBItem();
        fbItem.category = saveItem.category;
        fbItem.exercise = saveItem.exercise;
        fbItem.orderNumber = saveItem.orderNumber;
        fbItem.setNumber = saveItem.setNumber;
        fbItem.primaryLabel = saveItem.primaryLabel;
        fbItem.primaryValue = saveItem.primaryValue;
        fbItem.secondaryLabel = saveItem.secondaryLabel;
        fbItem.secondaryValue = saveItem.secondaryValue;

        //save to firebase
        fbHelper.addClientExerciseReferenceByTimestamp(mUserId, saveItem.clientKey, saveItem.timestamp, fbItem);

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(ClientExerciseItem saveItem){
        //get uri value for table
        Uri uriValue = ClientExerciseContract.CONTENT_URI;

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
     * void deleteClientExercise(...) - delete from firebase
     */
    public void deleteClientExercise(ClientExerciseItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;

        //create string values used to delete
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;
        final String exercise = deleteItem.exercise;

        //get firebase helper instance
        ClientExerciseFirebaseHelper fbHelper = ClientExerciseFirebaseHelper.getInstance();
        //delete notes from firebase
        fbHelper.deleteClientExerciseByExercise(mUserId, clientKey, timestamp, exercise, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

        deleteFromDatabase(deleteItem);
    }

    /*
     * void deleteFromDatabase(...) - delete data from database
     */
    private void deleteFromDatabase(ClientExerciseItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;
        String exercise = deleteItem.exercise;

        //get uri value for table
        Uri uri = ClientExerciseContract.CONTENT_URI;

        //remove notes from database
        mActivity.getContentResolver().delete(uri,
                ClientExerciseQueryHelper.clientKeyTimestampExerciseSelection,
                new String[]{mUserId, clientKey, timestamp, exercise});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/

}
