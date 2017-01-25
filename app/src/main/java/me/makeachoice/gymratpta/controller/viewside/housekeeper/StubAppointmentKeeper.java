package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.viewside.toolbar.HomeToolbar;
import me.makeachoice.library.android.base.controller.viewside.bartender.MyBartender;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;
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

public class StubAppointmentKeeper extends MyHouseKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      mBoss - Boss application
 */
/**************************************************************************************************/

    //mBoss - Boss application
    private Boss mBoss;

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
        super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        //get Boss application
        mBoss = (Boss)mActivity.getApplication();

        initializeForMobileLayout();

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
        initializeToolbar();
        TextView txtTitle = (TextView)mActivity.findViewById(R.id.txtTitle);
        txtTitle.setText(me.makeachoice.gymratpta.controller.manager.HouseKeeperRegistry.KEEPER_APPOINTMENT);
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
                Log.d("Choice", "StubAppointmentKeeper.onNavIconClick");
            }
        });

        toolbar.setOnMenuItemClick(new MyBartender.OnMenuItemClick() {
            @Override
            public void onMenuItemClick(MenuItem menuItem) {
                Log.d("Choice", "StudAppointmentKeeper.onMenuItemClick");
            }
        });

        //set bartender as options menu bridge
        mActivity.setOptionsMenuBridge(toolbar);
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
