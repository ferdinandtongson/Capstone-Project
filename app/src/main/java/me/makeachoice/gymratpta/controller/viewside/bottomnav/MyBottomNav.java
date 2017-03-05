package me.makeachoice.gymratpta.controller.viewside.bottomnav;

import android.support.design.widget.BottomNavigationView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * MyBottomNav is the base class for Nav classes
 */
/**************************************************************************************************/

public abstract class MyBottomNav{

/**************************************************************************************************/
/*
 * Class Variables:
 *      BottomNavigationView mNav - bottom navigation view component
 */
/**************************************************************************************************/

    protected final static int DEFAULT_BOTTOMNAV_ID = R.id.choiceBottomNavigation;

    //mNav - bottom navigation view component
    BottomNavigationView mNav;

    //mActivity - activity context
    MyActivity mActivity;

    protected OnNavSelectedListener mListener;
    public interface OnNavSelectedListener{
        public void onNavSelected(int navId);
    }


/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Select Item Methods
 *      void checkItem(int) - mark menu item as checked
 *      void uncheckAll() - mark all menu items as unchecked
 */
/**************************************************************************************************/
    /*
     * void checkItem(int) - mark menu item as checked
     */
    public void checkItem(int position){
        int count = mNav.getMenu().size();

        for(int i = 0; i < count; i++){
            if(i == position){
                mNav.getMenu().getItem(i).setChecked(true);
            }
            else{
                mNav.getMenu().getItem(i).setChecked(false);
            }
        }
    }

    /*
     * void uncheckAll() - mark all menu items as unchecked
     */
    public void uncheckAll(){
        int count = mNav.getMenu().size();
        for(int i = 0; i < count; i++){
            mNav.getMenu().getItem(i).setChecked(false);
        }

    }

    public void setOnNavSelectedListener(OnNavSelectedListener listener){
        mListener = listener;
    }

/**************************************************************************************************/

}
