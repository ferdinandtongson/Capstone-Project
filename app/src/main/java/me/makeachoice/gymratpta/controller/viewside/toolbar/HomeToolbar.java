package me.makeachoice.gymratpta.controller.viewside.toolbar;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
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
 */
/**************************************************************************************************/

    private final int DEFAULT_TOOLBAR_ID = R.id.choiceToolbar;
    private final int DEFAULT_MENU_ID = R.menu.toolbar_menu;
    private final int DEFAULT_NAV_ICON_ID = R.drawable.gym_rat_black_right_48dp;
    private final int DEFAULT_OVERFLOW_ICON_ID = R.drawable.ic_more_vert_white_36dp;
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

        int bgColor = DeprecatedUtility.getColor(mActivity, R.color.orange);
        int textColor = DeprecatedUtility.getColor(mActivity, R.color.white);
        mToolbar.setBackgroundColor(bgColor);
        mToolbar.setTitleTextColor(textColor);
        mToolbar.setSubtitleTextColor(textColor);

        Drawable drawable = DeprecatedUtility.getDrawable(mActivity, DEFAULT_OVERFLOW_ICON_ID);
        mToolbar.setOverflowIcon(drawable);

        mToolbar.setNavigationContentDescription("Open Drawer Navigation");
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

    public View getToolbarNavigationIcon(){
        //check if contentDescription previously was set
        boolean hadContentDescription = TextUtils.isEmpty(mToolbar.getNavigationContentDescription());

        String contentDescription;
        if(hadContentDescription){
            contentDescription = mToolbar.getNavigationContentDescription().toString();
        }
        else{
            contentDescription = "navigationIcon";
            mToolbar.setNavigationContentDescription(contentDescription);
        }

        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        mToolbar.findViewsWithText(potentialViews,contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
        View navIcon = null;
        if(potentialViews.size() > 0){
            navIcon = potentialViews.get(0); //navigation icon is ImageButton
        }
        //Clear content description if not previously present
        if(hadContentDescription)
            mToolbar.setNavigationContentDescription(null);
        return navIcon;
    }
}
