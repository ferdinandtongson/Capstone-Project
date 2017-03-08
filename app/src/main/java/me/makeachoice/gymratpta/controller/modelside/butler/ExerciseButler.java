package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.ExerciseFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ExerciseLoader;
import me.makeachoice.gymratpta.controller.modelside.query.ExerciseQueryHelper;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseFBItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  ExerciseButler assists in loading, saving and deleting exercises to database and firebase
 */
/**************************************************************************************************/

public class ExerciseButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mExerciseList - data loaded from database
    private ArrayList<ExerciseItem> mExerciseList;

    private ExerciseItem mDeleteItem;

    //mLoaderId - loader id number
    private int mLoaderId;

    //mExerciseLoader - loader class used to load data
    private ExerciseLoader mExerciseLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<ExerciseItem> exerciseList);
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

    public ExerciseButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        mExerciseList = new ArrayList<>();
        mExerciseLoader = new ExerciseLoader(mActivity, mUserId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load
 */
/**************************************************************************************************/
    /*
     * void loadExercises() - load data for given day from database
     */
    public void loadExercises(String categoryKey, int loaderId, ExerciseButler.OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mExerciseLoader.loadExercises(categoryKey, mLoaderId, new ExerciseLoader.OnExerciseLoadListener() {
            @Override
            public void onExerciseLoadFinished(Cursor cursor) {
                onExerciseLoaded(cursor);
            }
        });
    }

    public void loadExerciseByName(String categoryKey, String exercise, int loaderId,
                                   ExerciseButler.OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get data from database
        mExerciseLoader.loadExercisesByName(categoryKey, exercise, mLoaderId,
                new ExerciseLoader.OnExerciseLoadListener() {
                    @Override
                    public void onExerciseLoadFinished(Cursor cursor) {
                        onExerciseLoaded(cursor);
                    }
        });
    }

    /*
     * void onExerciseLoaded(Cursor) - data from database has been loaded
     */
    private void onExerciseLoaded(Cursor cursor){
        //clear list array
        mExerciseList.clear();

        //get number of scheduled appointments loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            ExerciseItem item = new ExerciseItem(cursor);

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

    public void saveExercise(ExerciseItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

        //save to local database
        saveToDatabase(saveItem);

    }

    /*
     * void saveToFirebase(ExerciseItem) - save to firebase
     */
    private void saveToFirebase(ExerciseItem saveItem){
        //get firebase helper instance
        ExerciseFirebaseHelper fbHelper = ExerciseFirebaseHelper.getInstance();

        //create firebase item
        ExerciseFBItem fbItem = new ExerciseFBItem();
        fbItem.exerciseName = saveItem.exerciseName;
        fbItem.exerciseCategory = saveItem.exerciseCategory;
        fbItem.recordPrimary = saveItem.recordPrimary;
        fbItem.recordSecondary = saveItem.recordSecondary;

        //save notes item to firebase
        fbHelper.addExerciseToCategory(mUserId, saveItem.categoryKey, fbItem);

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(ExerciseItem saveItem){
        //get uri value to table
        Uri uriValue = ExerciseContract.CONTENT_URI;

        //appointment is new, add notes to database
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
     * void deleteExercise(...) - delete from firebase
     */
    public void deleteExercise(ExerciseItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete notes
        String exercise = deleteItem.exerciseName;
        String categoryKey = deleteItem.categoryKey;

        //get notes firebase helper instance
        ExerciseFirebaseHelper fbHelper = ExerciseFirebaseHelper.getInstance();

        //delete from firebase
        fbHelper.deleteExercise(mUserId, categoryKey, exercise, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().removeValue();
                }
                deleteExerciseFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

    }

    /*
     * void deleteExerciseFromDatabase(...) - delete notes data from database
     */
    private void deleteExerciseFromDatabase(ExerciseItem deleteItem){
        //create string values used to delete notes
        String exercise = deleteItem.exerciseName;
        String categoryKey = deleteItem.categoryKey;

        //get uri value from notes table
        Uri uri = ExerciseContract.CONTENT_URI;

        //remove from database
        mActivity.getContentResolver().delete(uri,
                ExerciseQueryHelper.categoryKeyExerciseSelection,
                new String[]{mUserId, categoryKey, exercise});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/

}
