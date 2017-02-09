package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.content.Intent;
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
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineRecyclerAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineColumns;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameColumns;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.view.activity.RoutineDetailActivity;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE_NAME;

/**************************************************************************************************/
/*
 * RoutineMaid displays a list of user-defined exercise routines
 *
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
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

public class RoutineMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge,
        RecyclerView.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

/**************************************************************************************************/
/*
 * Class Variables
 *      int CONTEXT_MENU_EDIT - "edit" context menu id number
 *      int CONTEXT_MENU_DELETE = "delete" context menu id number
 *      ArrayList<RoutineItem> mData - data list consumed by the adapter
 *      ExerciseRecyclerAdapter mAdapter - adapter consumed by recycler
 */
/**************************************************************************************************/

    //CONTEXT_MENU_EDIT - "edit" context menu id number
    private final static int CONTEXT_MENU_EDIT = 0;

    //CONTEXT_MENU_DELETE - "delete" context menu id number
    private final static int CONTEXT_MENU_DELETE = 1;

    //mData - data list consumed by the adapter
    private ArrayList<RoutineDetailItem> mData;

    //mAdapter - adapter consumed by recycler
    private RoutineRecyclerAdapter mAdapter;

    private String mUserId;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineMaid - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineMaid(...) - constructor
     */
    public RoutineMaid(String maidKey, int layoutId, String userId){
        //get maidKey
        mMaidKey = maidKey;

        mUserId = userId;

        //fragment layout id number
        mLayoutId = layoutId;
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

        mData = new ArrayList();
        //load routines
        loadRoutineNames();
        prepareFragment();
    }

    /*
     * void detach() - fragment is being disassociated from activity
     */
    @Override
    public void detach(){
        //fragment is being disassociated from Activity
        super.detach();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void prepareFragment(View) - prepare components and data to be displayed by fragment
 *      void initializeRecycler() - initialize recycler to display exercise items
 *      EditAddDialog initializeDialog(...) - create exercise edit/add dialog
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        //Log.d("Choice", "RoutineMaid.prepareFragment: " + cursor.getCount());
        //initialize "empty" text, displayed if data is empty
        initializeEmptyText();

        //initialize adapter
        initializeAdapter();

        //initialize recycler view
        initializeRecycler();

        initializeFAB();

        //check if data is empty
        checkForEmptyRecycler(mData.isEmpty());

    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" text message
        String emptyMsg = mLayout.getResources().getString(R.string.emptyRecycler_addRoutine);

        //set message
        setEmptyMessage(emptyMsg);
    }


    private void initializeAdapter() {
        //get data stub
        //mData = RoutineStubData.createDefaultRoutines(mLayout.getContext());

        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_routine;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineRecyclerAdapter(mLayout.getContext(), adapterLayoutId);

        mAdapter.setOnCreateContextMenuListener(this);

        //swap old data with new data
        mAdapter.swapData(mData);
    }

    /*
     * void initializeRecycler() - initialize recycler to display exercise items
     */
    private void initializeRecycler(){
        //set adapter
        mBasicRecycler.setAdapter(mAdapter);
    }

    private void initializeFAB(){
        String description = mFragment.getString(R.string.description_fab_routine);
        mFAB.setContentDescription(description);

        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked(view);
            }
        });
    }


    private void onFabClicked(View view){
        Log.d("Choice", "RoutineMaid.onFabClicked");
        mRoutineDetailItem = new RoutineDetailItem();
        mRoutineDetailItem.routineName = "";
        mRoutineDetailItem.routineExercises = new ArrayList<RoutineItem>();

        ((Boss)mFragment.getActivity().getApplication()).setRoutineDetailItem(mRoutineDetailItem);
        startRoutineDetail();
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
        mRoutineDetailItem = (RoutineDetailItem)v.getTag(R.string.recycler_tagItem);

        //get routine name
        String routineName = (mRoutineDetailItem.routineName);

        //create string values for menu
        String strEdit = mFragment.getString(R.string.edit);
        String strDelete = mFragment.getString(R.string.delete);

        //create context menu
        menu.setHeaderTitle(routineName);
        menu.add(0, CONTEXT_MENU_EDIT, 0, strEdit);
        menu.add(0, CONTEXT_MENU_DELETE, 0, strDelete);

        int count = menu.size();
        for(int i = 0; i < count; i++){
            menu.getItem(i).setOnMenuItemClickListener(this);
        }

    }

    private RoutineDetailItem mRoutineDetailItem;

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_EDIT:
                Log.d("Choice", "     edit");
                ((Boss)mFragment.getActivity().getApplication()).setRoutineDetailItem(mRoutineDetailItem);
                startRoutineDetail();
                return true;
            case CONTEXT_MENU_DELETE:
                Log.d("Choice", "     delete");
                //TODO - need to delete exercise routine
                return true;
        }
        return false;
    }

    private void startRoutineDetail(){
        Intent intent = new Intent(mFragment.getContext(), RoutineDetailActivity.class);
        mFragment.startActivity(intent);

    }

