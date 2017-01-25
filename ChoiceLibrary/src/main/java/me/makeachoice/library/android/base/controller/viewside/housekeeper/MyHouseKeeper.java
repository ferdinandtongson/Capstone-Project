package me.makeachoice.library.android.base.controller.viewside.housekeeper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import me.makeachoice.library.android.base.R;
import me.makeachoice.library.android.base.controller.MyBoss;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  TODO - look and test configuration changes
 *  Configuration Change: change in orientation, language, input devices, etc causes current activity
 *      to be destroyed, going through onPuase(), onStop(), and onDestroy()
 *  TODO - add tablet layout and default _ID values for tablet components
 *  TODO - handle tablet layout components and initialization
 *  TODO - currently tablet and phone orientations are fixed to landscape and portrait respectively
 */
/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Abstract MyHouseKeeper - HouseKeeper class is the main class that manages Activity lifecycle
 * calls and top-level Activity components.
 */

/**************************************************************************************************/

public abstract class MyHouseKeeper {

/**************************************************************************************************/
/*
 * Class Variables:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *
 *      MyActivity mActivity - current Activity
 *      MyBoss mBoss - application context object
 *
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 */
/**************************************************************************************************/

    //TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
    protected static final int TABLET_LAYOUT_ID = R.id.choiceTabletLayout;

    //mActivity - current Activity
    protected MyActivity mActivity;

    //mLayoutId - resource layout id
    protected int mActivityLayoutId;

    //mIsTablet - boolean flag used to determine if device is a tablet
    protected boolean mIsTablet;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void create(MyActivity, Bundle) - called when onCreate is called in the activity
 *      boolean deviceIsTablet() - determines if user device is tablet
 *      boolean isTablet() - return device flag if device is tablet or not
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
        //save Activity to class variable
        mActivity = activity;

        //set activity layout
        mActivity.setContentView(mActivityLayoutId);

        //check if device is a tablet
        mIsTablet = deviceIsTablet();

    }


    /*
     * boolean deviceIsTablet() - sets flag if device is tablet or not
     */
    private boolean deviceIsTablet(){
        //TODO - allow user option to adjust screen orientation, currently fixed
        //get tablet view layout
        View tabletView = mActivity.findViewById(TABLET_LAYOUT_ID);

        //check if tablet view is valid
        if(tabletView != null){
            //set tablet orientation to landscape mode only
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            return true;
        }
        else{
            //set phone orientation to portrait mode only
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            return false;
        }
    }

    /*
     * boolean isTablet() - return device flag if device is tablet or not
     */
    protected boolean isTablet(){
        return mIsTablet;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyActivity.Bridge Interface Implementation:
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
    /*
     * void start() - called when activity is visible to user. Place to register broadcast receivers
     */
    public void start(){
        //activity is visible, place to register broadcast receivers if any
    }

    /*
     * void resume() - called when user can start interacting with activity. Place to begin animation
     *      open exclusive-access devices (such as cameras) and restore activity state, if any.
     */
    public void resume(){
        //user can interact with activity, begin animation, restore state or open exclusive-access
        //devices
    }

    /*
     * void pause() - called when activity is going into the background. Place to do lightweight
     *      operations, new activity will not be created until this activity completes onPause(), such
     *      as saving persistent state or stopping animations or other things to make the switch to
     *      the next activity as fast as possible.
     */
    public void pause(){
        //should be lightweight, save persistent state to bundle, stop animations
    }

    /*
     * void stop() - called when activity is no longer visible to user. Place to unregister broadcast
     *      receivers.
     */
    public void stop(){
        //activity no longer visible, unregister broadcast receivers
    }

    /*
     * void destroy() - called by finish() or system is saving space. Use for any final cleanup
     */
    public void destroy(){
        //final cleanup
    }

    /*
     * void backPressed() - called when onBackPressed() is called in the Activity.
     */
    public void backPressed(){
        //finish activity
        mActivity.finishActivity();
    }

    /*
     * void saveInstanceState(Bundle) - called before onDestroy(), save state to bundle
     */
    public void saveInstanceState(Bundle bundle){
        //save instance data before onDestroy is called
    }

    /*
     * void activityResult(...) - result of Activity called by this Activity
     */
    public void activityResult(int requestCode, int resultCode, Intent data){
        //get result of Activity
    }

/**************************************************************************************************/

}
