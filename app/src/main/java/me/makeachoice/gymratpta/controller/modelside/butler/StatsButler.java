package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.client.StatsFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.StatsLoader;
import me.makeachoice.gymratpta.controller.modelside.query.StatsQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.item.client.StatsFBItem;
import me.makeachoice.gymratpta.model.item.client.StatsItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  StatsButler assists in loading, saving and deleting client stats to database and firebase
 */
/**************************************************************************************************/

public class StatsButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mSaveItem - appointment item to save to database
    //private ScheduleItem mSaveItem;

    //mDeleteItem - delete item from database
    private StatsItem mDeleteItem;

    //mStatsList - list of stats loaded from database
    private ArrayList<StatsItem> mStatsList;

    //mLoaderId - loader id number
    private int mLoaderId;

    //mStatsLoader - loader class used to load stats data
    private StatsLoader mStatsLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<StatsItem> statsList);
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

    public StatsButler(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;

        mStatsList = new ArrayList<>();
        mStatsLoader = new StatsLoader(mActivity, mUserId, clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Stats
 */
/**************************************************************************************************/
    /*
     * void loadStats() - load stats data from database
     */
    public void loadStats(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get stats data from database
        mStatsLoader.loadStatsByClientKey(mLoaderId, new StatsLoader.OnStatsLoadListener() {
            @Override
            public void onStatsLoadFinished(Cursor cursor) {
                onStatsLoaded(cursor);
            }
        });
    }

    /*
     * void onStatsLoaded(Cursor) - data from database has been loaded
     */
    private void onStatsLoaded(Cursor cursor){
        //clear list array
        mStatsList.clear();

        //get number of scheduled appointments loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            StatsItem item = new StatsItem(cursor);

            //add item to list
            mStatsList.add(item);
        }

        //destroy loader
        mStatsLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(mStatsList);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Stats
 */
/**************************************************************************************************/

    public void saveStats(StatsItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

        //save to local database
        saveToDatabase(saveItem);

    }

    /*
     * void saveToStatsFB(StatsItem) - save notes to notes firebase
     */
    private void saveToFirebase(StatsItem saveItem){
        //get firebase helper instance
        StatsFirebaseHelper fbHelper = StatsFirebaseHelper.getInstance();

        //create firebase item
        StatsFBItem fbItem = new StatsFBItem();
        fbItem.appointmentDate = saveItem.appointmentDate;
        fbItem.appointmentTime = saveItem.appointmentTime;
        fbItem.modifiedDate = saveItem.modifiedDate;
        fbItem.statWeight = saveItem.statWeight;
        fbItem.statBodyFat = saveItem.statBodyFat;
        fbItem.statBMI = saveItem.statBMI;
        fbItem.statNeck = saveItem.statNeck;
        fbItem.statChest = saveItem.statChest;
        fbItem.statRBicep = saveItem.statRBicep;
        fbItem.statLBicep = saveItem.statLBicep;
        fbItem.statWaist = saveItem.statWaist;
        fbItem.statNavel = saveItem.statNavel;
        fbItem.statHips = saveItem.statHips;
        fbItem.statRThigh = saveItem.statRThigh;
        fbItem.statLThigh = saveItem.statLThigh;
        fbItem.statRCalf = saveItem.statRCalf;
        fbItem.statLCalf = saveItem.statLCalf;

        //save notes item to firebase
        fbHelper.addStatsByTimestamp(mUserId, saveItem.clientKey, saveItem.timestamp, fbItem);

    }

    /*
     * void saveToDatabase(...) - save notes to local database
     */
    private void saveToDatabase(StatsItem saveItem){
        //get uri value for routine name table
        Uri uriValue = StatsContract.CONTENT_URI;

        //appointment is new, add notes to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Stats
 */
/**************************************************************************************************/
    /*
     * void deleteStats(...) - delete notes from firebase
     */
    public void deleteStats(StatsItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String appDate = deleteItem.appointmentDate;
        final String appTime = deleteItem.appointmentTime;

        //get notes firebase helper instance
        StatsFirebaseHelper notesFB = StatsFirebaseHelper.getInstance();
        //delete notes from firebase
        notesFB.deleteStats(mUserId, clientKey, appDate, appTime, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    StatsFBItem notes = postSnapshot.getValue(StatsFBItem.class);
                    if(notes.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }
                deleteStatsFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

    }

    /*
     * void deleteStatsFromDatabase(...) - delete notes data from database
     */
    private void deleteStatsFromDatabase(StatsItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String appDate = deleteItem.appointmentDate;
        String appTime = deleteItem.appointmentTime;

        //get uri value from notes table
        Uri uri = StatsContract.CONTENT_URI;

        //remove notes from database
        mActivity.getContentResolver().delete(uri,
                StatsQueryHelper.clientKeyDateTimeSelection,
                new String[]{mUserId, clientKey, appDate, appTime});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/

}
