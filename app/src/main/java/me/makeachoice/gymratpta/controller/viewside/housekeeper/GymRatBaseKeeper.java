package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import me.makeachoice.gymratpta.BuildConfig;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.viewside.drawer.HomeDrawer;
import me.makeachoice.gymratpta.controller.viewside.toolbar.HomeToolbar;
import me.makeachoice.library.android.base.controller.viewside.bartender.MyBartender;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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
 * TODO - need to style components
 *          todo - toolbar
 *          todo - drawer
 *          todo - navigation header
 * TODO - need "no sign in" dialog
 */
/**************************************************************************************************/

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

        //initialize firebase authentication
        initializeFirebaseAuth();
    }

    /*
     * void start() - called when onStart is called in the Activity. Sets the authentication state
     * listener to the firebase authentication instance
     */
    public void start(){
        super.start();
        //set authentication listener
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /*
     * void stop() - called when onStop is called in the Activity. Removes authentication state
     * listener from firebase authentication instance
     */
    public void stop(){
        super.stop();
        if(mAuthStateListener != null){
            //remove authentication listener
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /*
     * void activityResult(int,int,Intent) - authentication results returned from firebase authUI
     */
    @Override
    public void activityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //user is signed in, initialize navigation components
                initializeNavigation();
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
                //TODO - need to show dialog
                return;
            }
        }
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
     * void initializeFirebaseAuth() - initialize Firebase authentication
     */
    private void initializeFirebaseAuth(){
        Log.d("Choice", "GymRatBaseKeeper.initializeFirebaseAuth");
        mFirebaseDebugCount = 0;
        //get authentication instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        //create firebase authentication listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user signed in, initialize navigation layout
                    initializeNavigation();
                }else{
                    //debug count used to prevent multiple userSignIn calls, known bug
                    if(mFirebaseDebugCount == 0){
                        userSignIn();
                        mFirebaseDebugCount++;
                    }
                }
            }
        };
    }

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

        mHomeToolbar.setOnMenuItemClick(new MyBartender.OnMenuItemClick() {
            @Override
            public void onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch(itemId){
                    case R.id.toolbar_sign_out:
                        mFirebaseAuth.signOut();
                        break;
                    case R.id.toolbar_quick_help:
                        if(mQuickHelpListener != null){
                            //notify listener, "quick help" requested
                            mQuickHelpListener.onQuickHelpRequested();
                        }
                        break;
                }
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

    private void userSignIn(){
        mActivity.startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .build(), REQUEST_CODE_SIGN_IN);
    }

    public void setOnQuickHelpRequestListener(OnQuickHelpRequestListener listener){
        mQuickHelpListener = listener;
    }

    /*private void updateUserProfile(FirebaseUser user){
        if(mBoss.getUser() == null || !mBoss.getUser().getUid().equals(user.getUid())){
            mBoss.setUser(user);

            FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
            mBoss.initializeUser(fireDB);
        }

    }*/


/**************************************************************************************************/




}
