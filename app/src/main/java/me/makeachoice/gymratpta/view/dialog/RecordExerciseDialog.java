package me.makeachoice.gymratpta.view.dialog;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.CategoryButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientExerciseButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ExerciseButler;
import me.makeachoice.gymratpta.model.item.client.ClientExerciseItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CATEGORY;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_EXERCISE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_EXERCISE_BASE;

public class RecordExerciseDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private final int MAX_SET = 7;
    private int PRIMARY = 0;
    private int SECONDARY = 1;


    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number from firebase authentication
    private String mUserId;
    private int mSetCount;

    private Drawable mBlueBg;
    private Drawable mWhiteBg;
    private Drawable mInvalidBg;
    private Drawable mButtonBg;

    private String mMsgInvalidInput;

    private ArrayList<ClientExerciseItem> mRecords;
    private HashMap<String,ClientExerciseItem> mCurrentMap;
    private HashMap<String,ClientExerciseItem> mLastMap;

    private ArrayList<TextView> mSetViews;
    private ArrayList<EditText> mPrimaryEdit;
    private ArrayList<TextView> mPrimaryViews;
    private ArrayList<TextView> mPrimaryLastViews;
    private ArrayList<EditText> mSecondaryEdit;
    private ArrayList<TextView> mSecondaryViews;
    private ArrayList<TextView> mSecondaryLastViews;

    private ArrayList<String> mPrimaryValues;
    private ArrayList<String> mSecondaryValues;

    private ClientRoutineItem mRoutineExercise;
    private ExerciseItem mExerciseItem;

    private ArrayList<EditText> mBlankInput;
    private boolean mSkippedInput;


    private CategoryButler mCategoryButler;
    private ExerciseButler mExerciseButler;
    private ClientExerciseButler mClientButler;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    private OnCancelListener mCancelListener;
    public interface OnCancelListener{
        void onCancel();
    }

    private OnSaveListener mSaveListener;
    public interface OnSaveListener{
        void onSave(int setCount, ExerciseItem item, ArrayList<String> primaryValues,
                    ArrayList<String> secondaryValues);
    }

    private OnDismissListener mDismissListener;
    public interface OnDismissListener{
        void onDismiss(DialogInterface dialogInterface);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RecordExerciseDialog Constructor
 */
/**************************************************************************************************/
    /*
     * RecordExerciseDialog - constructor
     */
    public RecordExerciseDialog(){
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setter Method
 *      void setDialogValues(...) - set dialog values
 *      void setOnDismissListener(...) - set listener for dialog dismiss events
 *      void setOnDeleteListener(...) - set listener for delete events
 */
/**************************************************************************************************/
    /*
     * void setDialogValues(...) - set dialog values
     */
    public void setDialogValues(MyActivity activity, String userId, ClientRoutineItem item){
        //get activity context
        mActivity = activity;

        //get user id from firebase authentication
        mUserId = userId;

        //get routine exercise item
        mRoutineExercise = item;

        mRecords = new ArrayList<>();

        mPrimaryValues = new ArrayList<>();
        mSecondaryValues = new ArrayList<>();
        mCurrentMap = new HashMap<>();
        mLastMap = new HashMap<>();

    }

    /*
     * void setOnCancelListener(...) - set listener for cancel event
     */
    public void setOnCancelListener(OnCancelListener listener){
        mCancelListener = listener;
    }

    /*
     * void setOnSaveListener(...) - set listener for save event
     */
    public void setOnSaveListener(OnSaveListener listener){
        mSaveListener = listener;
    }

    /*
     * void setOnDismissListener(...) - set listener for dialog dismiss events
     */
    public void setOnDismissListener(OnDismissListener listener){
        mDismissListener = listener;
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeTitleTextView() - initialize textView title component
 *      void initializeButtonTextView() - initialize textView button component
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //layout resource id number
        int layoutId = R.layout.dia_record_exercise;

        //inflate root view, parent child components
        mRootView = inflater.inflate(layoutId, container, false);

        //initialize butler used to load data
        mClientButler = new ClientExerciseButler(mActivity, mUserId, mRoutineExercise.clientKey);
        mExerciseButler = new ExerciseButler(mActivity, mUserId);
        mCategoryButler = new CategoryButler(mActivity, mUserId);

        mBlueBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.bg_blue);
        mWhiteBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.bg_white);
        mInvalidBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.bg_orange);
        mButtonBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.button_border);

        mMsgInvalidInput = mActivity.getString(R.string.msg_forget_input);

        return mRootView;
    }

    /*
     * void onStart() - called when dialog start is request. Gets the devices screen dimensions and
     * then calculates the size of the dialog
     */
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = this.getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        //calculate dialog dimensions relative to screen size
        int dialogWidth = (int)(displayRectangle.width() * 0.95f); // specify a value here
        int dialogHeight = (int)(displayRectangle.height() * 0.6f); // specify a value here

        //set dialog size
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        //load data before initialize ui
        mCategoryButler.loadCategoryByName(mRoutineExercise.category, LOADER_CATEGORY,
                new CategoryButler.OnLoadedListener() {
                    @Override
                    public void onLoaded(ArrayList<CategoryItem> categoryList) {
                        onCategoriesLoaded(categoryList);
                    }
                });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods
 *      void initializeDialog() - initialize dialog components
 *      void initializeViewLists() - initialize views and add them to list buffers
 *      void initializeTitleTextView() - initialize textView title component
 *      void initializeButtonTextView() - initialize textView button component
 */
