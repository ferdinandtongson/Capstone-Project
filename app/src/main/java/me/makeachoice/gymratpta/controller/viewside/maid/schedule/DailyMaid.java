package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardStateViewPager;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DAY_MAX;

/**************************************************************************************************/
/*
 * DailyMaid manages the ViewPager and the creation of the DayViewMaids that display the daily schedule
 * of the personal trainer
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

public class DailyMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mUserId - user id taken from firebase authentication
    private String mUserId;

    //mPageTitles - list of titles used by viewPager tabLayout
    ArrayList<String> mPageTitles;

    //mDatestamps - list of datestamps
    private ArrayList<Long> mDatestamps;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DailyMaid - constructor
 */
/**************************************************************************************************/
    /*
     * DailyMaid(...) - constructor
     */
    public DailyMaid(String maidKey, int layoutId, String userId){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        //user id take from firebase authentication
        mUserId = userId;
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

        mPageTitles = new ArrayList<>();
        mDatestamps = new ArrayList<>();

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
 *      void initializeArrays() - initialize title and datestamp buffer array
 *      void initializeVPMaids() - initialize maids used by viewPager component
 *      void initializeViewPager() - initialize viewPager component
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        //initialize daily titles
        initializeTitles();

        //initialize maids used by viewPager
        initializeVPMaids();

        //initialize viewPager component
        initializeViewPager();
    }

    /*
     * void initializeArrays() - initialize title and datestamp buffer array
     */
    private void initializeTitles(){
        //clear array buffers
        mPageTitles.clear();
        mDatestamps.clear();

        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        int numberOfDays = prefs.getInt(PREF_DAY_MAX, 30);


        // Start date
        String day;
        long datestamp;

        //loop through number of days
        for(int i = 0; i < numberOfDays; i++){
            //get "MMM dd" value
            day = DateTimeHelper.getMonthDayFromToday(i);
            mPageTitles.add(day);

            //get datestamp
            datestamp = DateTimeHelper.getDatestamp(i);
            mDatestamps.add(datestamp);
        }
    }

    /*
     * void initializeVPMaids() - initialize maids used by viewPager component
     */
    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        int layoutId = R.layout.standard_recycler_fab;

        //get number of pages to create
        int count = mPageTitles.size();

        //initialize maids
        for(int i = 0; i < count; i++){
            long datestamp = mDatestamps.get(i);

            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_DAY_VP + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeDayViewMaid(maidKey, layoutId, mUserId, datestamp, i);
        }

    }

    /*
     * void initializeViewPager() - initialize viewPager component
     */
    private void initializeViewPager(){
        //initialize view pager
        new StandardStateViewPager(mFragment, mPageTitles, MaidRegistry.MAID_DAY_VP);
    }


/**************************************************************************************************/

}
