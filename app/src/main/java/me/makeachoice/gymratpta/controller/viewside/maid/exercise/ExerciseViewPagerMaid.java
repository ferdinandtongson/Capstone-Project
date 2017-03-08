package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ExerciseButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.ExerciseAdapter;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_EXERCISE_BASE;

/**************************************************************************************************/
/*
 * ExerciseViewPagerMaid is utilized by a ViewPager component to display a list of exercises separated
 * into categories by the ViewPager.
 *
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      MyFragment mFragment - fragment being maintained by the Maid
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

public class ExerciseViewPagerMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mData - data list consumed by the adapter
    private ArrayList<ExerciseItem> mData;

    //mAdapter - adapter consumed by recycler
    private ExerciseAdapter mAdapter;

    private String mUserId;
    private String mCategoryKey;
    private int mLoaderId;
    private ExerciseButler mExerciseButler;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseViewPagerMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseViewPagerMaid(...) - constructor
     */
    public ExerciseViewPagerMaid(String maidKey, int layoutId, int index, CategoryItem item){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = item.uid;
        mCategoryKey = item.fkey;
        mData = new ArrayList<>();

        mLoaderId = LOADER_EXERCISE_BASE + index;

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

        mExerciseButler = new ExerciseButler(mActivity, mUserId);

        //load exercises
        mExerciseButler.loadExercises(mCategoryKey, mLoaderId, new ExerciseButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<ExerciseItem> exerciseList) {
                onExercisesLoaded(exerciseList);
            }
        });
    }

    /*
     * void detach() - fragment is being disassociated from activity
     */
    @Override
    public void detach(){
        //fragment is being disassociated from Activity
        super.detach();
        mFragment.getActivity().getSupportLoaderManager().destroyLoader(mLoaderId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void prepareFragment(View) - prepare components and data to be displayed by fragment
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler to display exercise items
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){

        //initialize "empty" textView used when recycler is empty
        initializeEmptyText();

        //initialize adapter used by recycler
        initializeAdapter();

        //initialize recycler
        initializeRecycler();

        //initialize floating action button
        initializeFAB();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" text message
        String emptyMsg = mLayout.getResources().getString(R.string.emptyRecycler_addExercise);

        //set message
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_simple;

        //create adapter consumed by the recyclerView
        mAdapter = new ExerciseAdapter(mActivity, adapterLayoutId);

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //does nothing

                return false;
            }
        });

        //swap data into adapter
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

    }

    /*
 * void initializeFAB() - initialize FAB component
 */
    private void initializeFAB(){
        //get content description string value
        //String description = mActivity.getString(R.string.description_fab_appointment);

        //add content description to FAB
        //mFAB.setContentDescription(description);

        //add listener for onFABClick events
        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onFABClick event occurred
                //onFabClicked(view);
                String msg = mActivity.getString(R.string.msg_purchase_gymrat);
                Toast.makeText(mActivity,msg, Toast.LENGTH_LONG).show();
            }
        });
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
 * Load Methods:
 */
/**************************************************************************************************/
    /*
     * void onExercisesLoaded() - exercises loaded
     */
    private void onExercisesLoaded(ArrayList<ExerciseItem> exerciseList){
        mData.clear();

        mData.addAll(exerciseList);

        prepareFragment();

    }


/**************************************************************************************************/


}
