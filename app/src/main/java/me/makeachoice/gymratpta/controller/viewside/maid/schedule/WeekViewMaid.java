package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientRoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientRoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.query.ClientRoutineQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.schedule.ScheduleWeekAdapter;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.activity.ScheduleDetailActivity;
import me.makeachoice.gymratpta.view.activity.SessionDetailActivity;
import me.makeachoice.gymratpta.view.dialog.AppointmentDialog;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_WARNING_DELETE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING_APPOINTMENT;

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
    private ArrayList<RoutineItem> mExercises;
    private HashMap<String,String> mAppointmentMap;

    private String mUserId;

    private String[] mDateRange;
    private Date mStartDate;

    private int mAppointmentLoaderId;
    private int mClientLoaderId;

    private ScheduleButler mScheduleButler;
    private ClientRoutineButler mClientRoutineButler;
    private RoutineButler mRoutineButler;
    private ClientButler mClientButler;

    private HashMap<String,ClientItem> mClientMap;
    private ScheduleWeekAdapter mAdapter;

    private AppointmentDialog mAppDialog;
    private DeleteWarningDialog mWarningDialog;
    private int mDialogCounter;

    private boolean mShowWarning;
    private boolean mEditingAppointment;
    private boolean mRefreshOnDismiss;
    private ScheduleItem mSaveItem;
    private ScheduleItem mDeleteAppointment;
    private AppointmentCardItem mContextCard;

    private String mStrDelete;
    private String mMsgUpdatePart1;
    private String mMsgUpdatePart2;


    private ScheduleButler.OnScheduleLoadedListener mOnScheduleLoadListener =
            new ScheduleButler.OnScheduleLoadedListener() {
                @Override
                public void onScheduleLoaded(ArrayList<ScheduleItem> scheduleList) {
                    onDayScheduleLoaded(scheduleList);
                }
            };

    private ScheduleButler.OnScheduleSavedListener mOnScheduleSaveListener =
            new ScheduleButler.OnScheduleSavedListener() {
                @Override
                public void onScheduleSaved() {
                    mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleLoadListener);
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

        mUserId = userId;
        mDateRange = dateRange;
        mStartDate = startDate;

        mAppointmentLoaderId = LOADER_SCHEDULE + counter;
        mClientLoaderId = LOADER_CLIENT + counter;

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

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        mShowWarning = prefs.getBoolean(PREF_DELETE_WARNING_APPOINTMENT, true);

        mData = new ArrayList<>();
        mClientMap = new HashMap<>();
        mAppointmentMap = new HashMap<>();
        mExercises = new ArrayList<>();

        mStrDelete = mActivity.getString(R.string.delete);
        mMsgUpdatePart1 = mActivity.getString(R.string.msg_update_session_part1);
        mMsgUpdatePart2 = mActivity.getString(R.string.msg_update_session_part2);


        mScheduleButler = new ScheduleButler(mActivity, mUserId);
        mRoutineButler = new RoutineButler(mActivity, mUserId);
        mClientButler = new ClientButler(mActivity, mUserId);

    }


    public void start(){

        if(mClientMap.isEmpty()){
            loadClient();
        }
        else{
            //update screen
            mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleLoadListener);
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
        int adapterLayoutId = R.layout.card_appointment;

        //create adapter consumed by the recyclerView
        mAdapter = new ScheduleWeekAdapter(mActivity, adapterLayoutId);

        //set icon click listener
        mAdapter.setOnImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);

                mDialogCounter = -1;
                onProfileImageClicked(item);
            }
        });

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);

                if(item.isActive){
                    mDialogCounter = -1;
                    onScheduleClicked(item);
                }
                else{
                    onDateClicked(item);
                }

            }
        });

        mAdapter.setContextMenuListener(new ScheduleWeekAdapter.ContextMenuListener() {
            @Override
            public void contextMenuCreated(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                mContextCard = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);
            }

            @Override
            public void contextMenuItemSelected(MenuItem item) {
                //get string value of menu item selected
                String strItem = item.toString();

                //get "Edit" string value from resource
                String strEdit = mActivity.getString(R.string.edit);
                //get "Delete" string value from resource
                String strDelete = mActivity.getString(R.string.delete);

                //find which menu item was selected
                if(strItem.equals(strEdit)){
                    //edit item in list and database
                    onEditAppointment(mContextCard);
                }
                else if(strItem.equals(strDelete)){
                    //delete item from list and database
                    onDeleteAppointment(mContextCard);
                }

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

    }

    /*
     * AppointmentDialog initializeScheduleDialog - initialize appointment scheduling dialog
     */
    private AppointmentDialog initializeScheduleDialog(ScheduleItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        if(mDialogCounter == 0){
            //create dialog
            mAppDialog = new AppointmentDialog();
            mAppDialog.setDialogValues(mActivity, mUserId, item, mAppointmentMap);
            mAppDialog.setEditMode(mEditingAppointment);

            mAppDialog.setOnSavedListener(new AppointmentDialog.OnSaveClickListener() {
                @Override
                public void onSaveClicked(ScheduleItem appItem) {
                    mAppDialog.dismiss();
                    mDialogCounter = -1;
                    onSaveAppointment(appItem);
                }
            });

            mAppDialog.show(fm, DIA_SCHEDULE);
            mDialogCounter = 1;
        }

        return mAppDialog;
    }

    /*
     * DeleteWarningDialog initializeDialog(...) - delete warning dialog
     */
    private DeleteWarningDialog initializeWarningDialog(ClientItem clientItem, String title) {
        String strTitle = title;
        mRefreshOnDismiss = true;

        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        if(mDialogCounter == 0){
            //create dialog
            mWarningDialog = new DeleteWarningDialog();

            //set dialog values
            mWarningDialog.setDialogValues(mActivity, mUserId, strTitle);
            mWarningDialog.setTitle(strTitle);

            //set onDismiss listener
            mWarningDialog.setOnDismissListener(new DeleteWarningDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(mRefreshOnDismiss){
                        mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleLoadListener);
                    }
                    mDialogCounter = -1;
                }
            });

            //set onDelete listener
            mWarningDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
                @Override
                public void onDelete() {
                    mRefreshOnDismiss = false;

                    //dismiss dialog
                    mWarningDialog.dismiss();

                    if(mEditingAppointment){
                        deleteOldEditAppointment();
                    }
                    else{
                        //delete routine
                        deleteAppointment();
                    }

                }
            });

            //show dialog
            mWarningDialog.show(fm, DIA_WARNING_DELETE);
            mDialogCounter = 1;
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

        mAppointmentMap.clear();

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
                clientItem = mClientMap.get(weekItem.clientKey);

                String mapKey = weekItem.clientKey + weekItem.datestamp + weekItem.appointmentTime;
                String mapValue = weekItem.clientKey + weekItem.datestamp;
                mAppointmentMap.put(mapKey, mapValue);

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
        mClientButler.loadActiveClients(mClientLoaderId, new ClientButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<ClientItem> clientList) {
                onClientDataLoaded();
            }
        });
    }

    /*
     * void onClientDataLoaded(...) - client data has been loaded
     */
    private void onClientDataLoaded(){
        mClientMap.putAll(mClientButler.getClientMap());
        mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleLoadListener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
 *      void onIconClicked(View) - icon clicked
 *      void onEditAppointment() - edit appointment item
 *      void onSaveAppointment(...) - save appointment item
 *      void onAppointmentDeleteRequest(int) - delete appointment requested
 */
/**************************************************************************************************/
    /*
     * void onIconClicked(View) - icon clicked
     */
    private void onProfileImageClicked(AppointmentCardItem item){
        Boss boss = (Boss)mActivity.getApplication();

        //get index position
        ClientItem clientItem = mClientMap.get(item.clientKey);

        boss.setClient(clientItem);

        //info icon, show client info screen
        Intent intent = new Intent(mActivity, ClientDetailActivity.class);
        mActivity.startActivity(intent);
    }

    /*
     * void onEditAppointment() - edit appointment item
     */
    private void onEditAppointment(AppointmentCardItem appointmentItem){
        //set edit appointment status flag as true
        mEditingAppointment = true;

        ClientItem clientItem = mClientMap.get(appointmentItem.clientKey);

        //save edit appointment as delete appointment item
        mOldEditAppointment = createScheduleItem(clientItem, appointmentItem);

        mClientEditItem = mClientMap.get(appointmentItem.clientKey);

        mDialogCounter = 0;
        //open schedule appointment dialog
        initializeScheduleDialog(mOldEditAppointment);
    }

    private ClientItem mClientEditItem;
    private ScheduleItem mOldEditAppointment;

    /*
     * void onDeleteAppointment(int) - delete appointment requested
     */
    private void onDeleteAppointment(AppointmentCardItem appointmentItem){
        //set editing old appointment flag, false
        mEditingAppointment = false;

        //get clientItem
        ClientItem clientItem = mClientMap.get(appointmentItem.clientKey);

        //get appointment item to be deleted
        mDeleteAppointment = createScheduleItem(clientItem, appointmentItem);

        //check status
        if(mShowWarning){
            String msg = mStrDelete + ": " + clientItem.clientName + " @" + mDeleteAppointment.appointmentTime;

            mDialogCounter = 0;
            //wants warning, show "delete warning" dialog
            initializeWarningDialog(clientItem, msg);
        }
        else{
            //no warning, delete routine
            deleteAppointment();
        }
    }

    /*
     * void onItemClicked(int) - exercise routine has been clicked
     */
    private void onScheduleClicked(AppointmentCardItem appointmentItem){
        Boss boss = (Boss)mActivity.getApplication();

        ClientItem clientItem = mClientMap.get(appointmentItem.clientKey);

        ScheduleItem scheduleItem = createScheduleItem(clientItem, appointmentItem);

        boss.setClient(clientItem);
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
     * void onDateClicked(View) - date list item clicked, open schedule appointment dialog
     */
    private void onDateClicked(AppointmentCardItem item){
        //set edit appointment false, creating new appointment
        mEditingAppointment = false;

        ScheduleItem tmpItem = new ScheduleItem();
        tmpItem.uid = mUserId;
        tmpItem.datestamp = item.clientName;
        tmpItem.appointmentDate = DateTimeHelper.convertDatestampToDate(item.clientName);
        tmpItem.appointmentTime = DateTimeHelper.getCurrentTime();
        tmpItem.clientKey = "";
        tmpItem.clientName = "";
        tmpItem.routineName = "";
        tmpItem.status = "";

        mDialogCounter = 0;
        //initialize schedule appointment dialog
        initializeScheduleDialog(tmpItem);
    }

    private ScheduleItem createScheduleItem(ClientItem clientItem, AppointmentCardItem appointmentItem){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.datestamp = appointmentItem.datestamp;
        scheduleItem.appointmentDate = DateTimeHelper.convertDatestampToDate(appointmentItem.datestamp);
        scheduleItem.appointmentTime = appointmentItem.clientInfo;;
        scheduleItem.clientKey = clientItem.fkey;
        scheduleItem.clientName = clientItem.clientName;
        scheduleItem.routineName = appointmentItem.routineName;
        scheduleItem.status = clientItem.status;

        return scheduleItem;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Methods
 *      void saveAppointment(...) - save appointment
 *      void saveAppointmentToFirebase(ScheduleItem) - save appointment to firebase
 *      void saveAppointmentToAppointmentFB(ScheduleItem) - save appointment to appointment firebase
 *      void saveAppointmentToClientAppFB(ScheduleItem) - save appointment to client appointment firebase
 *      void saveAppointmentToDatabase(...) - save appointment to local database
 */
/**************************************************************************************************/
    /*
     * void onSaveAppointment(...) - save appointment item
     */
    private void onSaveAppointment(ScheduleItem item){
        //save appointment item
        mSaveItem = item;

        //open routine butler
        mClientRoutineButler = new ClientRoutineButler(mActivity, mUserId, item.clientKey);

        //check if editing an old appointment
        if(mEditingAppointment){
            //check status
            if(mShowWarning){
                //yes, delete old appointment (new appointment will be saved after deletion)
                String msg = mMsgUpdatePart1 + " @" + mOldEditAppointment.appointmentTime +
                        " " + mMsgUpdatePart2;

                mDialogCounter = 0;
                //wants warning, show "delete warning" dialog
                initializeWarningDialog(mClientEditItem, msg);
            }
            else{
                //no warning, delete routine
                deleteOldEditAppointment();
            }
        }
        else{
            //load exercise details of given routine
            mRoutineButler.loadRoutineByName(item.routineName, LOADER_ROUTINE, new RoutineButler.OnLoadedListener() {
                @Override
                public void onLoaded(ArrayList<RoutineItem> routineList) {
                    //data loaded
                    onExercisesLoaded(routineList);
                }
            });
        }

    }

    /*
     * void onExercisesLoaded(...) - exercises for given routine has been loaded
     */
    private void onExercisesLoaded(ArrayList<RoutineItem> routineList){
        //clear routine exercise list
        mExercises.clear();

        //save new routine exercise list
        mExercises.addAll(routineList);

        //no, save new appointment
        mScheduleButler.saveSchedule(mSaveItem, new ScheduleButler.OnScheduleSavedListener() {
            @Override
            public void onScheduleSaved(){
                broadcastScheduleUpdate(mSaveItem);
                mExerciseCounter = 0;
                saveExercises(mExerciseCounter);
            }
        });

    }

    private void broadcastScheduleUpdate(ScheduleItem item){
        Boss boss = (Boss)mActivity.getApplication();
        boss.broadcastScheduleUpdate(item);
    }


    private int mExerciseCounter;
    private void saveExercises(int counter){

        if(counter < mExercises.size()){
            RoutineItem routineItem = mExercises.get(counter);
            ClientRoutineItem saveClientRoutineItem = new ClientRoutineItem(mSaveItem, routineItem);

            mClientRoutineButler.saveClientRoutine(saveClientRoutineItem, new ClientRoutineButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    mExerciseCounter++;
                    saveExercises(mExerciseCounter);
                }
            });
        }
        else{

            //load appointments to update recycler view
            mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleLoadListener);
        }

    }

    private void deleteOldEditAppointment(){

        mScheduleButler.deleteSchedule(mOldEditAppointment, new ScheduleButler.OnScheduleDeletedListener() {
            @Override
            public void onScheduleDeleted() {
                deleteOldRoutineExercises();
            }
        });
    }

    private void deleteOldRoutineExercises(){
        String timestamp = DateTimeHelper.getTimestamp(mOldEditAppointment.appointmentDate,
                mOldEditAppointment.appointmentTime);
        ClientRoutineItem oldRoutine = new ClientRoutineItem();

        oldRoutine.clientKey = mClientEditItem.fkey;
        oldRoutine.timestamp = timestamp;

        mClientRoutineButler.deleteClientRoutine(oldRoutine, new ClientRoutineButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                updateEditAppointment();
            }
        });
    }

    private void updateEditAppointment(){
        //load exercise details of given routine
        mRoutineButler.loadRoutineByName(mSaveItem.routineName, LOADER_ROUTINE, new RoutineButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<RoutineItem> routineList) {
                //data loaded
                onExercisesLoaded(routineList);
            }
        });
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
     * void deleteAppointment(ScheduleItem) - delete appointment
     */
    private void deleteAppointment(){

        mScheduleButler.deleteSchedule(mDeleteAppointment, new ScheduleButler.OnScheduleDeletedListener() {
            @Override
            public void onScheduleDeleted() {
                broadcastScheduleUpdate(mDeleteAppointment);
                deleteRoutineExercises(mDeleteAppointment);
            }
        });
    }

    private void deleteRoutineExercises(ScheduleItem deleteItem){
        //create string values used to delete appointment
        String timestamp = DateTimeHelper.getTimestamp(deleteItem.appointmentDate, deleteItem.appointmentTime);
        String clientKey = deleteItem.clientKey;

        //get client routine firebase helper
        ClientRoutineFirebaseHelper routineFB = ClientRoutineFirebaseHelper.getInstance();


        //delete client routine from firebase
        routineFB.deleteClientRoutine(mUserId, clientKey, timestamp, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deleteClientRoutineFromDatabase(mDeleteAppointment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*
     * void deleteClientRoutineFromDatabase(...) - delete client routine data from database
     */
    private void deleteClientRoutineFromDatabase(ScheduleItem deleteItem){
        //create string values used to delete appointment
        String timestamp = DateTimeHelper.getTimestamp(deleteItem.appointmentDate, deleteItem.appointmentTime);
        String clientKey = deleteItem.clientKey;

        //get uri value from appointment table
        Uri uri = ClientRoutineContract.CONTENT_URI;

        //remove client routine from database
        mActivity.getContentResolver().delete(uri,
                ClientRoutineQueryHelper.timestampSelection,
                new String[]{mUserId, clientKey, timestamp});

        if(mEditingAppointment) {
            //saveAppointment(mSaveItem, mExercises);
        }else{
            //load appointments to update recycler view
            mScheduleButler.loadRangeSchedule(mAppointmentLoaderId, mDateRange, mOnScheduleLoadListener);
        }
    }

/**************************************************************************************************/

}
