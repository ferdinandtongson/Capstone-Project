package me.makeachoice.gymratpta.controller.viewside.drawer;

import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.R.attr.textColor;

/**************************************************************************************************/
/*
 * todo - description
 */
/**************************************************************************************************/

public class HomeDrawer{

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private final int DEFAULT_DRAWER_ID = R.id.choiceDrawer;
    private final int DEFAULT_MENU_ID = R.menu.drawer_menu;
    private final int DEFAULT_NAV_ICON_ID = R.drawable.gym_rat_black_right_48dp;
    private final int DEFAULT_TITLE_ID = R.string.app_title;
    private final int DEFAULT_SUBTITLE_ID = R.string.app_subtitle;

    private MyActivity mActivity;
    private DrawerLayout mDrawer;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * HomeToolbar - constructor & initialization methods
 */
/**************************************************************************************************/
    /*
     * HomeToolbar(MyActivity) - class constructor
     */
    public HomeDrawer(MyActivity activity){
        //current activity
        mActivity = activity;

        String title = mActivity.getResources().getString(DEFAULT_TITLE_ID);
        String subtitle = mActivity.getResources().getString(DEFAULT_SUBTITLE_ID);

        initialize(DEFAULT_DRAWER_ID, DEFAULT_MENU_ID, DEFAULT_NAV_ICON_ID, title, subtitle);
    }

    public HomeDrawer(MyActivity activity, int drawerId, int menuId, int iconId){
        //current activity
        mActivity = activity;

        String title = mActivity.getResources().getString(DEFAULT_TITLE_ID);
        String subtitle = mActivity.getResources().getString(DEFAULT_SUBTITLE_ID);

        initialize(drawerId, menuId, iconId, title, subtitle);
    }

    public HomeDrawer(MyActivity activity, int drawerId, int menuId, int iconId, String title, String subtitle){
        //current activity
        mActivity = activity;

        initialize(drawerId, menuId, iconId, title, subtitle);
    }

    /*
     * void initialize(int,int) - initializes toolbar
     */
    //@Override
    protected void initialize(int drawerId, int menuId, int iconId, String title, String subtitle){
        int bgColor = DeprecatedUtility.getColor(mActivity, R.color.orange);
        int textColor = DeprecatedUtility.getColor(mActivity, R.color.white);

        mDrawer = (DrawerLayout)mActivity.findViewById(drawerId);

        /*ImageView imgIcon = (ImageView)mActivity.findViewById(R.id.drawerNav_imgIcon);

        Drawable drawableIcon = DeprecatedUtility.getDrawable(mActivity, iconId);
        imgIcon.setBackground(drawableIcon);

        TextView txtTitle = (TextView)mActivity.findViewById(R.id.drawerNav_txtTitle);
        TextView txtSubtitle = (TextView)mActivity.findViewById(R.id.drawerNav_txtSubtitle);

        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
        txtTitle.setTextColor(textColor);
        txtSubtitle.setTextColor(textColor);

        ImageView imgBackground = (ImageView)mActivity.findViewById(R.id.drawerNav_imgBackground);

        imgBackground.setBackgroundColor(bgColor);*/
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
    /*public boolean optionsItemSelected(MenuItem item){
        if(mOnMenuClick != null){
            mOnMenuClick.onMenuItemClick(item);
        }

        return false;
    }*/

    public void openDrawer(){
        mDrawer.openDrawer(GravityCompat.START);
    }

/**************************************************************************************************/

}
