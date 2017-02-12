package me.makeachoice.gymratpta.controller.viewside.bottomnav;

import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import me.makeachoice.gymratpta.R;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.id.bottom_nav_item1;
import static me.makeachoice.gymratpta.R.id.bottom_nav_item2;


/**************************************************************************************************/
/*
 * SaveNav gives the user the option to save or cancel a exercise training routine using the bottom
 * navigation component
 */
/**************************************************************************************************/

public class SaveNav extends MyBottomNav implements BottomNavigationView.OnNavigationItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 *      DEFAULT_MENU_ID - default menu id
 *      mListener - listens for an onSave click event
 */
/**************************************************************************************************/

    //DEFAULT_MENU_ID - default menu id
    private final static int DEFAULT_MENU_ID = R.menu.bottom_nav_save_menu;

    //mListener - listens for an onSave click event
    private OnSaveRoutineListener mListener;
    public interface OnSaveRoutineListener{
        //notify listener of an onSave click event
        public void onSaveRoutine();
    }

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

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods
 *      void initializeNavigation(int) - initialize bottom navigation menu components
 */
/**************************************************************************************************/
    /*
     * void initializeNavigation(int) - initialize bottom navigation menu components
     */
    private void initializeNavigation(int menuId){
        //inflate menu
        mNav.inflateMenu(menuId);

        //set onNavigationItemSelected listener
        mNav.setOnNavigationItemSelectedListener(this);

        //deselect menu items
        mNav.getMenu().getItem(0).setChecked(false);
        mNav.getMenu().getItem(1).setChecked(false);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setter Methods
 *      void setSaveListener(...) - set listener to listen for onSave click event
 */
/**************************************************************************************************/
    /*
     * void setSaveListener(...) - set listener to listen for onSave click event
     */
    public void setSaveListener(OnSaveRoutineListener listener){
        mListener = listener;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      boolean onNavigationItemSelected(MenuItem) - bottom navigation menu item selected
 */
/**************************************************************************************************/
    /*
     * boolean onNavigationItemSelected(MenuItem) - bottom navigation menu item selected
     */
    public boolean onNavigationItemSelected(MenuItem item){
        //get menu id
        int itemId = item.getItemId();

        //find which menu item was selected
        switch (itemId) {
            case bottom_nav_item1: // 0 - cancel
                //onCancel event, return to parent activity
                mActivity.onBackPressed();
                break;
            case bottom_nav_item2: // 1 - save
                //onSave event, check if there is a listener
                if(mListener != null){
                    //notify listener of onSave click event
                    mListener.onSaveRoutine();
                }

                //return to parent activity
                mActivity.onBackPressed();
                break;
        }

        return false;
    }

/**************************************************************************************************/


}
