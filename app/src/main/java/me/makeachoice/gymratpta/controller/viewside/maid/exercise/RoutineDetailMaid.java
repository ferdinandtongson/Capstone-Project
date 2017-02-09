package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineDetailRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * RoutineDetailMaid manages the routine detail screen where the user can created custom exercise
 * routines for his/her clients
 */
/**************************************************************************************************/

public class RoutineDetailMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 *      int CONTEXT_MENU_EDIT - "edit" context menu id number
 *      int CONTEXT_MENU_DELETE = "delete" context menu id number
 *      ArrayList<RoutineItem> mData - data list consumed by the adapter
 *      ExerciseRecyclerAdapter mAdapter - adapter consumed by recycler
 */
/**************************************************************************************************/

    //mData - data list consumed by the adapter
    private ArrayList<RoutineItem> mData;

    //mAdapter - adapter consumed by recycler
    private RoutineDetailRecyclerAdapter mAdapter;

    private String mUserId;
    private RoutineDetailItem mRoutineDetailItem;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineDetailMaid - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineDetailMaid(...) - constructor
     */
    public RoutineDetailMaid(String maidKey, int layoutId, String userId, RoutineDetailItem item){
        //get maidKey
        mMaidKey = maidKey;

        mUserId = userId;
        mRoutineDetailItem = item;
        mData = mRoutineDetailItem.routineExercises;

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

        //mData = new ArrayList();
        //load routines
        //loadRoutineNames();
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
        initializeEditText();
        //initialize "empty" text, displayed if data is empty
        initializeEmptyText();

        //initialize adapter
        initializeAdapter();

        //initialize recycler view
        initializeRecycler();

        initializeFAB();

        //check if data is empty
        checkForEmptyRecycler(mData.isEmpty());
        //checkForEmptyRecycler(true);

    }

    private void initializeEditText(){
        EditText edtName = (EditText)mLayout.findViewById(R.id.routineDetail_edtName);
        edtName.setText(mRoutineDetailItem.routineName);
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

        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_exercise_detail;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineDetailRecyclerAdapter(mLayout.getContext(), adapterLayoutId);

        //mAdapter.setOnCreateContextMenuListener(this);

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
        //startRoutineDetail();
    }

/**************************************************************************************************/


}