/**************************************************************************************************/
    /*
     * void initializeDialog() - initialize dialog components
     */
    private void initializeDialog(){

        //initialize dialog view components
        initializeViewLists();

        initializeTitleTextView();
        initializeButtonTextView();

        mSetCount = Integer.valueOf(mRoutineExercise.numOfSets);
        enableViews(mSetCount);
    }

    /*
     * void initializeViewLists() - initialize views and add them to list buffers
     */
    private void initializeViewLists(){
        mSetViews = new ArrayList<>();
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet01));
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet02));
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet03));
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet04));
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet05));
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet06));
        mSetViews.add((TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSet07));

        mPrimaryEdit = new ArrayList<>();
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary01));
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary02));
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary03));
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary04));
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary05));
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary06));
        mPrimaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtPrimary07));

        mPrimaryViews = new ArrayList<>();
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary01));
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary02));
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary03));
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary04));
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary05));
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary06));
        mPrimaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtPrimary07));

        mPrimaryLastViews = new ArrayList<>();
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary01));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary02));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary03));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary04));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary05));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary06));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary07));

        mPrimaryLastViews = new ArrayList<>();
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary01));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary02));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary03));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary04));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary05));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary06));
        mPrimaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastPrimary07));

        mSecondaryEdit = new ArrayList<>();
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary01));
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary02));
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary03));
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary04));
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary05));
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary06));
        mSecondaryEdit.add((EditText) mRootView.findViewById(R.id.diaRecordExercise_edtSecondary07));

        mSecondaryViews = new ArrayList<>();
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary01));
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary02));
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary03));
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary04));
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary05));
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary06));
        mSecondaryViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtSecondary07));

        mSecondaryLastViews = new ArrayList<>();
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary01));
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary02));
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary03));
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary04));
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary05));
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary06));
        mSecondaryLastViews.add((TextView) mRootView.findViewById(R.id.diaRecordExercise_txtLastSecondary07));

    }

    /*
     * void initializeTitleTextView() - initialize textView title component
     */
    private void initializeTitleTextView(){
        //get title textView component
        TextView txtTitle = (TextView)mRootView.findViewById(R.id.diaRecordExercise_txtName);
        TextView txtPrimary = (TextView)mRootView.findViewById(R.id.diaRecordExercise_txtPrimaryLabel);
        TextView txtSecondary = (TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSecondaryLabel);

        //set title value
        txtTitle.setText(mRoutineExercise.exercise);
        txtPrimary.setText(mExerciseItem.recordPrimary);
        txtSecondary.setText(mExerciseItem.recordSecondary);

    }

    /*
     * void initializeButtonTextView() - initialize textView button component
     */
    private void initializeButtonTextView(){
        //get delete textView component
        TextView txtSave = (TextView)mRootView.findViewById(R.id.diaRecordExercise_txtSave);

        //set delete listener
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked();
            }
        });

        //get cancel textView component
        TextView txtCancel = (TextView)mRootView.findViewById(R.id.diaRecordExercise_txtCancel);

        //set cancel listener
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Enable Views
 *      void enableViews(int) - enable and disable component views depending on number of sets
 *      String getCurrentValue(...) - get current value to be displayed
 *      String getLastValue(...) - get last value to be displayed
 */
