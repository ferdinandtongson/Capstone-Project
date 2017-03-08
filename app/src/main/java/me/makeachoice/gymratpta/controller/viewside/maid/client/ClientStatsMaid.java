package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.StatsButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.StatsAdapter;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.StatsItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.dialog.StatsDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_STATS_RECORD;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_STATS;

/**************************************************************************************************/
/*
 * ClientStatsMaid display a list of stats of a given client
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

public class ClientStatsMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private String mUserId;
    private MyActivity mActivity;
    private ClientItem mClientItem;
    private ScheduleItem mScheduleItem;
    private StatsItem mSaveItem;
    private StatsItem mDeleteItem;

    private ArrayList<StatsItem> mData;
    private ArrayList<StatsItem> mPrevList;
    private StatsAdapter mAdapter;

    private int mActiveStatsIndex;

    private StatsButler mStatsButler;

    private StatsButler.OnLoadedListener mOnLoadListener =
            new StatsButler.OnLoadedListener() {
                @Override
                public void onLoaded(ArrayList<StatsItem> statsList) {
                    onStatsLoaded(statsList);
                }
            };

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
                           ScheduleItem scheduleItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;
        mScheduleItem = scheduleItem;

        mSaveItem = createStatsItem();
    }

    /*
     * void createStatsItem() -
     */
    private StatsItem createStatsItem(){
        StatsItem statItem = new StatsItem();
        statItem.uid = mUserId;
        statItem.clientKey = mClientItem.fkey;
        statItem.timestamp = mScheduleItem.datestamp + " " + mScheduleItem.appointmentTime;
        statItem.appointmentDate = mScheduleItem.appointmentDate;
        statItem.appointmentTime = mScheduleItem.appointmentTime;
        statItem.modifiedDate = mScheduleItem.appointmentDate;
        statItem.statWeight = 0;
        statItem.statBodyFat = 0;
        statItem.statBMI = 0;
        statItem.statNeck = 0;
        statItem.statChest = 0;
        statItem.statRBicep = 0;
        statItem.statLBicep = 0;
        statItem.statWaist = 0;
        statItem.statNavel = 0;
        statItem.statHips = 0;
        statItem.statRThigh = 0;
        statItem.statLThigh = 0;
        statItem.statRCalf = 0;
        statItem.statLCalf = 0;

        return statItem;
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
        mPrevList = new ArrayList<>();
        mStatsButler = new StatsButler(mActivity, mUserId, mClientItem.fkey);

    }

    public void start(){
        super.start();
        mStatsButler.loadStats(LOADER_STATS, mOnLoadListener);
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
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler component
 *      NotesDialog initializeNotesDialog - initialize client notes dialog
 *      void updateEmptyText() - check if adapter is empty or not then updates empty textView
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        initializeEmptyText();

        initializeAdapter();

        initializeRecycler();
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
        mAdapter.setFirstItem(mFirstItem);
        mAdapter.setPreviousItems(mPrevList);
        mAdapter.setActiveIndex(mActiveStatsIndex);

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
     * StatsDialog initializeStatsDialog - initialize client stats dialog
     */
    private StatsDialog initializeStatsDialog(int mode, StatsItem item, StatsItem prevItem){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mStatsDialog = new StatsDialog();
        mStatsDialog.setDialogValues(mActivity, mUserId, mode, item, prevItem);

        if(mode != StatsDialog.MODE_READ){
            mStatsDialog.setOnSaveListener(new StatsDialog.OnSaveListener() {
                @Override
                public void onSave(StatsItem statsItem) {
                    onStatsSaved(statsItem);
                }
            });

            mStatsDialog.setOnCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStatsCanceled();
                }
            });

            mStatsDialog.setCancelable(false);
        }
        else{
            mStatsDialog.setCancelable(true);
        }

        mStatsDialog.show(fm, DIA_STATS_RECORD);

        return mStatsDialog;
    }

    private StatsDialog mStatsDialog;

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
 *      void onStatsLoaded() - load client stats data from database
 *      void onStatsLoaded(Cursor) - stats from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void onStatsLoaded() - load client stats data from database
     */
    private void onStatsLoaded(ArrayList<StatsItem> statsList){
        //list in descending order, get first item
        mFirstItem = getFirstItem(statsList);

        //clear list buffers
        mData.clear();
        mPrevList.clear();

        //clear active stat index
        mActiveStatsIndex = -1;
        StatsItem prevItem;
        ArrayList<StatsItem> invertedList = new ArrayList<>();

        //get number of saved client stats
        int count = statsList.size();

        //loop through stats
        for(int i = 0; i < count; i++){
            //get note item, last recorded stat
            StatsItem item = statsList.get(i);

            //check if note item is equal to appointment schedule timestamp
            if(item.timestamp.equals(mSaveItem.timestamp)){
                //save index of item in list as active
                mActiveStatsIndex = i;
            }

            //check if list count is 1
            if(count == 1){
                //create empty statItem, for previous stat record
                prevItem = createStatsItem();
            }
            else{
                //get previous stat record index
                int prevItemIndex = i + 1;

                //check previous stat record index value
                if(prevItemIndex < count){
                    //less than stat record size, get previous stat record
                    prevItem = statsList.get(prevItemIndex);
                }
                else{
                    //greater than list, must be first stat record so no previous record, create empty stat
                    prevItem = createStatsItem();
                }
            }
            invertedList.add(prevItem);
        }

        int invertCount = invertedList.size() - 1;
        for(int i = invertCount; i >= 0; i--){
            mPrevList.add(invertedList.get(i));
        }

        //check if active index was found
        if(mActiveStatsIndex == -1){
            //no active index, add blank notes object to data list
            mData.add(mSaveItem);

            //check stat list count
            if(count > 0){
                //greater than zero, get last stat record (first in list since desc order)
                mPrevList.add(statsList.get(0));
            }
            else{
                //no records, create empty stat record
                mPrevList.add(createStatsItem());
            }
            //set active index to blank notes object
            mActiveStatsIndex = 0;
        }

        //add saved client notes
        mData.addAll(statsList);


        //load client data
        prepareFragment();
    }

    private StatsItem mFirstItem;

    private StatsItem getFirstItem(ArrayList<StatsItem> statsList){
        //list is in descending order, first item is last item in list
        int count = statsList.size();

        //check if list count is greater than 0
        if(count > 0){
            //greater than zero, get last item in list
            return statsList.get(count - 1);
        }

        return createStatsItem();
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 */
/**************************************************************************************************/


    private void onItemClicked(int index){
        StatsItem item = mData.get(index);
        StatsItem prevItem = mPrevList.get(index);

        if(index == mActiveStatsIndex){
            mDeleteItem = item;
            initializeStatsDialog(StatsDialog.MODE_EDIT, item, prevItem);
        }
        else{
            initializeStatsDialog(StatsDialog.MODE_READ, item, prevItem);
        }
    }

    private void onStatsCanceled(){
        mStatsDialog.dismiss();
    }

    private void onStatsSaved(StatsItem statsItem){

        mSaveItem = statsItem;
        mSaveItem.modifiedDate = DateTimeHelper.getToday();
        deleteStats(mDeleteItem);

        mStatsDialog.dismiss();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Methods
 *      void saveStats(StatsItem) - save stats item
 */
/**************************************************************************************************/
    /*
     * void saveStats(StatsItem) - save stats item
     */
    private void saveStats(StatsItem saveItem){
        mStatsButler.saveStats(saveItem, new StatsButler.OnSavedListener() {
            @Override
            public void onSaved() {
                mStatsButler.loadStats(LOADER_STATS, mOnLoadListener);
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Methods
 *      void deleteStats(StatsItem) - delete notes
 *      void onStatsDeleted() - notes have been deleted from database
 */
/**************************************************************************************************/
    /*
     * void deleteStats(StatsItem) - delete notes
     */
    private void deleteStats(StatsItem deleteItem){
        mStatsButler.deleteStats(deleteItem, new StatsButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                onStatsDeleted();
            }
        });
    }

    /*
     * void onStatsDeleted() - notes have been deleted from database
     */
    private void onStatsDeleted(){
        saveStats(mSaveItem);
    }

/**************************************************************************************************/

}
