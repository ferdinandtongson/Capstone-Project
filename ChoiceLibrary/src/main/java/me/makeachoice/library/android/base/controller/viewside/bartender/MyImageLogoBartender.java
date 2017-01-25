package me.makeachoice.library.android.base.controller.viewside.bartender;

import android.view.MenuItem;
import android.widget.ImageView;

import me.makeachoice.library.android.base.R;

/**************************************************************************************************/
/*
 * MyImageLogoBartender manages a Toolbar object with a simple title, optional menu items, and an logo
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
 * Methods From MyBartender
 *      void createOptionsMenu() - called by Activity to inflate menu
 *      void setOverflowIcon(int) - set overflow icon
 *      void setNavigationIcon(int) - set navigation icon
 *      void setNavigationDescription(int) - set content description using string resource id
 *      void setNavigationDescription(String) - set content description
 *      void setNavigationOnClickListener(View.OnClickListener) - set navigation onClickListener
 *      abstract boolean optionsItemSelected(MenuItem) - called when a menu item is click
 *
 * Methods from MyTitleBartender
 *      void initialize() - initializes the toolbar
 *      TextView getTextViewTitle() - get textView Title component
 *      void setTitle(int) - set title using string resource id
 *      void setTitle(String) - set title
 *      void setTitleDescription(int) - set content description using string resource id
 *      void setTitleDescription(String) - set content description
 *
 *  TODO - add ripple effect on image??
 *  TODO - test if content description is enabled, do I need to set an onClick listener?
 *
 *  NOTE: Logo layout gravity (start, center, end) can be set in styles_activity.xml
 */
/**************************************************************************************************/

public abstract class MyImageLogoBartender extends MyTitleBartender{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int IMG_LOGO_ID - resource id number for imageView Logo object
 *      ImageView mImgLogo - logo imageView object
 */
/**************************************************************************************************/

    //IMG_LOGO_ID - resource id number for imageView Logo object
    protected final static int IMG_LOGO_ID = R.id.choiceToolbar_imgLogo;

    //mImgLogo - logo imageView object
    private ImageView mImgLogo;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Protected Methods:
 *      void initialize() - initializes the toolbar
 *      ImageView getImageViewLogo() - get ImageView Logo component
 *      void setLogo(int) - set logo using drawable resource id
 *      void setLogoDescription(int) - set content description using string resource id
 *      void setLogoDescription(String) - set content description
 */
/**************************************************************************************************/
/*
 * void initialize() - initializes the toolbar
 */
    @Override
    protected void initialize(int toolbarId, int menuId){
        super.initialize(toolbarId, menuId);

        //get imageView object for logo
        mImgLogo = (ImageView)mActivity.findViewById(IMG_LOGO_ID);
    }

    /*
     * ImageView getImageViewLogo() - get ImageView Logo component
     */
    public ImageView getImageViewLogo(){
        return mImgLogo;
    }

    /*
     * void setLogo(int) - set logo using drawable resource id
     */
    public void setLogo(int imageId){
        mImgLogo.setImageResource(imageId);
    }

    /*
     * void setLogoDescription(int) - set content description using string resource id
     */
    public void setLogoDescription(int descriptionId){
        String description = mActivity.getString(descriptionId);
        setLogoDescription(description);
    }

    /*
     * void setLogoDescription(String) - set content description
     */
    public void setLogoDescription(String description){
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
