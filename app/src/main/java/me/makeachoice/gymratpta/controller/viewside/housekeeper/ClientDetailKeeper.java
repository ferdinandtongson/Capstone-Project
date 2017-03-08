package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.ClientDetailNav;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item3;

/**************************************************************************************************/
/*
 * ClientDetailKeeper is responsible for maintaining the Client Detail screens. It is directly
 * responsible for handling toolbar and drawer events (see GymRatBaseKeeper), bottom navigation
 * events (see ClientDetailNav) and the creation of ClientInfo, ClientSchedule and ClientHistory maids.
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Variables from GymRatBaseKeeper:
 *      mBoss - Boss application
 *      mHomeDrawer - drawer navigation component
 *      mSelectedNavItemId - used to determine which menu item is selected in the drawer
 *
 * Methods from MyHouseKeeper
 *      void create(MyActivity,Bundle) - called by onCreate(Bundle) in the activity lifecycle
 *      boolean isTablet() - return device flag if device is tablet or not
 *
 * Methods from GymRatBaseKeeper
 *      void initializeNavigation() - initialize navigation ui
 *      void initializeToolbar() - initialize toolbar component
 *      void initializeDrawer() - initialize drawer navigation component
 *
 * MyHouseKeeper Implements MyActivity.Bridge
 *      void start() - called by onStart() in the activity lifecycle
 *      void resume() - called by onResume() in the activity lifecycle
 *      void pause() - called by onPause() in the activity lifecycle
 *      void stop() - called by onStop() in the activity lifecycle
 *      void destroy() - called by onDestroy() in the activity lifecycle
 *      void backPressed() - called by onBackPressed() in the activity
 *      void saveInstanceState(Bundle) - called before onDestroy(), save state to bundle
 *      void activityResult(...) - result of Activity called by this Activity
 */
/**************************************************************************************************/

public class ClientDetailKeeper extends GymRatBaseKeeper{

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private ClientItem mClientItem;
    private String mStrInfo;
    private String mStrSchedule;
    private String mStrHistory;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientDetailKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * ClientDetailKeeper - constructor
     */
    public ClientDetailKeeper(int layoutId){
        //get layout id from HouseKeeper Registry
        mActivityLayoutId = layoutId;

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Activity Lifecycle Methods
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
 */
/**************************************************************************************************/
    /*
     * void create(MyActivity, Bundle) - called when onCreate is called in the activity. Sets the
     * activity layout, fragmentManager and other child views of the activity
     *
     * NOTE: Both FragmentManager and FAB are context sensitive and need to be recreated every time
     * onCreate() is called in the Activity
     * @param activity - current activity being shown
     * @param bundle - instant state values
     */
    public void create(MyActivity activity, Bundle bundle){
        super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        mClientItem = mBoss.getClient();

        mStrInfo = mActivity.getString(R.string.client_info);
        mStrSchedule = mActivity.getString(R.string.client_schedule);
        mStrHistory = mActivity.getString(R.string.client_history);

    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //set saved instance state data
    }

    public void start(){
        super.start();
        if(mIsAuth){
            if(!mInitialized){
                mInitialized = true;
                initializeLayout();
            }
        }
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize maids and bottom navigation
 *      void initializeMaid() - initialize DailyMaid and WeeklyMaid
 *      void initializeBottomNavigation - initialize bottom navigation for the appointment screen
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize maids and bottom navigation
     */
    private void initializeLayout(){
        String title = mStrInfo;
        String subtitle = mClientItem.clientName;
        mHomeToolbar.setGymRatToolbarTitle(title, subtitle);

        //initialize maids
        initializeMaid();

        //initialize bottom navigation
        initializeBottomNavigation();
    }

    /*
     * void initializeMaid() - initialize DailyMaid and WeeklyMaid
     */
    private void initializeMaid(){
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        int infoId = R.layout.fragment_client_info;
        maidRegistry.initializeClientInfoMaid(MaidRegistry.MAID_CLIENT_INFO, infoId, mClientItem);

        int scheduleId = R.layout.standard_recycler_fab;
        maidRegistry.initializeClientScheduleMaid(MaidRegistry.MAID_CLIENT_SCHEDULE, scheduleId, mClientItem);

        //viewPager layout used by exercise maid
        int pagerId = R.layout.viewpager_fixed;
        maidRegistry.initializeClientHistoryMaid(MaidRegistry.MAID_CLIENT_HISTORY, pagerId);
    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation for the appointment screen
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        ClientDetailNav nav = new ClientDetailNav(mActivity);
        nav.setOnNavSelectedListener(new ClientDetailNav.OnNavSelectedListener() {
            @Override
            public void onNavSelected(int navId) {
                onBottomNavigationSelected(navId);
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void backPressed() - called when Activity.onBackPressed is called
 */
/**************************************************************************************************/
    /*
     * void backPressed() - called when Activity.onBackPressed is called
     */
    @Override
    public void backPressed(){
    }

    /*
     * void onBottomNavigationSelected(int) - bottom navigation item has been selected
     */
    public void onBottomNavigationSelected(int navId){
        String subtitle = mClientItem.clientName;
        String title;
        switch (navId) {
            case bottom_nav_item1: // 0 - session routine
                title = mStrInfo;
                mHomeToolbar.setGymRatToolbarTitle(title, subtitle);
                break;
            case bottom_nav_item2: // 1 - session stats
                title = mStrSchedule;
                mHomeToolbar.setGymRatToolbarTitle(title, subtitle);
                break;
            case bottom_nav_item3: // 1 - session notes
                title = mStrHistory;
                mHomeToolbar.setGymRatToolbarTitle(title, subtitle);
                break;
        }

    }


/**************************************************************************************************/

}
