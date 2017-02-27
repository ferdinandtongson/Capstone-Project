package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientRoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.query.ClientRoutineQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.AppointmentAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.activity.SessionDetailActivity;
import me.makeachoice.gymratpta.view.dialog.AppointmentDialog;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING_APPOINTMENT;

/**************************************************************************************************/
/*
 *  TODO - Enable Context Menu
 *          todo - reschedule session
 *          todo - cancel session
 *  TODO - Add divider lines in Context Menu
 *  TODO - Use client data for email and phone icon events
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * SessionKeeper is responsible for showing the list of clients scheduled to have a session for that
 * day. It extends GymRatRecyclerKeeper which extends GymRatRecyclerBase so is directly responsible
 * for drawer navigation, toolbar, "empty" textView, recycler view and FAB components.
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Variables from GymRatBaseKeeper:
 *      Boss mBoss - Boss application
 *      HomeToolbar mToolbar - toolbar component
 *      HomeDrawer mHomeDrawer - drawer navigation component
 *      int mToolbarMenuId - toolbar menu resource id
 *      int mBottomNavSelectedItemId - used to determine which menu item is selected in the drawer
 *
 * Variables from GymRateRecyclerKeeper:
 *      TextView mTxtEmpty - textView component displayed when recycler is empty
 *      BasicRecycler mBasicRecycler - recycler component
 *      FloatingActionButton mFAB - floating action button component

 * Methods from MyHouseKeeper
 *      void create(MyActivity,Bundle) - called by onCreate(Bundle) in the activity lifecycle
 *      boolean isTablet() - return device flag if device is tablet or not
 *
 * MyHouseKeeper Implements MyActivity.Bridge
 *      void start() - called by onStart() in the activity lifecycle
 *      void resume() - called by onResume() in the activity lifecycle
 *      void pause() - called by onPause() in the activity lifecycle
 *      void stop() - called by onStop() in the activity lifecycle
 *      void destroy() - called by onDestroy() in the activity lifecycle
 *      void backPressed() - called by onBackPressed() in the activity
 *      void saveInstanceState(Bundle) - called before onDestroy(), save state to bundle
 *      void activityResult(...) - result of Activity called by this Activity
 *
 */

/**************************************************************************************************/

public class SessionKeeper extends GymRatRecyclerKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      ArrayList<ClientCardItem> mData - array of data used by AppointmentAdapter
 *      AppointmentAdapter mAdapter - recycler adapter
 */
