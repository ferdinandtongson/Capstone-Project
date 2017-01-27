package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.viewside.drawer.HomeDrawer;
import me.makeachoice.gymratpta.controller.viewside.toolbar.HomeToolbar;
import me.makeachoice.library.android.base.controller.viewside.bartender.MyBartender;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * TODO - Need to add shared transition animation between toolbar and drawer component
 *          todo - components to animate are Gym Rat icon, title and subtitle
 * TODO - need to add accessibility values to toolbar and drawer menu items
 *          todo - add content descriptions to toolbar
 *                  todo - navigation icon, title, subtitle, options menu, menu items
 *          todo - add content descriptions to drawer
 *                  todo - navigation icon, title, subtitle, menu items
 *          todo - add d-pad navigation
 *                  todo - toolbar
 *                  todo - drawer
 * TODO - nee to add menu item click function for toolbar options menu
 *          todo - quick help functionality
 *          todo - user sign out functionality
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * GymRatBaseKeeper is the base HouseKeeper for all HouseKeeper classes used in the GymRat project. It's
 * main function is to handle the initialization and event handling of the toolbar and drawer
 * components
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

public abstract class GymRatBaseKeeper extends MyHouseKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      mBoss - Boss application
 *      mNavigationId - drawer navigation id
 */
/**************************************************************************************************/

    //mBoss - Boss application
    protected Boss mBoss;

    //mNavigationId - drawer navigation id
    protected int mNavigationId;

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

        //get Boss application
        mBoss = (Boss)mActivity.getApplication();

        //initialize navigation components
        initializeNavigation();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeNavigation() - initialize navigation ui
 */
/**************************************************************************************************/
    /*
     * void initializeNavigation() - initialize navigation ui
     */
    private void initializeNavigation(){
        //initialize toolbar component
        initializeToolbar();

        //initialize drawer and navigation components
        initializeNavigationView();
    }

    /*
     * void initializeToolbar() - initialize toolbar component
     */
    private void initializeToolbar() {

        //create toolbar component
        HomeToolbar toolbar = new HomeToolbar(mActivity);
        toolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //react to click
                mHomeDrawer.openDrawer();

            }
        });

        toolbar.setOnMenuItemClick(new MyBartender.OnMenuItemClick() {
            @Override
            public void onMenuItemClick(MenuItem menuItem) {
                Log.d("Choice", "StudAppointmentKeeper.onMenuItemClick");
                //TODO - handle toolbar options menu item click
            }
        });

        View navigationIcon = toolbar.getToolbarNavigationIcon();

        //check api level is 21 or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO - this is used for creating a transition from toolbar to drawer
            String transitionName = mActivity.getResources().getString(R.string.trans_navIcon);
            navigationIcon.setTransitionName(transitionName);
        }

        //set bartender as options menu bridge
        mActivity.setOptionsMenuBridge(toolbar);

    }

    HomeDrawer mHomeDrawer;
    private void initializeNavigationView(){
        mHomeDrawer = new HomeDrawer(mActivity);
        mHomeDrawer.setNavigationItemChecked(mNavigationId);
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
