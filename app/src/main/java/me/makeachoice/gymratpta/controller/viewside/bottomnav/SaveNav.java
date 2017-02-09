package me.makeachoice.gymratpta.controller.viewside.bottomnav;

import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;


/**
 * Created by Usuario on 2/8/2017.
 */

public class SaveNav extends MyBottomNav implements BottomNavigationView.OnNavigationItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private final static int DEFAULT_MENU_ID = R.menu.bottom_nav_save_menu;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * BottomNavigationView - constructor
 */
/**************************************************************************************************/
    /*
     * BottomNavigationView - constructor
     */
    public SaveNav(MyActivity activity){
        //get activity
        mActivity = activity;

        //get navigation bar
        mNav = (BottomNavigationView)activity.findViewById(DEFAULT_BOTTOMNAV_ID);

        initializeNavigation(DEFAULT_MENU_ID);
    }

    public SaveNav(MyActivity activity, int bottomId, int menuId){
        //get activity
        mActivity = activity;

        //get navigation bar
        mNav = (BottomNavigationView)activity.findViewById(bottomId);

        initializeNavigation(menuId);
    }

    private void initializeNavigation(int menuId){
        //inflate menu
        mNav.inflateMenu(menuId);

        mNav.setOnNavigationItemSelectedListener(this);
        mNav.getMenu().getItem(0).setChecked(false);
        mNav.getMenu().getItem(1).setChecked(false);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Select Item Methods
 *      void setOnItemSelectedListener(OnNavigationItemSelectedListener) - listener for item select events
 *      void checkItem(int) - mark menu item as checked
 *      void uncheckAll() - mark all menu items as unchecked
 */
/**************************************************************************************************/

    public boolean onNavigationItemSelected(MenuItem item){
        int itemId = item.getItemId();
        uncheckAll();
        item.setChecked(true);

        MyMaid maid;
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        switch (itemId) {
            case bottom_nav_item1: // 0 - save
                //TODO - save routine
                break;
            case bottom_nav_item2: // 1 - cancel
                mActivity.onBackPressed();
                break;
            default:
                maid = null;
        }

        return false;
    }

/**************************************************************************************************/


}
