package me.makeachoice.gymratpta.controller.viewside.drawer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
import me.makeachoice.gymratpta.view.activity.ScheduleActivity;
import me.makeachoice.gymratpta.view.activity.ClientActivity;
import me.makeachoice.gymratpta.view.activity.ExerciseActivity;
import me.makeachoice.gymratpta.view.activity.SessionActivity;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * todo - description
 */
/**************************************************************************************************/

public class HomeDrawer extends MyDrawer implements NavigationView.OnNavigationItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final int APPOINTMENTS_ID = R.id.nav_appointments;
    public final int CLIENTS_ID = R.id.nav_clients;
    public final int EXERCISES_ID = R.id.nav_exercises;
    public final int SESSIONS_ID = R.id.nav_sessions;

    private final int DEFAULT_DRAWER_ID = R.id.choiceDrawer;
    private final int DEFAULT_NAVIGATION_ID = R.id.choiceNavigationView;

    private final int DEFAULT_MENU_ID = R.menu.drawer_menu;
    private final int DEFAULT_NAV_ICON_ID = R.drawable.gym_rat_black_right_48dp;
    private final int DEFAULT_TITLE_ID = R.string.app_title;
    private final int DEFAULT_SUBTITLE_ID = R.string.app_subtitle;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * HomeToolbar - constructor & initialization methods
 */
/**************************************************************************************************/
    /*
     * HomeToolbar(MyActivity) - class constructor
     */
    public HomeDrawer(MyActivity activity){
        //current activity
        mActivity = activity;

        initialize(DEFAULT_DRAWER_ID, DEFAULT_NAVIGATION_ID);
    }

    public HomeDrawer(MyActivity activity, int drawerId, int navId){
        //current activity
        mActivity = activity;

        initialize(drawerId, navId);
    }

    /*
     * void initialize(int,int) - initializes toolbar
     */
    protected void initialize(int drawerId, int navId){

        mDrawer = (DrawerLayout)mActivity.findViewById(drawerId);
        mNavigation = (NavigationView)mActivity.findViewById(navId);

        mNavigation.setNavigationItemSelectedListener(this);
    }

    public void initializeNavigationHeader(int iconId, String title, String subtitle){
        Drawable drawableIcon = DeprecatedUtility.getDrawable(mActivity, iconId);

        ImageView imgIcon = (ImageView)mActivity.findViewById(R.id.drawerNav_imgIcon);

        imgIcon.setBackground(drawableIcon);
        TextView txtTitle = (TextView)mActivity.findViewById(R.id.drawerNav_txtTitle);
        TextView txtSubtitle = (TextView)mActivity.findViewById(R.id.drawerNav_txtSubtitle);

        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      boolean onNavigationItemSelected(MenuItem) - called when a menu item is click
 *      void requestActivity(int) - request intent to start a new activity
 */
/**************************************************************************************************/
    /*
     * boolean onNavigationItemSelected(MenuItem) - called when a menu item is click. If item is
     *      already check, will close drawer. If not checked, will start a new activity.
     */
    public boolean onNavigationItemSelected(MenuItem menuItem){

        //Checking if the item is in checked state or not
        if (menuItem.isChecked()){
            //already checked, close drawer
            mDrawer.closeDrawers();
        } else {
            //not checked, request activity
            requestActivity(menuItem.getItemId());
        }

        return false;
    }

    /*
     * void requestActivity(int) - request intent to start a new activity
     */
    private void requestActivity(int menuId){
        Boss boss = (Boss)mActivity.getApplication();

        Intent intent;
        switch (menuId) {
            case APPOINTMENTS_ID:
                intent = new Intent(mActivity, ScheduleActivity.class);
                break;
            case CLIENTS_ID:
                intent = new Intent(mActivity, ClientActivity.class);
                break;
            case EXERCISES_ID:
                intent = new Intent(mActivity, ExerciseActivity.class);
                break;
            case SESSIONS_ID:
                intent = new Intent(mActivity, SessionActivity.class);
                break;
            case R.id.nav_help:
                intent = null;
                Toast.makeText(mActivity, "Help Coming Soon!", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_signout:
                intent = null;
                boss.signOutFirebaseAuth();
                break;
            default:
                intent = null;
        }

        mDrawer.closeDrawer(GravityCompat.START);

        if(intent != null){
            mActivity.startActivity(intent);
        }

    }

/**************************************************************************************************/

}
