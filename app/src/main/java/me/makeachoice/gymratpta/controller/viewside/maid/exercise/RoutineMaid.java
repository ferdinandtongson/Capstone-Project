package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineNameLoader;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineRecyclerAdapter;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameColumns;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.view.activity.RoutineDetailActivity;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.REQUEST_CODE_ROUTINE_DETAIL;

/**************************************************************************************************/
/*
 * RoutineMaid displays a list of user-defined exercise routines
 *
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *
 * Variables from GymRatRecyclerMaid:
 *      TextView mTxtEmpty - textView component displayed when recycler is empty
 *      BasicRecycler mBasicRecycler - recycler component
 *      FloatingActionButton mFAB - floating action button component
 *
 * Methods from MyMaid:
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      String getKey() - get maid key value
 *      Fragment getFragment() - get new instance fragment
 *
 * Methods from GymRatRecyclerMaid:
 *      void setEmptyMessage(String) - set "empty" message to be displayed when recycler is empty
 *      void setOnClickFABListener(View.OnClickListener) - set onClick listener for FAB
 *      void checkForEmptyRecycler(boolean) - checks whether to display "empty" message or not
 */
/**************************************************************************************************/

public class RoutineMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 *      int CONTEXT_MENU_EDIT - "edit" context menu id number
 *      int CONTEXT_MENU_DELETE = "delete" context menu id number
 *      ArrayList<RoutineItem> mData - data list consumed by the adapter
 *      ExerciseRecyclerAdapter mAdapter - adapter consumed by recycler
 */
/**************************************************************************************************/

    //CONTEXT_MENU_EDIT - "edit" context menu id number
    private final static int CONTEXT_MENU_EDIT = 0;

    //CONTEXT_MENU_DELETE - "delete" context menu id number
    private final static int CONTEXT_MENU_DELETE = 1;

    //mData - data list consumed by the adapter
    private ArrayList<RoutineDetailItem> mData;

    //mAdapter - adapter consumed by recycler
    private RoutineRecyclerAdapter mAdapter;

    private String mUserId;

    private RoutineDetailItem mRoutineDetailItem;
    private int mRoutineLoadCount;



    //mTouchCallback - helper class that enables drag-and-drop and swipe to dismiss functionality to recycler
    private ItemTouchHelper.Callback mTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            //notify adapter of drag-and-drop event
            ///mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int itemIndex = viewHolder.getAdapterPosition();

            RoutineDetailItem item = mAdapter.getItem(itemIndex);

            //notify adapter of swip to dismiss event
            initializeDialog(item.routineName);
            //mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineMaid - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineMaid(...) - constructor
     */
    public RoutineMaid(String maidKey, int layoutId, String userId){
        //get maidKey
        mMaidKey = maidKey;

        //get user id
        mUserId = userId;

        //fragment layout id number
        mLayoutId = layoutId;

        //initialize data list
        mData = new ArrayList<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 */
/**************************************************************************************************/

    public void resetData(){
        //clear list
        mData.clear();

        //swap old data with new data
        mAdapter.swapData(mData);

        //load routines
        loadRoutineNames();

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

        //clear list
        mData.clear();

        //load routines
        loadRoutineNames();

        //initialize fragment as data is being loaded
        prepareFragment();
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
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeRecycler() - initialize recycler to display exercise items
 *      void initializeAdapter() - initialize adapter to display routine detail info
 *      void initializeRecycler() - initialize recycler to display list of routine details
 *      void initializeFAB() - initialize floating action button
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        //initialize "empty" text, displayed if data is empty
        initializeEmptyText();

        //initialize adapter
        initializeAdapter();

        //initialize recycler view
        initializeRecycler();

        //initialize floating action button
        initializeFAB();

        //update empty textView
        updateEmptyText();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" text message
        String emptyMsg = mLayout.getResources().getString(R.string.emptyRecycler_addRoutine);

        //set message
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - initialize adapter to display routine detail info
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_routine;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineRecyclerAdapter(mLayout.getContext(), adapterLayoutId);

        //add listener for onItemClick events
        mAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onItemClick event occurred
                onItemClicked(view);
            }
        });

        //swap old data with new data
        mAdapter.swapData(mData);
    }

    /*
     * void initializeRecycler() - initialize recycler to display list of routine details
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
     * void initializeFAB() - initialize floating action button
     */
    private void initializeFAB(){
        //get content description string value
        String description = mFragment.getString(R.string.description_fab_routine);

        //set content description for FAB
        mFAB.setContentDescription(description);

        //set onClick listener for FAB
        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked(view);
            }
        });
    }

    /*
     * RoutineExerciseDialog initializeDialog(...) - show routine exercise details dialog
     */
    private DeleteWarningDialog initializeDialog(String routineName) {
        //get activity context
        MyActivity activity = (MyActivity)mFragment.getActivity();

        //get fragment manager
        FragmentManager fm = activity.getSupportFragmentManager();

        //create dialog
        mDialog = new DeleteWarningDialog();
        mDialog.setDialogValues(activity, routineName);
        mDialog.setOnDismissListener(new DeleteWarningDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetData();
            }
        });

        mDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
            @Override
            public void onDelete() {
                Log.d("Choice", "RoutineMaid.onDelete");
            }
        });


        //show dialog
        mDialog.show(fm, "diaDeleteRoutine");

        return mDialog;
    }

    private DeleteWarningDialog mDialog;

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
 * Event Methods:
 *      void onItemClicked(View) - recyclerView item was clicked, show routine detail screen
 *      void onFabClicked(View) - fab click event occurred, show routine detail screen
 *      void startRoutineDetail() - start routine detail activity
 */
