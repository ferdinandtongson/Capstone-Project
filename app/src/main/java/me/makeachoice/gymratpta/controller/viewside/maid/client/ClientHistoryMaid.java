package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardStateViewPager;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
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

public class ClientHistoryMaid extends MyMaid implements BasicFragment.Bridge {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public final static int MAID_SESSION_INDEX = 0;
    public final static int MAID_STATS_INDEX = 1;
    public final static int MAID_NOTES_INDEX = 2;

    //mPageTitles - list of titles used by viewPager tabLayout
    private ArrayList<String> mPageTitles;

    private String mUserId;
    private ClientItem mClientItem;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientHistoryMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientHistoryMaid(...) - constructor
     */
    public ClientHistoryMaid(String maidKey, int layoutId){
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

        Boss boss = (Boss)mActivity.getApplication();

        //get user id from Boss
        mUserId = boss.getUserId();
        mClientItem = boss.getClient();

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
        new StandardStateViewPager(mFragment, mPageTitles, MaidRegistry.MAID_CLIENT_HISTORY_VP);

    }

    private void loadData(){
        mPageTitles = new ArrayList();

        String strSessions = mActivity.getString(R.string.sessions);
        String strStats = mActivity.getString(R.string.stats);
        String strNotes = mActivity.getString(R.string.notes);
        mPageTitles.add(strSessions);
        mPageTitles.add(strStats);
        mPageTitles.add(strNotes);
    }

    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        //int layoutId = R.layout.standard_recycler_fab;
        int layoutId = R.layout.standard_recycler_fab;

        String maidSessionId = MaidRegistry.MAID_CLIENT_HISTORY_VP + MAID_SESSION_INDEX;
        String maidStatsId = MaidRegistry.MAID_CLIENT_HISTORY_VP + MAID_STATS_INDEX;
        String maidNotesId = MaidRegistry.MAID_CLIENT_HISTORY_VP +MAID_NOTES_INDEX;

        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        int noFabId = R.layout.standard_recycler;

        maidRegistry.initializeClientHistorySessionMaid(maidSessionId, noFabId, mUserId, mClientItem);
        maidRegistry.initializeClientHistoryStatsMaid(maidStatsId, noFabId, mUserId, mClientItem);
        maidRegistry.initializeClientHistoryNotesMaid(maidNotesId, noFabId, mUserId, mClientItem);

    }


/**************************************************************************************************/

}
