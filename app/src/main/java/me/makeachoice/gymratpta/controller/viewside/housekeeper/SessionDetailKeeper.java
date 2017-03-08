package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.ScheduleNav;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.SessionDetailNav;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item3;

/**************************************************************************************************/
/*
 * SessionDetailKeeper is responsible for maintaining the Session Detail screens. It is directly
 * responsible for handling toolbar and drawer events (see GymRatBaseKeeper), bottom navigation
 * events (see ClientDetailNav) and the creation of SessionRoutine, SessionStats and Session Notes
 * maids.
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

public class SessionDetailKeeper extends GymRatBaseKeeper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private String mUserId;
    private ClientItem mClientItem;
    private ScheduleItem mAppointmentItem;
    private String mStrRoutine;
    private String mStrSession;
    private String mStrStats;
    private String mStrNotes;
    private String mStrToday;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * SessionDetailKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * SessionDetailKeeper - constructor
     */
    public SessionDetailKeeper(int layoutId){
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

        //get user id from Boss
        mUserId = mBoss.getUserId();
        mClientItem = mBoss.getClient();
        mAppointmentItem = mBoss.getAppointmentItem();

        mStrRoutine = mActivity.getString(R.string.routine);
        mStrSession = mActivity.getString(R.string.session);
        mStrStats = mActivity.getString(R.string.stats);
        mStrNotes = mActivity.getString(R.string.notes);
        mStrToday = mActivity.getString(R.string.today);

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
        String title = mStrSession + " - " + mClientItem.clientName;;
        String subtitle = mStrToday + " @" + mAppointmentItem.appointmentTime;
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

        //viewPager layout used by exercise maid
        int recyclerId = R.layout.standard_recycler_fab;
        maidRegistry.initializeClientRoutineMaid(MaidRegistry.MAID_SESSION_ROUTINE, recyclerId, mUserId,
                mClientItem, mAppointmentItem);

        int noFabId = R.layout.standard_recycler;
        maidRegistry.initializeClientStatsMaid(MaidRegistry.MAID_SESSION_STATS, noFabId, mUserId,
                mClientItem, mAppointmentItem);
        maidRegistry.initializeClientNotesMaid(MaidRegistry.MAID_SESSION_NOTES, noFabId, mUserId,
                mClientItem, mAppointmentItem);
    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation for the appointment screen
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        SessionDetailNav nav = new SessionDetailNav(mActivity);
        nav.setOnNavSelectedListener(new ScheduleNav.OnNavSelectedListener() {
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
 *      void onBottomNavigationSelected(int) - bottom navigation item has been selected
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
        String subtitle = mStrToday + " @" + mAppointmentItem.appointmentTime;;
        String title;
        switch (navId) {
            case bottom_nav_item1: // 0 - session routine
                title = mStrSession + " - " + mClientItem.clientName;
                mHomeToolbar.setGymRatToolbarTitle(title, subtitle);
                break;
            case bottom_nav_item2: // 1 - session stats
                title = mStrStats + " - " + mClientItem.clientName;
                mHomeToolbar.setGymRatToolbarTitle(title, subtitle);
                break;
            case bottom_nav_item3: // 1 - session notes
                title = mStrNotes + " - " + mClientItem.clientName;
                mHomeToolbar.setGymRatToolbarTitle(title, subtitle);
                break;
        }

    }

/**************************************************************************************************/


}