/**************************************************************************************************/
    /*
     * void onItemClicked(View) - recyclerView item was clicked, show routine detail screen
     */
    private void onItemClicked(View view){
        //get routine detail item from item clicked
        mRoutineDetailItem = (RoutineDetailItem)view.getTag(R.string.recycler_tagItem);

        //save routine detail item in Boss
        ((Boss)mFragment.getActivity().getApplication()).setRoutineDetailItem(mRoutineDetailItem);

        //start routine detail screen
        startRoutineDetail();

    }

    /*
     * void onFabClicked(View) - fab click event occurred, show routine detail screen
     */
    private void onFabClicked(View view){
        //create routine detail item buffer
        mRoutineDetailItem = new RoutineDetailItem();
        mRoutineDetailItem.routineName = "";
        mRoutineDetailItem.routineExercises = new ArrayList<RoutineItem>();

        //save routine detail item in Boss
        ((Boss)mFragment.getActivity().getApplication()).setRoutineDetailItem(mRoutineDetailItem);

        //start routine detail screen
        startRoutineDetail();
    }

    /*
     * void startRoutineDetail() - start routine detail activity
     */
    private void startRoutineDetail(){
        //get activity context
        MyActivity activity = (MyActivity)mFragment.getActivity();

        //create intent
        Intent intent = new Intent(activity, RoutineDetailActivity.class);

        //NOTE - if you use mFragment.startActivityForResult, result code will be modified
        activity.startActivityForResult(intent, REQUEST_CODE_ROUTINE_DETAIL);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Methods:
 *      void loadRoutineNames() - load routine name data from database
 *      void loadRoutine() - load routine exercise data from database
 *      void onRoutineNameDataLoaded(Cursor) - routine name data loaded from database
 *      void onRoutineDataLoaded(Cursor) - routine exercise data loaded from database
 *      void loadMoreRoutines() - check if more routine exercise data needs to be loaded
 */
/**************************************************************************************************/
    /*
     * void loadRoutineNames() - load routine name data from database
     */
    private void loadRoutineNames(){
        MyActivity activity = (MyActivity)mFragment.getActivity();

        //start loader to get routine data from database
        RoutineNameLoader.loadRoutineNames(activity, mUserId, new RoutineNameLoader.OnRoutineNameLoadListener() {
            @Override
            public void onRoutineNameLoadFinished(Cursor cursor) {
                onRoutineNameDataLoaded(cursor);
            }
        });
    }

    /*
     * void loadRoutine() - load routine exercise data from database
     */
    private void loadRoutine(int loadCount){
        //get activity context
        MyActivity activity = (MyActivity)mFragment.getActivity();

        //get routine name at loadCount index
        String routineName = mData.get(loadCount).routineName;

        //start loader to get routine data from database
        RoutineLoader.loadRoutineByName(activity, mUserId, routineName, new RoutineLoader.OnRoutineLoadListener() {
            @Override
            public void onRoutineLoadFinished(Cursor cursor) {
                onRoutineDataLoaded(cursor);
            }
        });
    }



    /*
     * void onRoutineNameDataLoaded(Cursor) - routine name data loaded from database
     */
    private void onRoutineNameDataLoaded(Cursor cursor){
        //get size of cursor
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create routine detail object
            RoutineDetailItem item = new RoutineDetailItem();

            //save routine name
            item.routineName = cursor.getString(RoutineNameColumns.INDEX_ROUTINE_NAME);

            //create array list buffer for routine exercises
            item.routineExercises = new ArrayList<>();

            //add
            mData.add(item);
        }

        //destroy routine name loader
        RoutineNameLoader.destroyLoader((MyActivity)mFragment.getActivity());

        //initialize routine load count
        mRoutineLoadCount = 0;

        //load routine
        loadRoutine(mRoutineLoadCount);

    }



    /*
     * void onRoutineDataLoaded(Cursor) - routine exercise data loaded from database
     */
    private void onRoutineDataLoaded(Cursor cursor){
        //get activity context
        MyActivity activity = (MyActivity)mFragment.getActivity();

        //create routine item list buffer
        ArrayList<RoutineItem> routines = new ArrayList();

        //get size of cursor
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index
            cursor.moveToPosition(i);

            //create routine item from cursor data
            RoutineItem item = new RoutineItem(cursor);

            //add routine item to list buffer
            routines.add(item);
        }

        //save routine exercise list to routineDetail items list
        mData.get(mRoutineLoadCount).routineExercises = routines;

        //destroy loader activity
        RoutineLoader.destroyLoader(activity);

        //add routine detail item to adapter
        mAdapter.addItem(mData.get(mRoutineLoadCount));

        updateEmptyText();

        loadMoreRoutines();

    }

    /*
     * void loadMoreRoutines() - check if more routine exercise data needs to be loaded
     */
    private void loadMoreRoutines(){
        //increment routine load count
        mRoutineLoadCount++;

        //check if load count is less than routineDetail list count
        if(mRoutineLoadCount < mData.size()){
            //less than, load next routine exercise data
            loadRoutine(mRoutineLoadCount);
        }
        else{
            //routine load is finished
        }
    }

/**************************************************************************************************/

}
