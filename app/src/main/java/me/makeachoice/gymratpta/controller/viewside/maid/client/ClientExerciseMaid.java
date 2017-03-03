package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientExerciseButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientRoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.ClientRoutineAdapter;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientExerciseItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.dialog.RecordExerciseDialog;
import me.makeachoice.gymratpta.view.dialog.RoutineExerciseDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_EXERCISE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_ROUTINE;

/**************************************************************************************************/
/*
 * ClientExerciseMaid displays the exercises to be done with the client for the given session
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

public class ClientExerciseMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private static int WARNING_DELETE = 0;
    private static int WARNING_EDIT = 1;
    private int mWarningMode;

    private boolean mShowingDialog;
    private String mUserId;
    private MyActivity mActivity;

    private ClientItem mClientItem;
    private ScheduleItem mScheduleItem;

    private String mStrCustomRoutine;
    private String mTimestamp;
    private String mOrderNumber;
    private int mDataCount;

    private Drawable mBlueBg;
    private Drawable mWhiteBg;
    private Drawable mOrangeBg;

    private ArrayList<ClientRoutineItem> mData;
    private ClientRoutineItem mSelectedRoutineItem;
    private ClientRoutineItem mCheckRecordItem;

    private HashMap<String, ArrayList<ClientExerciseItem>> mDataMap;
    private ArrayList<Drawable> mBgStatus;
    private ClientRoutineAdapter mAdapter;

    private ClientRoutineButler mRoutineButler;
    private ClientExerciseButler mExerciseButler;
    private ScheduleButler mScheduleButler;

    private RecordExerciseDialog mExerciseRecordDialog;
    private DeleteWarningDialog mWarningDialog;
    private RoutineExerciseDialog mAddDialog;

    private ArrayList<String> mSavePrimary;
    private ArrayList<String> mSaveSecondary;
    private ArrayList<ClientExerciseItem> mPreviousValues;
    private String mPrimaryLabel;
    private String mSecondaryLabel;
    private int mSaveCounter;
    private int mSelectedIndex;

    private boolean mRefreshOnDismiss;
    private boolean mAlreadyModified;
    private boolean mIsModified;

    private ClientRoutineButler.OnLoadedListener mRLoadedListener = new ClientRoutineButler.OnLoadedListener() {
        @Override
        public void onLoaded(ArrayList<ClientRoutineItem> routineList) {
            onRoutineLoaded(routineList);
        }
    };

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

            //get index of item swiped
            int itemIndex = viewHolder.getAdapterPosition();

            //onDelete has been requested
            swipeDeleteRequest(itemIndex);
        }
    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientExerciseMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientExerciseMaid(...) - constructor
     */
    public ClientExerciseMaid(String maidKey, int layoutId, String userId, ClientItem clientItem,
                              ScheduleItem appItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;

        mScheduleItem = appItem;
        mShowingDialog = false;

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
        mSavePrimary = new ArrayList<>();
        mSaveSecondary = new ArrayList<>();
        mPreviousValues = new ArrayList<>();

        mDataMap = new HashMap<>();
        mBgStatus = new ArrayList<>();

        mBlueBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.bg_blue);
        mWhiteBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.bg_white);
        mOrangeBg = DeprecatedUtility.getDrawable(mActivity, R.drawable.bg_orange);

        mStrCustomRoutine = "*** " + mActivity.getString(R.string.custom_routine) + " ***";
        mAlreadyModified = false;
        mIsModified = false;

        mData = new ArrayList<>();

        mExerciseButler = new ClientExerciseButler(mActivity, mUserId, mClientItem.fkey);
        mRoutineButler = new ClientRoutineButler(mActivity, mUserId, mClientItem.fkey);
        mScheduleButler = new ScheduleButler(mActivity, mUserId);

        mTimestamp = DateTimeHelper.getTimestamp(mScheduleItem.appointmentDate, mScheduleItem.appointmentTime);

        mRoutineButler.loadClientRoutinesByTimestamp(mTimestamp, LOADER_CLIENT_ROUTINE, mRLoadedListener);

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

        updateRoutineModified(mIsModified);
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" textView message
        String emptyMsg = mFragment.getResources().getString(R.string.emptyRecycler_noStats);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_exercise_detail;

        //create adapter consumed by the recyclerView
        mAdapter = new ClientRoutineAdapter(mFragment.getActivity(), adapterLayoutId);
        mAdapter.setStatusList(mBgStatus);

        //add listener for onItemClick events
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //onItemClick event occurred
                onItemClicked(index);
            }
        });

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("Choice", "Maid.onLonClick");
                int index = (int)view.getTag(R.string.recycler_tagPosition);
                onEditRoutine(index);
                return false;
            }
        });

        //swap old data with new data
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
        //String description = mFragment.getString(R.string.description_fab_appointment);

        //add content description to FAB
        //mFAB.setContentDescription(description);

        //add listener for onFABClick events
        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditIndex = -1;
                //add exercise, initialize exercise dialog
                initializeExerciseDialog(null);
            }
        });
    }

    private RecordExerciseDialog initializeExerciseRecordDialog(ClientRoutineItem item){
        if(!mShowingDialog){
            mShowingDialog = true;

            //get fragment manager
            FragmentManager fm = mActivity.getSupportFragmentManager();

            //create dialog
            mExerciseRecordDialog = new RecordExerciseDialog();
            mExerciseRecordDialog.setDialogValues(mActivity, mUserId, item);
            mExerciseRecordDialog.setCancelable(false);

            mExerciseRecordDialog.setOnDismissListener(new RecordExerciseDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mShowingDialog = false;
                }
            });

            mExerciseRecordDialog.setOnCancelListener(new RecordExerciseDialog.OnCancelListener() {
                @Override
                public void onCancel() {
                    mShowingDialog = false;
                }
            });

            mExerciseRecordDialog.setOnSaveListener(new RecordExerciseDialog.OnSaveListener() {
                @Override
                public void onSave(int setCount, ExerciseItem item, ArrayList<String> primaryValue,
                                   ArrayList<String> secondaryValue) {
                    onSaved(setCount, item, primaryValue, secondaryValue);
                    mExerciseRecordDialog.dismiss();
                    mShowingDialog = false;
                }
            });


            mExerciseRecordDialog.show(fm, "diaExerciseRecord");

            return mExerciseRecordDialog;
        }
        return null;
    }


    /*
     * DeleteWarningDialog initializeDialog(...) - delete warning dialog
     */
    private DeleteWarningDialog initializeWarningDialog(ClientRoutineItem deleteItem) {
        if(!mShowingDialog) {
            mShowingDialog = true;

            String deleteAnyway = mActivity.getString(R.string.msg_delete_anyway);
            String strTitle = deleteItem.exercise + " " + deleteAnyway;
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
                        onRefresh(false);
                    }
                    else{
                        mShowingDialog = false;
                    }
                }
            });

            //set onDelete listener
            mWarningDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
                @Override
                public void onDelete() {
                    //do Not refresh, will do db operation first
                    mRefreshOnDismiss = false;

                    if(mWarningMode == WARNING_DELETE){
                        deleteExerciseRecords();
                    }
                    else if (mWarningMode == WARNING_EDIT){
                        deleteEditRecords();
                    }

                    //dismiss dialog
                    mWarningDialog.dismiss();
                }
            });


            //show dialog
            mWarningDialog.show(fm, "diaDeleteRoutine");

            return mWarningDialog;
        }
        return null;
    }


    /*
     * RoutineExerciseDialog initializeExerciseDialog(...) - show routine exercise details dialog
     */
    private RoutineExerciseDialog initializeExerciseDialog(RoutineItem item) {
        if(!mShowingDialog){
            mShowingDialog = true;

            //get fragment manager
            FragmentManager fm = mFragment.getActivity().getSupportFragmentManager();

            //create dialog
            mAddDialog = new RoutineExerciseDialog();

            //set dialog values
            mAddDialog.setDialogValues((MyActivity)mFragment.getActivity(), mUserId, item);

            mAddDialog.setOnDismissListener(new RoutineExerciseDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mShowingDialog = false;
                }
            });

            //set onSaved listener
            mAddDialog.setOnSavedListener(new RoutineExerciseDialog.OnSaveClickListener() {
                @Override
                public void onSaveClicked(RoutineItem item) {
                    //save routine detail
                    onSaveRoutineDetail(item);
                }
            });

            //show dialog
            mAddDialog.show(fm, "diaRoutineExercise");

            return mAddDialog;
        }
        return null;
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

    private void onRefresh(boolean routineModified){
        mIsModified = routineModified;

        mRoutineButler.loadClientRoutinesByTimestamp(mTimestamp, LOADER_CLIENT_ROUTINE, mRLoadedListener);
    }

    private void updateRoutineModified(boolean isModified){
        if(!mAlreadyModified && isModified){
            mAlreadyModified = true;

            mScheduleButler.deleteSchedule(mScheduleItem, new ScheduleButler.OnScheduleDeletedListener() {
                @Override
                public void onScheduleDeleted() {
                    mScheduleItem.routineName = mStrCustomRoutine;
                    mScheduleButler.saveSchedule(mScheduleItem, null);
                }
            });
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onRoutineLoaded(...) - data loaded from database
 *      void requestClientExerciseRecord(int) - request exercise record of given exercise
 *      void checkExerciseStatus(int) - check if exercise has already been or partially done
 */
/**************************************************************************************************/
    /*
     * void onRoutineLoaded(...) - data loaded from database
     */
    private void onRoutineLoaded(ArrayList<ClientRoutineItem> routineList){
        //clear list array
        mData.clear();
        mDataMap.clear();

        //clear background drawable list
        mBgStatus.clear();

        //save client routine list
        mData.addAll(routineList);

        //reset data counter
        mDataCount = 0;

        //request exercise records from routineList
        requestClientExerciseRecord(mDataCount);
    }

    /*
     * void requestClientExerciseRecord(int) - request exercise record of given exercise
     */
    private void requestClientExerciseRecord(int count){
        //check if count is greater than data list
        if(count < mData.size()){
            //get exercise item to check for records
            mCheckRecordItem = mData.get(count);

            //get exercise name
            mOrderNumber = mCheckRecordItem.orderNumber;

            //increase data counter
            mDataCount++;

            //request client exercise record for today's exercise
            mExerciseButler.loadClientExercisesByTimestampOrderNumber(mTimestamp, mOrderNumber, LOADER_CLIENT_EXERCISE,
                    new ClientExerciseButler.OnLoadedListener() {
                        @Override
                        public void onLoaded(ArrayList<ClientExerciseItem> exerciseList) {
                            ArrayList<ClientExerciseItem> list = new ArrayList<>(exerciseList);
                            //put exercise record into data map
                            mDataMap.put(mOrderNumber, list);

                            //get background drawable
                            Drawable drawable = getBackgroundStatus(mCheckRecordItem, exerciseList.size());

                            //check if exercise has already been done or partially done
                            mBgStatus.add(drawable);

                            //request next exercise record
                            requestClientExerciseRecord(mDataCount);
                        }
                    });
        }
        else{
            //load client data
            prepareFragment();
        }

    }

    /*
     * void getBackgroundStatus(int) - check if exercise has already been or partially done
     */
    private Drawable getBackgroundStatus(ClientRoutineItem checkItem, int recordCount){
        if(recordCount == Integer.valueOf(checkItem.numOfSets)){
            return mWhiteBg;
        }
        else if(recordCount == 0){
            return mBlueBg;
        }
        return mOrangeBg;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onItemClicked(int) - exercise routine has been clicked
 *      void onSaved(...) - dialog "save" button was clicked, save exercise record
 *      void updateNumberOfSets(...) - update number of sets in routine exercise
 *      void updateRoutine() - update routine item with new number of sets value
 *      void deletePreviousExerciseValues(...) - delete any previous values saved during current session (to update later)
 *      void saveValues(int) - save exercise record values
 *      ClientExerciseItem createExerciseItem(...) - create client exercise item to save to db
 */
/**************************************************************************************************/
    /*
     * void onItemClicked(int) - exercise routine has been clicked
     */
    private void onItemClicked(int index){
        //save index value
        mSelectedIndex = index;

        //get selected item from data list
        mSelectedRoutineItem = mData.get(mSelectedIndex);

        //initialize dialog
        initializeExerciseRecordDialog(mSelectedRoutineItem);
    }

    /*
     * void onSaved(...) - dialog "save" button was clicked, save exercise record
     */
    private void onSaved(int setCount, ExerciseItem exerciseItem, ArrayList<String> primaryValue,
                         ArrayList<String> secondaryValue){

        //clear save buffers
        mSavePrimary.clear();
        mSaveSecondary.clear();
        mPreviousValues.clear();

        //get primary and secondary labels
        mPrimaryLabel = exerciseItem.recordPrimary;
        mSecondaryLabel = exerciseItem.recordSecondary;

        //get client exercise records corresponding to routine item
        if(mDataMap.isEmpty() || !mDataMap.containsKey(mSelectedRoutineItem.orderNumber)){
            mPreviousValues = new ArrayList<>();
        }
        else{
            mPreviousValues = mDataMap.get(mSelectedRoutineItem.orderNumber);
        }

        //save recorded values
        mSavePrimary.addAll(primaryValue);
        mSaveSecondary.addAll(secondaryValue);

        //initialize save counter
        mSaveCounter = 0;

        //get number of set from selected routine
        int oldNumOfSets = Integer.valueOf(mSelectedRoutineItem.numOfSets);

        //check if number of sets have changed
        if(oldNumOfSets != setCount){
            //has changed, save new number of sets
            mSelectedRoutineItem.numOfSets = String.valueOf(setCount);

            //update number of sets value in database
            updateNumberOfSets(mSelectedRoutineItem);
        }
        else{
            //delete any previously saved values
            deletePreviousExerciseValues();
        }
    }

    /*
     * void updateNumberOfSets(...) - update number of sets in routine exercise
     */
    private void updateNumberOfSets(ClientRoutineItem routineItem){
        mRoutineButler.deleteClientRoutineAtOrderNumber(routineItem, new ClientRoutineButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                updateRoutine();
            }
        });
    }

    /*
     * void updateRoutine() - update routine item with new number of sets value
     */
    private void updateRoutine(){
        mRoutineButler.saveClientRoutine(mSelectedRoutineItem, new ClientRoutineButler.OnSavedListener() {
            @Override
            public void onSaved() {
                //delete any previously saved values
                deletePreviousExerciseValues();
            }
        });
    }

    /*
     * void deletePreviousExerciseValues(...) - delete any previous values saved during current session (to update later)
     */
    private void deletePreviousExerciseValues(){

        //check if there any previous records
        if(mPreviousValues.size() > 0){
            //have previous records, get record
            ClientExerciseItem item = mPreviousValues.get(0);

            //delete exercise from record
            mExerciseButler.deleteClientExerciseByOrderNumber(item, new ClientExerciseButler.OnDeletedListener() {
                @Override
                public void onDeleted() {
                    //finished deletion, save new records
                    saveValues(mSaveCounter);
                }
            });
        }
        else{
            //no old records, save records
            saveValues(mSaveCounter);
        }

    }

    /*
     * void saveValues(int) - save exercise record values
     */
    private void saveValues(int count){
        //check if count is greater than records to save
        if(count < mSavePrimary.size()) {

            //get primary and secondary values
            String pValue = mSavePrimary.get(count);
            String sValue = mSaveSecondary.get(count);

            //make sure values are not empty
            if(!pValue.isEmpty()){
                //create client exercise item
                ClientExerciseItem item = createExerciseItem(count, mPrimaryLabel, mSecondaryLabel,
                        pValue, sValue);

                //save item to database
                mExerciseButler.saveClientExercise(item, new ClientExerciseButler.OnSavedListener() {
                    @Override
                    public void onSaved() {
                        //increase save counter
                        mSaveCounter++;

                        //save next value, if any left
                        saveValues(mSaveCounter);
                    }
                });
            }
            else{
                //value is empty (for some reason, set count to max size to exit loop
                mSaveCounter = mSavePrimary.size();
                saveValues(mSaveCounter++);
            }
        }
        else{
            //update exercise routine
            onRefresh(false);

        }
    }

    /*
     * ClientExerciseItem createExerciseItem(...) - create client exercise item to save to db
     */
    private ClientExerciseItem createExerciseItem(int index, String pLabel, String sLabel,
                                                  String pValue, String sValue){
        ClientExerciseItem saveExerciseItem = new ClientExerciseItem();
        saveExerciseItem.uid = mUserId;
        saveExerciseItem.clientKey = mSelectedRoutineItem.clientKey;
        saveExerciseItem.timestamp = mSelectedRoutineItem.timestamp;
        saveExerciseItem.category = mSelectedRoutineItem.category;
        saveExerciseItem.exercise = mSelectedRoutineItem.exercise;
        saveExerciseItem.orderNumber = mSelectedRoutineItem.orderNumber;
        saveExerciseItem.setNumber = String.valueOf(index);
        saveExerciseItem.primaryLabel = pLabel;
        saveExerciseItem.primaryValue = pValue;
        saveExerciseItem.secondaryLabel = sLabel;
        saveExerciseItem.secondaryValue = sValue;

        return saveExerciseItem;
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * SwipeDelete Methods
 *      void swipeDeleteRequest(int) - a swipe delete event occurred
 *      void deleteClientRoutine() - delete routine item
 *      void modifyRoutineItems(int) - modify order number of remaining routine items in adapter
 *      void modifyExerciseRecords(int) - modify any exercise records linked to routine
 *      void updateExerciseRecords(int) - update old records with new order number
 *      void deleteExerciseRecords() - delete exercise records of exercise routine being swipe deleted
 */
/**************************************************************************************************/

    private String mOldOrderNumber;
    private int mDeleteIndex;
    private int mExerciseUpdateCounter;
    private ClientRoutineItem mDeleteRoutine;
    private ArrayList<ClientExerciseItem> mModifyRecords;

    /*
     * void swipeDeleteRequest(int) - a swipe delete event occurred
     */
    private void swipeDeleteRequest(int index){
        mDeleteIndex = index;
        //get routine item to delete
        mDeleteRoutine = mData.get(index);

        //get client exercise records corresponding to routine item
        ArrayList<ClientExerciseItem> exerciseRecords;
        //get client exercise records corresponding to routine item
        if(mDataMap.isEmpty() || !mDataMap.containsKey(mDeleteRoutine.orderNumber)){
            exerciseRecords = new ArrayList<>();
        }
        else{
            exerciseRecords = mDataMap.get(mDeleteRoutine.orderNumber);
        }


        //check if any records exist
        if(!exerciseRecords.isEmpty()){
            mWarningMode = WARNING_DELETE;
            //has records, warn user
            initializeWarningDialog(mDeleteRoutine);
        }
        else{
            //no records, notify adapter of swipe to dismiss event
            mAdapter.onItemDismiss(mDeleteIndex);

            //delete client routine
            deleteClientRoutine();
        }
    }

    /*
     * void deleteClientRoutine() - delete routine item
     */
    private void deleteClientRoutine(){
        //delete all routine items in database
        mRoutineButler.deleteClientRoutine(mDeleteRoutine, new ClientRoutineButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                //item deleted, need to update other routine items with new order values
                //set save count to zero
                mSaveCounter = 0;

                mDataMap.remove(mDeleteRoutine.orderNumber);

                //check adapter for other routine items
                modifyRoutineItems(mSaveCounter);
            }
        });
    }

    /*
     * void modifyRoutineItems(int) - modify order number of remaining routine items in adapter
     */
    private void modifyRoutineItems(int counter){
        //check number of items in adapter
        if(counter < mAdapter.getItemCount()){
            //less than counter, get routine item
            ClientRoutineItem saveItem = mAdapter.getItem(counter);

            //save old order number
            mOldOrderNumber = saveItem.orderNumber;

            //update order number
            saveItem.orderNumber = String.valueOf(counter + 1);

            //save routine item to database
            mRoutineButler.saveClientRoutine(saveItem, new ClientRoutineButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    //modify exercise records
                    modifyExerciseRecords();
                }
            });
        }
        else{
            mShowingDialog = false;
            onRefresh(true);
        }
    }

    /*
     * void modifyExerciseRecords(int) - modify any exercise records linked to routine
     */
    private void modifyExerciseRecords() {
        //get exercise records linked to routine
        if(mDataMap.isEmpty() || !mDataMap.containsKey(mOldOrderNumber)){
            mModifyRecords = new ArrayList<>();
        }
        else{
            mModifyRecords = mDataMap.get(mOldOrderNumber);
        }

        //check if there are any records
        if (mModifyRecords != null && !mModifyRecords.isEmpty()) {

            //get first exercise record
            ClientExerciseItem deleteRecord = mModifyRecords.get(0);

            //delete all records with order number
            mExerciseButler.deleteClientExerciseByOrderNumber(deleteRecord,
                    new ClientExerciseButler.OnDeletedListener() {
                        @Override
                        public void onDeleted() {
                            //set exercise counter to zero
                            mExerciseUpdateCounter = 0;

                            updateExerciseRecords(mExerciseUpdateCounter);
                        }
                    });
        } else {
            mSaveCounter++;
            modifyRoutineItems(mSaveCounter);
        }
    }

    /*
     * void updateExerciseRecords(int) - update old records with new order number
     */
    private void updateExerciseRecords(int counter){
        //check if exercise counter is less then exercise records
        if(counter < mModifyRecords.size()){
            //get exercise record
            ClientExerciseItem saveRecord = mModifyRecords.get(0);

            //update order number
            saveRecord.orderNumber = String.valueOf(mSaveCounter + 1);

            //save exercise record
            mExerciseButler.saveClientExercise(saveRecord, new ClientExerciseButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    //update exercise counter
                    mExerciseUpdateCounter++;

                    //modify next exercise record, if any
                    updateExerciseRecords(mExerciseUpdateCounter);
                }
            });
        }
        else{
            //no more records, increase routine save counter
            mSaveCounter++;

            //modify next routine item, if any
            modifyRoutineItems(mSaveCounter);
        }
    }

    /*
     * void deleteExerciseRecords() - delete exercise records of exercise routine being swipe deleted
     */
    private void deleteExerciseRecords(){

        //get client exercise records corresponding to routine item
        ArrayList<ClientExerciseItem> exerciseRecords;
        //get client exercise records corresponding to routine item
        if(mDataMap.isEmpty() || !mDataMap.containsKey(mDeleteRoutine.orderNumber)){
            exerciseRecords = new ArrayList<>();
        }
        else{
            exerciseRecords = mDataMap.get(mDeleteRoutine.orderNumber);
        }

        ClientExerciseItem item = exerciseRecords.get(0);

        //delete exercise from record
        mExerciseButler.deleteClientExerciseByOrderNumber(item, new ClientExerciseButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                //notify adapter of swipe to dismiss event
                mAdapter.onItemDismiss(mDeleteIndex);

                //delete routine
                deleteClientRoutine();
            }
        });

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Edit Methods
 *      void onEditRoutine(int) - exercise routine item is being edited in exercise list
 *      void onSaveRoutineDetail(RoutineItem) - save routine exercise detail from dialog
 *      void deleteEditRoutine() - delete previous exercise routine
 *      void deleteEditRecords() - delete exercise records after warning
 *      void saveEditRoutine() - save modified exercise routine
 *      RoutineItem createRoutineItem(...) - create client routine item
 */
