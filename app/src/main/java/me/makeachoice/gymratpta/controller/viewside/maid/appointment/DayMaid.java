package me.makeachoice.gymratpta.controller.viewside.maid.appointment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardStateViewPager;
import me.makeachoice.gymratpta.model.item.ClientCardItem;
import me.makeachoice.gymratpta.model.stubData.SessionStubData;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DAY_MAX;

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

public class DayMaid extends MyMaid implements BasicFragment.Bridge{

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
 * DayMaid - constructor
 */
/**************************************************************************************************/
    /*
     * DayMaid(...) - constructor
     */
    public DayMaid(String maidKey, int layoutId, String userId){
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

        mActivity = (MyActivity)mFragment.getActivity();

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


    }

    private MyActivity mActivity;
    private ArrayList<String> mDates;

    private void loadData(){
        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        int numberOfDays = prefs.getInt(PREF_DAY_MAX, 30);

        mPageTitles = new ArrayList<>();
        mDates = new ArrayList<>();

        // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        Calendar c = Calendar.getInstance();
        String day = sdf.format(c.getTime());
        String appointmentDate = dateFormat.format(c.getTime());

        mPageTitles.add(day);
        mDates.add(appointmentDate);

        for(int i = 0; i < numberOfDays; i++){
            c.add(Calendar.DATE, 1);
            day = sdf.format(c.getTime());
            appointmentDate = dateFormat.format(c.getTime());
            mPageTitles.add(day);
            mDates.add(appointmentDate);
        }

        //initialize maids used by viewPager
        initializeVPMaids();

        //initialize view pager
        new StandardStateViewPager(mFragment, mPageTitles, MaidRegistry.MAID_DAY_VP);
    }

    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        //int layoutId = R.layout.standard_recycler_fab;
        int layoutId = R.layout.standard_recycler_fab;

        int count = mPageTitles.size();

        //initialize maids
        for(int i = 0; i < count; i++){
            String strDate = mDates.get(i);

            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_DAY_VP + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeDayViewPagerMaid(maidKey, layoutId, mUserId, strDate, i);
        }

    }


/**************************************************************************************************/

}
