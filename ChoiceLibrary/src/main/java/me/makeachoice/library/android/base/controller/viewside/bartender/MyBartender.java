package me.makeachoice.library.android.base.controller.viewside.bartender;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * MyBartender is an Abstract class that implements MyActivity.OptionsMenuBridge. Classes derived
 * from this class is responsible for the creation and maintenance of the Toolbar view object
 *
 * Implements MyActivity.OptionsMenuBridge
 *      void createOptionsMenu(Menu) - called by onCreateOptionsMenu(Menu) if Options Menu exist
 *      void optionsItemSelected(MenuItem) - called by onOptionsItemSelected(MenuItem) when a menu
 *          item is clicked
 */

/**************************************************************************************************/

public abstract class MyBartender implements MyActivity.OptionsMenuBridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      MyActivity mActivity - Activity using bartender
 *      Toolbar mToolbar - toolbar object
 *      int mMenuId - menu resource id number
 */
/**************************************************************************************************/

    //MyActivity mActivity - Activity using bartender
    protected MyActivity mActivity;

    //Toolbar mToolbar - toolbar object
    protected Toolbar mToolbar;

    //int mMenuId - menu resource id number
    protected int mMenuId;

    //int NO_MENU_OPTIONS - int value for no menu item options
    public final static int NO_MENU_OPTIONS = -1;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void createOptionsMenu(Menu) - called by Activity to inflate menu
 */
/**************************************************************************************************/
    /*
     * void createOptionsMenu(Menu) - called by Activity to inflate menu
     * @param menu - menu object
     */
    public void createOptionsMenu(Menu menu){
        if(mMenuId != NO_MENU_OPTIONS){
            Log.d("Choice", "     here");
            mActivity.getMenuInflater().inflate(mMenuId, menu);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods:
 *      optionsItemSelected(MenuItem) - called when a menu item is click
 */
/**************************************************************************************************/
    /*
     * optionsItemSelected(MenuItem) - called when a menu item is click
     */
    public abstract boolean optionsItemSelected(MenuItem item);

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      Toolbar getToolbar() - get toolbar object
 */
/**************************************************************************************************/
    /*
     * Toolbar getToolbar() - get toolbar object
     */
    public Toolbar getToolbar(){ return mToolbar; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void setTitle(String) - set title string value
 *      void setTitle(String,String) - set title and subtitle string value
 *      void setOverflowIcon(int) - set overflow icon
 *      void setNavigationIcon(int) - set navigation icon
 *      void setNavigationDescription(int) - set content description using string resource id
 *      void setNavigationDescription(String) - set content description
 *      void setNavigationOnClickListener(View.OnClickListener) - set navigation onClickListener
 */
/**************************************************************************************************/
    /*
     * void setTitle(String) - set title string value
     */
    public void setTitle(String title){
        setTitle(title, "");
    }

    /*
     * void setTitle(String,String) - set title and subtitle string value
     */
    public void setTitle(String title, String subtitle){
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        mActivity.getSupportActionBar().setTitle(title);

        if(!subtitle.isEmpty()){
            mActivity.getSupportActionBar().setSubtitle(subtitle);
        }

    }

    /*
     * void setOverflowIcon(int) - set overflow icon
     */
    public void setOverflowIcon(int iconId) {
        Drawable icon;
        if(Build.VERSION.SDK_INT < 21 ){
            icon = mActivity.getResources().getDrawable(iconId);
        }
        else{
            icon = mActivity.getDrawable(iconId);
        }

        //set overflow icon
        mToolbar.setOverflowIcon(icon);
    }

    /*
     * void setNavigationIcon(int) - set navigation icon. Top-level phone navigation icons are
     *      typically "hamburger" icons (three stacked horizontal lines) and is (by default) located
     *      to the "start" of the toolbar.
     */
    public void setNavigationIcon(int iconId) {
        //set navigation icon
        mToolbar.setNavigationIcon(iconId);
    }

    /*
     * void setNavigationDescription(int) - set content description using string resource id
     */
    public void setNavigationDescription(int descriptionId){
        String description = mActivity.getString(descriptionId);
        setNavigationDescription(description);
    }

    /*
     * void setNavigationDescription(String) - set content description
     */
    public void setNavigationDescription(String description){
        mToolbar.setNavigationContentDescription(description);
    }


    /*
     * void setNavigationOnClickListener(View.OnClickListener) - set navigation onClickListener
     */
    public void setNavigationOnClickListener(View.OnClickListener listener){
        mToolbar.setNavigationOnClickListener(listener);
    }

/**************************************************************************************************/

}
