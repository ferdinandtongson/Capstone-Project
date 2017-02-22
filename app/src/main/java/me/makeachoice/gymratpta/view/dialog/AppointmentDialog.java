package me.makeachoice.gymratpta.view.dialog;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import me.makeachoice.gymratpta.controller.modelside.loader.ClientLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineNameLoader;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_ACTIVE;

/**************************************************************************************************/
/*
 * AppointmentDialog is used for scheduling workout session with client
 */
/**************************************************************************************************/

public class AppointmentDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number from firebase authentication
    private String mUserId;

    //mNoneSelected - string value used when no routine is selected
    private String mNoneSelected;

    //mClientIndex - client spinner index used if editing a previous client appointment item
    private int mClientIndex;

    //mRoutineIndex - routine spinner index used if editing a previous client appointment item
    private int mRoutineIndex;

    //mSaveApmtItem - client appointment item to save
    private AppointmentItem mSaveApmtItem;

    //mRoutineItem - routine item selected for appointment
    private ArrayList<RoutineItem> mExercises;

    //mClientList - list of client items
    private ArrayList<ClientItem> mClientList;

    //mClientNames - list of client names
    private ArrayList<String> mClientNames;

    //mRoutineNames - list of routine names
    private ArrayList<String> mRoutineNames;

    private ClientLoader mClientLoader;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    private Spinner mSpnRoutine;

    private TextView mTxtTimeSelected;

    private TimePickerDialog.OnTimeSetListener mTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    onTimePickerSet(selectedHour, selectedMinute);
                }
            };


    //mSavedListener - notifies listener that the saved button was clicked
    private OnSaveClickListener mSavedListener;
    public interface OnSaveClickListener{
        public void onSaveClicked(AppointmentItem appItem, ArrayList<RoutineItem> exercises);
    }