/**************************************************************************************************/

    private int mEditIndex;
    private ClientRoutineItem mEditDeleteRoutine;
    private ClientRoutineItem mEditSaveRoutine;

    /*
     * void onEditRoutine(int) - exercise routine item is being edited in exercise list
     */
    private void onEditRoutine(int index){
        //get index of routine item being edited
        mEditIndex = index;

        //get routine item to delete
        mEditDeleteRoutine = mData.get(index);

        //open exercise dialog
        initializeExerciseDialog(createRoutineItem(mEditDeleteRoutine));

    }

    /*
     * void onSaveRoutineDetail(RoutineItem) - save routine exercise detail from dialog
     */
    private void onSaveRoutineDetail(RoutineItem item){
        //create exercise routine to save to list
        mEditSaveRoutine = new ClientRoutineItem(mScheduleItem, item);

        //buffer to hold exercise records, if any, of routine being edited
        ArrayList<ClientExerciseItem> exerciseRecords;

        //get client exercise records corresponding to routine item
        if(mDataMap.isEmpty() || !mDataMap.containsKey(mEditSaveRoutine.orderNumber)){
            //not available, create new list
            exerciseRecords = new ArrayList<>();
        }
        else{
            //get exercise records
            exerciseRecords = mDataMap.get(mEditDeleteRoutine.orderNumber);
        }

        //check index
        if(mEditIndex != -1){
            //routine being edited, check if any records exist
            if(exerciseRecords != null && !exerciseRecords.isEmpty()){
                //has records, dismiss exercise dialog
                mAddDialog.dismiss();
                mShowingDialog = false;

                //show warning dialog
                mWarningMode = WARNING_EDIT;
                initializeWarningDialog(mEditDeleteRoutine);
            }
            else{
                //delete routine to be deleted
                deleteEditRoutine();
            }
        }
        else{
            //save routine from FAB, add new routine to end of list
            mEditSaveRoutine.orderNumber = String.valueOf(mAdapter.getItemCount() + 1);

            //add new exercise routine to list
            mRoutineButler.saveClientRoutine(mEditSaveRoutine, new ClientRoutineButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    onRefresh(true);
                }
            });
        }

        //dismiss dialog
        mAddDialog.dismiss();
    }

    /*
     * void deleteEditRoutine() - delete previous exercise routine
     */
    private void deleteEditRoutine(){
        //delete old exercise routine
        mRoutineButler.deleteClientRoutineAtOrderNumber(mEditDeleteRoutine, new ClientRoutineButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                //save routine edited by onLongClick
                saveEditRoutine();
            }
        });
    }

    /*
     * void deleteEditRecords() - delete exercise records after warning
     */
    private void deleteEditRecords(){
        //get client exercise records corresponding to routine item
        ArrayList<ClientExerciseItem> exerciseRecords;

        //get client exercise records corresponding to routine item
        if(mDataMap.isEmpty() || !mDataMap.containsKey(mEditDeleteRoutine.orderNumber)){
            //not available, create new list
            exerciseRecords = new ArrayList<>();
        }
        else{
            //get exercise records
            exerciseRecords = mDataMap.get(mEditDeleteRoutine.orderNumber);
        }

        //get first exercise item from list
        ClientExerciseItem item = exerciseRecords.get(0);

        //delete exercise from record
        mExerciseButler.deleteClientExerciseByOrderNumber(item, new ClientExerciseButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                //delete client exercise routine
                deleteEditRoutine();
            }
        });
    }

    /*
     * void saveEditRoutine() - save modified exercise routine
     */
    private void saveEditRoutine(){
        //save routine edited by onLongClick, use same orderNumber from old routine being edited
        mEditSaveRoutine.orderNumber = String.valueOf(mEditDeleteRoutine.orderNumber);
        mRoutineButler.saveClientRoutine(mEditSaveRoutine, new ClientRoutineButler.OnSavedListener() {
            @Override
            public void onSaved() {
                onRefresh(true);
            }
        });

    }

    /*
     * RoutineItem createRoutineItem(...) - create client routine item
     */
    private RoutineItem createRoutineItem(ClientRoutineItem item){
        RoutineItem routineItem = new RoutineItem();
        routineItem.uid = item.uid;
        routineItem.routineName = "";
        routineItem.orderNumber = item.orderNumber;
        routineItem.exercise = item.exercise;
        routineItem.category = item.category;
        routineItem.numOfSets = item.numOfSets;

        return routineItem;
    }

/**************************************************************************************************/

}
