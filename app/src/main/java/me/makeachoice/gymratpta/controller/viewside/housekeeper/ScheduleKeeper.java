package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.ScheduleNav;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_WARNING_DELETE;

/**************************************************************************************************/
/*
 * TODO - Need to handle "Quick Help" request event from toolbar menu option
 *      todo - get bottom navigation status to know which screen is being displayed (day or week)
 *      todo - set "quick help" listener here (keeper) or at maid level???
 *      todo - create "quick help" dialog
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ScheduleKeeper is responsible for maintaining the Schedule screen. It is directly responsible
 * for handling toolbar and drawer events (see GymRatBaseKeeper), bottom navigation events (see
 * ScheduleNav) and the creation of DailyMaid and WeeklyMaid.
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

public class ScheduleKeeper extends GymRatBaseKeeper implements MyActivity.Bridge {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private String mUserId;
    private String mStrSchedule;
    private String mStrDaily;
    private String mStrWeekly;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ScheduleKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * ScheduleKeeper - constructor
     */
    public ScheduleKeeper(int layoutId){

        //get layout id
        mActivityLayoutId = layoutId;

        mToolbarMenuId = R.menu.toolbar_menu;
        mBottomNavSelectedItemId = R.id.nav_appointments;

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

        mStrSchedule = mActivity.getString(R.string.schedule);
        mStrDaily = mActivity.getString(R.string.daily);
        mStrWeekly = mActivity.getString(R.string.weekly);

        mBoss.initializeFirebaseAuth(new Boss.OnSignedInListener() {
            @Override
            public void onSignedIn(String userId) {
                //get user id from Boss
                mUserId = mBoss.getUserId();

                //initialize layout
                initializeLayout();

            }
        });

    }

    public void pause(){
        super.pause();
        FragmentManager fm = mActivity.getSupportFragmentManager();

        ArrayList<String> tagList = new ArrayList<>();
        tagList.add(DIA_SCHEDULE);
        tagList.add(DIA_WARNING_DELETE);

        String tag;
        DialogFragment dia;
        int count = tagList.size();
        for(int i = 0; i < count; i++){
            tag = tagList.get(i);

            dia = (DialogFragment)fm.findFragmentByTag(tag);
            if (dia != null) { dia.dismiss(); }
        }
    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    private void openBundle(Bundle bundle){
        //TODO - handle saved instance states from bundle
        //set saved instance state data
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

        mHomeToolbar.setGymRatToolbarTitle(mStrSchedule, mStrDaily);

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
        int pagerId = R.layout.viewpager;
        maidRegistry.initializeDailyMaid(MaidRegistry.MAID_DAY, pagerId, mUserId);
        maidRegistry.initializeWeeklyMaid(MaidRegistry.MAID_WEEK, pagerId, mUserId);
    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation for the appointment screen
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        ScheduleNav appointmentNav = new ScheduleNav(mActivity);
        appointmentNav.setOnNavSelectedListener(new ScheduleNav.OnNavSelectedListener() {
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

    public void onBottomNavigationSelected(int navId){
        switch (navId) {
            case bottom_nav_item1: // 0 - scheduled appointments for a day
                mHomeToolbar.setGymRatToolbarTitle(mStrSchedule, mStrDaily);
                break;
            case bottom_nav_item2: // 1 - scheduled appointments for a week
                mHomeToolbar.setGymRatToolbarTitle(mStrSchedule, mStrWeekly);
                break;
        }

    }

/**************************************************************************************************/

}
