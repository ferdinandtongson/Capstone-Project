package me.makeachoice.gymratpta.controller.modelside.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryColumns;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CATEGORY;

/**
 * Created by Usuario on 2/9/2017.
 */

public class CategoryLoader {
    private static MyActivity mActivity;
    private static String mUserId;

    private static OnCategoryLoadListener mListener;
    public interface OnCategoryLoadListener{
        public void onCategoryLoadFinished(Cursor cursor);
    }

    public static void loadCategories(MyActivity activity, String userId, OnCategoryLoadListener listener){
        mActivity = activity;
        mListener = listener;
        mUserId = userId;

        // Initializes a loader for loading clients
        mActivity.getSupportLoaderManager().initLoader(LOADER_CATEGORY, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.CategoryEntry.buildCategoryByUID(mUserId);
                        //get cursor
                        return new CursorLoader(
                                mActivity,
                                uri,
                                CategoryColumns.PROJECTION_CATEGORY,
                                null,
                                null,
                                Contractor.CategoryEntry.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        mListener.onCategoryLoadFinished(cursor);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    }
                });
    }

}
