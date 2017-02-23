package me.makeachoice.gymratpta.controller.viewside.maid.appointment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import me.makeachoice.gymratpta.controller.modelside.firebase.client.AppointmentFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientAppFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientRoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.AppointmentLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.ClientLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineLoader;
import me.makeachoice.gymratpta.controller.modelside.query.AppointmentQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.AppointmentAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.AppointmentFBItem;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.dialog.AppointmentDialog;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_APPOINTMENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING_APPOINTMENT;

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

public class DayViewPagerMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mData - array of data used by AppointmentAdapter
    private ArrayList<AppointmentCardItem> mData;

    private ArrayList<AppointmentItem> mAppointments;
    private ArrayList<ClientItem> mClients;
    private ArrayList<RoutineItem> mExercises;
    private int mAppCount;

    //mAdapter - recycler adapter
    private AppointmentAdapter mAdapter;

    private MyActivity mActivity;
    private String mUserId;
    private String mAppointmentDate;
    private boolean mEditingAppointment;
    private AppointmentItem mDeleteItem;
    private AppointmentItem mSaveItem;
    private boolean mRefreshOnDismiss;

    private AppointmentLoader mAppointmentLoader;
    private ClientLoader mClientLoader;
    private int mAppointmentLoaderId;
    private int mClientLoaderId;

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

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DayViewPagerMaid - constructor
 */
