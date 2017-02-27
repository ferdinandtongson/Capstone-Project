package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.schedule.ScheduleWeekAdapter;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_SCHEDULE;

/**************************************************************************************************/
/*
 * WeekViewMaid displays a list of scheduled appointments for a given week
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

public class WeekViewMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mData - array of data used by AppointmentAdapter
    private ArrayList<AppointmentCardItem> mData;

    private String mUserId;

    private String[] mDateRange;
    private Date mStartDate;

    private int mAppointmentLoaderId;
    private int mClientLoaderId;

    private ScheduleButler mScheduleButler;
    private ClientButler mClientButler;

    private HashMap<String,ClientItem> mClientMap;
    private ScheduleWeekAdapter mAdapter;

    private ScheduleButler.OnScheduleLoadedListener mOnScheduleListener =
            new ScheduleButler.OnScheduleLoadedListener() {
                @Override
                public void onScheduleLoaded(ArrayList<ScheduleItem> scheduleList) {
                    onDayScheduleLoaded(scheduleList);
                }
            };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * WeekViewMaid - constructor
 */
/**************************************************************************************************/
    /*
     * WeekViewMaid(...) - constructor
     */
    public WeekViewMaid(String maidKey, int layoutId, String userId, String[] dateRange, Date startDate, int counter){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mData = new ArrayList<>();
        mClientMap = new HashMap<>();

        mUserId = userId;
        mDateRange = dateRange;
        mStartDate = startDate;

        mAppointmentLoaderId = LOADER_SCHEDULE + counter;
        mClientLoaderId = LOADER_CLIENT + counter;
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

        mScheduleButler = new ScheduleButler(mActivity, mUserId);
        mClientButler = new ClientButler(mActivity, mUserId);

    }

    public void start(){

        if(mClientMap.isEmpty()){
            loadClient();
        }
        else{
            //update screen
            mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleListener);
        }
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
        //initialize "empty" textView used when recycler is empty
        initializeEmptyText();

        //initialize adapter used by recycler
        initializeAdapter();

        //initialize recycler
        initializeRecycler();

        //initialize floating action button
        initializeFAB();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" textView message
        String emptyMsg = mActivity.getResources().getString(R.string.emptyRecycler_noSessions);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_week_schedule;

        //create adapter consumed by the recyclerView
        mAdapter = new ScheduleWeekAdapter(mActivity, adapterLayoutId);

        //set icon click listener
        mAdapter.setOnImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onProfileImageClicked(view);
            }
        });

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //edit appointment item
                //onEditAppointment(index);

                return false;
            }
        });

        //swap data into adapter
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

        //create item touch helper
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mTouchCallback);

        //attach helper to recycler, enables drag-and-drop and swipe to dismiss functionality
        //itemTouchHelper.attachToRecyclerView(mBasicRecycler.getRecycler());
    }

    /*
     * void initializeFAB() - initialize FAB component
     */
    private void initializeFAB(){
        //get content description string value
        String description = mActivity.getString(R.string.description_fab_appointment);

        //add content description to FAB
        mFAB.setContentDescription(description);

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
 *      void onDayScheduleLoaded(...) - appointment schedule for given day has been loaded
 *      void loadClient() - load client data has been requested
 *      void onClientDataLoaded(...) - client data has been loaded
 */
/**************************************************************************************************/
    /*
     * void onDayScheduleLoaded(...) - appointment schedule for given day has been loaded
     */
    private void onDayScheduleLoaded(ArrayList<ScheduleItem> scheduleList){

        //initialize buffer to hold appointments for week
        ArrayList<ScheduleItem> weekSchedule = new ArrayList<>();

        //get end date of range of dates
        String endDate = mDateRange[1];

        //current index in scheduleList
        int scheduleIndex = 0;
        //current index in date range
        int dateIndex = 0;
        //debug index, to prevent infinite loop
        int debugIndex = 0;

        //get current appointment date being added to weekSchedule buffer
        String currentDate = DateTimeHelper.getDatestamp(mStartDate, 0);

        //previous date
        String oldDate = "";

        //date taken from scheduleList item
        String scheduleDate;

        //loop through until endDate is reached
        while(!currentDate.equals(endDate) && debugIndex < 20){

            //check if current date is equal to oldDate
            if(currentDate.equals(oldDate)){
                //is equal, check scheduleList index
                if(scheduleIndex < scheduleList.size()) {
                    //valid index, get scheduleList date
                    scheduleDate = scheduleList.get(scheduleIndex).datestamp;
                }
                else{
                    //invalid index
                    scheduleDate = "";
                }

                //check if scheduleDate is equal to currentDate
                if(scheduleDate.equals(currentDate)){
                    //equal, add scheduleList item to weekSchedule list
                    weekSchedule.add(scheduleList.get(scheduleIndex));

                    //increment scheduleList index
                    scheduleIndex++;
                }
                else{
                    //dates not equal, increment date range index
                    dateIndex++;
                    //update currentDate with new date
                    currentDate = DateTimeHelper.getDatestamp(mStartDate, dateIndex);
                }
            }
            else{
                //currentDate not equal to oldDate, save new currentDate to weekSchedule buffer
                ScheduleItem item = new ScheduleItem();
                item.datestamp = currentDate;
                item.clientName = currentDate;

                //add item to weekScheduleBuffer
                weekSchedule.add(item);

                //set oldDate to currentDate
                oldDate = currentDate;
            }

            //increment debug index
            debugIndex++;
        }

        mData.clear();
        ScheduleItem weekItem;
        ClientItem clientItem;
        int weekCount = weekSchedule.size();
        for(int i = 0; i < weekCount; i++){
            //get item from weekSchedule list
            weekItem = weekSchedule.get(i);

            //check if appointment or date marker
            if(!weekItem.clientName.equals(weekItem.datestamp)){
                //appointment, get client item
                clientItem = mClientMap.get(weekItem.clientName);

                //create appointment card
                mData.add(mClientButler.createAppointmentCardItem(weekItem, clientItem));
            }
            else{
                //is date marker, create dummy appointment card item
                AppointmentCardItem item = new AppointmentCardItem();
                item.clientName = weekItem.clientName;
                item.clientInfo = "";
                item.profilePic = Uri.EMPTY;
                item.routineName = "";
                item.isActive = false;

                //add to data list
                mData.add(item);
            }
        }

        prepareFragment();

    }

    /*
     * void loadClient() - load client data has been requested
     */
    private void loadClient(){
        mClientMap.clear();
        mClientButler.loadAllClients(mClientLoaderId, new ClientButler.OnAllClientsLoadedListener() {
            @Override
            public void onAllClientsLoaded(HashMap<String,ClientItem> clientMap) {
                onClientDataLoaded(clientMap);
            }
        });
    }

    /*
     * void onClientDataLoaded(...) - client data has been loaded
     */
    private void onClientDataLoaded(HashMap<String,ClientItem> clientMap){
        mClientMap.putAll(clientMap);
        mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleListener);
    }

/**************************************************************************************************/

}
