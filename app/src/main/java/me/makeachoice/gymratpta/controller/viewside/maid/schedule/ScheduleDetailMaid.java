package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientRoutineButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.schedule.ScheduleDetailAdapter;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.dialog.RoutineExerciseDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_ROUTINE;

/**************************************************************************************************/
/*
 * ScheduleDetailMaid displays the exercises to be done with the client for the given session
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

public class ScheduleDetailMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private String mUserId;
    private MyActivity mActivity;

    private ClientItem mClientItem;
    private ScheduleItem mScheduleItem;

    private ArrayList<ClientRoutineItem> mData;

    private ScheduleDetailAdapter mAdapter;

    private ClientRoutineButler mRoutineButler;

    private RoutineExerciseDialog mAddDialog;

    private boolean mEditMode;
    private int mEditIndex;
    private EditText mEdtName;

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
            //notify adapter of drag-and-drop event
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            //get index of item swiped
            int itemIndex = viewHolder.getAdapterPosition();

            //no records, notify adapter of swipe to dismiss event
            mAdapter.onItemDismiss(itemIndex);
        }
    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ScheduleDetailMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ScheduleDetailMaid(...) - constructor
     */
    public ScheduleDetailMaid(String maidKey, int layoutId, String userId, ClientItem clientItem,
                              ScheduleItem appItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;

        mScheduleItem = appItem;
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

        mEditMode = false;

        mData = new ArrayList<>();

        mRoutineButler = new ClientRoutineButler(mActivity, mUserId, mClientItem.fkey);

        String timestamp = DateTimeHelper.getTimestamp(mScheduleItem.appointmentDate, mScheduleItem.appointmentTime);

        mRoutineButler.loadClientRoutinesByTimestamp(timestamp, LOADER_CLIENT_ROUTINE, mRLoadedListener);

    }

    /*
     * void detach() - fragment is being disassociated from activity
     */
    @Override
    public void detach(){
        //fragment is being disassociated from Activity
        super.detach();
    }

    public ArrayList<ClientRoutineItem> getRoutine(){
        return mAdapter.getData();
    }

    public String getRoutineName(){
        return mEdtName.getText().toString();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void prepareFragment(View) - prepare components and data to be displayed by fragment
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeEditText() - initialize editText component displaying routine name
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler to display exercise items
 *      void initializeFAB() - initialize FAB component
 *      RoutineExerciseDialog initializeExerciseDialog(...) - show routine exercise details dialog
 *      void updateEmptyText() - check if adapter is empty or not then updates empty textView
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        initializeEmptyText();

        initializeEditText();

        initializeAdapter();

        initializeRecycler();

        initializeFAB();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" textView message
        String emptyMsg = mFragment.getResources().getString(R.string.emptyRecycler_addExercise);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeEditText() - initialize editText component displaying routine name
     */
    private void initializeEditText(){
        //get editText component
        mEdtName = (EditText)mLayout.findViewById(R.id.routineDetail_edtName);

        //set routine name
        mEdtName.setText(mScheduleItem.routineName);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_exercise_detail;

        //create adapter consumed by the recyclerView
        mAdapter = new ScheduleDetailAdapter(mFragment.getActivity(), adapterLayoutId);

        //add listener for onItemClick events
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get marker id from view
                String markerId = (String)view.getTag(R.string.recycler_tagId);

                //onItemClick event occurred
                onItemClicked(markerId);
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
                mEditMode = false;
                //add exercise, initialize exercise dialog
                initializeExerciseDialog(null);
            }
        });
    }

    /*
     * RoutineExerciseDialog initializeExerciseDialog(...) - show routine exercise details dialog
     */
    private RoutineExerciseDialog initializeExerciseDialog(RoutineItem item) {

        //get fragment manager
        FragmentManager fm = mFragment.getActivity().getSupportFragmentManager();

        //create dialog
        mAddDialog = new RoutineExerciseDialog();

        //set dialog values
        mAddDialog.setDialogValues((MyActivity)mFragment.getActivity(), mUserId, item);

        //set onSaved listener
        mAddDialog.setOnSavedListener(new RoutineExerciseDialog.OnSaveClickListener() {
            @Override
            public void onSaveClicked(RoutineItem item) {
                //dismiss dialog
                mAddDialog.dismiss();

                if(mEditMode){
                    onEditRoutine(item);
                }
                else{
                    //save routine detail
                    onAddRoutine(item);
                }
            }
        });

        //show dialog
        mAddDialog.show(fm, "diaRoutineExercise");

        return mAddDialog;
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
 *      void onRoutineLoaded(...) - data loaded from database
 */
/**************************************************************************************************/
    /*
     * void onRoutineLoaded(...) - data loaded from database
     */
    private void onRoutineLoaded(ArrayList<ClientRoutineItem> routineList){
        //clear list array
        mData.clear();

        //save client routine list
        mData.addAll(routineList);

        //load client data
        prepareFragment();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onItemClicked(int) - exercise routine has been clicked
 */
/**************************************************************************************************/
    /*
     * void onItemClicked(int) - exercise routine has been clicked
     */
    private void onItemClicked(String markerId){
        mEditMode = true;

        //get item index location using markerId
        mEditIndex = mAdapter.getPosition(markerId);

        //get selected item from data list
        ClientRoutineItem item = mAdapter.getItem(mEditIndex);

        RoutineItem routineItem = new RoutineItem();
        routineItem.uid = item.uid;
        routineItem.category = item.category;
        routineItem.exercise = item.exercise;
        routineItem.numOfSets = item.numOfSets;
        routineItem.routineName = mScheduleItem.routineName;
        routineItem.orderNumber = item.orderNumber;

        initializeExerciseDialog(routineItem);
    }


/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Edit Methods
 *      void onEditRoutine(int) - exercise routine item is being edited in exercise list
 *      void onAddRoutineDetail(RoutineItem) - add routine exercise detail from dialog
 */
/**************************************************************************************************/

    /*
     * void onEditRoutine(int) - exercise routine item is being edited in exercise list
     */
    private void onEditRoutine(RoutineItem item){
        ClientRoutineItem routineItem = new ClientRoutineItem(mScheduleItem, item);

        mAdapter.replaceItemAt(routineItem, mEditIndex);


    }

    /*
     * void onAddRoutineDetail(RoutineItem) - add routine exercise detail from dialog
     */
    private void onAddRoutine(RoutineItem item){
        //todo - keep
        ClientRoutineItem routineItem = new ClientRoutineItem(mScheduleItem, item);

        mAdapter.addItem(routineItem);

    }

/**************************************************************************************************/

}
