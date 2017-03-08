package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.AppointmentAdapter;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
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
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING_APPOINTMENT;

/**************************************************************************************************/
/*
 * DayViewMaid display daily appointment schedule of the user
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

public class DayViewMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mData - array of data used by AppointmentAdapter
    private ArrayList<AppointmentCardItem> mData;

    private ArrayList<ScheduleItem> mAppointments;
    private HashMap<String,String> mAppointmentMap;
    private ArrayList<ClientItem> mClients;
    private ArrayList<RoutineItem> mExercises;
    private int mAppCount;

    //mAdapter - recycler adapter
    private AppointmentAdapter mAdapter;

    private String mUserId;
    private boolean mEditingAppointment;
    private ScheduleItem mDeleteAppointment;
    private ScheduleItem mSaveItem;
    private boolean mRefreshOnDismiss;

    private ScheduleButler mScheduleButler;
    private ClientButler mClientButler;
    private ClientRoutineButler mClientRoutineButler;
    private RoutineButler mRoutineButler;

    private int mAppointmentLoaderId;
    private int mClientLoaderId;
    private String mDatestamp;
    private boolean mShowWarning;

    private String mStrDelete;
    private String mMsgUpdatePart1;
    private String mMsgUpdatePart2;
    private int mPageIndex;
    private int mDialogCounter;

    private AppointmentDialog mAppDialog;
    private DeleteWarningDialog mWarningDialog;

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
                    mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
                }
            };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DayViewMaid - constructor
 */
