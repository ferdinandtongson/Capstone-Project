package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.viewside.drawer.HomeDrawer;
import me.makeachoice.gymratpta.controller.viewside.toolbar.HomeToolbar;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;
import me.makeachoice.library.android.base.utilities.NetworkHelper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_CONTACTS;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_EXERCISE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_EXERCISE_RECORD;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_GOALS;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_NOTES_RECORD;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_SCHEDULE;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_STATS_RECORD;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_WARNING_DELETE;

/**************************************************************************************************/
/*
 * GymRatBaseKeeper is the base HouseKeeper for all HouseKeeper classes used in the GymRat project.
 * It's main function is to handle the initialization and event handling of the toolbar, drawer
 * components and firebase login
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
 *      Boss mBoss - Boss application
 *      HomeToolbar mToolbar - toolbar component
 *      HomeDrawer mHomeDrawer - drawer navigation component
 *
 *      int mToolbarMenuId - toolbar menu resource id
 *      int mBottomNavSelectedItemId - used to determine which menu item is selected in the drawer
 *
 *      FirebaseAuth mFirebaseAuth - firebase authentication instance
 *      FirebaseAuth.AuthStateListener mAuthStateListener - firebase authentication listener
 *      int mFirebaseDebugCount - used to counter firebase bug calling sign in multiple times
 *      OnQuickHelpRequestListener mQuickHelpListener - listens for toolbar menu item "Quick Help" request
 */
/**************************************************************************************************/

    //mBoss - Boss application
    protected Boss mBoss;

    //mToolbar - toolbar component
    protected HomeToolbar mHomeToolbar;

    //mHomeDrawer - drawer navigation component
    protected HomeDrawer mHomeDrawer;

    //mToolbarMenuId - toolbar menu resource id
    protected int mToolbarMenuId = -1;

    //mBottomNavSelectedItemId - used to determine which menu item is selected in the drawer
    protected int mBottomNavSelectedItemId;

    protected boolean mIsAuth;
    protected boolean mInitialized;
    protected String mUserId;

    //mFirebaseAuth - firebase authentication instance
    private FirebaseAuth mFirebaseAuth;

    //mAuthStateListener - firebase authentication listener
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //REQUEST_CODE_SIGN_IN - request code used by firebaseUI
    private static final int REQUEST_CODE_SIGN_IN = 200;

    //mFirebaseDebugCount - used to counter firebase bug calling sign in multiple times
    private int mFirebaseDebugCount;

    //mQuickHelpListener - listens for toolbar menu item "Quick Help" request
    private OnQuickHelpRequestListener mQuickHelpListener;
    public interface OnQuickHelpRequestListener{
        void onQuickHelpRequested();
    }

    DialogInterface.OnClickListener mOnRefreshListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            onRefreshLogin();
        }
    };

    DialogInterface.OnClickListener mOnCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            onCancelLogin();
        }
    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Activity Lifecycle Methods
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
 *      void start() - called when onStart is called in the Activity
 *      void stop() - called when onStop is called in the Activity
 *      void activityResult(int,int,Intent) - authentication results returned from firebase authUI
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
        mIsAuth = true;
        mInitialized = false;

        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();

        if(user == null){
            mBoss.userSignIn();
            mIsAuth = false;
        }
        else{
            if(mBoss.getUserId().isEmpty()){
                mBoss.setUser(user);
            }
        }
    }

    /*
     * void start() - called when onStart is called in the Activity. Sets the authentication state
     * listener to the firebase authentication instance
     */
    public void start(){
        super.start();

        if(mIsAuth){
            mUserId = mBoss.getUserId();
            //get user id from Boss
            if(!mInitialized){
                initializeNavigation();
            }
        }
    }


    public void pause(){
        super.pause();
        FragmentManager fm = mActivity.getSupportFragmentManager();

        ArrayList<String> tagList = new ArrayList<>();
        tagList.add(DIA_SCHEDULE);
        tagList.add(DIA_WARNING_DELETE);
        tagList.add(DIA_CONTACTS);
        tagList.add(DIA_GOALS);
        tagList.add(DIA_EXERCISE_RECORD);
        tagList.add(DIA_EXERCISE);
        tagList.add(DIA_STATS_RECORD);
        tagList.add(DIA_NOTES_RECORD);


        String tag;
        DialogFragment dia;
        int count = tagList.size();
        for(int i = 0; i < count; i++){
            tag = tagList.get(i);

            dia = (DialogFragment)fm.findFragmentByTag(tag);
            if (dia != null) { dia.dismiss(); }
        }

    }

    /*
     * void stop() - called when onStop is called in the Activity. Removes authentication state
     * listener from firebase authentication instance
     */
    public void stop(){
        super.stop();
        if(mAuthStateListener != null){
            //remove authentication listener
            //mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /*
     * void activityResult(int,int,Intent) - authentication results returned from firebase authUI
     */
    @Override
    public void activityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                mBoss.checkInUser();
                mIsAuth = true;
                return;
            }

            // Sign in canceled
            if (resultCode == RESULT_CANCELED) {
                //TODO - need to show dialog
                mActivity.finish();

                return;
            }

            // No network
            if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                NetworkHelper.showNoNetworkDialog(mActivity, mOnRefreshListener, mOnCancelListener);
                return;
            }
        }
    }

    private void onCancelLogin(){
        mActivity.finish();
    }

    private void onRefreshLogin(){
        mBoss.userSignIn();
        mIsAuth = false;

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeFirebaseAuth() - initialize Firebase authentication
 *      void initializeNavigation() - initialize navigation ui
 *      void initializeToolbar() - initialize toolbar component
 *      void initializeDrawer() - initialize drawer navigation component
 */
/**************************************************************************************************/

    /*
     * void initializeNavigation() - initialize navigation ui
     */
    private void initializeNavigation(){
        //initialize toolbar component
        initializeToolbar();

        //initialize drawer and navigation components
        initializeDrawer();
    }

    /*
     * void initializeToolbar() - initialize toolbar component
     */
    private void initializeToolbar() {

        //create toolbar component
        if(mToolbarMenuId == -1){
            mHomeToolbar = new HomeToolbar(mActivity);
        }
        else{
            mHomeToolbar = new HomeToolbar(mActivity, mToolbarMenuId);
        }

        mHomeToolbar.setOnNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //react to click
                mHomeDrawer.openDrawer();
            }
        });


        View navigationIcon = mHomeToolbar.getToolbarNavigationIcon();

        //check api level is 21 or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO - this is used for creating a transition from toolbar to drawer
            String transitionName = mActivity.getResources().getString(R.string.trans_navIcon);
            navigationIcon.setTransitionName(transitionName);
        }

        //set bartender as options menu bridge
        mActivity.setOptionsMenuBridge(mHomeToolbar);

    }

    /*
     * void initializeDrawer() - initialize drawer navigation component
     */
    private void initializeDrawer(){
        //instantiate homeDrawer class
        mHomeDrawer = new HomeDrawer(mActivity);

        //selected menu item in navigation drawer
        mHomeDrawer.setNavigationItemChecked(mBottomNavSelectedItemId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 */
/**************************************************************************************************/

    public void setOnQuickHelpRequestListener(OnQuickHelpRequestListener listener){
        mQuickHelpListener = listener;
    }

/**************************************************************************************************/




}