/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/
    /*
     * AppointmentDialog - constructor
     */
    public AppointmentDialog(){}

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 *      void setDialogValues(MyActivity,String,RoutineItem) - set dialog values
 *      void initializeEditItem(...) - initialize client appointment item being edited
 *      void initializeSaveItem(...) - initialize appointment item to be saved
 *      void setOnSavedListener(...) - set listener for saved click event
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

        //set string value
        mNoneSelected = mActivity.getString(R.string.dialogSchedule_no_routine_selected);

        //initialize client list buffer
        mClientList = new ArrayList<>();

        //initialize client name list buffer
        mClientNames = new ArrayList<>();

        //initialize routine name list buffer
        mRoutineNames = new ArrayList<>();

        //initialize exercises in routine
        mExercises = new ArrayList<>();

        mClientLoader = new ClientLoader(mActivity, mUserId);

        //initialize appointment item to save
        initializeSaveItem(item);

        if(item == null){
            //set client spinner selected index
            mClientIndex = 0;

            //set routine spinner selected index
            mRoutineIndex = 0;
        }
    }

    /*
     * void initializeSaveItem(...) - initialize appointment item to be saved
     */
    private void initializeSaveItem(AppointmentItem item){
        //create appointment item to be saved
        mSaveApmtItem = new AppointmentItem();

        //check if item is null
        if(item == null){
            //if null, new appointment item being created
            mSaveApmtItem.uid = mUserId;
            mSaveApmtItem.fkey = "";
            mSaveApmtItem.appointmentDate = DateTimeHelper.getToday();
            mSaveApmtItem.appointmentTime = DateTimeHelper.getCurrentTime();
            mSaveApmtItem.clientKey = "";
            mSaveApmtItem.clientName = "";
            mSaveApmtItem.routineName = mNoneSelected;
            mSaveApmtItem.status = "";
        }
        else{
            //old appointment item being edited, save values to save item
            mSaveApmtItem.uid = mUserId;
            mSaveApmtItem.fkey = item.fkey;
            mSaveApmtItem.appointmentDate = item.appointmentDate;
            mSaveApmtItem.appointmentTime = item.appointmentTime;
            mSaveApmtItem.clientKey = item.clientKey;
            mSaveApmtItem.clientName = item.clientName;
            mSaveApmtItem.routineName = item.routineName;
            mSaveApmtItem.status = item.status;

            loadRoutine(item.routineName);
        }
    }

    /*
     * void setOnSavedListener(...) - set listener for saved click event
     */
    public void setOnSavedListener(OnSaveClickListener listener){
        mSavedListener = listener;
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Initialize Dialog
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeDialog() - initialize dialog component
 *      void initializeDateTextView(View) - initialize textView component for dialog
 *      void initializeTimeTextView() - initialize time textView component
 *      void initializeSaveTextView() - initialize save textView component
 *      void initializeClientSpinner() - initialize client spinner component
 *      void initializeRoutineSpinner() - initialize routine spinner component
 *      String getTimeSelected() - get appointment time selected
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

        //clear routine names list
        mRoutineNames.clear();
        mRoutineNames.add(mNoneSelected);

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

        //initialize client spinner component
        initializeClientSpinner();

        //initialize routine spinner component
        initializeRoutineSpinner();
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
        String strTime = mSaveApmtItem.appointmentTime;

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

    /*
     * void initializeSaveTextView() - initialize save textView component
     */
    private void initializeSaveTextView(){
        //get save textView component
        TextView txtSave = (TextView)mRootView.findViewById(R.id.diaSchedule_txtSave);

        //set onClick listener
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveRequested();
            }
        });
    }

    /*
     * void initializeClientSpinner() - initialize client spinner component
     */
    private void initializeClientSpinner(){

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
        spnClient.setSelection(mClientIndex);
    }

    /*
     * void initializeRoutineSpinner() - initialize routine spinner component
     */
    private void initializeRoutineSpinner(){

        //get spinner component
        mSpnRoutine = (Spinner)mRootView.findViewById(R.id.diaSchedule_spnRoutine);

        //set onItemSelected listener
        mSpnRoutine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //notify routine was selected
                onRoutineSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //does nothing
            }
        });

        //create routine adapter with routine names
        ArrayAdapter<String> adpRoutine = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, mRoutineNames);

        //set drop down layout
        adpRoutine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set spinner with adapter
        mSpnRoutine.setAdapter(adpRoutine);

        //set client selection
        mSpnRoutine.setSelection(mRoutineIndex);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Database Methods
 *      void loadClients() - load active clients from database
 *      void onClientDataLoaded(Cursor) - active client data from database has been loaded
 *      void loadRoutinesNames() - load routines names from database
 *      void onRoutineNameDataLoaded(Cursor) - routine names from database has been loaded
 *      void loadRoutine() - load routine exercises from database
 *      void onRoutineLoaded - routine exercise data from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadClients() - load active clients from database
     */
    private void loadClients(){
        //load client data with only clients with an Active status
        mClientLoader.loadClientsByStatus(CLIENT_ACTIVE, new ClientLoader.OnClientLoadListener() {
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
        String selectedClient = mSaveApmtItem.clientName;

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
                mClientIndex = i;
            }

        }

        mClientLoader.destroyLoader();

        loadRoutinesNames();
    }

    /*
     * void loadRoutinesNames() - load routines names from database
     */
    private void loadRoutinesNames(){
        //load client data with only clients with an Active status
        RoutineNameLoader.loadRoutineNames(mActivity, mUserId, new RoutineNameLoader.OnRoutineNameLoadListener() {
            @Override
            public void onRoutineNameLoadFinished(Cursor cursor) {
                onRoutineNameDataLoaded(cursor);
            }
        });

    }

    /*
     * void onRoutineNameDataLoaded(Cursor) - routine names from database has been loaded
     */
    private void onRoutineNameDataLoaded(Cursor cursor){
        //get routine name from client appointment item
        String selectedRoutine = mSaveApmtItem.routineName;

        //get size of cursor
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //get routineItem from cursor
            RoutineNameItem item = new RoutineNameItem(cursor);

            //get routine name from item
            String routine = item.routineName;

            //add routine name to routine name list
            mRoutineNames.add(routine);

            //check if client name is equal to client appointment item name
            if(selectedRoutine.equals(routine)){
                //match, set spinner selected index, need to add 1 because of "- None Selected -"
                mRoutineIndex = i + 1;
            }
        }

        //destroy loader
        RoutineNameLoader.destroyLoader(mActivity);

        //initialize dialog
        initializeDialog();

    }

    /*
     * void loadRoutine() - load routine exercises from database
     */
    private void loadRoutine(String routineName){
        //load exercise routine by routine name
        RoutineLoader.loadRoutineByName(mActivity, mUserId, routineName, new RoutineLoader.OnRoutineLoadListener() {
            @Override
            public void onRoutineLoadFinished(Cursor cursor) {
                onRoutineLoaded(cursor);
            }
        });
    }

    /*
     * void onRoutineLoaded - routine exercise data from database has been loaded
     */
    private void onRoutineLoaded(Cursor cursor){

        //check that cursor is Not null or empty
        if(cursor != null && cursor.getCount() > 0){
            //get number of items in cursor
            int count = cursor.getCount();

            //loop through cursor
            for(int i = 0; i < count; i++){
                //move cursor to index position
                cursor.moveToPosition(i);

                //create routine item from cursor data
                RoutineItem item = new RoutineItem(cursor);

                //add routine exercise into exercise list
                mExercises.add(item);
            }
        }

        //destroy loader
        RoutineLoader.destroyLoader(mActivity);

        //check if routine spinner has been created
        if(mSpnRoutine != null){
            //enable routine spinner
            mSpnRoutine.setEnabled(true);
        }

    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void onClientSelected(int) - client was selected from spinner
 *      void onTimePickerSet(int,int) - appointment time has been selected
 *      void onSaveRequested() - save appointment has been requested
 *      void hasPassed(String) - check if selected time has already passed
 */
/**************************************************************************************************/
    /*
     * void onClientSelected(int) - client was selected from spinner
     */
    private void onClientSelected(int index){
        //get client from list
        ClientItem clientItem = mClientList.get(index);

        //save client data to save appointment item
        mSaveApmtItem.clientKey = clientItem.fkey;
        mSaveApmtItem.clientName = clientItem.clientName;
    }

    private void onRoutineSelected(int index){
        //save routine data to save appointment item
        mSaveApmtItem.routineName = mRoutineNames.get(index);

        if(index != 0){
            mSpnRoutine.setEnabled(false);
            loadRoutine(mSaveApmtItem.routineName);
        }
        else{
            mExercises.clear();
        }
    }

    /*
     * void onTimePickerSet(int,int) - appointment time has been selected
     */
    private void onTimePickerSet(int selectedHour, int selectedMinute){

        //convert time to string value
        String strTime = DateTimeHelper.convert24Hour(selectedHour, selectedMinute);

        // set current time into textView
        mTxtTimeSelected.setText(strTime);

        //save time to save appointment item
        mSaveApmtItem.appointmentTime = strTime;

        //notify user if appointment time has passed
        hasPassed(strTime);
    }

    /*
     * void onSaveRequested() - save appointment has been requested
     */
    private void onSaveRequested(){
        //check if save listener is Not null
        if(mSavedListener != null){
            //notify save listener
            mSavedListener.onSaveClicked(mSaveApmtItem, mExercises);
        }
    }

    /*
     * void hasPassed(String) - check if selected time has already passed
     */
    private void hasPassed(String selectedTime){
        //create "time has passed" message
        String msg = mActivity.getString(R.string.msg_appointment_time_passed);

        //get current time
        String currentTime = DateTimeHelper.getCurrentTime();

        //compare current time with selected time
        if(DateTimeHelper.isTime1AfterTime2(currentTime, selectedTime) == 1){
            //selected time has already passed, notify user
            Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
        }

    }

/**************************************************************************************************/

}