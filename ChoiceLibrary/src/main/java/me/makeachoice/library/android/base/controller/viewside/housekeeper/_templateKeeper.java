package me.makeachoice.library.android.base.controller.viewside.housekeeper;

import android.os.Bundle;
import android.util.Log;

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

public class _templateKeeper extends MyHouseKeeper{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int KEEPER_ID - string resource id used as id number for HouseKeeper
 */
/**************************************************************************************************/

    //TODO - define housekeeper id
    //KEEPER_ID - string resource id used as id number for HouseKeeper
    //public final static int KEEPER_ID = R.string.housekeeper_detail;

    //TODO - define Boss
    //mBoss - Boss application
    //private Boss mBoss;

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
    public _templateKeeper(int layoutId){

        //get layout id
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
        //TODO - uncomment after Boss is defined
        /*super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        //get Boss application
        mBoss = (Boss)mActivity.getApplication();

        //notify boss if Tablet
        mBoss.setIsTablet(isTablet());

        if (isTablet()) {
            initializeForTabletLayout();
        }else{
            initializeForMobileLayout();
        }*/

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
 *      void initializeForMobileLayout(MyActivity) - initialize ui for mobile device
 *      void initializeForTabletLayout() - initialize ui for tablet device
 *      void initializeTabletDetailLayout(MyActivity) - initialize detail ui for tablet device
 */
/**************************************************************************************************/
    /*
     * void initializeForMobileLayout() - initialize ui for mobile device
     */
    private void initializeForMobileLayout(){
        //TODO - initialize mobile layout components, maybe check for network availability
        Log.d("Choice", "MainKeeper.initMobile");
        //TODO - initialize bottom navigator bar, if necessary
        //TODO - initialize ViewPager, Recycler, FragmentManager
        //TODO - initialize any other view components
    }

    /*
     * void initializeForTabletLayout() - initialize ui for tablet device
     */
    private void initializeForTabletLayout(){
        Log.d("Choice", "MainKeeper.initTablet");
        //TODO - initialize tablet layout components, this will only be called if this housekeeper
        //is maintaining a master activity since this is called during the activities onCreate().
        //See initializeTabletLayout() for when the housekeeper is maintaining a detail activity

    }

    /*
     * void initializeTabletDetailLayout(MyActivity) - initialize detail ui for tablet device, this
     *      housekeeper is paired with a master housekeeper (master-detail layout)
     */
    public void initializeTabletDetailLayout(MyActivity activity){
        //get current activity context
        mActivity = activity;

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
