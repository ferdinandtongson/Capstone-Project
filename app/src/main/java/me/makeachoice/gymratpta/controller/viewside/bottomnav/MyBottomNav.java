package me.makeachoice.gymratpta.controller.viewside.bottomnav;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import java.util.HashMap;

/**************************************************************************************************/
/*
 * MyBottomNav - helps manage bottom navigation bars used by activities
 */
/**************************************************************************************************/

public class MyBottomNav implements BottomNavigationView.OnNavigationItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 *      BottomNavigationView mNav - bottom navigation view component
 *      int mSelectedItem - menu item currently selected (checked)
 *      Bridge mBridge - communication line interface
 */
/**************************************************************************************************/

    public final static int ITEM_01 = 0;
    public final static int ITEM_02 = 1;
    public final static int ITEM_03 = 2;
    public final static int ITEM_04 = 3;
    public final static int ITEM_05 = 4;

    //mNav - bottom navigation view component
    BottomNavigationView mNav;

    //mSelectedItem - menu item currently selected (checked)
    int mSelectedItem;

    //mItemToMaidMap - convert item number to Maid id
    private HashMap<Integer, String> mItemToMaidIdMap = new HashMap<>();

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
    public MyBottomNav(Activity activity, int barId, int menuId){
        //get navigation bar
        mNav = (BottomNavigationView)activity.findViewById(barId);

        mNav.inflateMenu(menuId);

        //initialize item position to maid id map
        mItemToMaidIdMap = new HashMap<>();

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

    public void setPositionToMaidIdMap(int position, String maidKey){
        if(mItemToMaidIdMap.containsKey(position)){
            mItemToMaidIdMap.remove(position);
        }
        mItemToMaidIdMap.put(position, maidKey);
    }

    public String getMaidKey(int position){
        return mItemToMaidIdMap.get(position);
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
        switch (item.getItemId()) {
            /*case R.id.bottom_nav_item1:
                Log.d("Choice", "     item1");
                checkItem(ITEM_01);
                mSelectedItem = ITEM_01;
                break;
            case R.id.bottom_nav_item2:
                Log.d("Choice", "     item2");
                checkItem(ITEM_02);
                mSelectedItem = ITEM_02;
                break;
            case R.id.bottom_nav_item3:
                Log.d("Choice", "     item3");
                checkItem(ITEM_03);
                mSelectedItem = ITEM_03;
                break;
            case R.id.bottom_nav_item4:
                Log.d("Choice", "     item4");
                mNav.getMenu().getItem(0).setChecked(false);
                mNav.getMenu().getItem(1).setChecked(false);
                mNav.getMenu().getItem(2).setChecked(true);
                mSelectedItem = ITEM_04;
                break;
            case R.id.bottom_nav_item5:
                Log.d("Choice", "     item5");
                mNav.getMenu().getItem(0).setChecked(false);
                mNav.getMenu().getItem(1).setChecked(false);
                mNav.getMenu().getItem(2).setChecked(true);
                mSelectedItem = ITEM_05;
                break;*/
            default:
                mNav.getMenu().getItem(0).setChecked(false);
                mNav.getMenu().getItem(1).setChecked(false);
                //mNav.getMenu().getItem(2).setChecked(false);
                mSelectedItem = ITEM_01;
        }

        if(mBridge != null){
            mBridge.bottomNavigationItemSelected(mSelectedItem);
        }

        return false;
    }

    private void checkItem(int position){
        int count = mItemToMaidIdMap.size();

        for(int i = 0; i < count; i++){
            if(i == position){
                mNav.getMenu().getItem(i).setChecked(true);
            }
            else{
                mNav.getMenu().getItem(i).setChecked(false);
            }
        }
    }

    public void uncheckAll(){
        int count = mNav.getMenu().size();
        for(int i = 0; i < count; i++){
            mNav.getMenu().getItem(i).setChecked(false);
        }

    }

/**************************************************************************************************/

}
