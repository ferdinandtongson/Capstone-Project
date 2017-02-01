package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.exercise.RoutineSessionItem;
import me.makeachoice.gymratpta.model.stubData.RoutineStubData;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * TODO - add description
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

public class RoutineMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private ArrayList<RoutineSessionItem> mData;

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
        String emptyMsg = mLayout.getResources().getString(R.string.emptyRecycler_addRoutine);
        setEmptyMessage(emptyMsg);

        mData = RoutineStubData.createDefaultRoutineSessions(mLayout.getContext());
        checkForEmptyRecycler(mData.isEmpty());

        initializeAdapter();
    }

    private RoutineRecyclerAdapter mAdapter;
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_brief_routine;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineRecyclerAdapter(mLayout.getContext(), adapterLayoutId);
        mAdapter.swapData(mData);

        mBasicRecycler.setAdapter(mAdapter);
    }

/**************************************************************************************************/

}
