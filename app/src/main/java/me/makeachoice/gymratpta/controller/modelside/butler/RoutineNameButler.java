package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.RoutineNameFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineNameLoader;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameContract;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  RoutineNameButler assists in loading, saving and deleting exercises to database and firebase
 */
/**************************************************************************************************/

public class RoutineNameButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mDeleteItem - item to be deleted
    private RoutineNameItem mDeleteItem;

    //mStatsList - list of data loaded from database
    private ArrayList<RoutineNameItem> mNameList;

    //mLoaderId - loader id number
    private int mLoaderId;

    //RoutineNameLoader - loader class used to load data
    private RoutineNameLoader mRoutineNameLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<RoutineNameItem> routineList);
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

    public RoutineNameButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        mNameList = new ArrayList<>();
        mRoutineNameLoader = new RoutineNameLoader(mActivity, mUserId);
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
    public void loadRoutineNames(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mRoutineNameLoader.loadRoutineNames(mLoaderId, new RoutineNameLoader.OnRoutineNameLoadListener() {
            @Override
            public void onRoutineNameLoadFinished(Cursor cursor) {
                onRoutineNamesLoaded(cursor);
            }
        });
    }

    /*
     * void onRoutineNamesLoaded(Cursor) - data from database has been loaded
     */
    private void onRoutineNamesLoaded(Cursor cursor){
        //clear list array
        mNameList.clear();

        //get number loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            RoutineNameItem item = new RoutineNameItem(cursor);

            //add item to list
            mNameList.add(item);
        }

        //destroy loader
        mRoutineNameLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(mNameList);
        }

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save
 */
/**************************************************************************************************/

    public void saveRoutineName(RoutineNameItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

        //save to local database
        saveToDatabase(saveItem);

    }

    /*
     * void saveToFirebase(RoutineNameItem) - save to firebase
     */
    private void saveToFirebase(RoutineNameItem saveItem){
        //get firebase helper instance
        RoutineNameFirebaseHelper fbHelper = RoutineNameFirebaseHelper.getInstance();

        //save notes item to firebase
        fbHelper.addRoutineName(mUserId, saveItem.routineName);

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(RoutineNameItem saveItem){
        //get uri value to table
        Uri uriValue = RoutineNameContract.CONTENT_URI;

        //appointment is new, add notes to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/


}
