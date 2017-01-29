package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.controller.viewside.maid.MyMaid;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * ExerciseMaid initializes and takes care of the viewPager fragment that show different lists of
 * exercises separated by category
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
 *      MyFragment getFragment() - get fragment maintained by maid
 */
/**************************************************************************************************/

public class ExerciseMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mCategories - list of category items used by viewPager tabLayout
    ArrayList<CategoryItem> mCategories;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseMaid(...) - constructor
     */
    public ExerciseMaid(String maidKey, int layoutId){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        //create fragment
        //mFragment = BasicFragment.newInstance(maidKey);
        mFragment = new BasicFragment();

        //attach maid key to fragment
        mFragment.setMaidKey(maidKey);
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

        //prepare fragment components
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
 *      void initializeRecycler() - initialize recycler to display exercise items
 *      EditAddDialog initializeDialog(...) - create exercise edit/add dialog
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment() {
        MyActivity activity = (MyActivity)mFragment.getActivity();

        TextView txtTitle = (TextView)activity.findViewById(R.id.stub_txtTitle);
        txtTitle.setText(mMaidKey);

        //load category and exercise data
        //loadData();

        //initialize maids used by viewPager
        //initializeViewPagerMaids(mCategories.size());

        //initialize view pager
        /*ExerciseViewPager pager = new ExerciseViewPager(mFragment.getActivity(),
                mFragment.getChildFragmentManager(), mCategories);*/

    }

    /*
     * void loadData() - load category and exercise data
     */
    private void loadData(){
        //create category stub data
        createCategoryStub();

        //create exercise stub data
        //createExercisesStub();
    }

    /*
     * void initializeMaid(int) - initialize Maids used by the viewPager component
     */
    private void initializeViewPagerMaids(int size){
        //layout resource id maid will use to create fragment
        int layoutId = R.layout.standard_recycler_fab;

        //initialize maids
        for(int i = 0; i < size; i++){
            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_EXERCISE_LIST + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeExerciseListMaid(maidKey, layoutId, i);
        }
    }

/**************************************************************************************************/

    private void createCategoryStub(){
        mCategories = new ArrayList();

        CategoryItem item01 = new CategoryItem();
        item01.categoryName = "Arms";
        CategoryItem item02 = new CategoryItem();
        item02.categoryName = "Back";
        CategoryItem item03 = new CategoryItem();
        item03.categoryName = "Cardio";
        CategoryItem item04 = new CategoryItem();
        item04.categoryName = "Chest";
        CategoryItem item05 = new CategoryItem();
        item05.categoryName = "Core";
        CategoryItem item06 = new CategoryItem();
        item06.categoryName = "Legs";
        CategoryItem item07 = new CategoryItem();
        item07.categoryName = "Shoulders";

        mCategories.add(item01);
        mCategories.add(item02);
        mCategories.add(item03);
        mCategories.add(item04);
        mCategories.add(item05);
        mCategories.add(item06);
        mCategories.add(item07);

    }

}
