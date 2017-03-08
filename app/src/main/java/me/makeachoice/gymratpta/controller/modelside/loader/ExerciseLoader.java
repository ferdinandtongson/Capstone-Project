package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_EXERCISE_BASE;

/**************************************************************************************************/
/*
 * ExerciseLoader loads data from exercise table
 */
/**************************************************************************************************/

public class ExerciseLoader {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number taken from firebase authentication
 *      String mCategoryKey - category he list of exercises should belong to
 *      OnExerciseLoadListener mListener - listens for when the exercise data is loaded
 *
 *      interface OnExerciseLoadListener:
 *          onExerciseLoadFinished(Cursor) - notifies listener exercise data has finished loading
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private String mUserId;

    //mCategoryKey - category he list of exercises should belong to
    private String mCategoryKey;

    private String mExercise;

    //mListener - listens for when the category data is loaded
    private OnExerciseLoadListener mListener;
    public interface OnExerciseLoadListener{
        //notifies listener exercise data has finished loading
        void onExerciseLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ExerciseLoader(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadExercises(...) - start loader to load exercise data from database
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadExercises(...) - start loader to load exercise data from database
     */
     public void loadExercises(String categoryKey, OnExerciseLoadListener listener){
         //load exercise using default loader exercise id
         loadExercises(categoryKey, LOADER_EXERCISE_BASE, listener);
     }

    /*
     * void loadExercises(...) - start loader to load exercise data from database
     */
    public void loadExercises(String categoryKey, int loaderId, OnExerciseLoadListener listener){

        //get category key
        mCategoryKey = categoryKey;

        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = ExerciseContract.buildExerciseByCategoryKey(mUserId, mCategoryKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ExerciseContract.PROJECTION_EXERCISE,
                                null,
                                null,
                                ExerciseContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that exercise data has finished loading
                            mListener.onExerciseLoadFinished(cursor);
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                        //does nothing
                    }
                });
    }

    /*
     * void loadExercisesByName(...) - start loader to load exercise data from database by name
     */
    public void loadExercisesByName(String categoryKey, String exercise, OnExerciseLoadListener listener){
        //load exercise using default loader exercise id
        loadExercisesByName(categoryKey, exercise, LOADER_EXERCISE_BASE, listener);
    }

    /*
     * void loadExercisesByName(...) - start loader to load exercise data from database
     */
    public void loadExercisesByName(String categoryKey, String exercise, int loaderId, OnExerciseLoadListener listener){

        mCategoryKey = categoryKey;
        mExercise = exercise;

        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(loaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = ExerciseContract.buildExerciseByName(mUserId, mCategoryKey, mExercise);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ExerciseContract.PROJECTION_EXERCISE,
                                null,
                                null,
                                ExerciseContract.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //make sure listener is not NULL
                        if(mListener != null){
                            //notify listener that exercise data has finished loading
                            mListener.onExerciseLoadFinished(cursor);
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
        mActivity.getSupportLoaderManager().destroyLoader(LOADER_EXERCISE_BASE);
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
