package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.RoutineDetailRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.view.dialog.RoutineExerciseDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * RoutineDetailMaid manages the routine detail screen where the user can created custom exercise
 * routines for his/her clients
 * 
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      
 * Variables from GymRatRecyclerMaid:
 *      mTxtEmpty - textView component displayed when recycler is empty
 *      mBasicRecycler - recycler component
 *      mFAB - floating action button component
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

public class RoutineDetailMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 *      int MAX_EXERCISES - maximum number of exercises that can be added to routine
 *      String mUserId - user id taken from firebase authentication
 *      int mExerciseIndex - index location of exercise item in routine
 *
 *      RoutineItem mRoutineItem - item containing exercise details for routine
 *      RoutineDetailItem mRoutineDetailItem - details of routine being viewed
 *      ArrayList<RoutineItem> mRoutineExerciseList - list of exercises in routine, consumed by adapter
 *
 *      EditText mEdtName - editText component used to display routine name
 *      RoutineExerciseDialog mDialog - dialog displaying exercise details used for routine
 *      RoutineDetailRecyclerAdapter mAdapter - adapter consumed by recycler
 *      ItemTouchHelper.Callback mTouchCallback - helper class that enables drag-and-drop and swipe
 *          to dismiss functionality to recycler
 */
/**************************************************************************************************/

    //MAX_EXERCISES - maximum number of exercises that can be added to routine
    private int MAX_EXERCISES = 10;

    //mUserId - user id taken from firebase authentication
    private String mUserId;

    //mExerciseIndex - index location of exercise item in routine
    private int mExerciseIndex;

    //mRoutineItem - item containing exercise details for routine
    private RoutineItem mRoutineItem;

    //mRoutineDetailItem - details of routine being viewed
    private RoutineDetailItem mRoutineDetailItem;

    //mRoutineExerciseList - list of exercises in routine, consumed by adapter
    private ArrayList<RoutineItem> mRoutineExerciseList;

    //mEdtName - editText component used to display routine name
    private EditText mEdtName;

    //mDialog - dialog displaying exercise details used for routine
    private RoutineExerciseDialog mDialog;

    //mAdapter - adapter consumed by recycler
    private RoutineDetailRecyclerAdapter mAdapter;

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
            //notify adapter of swip to dismiss event
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());

            //update empty adapter textView
            updateEmptyText();
        }
    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineDetailMaid - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineDetailMaid(...) - constructor
     */
    public RoutineDetailMaid(String maidKey, int layoutId, String userId, RoutineDetailItem item){
        //get maidKey
        mMaidKey = maidKey;

        //get user id
        mUserId = userId;

        //get routine details object
        mRoutineDetailItem = item;

        //get routine exercise list
        mRoutineExerciseList = mRoutineDetailItem.routineExercises;

        //fragment layout id number
        mLayoutId = layoutId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods:
 *      String getRoutineName() - get routine name
 *      ArrayList<RoutineItem> getRoutineExerciseList() - get list of exercises in routine
 */
/**************************************************************************************************/
    /*
     * String getRoutineName() - get routine name
     */
    public String getRoutineName(){
        //get routine name from editText component
        return mEdtName.getText().toString();
    }

    /*
     * ArrayList<RoutineItem> getRoutineExerciseList() - get list of exercises in routine
     */
    public ArrayList<RoutineItem> getRoutineExerciseList(){
        return mAdapter.getData();
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

        //prepare fragment
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
 *      void initializeEditText() - initialize editText component displaying routine name
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - initialize adapter to display exercise details of routine
 *      void initializeRecycler() - initialize recycler to display list of exercise items
 *      void initializeFAB() - initialize FAB component
 *      RoutineExerciseDialog initializeDialog(...) - show routine exercise list dialog
 *      void updateEmptyText() - check if adapter is empty or not then updates empty textView
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        initializeEditText();
        //initialize "empty" text, displayed if data is empty
        initializeEmptyText();

        //initialize adapter
        initializeAdapter();

        //initialize recycler view
        initializeRecycler();

        initializeFAB();

        //check if exercise list is empty
        isEmptyRecycler(mRoutineExerciseList.isEmpty());

    }

    /*
     * void initializeEditText() - initialize editText component displaying routine name
     */
    private void initializeEditText(){
        //get editText component
        mEdtName = (EditText)mLayout.findViewById(R.id.routineDetail_edtName);

        //set routine name
        mEdtName.setText(mRoutineDetailItem.routineName);

        //get content description value
        String strDescription = mFragment.getString(R.string.description_edit_routine_name);

        //set content description
        mEdtName.setContentDescription(strDescription);
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" text message
        String emptyMsg = mLayout.getResources().getString(R.string.emptyRecycler_addRoutineExercise);

        //set message
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - initialize adapter to display exercise details of routine
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_exercise_detail;

        //create adapter consumed by the recyclerView
        mAdapter = new RoutineDetailRecyclerAdapter(mLayout.getContext(), adapterLayoutId);

        //add listener for onItemClick events
        mAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onItemClick event occurred
                onItemClicked(view);
            }
        });

        //swap old data with new data
        mAdapter.swapData(mRoutineExerciseList);
    }

    /*
     * void initializeRecycler() - initialize recycler to display list of exercise items
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
        String description = mFragment.getString(R.string.description_fab_routine);

        //add content description to FAB
        mFAB.setContentDescription(description);

        //add listener for onFABClick events
        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onFABClick event occurred
                onFabClicked(view);
            }
        });
    }

    /*
     * RoutineExerciseDialog initializeDialog(...) - show routine exercise details dialog
     */
    private RoutineExerciseDialog initializeDialog(RoutineItem item) {
        //get fragment manager
        FragmentManager fm = mFragment.getActivity().getSupportFragmentManager();

        //create dialog
        mDialog = new RoutineExerciseDialog();

        //set dialog values
        mDialog.setDialogValues((MyActivity)mFragment.getActivity(), mUserId, item);

        //set onSaved listener
        mDialog.setOnSavedListener(new RoutineExerciseDialog.OnSaveClickListener() {
            @Override
            public void onSaveClicked(RoutineItem item) {
                //save routine detail
                onSaveRoutineDetail(item);
            }
        });

        //show dialog
        mDialog.show(fm, "diaRoutineExercise");

        return mDialog;
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
 *      void onItemClicked(View) - recyclerView item was clicked, show dialog
 *      void onFabClicked(View) - FAB item was clicked, show dialog
 *      void onSaveRoutineDetail(RoutineItem) - save routine exercise detail from dialog
 */
/**************************************************************************************************/
    /*
     * void onItemClicked(View) - recyclerView item was clicked, show dialog
     */
    private void onItemClicked(View view){
        //get marker id from view
        String markerId = (String)view.getTag(R.string.recycler_tagId);

        //get routineItem from view
        mRoutineItem = (RoutineItem)view.getTag(R.string.recycler_tagItem);

        //get item inde location using markerId
        mExerciseIndex = mAdapter.getPosition(markerId);

        //show dialog
        initializeDialog(mRoutineItem);

    }

    /*
     * void onFabClicked(View) - FAB item was clicked, show dialog
     */
    private void onFabClicked(View view){
        //check that exercise list is not maxed
        if(mAdapter.getItemCount() >= MAX_EXERCISES){
            //get "maximum exercise" message
            String msg = mFragment.getString(R.string.msg_maximum_exercises);

            //display toast message
            Toast.makeText(mFragment.getContext(), msg, Toast.LENGTH_LONG).show();
        }
        else{
            //create new routine item
            mRoutineItem = new RoutineItem();

            //no set index
            mExerciseIndex = -1;

            //show dialog
            initializeDialog(null);
        }
    }

    /*
     * void onSaveRoutineDetail(RoutineItem) - save routine exercise detail from dialog
     */
    private void onSaveRoutineDetail(RoutineItem item){
        //check index
        if(mExerciseIndex != -1){
            //update routine exercise item in adapter
            mAdapter.replaceItemAt(item, mExerciseIndex);
        }
        else{
            //add routine exercise item to adapter
            mAdapter.addItem(item);
        }

        //update empty adapter textView
        updateEmptyText();

        //dismiss dialog
        mDialog.dismiss();
    }

/**************************************************************************************************/

}
