package me.makeachoice.gymratpta.controller.viewside.toolbar;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.makeachoice.gymratpta.R;
import me.makeachoice.library.android.base.controller.viewside.bartender.MyBartender;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * HomeToolbar manages the toolbar for activities using only a simple title display
 *
 * Variables from MyBartender:
 *      MyActivity mActivity
 *      Toolbar mToolbar
 *      int mMenuId
 *
 * Variables from MyTitleBartender
 *      int TXT_TITLE_ID
 *      TextView mTxtTitle
 *
 * Variables from MyImageLogoBartender:
 *      int IMG_LOGO_ID - resource id number for imageView Logo object
 *      ImageView mImgLogo - logo imageView object
 *
 * Methods From MyBartender
 *      void createOptionsMenu() - called by Activity to inflate menu
 *      boolean optionsItemSelected(MenuItem) - called when a menu item is click
 *      void setOverflowIcon(int) - set overflow icon
 *      void setNavigationIcon(int) - set navigation icon
 *      void setNavigationDescription(int) - set content description using string resource id
 *      void setNavigationDescription(String) - set content description
 *      void setNavigationOnClickListener(View.OnClickListener) - set navigation onClickListener
 *
 * Methods from MyTitleBartender
 *      void initialize() - initializes the toolbar
 *      TextView getTextViewTitle() - get textView Title component
 *      void setTitle(int) - set title using string resource id
 *      void setTitle(String) - set title
 *      void setTitleDescription(int) - set content description using string resource id
 *      void setTitleDescription(String) - set content description
 *
 * Methods from MyImageLogoBartender
 *      void initialize() - initializes the toolbar
 *      ImageView getImageViewLogo() - get ImageView Logo component
 *      void setLogo(int) - set logo using drawable resource id
 *      void setLogoDescription(int) - set content description using string resource id
 *      void setLogoDescription(String) - set content description
 */

/**************************************************************************************************/


public class HomeToolbar extends MyBartender implements MyActivity.OptionsMenuBridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      Bridge mBridge - communication bridge
 */
/**************************************************************************************************/

    private final int DEFAULT_TOOLBAR_ID = R.id.choiceToolbar;
    private final int DEFAULT_MENU_ID = R.menu.toolbar_home;
    private final int DEFAULT_NAV_ICON_ID = R.drawable.gym_rat_right_48dp;
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
    public HomeToolbar(MyActivity activity){
        //current activity
        mActivity = activity;

        mMenuId = DEFAULT_MENU_ID;
        String title = mActivity.getResources().getString(DEFAULT_TITLE_ID);
        String subtitle = mActivity.getResources().getString(DEFAULT_SUBTITLE_ID);

        initialize(DEFAULT_TOOLBAR_ID, DEFAULT_NAV_ICON_ID, title, subtitle);
    }

    public HomeToolbar(MyActivity activity, int toolbarId, int menuId, int iconId){
        //current activity
        mActivity = activity;

        mMenuId = menuId;
        String title = mActivity.getResources().getString(DEFAULT_TITLE_ID);
        String subtitle = mActivity.getResources().getString(DEFAULT_SUBTITLE_ID);

        initialize(toolbarId, iconId, title, subtitle);
    }

    public HomeToolbar(MyActivity activity, int toolbarId, int menuId, int iconId, String title, String subtitle){
        //current activity
        mActivity = activity;

        mMenuId = menuId;

        initialize(toolbarId, iconId, title, subtitle);
    }

    /*
     * void initialize(int,int) - initializes toolbar
     * @param toolbarId - resource id of toolbar object
     * @param iconId - drawable resource id for navigation icon
     */
    //@Override
    protected void initialize(int toolbarId, int iconId, String title, String subtitle){
        mToolbar = (Toolbar) mActivity.findViewById(toolbarId);

        //set navigation icon
        mToolbar.setNavigationIcon(iconId);

        //enable toolbar to act as an actionbar
        mActivity.setSupportActionBar(mToolbar);

        //set title and subtitle of toolbar, can only be called after setSupportActionBar()
        setTitle(title, subtitle);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * User Event Methods:
 *      optionsItemSelected(MenuItem) - called when a menu item is click
 */
/**************************************************************************************************/
    /*
     * boolean optionsItemSelected(MenuItem) - called when a menu item is click
     */
    public boolean optionsItemSelected(MenuItem item){
        if(mOnMenuClick != null){
            mOnMenuClick.onMenuItemClick(item);
        }

        return false;
    }

/**************************************************************************************************/

}
