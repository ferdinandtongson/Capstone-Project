package me.makeachoice.library.android.base.controller.viewside.housekeeper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

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
 * MySwipeRefreshKeeper Abstract class extends MyHouseKeeper class. It contains a toolbar, a
 * swipeRefresh layout and a recycler view.
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

public abstract class MySwipeRefreshKeeper extends MyHouseKeeper implements MyActivity.Bridge,
    SwipeRefreshLayout.OnRefreshListener{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int DEFAULT_LAYOUT_ID - default swipeRefresh layout resource id
 *      int DEFAULT_TOOLBAR_ID - default toolbar resource id
 *      int DEFAULT_SWIPE_REFRESH_ID - default swipeRefresh resource id
 *      int DEFAULT_RECYCLER_ID - default recycler resource id
 *
 *      MyBartender mBartender - manages Toolbar for this activity
 *      SwipeRefreshLayout mSwipeRefreshLayout - swipeRefresh component
 *      RecyclerView mRecycler - recycler component
 *      RecyclerView.LayoutManager mLayoutManager - layout manager for recyclerView
 */
/**************************************************************************************************/

    //DEFAULT_LAYOUT_ID - default swipeRefresh layout resource id
    protected static final int DEFAULT_LAYOUT_ID = R.layout.activity_swipe_refresh;

    //DEFAULT_TOOLBAR_ID - default toolbar resource id
    protected static final int DEFAULT_TOOLBAR_ID = R.id.choiceToolbar;

    //DEFAULT_SWIPE_REFRESH_ID - default swipeRefresh resource id
    protected static final int DEFAULT_SWIPE_REFRESH_ID = R.id.choiceSwipeRefresh;

    //DEFAULT_RECYCLER_ID - default recycler resource id
    protected static final int DEFAULT_RECYCLER_ID = R.id.choiceRecycler;

    //mBartender - manages Toolbar for this activity
    protected MyBartender mBartender;

    //mSwipeRefreshLayout - swipeRefresh component
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    //mRecycler - recycler component
    protected RecyclerView mRecyclerView;

    //mLayoutManager - layout manager for recyclerView
    protected RecyclerView.LayoutManager mLayoutManager;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods
 *      void initializeSwipeRefresh() - initialize SwipeRefreshLayout with default id
 *      void initializeSwipeRefresh(int) - initialize SwipeRefreshLayout with user defined id
 *      void initializeRecycler() - initialize RecyclerView with default id
 *      void initializeRecycler(int) - initialize RecyclerView with user defined id
 */
/**************************************************************************************************/
    /*
     * void initializeSwipeRefresh() - initialize SwipeRefreshLayout
     */
    protected void initializeSwipeRefresh(){
        //get SwipeRefreshLayout component
        initializeSwipeRefresh(DEFAULT_SWIPE_REFRESH_ID);
    }

    /*
     * void initializeSwipeRefresh(int) - initialize SwipeRefreshLayout
     * @param id - swipeRefreshLayout component resource id
     */
    protected void initializeSwipeRefresh(int id){
        //get SwipeRefreshLayout component
        mSwipeRefreshLayout = (SwipeRefreshLayout)mActivity.findViewById(id);

        //set SwipeRefreshLayout.onRefreshListener
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /*
     * void initializeRecycler() - initialize RecyclerView
     */
    protected void initializeRecycler(){
        //get RecyclerView component
        initializeRecycler(DEFAULT_RECYCLER_ID);

    }

    /*
     * void initializeRecycler(int) - initialize RecyclerView
     * @param id - recyclerView component resource id
     */
    protected void initializeRecycler(int id){
        //get RecyclerView component
        mRecyclerView = (RecyclerView)mActivity.findViewById(id);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
 *      void initializeBartender() - initialize bartender
 *      void onRefresh() - implements SwipeRefreshLayout.OnRefreshListener interface
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

    /*
     * void onRefresh() - implements SwipeRefreshLayout.OnRefreshListener interface
     */
    public abstract void onRefresh();

/**************************************************************************************************/

}
