package me.makeachoice.gymratpta.controller.viewside.bottomnav;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;

/**************************************************************************************************/
/*
 * ScheduleNav - helps manage bottom navigation bars used by activities
 */
/**************************************************************************************************/

public class ScheduleNav extends MyBottomNav implements BottomNavigationView.OnNavigationItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private final static int DEFAULT_MENU_ID = R.menu.bottom_nav_schedule;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ScheduleNav - constructor
 */
/**************************************************************************************************/
    /*
     * ScheduleNav - constructor
     */
    public ScheduleNav(MyActivity activity){
        //get activity
        mActivity = activity;

        //get navigation bar
        mNav = (BottomNavigationView)activity.findViewById(DEFAULT_BOTTOMNAV_ID);

        initializeNavigation(DEFAULT_MENU_ID);
    }

    public ScheduleNav(MyActivity activity, int bottomId, int menuId){
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
        onNavigationItemSelected(mNav.getMenu().getItem(0));
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Select Item Methods
 *      boolean onNavigationItemSelected(MenuItem) - bottom navigation item selection event
 *      void loadFragment(MyMaid) - load appropriate fragment requested by user
 */
/**************************************************************************************************/
    /*
     * boolean onNavigationItemSelected(MenuItem) - bottom navigation item selection event
     */
    public boolean onNavigationItemSelected(MenuItem item){
        int itemId = item.getItemId();
        uncheckAll();
        item.setChecked(true);

        MyMaid maid;
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        switch (itemId) {
            case bottom_nav_item1: // 0 - viewPage of lists of exercises
                maid = maidRegistry.requestMaid(MaidRegistry.MAID_DAY);
                break;
            case bottom_nav_item2: // 1 - list of exercise routines
                maid = maidRegistry.requestMaid(MaidRegistry.MAID_WEEK);
                break;
            default:
                maid = null;
        }

        if(mListener != null){
            mListener.onNavSelected(itemId);
        }

        //load fragment
        loadFragment(maid);

        return false;
    }

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

}
