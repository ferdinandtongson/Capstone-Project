package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientRoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.query.ClientRoutineQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.AppointmentAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.dialog.AppointmentDialog;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
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
    private ArrayList<ClientItem> mClients;
    private ArrayList<RoutineItem> mExercises;
    private int mAppCount;

    //mAdapter - recycler adapter
    private AppointmentAdapter mAdapter;

    private String mUserId;
    private String mDatestamp;
    private boolean mEditingAppointment;
    private ScheduleItem mDeleteItem;
    private ScheduleItem mSaveItem;
    private boolean mRefreshOnDismiss;

    private int mAppointmentLoaderId;
    private int mClientLoaderId;

    private ScheduleButler mScheduleButler;
    private ClientButler mClientButler;

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
            onAppointmentDeleteRequest(viewHolder.getAdapterPosition());
        }
    };

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

        mData = new ArrayList<>();

        mUserId = userId;
        mDatestamp = appointmentDate;
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

        mAppointments = new ArrayList<>();
        mClients = new ArrayList<>();
        mExercises = new ArrayList<>();
        mEditingAppointment = false;

        mClientButler = new ClientButler(mActivity, mUserId);
        mScheduleButler = new ScheduleButler(mActivity, mUserId);
    }

    public void start(){
        //update screen
        mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleListener);
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

        //create dialog
        mAppDialog = new AppointmentDialog();
        mAppDialog.setDialogValues(mActivity, mUserId, item);
        mAppDialog.setOnSavedListener(new AppointmentDialog.OnSaveClickListener() {
            @Override
            public void onSaveClicked(ScheduleItem appItem, ArrayList<RoutineItem> exercises) {
                onSaveAppointment(appItem, exercises);
            }
        });

        mAppDialog.show(fm, "diaScheduleAppointment");

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
                    mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleListener);
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
        mWarningDialog.show(fm, "diaWarning");

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
     * void loadClient() - load client data has been requested
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
            prepareFragment();
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
        initializeScheduleDialog(item);
    }

    /*
     * void onIconClicked(View) - icon clicked
     */
    private void onProfileImageClicked(View view){
        Boss boss = (Boss)mActivity.getApplication();

        //get index position
        int index = (int)view.getTag(R.string.recycler_tagPosition);

        //info icon, show client info screen
        Intent intent = new Intent(mActivity, ClientDetailActivity.class);
        boss.setClient(mClients.get(index));
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
        initializeScheduleDialog(mDeleteItem);
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
     * void onAppointmentDeleteRequest(int) - delete appointment requested
     */
    private void onAppointmentDeleteRequest(int index){
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

        mScheduleButler.saveSchedule(saveItem, new ScheduleButler.OnScheduleSavedListener() {
            @Override
            public void onScheduleSaved() {
                //load appointments to refresh recycler view
                mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleListener);
            }
        });
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
            //editing, save new appointment used to replace old appointment
            saveAppointment(mSaveItem, mExercises);
        }else{
            //load appointments to update recycler view
            mScheduleButler.loadSchedule(mAppointmentLoaderId, mDatestamp, mOnScheduleListener);
        }
    }

/**************************************************************************************************/


}