/**************************************************************************************************/

    //mData - array of data used by AppointmentAdapter
    private ArrayList<AppointmentCardItem> mData;

    private ArrayList<ScheduleItem> mAppointments;
    private ArrayList<ClientItem> mClients;
    private ArrayList<RoutineItem> mExercises;
    private int mAppCount;

    //mAdapter - recycler adapter
    private AppointmentAdapter mAdapter;

    private String mUserId;
    private boolean mEditingAppointment;
    private ScheduleItem mDeleteItem;
    private ScheduleItem mSaveItem;
    private boolean mRefreshOnDismiss;

    private ScheduleButler mScheduleButler;
    private ClientButler mClientButler;

    private int mAppointmentLoaderId;
    private int mClientLoaderId;
    private String mDatestamp;

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
 * SessionKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * SessionKeeper - constructor
     */
    public SessionKeeper(int layoutId){

        //get layout id from HouseKeeper Registry
        mActivityLayoutId = layoutId;

        //set toolbar menu resource id consumed by HomeToolbar
        mToolbarMenuId = R.menu.toolbar_menu;

        //flag used to determine which menu items is selected in drawer component
        mBottomNavSelectedItemId = R.id.nav_sessions;

        mData = new ArrayList<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Activity Lifecycle Methods
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
 */
/**************************************************************************************************/
    /*
     * void create(MyActivity, Bundle) - called when onCreate is called in the activity. Sets the
     * activity layout, fragmentManager and other child views of the activity
     *
     * NOTE: Both FragmentManager and FAB are context sensitive and need to be recreated every time
     * onCreate() is called in the Activity
     * @param activity - current activity being shown
     * @param bundle - instant state values
     */
    public void create(MyActivity activity, Bundle bundle){
        super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        mAppointments = new ArrayList<>();
        mClients = new ArrayList<>();
        mExercises = new ArrayList<>();
        mEditingAppointment = false;

        //get user id from Boss
        mUserId = mBoss.getUserId();

        mDatestamp = DateTimeHelper.getDatestamp(0);
        mAppointmentLoaderId = LOADER_SCHEDULE;
        mClientLoaderId = LOADER_CLIENT;

        mClientButler = new ClientButler(mActivity, mUserId);
        mScheduleButler = new ScheduleButler(mActivity, mUserId);
    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //set saved instance state data
    }

    public void start(){
        super.start();
        mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize ui
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler component
 *      AppointmentDialog initializeScheduleDialog - initialize appointment scheduling dialog
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize ui
     */
    private void initializeLayout(){

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
                onProfileImageClicked(view);
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

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //onClick event
                onItemClicked(index);
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
     * AppointmentDialog initializeAppointmentDialog - initialize appointment scheduling dialog
     */
    private AppointmentDialog initializeAppointmentDialog(ScheduleItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mAppDialog = new AppointmentDialog();
        mAppDialog.setDialogValues(mActivity, mUserId, item);
        mAppDialog.setOnSavedListener(new AppointmentDialog.OnSaveClickListener() {
            @Override
            public void onSaveClicked(ScheduleItem appItem, ArrayList<RoutineItem> exercises) {
                onSaveAppointment(appItem, exercises);
            }
        });

        mAppDialog.show(fm, "diaAppointmentDialog");

        return mAppDialog;
    }

    /*
     * DeleteWarningDialog initializeDialog(...) - delete warning dialog
     */
    private DeleteWarningDialog initializeWarningDialog(ClientItem clientItem) {
        String strTitle = clientItem.clientName + " @" + mDeleteItem.appointmentTime;
        mRefreshOnDismiss = true;

        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mWarningDialog = new DeleteWarningDialog();

        //set dialog values
        mWarningDialog.setDialogValues(mActivity, mUserId, strTitle);

        //set onDismiss listener
        mWarningDialog.setOnDismissListener(new DeleteWarningDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(mRefreshOnDismiss){
                    mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
                }
            }
        });

        //set onDelete listener
        mWarningDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
            @Override
            public void onDelete() {
                mRefreshOnDismiss = false;
                //delete routine
                deleteAppointment(mDeleteItem);

                //dismiss dialog
                mWarningDialog.dismiss();
            }
        });


        //show dialog
        mWarningDialog.show(fm, "diaDeleteRoutine");

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

        //load client data
        loadClient();

    }

    /*
     * void loadClient() - load client data from database
     */
    private void loadClient(){
        //check appointment count index
        if(mAppCount < mAppointments.size()){
            //index less than appointment list size, get appointment item from list
            ScheduleItem item = mAppointments.get(mAppCount);

            mClientButler.loadClient(mClientLoaderId, item, new ClientButler.OnClientLoadedListener() {
                @Override
                public void onClientLoaded(ClientItem clientItem, AppointmentCardItem cardItem) {
                    onClientDataLoaded(clientItem, cardItem);
                }
            });
        }
        else{
            //index is more than appointment list, initialize layout
            initializeLayout();
        }

    }

    /*
     * void onClientDataLoaded(...) - client data has been loaded
     */
    private void onClientDataLoaded(ClientItem clientItem, AppointmentCardItem cardItem){
        if(clientItem != null && cardItem != null){
            //add client item to client list
            mClients.add(clientItem);

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
 *      void backPressed() - called when Activity.onBackPressed is called
 *      void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
 *      void onProfileImageClicked(View) - profile image clicked, open client info screen
 *      void onEditAppointment() - edit appointment item
 *      void onSaveAppointment(...) - save appointment item
 *      void onAppointmentDeleteRequest(int) - delete appointment requested
 */
/**************************************************************************************************/
    /*
     * void backPressed() - called when Activity.onBackPressed is called
     */
    @Override
    public void backPressed(){
    }

    /*
     * void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
     */
    private void onFabClicked(View view){
        //set delete appointment item to null
        mDeleteItem = null;

        //set edit appointment false, creating new appointment
        mEditingAppointment = false;

        String noneSelected = mActivity.getString(R.string.dialogSchedule_no_routine_selected);

        ScheduleItem item = new ScheduleItem();
        item.uid = mUserId;
        item.datestamp = mDatestamp;
        item.appointmentDate = DateTimeHelper.convertDatestampToDate(mDatestamp);
        item.appointmentTime = DateTimeHelper.getCurrentTime();
        item.clientKey = "";
        item.clientName = "";
        item.routineName = noneSelected;
        item.status = "";

        //initialize schedule appointment dialog
        initializeAppointmentDialog(null);
    }

    /*
     * void onProfileImageClicked(View) - profile image clicked, open client info screen
     */
    private void onProfileImageClicked(View view){
        //get index position
        int index = (int)view.getTag(R.string.recycler_tagPosition);

        //info icon, show client info screen
        Intent intent = new Intent(mActivity, ClientDetailActivity.class);
        mBoss.setClient(mClients.get(index));
        mActivity.startActivity(intent);
    }

    /*
     * void onEditAppointment() - edit appointment item
     */
    private void onEditAppointment(int index){
        //set edit appointment status flag as true
        mEditingAppointment = true;

        //save edit appointment as delete appointment item
        mDeleteItem = mAppointments.get(index);

        //open schedule appointment dialog
        initializeAppointmentDialog(mDeleteItem);
    }

    /*
     * void onSaveAppointment(...) - save appointment item
     */
    private void onSaveAppointment(ScheduleItem appointmentItem, ArrayList<RoutineItem> exercises){
        //clear routine exercise list
        mExercises.clear();

        //save new routine exercise list
        mExercises = exercises;

        //get save appointment item
        mSaveItem = appointmentItem;

        //dismiss schedule appointment dialog
        mAppDialog.dismiss();

        //check if editing an old appointment
        if(mEditingAppointment){
            //yes, delete old appointment (new appointment will be saved after deletion)
            deleteAppointment(mDeleteItem);
        }
        else{
            //no, save new appointment
            saveAppointment(mSaveItem, mExercises);
        }
    }


    /*
     * void onDeleteAppointment(int) - delete appointment requested
     */
    private void onDeleteAppointment(int index){
        //set editing old appointment flag, false
        mEditingAppointment = false;
        
        //get clientItem
        ClientItem clientItem = mClients.get(index);

        //get appointment item to be deleted
        mDeleteItem = mAppointments.get(index);

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        boolean showWarning = prefs.getBoolean(PREF_DELETE_WARNING_APPOINTMENT, true);

        //check status
        if(showWarning){
            //wants warning, show "delete warning" dialog
            initializeWarningDialog(clientItem);
        }
        else{
            //no warning, delete routine
            deleteAppointment(mDeleteItem);
        }
    }

    private void onItemClicked(int index){
        //get appointment card item from icon imageView component
        ScheduleItem appItem = mAppointments.get(index);

        //info icon, show client info screen
        Intent intent = new Intent(mActivity, SessionDetailActivity.class);
        mBoss.setClient(mClients.get(index));
        mBoss.setAppointmentItem(appItem);
        mActivity.startActivity(intent);

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
     * void saveAppointment(ScheduleItem) - save appointment
     */
    private void saveAppointment(ScheduleItem saveItem, ArrayList<RoutineItem> exercises){
        saveExercisesToFirebase(saveItem, exercises);
        saveExercisesToDatabase(saveItem, exercises);

        mScheduleButler.saveSchedule(saveItem, mOnScheduleSaveListener);
    }

    private void saveExercisesToFirebase(ScheduleItem appItem, ArrayList<RoutineItem> exercises){
        //get client routine firebase helper instance
        ClientRoutineFirebaseHelper routineFB = ClientRoutineFirebaseHelper.getInstance();

        //save client appointment to firebase
        routineFB.addRoutineDataByDateTime(mUserId, appItem.clientKey, appItem.appointmentDate,
                appItem.appointmentTime, exercises);
    }

    private void saveExercisesToDatabase(ScheduleItem saveItem, ArrayList<RoutineItem> exercises){
        //get uri value for routine name table
        Uri uriValue = Contractor.ClientRoutineEntry.CONTENT_URI;

        int count = exercises.size();
        for(int i = 0; i < count; i++){
            RoutineItem exercise = exercises.get(i);
            ClientRoutineItem item = new ClientRoutineItem(saveItem, exercise);

            //appointment is new, add appointment to database
            mActivity.getContentResolver().insert(uriValue, item.getContentValues());
        }
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
    private void deleteAppointment(ScheduleItem deleteItem){
        mDeleteItem = deleteItem;

        mScheduleButler.deleteSchedule(deleteItem, new ScheduleButler.OnScheduleDeletedListener() {
            @Override
            public void onScheduleDeleted() {
                deleteRoutineExercises(mDeleteItem);
            }
        });
    }

    private void deleteRoutineExercises(ScheduleItem deleteItem){
        //create string values used to delete appointment
        String appDate = deleteItem.appointmentDate;
        final String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;

        //get client routine firebase helper
        ClientRoutineFirebaseHelper routineFB = ClientRoutineFirebaseHelper.getInstance();


        //delete client routine from firebase
        routineFB.deleteClientRoutine(mUserId, clientKey, appDate, appTime, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deleteClientRoutineFromDatabase(mDeleteItem);
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
        String appDate = deleteItem.appointmentDate;
        String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;

        //get uri value from appointment table
        Uri uri = Contractor.ClientRoutineEntry.CONTENT_URI;

        //remove client routine from database
        mActivity.getContentResolver().delete(uri,
                ClientRoutineQueryHelper.clientKeyDateTimeSelection,
                new String[]{mUserId, clientKey, appDate, appTime});

        if(mEditingAppointment) {
            saveAppointment(mSaveItem, mExercises);
       }else{
            //load appointments to update recycler view
            mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleLoadListener);
        }
    }

/**************************************************************************************************/

}
