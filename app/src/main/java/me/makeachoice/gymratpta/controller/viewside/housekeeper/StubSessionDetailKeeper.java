package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.util.Log;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.SessionDetailNav;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * TODO - need to add accessibility values to bottom navigation menu items
 *          todo - add content descriptions to menu items
 *          todo - add d-pad navigation
 * TODO - need to style components
 *          todo - bottom navigation
 */
/**************************************************************************************************/

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

public class StubSessionDetailKeeper extends GymRatBaseKeeper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private String mUserId;
    private ClientItem mClientItem;
    private AppointmentItem mAppointmentItem;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StubSessionDetailKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * StubSessionDetailKeeper - constructor
     */
    public StubSessionDetailKeeper(int layoutId){
        //get layout id from HouseKeeper Registry
        mActivityLayoutId = layoutId;

        //set toolbar menu resource id consumed by HomeToolbar
        mToolbarMenuId = R.menu.toolbar_menu;

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

        initializeLayout();
    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //set saved instance state data
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize maids and bottom navigation
 *      void initializeMaid() - initialize DayMaid and WeekMaid
 *      void initializeBottomNavigation - initialize bottom navigation for the appointment screen
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize maids and bottom navigation
     */
    private void initializeLayout(){
        //initialize maids
        initializeMaid();

        //initialize bottom navigation
        initializeBottomNavigation();
    }

    /*
     * void initializeMaid() - initialize DayMaid and WeekMaid
     */
    private void initializeMaid(){
        Log.d("Choice", "SessionDetail.initializeMaid");
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        //viewPager layout used by exercise maid
        int recyclerId = R.layout.standard_recycler_fab;
        maidRegistry.initializeSessionRoutineMaid(MaidRegistry.MAID_SESSION_ROUTINE, recyclerId);

        int pagerId = R.layout.viewpager;
        maidRegistry.initializeSessionStatsMaid(MaidRegistry.MAID_SESSION_STATS, pagerId);
        maidRegistry.initializeClientNotesMaid(MaidRegistry.MAID_SESSION_NOTES, recyclerId, mUserId,
                mClientItem, mAppointmentItem);
    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation for the appointment screen
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        new SessionDetailNav(mActivity);
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

/**************************************************************************************************/


}
