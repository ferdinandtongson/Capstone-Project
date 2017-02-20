package me.makeachoice.gymratpta.controller.viewside.maid.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardStateViewPager;
import me.makeachoice.gymratpta.model.item.ClientCardItem;
import me.makeachoice.gymratpta.model.stubData.SessionStubData;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * TODO - add description
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

public class WeekMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mPageTitles - list of titles used by viewPager tabLayout
    ArrayList<String> mPageTitles;

    private String mUserId;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * WeekMaid - constructor
 */
/**************************************************************************************************/
    /*
     * WeekMaid(...) - constructor
     */
    public WeekMaid(String maidKey, int layoutId, String userId){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

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
        //load category and exercise data
        loadData();

        //initialize maids used by viewPager
        initializeVPMaids();

        //initialize view pager
        new StandardStateViewPager(mFragment, mPageTitles, MaidRegistry.MAID_DAY_VP);
    }

    private void loadData(){
        mPageTitles = new ArrayList();

        // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);
        int year = c.get(Calendar.YEAR);
        mPageTitles.add(getStartEndOFWeek(week, year));

        for(int i = 0; i < 52; i++){
            c.add(Calendar.WEEK_OF_YEAR, 1);
            week = c.get(Calendar.WEEK_OF_YEAR);
            year = c.get(Calendar.YEAR);
            mPageTitles.add(getStartEndOFWeek(week, year));
        }

    }

    private String getStartEndOFWeek(int enterWeek, int enterYear){
        //enterWeek is week number
        //enterYear is year
        String startEndWeek;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd"); // PST`
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        startEndWeek = startDateInStr + " - ";

        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();
        String endDateInString = formatter.format(endDate);

        return startDateInStr + " - " + endDateInString;
    }

    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        //int layoutId = R.layout.standard_recycler_fab;
        int layoutId = R.layout.standard_recycler_fab;

        int count = mPageTitles.size();

        //initialize maids
        for(int i = 0; i < count; i++){
            //get exercise list
            ArrayList<ClientCardItem> clients = SessionStubData.createData(mFragment.getContext());

            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_DAY_VP + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeDayViewPagerMaid(maidKey, layoutId, mUserId, clients);
        }

    }

/**************************************************************************************************/

}
