package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.recycler.BasicRecycler;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * GymRatRecyclerKeeper extends GymRatBaseKeeper and adds initialization and event handle of a
 * recyclerView along with an "empty" textView component (shown when the recycler is empty) and a
 * floating action button (FAB) the displays when reaching the end of the recycler view list
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Variables from GymRatBaseKeeper:
 *      Boss mBoss - Boss application
 *      HomeToolbar mToolbar - toolbar component
 *      HomeDrawer mHomeDrawer - drawer navigation component
 *      int mToolbarMenuId - toolbar menu resource id
 *      int mBottomNavSelectedItemId - used to determine which menu item is selected in the drawer
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
 *
 */
/**************************************************************************************************/

public abstract class GymRatRecyclerKeeper extends GymRatBaseKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      TextView mTxtEmpty - textView component displayed when recycler is empty
 *      BasicRecycler mBasicRecycler - recycler component
 *      mProgressBar - progress bar component
 *      FloatingActionButton mFAB - floating action button component
 */
/**************************************************************************************************/

    //mTxtEmpty - textView component displayed when recycler is empty
    protected TextView mTxtEmpty;

    //mBasicRecycler - recycler component
    protected BasicRecycler mBasicRecycler;

    //mProgressBar - progress bar component
    protected ProgressBar mProgressBar;

    //mFAB - floating action button component
    protected FloatingActionButton mFAB;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Activity Lifecycle Methods
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
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

    }

    public void start(){
        super.start();

        if(mIsAuth){
            //get user id from Boss
            if(!mInitialized){
                //initialize recycler components
                initializeRecyclerComponents();
            }
        }
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeRecyclerComponents() - initialize recycler and related components
 */
/**************************************************************************************************/
    /*
     * void initializeRecyclerComponents() - initialize recycler and related components
     */
    private void initializeRecyclerComponents(){
        //initialize recycler component
        mBasicRecycler = new BasicRecycler(mActivity);

        //initialize "empty" textView component
        int emptyViewId = R.id.choiceEmptyView;
        mTxtEmpty = (TextView) mActivity.findViewById(emptyViewId);

        //initialize progressbar component
        int progressBarId = R.id.choiceProgressBar;
        mProgressBar = (ProgressBar) mActivity.findViewById(progressBarId);

        //initialize floating action button component
        int fabId = R.id.choiceFab;
        mFAB = (FloatingActionButton) mActivity.findViewById(fabId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void setEmptyMessage(String) - set "empty" message to be displayed when recycler is empty
 *      void setOnClickFABListener(View.OnClickListener) - set onClick listener for FAB
 *      void showProgressBar(boolean) - show or hide progressbar
 *      void checkForEmptyRecycler(boolean) - checks whether to display "empty" message or not
 *      void checkForEmptyRecycler(int) - checks whether to display "empty" message or not
 */
/**************************************************************************************************/
    /*
     * void setEmptyMessage(String) - set "empty" message to be displayed when recycler is empty
     */
    protected void setEmptyMessage(String msg){
        mTxtEmpty.setText(msg);
    }

    /*
     * void setOnClickFABListener(View.OnClickListener) - set onClick listener for FAB
     */
    protected void setOnClickFABListener(View.OnClickListener listener){
        mFAB.setOnClickListener(listener);
    }

    /*
     * void showProgressBar(boolean) - show or hide progressbar
     */
    protected void showProgressBar(boolean show){
        if(show){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /*
     * void isEmptyRecycler(boolean) - checks whether to display "empty" message or not
     */
    protected void isEmptyRecycler(boolean isEmpty){
        if(isEmpty){
            mTxtEmpty.setVisibility(View.VISIBLE);
        }
        else{
            mTxtEmpty.setVisibility(View.GONE);
        }
    }

/**************************************************************************************************/




}