/**************************************************************************************************/
    /*
     * DayViewMaid(...) - constructor
     */
    public DayViewMaid(String maidKey, int layoutId, String userId, String appointmentDate, int counter){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;
        mDatestamp = appointmentDate;
        mPageIndex = counter;
        mAppointmentLoaderId = LOADER_SCHEDULE + mPageIndex;
        mClientLoaderId = LOADER_CLIENT + mPageIndex;

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
        mAppointments = new ArrayList<>();
        mAppointmentMap = new HashMap<>();
        mClients = new ArrayList<>();
        mExercises = new ArrayList<>();
        mEditingAppointment = false;

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        mShowWarning = prefs.getBoolean(PREF_DELETE_WARNING_APPOINTMENT, true);

        mStrDelete = mActivity.getString(R.string.delete);
        mMsgUpdatePart1 = mActivity.getString(R.string.msg_update_session_part1);
        mMsgUpdatePart2 = mActivity.getString(R.string.msg_update_session_part2);

        mClientButler = new ClientButler(mActivity, mUserId);
        mScheduleButler = new ScheduleButler(mActivity, mUserId);
        mRoutineButler = new RoutineButler(mActivity, mUserId);
    }

    public void start(){
        //update screen
        mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
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
        int adapterLayoutId = R.layout.card_appointment;

        //create adapter consumed by the recyclerView
        mAdapter = new AppointmentAdapter(mActivity, adapterLayoutId);

        //set icon click listener
        mAdapter.setOnImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);
                ClientItem clientItem = (ClientItem)view.getTag(R.string.recycler_tagItem02);

                mDialogCounter = -1;
                onProfileImageClicked(item, clientItem);
            }
        });

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);
                ClientItem clientItem = (ClientItem)view.getTag(R.string.recycler_tagItem02);

                //edit appointment item
                onEditAppointment(item, clientItem);

                return false;
            }
        });

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);
                ClientItem clientItem = (ClientItem)view.getTag(R.string.recycler_tagItem02);

                //onItemClick event occurred
                onItemClicked(item, clientItem);
            }
        });
        mAdapter.setClientList(mClients);

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mTouchCallback);

        //attach helper to recycler, enables drag-and-drop and swipe to dismiss functionality
        itemTouchHelper.attachToRecyclerView(mBasicRecycler.getRecycler());
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
                onFabClicked(view);
            }
        });
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

            mAppDialog.setOnDismissListener(new AppointmentDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mDialogCounter = -1;
                }
            });

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
                        mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
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
                    mDialogCounter = -1;

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
        //save scheduled appointments
        mAppointments = scheduleList;

        //initialize appointment counter
        mAppCount = 0;

        //clear data list consumed by recycler
        mData.clear();

        //clear client list
        mClients.clear();
        mAppointmentMap.clear();
        //mClientMap.clear();

        //load client data
        loadClient();
    }

    /*
     * void loadClient() - load client data has been requested
     */
    private void loadClient(){
        //check appointment count index
        if(mAppCount < mAppointments.size()){
            //index less than appointment list size, get appointment item from list
            mScheduleItem = mAppointments.get(mAppCount);
            String mapKey = mScheduleItem.clientKey + mScheduleItem.datestamp + mScheduleItem.appointmentTime;
            String mapValue = mScheduleItem.clientKey + mScheduleItem.datestamp;
            mAppointmentMap.put(mapKey, mapValue);

            mClientButler.loadClient(mClientLoaderId, mScheduleItem, new ClientButler.OnClientLoadedListener() {
                @Override
                public void onClientLoaded(ClientItem clientItem) {
                    onClientDataLoaded(clientItem);
                }
            });
        }
        else{
            //index is more than appointment list, initialize layout
            prepareFragment();
        }
    }
    private ScheduleItem mScheduleItem;

    /*
     * void onClientDataLoaded(...) - client data has been loaded
     */
    private void onClientDataLoaded(ClientItem clientItem){
        if(clientItem != null){
            //add client item to client list
            mClients.add(clientItem);

            AppointmentCardItem cardItem = mClientButler.createAppointmentCardItem(mScheduleItem, clientItem);
            //add clientCard item to data list
            mData.add(cardItem);
        }

        //increase appointment index count
        mAppCount++;

        //load next client
        loadClient();
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
     * void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
     */
    private void onFabClicked(View view){
        //set edit appointment false, creating new appointment
        mEditingAppointment = false;

        ScheduleItem tmpItem = new ScheduleItem();
        tmpItem.uid = mUserId;
        tmpItem.datestamp = mDatestamp;
        tmpItem.appointmentDate = DateTimeHelper.convertDatestampToDate(mDatestamp);
        tmpItem.appointmentTime = DateTimeHelper.getCurrentTime();
        tmpItem.clientKey = "";
        tmpItem.clientName = "";
        tmpItem.routineName = "";
        tmpItem.status = "";

        mDialogCounter = 0;
        //initialize schedule appointment dialog
        initializeScheduleDialog(tmpItem);
    }

    /*
     * void onIconClicked(View) - icon clicked
     */
    private void onProfileImageClicked(AppointmentCardItem item, ClientItem clientItem){
        Boss boss = (Boss)mActivity.getApplication();

        boss.setClient(clientItem);

        //info icon, show client info screen
        Intent intent = new Intent(mActivity, ClientDetailActivity.class);
        mActivity.startActivity(intent);
    }

    /*
     * void onEditAppointment() - edit appointment item
     */
    private void onEditAppointment(AppointmentCardItem item, ClientItem clientItem){

        //set edit appointment status flag as true
        mEditingAppointment = true;

        //save edit appointment as delete appointment item
        mOldEditAppointment = createScheduleItem(clientItem, item);

        mClientEditItem = clientItem;

        mDialogCounter = 0;
        //open schedule appointment dialog
        initializeScheduleDialog(mOldEditAppointment);
    }

    private ClientItem mClientEditItem;
    private ScheduleItem mOldEditAppointment;

    /*
     * void onDeleteAppointment(int) - delete appointment requested
     */
    private void onDeleteAppointment(int index){
        //set editing old appointment flag, false
        mEditingAppointment = false;

        //get clientItem
        ClientItem clientItem = mClients.get(index);

        //get appointment item to be deleted
        mDeleteAppointment = mAppointments.get(index);

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
    private void onItemClicked(AppointmentCardItem item, ClientItem clientItem){

        Boss boss = (Boss)mActivity.getApplication();

        ScheduleItem scheduleItem = createScheduleItem(clientItem, item);

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

    private ScheduleItem createScheduleItem(ClientItem clientItem, AppointmentCardItem appointmentItem){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.datestamp = appointmentItem.datestamp;
        scheduleItem.appointmentDate = DateTimeHelper.convertDatestampToDate(appointmentItem.datestamp);
        scheduleItem.appointmentTime = appointmentItem.clientInfo;;
        scheduleItem.clientKey = appointmentItem.clientKey;
        scheduleItem.clientName = appointmentItem.clientName;
        scheduleItem.routineName = appointmentItem.routineName;
        scheduleItem.status = "";

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
                mExerciseCounter = 0;
                saveExercises(mExerciseCounter);
            }
        });

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
            mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
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
            mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
        }
    }

/**************************************************************************************************/


}
