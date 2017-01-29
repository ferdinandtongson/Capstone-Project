package me.makeachoice.gymratpta.controller.viewside.viewpager;

/**************************************************************************************************/
/*
 * ExerciseViewPager displays different list of exercises separated by category.
 */

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.StubMaid;
import me.makeachoice.library.android.base.controller.viewside.adapter.MyFragmentPagerAdapter;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/

public class ExerciseViewPager implements MyFragmentPagerAdapter.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      DEFAULT_START_PAGE - default tab layout to be displayed first
 *      BASE_MAID_ID - base maid id
 *      mPagerId - viewPager component resource id
 *      ArrayList<String> mTitleList - list of exercise categories used by the tabLayout and
 *          viewPager components
 *
 *      ViewPager mPager - viewPager component
 *      MyFragmentPagerAdapter mAdapter - FragmentPagerAdapter used small sets of pages
 *      Boss mBoss - Boss application class
 *      mActivity - activity context
 *      mFragmentManager - fragment manager
 *      mFBCategory - firebase access to category and exercises data
 */
/**************************************************************************************************/

    private final static int DEFAULT_VIEWPAGER_ID = R.id.choiceViewPager;

    private Fragment mFragment;
    private ViewPager mPager;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  ExerciseViewPager - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseViewPager - constructor
     */
    public ExerciseViewPager(Fragment fragment){
        //get fragment
        mFragment = fragment;

        //get viewPager
        mPager = (ViewPager)mFragment.getActivity().findViewById(DEFAULT_VIEWPAGER_ID);

        prepareFragment();
    }

    private MyActivity mActivity;
    public ExerciseViewPager(MyActivity activity){
        mActivity = activity;

        mPager = (ViewPager)mActivity.findViewById(DEFAULT_VIEWPAGER_ID);
        prepareFragment();
    }

    public ExerciseViewPager(Fragment fragment, int pagerId){
        //get fragment
        mFragment = fragment;

        //get viewPager
        mPager = (ViewPager)mFragment.getActivity().findViewById(pagerId);

        prepareFragment();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Initialization Methods
 *      void initializeMaid(int) - initialize Maids used by the viewPager component
 *      void initializeViewPager(Activity,FragmentManager,int) - initialize viewPager component
 *      void initializeTabLayout(Activity) - initialize tabLayout component
 */
/**************************************************************************************************/

    private void prepareFragment(){
        //initialize list of titles
        initializeTitleList();

        //initialize maids
        initializeMaid(mTitleList.size());

        //initialize viewPager component
        initializeViewPager();

        //initialize tabLayout component
        initializeTabLayout();
    }

    private ArrayList<String> mTitleList;
    private void initializeTitleList(){
        mTitleList = new ArrayList();
        mTitleList.add("Stub01");
        mTitleList.add("Stub02");
        mTitleList.add("Stub03");
        mTitleList.add("Stub04");
        mTitleList.add("Stub05");
        mTitleList.add("Stub06");
        mTitleList.add("Stub07");
    }

    private void initializeMaid(int size){
        Log.d("Choice", "ExerciseViewPager.initializeMaid");
        FragmentManager fm;
        if(mFragment != null){
            fm = mFragment.getChildFragmentManager();
        }
        else{
            fm = mActivity.getSupportFragmentManager();
        }

        mAdapter = new ViewPagerAdapter(fm);

        for(int i = 0; i < size; i++){
            //get maid registry
            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            String maidKey = MaidRegistry.MAID_STUB + i;
            //StubMaid maid = maidRegistry.initializeStubMaid(maidKey);
            maidRegistry.initializeRoutineMaid(maidKey, R.layout.stub_text);
            Log.d("Choice", "     add: " + maidKey);

            mAdapter.addTitleList(mTitleList.get(i));
        }
    }

    private ViewPagerAdapter mAdapter;
    private void initializeViewPager(){

        //set title pages used by tabLayout and viewPager component
        //mAdapter.setPageTitle(mTitleList);

        //set adapter for viewPager
        mPager.setAdapter(mAdapter);

        //set default start page to be displayed
        mPager.setCurrentItem(0);

    }

    /*
     * void initializeTabLayout(Activity) - initialize tabLayout component
     */
    private void initializeTabLayout(){
        TabLayout tabLayout;
        if(mFragment != null){
            //get tabLayout component
            tabLayout = (TabLayout)mFragment.getActivity().findViewById(R.id.choiceTabLayout);
        }
        else{
            tabLayout = (TabLayout)mActivity.findViewById(R.id.choiceTabLayout);
        }
        //link tabLayout to viewPager
        tabLayout.setupWithViewPager(mPager);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyFragmentStatePagerAdapter.Bridge Methods
 *      Fragment requestFragment(int) - get requested fragment maid
 *      int requestFragmentCount() - get number of fragments
 */
/**************************************************************************************************/
    /*
     * Fragment requestFragment(int) - get requested fragment maid
     */
    public Fragment requestFragment(int position){
        //get maid registry
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        String maidKey = MaidRegistry.MAID_STUB + position;

        //get maid in charge of fragment
        StubMaid maid = (StubMaid)maidRegistry.requestMaid(maidKey);
        //BasicFragment fragment = BasicFragment.newInstance(maidKey);

        //return fragment held by maid
        return maid.getFragment();
    }

    /*
     * int requestFragmentCount() - get number of fragments
     */
    public int requestFragmentCount(){
        //get number of fragments held by viewPager
        return mTitleList.size();
    }

/**************************************************************************************************/

}
