package me.makeachoice.library.android.base.controller.viewside.navigator;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;


/**************************************************************************************************/
/*
 *  TODO - see BottomNavigationBar document
 *  TODO - description
 *  TODO - create nav layout or add nav layout to activity layout
 *  TODO - create res/menu/your_menu.xml file for menu items
 *  TODO - create res/color/your_menu_selector_color to handle color state changes
 *  TODO - add menu item names to res/values/string.xml
 *  TODO - modify menuItemId if not using default menu names (taken from res/menu/your_menu.xml file
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * _templateBottomNavigator - helps manage bottom navigation bars used by activities
 */

/**************************************************************************************************/

public class _templateBottomNavigator implements BottomNavigationView.OnNavigationItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 *      BottomNavigationView mNav - bottom navigation view component
 *      int mSelectedItem - menu item currently selected (checked)
 *      Bridge mBridge - communication line interface
 */
/**************************************************************************************************/

    //mNav - bottom navigation view component
    BottomNavigationView mNav;

    //mSelectedItem - menu item currently selected (checked)
    int mSelectedItem;

    //mBridge - communication line interface
    Bridge mBridge;

    //Implemented communication line
    public interface Bridge{
        //called by onNavigationItemSelected(MenuItem)
        void bottomNavigationItemSelected(int position);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * BottomNavigationView - constructor
 */
/**************************************************************************************************/
    /*
     * BottomNavigationView - constructor
     * @param activity - activity with bottom navigation bar
     * @param barId - bottom navigation bar resource id
     */
    public _templateBottomNavigator(Activity activity, int barId){
        //get navigation bar
        mNav = (BottomNavigationView)activity.findViewById(barId);

        //add navigation listener
        mNav.setOnNavigationItemSelectedListener(this);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Select Item Methods
 *      void setBridge(Bridge) - set communication line
 *      void selectMenuItem(int) - select the given menu item
 *      int getSelectedMenuItem() - get current menu item that is selected
 *      boolean onNavigationItemSelected(MenuItem) - implements BottomNavigationView.OnNavigationItemSelectedListener
 */
/**************************************************************************************************/
    /*
     * void setBridge(Bridge) - set communication line
     */
    public void setBridge(Bridge bridge){
        mBridge = bridge;
    }

    /*
     * void selectMenuItem(int) - select the given menu item
     */
    public void selectMenuItem(int position){
        //call on nav item selected
        onNavigationItemSelected(mNav.getMenu().getItem(position));
    }

    /*
     * int getSelectedMenuItem() - get current menu item that is selected
     */
    public int getSelectedMenuItem(){
        return mSelectedItem;
    }

    /*
     * boolean onNavigationItemSelected(MenuItem) - implements BottomNavigationView.OnNavigationItemSelectedListener
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //TODO - modify menuItemId if not using default menu names (taken from res/menu/your_menu.xml file
        //check item id, check selected item and uncheck all others
        /*switch (item.getItemId()) {
            case R.id.bottom_nav_item1:
                Log.d("Choice", "     item1");
                mNav.getMenu().getItem(0).setChecked(true);
                mNav.getMenu().getItem(1).setChecked(false);
                mNav.getMenu().getItem(2).setChecked(false);
                mSelectedItem = 0;
                break;
            case R.id.bottom_nav_item2:
                Log.d("Choice", "     item2");
                mNav.getMenu().getItem(0).setChecked(false);
                mNav.getMenu().getItem(1).setChecked(true);
                mNav.getMenu().getItem(2).setChecked(false);
                mSelectedItem = 1;
                break;
            case R.id.bottom_nav_item3:
                Log.d("Choice", "     item3");
                mNav.getMenu().getItem(0).setChecked(false);
                mNav.getMenu().getItem(1).setChecked(false);
                mNav.getMenu().getItem(2).setChecked(true);
                mSelectedItem = 2;
                break;
            default:
                mNav.getMenu().getItem(0).setChecked(true);
                mNav.getMenu().getItem(1).setChecked(false);
                mNav.getMenu().getItem(2).setChecked(false);
                mSelectedItem = 0;
        }*/

        if(mBridge != null){
            mBridge.bottomNavigationItemSelected(mSelectedItem);
        }

        return false;
    }

/**************************************************************************************************/

}
