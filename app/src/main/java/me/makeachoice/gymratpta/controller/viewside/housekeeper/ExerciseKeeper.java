package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.util.Log;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.ClientDetailNav;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.ExerciseNav;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;

/**************************************************************************************************/
/*
 * AppointmentKeeper is responsible for maintaining the Appointment screen. It is directly responsible
 * for handling toolbar and drawer events (see GymRatBaseKeeper), bottom navigation events (see
 * ScheduleNav) and the creation of ExerciseMaid and RoutineMaid.
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

public class ExerciseKeeper extends GymRatBaseKeeper implements MyActivity.Bridge {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseKeeper - constructor
     */
    public ExerciseKeeper(int layoutId){

        //get layout id
        mActivityLayoutId = layoutId;

        mBottomNavSelectedItemId = R.id.nav_exercises;
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

    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //TODO - handle saved instance states from bundle
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
 *      void initializeMaid() - initialize ExerciseMaid and RoutineMaid
 *      void initializeBottomNavigation - initialize bottom navigation for the exercise screen
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
     * void initializeMaid() - initialize ExerciseMaid and RoutineMaid
     */
    private void initializeMaid(){
        //get maid registry
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        //viewPager layout used by exercise maid
        int layoutId = R.layout.viewpager;

        //initialize exercise maid
        maidRegistry.initializeExerciseMaid(MaidRegistry.MAID_EXERCISE, layoutId, mBoss.getUserId());

        //layout used by routine maid
        int routineId = R.layout.standard_recycler_fab;
        maidRegistry.initializeRoutineMaid(MaidRegistry.MAID_ROUTINE, routineId, mBoss.getUserId());
    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation for the appointment screen
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        ExerciseNav nav = new ExerciseNav(mActivity);
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
        switch (navId) {
            case bottom_nav_item1: // 0 - exercise
                //does nothing
                break;
            case bottom_nav_item2: // 1 - routine
                //does nothing
                break;
        }

    }

/**************************************************************************************************/




}
