package me.makeachoice.gymratpta.view.dialog;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.AppointmentFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.ClientAppFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ClientLoader;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentFBItem;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_ACTIVE;

/**************************************************************************************************/
/*
 * ScheduleAppointmentDialog is used for scheduling workout session with client
 */
/**************************************************************************************************/

public class ScheduleAppointmentDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number from firebase authentication
    private String mUserId;

    //mSelectedIndex - client spinner index used if editing a previous client appointment item
    private int mSelectedIndex;

    //mClientApmtItem - client appointment item
    private AppointmentItem mClientApmtItem;

    //mSaveApmtItem - client appointment item to save
    private AppointmentItem mSaveApmtItem;

    //mClientList - list of client items
    private ArrayList<ClientItem> mClientList;

    //mClientNames - list of client names
    private ArrayList<String> mClientNames;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    private TextView mTxtTimeSelected;

    private TimePickerDialog.OnTimeSetListener mTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    onTimePickerSet(selectedHour, selectedMinute);
                }
            };


    private boolean mIsNewAppointment;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/
    /*
     * ScheduleAppointmentDialog - constructor
     */
    public ScheduleAppointmentDialog(){}

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 *      void setDialogValues(MyActivity,String,RoutineItem) - set dialog values
 */
/**************************************************************************************************/
    /*
     * void setDialogValues(MyActivity,String) - set dialog values
     */
    public void setDialogValues(MyActivity activity, String userId, AppointmentItem item){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;


        mClientList = new ArrayList<>();

        mClientNames = new ArrayList<>();

        initializeSaveItem(item);

        initializeClientAppointmentItem(item);

        if(item == null){
            mIsNewAppointment = true;

            //set spinner selected index
            mSelectedIndex = 0;
        }
        else{
            mIsNewAppointment = false;
        }
    }

    private void initializeClientAppointmentItem(AppointmentItem item){
        if(item == null){
            //is null, create client appointment
            mClientApmtItem = new AppointmentItem();

            mClientApmtItem.uid = mUserId;
            mClientApmtItem.fkey = "";
            mClientApmtItem.appointmentDay = DateTimeHelper.getToday();
            mClientApmtItem.appointmentTime = "";
            mClientApmtItem.clientKey = "";
            mClientApmtItem.clientName = "";
            mClientApmtItem.status = "";
        }
        else{
            mClientApmtItem = item;
        }

    }

    private void initializeSaveItem(AppointmentItem item){
        mSaveApmtItem = new AppointmentItem();
        if(item == null){
            mSaveApmtItem.uid = mUserId;
            mSaveApmtItem.fkey = "";
            mSaveApmtItem.appointmentDay = DateTimeHelper.getToday();
            mSaveApmtItem.appointmentTime = "";
            mSaveApmtItem.clientKey = "";
            mSaveApmtItem.clientName = "";
            mSaveApmtItem.status = "";
        }
        else{
            mSaveApmtItem.uid = mUserId;
            mSaveApmtItem.fkey = item.fkey;
            mSaveApmtItem.appointmentDay = item.appointmentDay;
            mSaveApmtItem.appointmentTime = item.appointmentTime;
            mSaveApmtItem.clientKey = item.clientKey;
            mSaveApmtItem.clientName = item.clientName;
            mSaveApmtItem.status = item.status;
        }

    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Initialize Dialog
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeDialog() - initialize dialog component
 *      void initializeDateTextView(View) - initialize textView component for dialog
 *      void initializeTimeTextView() - initialize time textView component
 *      void initializeSpinner() - initialize client spinner component
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //layout resource id number
        int layoutId = R.layout.dia_schedule_appointment;

        //inflate root view, parent child components
        mRootView = inflater.inflate(layoutId, container, false);

        //clear client items list
        mClientList.clear();

        //clear client names list
        mClientNames.clear();

        //load clients used by spinner
        loadClients();

        return mRootView;
    }

    /*
     * void initializeDialog() - initialize dialog component
     */
    private void initializeDialog(){
        //initialize date textView components
        initializeDateTextView();

        //initialize time textView component
        initializeTimeTextView();

        //initialize save textView component
        initializeSaveTextView();

        //initialize spinner component
        initializeSpinner();
    }

    /*
     * void initializeDateTextView(View) - initialize date textView component
     */
    private void initializeDateTextView(){
        //get string "today"
        String strToday = mActivity.getString(R.string.today);

        //get date textView component
        TextView txtDate = (TextView)mRootView.findViewById(R.id.diaSchedule_txtDate);

        //create date string to display
        strToday = strToday + ", " + DateTimeHelper.getToday();

        //set string value
        txtDate.setText(strToday);
    }

    /*
     * void initializeTimeTextView() - initialize time textView component
     */
    private void initializeTimeTextView(){
        //get time textView component
        mTxtTimeSelected = (TextView)mRootView.findViewById(R.id.diaSchedule_txtTimeSelect);

        //get appointment time selected string value
        String strTime = getTimeSelected();

        //set text to current time
        mTxtTimeSelected.setText(strTime);

        //set onClick listener
        mTxtTimeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current hour
                int hour = DateTimeHelper.getCurrentHour();

                //get current minute
                int minute = DateTimeHelper.getCurrentMinute();

                //open time picker with current hour and minute
                new TimePickerDialog(mActivity, mTimePickerListener, hour, minute,false).show();
            }
        });
    }

    private void initializeSaveTextView(){
        TextView txtSave = (TextView)mRootView.findViewById(R.id.diaSchedule_txtSave);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveRequested();
            }
        });
    }

    /*
     * void initializeSpinner() - initialize client spinner component
     */
    private void initializeSpinner(){

        //get spinner component
        Spinner spnClient = (Spinner)mRootView.findViewById(R.id.diaSchedule_spnClient);

        //set onItemSelected listener
        spnClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //notify client was selected
                onClientSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //does nothing
            }
        });

        //create client adapter with client names
        ArrayAdapter<String> adpClients = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, mClientNames);

        //set drop down layout
        adpClients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set spinner with adapter
        spnClient.setAdapter(adpClients);

        //set client selection
        spnClient.setSelection(mSelectedIndex);
    }


    private String getTimeSelected(){
        if(mClientApmtItem != null){
            return mClientApmtItem.appointmentTime;
        }
        else{
            return DateTimeHelper.getCurrentTime();
        }

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Database Methods
 *      void loadClients() - load active clients from database
 *      void onClientDataLoaded(Cursor) - active client data from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadClients() - load active clients from database
     */
    private void loadClients(){
        //load client data with only clients with an Active status
        ClientLoader.loadClientsByStatus(mActivity, mUserId, CLIENT_ACTIVE, new ClientLoader.OnClientLoadListener() {
            @Override
            public void onClientLoadFinished(Cursor cursor) {
                //client data has been loaded
                onClientDataLoaded(cursor);
            }
        });

    }

    /*
     * void onClientDataLoaded(Cursor) - active client data from database has been loaded
     */
    private void onClientDataLoaded(Cursor cursor){
        //get client name from client appointment item
        String selectedClient = mClientApmtItem.clientName;

        //get size of cursor
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //get clientItem from cursor
            ClientItem item = new ClientItem(cursor);

            //add item to client item list
            mClientList.add(item);

            //get client name from item
            String client = item.clientName;

            //add client name to client name list
            mClientNames.add(client);

            //check if client name is equal to client appointment item name
            if(selectedClient.equals(client)){
                //match, set spinner selected index
                mSelectedIndex = i;
            }

        }

        //initialize dialog
        initializeDialog();

        ClientLoader.destroyLoader(mActivity);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 */
/**************************************************************************************************/

    private void onClientSelected(int index){
        Log.d("Choice", "SessionDialog.onClientSelected: " + index);
        ClientItem item = mClientList.get(index);

        mSaveApmtItem.clientKey = item.fkey;
        mSaveApmtItem.clientName = item.clientName;
    }

    private int mHour;
    private int mMinute;
    private void onTimePickerSet(int selectedHour, int selectedMinute){
        Log.d("Choice", "SessionDialog.onTimePickerSet");
        Log.d("Choice", "     hour: " + selectedHour);
        mHour = selectedHour;
        mMinute = selectedMinute;

        String strTime = DateTimeHelper.convert24Hour(selectedHour, selectedMinute);

        // set current time into textView
        mTxtTimeSelected.setText(strTime);

        mSaveApmtItem.appointmentTime = strTime;

    }

    private void onSaveRequested(){
        saveAppointment(mSaveApmtItem);
        if(validTime()){
            //save to firebase
        }
        else{
            //show time warning dialog
        }

    }

    private boolean validTime(){
        Toast.makeText(mActivity, "Session Time has passed.", Toast.LENGTH_LONG).show();
        return true;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Methods
 */
/**************************************************************************************************/

    /*
     * void saveAppointment(AppointmentItem) - save appointment to firebase and local database
     */
    private void saveAppointment(AppointmentItem item){
        //save to firebase
        saveAppointmentToFirebase(item);

        //save to local database
        saveAppointmentToDatabase(item);

    }

    /*
     * void saveAppointmentToFirebase(AppointmentItem) - save appointment to firebase
     */
    private void saveAppointmentToFirebase(AppointmentItem saveItem){
        //get client appointment firebase helper instance
        AppointmentFirebaseHelper appointmentFB = AppointmentFirebaseHelper.getInstance();
        ClientAppFirebaseHelper clientFB = ClientAppFirebaseHelper.getInstance();

        //get client key from old appointment item
        String oldKey = mClientApmtItem.clientKey;

        //check if appointment is NOT new and client key is NOT the same
        /*if(!mIsNewAppointment && !oldKey.equals(saveItem.clientKey)){
            //routine name has been edited, remove old routine name from firebase
            appointmentFB.deleteRoutineName(mUserId, oldName);

            //remove routine from routine firebase
            routineFB.deleteRoutine(mUserId, oldName);
        }*/

        AppointmentFBItem appointmentFBItem = new AppointmentFBItem();
        appointmentFBItem.appointmentTime = saveItem.appointmentTime;
        appointmentFBItem.clientKey = saveItem.clientKey;
        appointmentFBItem.clientName = saveItem.clientName;
        appointmentFBItem.status = saveItem.status;

        //save appointment
        appointmentFB.addAppointmentByDay(mUserId, saveItem.appointmentDay, appointmentFBItem);

        ClientAppFBItem clientFBItem = new ClientAppFBItem();
        clientFBItem.appointmentDate = saveItem.appointmentDay;
        clientFBItem.appointmentTime = saveItem.appointmentTime;
        clientFBItem.clientName = saveItem.clientName;
        clientFBItem.status = saveItem.status;

        clientFB.addClientAppByClientKey(mUserId, saveItem.clientKey, clientFBItem);
    }

    private void saveAppointmentToDatabase(AppointmentItem saveItem){
        //get uri value for routine name table
        Uri uriValue = Contractor.AppointmentEntry.CONTENT_URI;

        //get name from routine detail item
        //String oldName = mRoutineDetailItem.routineName;

        //check if routine detail is NOT new
        /*if(!mIsNewRoutine){
            //routine is being edited, check if routine name is NOT the same
            if(!oldName.equals(nameItem.routineName)){
                //routine name has been edited, remove old routine name from database
                int rowDeleted = mActivity.getContentResolver().delete(uriValue,
                        RoutineNameQueryHelper.routineNameSelection, new String[]{mUserId, oldName});

                //add new routine name to database
                mActivity.getContentResolver().insert(uriValue, nameItem.getContentValues());
            }

        }
        else{
            //routine is new, add routine name to database
            mActivity.getContentResolver().insert(uriValue, nameItem.getContentValues());
        }*/

        //appointment is new, add appointment to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());
    }

/**************************************************************************************************/



}