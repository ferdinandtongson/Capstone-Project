package me.makeachoice.gymratpta.controller.viewside.maid.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardStateViewPager;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardViewPager;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * TODO - add description
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
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

public class SessionNotesMaid extends MyMaid implements BasicFragment.Bridge {

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
 * SessionNotesMaid - constructor
 */
/**************************************************************************************************/
    /*
     * SessionNotesMaid(...) - constructor
     */
    public SessionNotesMaid(String maidKey, int layoutId){
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
        new StandardViewPager(mFragment, mPageTitles, MaidRegistry.MAID_SESSION_NOTES_VP);

    }

    private void loadData(){
        mPageTitles = new ArrayList();

        mPageTitles.add("Subjective");
        mPageTitles.add("Objective");
        mPageTitles.add("Assessment");
        mPageTitles.add("Plan");
    }

    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        //int layoutId = R.layout.standard_recycler_fab;
        int layoutId = R.layout.fragment_notes_detail;

        int count = mPageTitles.size();

        //initialize maids
        for(int i = 0; i < count; i++){
            //get exercise list
            //ArrayList<ClientCardItem> clients = SessionStubData.createData(mFragment.getContext());

            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_SESSION_NOTES_VP + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeSessionStatsViewPagerMaid(maidKey, layoutId);
        }

    }


/**************************************************************************************************/

}
