package me.makeachoice.gymratpta.controller.viewside.drawer;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * MyDrawer is an Abstract class that implements MyActivity.OptionsMenuBridge. Classes derived
 * from this class is responsible for the creation and maintenance of the Toolbar view object
 *
 * Implements MyActivity.OptionsMenuBridge
 *      void createOptionsMenu(Menu) - called by onCreateOptionsMenu(Menu) if Options Menu exist
 *      void optionsItemSelected(MenuItem) - called by onOptionsItemSelected(MenuItem) when a menu
 *          item is clicked
 */

/**************************************************************************************************/

public abstract class MyDrawer{

/**************************************************************************************************/
/*
 * Class Variables:
 *      MyActivity mActivity - Activity using bartender
 *      int NO_MENU_OPTIONS - int value for no menu item options
 *      mDrawer - drawerLayout object
 *      mNavigation - navigationView object
 */
/**************************************************************************************************/

    //mActivity - Activity using bartender
    protected MyActivity mActivity;

    //NO_MENU_OPTIONS - int value for no menu item options
    public final static int NO_MENU_OPTIONS = -1;

    //mDrawer - drawerLayout object
    protected DrawerLayout mDrawer;

    //mNavigation - navigationView object
    protected NavigationView mNavigation;

    //mOnNavigationMenuClick - communication bridge when a menu icon is clicked
    protected OnNavigationMenuItemClick mOnNavigationMenuClick;

    //Implemented communication bridge for menu icon click
    public interface OnNavigationMenuItemClick{
        //called when user clicks on menu item
        void onNavigationMenuItemClick(MenuItem item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      DrawerLayout getDrawer() - get drawerLayout object
 *      NavigationView getNavigation() - get navigationView object
 */
/**************************************************************************************************/
    /*
     * DrawerLayout getDrawer() - get drawerLayout object
     */
    public DrawerLayout getDrawer(){ return mDrawer; }

    /*
     * NavigationView getNavigation() - get navigationView object
     */
    public NavigationView getNavigation(){ return mNavigation; }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Public Methods:
 *      void createNavigationMenu(Menu) - create menu options for navigationView
 *      void openDrawer() - open drawer to display navigation menu items
 *      void openDrawer(int) - open drawer to display navigation menu items
 */
/**************************************************************************************************/
    /*
     * void createNavigationMenu(Menu) - create menu options for navigationView
     */
    public void createNavigationMenu(int menuId){
        if(menuId != NO_MENU_OPTIONS && mNavigation != null){
            mNavigation.inflateMenu(menuId);
        }
    }

    /*
     * void openDrawer() - open drawer to display navigation menu items
     */
    public void openDrawer(){
        if(mDrawer != null){
            mDrawer.openDrawer(GravityCompat.START);
        }
    }

    /*
     * void openDrawer(int) - open drawer to display navigation menu items
     */
    public void openDrawer(int gravity){
        if(mDrawer != null){
            mDrawer.openDrawer(gravity);
        }
    }

    public void setNavigationItemChecked(int menuId){
        if(mNavigation != null){
            mNavigation.setCheckedItem(menuId);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods:
 */
/**************************************************************************************************/

/**************************************************************************************************/

}
