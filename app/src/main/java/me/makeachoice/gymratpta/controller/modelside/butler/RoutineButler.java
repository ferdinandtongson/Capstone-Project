package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.RoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineLoader;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineQueryHelper;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;
import me.makeachoice.gymratpta.model.item.exercise.RoutineFBItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  RoutineButler assists in loading, saving and deleting exercises to database and firebase
 */
/**************************************************************************************************/

public class RoutineButler {

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
    private ArrayList<RoutineItem> mRoutineList;

    private RoutineItem mDeleteItem;

    //mLoaderId - loader id number
    private int mLoaderId;

    //RoutineLoader - loader class used to load data
    private RoutineLoader mRoutineLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<RoutineItem> routineList);
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

    public RoutineButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        mRoutineList = new ArrayList<>();
        mRoutineLoader = new RoutineLoader(mActivity, mUserId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load
 */
/**************************************************************************************************/
    /*
     * void loadRoutines() - load data from database
     */
    public void loadRoutines(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mRoutineLoader.loadRoutines(mLoaderId, new RoutineLoader.OnRoutineLoadListener() {
            @Override
            public void onRoutineLoadFinished(Cursor cursor) {
                onRoutineLoaded(cursor);
            }
        });
    }

    /*
     * void loadRoutineByName() - load data from database
     */
    public void loadRoutineByName(String routineName, int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mRoutineLoader.loadRoutineByName(routineName, mLoaderId, new RoutineLoader.OnRoutineLoadListener() {
            @Override
            public void onRoutineLoadFinished(Cursor cursor) {
                onRoutineLoaded(cursor);
            }
        });
    }

    /*
     * void onRoutineLoaded(Cursor) - data from database has been loaded
     */
    private void onRoutineLoaded(Cursor cursor){
        //clear list array
        mRoutineList.clear();

        //get number loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            RoutineItem item = new RoutineItem(cursor);

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

    public void saveRoutine(RoutineItem saveItem, RoutineButler.OnSavedListener listener){
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
    private void saveToFirebase(RoutineItem saveItem){
        //get firebase helper instance
        RoutineFirebaseHelper fbHelper = RoutineFirebaseHelper.getInstance();

        //create firebase item
        RoutineFBItem fbItem = new RoutineFBItem();
        fbItem.exercise = saveItem.exercise;
        fbItem.category = saveItem.category;
        fbItem.numOfSets = saveItem.numOfSets;

        //save to firebase
        fbHelper.addRoutine(mUserId, saveItem, fbItem);

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(RoutineItem saveItem){
        //get uri value for table
        Uri uriValue = RoutineContract.CONTENT_URI;

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
     * void deleteRoutine(...) - delete from firebase
     */
    public void deleteRoutine(RoutineItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete
        String routineName = deleteItem.routineName;

        //get firebase helper instance
        RoutineFirebaseHelper fbHelper = RoutineFirebaseHelper.getInstance();

        //delete notes from firebase
        fbHelper.deleteRoutine(mUserId, routineName, new ValueEventListener() {
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
     * void deleteRoutineExerciseByIndex(...) - delete from firebase
     */
    public void deleteRoutineExerciseByIndex(RoutineItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete
        String routineName = deleteItem.routineName;
        String orderNumber = deleteItem.orderNumber;

        //get firebase helper instance
        RoutineFirebaseHelper fbHelper = RoutineFirebaseHelper.getInstance();

        //delete notes from firebase
        fbHelper.deleteRoutineExerciseByIndex(mUserId, routineName, orderNumber,
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
    private void deleteOrderNumberFromDatabase(RoutineItem deleteItem){
        //create string values used to delete notes
        String routineName = deleteItem.routineName;
        String orderNumber = deleteItem.orderNumber;

        //get uri value for table
        Uri uri = RoutineContract.CONTENT_URI;

        //remove notes from database
        int delete = mActivity.getContentResolver().delete(uri,
                RoutineQueryHelper.orderNumberSelection,
                new String[]{mUserId, routineName, orderNumber});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

    /*
     * void deleteFromDatabase(...) - delete data from database
     */
    private void deleteFromDatabase(RoutineItem deleteItem){
        //create string values used to delete notes
        String routineName = deleteItem.routineName;

        //get uri value for table
        Uri uri = RoutineContract.CONTENT_URI;

        //remove notes from database
        mActivity.getContentResolver().delete(uri,
                RoutineQueryHelper.routineNameSelection,
                new String[]{mUserId, routineName});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/


}
