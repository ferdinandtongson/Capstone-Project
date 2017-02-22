package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineLoader;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineDetailRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

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

public class ClientRoutineMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

private String mUserId;
    private MyActivity mActivity;
    private ClientItem mClientItem;
    private AppointmentItem mAppointmentItem;
    private RoutineItem mSaveItem;

    private ArrayList<RoutineItem> mData;
    private RoutineDetailRecyclerAdapter mAdapter;

    //private boolean mEditingRoutine;
    private RoutineLoader mRoutineLoader;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientRoutineMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientRoutineMaid(...) - constructor
     */
    public ClientRoutineMaid(String maidKey, int layoutId, String userId, ClientItem clientItem,
                             AppointmentItem appItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;

        mAppointmentItem = appItem;

        //mEditingRoutine = false;
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

        mActivity = (MyActivity)mFragment.getActivity();

        mData = new ArrayList<>();
        RoutineItem item = new RoutineItem();
        //item.appointmentDate = DateTimeHelper.getToday();
        //item.appointmentTime = DateTimeHelper.getCurrentTime();
        mData.add(item);

        //mRoutineLoader = new RoutineLoader(mActivity, mUserId, mClientItem.fkey);

        prepareFragment();
        //loadStats();
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
        initializeEmptyText();

        initializeAdapter();

        initializeRecycler();

        initializeFAB();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" textView message
        String emptyMsg = mFragment.getResources().getString(R.string.emptyRecycler_noStats);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_exercise_detail;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineDetailRecyclerAdapter(mFragment.getActivity(), adapterLayoutId);

        /*mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                onItemClicked(index);
            }
        });

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //edit appointment item
                //TODO - onEditNotes(index);

                return false;
            }
        });*/

        //swap client data into adapter
        mAdapter.swapData(mData);

        //check if recycler has any data; if not, display "empty" textView
        updateEmptyText();
    }

    /*
     * void initializeRecycler() - initialize recycler component
     */
    private void initializeRecycler(){
        //set adapter
        mBasicRecycler.setAdapter(mAdapter);
    }

    /*
     * void initializeFAB() - initialize FAB component
     */
    private void initializeFAB(){
        //get content description string value
        //String description = mFragment.getString(R.string.description_fab_appointment);

        //add content description to FAB
        //mFAB.setContentDescription(description);

        //add listener for onFABClick events
        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onFABClick event occurred
                //onFabClicked(view);
            }
        });
    }

    /*
     * void updateEmptyText() - check if adapter is empty or not then updates empty textView
     */
    private void updateEmptyText(){
        if(mAdapter.getItemCount() > 0){
            //is not empty
            isEmptyRecycler(false);
        }
        else{
            //is empty
            isEmptyRecycler(true);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void loadRoutine() - load client routine data from database
 *      void onRoutineLoaded(Cursor) - routine from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadRoutine() - load client stats data from database
     */
    private void loadRoutine(){
        //start loader to get appointment data from database
        /*mRoutineLoader.loadRoutineByClientKey(new RoutineLoader.OnRoutineLoadListener() {
            @Override
            public void onRoutineLoadFinished(Cursor cursor) {
                onRoutineLoaded(cursor);
            }
        });*/
    }

    /*
     * void onRoutineLoaded(Cursor) - stats from database has been loaded
     */
    private void onRoutineLoaded(Cursor cursor){
        //clear stats list array
        mData.clear();

        //get number of stats loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create stats item from cursor data
            RoutineItem item = new RoutineItem(cursor);

            //add stats item to list
            mData.add(item);
        }

        //destroy loader
        //mRoutineLoader.destroyLoader();

        //load client data
        prepareFragment();
    }

/**************************************************************************************************/


}
