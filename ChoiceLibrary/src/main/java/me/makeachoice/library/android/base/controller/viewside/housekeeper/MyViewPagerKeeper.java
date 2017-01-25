package me.makeachoice.library.android.base.controller.viewside.housekeeper;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import me.makeachoice.library.android.base.R;
import me.makeachoice.library.android.base.controller.viewside.bartender.MyBartender;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * TODO -  need to be able to handle tablet devices
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewPagerKeeper Abstract class extends MyHouseKeeper class. It contains a toolbar and a
 * viewPager layout.
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      MyBoss mBoss - application context object
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Methods from MyHouseKeeper
 *      Context getActivityContext() - return current Activity Context
 *      boolean deviceIsTablet() - determines if user device is tablet
 *
 * Implements MyActivity.Bridge
 *      void start() - called by onStart() in the activity lifecycle
 *      void resume() - called by onResume() in the activity lifecycle
 *      void pause() - called by onPause() in the activity lifecycle
 *      void stop() - called by onStop() in the activity lifecycle
 *      void destroy() - called by onDestroy() in the activity lifecycle
 *      void backPressed() - called by onBackPressed() in the activity
 *      void saveInstanceState(Bundle) - called before onDestroy(), save state to bundle
 */

/**************************************************************************************************/

public abstract class MyViewPagerKeeper extends MyHouseKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int DEFAULT_LAYOUT_ID - default viewPager layout resource id
 *      int DEFAULT_TOOLBAR_ID - default toolbar resource id
 *      int DEFAULT_VIEW_PAGER_ID - default viewPager resource id
 *
 *      mLayoutId - buffer for layout resource id number
 *      mViewPagerId - buffer for viewPager resource id number
 *      MyBartender mBartender - manages Toolbar for this activity
 *      ViewPager mViewPager - viewPager component
 */
/**************************************************************************************************/

    //DEFAULT_LAYOUT_ID - default swipeRefresh layout resource id
    protected static final int DEFAULT_LAYOUT_ID = R.layout.activity_view_pager;

    //DEFAULT_TOOLBAR_ID - default toolbar resource id
    protected static final int DEFAULT_TOOLBAR_ID = R.id.choiceToolbar;

    //DEFAULT_VIEW_PAGER_ID - default viewPager resource id
    protected static final int DEFAULT_VIEW_PAGER_ID = R.id.choiceViewPager;

    //mBartender - manages Toolbar for this activity
    protected MyBartender mBartender;

    //mViewPager - viewPager component
    protected ViewPager mViewPager;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods
 *      void initializeViewPager() - initialize ViewPager with default id
 *      void initializeViewPager(int) - initialize ViewPager with user defined id
 */
/**************************************************************************************************/
    /*
     * void initializeViewPager() - initialize ViewPager
     */
    protected void initializeViewPager(){
        //get ViewPager component
        initializeViewPager(DEFAULT_VIEW_PAGER_ID);
    }

    /*
     * void initializeViewPager(int) - initialize ViewPager
     * @param id - ViewPager component resource id
     */
    protected void initializeViewPager(int id){
        //get ViewPager component
        mViewPager = (ViewPager)mActivity.findViewById(id);
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Abstract Methods
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
 *      void initializeBartender() - initialize bartender
 */
/**************************************************************************************************/
    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create()
     */
    protected abstract void openBundle(Bundle bundle);

    /*
     * void initializeBartender() - initialize bartender
     */
    protected abstract void initializeBartender();

/**************************************************************************************************/

}
