package me.makeachoice.gymratpta.view.dialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineNameButler;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.string.routine;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE_NAME;

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

    //mNoneSelected - string value used when user will does Not select a routine
    private String mNoneSelected;

    //mClientIndex - client spinner index used if editing a previous client appointment item
    private int mClientIndex;

    //mRoutineIndex - routine spinner index used if editing a previous client appointment item
    private int mRoutineIndex;

    private ScheduleItem mSaveApmtItem;

    private HashMap<String,String> mAppointmentMap;
    private HashMap<String,String> mEditAppointmentMap;

    //mClientList - list of client items
    private ArrayList<ClientItem> mClientList;

    //mClientNames - list of client names
    private ArrayList<String> mClientNames;

    //mRoutineNames - list of routine names
    private ArrayList<String> mRoutineNames;

    private ClientButler mClientButler;
    private RoutineNameButler mRoutineNameButler;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    //dialog ui components
    private Spinner mSpnRoutine;
    private Spinner mSpnClient;
    private TextView mTxtTimeSelected;
    private TextView mTxtTimeLabel;
    private TextView mTxtClientLabel;
    private TextView mTxtSave;

    //string values - used by session time labels
    private String mStrSessionTime;

    //string value - used by client label
    private String mStrClient;
    private String mMsgAlreadyBooked;
    private String mMsgSameTime;
    private String mMsgAlreadyPast;

    //string value - used by save button
    private String mStrSave;
    private String mStrSaveAnyway;
    private String mStrNoClients;

    //background drawable and color
    private int mTxtOrange;
    private int mTxtBlack;

    //valid status flags
    private boolean mValidTime;
    private boolean mSameBooking;
    private boolean mDoubleClient;
    private boolean mEditMode;
    private boolean mNoClients;

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
    public void setDialogValues(MyActivity activity, String userId, ScheduleItem item,
                                HashMap<String,String> appointmentMap){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;

        //set string value
        mNoneSelected = mActivity.getString(R.string.msg_none_selected);

        //initialize client list buffer
        mClientList = new ArrayList<>();

        //initialize client name list buffer
        mClientNames = new ArrayList<>();

        Boss boss = (Boss)mActivity.getApplication();
        //initialize routine name list buffer
        mRoutineNames = boss.getRoutineNamesForDialog();

        //initialize exercises in routine
        mAppointmentMap = appointmentMap;
        mEditAppointmentMap = new HashMap<>();

        mRoutineNameButler = new RoutineNameButler(mActivity, mUserId);
        mClientButler = new ClientButler(mActivity, mUserId);

        mNoClients = true;
        mEditMode = false;
        //initialize appointment item to save
        initializeSaveItem(item);

        if(item == null){
            //set client spinner selected index
            mClientIndex = 0;

            //set routine spinner selected index
            mRoutineIndex = 0;
        }
        else{
            //need to define indexes after data is loaded
            mClientIndex = -1;
            mRoutineIndex = -1;
        }
    }

    /*
     * void initializeSaveItem(...) - initialize appointment item to be saved
     */
    private void initializeSaveItem(ScheduleItem item){
        //create appointment item to be saved
        mSaveApmtItem = new ScheduleItem();

        //check if item is null
        if(item == null){
            //if null, new appointment item being created
            mSaveApmtItem.uid = mUserId;
            mSaveApmtItem.datestamp = DateTimeHelper.getDatestamp(0);
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
            mSaveApmtItem.datestamp = item.datestamp;
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

    /*
     * void setOnDismissListener(...) - set listener for dialog dismiss events
     */
    public void setOnDismissListener(OnDismissListener listener){
        mDismissListener = listener;
    }

    public void setEditMode(boolean isEditMode){
        mEditMode = isEditMode;

        if(mEditMode){
            mEditAppointmentMap.clear();

            String mapKey = mSaveApmtItem.clientKey + mSaveApmtItem.datestamp +  mSaveApmtItem.appointmentTime;
            mEditAppointmentMap.putAll(mAppointmentMap);
            mEditAppointmentMap.remove(mapKey);
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
        int layoutId = R.layout.dia_schedule_appointment;

        //inflate root view, parent child components
        mRootView = inflater.inflate(layoutId, container, false);

        mStrSessionTime = mActivity.getString(R.string.session_time);
        mMsgAlreadyPast = mActivity.getString(R.string.msg_already_past);

        mStrClient = mActivity.getString(R.string.client);
        mMsgAlreadyBooked = mActivity.getString(R.string.msg_already_booked);
        mMsgSameTime = mActivity.getString(R.string.msg_booked_that_time);

        mStrSave = mActivity.getString(R.string.save);
        mStrSaveAnyway = mActivity.getString(R.string.save_anyway);
        mStrNoClients = mActivity.getString(R.string.no_clients);

        mTxtOrange = DeprecatedUtility.getColor(mActivity, R.color.orange);
        mTxtBlack = DeprecatedUtility.getColor(mActivity, R.color.black);

        mValidTime = true;
        mDoubleClient = false;
        mSameBooking = false;

        //load clients used by spinner
        mClientButler.loadActiveClients(LOADER_CLIENT, new ClientButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<ClientItem> clientList) {
                onClientsLoaded(clientList);
            }
        });

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
        initializeClientViews();

        //initialize routine spinner component
        initializeRoutineSpinner();
    }

    /*
     * void initializeDateTextView(View) - initialize date textView component
     */
    private void initializeDateTextView(){
        //get date textView component
        TextView txtDate = (TextView)mRootView.findViewById(R.id.diaSchedule_txtDate);

        //set string value
        txtDate.setText(mSaveApmtItem.appointmentDate);
    }

    /*
     * void initializeTimeTextView() - initialize time textView component
     */
    private void initializeTimeTextView(){
        //get time textView component
        mTxtTimeSelected = (TextView)mRootView.findViewById(R.id.diaSchedule_txtTimeSelect);
        mTxtTimeLabel = (TextView)mRootView.findViewById(R.id.diaSchedule_txtTime);

        //get appointment time selected string value
        String strTime = mSaveApmtItem.appointmentTime;

        //set text to current time
        mTxtTimeSelected.setText(DateTimeHelper.convert24Hour(strTime));

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
        mTxtSave = (TextView)mRootView.findViewById(R.id.diaSchedule_txtSave);

        //set onClick listener
        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //schedule is valid,
                onSaveRequested();
            }
        });
    }

    /*
     * void initializeClientViews() - initialize client component
     */
    private void initializeClientViews(){

        mTxtClientLabel = (TextView)mRootView.findViewById(R.id.diaSchedule_txtClient);
        //get spinner component
        mSpnClient = (Spinner)mRootView.findViewById(R.id.diaSchedule_spnClient);

        //set onItemSelected listener
        mSpnClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mSpnClient.setAdapter(adpClients);

        //set client selection
        mSpnClient.setSelection(mClientIndex);

        if(mNoClients){
            setNoClients();
        }
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
 *      void onClientsLoaded(Cursor) - active client list from database has been loaded
 *      void loadRoutinesNames() - load routines names from database
 *      void onRoutineNameDataLoaded(Cursor) - routine names from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void onClientsLoaded(Cursor) - active client list from database has been loaded
     */
    private void onClientsLoaded(ArrayList<ClientItem> clientList){
        //create client name buffer
        String clientName = "";

        //check if client index equal zero, new appointment
        if(mClientIndex != 0){
            //editing an appointment, get client attached to appointment
            ClientItem item = mClientButler.getClientFromMap(mSaveApmtItem.clientKey);

            //make sure not null
            if(item != null){
                //get client name, used to find clientIndex
                clientName = item.clientName;
            }
            else{
                //null client, set index to zero
                mClientIndex = 0;
            }
        }

        //clear client buffers
        mClientList.clear();
        mClientNames.clear();

        //save client list to buffer
        mClientList.addAll(clientList);

        //get number of clients
        int count = mClientList.size();

        //loop through cursor
        for(int i = 0; i < count; i++){
            ClientItem item = mClientList.get(i);

            //get client name from item
            String client = item.clientName;

            //check if client name is equal to client
            if(clientName.equals(client)){
                //set client index
                mClientIndex = i;
            }

            //add client name to client name list
            mClientNames.add(client);
        }

        if(mClientNames.isEmpty()){
            mNoClients = true;
            mClientNames.add(mStrNoClients);
        }
        else{
            mNoClients = false;
        }

        if(mRoutineNames.isEmpty()){
            //load routine names for routine spinner
            loadRoutinesNames();
        }
        else{
            setRoutineNameIndex();

        }
    }

    private void setRoutineNameIndex(){
        int count = mRoutineNames.size();
        for(int i = 0; i < count; i++){
            //check if name is equal to routine item name
            if(mRoutineNames.equals(routine)){

                mRoutineIndex = i;
            }
        }
        //initialize dialog
        initializeDialog();
    }

    /*
     * void loadRoutinesNames() - load routines names from database
     */
    private void loadRoutinesNames(){
        mRoutineNameButler.loadRoutineNames(LOADER_ROUTINE_NAME, new RoutineNameButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<RoutineNameItem> routineList) {
                onRoutineNameDataLoaded(routineList);
            }
        });
    }

    /*
     * void onRoutineNameDataLoaded(Cursor) - routine names from database has been loaded
     */
    private void onRoutineNameDataLoaded(ArrayList<RoutineNameItem> routineList){
        //clear routineName buffer
        mRoutineNames.clear();
        mRoutineNames.add(mNoneSelected);

        //create routine name buffer
        String selectedRoutine = "";

        //check if client index equal zero, new appointment
        if(mRoutineIndex != 0){
            //editing an appointment, get routine attached to appointment
            selectedRoutine = mSaveApmtItem.routineName;
        }

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
                //match, set spinner selected index, need to add 1 because of "custom routine"
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
 *      void onClientSelected(int) - client was selected from spinner
 *      void onRoutineSelected(int) - exercise routine was selected from spinner
 *      void onTimePickerSet(int,int) - appointment time has been selected
 *      boolean validTime() - check that appointment time selected has not already passed
 *      void validateClient(String) - warn user if client already has a schedule appointment that day
 *      void onSaveRequested() - save appointment has been requested
 *      void onDismiss(DialogInterface) - dialog dismiss event occurred
 */
/**************************************************************************************************/
    /*
     * void onClientSelected(int) - client was selected from spinner
     */
    private void onClientSelected(int index) {
        if(!mNoClients){
            //get client from list
            ClientItem clientItem = mClientList.get(index);

            //save client data to save appointment item
            mSaveApmtItem.clientKey = clientItem.fkey;
            mSaveApmtItem.clientName = clientItem.clientName;

            validateClient(clientItem.fkey);
        }else{
            setNoClients();
        }
    }

    /*
     * void onRoutineSelected(int) - exercise routine was selected from spinner
     */
    private void onRoutineSelected(int index){
        //save routine data to save appointment item
        mSaveApmtItem.routineName = mRoutineNames.get(index);

    }

    /*
     * void onTimePickerSet(int,int) - appointment time has been selected
     */
    private void onTimePickerSet(int selectedHour, int selectedMinute){

        //convert time to string value
        String strTime = DateTimeHelper.getTime(selectedHour, selectedMinute);

        // set current time into textView
        mTxtTimeSelected.setText(DateTimeHelper.convert24Hour(strTime));

        //save time to save appointment item
        mSaveApmtItem.appointmentTime = strTime;

        String timestamp = DateTimeHelper.getTimestamp(mSaveApmtItem.appointmentDate, strTime);

        mValidTime = validTime(timestamp);

        if(!mNoClients){
            validateClient(mSaveApmtItem.clientKey);
        }
    }

    /*
     * boolean validTime() - check that appointment time selected has not already passed
     */
    private boolean validTime(String timestamp){
        boolean invalid = DateTimeHelper.hasAppointmentTimePassed(timestamp);

        if(invalid){
            String msg = mStrSessionTime + ": " + mMsgAlreadyPast + "!";
            mTxtTimeLabel.setText(msg);
            mTxtTimeLabel.setTextColor(mTxtOrange);

            mTxtSave.setClickable(false);
            mTxtSave.setVisibility(View.INVISIBLE);
            return false;
        }
        else{
            mTxtTimeLabel.setText(mStrSessionTime);
            mTxtTimeLabel.setTextColor(mTxtBlack);

            mTxtSave.setClickable(true);
            mTxtSave.setVisibility(View.VISIBLE);

            if(mDoubleClient){
                mTxtSave.setText(mStrSaveAnyway);
            }
            else{
                mTxtSave.setText(mStrSave);
            }
            return true;
        }
    }

    /*
     * void validateClient(String) - warn user if client already has a schedule appointment that day
     */
    private void validateClient(String clientKey){
        //String mapKey = clientKey + mSaveApmtItem.appointmentTime;
        String mapKey = clientKey + mSaveApmtItem.datestamp +  mSaveApmtItem.appointmentTime;
        String mapValue = clientKey + mSaveApmtItem.datestamp;

        mSameBooking = mAppointmentMap.containsKey(mapKey);

        if(mSameBooking){
            if(!mEditMode){
                mTxtClientLabel.setText(mStrClient + ": " + mMsgSameTime + "!");
                mTxtClientLabel.setTextColor(mTxtOrange);

                mTxtSave.setClickable(false);
                mTxtSave.setVisibility(View.INVISIBLE);
            }
        }
        else{

            if(mEditMode){
                mDoubleClient = mEditAppointmentMap.containsValue(mapValue);
            }
            else{
                mDoubleClient = mAppointmentMap.containsValue(mapValue);
            }

            if(mDoubleClient){
                mTxtClientLabel.setText(mStrClient + ": " + mMsgAlreadyBooked);
                mTxtClientLabel.setTextColor(mTxtOrange);

                if(mValidTime){
                    mTxtSave.setText(mStrSaveAnyway);
                }
            }
            else{
                mTxtClientLabel.setTextColor(mTxtBlack);
                mTxtClientLabel.setText(mStrClient);
                mTxtSave.setText(mStrSave);
            }
        }
    }

    private void setNoClients(){
        mTxtClientLabel.setTextColor(mTxtOrange);
        mTxtClientLabel.setText(mStrNoClients);
        mTxtSave.setVisibility(View.INVISIBLE);

    }

    /*
     * void onSaveRequested() - save appointment has been requested
     */
    private void onSaveRequested(){
        String timestamp = DateTimeHelper.getTimestamp(mSaveApmtItem.appointmentDate, mSaveApmtItem.appointmentTime);
        mValidTime = validTime(timestamp);

        if(mValidTime && mSavedListener != null){
            dismiss();
            //notify save listener
            mSavedListener.onSaveClicked(mSaveApmtItem);
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

}