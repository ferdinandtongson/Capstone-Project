package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.AppointmentFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientAppFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.AppointmentLoader;
import me.makeachoice.gymratpta.controller.modelside.query.AppointmentQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.ClientAppAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentFBItem;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppCardItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.view.dialog.ClientAppointmentDialog;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
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
    private ArrayList<AppointmentItem> mAppointments;

    private boolean mEditingAppointment;
    private AppointmentItem mDeleteItem;
    private AppointmentItem mSaveItem;
    private boolean mRefreshOnDismiss;

    private AppointmentLoader mAppointmentLoader;
    private ClientAppointmentDialog mAppDialog;
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
        mData = new ArrayList<>();
        mEditingAppointment = false;

        mAppointmentLoader = new AppointmentLoader(mActivity, mUserId);
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
     * ClientAppointmentDialog initializeAppointmentDialog - initialize appointment dialog
     */
    private ClientAppointmentDialog initializeAppointmentDialog(AppointmentItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mAppDialog = new ClientAppointmentDialog();
        mAppDialog.setDialogValues(mActivity, mUserId, mClientItem, item);
        mAppDialog.setOnSavedListener(new ClientAppointmentDialog.OnSaveClickListener() {
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
        Log.d("Choice", "ClientScheduleMaid.loadAppointment");

        //start loader to get appointment data from database
        mAppointmentLoader.loadAppointmentByClientKey(mClientItem.fkey,
                new AppointmentLoader.OnAppointmentLoadListener() {
            @Override
            public void onAppointmentLoadFinished(Cursor cursor){
                onAppointmentsLoaded(cursor);
            }
        });
    }

    /*
     * void onAppointmentsLoaded(Cursor) - appointments from database has been loaded
     */
    private void onAppointmentsLoaded(Cursor cursor){
        Log.d("Choice", "     cursor: " + cursor.getCount());
        //clear appointment list array
        mAppointments.clear();

        //clear data list consumed by recycler
        mData.clear();

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

            mData.add(createClientAppCardItem(item));
        }

        //destroy loader
        mAppointmentLoader.destroyLoader();
        prepareFragment();
    }

    /*
     * ClientAppCardItem createClientAppCardItem(...) - create appointmentCard item consumed by adapter
     */
    private ClientAppCardItem createClientAppCardItem(AppointmentItem appItem){
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
        initializeAppointmentDialog(null);
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
    private void onSaveAppointment(AppointmentItem appointmentItem){
        Log.d("Choice", "ClientScheduleMaid.onSaveAppointment");
        //get save appointment item
        mSaveItem = appointmentItem;

        //dismiss schedule appointment dialog
        mAppDialog.dismiss();

        //check if editing an old appointment
        if(mEditingAppointment){
            Log.d("Choice", "     editing");
            //yes, delete old appointment (new appointment will be saved after deletion)
            deleteAppointment(mDeleteItem);
        }
        else{
            Log.d("Choice", "     new appointment");
            //no, save new appointment
            saveAppointment(mSaveItem);
        }
    }

    /*
     * void onAppointmentDeleteRequest(int) - delete appointment requested
     */
    private void onAppointmentDeleteRequest(int index){
        //set editing old appointment flag, false
        mEditingAppointment = false;

        //get appointment item to be deleted
        mDeleteItem = mAppointments.get(index);

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        boolean showWarning = prefs.getBoolean(PREF_DELETE_WARNING_APPOINTMENT, true);

        //check status
        if(showWarning){
            //wants warning, show "delete warning" dialog
            initializeWarningDialog(mDeleteItem, mClientItem);
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
 *      void saveAppointmentToAppointmentFB(AppointmentItem) - save appointment to appointment firebase
 *      void saveAppointmentToClientAppFB(AppointmentItem) - save appointment to client appointment firebase
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
