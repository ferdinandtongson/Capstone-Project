package me.makeachoice.library.android.base.controller.viewside.bartender;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import me.makeachoice.library.android.base.R;

/**************************************************************************************************/
/*
 * MyTitleBartender manages a Toolbar object with a simple title and optional menu items
 *
 * Variables from MyBartender:
 *      MyActivity mActivity
 *      Toolbar mToolbar
 *      int mMenuId
 *
 * Methods From MyBartender
 *      void createOptionsMenu() - called by Activity to inflate menu
 *      void setOverflowIcon(int) - set overflow icon
 *      void setNavigationIcon(int) - set navigation icon
 *      void setNavigationDescription(int) - set content description using string resource id
 *      void setNavigationDescription(String) - set content description
 *      void setNavigationOnClickListener(View.OnClickListener) - set navigation onClickListener
 *      abstract boolean optionsItemSelected(MenuItem) - called when a menu item is click
 *
 *  TODO - add ripple effect on title bar
 *  TODO - test if content description is enabled, do I need to set an onClick listener?
 */
/**************************************************************************************************/

public abstract class MyTitleBartender extends MyBartender{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int TXT_TITLE_ID - resource id number for textView Title object
 *      TextView mTxtTitle - title textView object
 */
/**************************************************************************************************/

    //TXT_TITLE_ID - resource id number for textView Title object
    protected final static int TXT_TITLE_ID = R.id.choiceToolbar_txtTitle;

    //mTxtTitle - title textView object
    protected TextView mTxtTitle;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Protected Methods:
 *      void initialize() - initializes the toolbar
 *      TextView getTextViewTitle() - get textView Title component
 *      void setTitle(int) - set title using string resource id
 *      void setTitle(String) - set title
 *      void setTitleDescription(int) - set content description using string resource id
 *      void setTitleDescription(String) - set content description
 */
/**************************************************************************************************/
    /*
     * void initialize() - initializes the toolbar
     */
    protected void initialize(int toolbarID, int menuID){
        //get menu id to inflate, needs to be set before calling setSupportActionBar()
        mMenuId = menuID;

        //get toolbar layout
        mToolbar = (Toolbar) mActivity.findViewById(toolbarID);

        //enable toolbar to act as an actionbar
        mActivity.setSupportActionBar(mToolbar);

        //make sure actionbar does not displayed title
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //get textView object for title
        mTxtTitle = (TextView)mActivity.findViewById(TXT_TITLE_ID);
    }

    /*
     * TextView getTextViewTitle() - get textView Title component
     */
    public TextView getTextViewTitle(){
        return mTxtTitle;
    }

    /*
     * void setTitle(int) - set title using string resource id
     */
    public void setTitle(int titleId){
        String title = mActivity.getString(titleId);
        setTitle(title);
    }

    /*
     * void setTitle(String) - set title
     */
    public void setTitle(String title){
        mTxtTitle.setText(title);
    }

    /*
     * void setTitleDescription(int) - set content description using string resource id
     */
    public void setTitleDescription(int descriptionId){
        String description = mActivity.getString(descriptionId);
        setTitleDescription(description);
    }

    /*
     * void setTitleDescription(String) - set content description
     */
    public void setTitleDescription(String description){
        mTxtTitle.setContentDescription(description);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods:
 *      optionsItemSelected(MenuItem) - called when a menu item is click
 */
/**************************************************************************************************/
/*
 * boolean optionsItemSelected(MenuItem) - called when a menu item is click
 * @param item - menu item clicked
 * @return true
 */
    public abstract boolean optionsItemSelected(MenuItem item);


/**************************************************************************************************/

}
