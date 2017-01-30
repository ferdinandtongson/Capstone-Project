package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.SessionRecyclerAdapter;
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

public class StubSessionKeeper extends GymRatRecyclerKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      mBoss - Boss application
 */
/**************************************************************************************************/

    private SessionRecyclerAdapter mAdapter;

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
    public StubSessionKeeper(int layoutId){

        //get layout id
        mActivityLayoutId = layoutId;
        mToolbarMenuId = R.menu.toolbar_menu;
        mBottomNavSelectedItemId = R.id.nav_sessions;
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
        Log.d("Choice", "SessionKeeper.initializeLayout");
        String emptyMsg = mActivity.getResources().getString(R.string.emptyRecycler_noSessions);
        setEmptyMessage(emptyMsg);

        initializeAdapter();
    }

    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_appointment;

        mData = createDataStub();

        //create adapter consumed by the recyclerView
        mAdapter = new SessionRecyclerAdapter(mActivity, adapterLayoutId);
        mAdapter.setStubData(mData);

        mBasicRecycler.setAdapter(mAdapter);

        checkForEmptyRecycler(mData.isEmpty());

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


/**************************************************************************************************/
/*
 * StubItem - used for debugging purposes
 */
/**************************************************************************************************/


    private ArrayList<SessionRecyclerAdapter.StubItem> mData;
    private ArrayList<SessionRecyclerAdapter.StubItem> createDataStub(){
        ArrayList<SessionRecyclerAdapter.StubItem> stubData = new ArrayList();

        SessionRecyclerAdapter.StubItem item01 = new SessionRecyclerAdapter.StubItem();
        item01.clientName = "Quess Starbringer";
        item01.clientPhoneNumber = "Monday";
        item01.clientSMS = "appointments";
        item01.clientWorkout = "workout01";
        item01.appointmentTime = "09:00 - 10:00";

        SessionRecyclerAdapter.StubItem item02 = new SessionRecyclerAdapter.StubItem();
        item02.clientName = "Quess Starbringer";
        item02.clientPhoneNumber = "Monday";
        item02.clientSMS = "appointments";
        item02.clientWorkout = "workout01";
        item02.appointmentTime = "10:00 - 10:00";

        SessionRecyclerAdapter.StubItem item03 = new SessionRecyclerAdapter.StubItem();
        item03.clientName = "Quess Starbringer";
        item03.clientPhoneNumber = "Monday";
        item03.clientSMS = "appointments";
        item03.clientWorkout = "workout01";
        item03.appointmentTime = "11:00 - 12:00";

        SessionRecyclerAdapter.StubItem item04 = new SessionRecyclerAdapter.StubItem();
        item04.clientName = "Quess Starbringer";
        item04.clientPhoneNumber = "Monday";
        item04.clientSMS = "appointments";
        item04.clientWorkout = "workout01";
        item04.appointmentTime = "12:00 - 13:00";

        SessionRecyclerAdapter.StubItem item05 = new SessionRecyclerAdapter.StubItem();
        item05.clientName = "Quess Starbringer";
        item05.clientPhoneNumber = "Monday";
        item05.clientSMS = "appointments";
        item05.clientWorkout = "workout01";
        item05.appointmentTime = "13:00 - 14:00";

        SessionRecyclerAdapter.StubItem item06 = new SessionRecyclerAdapter.StubItem();
        item06.clientName = "Quess Starbringer";
        item06.clientPhoneNumber = "Monday";
        item06.clientSMS = "appointments";
        item06.clientWorkout = "workout01";
        item06.appointmentTime = "14:00 - 15:00";

        SessionRecyclerAdapter.StubItem item07 = new SessionRecyclerAdapter.StubItem();
        item07.clientName = "Quess Starbringer";
        item07.clientPhoneNumber = "Monday";
        item07.clientSMS = "appointments";
        item07.clientWorkout = "workout01";
        item07.appointmentTime = "15:00 - 16:00";

        //TODO - create actual data for adapter to consume
        stubData.add(item01);
        stubData.add(item02);
        stubData.add(item03);
        stubData.add(item04);
        stubData.add(item05);
        stubData.add(item06);
        stubData.add(item07);

        return stubData;
    }

/**************************************************************************************************/

}