/**************************************************************************************************/
    /*
     * DayViewPagerMaid(...) - constructor
     */
    public DayViewPagerMaid(String maidKey, int layoutId, String userId, String appointmentDate, int counter){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mData = new ArrayList<>();

        mUserId = userId;
        mAppointmentDate = appointmentDate;
        mAppointmentLoaderId = LOADER_APPOINTMENT + counter;
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

        mAppointmentLoader = new AppointmentLoader(mActivity, mUserId);
        mClientLoader = new ClientLoader(mActivity, mUserId);
        loadAppointment();

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
    private AppointmentDialog initializeScheduleDialog(AppointmentItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mAppDialog = new AppointmentDialog();
        mAppDialog.setDialogValues(mActivity, mUserId, item);
        mAppDialog.setOnSavedListener(new AppointmentDialog.OnSaveClickListener() {
            @Override
            public void onSaveClicked(AppointmentItem appItem, ArrayList<RoutineItem> exercises) {
                onSaveAppointment(appItem, exercises);
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
                    loadAppointment();
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

        //start loader to get appointment data from database
        mAppointmentLoader.loadAppointmentByDate(mAppointmentDate, mAppointmentLoaderId,
                new AppointmentLoader.OnAppointmentLoadListener() {
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
            mClientLoader.loadClientsByFKey(item.clientKey, mClientLoaderId, new ClientLoader.OnClientLoadListener() {
                @Override
                public void onClientLoadFinished(Cursor cursor) {
                    onClientLoaded(cursor);
                }
            });
        }
        else{
            //index is more than appointment list, initialize layout
            prepareFragment();
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
        mAppointmentLoader.destroyLoader(mAppointmentLoaderId);

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
        mClientLoader.destroyLoader(mClientLoaderId);

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

        //initialize schedule appointment dialog
        initializeScheduleDialog(null);
    }

    /*
     * void onIconClicked(View) - icon clicked
     */
    private void onProfileImageClicked(View view){
        Boss boss = (Boss)mActivity.getApplication();

        //get appointment card item from icon imageView component
        AppointmentCardItem item = (AppointmentCardItem)view.getTag(R.string.recycler_tagItem);

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
    private void onSaveAppointment(AppointmentItem appointmentItem, ArrayList<RoutineItem> exercises){
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
 *      void saveAppointment(...) - save appointment
 *      void saveAppointmentToFirebase(AppointmentItem) - save appointment to firebase
 *      void saveAppointmentToAppointmentFB(AppointmentItem) - save appointment to appointment firebase
 *      void saveAppointmentToClientAppFB(AppointmentItem) - save appointment to client appointment firebase
 *      void saveAppointmentToDatabase(...) - save appointment to local database
 */
/**************************************************************************************************/
    /*
     * void saveAppointment(AppointmentItem) - save appointment
     */
    private void saveAppointment(AppointmentItem saveItem, ArrayList<RoutineItem> exercises){
        //save to firebase
        saveAppointmentToFirebase(saveItem);

        //save to firebase
        saveExercisesToFirebase(saveItem, exercises);

        //save to local database
        saveAppointmentToDatabase(saveItem);

        saveExercisesToDatabase(saveItem, exercises);

        //load appointments to refresh recycler view
        loadAppointment();
    }

    /*
     * void saveAppointmentToFirebase(AppointmentItem) - save appointment to firebase
     */
    private void saveAppointmentToFirebase(AppointmentItem saveItem){
        //save appointment to appointment firebase
        saveAppointmentToAppointmentFB(saveItem);

        //save appointment to client appointment firebase
        saveAppointmentToClientAppFB(saveItem);
    }

    /*
     * void saveAppointmentToAppointmentFB(AppointmentItem) - save appointment to appointment firebase
     */
    private void saveAppointmentToAppointmentFB(AppointmentItem saveItem){
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

    }

    /*
     * void saveAppointmentToClientAppFB(AppointmentItem) - save appointment to client appointment firebase
     */
    private void saveAppointmentToClientAppFB(AppointmentItem saveItem){
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
    }

   private void saveExercisesToFirebase(AppointmentItem appItem, ArrayList<RoutineItem> exercises){
        //get client routine firebase helper instance
        ClientRoutineFirebaseHelper routineFB = ClientRoutineFirebaseHelper.getInstance();

        //save client appointment to firebase
        routineFB.addRoutineDataByDateTime(mUserId, appItem.clientKey, appItem.appointmentDate,
                appItem.appointmentTime, exercises);

    }

    private void saveExercisesToDatabase(AppointmentItem saveItem, ArrayList<RoutineItem> exercises){
        //get uri value for routine name table
        Uri uriValue = Contractor.ClientRoutineEntry.CONTENT_URI;

        int count = exercises.size();
        for(int i = 0; i < count; i++){
            RoutineItem exercise = exercises.get(i);
            ClientRoutineItem item = new ClientRoutineItem(saveItem, exercise);

            //appointment is new, add appointment to database
            Uri uri = mActivity.getContentResolver().insert(uriValue, item.getContentValues());
        }
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
        deleteRoutineExercises(deleteItem);
    }

    private void deleteRoutineExercises(AppointmentItem deleteItem){
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
                deleteAppointmentFromFirebase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /*
     * void deleteAppointmentFromFirebase(...) - delete appointment from firebase
     */
    private void deleteAppointmentFromFirebase(AppointmentItem deleteItem){
        //create string values used to delete appointment
        String appDate = deleteItem.appointmentDate;
        final String appTime = deleteItem.appointmentTime;
        String clientKey = deleteItem.clientKey;
        String clientName = deleteItem.clientName;

        //get appointment firebase helper instance
        AppointmentFirebaseHelper appointmentFB = AppointmentFirebaseHelper.getInstance();
        //delete appointment from firebase
        appointmentFB.deleteAppointment(mUserId, appDate, clientName, appTime, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    AppointmentFBItem appointment = postSnapshot.getValue(AppointmentFBItem.class);
                    if(appointment.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }

                if(mEditingAppointment){
                    saveAppointmentToAppointmentFB(mSaveItem);
                    saveExercisesToFirebase(mSaveItem,mExercises);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

        //get client appointment firebase helper
        ClientAppFirebaseHelper clientFB = ClientAppFirebaseHelper.getInstance();
        //delete client appointment from firebase
        clientFB.deleteAppointment(mUserId, clientKey, appDate, appTime, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClientAppFBItem appointment = postSnapshot.getValue(ClientAppFBItem.class);
                    if(appointment.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }
                if(mEditingAppointment){
                    saveAppointmentToClientAppFB(mSaveItem);
                }
                deleteAppointmentFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

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

        if(mEditingAppointment) {
            //editing, save new appointment used to replace old appointment
            saveAppointmentToDatabase(mSaveItem);
        }else{
            //load appointments to update recycler view
            loadAppointment();
        }
    }

/**************************************************************************************************/


}
