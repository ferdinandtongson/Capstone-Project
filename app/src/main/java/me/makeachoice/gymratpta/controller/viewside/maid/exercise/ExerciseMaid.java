package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.controller.viewside.maid.MyMaid;

/*
 * ExerciseMaid initializes and takes care of the viewPager fragment that show different lists of
 *      exercises
 *
 * Its main purpose is to upkeep and handle events and request from the Fragment and if the Maid
 * cannot handle a request or an event, it will pass it onto the HouseKeeper.
 *
 * The Maid is only aware of the Fragment and the views at the fragment level. It is NOT
 * aware of the view above it (the Activity containing the Fragment).
 *
 *
 * Variables from MyMaid:
 *      int mMaidId
 *      Bridge mBridge
 *      Fragment mFragment
 *      View mLayout
 *
 * Methods from MyMaid:
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      int getMaidId() - get maid id number
 *      MyFragment getFragment() - get fragment maintained by maid
 */
/**************************************************************************************************/

public class ExerciseMaid extends MyMaid {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseMaid(...) - constructor
     * @param maidId - maid id number
     * @param layoutId - resource layout id number used by fragment
     */
    public ExerciseMaid(int maidId, int layoutId){
        //maid id number
        mMaidId = maidId;

        //fragment layout id number
        mLayoutId = layoutId;

        //create fragment
        mFragment = new BasicFragment();

        //attach maid id to fragment
        mFragment.setMaidId(mMaidId);
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
     * @param inflater - layoutInflater to inflate the xml fragment layout resource file
     * @param container - view that will hold the fragment view
     * @param savedInstanceState - saved instance states
     * @return - view of fragment is ready
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
        prepareFragment(mLayout);

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
 *      void initializeToolbar() - initialize toolbar component
 *      void initializeRecycler() - initialize recycler to display exercise items
 *      EditAddDialog initializeDialog(...) - create exercise edit/add dialog
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     * @param layout - fragment container layout
     */
    private void prepareFragment(View layout) {

        //initialize view pager
        /*ExerciseViewPager pagerMeasure = new ExerciseViewPager(mFragment.getActivity(),
                mFragment.getChildFragmentManager(), VIEWPAGER_ID);*/

    }


/**************************************************************************************************/


}
