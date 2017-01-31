package me.makeachoice.gymratpta.controller.viewside.viewpager;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.StubMaid;
import me.makeachoice.library.android.base.controller.viewside.adapter.MyFragmentPagerAdapter;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * StandardViewPager displays different list of exercises separated by category.
 */
/**************************************************************************************************/

public class StandardViewPager {

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
    private final static int DEFAULT_TABLAYOUT_ID = R.id.choiceTabLayout;

    private Fragment mFragment;
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mAdapter;
    private ArrayList<String> mTitleList;
    private String mBaseMaidKey;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  StandardViewPager - constructor
 */
/**************************************************************************************************/
    /*
     * StandardViewPager - constructor
     */
    public StandardViewPager(Fragment fragment, ArrayList<String> titles, String baseMaidKey){
        //get fragment
        mFragment = fragment;

        //get list of titles
        mTitleList = titles;

        mBaseMaidKey = baseMaidKey;

        initialize(fragment.getActivity(), DEFAULT_VIEWPAGER_ID, DEFAULT_TABLAYOUT_ID);
    }

    private MyActivity mActivity;
    public StandardViewPager(MyActivity activity, ArrayList<String> titles, String baseMaidKey){
        //get activity
        mActivity = activity;

        //get list of titles
        mTitleList = titles;

        mBaseMaidKey = baseMaidKey;

        initialize(mActivity, DEFAULT_VIEWPAGER_ID, DEFAULT_TABLAYOUT_ID);
    }

    private void initialize(Activity activity, int pagerId, int tabId){
        //get viewPager
        mPager = (ViewPager)mFragment.getActivity().findViewById(pagerId);

        //get tabLayout component
        mTabLayout = (TabLayout)mFragment.getActivity().findViewById(tabId);

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
        //initialize maids
        initializeAdapter(mTitleList.size());

        //initialize viewPager component
        initializeViewPager();
   }


    private void initializeAdapter(int size){
        FragmentManager fm;
        if(mFragment != null){
            fm = mFragment.getChildFragmentManager();
        }
        else{
            fm = mActivity.getSupportFragmentManager();
        }

        mAdapter = new ViewPagerAdapter(fm, mBaseMaidKey);

        for(int i = 0; i < size; i++){
            mAdapter.addTitleList(mTitleList.get(i));
        }
    }

    private void initializeViewPager(){
        //set adapter for viewPager
        mPager.setAdapter(mAdapter);

        //set default start page to be displayed
        mPager.setCurrentItem(0);

        //link tabLayout to viewPager
        mTabLayout.setupWithViewPager(mPager);
    }

/**************************************************************************************************/

}
