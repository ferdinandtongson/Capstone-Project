package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.ExerciseNav;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  TODO - housekeeper description
 *  TODO - create activity class
 *  TODO - create activity layout
 *  TODO - define housekeeper id in res/values/strings.xml
 *  <!-- HouseKeeper Names -->
 *  <string name="housekeeper_main" translatable="false">Main HouseKeeper</string>
 *  TODO - initialize and register housekeeper in Boss class
 *  TODO - handle saved instance states from bundle
 *  TODO - initialize mobile layout components
 *  TODO - initialize activity components, for example
 *  TODO - initialize tablet layout components
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * TODO - housekeeper description,
 * Each housekeeper is responsible for an activity. It communicates directly with Boss, Activity
 * and Maids maintaining fragments within the Activity, if any.
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Methods from MyHouseKeeper
 *      void create(MyActivity,Bundle) - called by onCreate(Bundle) in the activity lifecycle
 *      boolean isTablet() - return device flag if device is tablet or not
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
 *
 */

/**************************************************************************************************/

public class StubAppointmentKeeper extends GymRatBaseKeeper implements MyActivity.Bridge {

/**************************************************************************************************/
/*
 * Class Variables:
 *      mBoss - Boss application
 */
/**************************************************************************************************/


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * _templateKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * _templateKeeper - constructor
     * @param layoutId - layout resource id used by Keeper
     */
    public StubAppointmentKeeper(int layoutId){

        //get layout id
        mActivityLayoutId = layoutId;
        mSelectedNavItemId = R.id.nav_appointments;
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

        initializeLayout();

    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //TODO - handle saved instance states from bundle
        //set saved instance state data
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize ui
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize ui for mobile device
     */
    private void initializeLayout(){
        initializeMaid();
        initializeBottomNavigation();

    }

    private void initializeMaid(){
        MaidRegistry maidRegistry = MaidRegistry.getInstance();
        int layoutId = R.layout.standard_recycler_fab;

        maidRegistry.initializeExerciseMaid(MaidRegistry.MAID_EXERCISE, layoutId);
    }

    private void initializeBottomNavigation(){

        //create bottom navigator
        ExerciseNav nav = new ExerciseNav(mActivity);
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
