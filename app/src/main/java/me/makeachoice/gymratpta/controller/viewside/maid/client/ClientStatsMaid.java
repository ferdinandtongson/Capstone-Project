package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.loader.StatsLoader;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.StatsAdapter;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.StatsItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**
 * Created by Usuario on 2/22/2017.
 */

public class ClientStatsMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private String mUserId;
    private MyActivity mActivity;
    private ClientItem mClientItem;
    private AppointmentItem mAppointmentItem;
    private StatsItem mSaveItem;

    private ArrayList<StatsItem> mData;
    private StatsAdapter mAdapter;

    private boolean mEditingStats;
    private StatsLoader mStatsLoader;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientStatsMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientStatsMaid(...) - constructor
     */
    public ClientStatsMaid(String maidKey, int layoutId, String userId, ClientItem clientItem,
                           AppointmentItem appItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;

        mAppointmentItem = appItem;

        mEditingStats = false;
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
        StatsItem item = new StatsItem();
        item.appointmentDate = DateTimeHelper.getToday();
        item.appointmentTime = DateTimeHelper.getCurrentTime();
        mData.add(item);

        mStatsLoader = new StatsLoader(mActivity, mUserId, mClientItem.fkey);

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
        int adapterLayoutId = R.layout.card_client_stats;

        //create adapter consumed by the recyclerView
       mAdapter = new StatsAdapter(mFragment.getActivity(), adapterLayoutId);

        mAdapter.setOnClickListener(new View.OnClickListener() {
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
        });

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
                onFabClicked(view);
            }
        });
    }

    /*
     * NotesDialog initializeNotesDialog - initialize client notes dialog
     */
    /*private NotesDialog initializeNotesDialog(int mode, NotesItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mNotesDialog = new NotesDialog();
        mNotesDialog.setDialogValues(mActivity, mUserId, mode, item);

        if(mode != NotesDialog.MODE_READ){
            mNotesDialog.setOnSaveListener(new NotesDialog.OnSaveNotesListener() {
                @Override
                public void onSaveNotes(ArrayList<String> notes) {
                    onNotesSaved(notes);
                }
            });

            mNotesDialog.setOnCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNotesCanceled();
                }
            });

            mNotesDialog.setCancelable(false);
        }
        else{
            mNotesDialog.setCancelable(true);
        }

        mNotesDialog.show(fm, "diaScheduleAppointment");

        return mNotesDialog;
    }

    private NotesDialog mNotesDialog;*/

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
 *      void loadStats() - load client stats data from database
 *      void onStatsLoaded(Cursor) - stats from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadStats() - load client stats data from database
     */
    private void loadStats(){
        //start loader to get appointment data from database
        mStatsLoader.loadStatsByClientKey(new StatsLoader.OnStatsLoadListener() {
            @Override
            public void onStatsLoadFinished(Cursor cursor) {
                onStatsLoaded(cursor);
            }
        });
    }

    /*
     * void onStatsLoaded(Cursor) - stats from database has been loaded
     */
    private void onStatsLoaded(Cursor cursor){
        //clear stats list array
        mData.clear();

        //get number of stats loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create stats item from cursor data
            StatsItem item = new StatsItem(cursor);

            //add stats item to list
            mData.add(item);
        }

        //destroy loader
        mStatsLoader.destroyLoader();

        //load client data
        prepareFragment();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onFabClicked(View) - floating action button clicked, open schedule stats dialog
 *      void onEditAppointment() - edit appointment item
 *      void onSaveAppointment(...) - save appointment item
 *      void onAppointmentDeleteRequest(int) - delete appointment requested
 */
/**************************************************************************************************/
    /*
     * void onFabClicked(View) - floating action button clicked, open schedule stats dialog
     */
    private void onFabClicked(View view){
        mEditingStats = true;

        /*mSaveItem = new NotesItem();
        mSaveItem.uid = mUserId;
        mSaveItem.clientKey = mClientItem.fkey;
        mSaveItem.appointmentDate = mAppointmentItem.appointmentDate;
        mSaveItem.appointmentTime = mAppointmentItem.appointmentTime;
        mSaveItem.modifiedDate = mAppointmentItem.appointmentDate;
        mSaveItem.subjectiveNotes = "";
        mSaveItem.objectiveNotes = "";
        mSaveItem.assessmentNotes = "";
        mSaveItem.planNotes = "";*/

        //initializeNotesDialog(NotesDialog.MODE_ADD, mSaveItem);
    }

    /*
     * void onEditStats() - edit stats item
     */
    private void onEditStats(int index){
        mEditingStats = true;

        mSaveItem = mData.get(index);

        //initializeNotesDialog(NotesDialog.MODE_EDIT, mSaveItem);
    }


    private void onItemClicked(int index){
        StatsItem item = mData.get(index);

        //initializeNotesDialog(NotesDialog.MODE_READ, item);
    }

    private void onStatsCanceled(){
        //mNotesDialog.dismiss();
    }

    private void onStatsSaved(ArrayList<String> notes){
        /*mSaveItem.subjectiveNotes = notes.get(0);
        mSaveItem.objectiveNotes = notes.get(1);
        mSaveItem.assessmentNotes = notes.get(2);
        mSaveItem.planNotes = notes.get(3);

        if(mEditingNotes){
            mSaveItem.modifiedDate = DateTimeHelper.getToday();
            deleteNotes(mSaveItem);
        }
        else{
            saveNotes(mSaveItem);
        }

        mNotesDialog.dismiss();*/
    }

/**************************************************************************************************/


}
