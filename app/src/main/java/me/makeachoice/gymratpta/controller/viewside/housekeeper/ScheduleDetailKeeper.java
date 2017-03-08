package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientRoutineButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.SaveNav;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.schedule.ScheduleDetailMaid;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT_ROUTINE;

/**************************************************************************************************/
/*
 * ScheduleDetailKeeper maintains the main components of the schedule detail screen. It is directly
 * responsible for handling toolbar and drawer events (see GymRatBaseKeeper), bottom navigation events
 * (see SaveNav) and the fragment container holding the fragment maintained by RoutineDetailMaid
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

public class ScheduleDetailKeeper extends GymRatBaseKeeper implements MyActivity.Bridge {

/**************************************************************************************************/
/*
 * Class Variables:
 *      String mUserId - user id taken from firebase authentication
 *      boolean mIsNewRoutine - status flag if routine is new routine or old
 *
 *      RoutineDetailItem mRoutineDetailItem - details of the given routine
 *      RoutineNameItem mSaveRoutineName - routine name item being saved
 *      HashMap<String,RoutineNameItem> mRoutineNameMap - hashMap of routine names in database
 *      RoutineDetailMaid mMaid - routine detail maid
 */
/**************************************************************************************************/

    //mClientItem - client item
    private ClientItem mClientItem;

    //mScheduleItem - schedule item
    private ScheduleItem mScheduleItem;

    private ArrayList<ClientRoutineItem> mExerciseList;
    private ArrayList<ClientRoutineItem> mOldList;
    private int mSaveCounter;
    private String mStrNone;
    private String mStrCustom;
    private String mStrRoutine;

    private ScheduleDetailMaid mMaid;

    private ClientRoutineButler mRoutineButler;
    private ScheduleButler mScheduleButler;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StubRoutineDetailKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * StubRoutineDetailKeeper - constructor
     */
    public ScheduleDetailKeeper(int layoutId){

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
        super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        //initialize strings
        mStrNone = mActivity.getString(R.string.msg_none_selected);
        mStrCustom = mActivity.getString(R.string.custom_routine);
        mStrRoutine = mActivity.getString(R.string.routine);

        //initialize list arrays
        mExerciseList = new ArrayList<>();
        mOldList = new ArrayList();

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
                initializeValues();
            }
        }
    }

    /*
     * void initializeValues() - initialize values used by keeper
     */
    private void initializeValues(){

        //get routine detail item from Boss
        mClientItem = mBoss.getClient();

        mScheduleItem = mBoss.getAppointmentItem();

        mRoutineButler = new ClientRoutineButler(mActivity, mUserId, mClientItem.fkey);
        mScheduleButler = new ScheduleButler(mActivity, mUserId);

        initializeLayout();
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods:
 *      void initializeValues() - initialize values used by keeper
 *      void initializeLayout() - initialize maids and bottom navigation
 *      void initializeMaid() - initialize ExerciseMaid and RoutineMaid
 *      void initializeBottomNavigation - initialize bottom navigation for the exercise screen
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize maids and bottom navigation
     */
    private void initializeLayout(){
        String title = mStrRoutine + " - " + mScheduleItem.clientName;
        String subtitle = mScheduleItem.appointmentDate + " @" + mScheduleItem.appointmentTime;
        mHomeToolbar.setGymRatToolbarTitle(title, subtitle);

        //initialize maids
        initializeMaid();

        //initialize bottom navigation
        initializeBottomNavigation();
    }


    /*
     * void initializeMaid() - initialize RoutineDetailMaid
     */
    private void initializeMaid(){
        //get maid registry
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        //layout used by routine detail screen
        int layoutId = R.layout.fragment_routine_detail;

        //initialize routine detail maid
        maidRegistry.initializeScheduleDetailMaid(MaidRegistry.MAID_SCHEDULE_DETAIL, layoutId,
                mUserId, mClientItem, mScheduleItem);

        //get routine detail maid from registry
        mMaid = (ScheduleDetailMaid)maidRegistry.requestMaid(MaidRegistry.MAID_SCHEDULE_DETAIL);

        //load fragment maintained by routine detail maid
        loadFragment(mMaid);

    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation used for saving routine details
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        SaveNav nav = new SaveNav(mActivity);

        //set onSave listener event
        nav.setSaveListener(new SaveNav.OnSaveRoutineListener() {
            @Override
            public void onSaveRoutine() {
                //routine save details event occurred
                onRoutineSaved();
            }

            @Override
            public void onCancelRoutine(){
                //routine cancel event occurred
                onRoutineCanceled();
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void loadFragment(MyMaid) - load appropriate fragment requested by user
 *      void loadRoutines() - load routine data from database
 */
/**************************************************************************************************/

    /*
     * void loadFragment(MyMaid) - load appropriate fragment requested by user
     */
    private void loadFragment(MyMaid maid){
        //get fragment manger
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();

        //get fragment transaction object
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //get fragment managed by maid
        Fragment fragment = maid.getFragment();

        //add fragment to fragment container
        fragmentTransaction.replace(R.id.choiceFragmentContainer, fragment);

        //commit fragment transaction
        fragmentTransaction.commit();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void backPressed() - called when Activity.onBackPressed is called
 *      void onRoutineCanceled() - cancel bottom navigation button clicked, do Not save routine modification
 *      void onRoutineSaved() - save bottom navigation button clicked, save routine modifications
 *      void saveSchedule() - save new routine schedule information
 *      void deleteOldRoutine(...) - delete old routine exercise information
 *      void saveRoutine(int) - save routine exercises in list
 */
/**************************************************************************************************/
    /*
     * void backPressed() - called when Activity.onBackPressed is called
     */
    @Override
    public void backPressed(){
        //NOTE: onBackPressed() will call backPress()

    }

    /*
     * void onRoutineCanceled() - cancel bottom navigation button clicked, do Not save routine modification
     */
    private void onRoutineCanceled(){
        mActivity.onBackPressed();
    }

    /*
     * void onRoutineSaved() - save bottom navigation button clicked, save routine modifications
     */
    private void onRoutineSaved(){
        //clear exercise list buffer
        mExerciseList.clear();
        mOldList.clear();

        //get routine exercises from maid
        mExerciseList.addAll(mMaid.getRoutine());

        //get number of exercises
        int count = mExerciseList.size();

        //loop through list
        for(int i = 0; i < count; i++){
            //set order number of list exercises
            mExerciseList.get(i).orderNumber = String.valueOf(i);
        }

        String timestamp = DateTimeHelper.getTimestamp(mScheduleItem.appointmentDate, mScheduleItem.appointmentTime);

        mRoutineButler.loadClientRoutinesByTimestamp(timestamp, LOADER_CLIENT_ROUTINE,
                new ClientRoutineButler.OnLoadedListener() {
                    @Override
                    public void onLoaded(ArrayList<ClientRoutineItem> routineList) {
                        saveOldList(routineList);
                    }
                });
    }

    private void saveOldList(ArrayList<ClientRoutineItem> routineList){
        mOldList.addAll(routineList);

        //delete old routine schedule information
        mScheduleButler.deleteSchedule(mScheduleItem, new ScheduleButler.OnScheduleDeletedListener() {
            @Override
            public void onScheduleDeleted() {
                //save new routine schedule information
                saveSchedule();
            }
        });
    }

    /*
     * void saveSchedule() - save new routine schedule information
     */
    private void saveSchedule(){
        String routineName = mMaid.getRoutineName();
        if(mExerciseList.isEmpty()){
            routineName = mStrNone;
        }
        else{
            if(routineName.isEmpty()){
                routineName = mStrCustom;
            }
        }

        //get routine name from maid
        mScheduleItem.routineName = routineName;

        //save schedule
        mScheduleButler.saveSchedule(mScheduleItem, new ScheduleButler.OnScheduleSavedListener() {
            @Override
            public void onScheduleSaved() {
                //delete old routine exercise information
                deleteOldRoutine();
            }
        });
    }

    /*
     * void deleteOldRoutine(...) - delete old routine exercise information
     */
    private void deleteOldRoutine(){
        if(mOldList.isEmpty()){
            //set save counter
            mSaveCounter = 0;

            //save routine exercises in list
            saveRoutine(mSaveCounter);

        }
        else{
            ClientRoutineItem item = mOldList.get(0);

            //delete info
            mRoutineButler.deleteClientRoutine(item, new ClientRoutineButler.OnDeletedListener() {
                @Override
                public void onDeleted() {
                    //set save counter
                    mSaveCounter = 0;

                    //save routine exercises in list
                    saveRoutine(mSaveCounter);
                }
            });

        }
    }

    /*
     * void saveRoutine(int) - save routine exercises in list
     */
    private void saveRoutine(int counter){
        //check save counter
        if(counter < mExerciseList.size()){
            //get exercise item from list
            ClientRoutineItem item = mExerciseList.get(counter);

            //save exercise item
            mRoutineButler.saveClientRoutine(item, new ClientRoutineButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    //save next exercise item in list, if any
                    mSaveCounter++;
                    saveRoutine(mSaveCounter);
                }
            });
        }
        else{
            //all items saved, exit activity
            mActivity.onBackPressed();
        }
    }

/**************************************************************************************************/


}
