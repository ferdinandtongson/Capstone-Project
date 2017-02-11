package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseColumns;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_EXERCISE_BASE;

/**************************************************************************************************/
/*
 * ExerciseLoader is a cursor loader that loads data from exercise table
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
    private static MyActivity mActivity;

    //mUserId - user id number taken from firebase authentication
    private static String mUserId;

    //mCategoryKey - category he list of exercises should belong to
    private static String mCategoryKey;

    //mListener - listens for when the category data is loaded
    private static OnExerciseLoadListener mListener;
    public interface OnExerciseLoadListener{
        //notifies listener exercise data has finished loading
        public void onExerciseLoadFinished(Cursor cursor);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      void loadCategories(...) - start loader to load category data from database
 *      void destroyLoader(...) - destroy loader and any data managed by the loader
 */
/**************************************************************************************************/
    /*
     * void loadExercises(...) - start loader to load exercise data from database
     */
     public static void loadExercises(MyActivity activity, String userId, String categoryKey,
                                      OnExerciseLoadListener listener){
         //load exercise using default loader exercise id
         loadExercises(activity, userId, categoryKey, LOADER_EXERCISE_BASE, listener);
     }


    /*
     * void loadExercises(...) - start loader to load exercise data from database
     */
    public static void loadExercises(MyActivity activity, String userId, String categoryKey,
                                     int loaderId, OnExerciseLoadListener listener){
        //get activity context
        mActivity = activity;

        //get user id
        mUserId = userId;

        //get category key
        mCategoryKey = categoryKey;

        //get listener
        mListener = listener;

        // Initializes exercise loader
        mActivity.getSupportLoaderManager().initLoader(LOADER_EXERCISE_BASE, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.ExerciseEntry.buildExerciseByCategoryKey(mUserId, mCategoryKey);

                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                ExerciseColumns.PROJECTION_EXERCISE,
                                null,
                                null,
                                Contractor.ExerciseEntry.SORT_ORDER_DEFAULT);
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
    public static void destroyLoader(MyActivity activity){
        //destroy loader using default loader exercise id
        activity.getSupportLoaderManager().destroyLoader(LOADER_EXERCISE_BASE);
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
