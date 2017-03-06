package me.makeachoice.gymratpta.controller.viewside.maid.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardStateViewPager;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * WeeklyMaid manages the viewPager and the creation of WeekViewMaid that displays the schedule of the week
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

public class WeeklyMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mUserId - user id taken from firebase authentication
    private String mUserId;

    //mPageTitles - list of titles used by viewPager tabLayout
    private ArrayList<String> mPageTitles;

    private String[][] mDateRange;
    private ArrayList<Date> mStartDates;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * WeeklyMaid - constructor
 */
/**************************************************************************************************/
    /*
     * WeeklyMaid(...) - constructor
     */
    public WeeklyMaid(String maidKey, int layoutId, String userId){
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

        //initialize buffers
        mPageTitles = new ArrayList<>();
        mStartDates = new ArrayList<>();

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

        //get number of weeks to display
        int numberOfWeeks = 52;

        mDateRange = new String[numberOfWeeks][2];

        Date startDate;
        Date endDate;
        for(int i = 0; i < numberOfWeeks; i++){
            startDate = DateTimeHelper.getStartOfWeek(i);
            endDate = DateTimeHelper.getEndOfWeek(i);

            mDateRange[i][0] = DateTimeHelper.getDatestamp(startDate);
            mDateRange[i][1] = DateTimeHelper.getDatestamp(endDate);
            mStartDates.add(startDate);
            mPageTitles.add(DateTimeHelper.convertWeekRange(startDate, endDate));
        }
    }

    /*
     * void initializeVPMaids() - initialize maids used by viewPager component
     */
    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        int layoutId = R.layout.standard_recycler;

        //get number of pages to create
        int count = mPageTitles.size();

        //initialize maids
        for(int i = 0; i < count; i++){

            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_WEEK_VP + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeWeekViewMaid(maidKey, layoutId, mUserId, mDateRange[i],
                    mStartDates.get(i), i);
        }

    }

    /*
     * void initializeViewPager() - initialize viewPager component
     */
    private void initializeViewPager(){
        //initialize view pager
        new StandardStateViewPager(mFragment, mPageTitles, MaidRegistry.MAID_WEEK_VP);
    }


/**************************************************************************************************/

}
