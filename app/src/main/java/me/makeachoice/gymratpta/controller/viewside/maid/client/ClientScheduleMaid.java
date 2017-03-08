package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientRoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.ClientAppAdapter;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppCardItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.activity.ScheduleDetailActivity;
import me.makeachoice.gymratpta.view.activity.SessionDetailActivity;
import me.makeachoice.gymratpta.view.dialog.ClientAppointmentDialog;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_WARNING_DELETE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING_APPOINTMENT;

/**************************************************************************************************/
/*
 * ClientScheduleMaid display the list of scheduled appointments a particular client has
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

public class ClientScheduleMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private String mUserId;

    private ArrayList<ClientAppCardItem> mData;
    private ClientAppAdapter mAdapter;
    private ClientItem mClientItem;

    private MyActivity mActivity;
    private ArrayList<ScheduleItem> mScheduleList;
    private ArrayList<RoutineItem> mExercises;
    private HashMap<String,String> mScheduleMap;

    private ScheduleItem mSaveItem;
    private ScheduleItem mEditItem;
    private ScheduleItem mDeleteItem;
    private boolean mRefreshOnDismiss;

    private RoutineButler mRoutineButler;
    private ScheduleButler mScheduleButler;
    private ClientRoutineButler mClientRoutineButler;

    private ClientAppointmentDialog mAppDialog;
    private DeleteWarningDialog mWarningDialog;

    private boolean mEditMode;
    private boolean mShowWarning;
    private int mDialogCounter;


    //mTouchCallback - helper class that enables drag-and-drop and swipe to dismiss functionality to recycler
    private ItemTouchHelper.Callback mTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            //does nothing
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            onDeleteAppointment(viewHolder.getAdapterPosition());
        }
    };

    ScheduleButler.OnScheduleLoadedListener mScheduleListener = new ScheduleButler.OnScheduleLoadedListener() {
        @Override
        public void onScheduleLoaded(ArrayList<ScheduleItem> scheduleList) {
            scheduleLoaded(scheduleList);
        }
    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientScheduleMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientScheduleMaid(...) - constructor
     */
    public ClientScheduleMaid(String maidKey, int layoutId, String userId, ClientItem item){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = item;
        mDialogCounter = -1;
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

        mScheduleList = new ArrayList<>();
        mExercises = new ArrayList<>();
        mData = new ArrayList<>();
        mScheduleMap = new HashMap<>();
        mEditMode = false;

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        mShowWarning = prefs.getBoolean(PREF_DELETE_WARNING_APPOINTMENT, true);

        String dateStamp = DateTimeHelper.getDatestamp(0);

        mRoutineButler = new RoutineButler(mActivity, mUserId);
        mClientRoutineButler = new ClientRoutineButler(mActivity, mUserId, mClientItem.fkey);
        mScheduleButler = new ScheduleButler(mActivity, mUserId);
        mScheduleButler.loadSchedulePendingByClientKey(mClientItem.fkey, dateStamp, LOADER_SCHEDULE, mScheduleListener);
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
        String emptyMsg = mFragment.getResources().getString(R.string.emptyRecycler_noSessions);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_client_appointment;

        //create adapter consumed by the recyclerView
        mAdapter = new ClientAppAdapter(mFragment.getActivity(), adapterLayoutId);

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //onItemClick event occurred
                onItemClicked(index);
            }
        });


        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //edit appointment item
                onEditAppointment(index);

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

        //create item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mTouchCallback);

        //attach helper to recycler, enables drag-and-drop and swipe to dismiss functionality
        itemTouchHelper.attachToRecyclerView(mBasicRecycler.getRecycler());
    }

    /*
     * void initializeFAB() - initialize FAB component
     */
    private void initializeFAB(){
        //get content description string value
        String description = mFragment.getString(R.string.description_fab_appointment);

        //add content description to FAB
        mFAB.setContentDescription(description);

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
     * ClientAppointmentDialog initializeScheduleDialog - initialize appointment dialog
     */
    private ClientAppointmentDialog initializeScheduleDialog(ScheduleItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        if(mDialogCounter == 0){
            //create dialog
            mAppDialog = new ClientAppointmentDialog();
            mAppDialog.setDialogValues(mActivity, mUserId, mClientItem, item, mScheduleMap);
            mAppDialog.setEditMode(mEditMode);

            mAppDialog.setOnSavedListener(new ClientAppointmentDialog.OnSaveClickListener() {
                @Override
                public void onSaveClicked(ScheduleItem appItem) {
                    onSaveAppointment(appItem);
                    mDialogCounter = -1;
                }
            });

            mAppDialog.show(fm, DIA_SCHEDULE);
        }

        return mAppDialog;
    }

    /*
     * DeleteWarningDialog initializeDialog(...) - delete warning dialog
     */
    private DeleteWarningDialog initializeWarningDialog(ScheduleItem deleteItem, ClientItem clientItem) {
        String strTitle = clientItem.clientName + " @" + deleteItem.appointmentTime;
        mRefreshOnDismiss = true;
        mDeleteItem = deleteItem;

        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        if(mDialogCounter == 0){
            //create dialog
            mWarningDialog = new DeleteWarningDialog();

            //set dialog values
            mWarningDialog.setDialogValues(mActivity, mUserId, strTitle);

            //set onDismiss listener
            mWarningDialog.setOnDismissListener(new DeleteWarningDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(mRefreshOnDismiss){
                        onRefresh();
                    }
                    mDialogCounter = -1;
                }
            });

            //set onDelete listener
            mWarningDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
                @Override
                public void onDelete() {
                    mRefreshOnDismiss = false;
                    //delete routine
                    butlerDeleteAppointment(mDeleteItem);

                    //dismiss dialog
                    mWarningDialog.dismiss();
                }
            });


            //show dialog
            mWarningDialog.show(fm, DIA_WARNING_DELETE);

        }

        return mWarningDialog;
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

    private void onRefresh(){
        //get timestamp for today
        String dateStamp = DateTimeHelper.getDatestamp(0);

        //get pending schedules
        mScheduleButler.loadSchedulePendingByClientKey(mClientItem.fkey, dateStamp, LOADER_SCHEDULE, mScheduleListener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void scheduleLoaded(...) - scheduled appointments from database has been loaded
 *      ClientAppCardItem createClientAppCardItem(...) - create appointmentCard item consumed by adapter
 */
/**************************************************************************************************/
    /*
     * void scheduleLoaded(...) - scheduled appointments from database has been loaded
     */
    private void scheduleLoaded(ArrayList<ScheduleItem> scheduleList){

        //clear buffer list array
        mScheduleMap.clear();
        mScheduleList.clear();
        mData.clear();


        mScheduleList.addAll(scheduleList);

        //clear data list consumed by recycler

        //get number of appointments loaded
        int count = mScheduleList.size();

        //loop through cursor
        for(int i = 0; i < count; i++){

            //create appointment item from cursor data
            ScheduleItem item = mScheduleList.get(i);

            String timestamp = DateTimeHelper.getTimestamp(item.appointmentDate, item.appointmentTime);
            mScheduleMap.put(timestamp, item.appointmentDate);
            mData.add(createClientAppCardItem(item));
        }

        prepareFragment();
    }

    /*
     * ClientAppCardItem createClientAppCardItem(...) - create appointmentCard item consumed by adapter
     */
    private ClientAppCardItem createClientAppCardItem(ScheduleItem appItem){
        //create clientCard item used by adapter
        ClientAppCardItem item = new ClientAppCardItem();
        item.appointmentDate = appItem.appointmentDate;
        item.appointmentTime = appItem.appointmentTime;
        item.routineName= appItem.routineName;
        item.isActive = true;

        return item;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
 *      void onEditAppointment() - edit appointment request
 *      void onSaveAppointment(...) - save appointment request
 *      onDeleteAppointment(...) - delete appointment request
 *      void onAppointmentDeleteRequest(int) - delete appointment requested
 */
/**************************************************************************************************/
    /*
     * void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
     */
    private void onFabClicked(View view){
        //set edit mode false, creating new appointment
        mEditMode = false;

        mDialogCounter = 0;
        //initialize schedule appointment dialog
        initializeScheduleDialog(null);
    }

    /*
     * void onEditAppointment() - edit appointment request
     */
    private void onEditAppointment(int index){
        //set edit appointment status flag as true
        mEditMode = true;

        //save edit appointment as delete appointment item
        mEditItem = mScheduleList.get(index);

        mDialogCounter = 0;

        //open schedule appointment dialog
        initializeScheduleDialog(mEditItem);
    }

    /*
     * void onItemClicked(int) - exercise routine has been clicked
     */
    private void onItemClicked(int index){

        Boss boss = (Boss)mActivity.getApplication();

        ScheduleItem scheduleItem = mScheduleList.get(index);

        boss.setAppointmentItem(scheduleItem);

        String today = DateTimeHelper.getToday();

        boolean isToday = today.equals(scheduleItem.appointmentDate);

        Intent intent;
        if(isToday){
            intent = new Intent(mActivity, SessionDetailActivity.class);
        }
        else{
            intent = new Intent(mActivity, ScheduleDetailActivity.class);
        }
        //info icon, show client info screen
        mActivity.startActivity(intent);
    }


    /*
     * void onSaveAppointment(...) - save appointment request
     */
    private void onSaveAppointment(ScheduleItem appointmentItem){
        //get save appointment item
        mSaveItem = appointmentItem;

        mDialogCounter = -1;

        //dismiss schedule appointment dialog
        mAppDialog.dismiss();

        //check if editing an old appointment
        if(mEditMode){
            //yes, delete old appointment (new appointment will be saved after deletion)
            butlerDeleteAppointment(mEditItem);
        }
        else{

            //no, save new appointment
            saveAppointment(mSaveItem);
        }
    }

    /*
     * void onDeleteAppointment(int) - delete appointment request
     */
    private void onDeleteAppointment(int index){
        //set editing old appointment flag, false
        mEditMode = false;

        //get appointment item to be deleted
        mDeleteItem = mScheduleList.get(index);

        //check status
        if(mShowWarning){
            mDialogCounter = 0;

            //wants warning, show "delete warning" dialog
            initializeWarningDialog(mDeleteItem, mClientItem);
        }
        else{
            //no warning, delete routine
            butlerDeleteAppointment(mDeleteItem);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Methods
 *      void saveAppointment(...) - save appointment
 *      void onSaveRoutineLoaded(...) - exercise routine to save has been loaded
 *      void butlerSaveRoutine(int) - user butler to save exercise routine
 *      void butlerSaveAppointment(...) - use butler to save scheduled appointment
 */
/**************************************************************************************************/
    private int mRoutineCounter;
    /*
     * void saveAppointment(...) - save appointment
     */
    private void saveAppointment(ScheduleItem saveItem){
        //save schedule to be saved
        mSaveItem = saveItem;

        //get "none selected" routine name
        String strNoneSelected = mActivity.getString(R.string.msg_none_selected);

        //check if routine is "none selected"
        if(!strNoneSelected.equals(saveItem.routineName)){
            //a routine was selected, get exercises in routine
            mRoutineButler.loadRoutineByName(mSaveItem.routineName, LOADER_ROUTINE, new RoutineButler.OnLoadedListener() {
                @Override
                public void onLoaded(ArrayList<RoutineItem> routineList) {
                    //routine list has loaded
                    onSaveRoutineLoaded(routineList);
                }
            });
        }
        else{
            //no routine, save scheduled appointment
            butlerSaveAppointment(mSaveItem);
        }
    }

    /*
     * void onSaveRoutineLoaded(...) - exercise routine to save has been loaded
     */
    private void onSaveRoutineLoaded(ArrayList<RoutineItem> routineList){
        //clear exercise buffer list
        mExercises.clear();

        //save exercise to buffer
        mExercises.addAll(routineList);

        //set routine recursive counter
        mRoutineCounter = 0;

        //save routine (recursive)
        butlerSaveRoutine(mRoutineCounter);
    }


    /*
     * void butlerSaveRoutine(int) - user butler to save exercise routine
     */
    private void butlerSaveRoutine(int count){
        //check recursive counter, less than exercise list
        if(mRoutineCounter < mExercises.size()){
            //get client routine item
            ClientRoutineItem item = new ClientRoutineItem(mSaveItem, mExercises.get(count));

            //save client routine to database
            mClientRoutineButler.saveClientRoutine(item, new ClientRoutineButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    //increase counter
                    mRoutineCounter++;

                    //save exercise routine
                    butlerSaveRoutine(mRoutineCounter);
                }
            });
        }
        else{
            //no more exercises to save, save scheduled appointment
            butlerSaveAppointment(mSaveItem);
        }

    }

    /*
     * void butlerSaveAppointment(...) - use butler to save scheduled appointment
     */
    private void butlerSaveAppointment(ScheduleItem saveItem){
        mScheduleButler.saveSchedule(saveItem, new ScheduleButler.OnScheduleSavedListener() {
            @Override
            public void onScheduleSaved() {
                broadcastScheduleUpdate(mSaveItem);
                onRefresh();
            }
        });
    }

    private void broadcastScheduleUpdate(ScheduleItem item){
        Boss boss = (Boss)mActivity.getApplication();
        boss.broadcastScheduleUpdate(item);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Methods
 *      void deleteAppointment(ScheduleItem) - delete appointment
 *      void deleteAppointmentFromFirebase(...) - delete appointment from firebase
 *      void deleteAppointmentFromDatabase(...) - delete appointment data from database
 */
/**************************************************************************************************/
    /*
     * void butlerDeleteAppointment(ScheduleItem) - delete appointment
     */
    private void butlerDeleteAppointment(ScheduleItem deleteItem){
        mDeleteItem = deleteItem;
        mScheduleButler.deleteSchedule(deleteItem, new ScheduleButler.OnScheduleDeletedListener() {
            @Override
            public void onScheduleDeleted() {
                butlerDeleteRoutine(mDeleteItem);
            }
        });
    }

    private void butlerDeleteRoutine(ScheduleItem deleteItem){
        mClientRoutineButler.deleteClientRoutine(deleteItem, new ClientRoutineButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                if(mEditMode){
                    saveAppointment(mSaveItem);
                }
                else{
                    broadcastScheduleUpdate(mDeleteItem);
                    onRefresh();
                }
            }
        });

    }

/**************************************************************************************************/

}
