package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.AppointmentFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientAppFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.AppointmentLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.ClientLoader;
import me.makeachoice.gymratpta.controller.modelside.query.AppointmentQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.AppointmentAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.AppointmentFBItem;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.dialog.ScheduleAppointmentDialog;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
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

public class StubSessionKeeper extends GymRatRecyclerKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      ArrayList<ClientCardItem> mData - array of data used by AppointmentAdapter
 *      AppointmentAdapter mAdapter - recycler adapter
 */
/**************************************************************************************************/

    //mData - array of data used by AppointmentAdapter
    private ArrayList<AppointmentCardItem> mData;

    private ArrayList<AppointmentItem> mAppointments;
    private ArrayList<ClientItem> mClients;
    private int mAppCount;

    //mAdapter - recycler adapter
    private AppointmentAdapter mAdapter;

    private String mUserId;
    private boolean editingAppointment;
    private AppointmentItem mDeleteItem;
    private AppointmentItem mSaveItem;


    private ScheduleAppointmentDialog mAppDialog;
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


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StubSessionKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * StubSessionKeeper - constructor
     */
    public StubSessionKeeper(int layoutId){

        //get layout id from HouseKeeper Registry
        mActivityLayoutId = layoutId;

        //set toolbar menu resource id consumed by HomeToolbar
        mToolbarMenuId = R.menu.toolbar_menu;

        //flag used to determine which menu items is selected in drawer component
        mBottomNavSelectedItemId = R.id.nav_sessions;

        mData = new ArrayList();
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

        //get user id from Boss
        mUserId = mBoss.getUserId();

        loadAppointment();
    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //set saved instance state data
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize ui
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler component
 *      ScheduleAppointmentDialog initializeScheduleDialog - initialize appointment scheduling dialog
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
     * ScheduleAppointmentDialog initializeScheduleDialog - initialize appointment scheduling dialog
     */
    private ScheduleAppointmentDialog initializeScheduleDialog(AppointmentItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mAppDialog = new ScheduleAppointmentDialog();
        mAppDialog.setDialogValues(mActivity, mUserId, item);
        mAppDialog.setOnSavedListener(new ScheduleAppointmentDialog.OnSaveClickListener() {
            @Override
            public void onSaveClicked(AppointmentItem appItem) {
                onSaveAppointment(appItem);
            }
        });

        mAppDialog.show(fm, "diaScheduleAppointment");

        return mAppDialog;
    }

    /*
     * DeleteWarningDialog initializeDialog(...) - delete warning dialog
     */
    private DeleteWarningDialog initializeWarningDialog(AppointmentItem deleteItem, ClientItem clientItem) {
        String strTitle = clientItem.clientName + " @" + mDeleteItem.appointmentTime;

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
                //resetData();
            }
        });

        //set onDelete listener
        mWarningDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
            @Override
            public void onDelete() {
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
 *      void loadAppointment() - load today's appointment data from database
 *      void loadClient() - load client data from database
 *      void onAppointmentsLoaded(Cursor) - appointments from database has been loaded
 *      void onClientLoaded(Cursor) - client data from database has been loaded
 *      createClientCardItem(...) - create clientCard item consumed by adapter
 */
/**************************************************************************************************/
    /*
     * void loadAppointment() - load today's appointment data from database
     */
    private void loadAppointment(){
        //get today's date
        String strToday = DateTimeHelper.getToday();

        //start loader to get appointment data from database
        AppointmentLoader.loadAppointmentByDate(mActivity, mUserId, strToday, new AppointmentLoader.OnAppointmentLoadListener() {
            @Override
            public void onAppointmentLoadFinished(Cursor cursor){
                onAppointmentsLoaded(cursor);
            }
        });
    }

    /*
     * void loadClient() - load client data from database
     */
    private void loadClient(){
        //check appointment count index
        if(mAppCount < mAppointments.size()){
            //index less than appointment list size, get appointment item from list
            AppointmentItem item = mAppointments.get(mAppCount);

            //load client data using client firebase key
            ClientLoader.loadClientsByFKey(mActivity, mUserId, item.clientKey, new ClientLoader.OnClientLoadListener() {
                @Override
                public void onClientLoadFinished(Cursor cursor) {
                    onClientLoaded(cursor);
                }
            });
        }
        else{
            //index is more than appointment list, initialize layout
            initializeLayout();
        }

    }

    /*
     * void onAppointmentsLoaded(Cursor) - appointments from database has been loaded
     */
    private void onAppointmentsLoaded(Cursor cursor){
        //clear appointment list array
        mAppointments.clear();

        //get number of appointments loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create appointment item from cursor data
            AppointmentItem item = new AppointmentItem(cursor);

            //add appointment item to list
            mAppointments.add(item);
        }

        //destroy loader
        AppointmentLoader.destroyLoader(mActivity);

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
     * void onClientLoaded(Cursor) - client data from database has been loaded
     */
    private void onClientLoaded(Cursor cursor){
        //check that cursor is Not null or empty
        if(cursor != null && cursor.getCount() > 0){
            //move cursor to first index position
            cursor.moveToFirst();

            //create client item from cursor data
            ClientItem clientItem = new ClientItem(cursor);

            //add client item to client list
            mClients.add(clientItem);

            //get appointment item from appointment list
            AppointmentItem appItem = mAppointments.get(mAppCount);

            AppointmentCardItem item = createAppointmentCardItem(appItem, clientItem);

            //add clientCardI item to data list
            mData.add(item);

        }

        //destroy loader
        ClientLoader.destroyLoader(mActivity);

        //increase appointment index count
        mAppCount++;

        //load next client
        loadClient();
    }

    /*
     * createAppointmentCardItem(...) - create appointmentCard item consumed by adapter
     */
    private AppointmentCardItem createAppointmentCardItem(AppointmentItem appItem, ClientItem clientItem){
        //create appointmentCard item used by adapter
        AppointmentCardItem item = new AppointmentCardItem();
        item.clientName = clientItem.clientName;
        item.clientInfo = appItem.appointmentTime;
        item.profilePic = Uri.parse(clientItem.profilePic);
        item.routineName = appItem.routineName;
        item.isActive = true;

        return item;
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void backPressed() - called when Activity.onBackPressed is called
 *      void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
 *      void onIconClicked(View) - icon clicked
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
        editingAppointment = false;

        //initialize schedule appointment dialog
        initializeScheduleDialog(null);
    }

    /*
     * void onIconClicked(View) - icon clicked
     */
    private void onProfileImageClicked(View view){
        //get appointment card item from icon imageView component
        AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);

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
        editingAppointment = true;

        //save edit appointment as delete appointment item
        mDeleteItem = mAppointments.get(index);

        //open schedule appointment dialog
        initializeScheduleDialog(mDeleteItem);
    }

    /*
     * void onSaveAppointment(...) - save appointment item
     */
    private void onSaveAppointment(AppointmentItem appointmentItem){
        //get save appointment item
        mSaveItem = appointmentItem;

        //dismiss schedule appointment dialog
        mAppDialog.dismiss();

        //check if editing an old appointment
        if(editingAppointment){
            //yes, delete old appointment (new appointment will be saved after deletion)
            deleteAppointment(mDeleteItem);
        }
        else{
            //no, save new appointment
            saveAppointment(mSaveItem);
        }
    }

    /*
     * void onAppointmentDeleteRequest(int) - delete appointment requested
     */
    private void onAppointmentDeleteRequest(int index){
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
            initializeWarningDialog(mDeleteItem, clientItem);
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
 *      void saveAppointment(AppointmentItem) - save appointment item
 *      void saveAppointmentToFirebase(AppointmentItem) - save appointment to firebase
 *      void saveAppointmentToDatabase(...) - save appointment to local database
 */
/**************************************************************************************************/
    /*
     * void saveAppointment(AppointmentItem) - save appointment item
     */
    private void saveAppointment(AppointmentItem saveItem){
        //save to firebase
        saveAppointmentToFirebase(saveItem);

        //save to local database
        saveAppointmentToDatabase(saveItem);

    }

    /*
     * void saveAppointmentToFirebase(AppointmentItem) - save appointment to firebase
     */
    private void saveAppointmentToFirebase(AppointmentItem saveItem){
        //get appointment firebase helper instance
        AppointmentFirebaseHelper appointmentFB = AppointmentFirebaseHelper.getInstance();

        //create appointment firebase item
        AppointmentFBItem appointmentFBItem = new AppointmentFBItem();
        appointmentFBItem.appointmentTime = saveItem.appointmentTime;
        appointmentFBItem.clientKey = saveItem.clientKey;
        appointmentFBItem.clientName = saveItem.clientName;
        appointmentFBItem.routineName = saveItem.routineName;
        appointmentFBItem.status = saveItem.status;

        //save appointment item to firebase
        appointmentFB.addAppointmentByDay(mUserId, saveItem.appointmentDate, appointmentFBItem);

        //get client appointment firebase helper instance
        ClientAppFirebaseHelper clientFB = ClientAppFirebaseHelper.getInstance();

        //create client appointment firebase item
        ClientAppFBItem clientFBItem = new ClientAppFBItem();
        clientFBItem.appointmentDate = saveItem.appointmentDate;
        clientFBItem.appointmentTime = saveItem.appointmentTime;
        clientFBItem.clientName = saveItem.clientName;
        clientFBItem.routineName = saveItem.routineName;
        clientFBItem.status = saveItem.status;

        //save client appointment to firebase
        clientFB.addClientAppByClientKey(mUserId, saveItem.clientKey, clientFBItem);
    }

    /*
     * void saveAppointmentToDatabase(...) - save appointment to local database
     */
    private void saveAppointmentToDatabase(AppointmentItem saveItem){
        //get uri value for routine name table
        Uri uriValue = Contractor.AppointmentEntry.CONTENT_URI;

        //appointment is new, add appointment to database
        Uri uri = mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        //load appointments to refresh recycler view
        loadAppointment();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Methods
 *      void deleteAppointment(AppointmentItem) - delete appointment
 *      void deleteAppointmentFromFirebase(...) - delete appointment from firebase
 *      void deleteAppointmentFromDatabase(...) - delete appointment data from database
 */
/**************************************************************************************************/
    /*
     * void deleteAppointment(AppointmentItem) - delete appointment
     */
    private void deleteAppointment(AppointmentItem deleteItem){
        deleteAppointmentFromFirebase(deleteItem);
        deleteAppointmentFromDatabase(deleteItem);

    }

    /*
     * void deleteAppointmentFromFirebase(...) - delete appointment from firebase
     */
    private void deleteAppointmentFromFirebase(AppointmentItem deleteItem){
        //create string values used to delete appointment
        String appDate = deleteItem.appointmentDate;
        String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;
        String clientName = deleteItem.clientName;

        //get appointment firebase helper instance
        AppointmentFirebaseHelper appointmentFB = AppointmentFirebaseHelper.getInstance();
        //delete appointment from firebase
        appointmentFB.deleteAppointment(mUserId, appDate, clientName, appTime);

        //get client appointment firebase helper
        ClientAppFirebaseHelper clientFB = ClientAppFirebaseHelper.getInstance();
        //delete client appointment from firebase
        clientFB.deleteAppointment(mUserId, clientKey, appDate, appTime);

        //check if editing old appointment
        if(editingAppointment){
            //editing, save new appointment used to replace old appointment
            saveAppointment(mSaveItem);
        }
    }

    /*
     * void deleteAppointmentFromDatabase(...) - delete appointment data from database
     */
    private void deleteAppointmentFromDatabase(AppointmentItem deleteItem){
        //create string values used to delete appointment
        String appDate = deleteItem.appointmentDate;
        String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;

        //get uri value from appointment table
        Uri uri = Contractor.AppointmentEntry.CONTENT_URI;

        //remove appointment from database
        int appointmentDeleted = mActivity.getContentResolver().delete(uri,
                AppointmentQueryHelper.clientKeyDateTimeSelection,
                new String[]{mUserId, clientKey, appDate, appTime});

        //load appointments to update recycler view
        loadAppointment();
    }

/**************************************************************************************************/

}
