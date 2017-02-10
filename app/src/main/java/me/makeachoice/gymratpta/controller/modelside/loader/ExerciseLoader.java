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

/**
 * Created by Usuario on 2/9/2017.
 */

public class ExerciseLoader {

    private static MyActivity mActivity;
    private static String mUserId;
    private static String mCategoryKey;

    private static OnExerciseLoadListener mListener;
    public interface OnExerciseLoadListener{
        public void onExerciseLoadFinished(Cursor cursor);
    }

    public static void loadExercises(MyActivity activity, String userId, String categoryKey, OnExerciseLoadListener listener){
        mActivity = activity;
        mListener = listener;
        mUserId = userId;
        mCategoryKey = categoryKey;


        // Initializes a loader for loading clients
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
                        mListener.onExerciseLoadFinished(cursor);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    }
                });
    }

}
