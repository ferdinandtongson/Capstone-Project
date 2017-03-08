package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineNameButler;
import me.makeachoice.gymratpta.controller.modelside.firebase.RoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.RoutineNameFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineNameQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineRecyclerAdapter;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineNameContract;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.view.activity.RoutineDetailActivity;
import me.makeachoice.gymratpta.view.dialog.DeleteWarningDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING;
import static me.makeachoice.gymratpta.controller.manager.Boss.REQUEST_CODE_ROUTINE_DETAIL;

//TODO - need to add dialogCounter to prevent multiple dialogs from popping up
//TODO - use Boss variable to tag dialog and add tag to GymRatBaseKeeper pause() method

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
 *      MyActivity mActivity - activity context
 *      String mUserId - user id from firebase authentication
 *      int mRoutineLoadCount - number of routines to load from database
 *
 *      RoutineDetailItem mRoutineDetailItem - routine detail item to be added, edited, or deleted
 *      mData - data list of user-defined routine exercises consumed by the adapter
 *      ArrayList<RoutineItem> mData - data list consumed by the adapter
 *
 *      ExerciseAdapter mAdapter - adapter consumed by recycler
 *      DeleteWarningDialog mWarningDialog - dialog warning user that a routine is about to be deleted
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id from firebase authentication
    private String mUserId;

    //mRoutineLoadCount - number of routines to load from database
    private int mRoutineLoadCount;

    //mRoutineDetailItem - routine detail item to be added, edited, or deleted
    private RoutineDetailItem mRoutineDetailItem;

    private RoutineNameButler mRoutineNameButler;
    private RoutineButler mRoutineButler;


    //mData - data list of user-defined routine exercises consumed by the adapter
    private ArrayList<RoutineDetailItem> mData;

    //mAdapter - adapter consumed by recycler
    private RoutineRecyclerAdapter mAdapter;

    //mWarningDialog - dialog warning user that a routine is about to be deleted
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
            //get index of item swiped
            int itemIndex = viewHolder.getAdapterPosition();

            //onDelete has been requested
            //onDeleteRequested(itemIndex);
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
        mRoutineNameButler.loadRoutineNames(LOADER_ROUTINE_NAME, new RoutineNameButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<RoutineNameItem> routineList) {
                onRoutineNamesLoaded(routineList);
            }
        });

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

        //get activity
        mActivity = (MyActivity)mFragment.getActivity();

        //clear list
        mData.clear();

        mRoutineButler = new RoutineButler(mActivity, mUserId);
        mRoutineNameButler = new RoutineNameButler(mActivity, mUserId);
        mRoutineNameButler.loadRoutineNames(LOADER_ROUTINE_NAME, new RoutineNameButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<RoutineNameItem> routineList) {
                onRoutineNamesLoaded(routineList);
            }
        });

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
 *      DeleteWarningDialog initializeDialog(...) - delete warning dialog
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
                //onItemClicked(view);
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
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mTouchCallback);

        //attach helper to recycler, enables drag-and-drop and swipe to dismiss functionality
        //itemTouchHelper.attachToRecyclerView(mBasicRecycler.getRecycler());
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
                //onFabClicked(view);
                String msg = mActivity.getString(R.string.msg_purchase_gymrat);
                Toast.makeText(mActivity,msg, Toast.LENGTH_LONG).show();

            }
        });
    }

    /*
     * DeleteWarningDialog initializeDialog(...) - delete warning dialog
     */
    private DeleteWarningDialog initializeDialog(String routineName) {

        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mWarningDialog = new DeleteWarningDialog();

        //set dialog values
        mWarningDialog.setDialogValues(mActivity, mUserId, routineName);

        //set onDismiss listener
        mWarningDialog.setOnDismissListener(new DeleteWarningDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetData();
            }
        });

        //set onDelete listener
        mWarningDialog.setOnDeleteListener(new DeleteWarningDialog.OnDeleteListener() {
            @Override
            public void onDelete() {
                //delete routine
                deleteRoutine(mRoutineDetailItem);

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
 * Event Methods:
 *      void onItemClicked(View) - recyclerView item was clicked, show routine detail screen
 *      void onFabClicked(View) - fab click event occurred, show routine detail screen
 *      void onDeleteRequested(int) - routine item delete request
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
        ((Boss)mActivity.getApplication()).setRoutineDetailItem(mRoutineDetailItem);

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
        mRoutineDetailItem.routineExercises = new ArrayList<>();

        //save routine detail item in Boss
        ((Boss)mActivity.getApplication()).setRoutineDetailItem(mRoutineDetailItem);

        //start routine detail screen
        startRoutineDetail();
    }

    /*
     * void onDeleteRequested(int) - routine item delete request
     */
    private void onDeleteRequested(int itemIndex){
        //get routine item to be requesting to be deleted
        mRoutineDetailItem = mAdapter.getItem(itemIndex);

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        boolean showWarning = prefs.getBoolean(PREF_DELETE_WARNING, true);

        //check status
        if(showWarning){
            //wants warning, show "delete warning" dialog
            initializeDialog(mRoutineDetailItem.routineName);
        }
        else{
            //no warning, delete routine
            deleteRoutine(mRoutineDetailItem);
        }

    }

    /*
     * void startRoutineDetail() - start routine detail activity
     */
    private void startRoutineDetail(){

        //create intent
        Intent intent = new Intent(mActivity, RoutineDetailActivity.class);

        //NOTE - if you use mFragment.startActivityForResult, result code will be modified
        mActivity.startActivityForResult(intent, REQUEST_CODE_ROUTINE_DETAIL);

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
     * void loadRoutine() - load routine exercise data from database
     */
    private void loadRoutine(int loadCount){
        //get routine name at loadCount index
        String routineName = mData.get(loadCount).routineName;

        mRoutineButler.loadRoutineByName(routineName, LOADER_ROUTINE, new RoutineButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<RoutineItem> routineList) {
                onRoutineDataLoaded(routineList);
            }
        });
    }

    /*
     * void onRoutineNamesLoaded(...) - routine name data loaded from database
     */
    private void onRoutineNamesLoaded(ArrayList<RoutineNameItem> routineList){
        //get size of cursor
        int count = routineList.size();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            RoutineNameItem nameItem = routineList.get(i);

            //create routine detail object
            RoutineDetailItem item = new RoutineDetailItem();

            //save routine name
            item.routineName = nameItem.routineName;

            //create array list buffer for routine exercises
            item.routineExercises = new ArrayList<>();

            //add
            mData.add(item);
        }

        //initialize routine load count
        mRoutineLoadCount = 0;

        //load routine
        loadRoutine(mRoutineLoadCount);

    }

    /*
     * void onRoutineDataLoaded(...) - routine exercise data loaded from database
     */
    private void onRoutineDataLoaded(ArrayList<RoutineItem> routineList){
        //create routine item list buffer
        ArrayList<RoutineItem> routines = new ArrayList<>();

        //get size of cursor
        int count = routineList.size();

        //loop through cursor
        for(int i = 0; i < count; i++){

            //create routine item from cursor data
            RoutineItem item = routineList.get(i);

            //add routine item to list buffer
            routines.add(item);
        }

        //save routine exercise list to routineDetail items list
        mData.get(mRoutineLoadCount).routineExercises = routines;

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
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Routine:
 *      void deleteRoutine(...) - delete routine data from firebase and database
 *      void deleteRoutineFromFirebase(...) - delete routine data from firebase
 *      void deleteRoutineFromDatabase(...) - delete routine data from database
 */
/**************************************************************************************************/
    /*
     * void deleteRoutine(...) - delete routine data from firebase and database
     */
    private void deleteRoutine(RoutineDetailItem deleteItem){
        //delete routine data from firebase
        deleteRoutineFromFirebase(deleteItem);

        //delete routine data from database
        deleteRoutineFromDatabase(deleteItem);

    }

    /*
     * void deleteRoutineFromFirebase(...) - delete routine data from firebase
     */
    private void deleteRoutineFromFirebase(RoutineDetailItem deleteItem){
        RoutineNameFirebaseHelper routineNameFB = RoutineNameFirebaseHelper.getInstance();
        //get firebase instance
        RoutineFirebaseHelper routineFB = RoutineFirebaseHelper.getInstance();

        String routineName = deleteItem.routineName;

        //delete routine name from firebase
        routineNameFB.deleteRoutineName(mUserId, routineName);

        //delete routine exercises from firebase
        routineFB.deleteRoutine(mUserId, routineName, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //does nothing
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
     * void deleteRoutineFromDatabase(...) - delete routine data from database
     */
    private void deleteRoutineFromDatabase(RoutineDetailItem deleteItem){
        //get uri value for routine name table
        Uri uriRoutineName = RoutineNameContract.CONTENT_URI;

        //get uri for routine table
        Uri uriRoutine = RoutineContract.CONTENT_URI;


        //get name from routine detail item
        String routineName = deleteItem.routineName;

        //remove routine name from database
        int nameDeleted = mActivity.getContentResolver().delete(uriRoutineName,
                RoutineNameQueryHelper.routineNameSelection, new String[]{mUserId, routineName});


        //remove exercise routines from database
        int exercisesDeleted = mActivity.getContentResolver().delete(uriRoutine,
                RoutineQueryHelper.routineNameSelection, new String[]{mUserId, routineName});

    }

/**************************************************************************************************/

}
