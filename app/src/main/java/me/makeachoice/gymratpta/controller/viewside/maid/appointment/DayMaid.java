package me.makeachoice.gymratpta.controller.viewside.maid.appointment;

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
import me.makeachoice.gymratpta.controller.viewside.viewpager.ExerciseViewPager;
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

public class DayMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mPageTitles - list of titles used by viewPager tabLayout
    ArrayList<String> mPageTitles;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DayMaid - constructor
 */
/**************************************************************************************************/
    /*
     * DayMaid(...) - constructor
     */
    public DayMaid(String maidKey, int layoutId){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;
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
        String today = sdf.format(c.getTime());
        mPageTitles.add(today);

        for(int i = 0; i < 30; i++){
            c.add(Calendar.DATE, 1);
            String day = sdf.format(c.getTime());
            mPageTitles.add(day);
        }

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
            maidRegistry.initializeDayViewPagerMaid(maidKey, layoutId, clients);
        }

    }


/**************************************************************************************************/

}