/**************************************************************************************************/

    /*
     * void loadRoutineNames() - loads routine names onto cursor used by Adapter.
     */
    private void loadRoutineNames(){
        Log.d("Choice", "RoutineMaid.laodRoutineNames");

        // Initializes a loader for loading clients
        mFragment.getActivity().getSupportLoaderManager().initLoader(LOADER_ROUTINE_NAME, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        //request client cursor from local database
                        Uri uri = Contractor.RoutineNameEntry.buildRoutineNameByUID(mUserId);
                        Log.d("Choice", "     uri: " + uri.toString());

                        //get cursor
                        return new CursorLoader(
                                mFragment.getActivity(),
                                uri,
                                RoutineNameColumns.PROJECTION_ROUTINE_NAME,
                                null,
                                null,
                                Contractor.RoutineNameEntry.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        Log.d("Choice", "RoutineMaid.loadRoutineName: " + cursor.getCount());
                        int count = cursor.getCount();
                        for(int i = 0; i < count; i++){
                            cursor.moveToPosition(i);
                            RoutineDetailItem item = new RoutineDetailItem();
                            item.routineName = cursor.getString(RoutineNameColumns.INDEX_ROUTINE_NAME);

                            mData.add(item);
                        }

                        mRoutineCount = 0;
                        loadRoutines();
                        mFragment.getLoaderManager().destroyLoader(LOADER_ROUTINE_NAME);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    }
                });
    }

    private int mRoutineCount;

    /*
     * void loadRoutines() - loads routines onto cursor used by Adapter.
     */
    private void loadRoutines(){
        // Initializes a loader for loading clients
        mFragment.getActivity().getSupportLoaderManager().initLoader(LOADER_ROUTINE, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                        RoutineDetailItem item = mData.get(mRoutineCount);

                        //request client cursor from local database
                        Uri uri = Contractor.RoutineEntry.buildRoutineByName(mUserId, item.routineName);
                        Log.d("Choice", "RoutineMaid.loadRoutines");
                        Log.d("Choice", "     uri: " + uri);

                        //get cursor
                        return new CursorLoader(
                                mFragment.getActivity(),
                                uri,
                                RoutineColumns.PROJECTION_ROUTINE,
                                null,
                                null,
                                Contractor.RoutineEntry.SORT_ORDER_DEFAULT);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                        //category cursor loaded, initialize layout
                        storeRoutines(cursor);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {
                    }
                });
    }

    private void storeRoutines(Cursor cursor){
        ArrayList<RoutineItem> routines = new ArrayList();

        int count = cursor.getCount();

        if(count > 0){
            checkForEmptyRecycler(false);
        }

        for(int i = 0; i < count; i++){
            cursor.moveToPosition(i);

            RoutineItem item = new RoutineItem(cursor);
            routines.add(item);
        }

        Log.d("Choice", "     routine: " + mData.get(mRoutineCount).routineName);
        Log.d("Choice", "          routines: " + routines.size());
        mData.get(mRoutineCount).routineExercises = routines;
        mAdapter.addItem(mData.get(mRoutineCount));
        mRoutineCount++;

        if(mRoutineCount < mData.size()){
            loadRoutines();
        }
        else{
            Log.d("Choice", "RoutineMaid.storeRoutines - finished!!!!!");
        }
    }

}
