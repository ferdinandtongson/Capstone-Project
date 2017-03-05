package me.makeachoice.gymratpta.view.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineNameButler;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE_NAME;

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

    private ScheduleItem mSaveItem;

    //mRoutineNames - list of routine names
    private ArrayList<String> mRoutineNames;
    private HashMap<String,String> mScheduleMap;
    private HashMap<String,String> mEditScheduleMap;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    private TextView mTxtDateSelected;
    private TextView mTxtDate;
    private TextView mTxtTime;
    private TextView mTxtSave;

    private RoutineNameButler mRoutineButler;

    //string values - used by session time labels
    private String mStrSessionTime;
    private String mStrSessionDate;

    //string value - used by client label
    private String mMsgAlreadyBooked;
    private String mMsgSameTime;
    private String mMsgAlreadyPast;

    //string value - used by save button
    private String mStrSave;
    private String mStrSaveAnyway;

    private int mTxtOrange;
    private int mTxtBlack;

    //valid status flags
    private boolean mEditMode;

    private String mSelectedDate;
    private String mSelectedTime;


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
        void onSaveClicked(ScheduleItem appItem);
    }

    private OnDismissListener mDismissListener;
    public interface OnDismissListener{
        void onDismiss(DialogInterface dialogInterface);
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
    public void setDialogValues(MyActivity activity, String userId, ClientItem clientItem,
                                ScheduleItem item, HashMap<String,String> scheduleMap){
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
        mScheduleMap = new HashMap<>();
        mEditScheduleMap = new HashMap<>();


        mScheduleMap.putAll(scheduleMap);

        mEditMode = false;

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
    private void initializeSaveItem(ScheduleItem item){
        //create appointment item to be saved
        mSaveItem = new ScheduleItem();

        //check if item is null
        if(item == null){
            //if null, new appointment item being created
            mSaveItem.uid = mUserId;
            mSaveItem.datestamp = DateTimeHelper.getDatestamp(0);
            mSaveItem.appointmentDate = DateTimeHelper.getToday();
            mSaveItem.appointmentTime = DateTimeHelper.getCurrentTime();
            mSaveItem.clientKey = mClientItem.fkey;
            mSaveItem.clientName = mClientItem.clientName;
            mSaveItem.routineName = mNoneSelected;
            mSaveItem.status = "";
        }
        else{
            //old appointment item being edited, save values to save item
            mSaveItem.uid = mUserId;
            mSaveItem.datestamp = item.datestamp;
            mSaveItem.appointmentDate = item.appointmentDate;
            mSaveItem.appointmentTime = item.appointmentTime;
            mSaveItem.clientKey = item.clientKey;
            mSaveItem.clientName = item.clientName;
            mSaveItem.routineName = item.routineName;
            mSaveItem.status = item.status;
        }

        mSelectedDate = mSaveItem.appointmentDate;
        mSelectedTime = mSaveItem.appointmentTime;

    }

    /*
     * void setOnSavedListener(...) - set listener for saved click event
     */
    public void setOnSavedListener(OnSaveClickListener listener){
        mSavedListener = listener;
    }

    /*
     * void setOnDismissListener(...) - set listener for dialog dismiss events
     */
    public void setOnDismissListener(OnDismissListener listener){
        mDismissListener = listener;
    }

    public void setEditMode(boolean isEditMode){
        mEditMode = isEditMode;

        if(mEditMode){
            mEditScheduleMap.clear();

            String timestamp = DateTimeHelper.getTimestamp(mSaveItem.appointmentDate,
                    mSaveItem.appointmentTime);
            mEditScheduleMap.putAll(mScheduleMap);
            mEditScheduleMap.remove(timestamp);
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

        mStrSessionTime = mActivity.getString(R.string.session_time);
        mStrSessionDate = mActivity.getString(R.string.session_date);
        mMsgAlreadyPast = mActivity.getString(R.string.msg_already_past);

        mMsgAlreadyBooked = mActivity.getString(R.string.msg_already_booked);
        mMsgSameTime = mActivity.getString(R.string.msg_booked_that_time);

        mStrSave = mActivity.getString(R.string.save);
        mStrSaveAnyway = mActivity.getString(R.string.save_anyway);

        mTxtOrange = DeprecatedUtility.getColor(mActivity, R.color.orange);
        mTxtBlack = DeprecatedUtility.getColor(mActivity, R.color.black);

        //clear routine names list
        mRoutineNames.clear();
        mRoutineNames.add(mNoneSelected);

        mRoutineButler = new RoutineNameButler(mActivity, mUserId);
        mRoutineButler.loadRoutineNames(LOADER_ROUTINE_NAME, new RoutineNameButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<RoutineNameItem> routineList) {
                onRoutineNamesLoaded(routineList);
            }
        });

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
        mTxtDate = (TextView)mRootView.findViewById(R.id.diaClientApp_txtDate);
        mTxtDate.setText(mStrSessionDate);

        //set string value
        mTxtDateSelected.setText(mSelectedDate);

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
        mTxtTime = (TextView)mRootView.findViewById(R.id.diaClientApp_txtTime);
        mTxtTime.setText(mStrSessionTime);

        //set text to current time
        mTxtTimeSelected.setText(DateTimeHelper.convert24Hour(mSelectedTime));

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
        mTxtSave = (TextView)mRootView.findViewById(R.id.diaClientApp_txtSave);

        //set onClick listener
        mTxtSave.setOnClickListener(new View.OnClickListener() {
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
 *      void loadRoutineNames() - load routine names from database
 *      void onRoutineNameDataLoaded(Cursor) - routine name data from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void onRoutineNameDataLoaded(Cursor) - routine name data from database has been loaded
     */
    private void onRoutineNamesLoaded(ArrayList<RoutineNameItem> routineList){
        //get routine name from client appointment item
        String selectedRoutine = mSaveItem.routineName;

        //get size of cursor
        int count = routineList.size();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //get routineItem from cursor
            RoutineNameItem item = routineList.get(i);

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

        //initialize dialog
        initializeDialog();

    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void onTimePickerSet(int,int) - appointment time has been selected
 *      void onDatePickerSet() - session date has been selected
 *      void onTimePickerSet(int,int) - session time has been selected
 */
/**************************************************************************************************/
    private void onRoutineSelected(int index){
        //save routine data to save appointment item
        mSaveItem.routineName = mRoutineNames.get(index);
    }

    /*
     * void onDatePickerSet() - session date has been selected
     */
    private void onDatePickerSet(int year, int month, int dayOfMonth){
        //get selected date
        String selectedDate = DateTimeHelper.getDate(year, month, dayOfMonth);

        //update display date
        mTxtDateSelected.setText(selectedDate);

        //save selected date to buffer
        mSelectedDate = selectedDate;

        //check if valid booking
        checkBooking(mSelectedDate, mSelectedTime);
    }

    /*
     * void onTimePickerSet(int,int) - session time has been selected
     */
    private void onTimePickerSet(int selectedHour, int selectedMinute){
        //convert time to string value (24hour)
        String strTime = DateTimeHelper.getTime(selectedHour, selectedMinute);

        // set current time into textView (AM/PM)
        mTxtTimeSelected.setText(DateTimeHelper.convert24Hour(strTime));

        //save 24hour selected time
        mSelectedTime = strTime;

        //check if valid booking
        checkBooking(mSelectedDate, mSelectedTime);
    }

    /*
     * void onSaveRequested() - save appointment has been requested
     */
    private void onSaveRequested(){
        //check if save listener is Not null
        if(mSavedListener != null){
            mSaveItem.datestamp = DateTimeHelper.convertDateToDatestamp(mSelectedDate);
            mSaveItem.appointmentDate = mSelectedDate;
            mSaveItem.appointmentTime = mSelectedTime;
            //notify save listener
            mSavedListener.onSaveClicked(mSaveItem);
        }
    }

    /*
     * void onDismiss(DialogInterface) - dialog dismiss event occurred
     */
    @Override
    public void onDismiss(DialogInterface dialogInterface){
        //check for listener
        if(mDismissListener != null){
            //notify listener for dismiss event
            mDismissListener.onDismiss(dialogInterface);
        }
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Validation Methods
 *      void checkBoooking() - check if booking is valid or not
 *      boolean validateTime(String) - check if time has already past
 *      void setDoubleBooked() - session date and time has already been scheduled
 *      void setAlreadyBookedDate() - check if client has already been booked that day
 *      boolean isToday() - check if selected date is currently today
 *      HashMap<String,String> getScheduleMap(...) - get map used for validating date and time
 *      void setDateText(...) - set date text and color
 *      void setTimeText(...) - set time text and color
 *      void setSaveText(...) - set visibility of save text
 */
/**************************************************************************************************/
    /*
     * void checkBoooking() - check if booking is valid or not
     */
    private void checkBooking(String selectedDate, String selectedTime){
        //create past date message
        String msgPastDate = mStrSessionDate + ": " + mMsgAlreadyPast + "!";

        //get timestamp and datestamp
        String timestamp = DateTimeHelper.getTimestamp(selectedDate, selectedTime);
        String datestamp = DateTimeHelper.convertDateToDatestamp(selectedDate);

        //get schedule map
        HashMap<String,String> validationMap = getScheduleMap(selectedDate, selectedTime);

        //check if today
        boolean isToday = isToday(selectedDate);

        //check if past date, (NOTE - today is consider past)
        boolean dateHasPast = DateTimeHelper.hasDatePast(datestamp);

        //check if already booked
        boolean alreadyBooked = validationMap.containsValue(selectedDate);

        //check if double booked, (NOTE - date/time could have already past)
        boolean doubleBooked = validationMap.containsKey(timestamp);

        //check if today
        if(isToday){
            //session date is today, check double booking
            if(doubleBooked){
                //date and time already booked, date and time invalid
                setDoubleBooked();
            }
            else{
                //is today, check if already booked
                setAlreadyBookedDate(alreadyBooked);

                //check time has not past
                validateTime(timestamp);
            }


        }
        else if(dateHasPast){
            //date is in the past
            setDateText(msgPastDate, mTxtOrange);
            setTimeText(mStrSessionTime, mTxtBlack);
            setSaveText(View.INVISIBLE);
        }
        else{
            //date is in the future, check double booking
            if(doubleBooked){
                //date and time already booked, date and time invalid
                setDoubleBooked();
            }
            else{
                //check if already booked for that date
                setAlreadyBookedDate(alreadyBooked);
                setTimeText(mStrSessionTime, mTxtBlack);
                setSaveText(View.VISIBLE);
            }
        }

    }

    /*
     * boolean validateTime(String) - check if time has already past
     */
    private boolean validateTime(String timestamp){
        String msgPastTime = mStrSessionTime + ": " + mMsgAlreadyPast + "!";

        //check if time has already past, date has already been validated
        boolean invalid = DateTimeHelper.hasAppointmentTimePassed(timestamp);
        if(invalid){
            //time has past
            setTimeText(msgPastTime, mTxtOrange);
            setSaveText(View.INVISIBLE);
            return false;
        }
        else{
            //time is valid
            setTimeText(mStrSessionTime, mTxtBlack);
            setSaveText(View.VISIBLE);
            return true;
        }
    }

    /*
     * void setDoubleBooked() - session date and time has already been scheduled
     */
    private void setDoubleBooked(){
        //set warning messages
        String msgDateBooked = mStrSessionDate + ": " + mMsgAlreadyBooked;
        String msgSameTime = mStrSessionTime + ": " + mMsgSameTime + "!";

        //display messages
        setDateText(msgDateBooked, mTxtOrange);
        setTimeText(msgSameTime, mTxtOrange);
        setSaveText(View.INVISIBLE);

    }

    /*
     * void setAlreadyBookedDate() - check if client has already been booked that day
     */
    private void setAlreadyBookedDate(boolean alreadyBooked){
        //set warning message
        String msgDateBooked = mStrSessionDate + ": " + mMsgAlreadyBooked;

        //check already booked
        if(alreadyBooked){
            //already booked, give warning (user will have option to save regardless)
            setDateText(msgDateBooked, mTxtOrange);
            mTxtSave.setText(mStrSaveAnyway);
        }
        else{
            //not already booked for that day
            setDateText(mStrSessionDate, mTxtBlack);
            mTxtSave.setText(mStrSave);
        }

    }

    /*
     * boolean isToday() - check if selected date is currently today
     */
    private boolean isToday(String selectedDate){
        String today = DateTimeHelper.getToday();

        return today.equals(selectedDate);
    }

    /*
     * HashMap<String,String> getScheduleMap(...) - get map used for validating date and time
     */
    private HashMap<String,String> getScheduleMap(String selectedDate, String selectedTime){
        if(mEditMode){
            return mEditScheduleMap;
        }
        else{
            return mScheduleMap;
        }
    }

    /*
     * void setDateText(...) - set date text and color
     */
    private void setDateText(String msg, int color){
        mTxtDate.setTextColor(color);
        mTxtDate.setText(msg);
    }

    /*
     * void setTimeText(...) - set time text and color
     */
    private void setTimeText(String msg, int color){
        mTxtTime.setTextColor(color);
        mTxtTime.setText(msg);
    }

    /*
     * void setSaveText(...) - set visibility of save text
     */
    private void setSaveText(int visibility){
        mTxtSave.setVisibility(visibility);
    }


/**************************************************************************************************/

}