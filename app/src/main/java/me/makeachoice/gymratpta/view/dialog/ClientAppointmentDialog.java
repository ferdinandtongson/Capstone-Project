package me.makeachoice.gymratpta.view.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineNameLoader;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * ClientAppointmentDialog is used for scheduling workout session with for a specific client
 */
/**************************************************************************************************/

public class ClientAppointmentDialog extends DialogFragment {

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

    //mRoutineIndex - routine spinner index used if editing a previous client appointment item
    private int mRoutineIndex;

    //mClientItem - client item object
    private ClientItem mClientItem;

    //mSaveApmtItem - client appointment item to save
    private AppointmentItem mSaveApmtItem;

    //mRoutineNames - list of routine names
    private ArrayList<String> mRoutineNames;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    private TextView mTxtDateSelected;

    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            onDatePickerSet(i, i1, i2);
        }
    };


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
        public void onSaveClicked(AppointmentItem appItem);
    }



/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/
    /*
     * ClientAppointmentDialog - constructor
     */
    public ClientAppointmentDialog(){}

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
    public void setDialogValues(MyActivity activity, String userId, ClientItem clientItem, AppointmentItem item){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;

        //set string value
        mNoneSelected = mActivity.getString(R.string.dialogSchedule_no_routine_selected);

        //set client item
        mClientItem = clientItem;

        //initialize routine name list buffer
        mRoutineNames = new ArrayList<>();

        //initialize appointment item to save
        initializeSaveItem(item);

        if(item == null){
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
            mSaveApmtItem.clientKey = mClientItem.fkey;
            mSaveApmtItem.clientName = mClientItem.clientName;
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
        int layoutId = R.layout.dia_client_appointment;

        //inflate root view, parent child components
        mRootView = inflater.inflate(layoutId, container, false);

        //clear routine names list
        mRoutineNames.clear();
        mRoutineNames.add(mNoneSelected);

        //load clients used by spinner
        loadRoutines();

        return mRootView;
    }

    /*
     * void initializeDialog() - initialize dialog component
     */
    private void initializeDialog(){
        initializeNameTextView();

        //initialize date textView components
        initializeDateTextView();

        //initialize time textView component
        initializeTimeTextView();

        //initialize save textView component
        initializeSaveTextView();

        //initialize routine spinner component
        initializeRoutineSpinner();
    }

    private void initializeNameTextView(){
        TextView txtName = (TextView)mRootView.findViewById(R.id.diaClientApp_txtName);

        txtName.setText(mClientItem.clientName);
    }

    /*
     * void initializeDateTextView(View) - initialize date textView component
     */
    private void initializeDateTextView(){
        //get date textView component
        mTxtDateSelected = (TextView)mRootView.findViewById(R.id.diaClientApp_txtDateSelect);

        //create date string to display
        String strToday = mSaveApmtItem.appointmentDate;

        //set string value
        mTxtDateSelected.setText(strToday);

        mTxtDateSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = DateTimeHelper.getCurrentYear();
                int month = DateTimeHelper.getCurrentMonth();
                int dayOfMonth = DateTimeHelper.getCurrentDayOfMonth();

                new DatePickerDialog(mActivity, mDatePickerListener, year, month, dayOfMonth).show();
            }
        });
    }

    /*
     * void initializeTimeTextView() - initialize time textView component
     */
    private void initializeTimeTextView(){
        //get time textView component
        mTxtTimeSelected = (TextView)mRootView.findViewById(R.id.diaClientApp_txtTimeSelect);

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
        TextView txtSave = (TextView)mRootView.findViewById(R.id.diaClientApp_txtSave);

        //set onClick listener
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveRequested();
            }
        });
    }

    /*
     * void initializeRoutineSpinner() - initialize routine spinner component
     */
    private void initializeRoutineSpinner(){

        //get spinner component
        Spinner spnRoutine = (Spinner)mRootView.findViewById(R.id.diaClientApp_spnRoutine);

        //set onItemSelected listener
        spnRoutine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spnRoutine.setAdapter(adpRoutine);

        //set client selection
        spnRoutine.setSelection(mRoutineIndex);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Database Methods
 *      void loadRoutines() - load routines from database
 *      void onRoutineNameDataLoaded(Cursor) - routine name data from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadRoutines() - load routines from database
     */
    private void loadRoutines(){
        //load client data with only clients with an Active status
        RoutineNameLoader.loadRoutineNames(mActivity, mUserId, new RoutineNameLoader.OnRoutineNameLoadListener() {
            @Override
            public void onRoutineNameLoadFinished(Cursor cursor) {
                onRoutineNameDataLoaded(cursor);
            }
        });

    }

    /*
     * void onRoutineNameDataLoaded(Cursor) - routine name data from database has been loaded
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

                //match, set spinner selected index, need to consider "- None Selected -"
                mRoutineIndex = i + 1;
            }
        }

        //destroy loader
        RoutineNameLoader.destroyLoader(mActivity);

        //initialize dialog
        initializeDialog();

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void onTimePickerSet(int,int) - appointment time has been selected
 *      void onSaveRequested() - save appointment has been requested
 *      void hasPassed(String) - check if selected time has already passed
 */
/**************************************************************************************************/
    private void onRoutineSelected(int index){
        //save routine data to save appointment item
        mSaveApmtItem.routineName = mRoutineNames.get(index);
    }

    private void onDatePickerSet(int year, int month, int dayOfMonth){
        String selectedDate = DateTimeHelper.getDate(year, month, dayOfMonth);

        mSaveApmtItem.appointmentDate = selectedDate;

        mTxtDateSelected.setText(selectedDate);
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
            mSavedListener.onSaveClicked(mSaveApmtItem);
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