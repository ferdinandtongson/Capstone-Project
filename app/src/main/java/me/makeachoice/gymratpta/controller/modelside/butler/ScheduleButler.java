package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientScheduleFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ScheduleFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ScheduleLoader;
import me.makeachoice.gymratpta.controller.modelside.query.ScheduleQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.item.client.ScheduleFBItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  ScheduleButler assists in loading, saving and deleting appointment schedules to database and firebase
 */
/**************************************************************************************************/

public class ScheduleButler {

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
    private ScheduleItem mSaveItem;

    //mDeleteItem - appointment item to delete from database
    private ScheduleItem mDeleteItem;

    //mScheduleList - list of scheduled appointments loaded from database
    private ArrayList<ScheduleItem> mScheduleList;

    //mLoaderId - loader id number
    private int mLoaderId;

    //mScheduleLoader - loader class used to load schedule data
    private ScheduleLoader mScheduleLoader;

    //mLoadListener - used to listen for onScheduleLoaded events
    private OnScheduleLoadedListener mLoadListener;
    public interface OnScheduleLoadedListener{
        public void onScheduleLoaded(ArrayList<ScheduleItem> scheduleList);
    }

    //mSaveListener - used to listen for onScheduleSaved events
    private OnScheduleSavedListener mSaveListener;
    public interface OnScheduleSavedListener{
        public void onScheduleSaved();
    }

    //mDeleteListener - used to listen for onScheduleDeleted events
    private OnScheduleDeletedListener mDeleteListener;
    public interface OnScheduleDeletedListener{
        public void onScheduleDeleted();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ScheduleButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        mScheduleList = new ArrayList<>();
        mScheduleLoader = new ScheduleLoader(mActivity, mUserId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Schedule
 */
/**************************************************************************************************/
    /*
     * void loadSchedule() - load schedule data for given day from database
     */
    public void loadSchedule(int loaderId, String datestamp, OnScheduleLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get schedule data from database
        mScheduleLoader.loadScheduleByTimestamp(datestamp, mLoaderId,
                new ScheduleLoader.OnScheduleLoadListener() {
                    @Override
                    public void onScheduleLoadFinished(Cursor cursor){
                        onScheduleLoaded(cursor);
                    }
                });
    }

    /*
     * void onScheduleLoaded(Cursor) - schedule from database has been loaded
     */
    private void onScheduleLoaded(Cursor cursor){
        //clear schedule list array
        mScheduleList.clear();

        //get number of scheduled appointments loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            ScheduleItem item = new ScheduleItem(cursor);

            //add item to list
            mScheduleList.add(item);
        }

        //destroy loader
        mScheduleLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onScheduleLoaded(mScheduleList);
        }
    }

    /*
     * void loadRangeSchedule() - load schedule data for given week from database
     */
    public void loadRangeSchedule(int loaderId, String[] dateRange, OnScheduleLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get schedule data from database
        mScheduleLoader.loadScheduleByRange(dateRange, mLoaderId,
                new ScheduleLoader.OnScheduleLoadListener() {
                    @Override
                    public void onScheduleLoadFinished(Cursor cursor){
                        onScheduleLoaded(cursor);
                    }
                });
    }



/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Schedule
 */
/**************************************************************************************************/

    public void saveSchedule(ScheduleItem saveItem, OnScheduleSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveScheduleToFirebase(saveItem);

        //save to local database
        saveScheduleToDatabase(saveItem);

    }

    /*
     * void saveScheduleToFirebase(ScheduleItem) - save to firebase
     */
    private void saveScheduleToFirebase(ScheduleItem saveItem){
        //save schedule to schedule firebase
        saveScheduleToScheduleFB(saveItem);

        //save schedule to client schedule firebase
        saveScheduleToClientScheduleFB(saveItem);
    }