/**************************************************************************************************/
    /*
     * void enableViews(int) - enable and disable component views depending on number of sets
     */
    private void enableViews(int setCount){
        //get background color used for disabled components
        int disabledColor = DeprecatedUtility.getColor(mActivity, R.color.lightgray);

        //loop through components
        for(int i = 0; i < MAX_SET; i++){

            //check index
            if(i < setCount){
                //less than user-defined set count, enable components
                mSetViews.get(i).setBackground(mBlueBg);
                mPrimaryEdit.get(i).setVisibility(View.VISIBLE);
                mPrimaryEdit.get(i).setText(getCurrentValue(PRIMARY, i));
                mPrimaryEdit.get(i).setHint(getLastValue(PRIMARY, i));
                mPrimaryEdit.get(i).setImeOptions(EditorInfo.IME_ACTION_NEXT);
                mPrimaryLastViews.get(i).setBackground(mBlueBg);
                mPrimaryLastViews.get(i).setText(getLastValue(PRIMARY, i));

                mSecondaryEdit.get(i).setEnabled(true);
                mSecondaryEdit.get(i).setVisibility(View.VISIBLE);
                mSecondaryEdit.get(i).setText(getCurrentValue(SECONDARY, i));
                mSecondaryEdit.get(i).setHint(getLastValue(SECONDARY, i));
                mSecondaryEdit.get(i).setImeOptions(EditorInfo.IME_ACTION_NEXT);
                mSecondaryLastViews.get(i).setBackground(mBlueBg);
                mSecondaryLastViews.get(i).setText(getLastValue(SECONDARY, i));

                mPrimaryViews.get(i).setVisibility(View.INVISIBLE);
                mSecondaryViews.get(i).setVisibility(View.INVISIBLE);
            }
            else{
                //greater than user-defined set count, disable
                mSetViews.get(i).setBackground(mButtonBg);
                mPrimaryEdit.get(i).setVisibility(View.INVISIBLE);
                mPrimaryEdit.get(i).setImeOptions(EditorInfo.IME_ACTION_NONE);
                mSecondaryEdit.get(i).setVisibility(View.INVISIBLE);
                mSecondaryEdit.get(i).setImeOptions(EditorInfo.IME_ACTION_NONE);

                mPrimaryLastViews.get(i).setBackgroundColor(disabledColor);
                mPrimaryLastViews.get(i).setText("-");

                mSecondaryLastViews.get(i).setBackgroundColor(disabledColor);
                mSecondaryLastViews.get(i).setText("-");

                mPrimaryViews.get(i).setVisibility(View.VISIBLE);
                mPrimaryViews.get(i).setBackgroundColor(disabledColor);
                mPrimaryViews.get(i).setText("-");

                mSecondaryViews.get(i).setVisibility(View.VISIBLE);
                mSecondaryViews.get(i).setBackgroundColor(disabledColor);
                mSecondaryViews.get(i).setText("-");
            }

            //initialize "set number" display
            mSetViews.get(i).setText("0" + (i+1));
            mSetViews.get(i).setTag(i);
            mSetViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSetClick(view);
                }
            });
        }

        //set editText component as "Done" action
        mSecondaryEdit.get(setCount-1).setImeOptions(EditorInfo.IME_ACTION_DONE);

    }

    /*
     * String getCurrentValue(...) - get current value to be displayed
     */
    private String getCurrentValue(int recordType, int index){
        if(mCurrentMap.containsKey(String.valueOf(index))){
            if(recordType == PRIMARY){
                return mCurrentMap.get(String.valueOf(index)).primaryValue;
            }
            else{
                return mCurrentMap.get(String.valueOf(index)).secondaryValue;
            }
        }
        return "";
    }

    /*
     * String getLastValue(...) - get last value to be displayed
     */
    private String getLastValue(int recordType, int index){
        if(mLastMap.containsKey(String.valueOf(index))){
            if(recordType == PRIMARY){
                return mLastMap.get(String.valueOf(index)).primaryValue;
            }
            else{
                return mLastMap.get(String.valueOf(index)).secondaryValue;
            }
        }
        return "-";
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Database Methods
 *      void onCategoriesLoaded(...) - exercise categories loaded from database
 *      void onExerciseLoaded(...) - exercise details loaded from database
 *      void onClientExerciseDataLoaded(...) - client exercise data loaded from database
 */
/**************************************************************************************************/
    /*
     * void onCategoriesLoaded(...) - exercise categories loaded from database
     */
    private void onCategoriesLoaded(ArrayList<CategoryItem> categoryList){
        //initialize category key
        String categoryKey = "";

        //check that category list is not empty
        if(categoryList != null && !categoryList.isEmpty()){
            //get category key from list
            categoryKey = categoryList.get(0).fkey;
        }

        //load exercise details data
        mExerciseButler.loadExerciseByName(categoryKey, mRoutineExercise.exercise, LOADER_EXERCISE_BASE,
                new ExerciseButler.OnLoadedListener() {
                    @Override
                    public void onLoaded(ArrayList<ExerciseItem> exerciseList) {
                        onExerciseLoaded(exerciseList);
                    }
                });
    }

    /*
     * void onExerciseLoaded(...) - exercise details loaded from database
     */
    private void onExerciseLoaded(ArrayList<ExerciseItem> exerciseList){
        //check that exercise list is not empty
        if(exerciseList != null && !exerciseList.isEmpty()){
            //get exercise item from list
            mExerciseItem = exerciseList.get(0);
        }

        //load exercise client data
        mClientButler.loadClientExercisesByExercise(mRoutineExercise.exercise, LOADER_CLIENT_EXERCISE,
                new ClientExerciseButler.OnLoadedListener() {
                    @Override
                    public void onLoaded(ArrayList<ClientExerciseItem> exerciseList) {
                        onClientExerciseDataLoaded(exerciseList);
                    }
                });
    }

    /*
     * void onClientExerciseDataLoaded(...) - client exercise data loaded from database
     */
    private void onClientExerciseDataLoaded(ArrayList<ClientExerciseItem> recordList){
        //clear records buffer
        mRecords.clear();

        //check exercise records list not empty
        if(recordList != null && !recordList.isEmpty()){
            //add records to buffer
            mRecords.addAll(recordList);

            //check exercise records
            checkExerciseRecords();
        }

        //initialize dialog
        initializeDialog();

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Check Timestamp Record Methods
 *      void checkExerciseRecords() - check exercise records for current and last time exercise was done
 *      boolean isCurrentRecord() - checks if client exercise record is same timestamp as current
 */
/**************************************************************************************************/
    /*
     * void checkExerciseRecords() - check exercise records for current and last time exercise was done
     */
    private void checkExerciseRecords(){
        //status flag used to check if we have current record
        boolean haveCurrentRecord = false;
        boolean saveNextRecord = false;

        //clear string buffers
        String lastTimestamp = "";

        //clear hashMap buffers
        mCurrentMap.clear();
        mLastMap.clear();

        //get exercise record size
        int count = mRecords.size();

        //loop through records, records are in descending order
        for(int i = 0; i < count; i++){
            //get record item
            ClientExerciseItem item = mRecords.get(i);

            //check if record is current record being modified
            if(isCurrentRecord(item)){
                //is current, save exercise record
                mCurrentMap.put(item.setNumber, item);
                mLastMap.clear();
                saveNextRecord = true;
            }
            else{
                if(i == 0){
                    lastTimestamp = item.timestamp;
                }

                if(saveNextRecord){
                    haveCurrentRecord = true;
                    //timestamp not equal, new "old" record clear buffer
                    mLastMap.clear();

                    lastTimestamp = item.timestamp;
                    saveNextRecord = false;
                }

                //do NOT have current record, check timestamps
                if(lastTimestamp.equals(item.timestamp)){
                    //save timestamp
                    lastTimestamp = item.timestamp;

                    //save record
                    mLastMap.put(item.setNumber, item);
                }
                else{
                    //check if already have current record
                    if(haveCurrentRecord){
                        //already have, exit loop
                        i = count;
                    }
                }

            }
        }
    }

    /*
     * boolean isCurrentRecord() - checks if client exercise record is same timestamp as current
     */
    private boolean isCurrentRecord(ClientExerciseItem record){
        //get current exercise timestamp
        String currentTimestamp = mRoutineExercise.timestamp;
        String currentOrderNumber = mRoutineExercise.orderNumber;

        //get record timestamp
        String recordTimestamp = record.timestamp;
        String recordOrderNumber = record.orderNumber;

        if(recordTimestamp.equals(currentTimestamp) && recordOrderNumber.equals(currentOrderNumber)){
            return true;
        }

        return false;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Validate and Save
 *      void onSaveClicked() - save exercise data
 *      boolean validInput(...) - check if input values are valid
 *      void highlightSkippedInput() - highlight skipped editText components
 *      String editTextValue() - get string value from editText component
 *      void onSetClick(View) - "set number" textView component has been clicked
 */
/**************************************************************************************************/
    /*
     * void onSaveClicked() - save exercise data
     */
    private void onSaveClicked() {
        boolean validInput = true;

        //clear input value buffers
        mPrimaryValues.clear();
        mSecondaryValues.clear();

        //initialize editText blank buffer
        mBlankInput = new ArrayList<>();
        //status flag to check if input has been skipped
        mSkippedInput = false;

        //create editText components
        EditText edtPrimary;
        EditText edtSecondary;

        //loop through components
        for (int i = 0; i < MAX_SET; i++) {
            //get editText components
            edtPrimary = mPrimaryEdit.get(i);
            edtSecondary = mSecondaryEdit.get(i);

            //check if input values are valid or both are empty
            if(validInput(edtPrimary, edtSecondary)){

                //do NOT save if editText components are empty
                if(!editTextValue(edtPrimary).isEmpty()){
                    //save editText values
                    mPrimaryValues.add(editTextValue(edtPrimary));
                    mSecondaryValues.add(editTextValue(edtSecondary));
                }

            }
            else{
                //invalid, show message
                Toast.makeText(mActivity, mMsgInvalidInput, Toast.LENGTH_LONG).show();
                validInput = false;
                i = MAX_SET;
            }
        }

        //check if input is valid
        if(validInput){
            //valid input, save values
            mSaveListener.onSave(mSetCount, mExerciseItem, mPrimaryValues, mSecondaryValues);
        }
    }

    /*
     * boolean validInput(...) - check if input values are valid
     */
    private boolean validInput(EditText edtPrimary, EditText edtSecondary){
        //get input values
        String primaryValue = editTextValue(edtPrimary);
        String secondaryValue = editTextValue(edtSecondary);

        //check if editText is Visible
        if(edtPrimary.getVisibility() == View.VISIBLE){
            //is Visible, check input
            if(secondaryValue.isEmpty() && !primaryValue.isEmpty()){

                //secondary input is missing, invalid input
                edtSecondary.setBackground(mInvalidBg);
                edtPrimary.setBackground(mWhiteBg);

                //highlight any skipped items
                if(mSkippedInput){
                    highlightSkippedInput();
                }

                //invalid
                return false;
            }
            else if(!secondaryValue.isEmpty() && primaryValue.isEmpty()){
                //primary input is missing, invalid input
                edtSecondary.setBackground(mWhiteBg);
                edtPrimary.setBackground(mInvalidBg);

                //highlight any skipped items
                if(mSkippedInput){
                    highlightSkippedInput();
                }

                //invalid
                return false;
            }
            else if(secondaryValue.isEmpty() && primaryValue.isEmpty()){
                //save blank input, only invalid if there are non-blank inputs after
                mBlankInput.add(edtPrimary);
                mBlankInput.add(edtSecondary);

                //set skipped input flag as true
                mSkippedInput = true;
            }
            else{
                edtSecondary.setBackground(mWhiteBg);
                edtPrimary.setBackground(mWhiteBg);
                //check if previous editText components have been skipped
                if(mSkippedInput){
                    //input components have been skipped, invalid input
                    highlightSkippedInput();
                    return false;
                }
            }
        }

        //valid input
        return true;
    }

    /*
     * void highlightSkippedInput() - highlight skipped editText components
     */
    private void highlightSkippedInput(){
        //get number skipped in list
        int count = mBlankInput.size();

        //loop through list
        for(int i = 0; i < count; i++){
            //set component background as invalid
            mBlankInput.get(i).setBackground(mInvalidBg);
        }
    }

    /*
     * String editTextValue() - get string value from editText component
     */
    private String editTextValue(EditText edt){
        return edt.getText().toString();
    }

    /*
     * void onSetClick(View) - "set number" textView component has been clicked
     */
    private void onSetClick(View view){
        //get index
        int index = (int)view.getTag();

        //get visibility value
        int visibility = mPrimaryViews.get(index).getVisibility();

        //check visibility
        if(visibility == View.INVISIBLE){
            //check number of set enabled, cannot be 0
            if(mSetCount > 1){
                //decrease number of exercise sets
                mSetCount = mSetCount - 1;
            }
        }
        else{
            //increase number of exercise sets
            mSetCount = mSetCount + 1;
        }

        //refresh views that are enabled/disabled
        enableViews(mSetCount);
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
