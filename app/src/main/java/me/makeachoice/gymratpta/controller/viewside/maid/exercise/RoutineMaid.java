package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
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
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineSessionItem;
import me.makeachoice.gymratpta.model.stubData.RoutineStubData;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

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
        RecyclerView.OnCreateContextMenuListener, MyActivity.OnContextItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables
 *      int CONTEXT_MENU_EDIT - "edit" context menu id number
 *      int CONTEXT_MENU_DELETE = "delete" context menu id number
 *      ArrayList<RoutineSessionItem> mData - data list consumed by the adapter
 *      ExerciseRecyclerAdapter mAdapter - adapter consumed by recycler
 */
/**************************************************************************************************/

    //CONTEXT_MENU_EDIT - "edit" context menu id number
    private final static int CONTEXT_MENU_EDIT = 0;

    //CONTEXT_MENU_DELETE - "delete" context menu id number
    private final static int CONTEXT_MENU_DELETE = 1;

    //mData - data list consumed by the adapter
    private ArrayList<RoutineSessionItem> mData;

    //mAdapter - adapter consumed by recycler
    private RoutineRecyclerAdapter mAdapter;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineMaid - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineMaid(...) - constructor
     */
    public RoutineMaid(String maidKey, int layoutId){
        //get maidKey
        mMaidKey = maidKey;

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

        //prepare fragment components
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
        //initialize "empty" text, displayed if data is empty
        initializeEmptyText();

        //initialize adapter
        initializeAdapter();

        //initialize recycler view
        initializeRecycler();

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
        mData = RoutineStubData.createDefaultRoutineSessions(mLayout.getContext());

        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_routine;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineRecyclerAdapter(mLayout.getContext(), adapterLayoutId);

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
        RoutineSessionItem item = (RoutineSessionItem)v.getTag(R.string.recycler_tagItem);

        //get routine name
        String routineName = (item.routineName);

        //create string values for menu
        String strEdit = mFragment.getString(R.string.edit);
        String strDelete = mFragment.getString(R.string.delete);

        //create context menu
        menu.setHeaderTitle(routineName);
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
                //TODO - need to edit exercise routine
                return true;
            case CONTEXT_MENU_DELETE:
                Log.d("Choice", "     delete");
                //TODO - need to delete exercise routine
                return true;
        }
        return false;
    }

/**************************************************************************************************/


}