    /*
     * void saveScheduleToScheduleFB(ScheduleItem) - save schedule to schedule firebase
     */
    private void saveScheduleToScheduleFB(ScheduleItem saveItem){
        //get schedule firebase helper instance
        ScheduleFirebaseHelper appointmentFB = ScheduleFirebaseHelper.getInstance();

        //create schedule firebase item
        ScheduleFBItem appointmentFBItem = new ScheduleFBItem();
        appointmentFBItem.appointmentDate = saveItem.appointmentDate;
        appointmentFBItem.appointmentTime = saveItem.appointmentTime;
        appointmentFBItem.clientKey = saveItem.clientKey;
        appointmentFBItem.clientName = saveItem.clientName;
        appointmentFBItem.routineName = saveItem.routineName;
        appointmentFBItem.status = saveItem.status;

        //save schedule item to firebase
        appointmentFB.addScheduleByTimestamp(mUserId, saveItem.datestamp, appointmentFBItem);

    }

    /*
     * void saveScheduleToClientScheduleFB(ScheduleItem) - save schedule to client schedule firebase
     */
    private void saveScheduleToClientScheduleFB(ScheduleItem saveItem){
        //get client schedule firebase helper instance
        ClientScheduleFirebaseHelper clientFB = ClientScheduleFirebaseHelper.getInstance();

        //create client schedule firebase item
        ClientAppFBItem clientFBItem = new ClientAppFBItem();
        clientFBItem.appointmentDate = saveItem.appointmentDate;
        clientFBItem.appointmentTime = saveItem.appointmentTime;
        clientFBItem.clientName = saveItem.clientName;
        clientFBItem.routineName = saveItem.routineName;
        clientFBItem.status = saveItem.status;

        //save client schedule to firebase
        clientFB.addClientScheduleByClientKey(mUserId, saveItem.clientKey, clientFBItem);
    }

    /*
     * void saveScheduleToDatabase(...) - save schedule to local database
     */
    private void saveScheduleToDatabase(ScheduleItem saveItem){
        //get uri value for userSchedule table
        Uri uriValue = ScheduleContract.CONTENT_URI;

        //schedule is new, add schedule to database
        Uri uri = mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onScheduleSaved();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Schedule
 */
/**************************************************************************************************/
    /*
     * void deleteSchedule(...) - delete schedule
     */
    public void deleteSchedule(ScheduleItem deleteItem, OnScheduleDeletedListener listener){

        //create string values used to delete schedule
        String timestamp = deleteItem.datestamp;
        String appDate = deleteItem.appointmentDate;
        final String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;
        String clientName = deleteItem.clientName;

        //mSaveItem = saveItem;
        mDeleteItem = deleteItem;
        mDeleteListener = listener;

        //get schedule firebase helper instance
        ScheduleFirebaseHelper appointmentFB = ScheduleFirebaseHelper.getInstance();

        //delete schedule from firebase
        appointmentFB.deleteSchedule(mUserId, String.valueOf(timestamp), clientName, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ScheduleFBItem appointment = postSnapshot.getValue(ScheduleFBItem.class);
                    if(appointment.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

        //get client schedule firebase helper
        ClientScheduleFirebaseHelper clientFB = ClientScheduleFirebaseHelper.getInstance();
        //delete client schedule from firebase
        clientFB.deleteSchedule(mUserId, clientKey, appDate, appTime, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClientAppFBItem appointment = postSnapshot.getValue(ClientAppFBItem.class);
                    if(appointment.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }
                deleteScheduleFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

    }

    /*
     * void deleteScheduleFromDatabase(...) - delete schedule data from database
     */
    private void deleteScheduleFromDatabase(ScheduleItem deleteItem){
        //create string values used to delete schedule
        String appDate = deleteItem.appointmentDate;
        String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;

        //get uri value from userSchedule table
        Uri uri = ScheduleContract.CONTENT_URI;

        //remove appointment schedule from database
        int deleted = mActivity.getContentResolver().delete(uri,
                ScheduleQueryHelper.clientKeyDateTimeSelection,
                new String[]{mUserId, clientKey, appDate, appTime});

        if(mDeleteListener != null){
            mDeleteListener.onScheduleDeleted();
        }
    }


/**************************************************************************************************/

}
