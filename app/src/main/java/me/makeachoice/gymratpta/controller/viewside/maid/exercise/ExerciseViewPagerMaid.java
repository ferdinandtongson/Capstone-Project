package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.ExerciseRecyclerAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseColumns;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_EXERCISE_BASE;

/**************************************************************************************************/
/*
 * ExerciseViewPagerMaid is utilized by a ViewPager component to display a list of exercises separated
 * into categories by the ViewPager.
 *
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      MyFragment mFragment - fragment being maintained by the Maid
 *
 * Methods from MyMaid:
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      String getKey() - get maid key value
 *      Fragment getFragment() - get new instance fragment
 */
/**************************************************************************************************/

public class ExerciseViewPagerMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge,
        RecyclerView.OnCreateContextMenuListener, MyActivity.OnContextItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables
 *      int CONTEXT_MENU_EDIT - "edit" context menu id number
 *      int CONTEXT_MENU_DELETE = "delete" context menu id number
 *      ArrayList<ExerciseItem> mData - data list consumed by the adapter
 *      ExerciseRecyclerAdapter mAdapter - adapter consumed by recycler
 */
/**************************************************************************************************/

    //CONTEXT_MENU_EDIT - "edit" context menu id number
    private final static int CONTEXT_MENU_EDIT = 0;

    //CONTEXT_MENU_DELETE = "delete" context menu id number
    private final static int CONTEXT_MENU_DELETE = 1;

    //mData - data list consumed by the adapter
    private ArrayList<ExerciseItem> mData;

    //mAdapter - adapter consumed by recycler
    private ExerciseRecyclerAdapter mAdapter;

    private String mUserId;
    private String mCategoryKey;
    private int mLoaderId;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseViewPagerMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseViewPagerMaid(...) - constructor
     */
    public ExerciseViewPagerMaid(String maidKey, int layoutId, int index, CategoryItem item){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = item.uid;
        mCategoryKey = item.fkey;

        mLoaderId = LOADER_EXERCISE_BASE + index;

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  MyMaid Implementation
 *      View createView(LayoutInflater,ViewGroup,Bundle) - called by Fragment onCreateView()
 *      void createActivity(Bundle) - is called by Fragment onCreateActivity(...)
 *      void detach() - fragment is being disassociated from activity
 */
/**************************************************************************************************/
    /*
     * View createView(LayoutInflater, ViewGroup, Bundle) - is called by Fragment when onCreateView(...)
     * is called in that class. Prepares the Fragment View to be presented.
     */
    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState){
        //inflate fragment layout
        mLayout = inflater.inflate(mLayoutId, container, false);

        //return fragment
        return mLayout;
    }

    /*
     * void createActivity(Bundle) - is called by Fragment when onCreateActivity(...) is called in
     * that class. Sets child views in fragment before being seen by the user
     * @param bundle - saved instance states
     */
    @Override
    public void activityCreated(Bundle bundle){
        super.activityCreated(bundle);

        //load exercises
        loadExercises();
    }

    /*
     * void detach() - fragment is being disassociated from activity
     */
    @Override
    public void detach(){
        //fragment is being disassociated from Activity
        super.detach();
        mFragment.getActivity().getSupportLoaderManager().destroyLoader(mLoaderId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void prepareFragment(View) - prepare components and data to be displayed by fragment
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler to display exercise items
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(Cursor cursor){

        //initialize "empty" text, displayed if data is empty
        initializeEmptyText();

        //initialize adapter
        initializeAdapter(cursor);

        //initialize recycler view
        initializeRecycler();

        if(cursor != null && cursor.getCount() > 0){
            //check if data is empty
            isEmptyRecycler(false);
        }
        else{
            isEmptyRecycler(true);
        }
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" text message
        String emptyMsg = mLayout.getResources().getString(R.string.emptyRecycler_addExercise);

        //set message
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter(Cursor cursor) {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_simple;

        //create adapter consumed by the recyclerView
        mAdapter = new ExerciseRecyclerAdapter(mLayout.getContext(), cursor, adapterLayoutId);

        //set context menu create listener
        mAdapter.setOnCreateContextMenuListener(this);

    }

    /*
     * void initializeRecycler() - initialize recycler to display exercise items
     */
    private void initializeRecycler(){
        //add dividers between items
        //mBasicRecycler.showItemDivider(mFragment.getContext());

        //set adapter
        mBasicRecycler.setAdapter(mAdapter);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Context Menu Methods:
 *      void onCreateContextMenu(...) - create context menu
 *      boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
 */
/**************************************************************************************************/
    /*
     * void onCreateContextMenu(...) - create context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //get clientCardItem
        ExerciseItem item = (ExerciseItem)v.getTag(R.string.recycler_tagItem);

        //get exercise name
        String exerciseName = (item.exerciseName);

        //create string values for menu
        String strEdit = mFragment.getString(R.string.edit);
        String strDelete = mFragment.getString(R.string.delete);

        //create context menu
        menu.setHeaderTitle(exerciseName);
        menu.add(0, CONTEXT_MENU_EDIT, 0, strEdit);
        menu.add(0, CONTEXT_MENU_DELETE, 0, strDelete);

    }

    /*
     * boolean onContextItemSelected(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_EDIT:
                Log.d("Choice", "     edit");
                //TODO - need to edit exercise
                return true;
            case CONTEXT_MENU_DELETE:
                Log.d("Choice", "     delete");
                //TODO - need to delete exercise
                return true;
        }
        return false;
    }


/**************************************************************************************************/
    /*
     * void loadExercises() - loads exercises onto cursor used by Adapter.
     */
    private void loadExercises(){
        // Initializes a loader for loading clients
        mFragment.getActivity().getSupportLoaderManager().initLoader(mLoaderId, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.ExerciseEntry.buildExerciseByCategoryKey(mUserId, mCategoryKey);

                        //get cursor
                        return new CursorLoader(
                                mFragment.getActivity(),
                                uri,
                                ExerciseColumns.PROJECTION_EXERCISE,
                                null,
                                null,
                                Contractor.ExerciseEntry.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //category cursor loaded, initialize layout
                        prepareFragment(cursor);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    }
                });
    }


}
