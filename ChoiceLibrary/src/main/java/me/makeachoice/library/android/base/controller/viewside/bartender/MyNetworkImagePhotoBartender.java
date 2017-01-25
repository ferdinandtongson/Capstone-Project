package me.makeachoice.library.android.base.controller.viewside.bartender;

import android.content.Context;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;

import me.makeachoice.library.android.base.R;
import me.makeachoice.library.android.base.view.utilities.NetworkImageLoaderHelper;
import me.makeachoice.library.android.base.view.widget.DynamicHeightNetworkImageView;

/**************************************************************************************************/
/*
 * MyNetworkImagePhotoBartender manages a Toolbar object with a simple title, optional menu items,
 * and an logo
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

public abstract class MyNetworkImagePhotoBartender extends MyTitleBartender{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int IMG_PHOTO_ID - resource id number for networkImageView photo object
 *      NetworkImageView mImgPhoto - photo networkImageView object
 */
/**************************************************************************************************/

    //IMG_PHOTO_ID - resource id number for networkImageView photo object
    protected final static int IMG_PHOTO_ID = R.id.choiceToolbar_imgPhoto;

    //mImgPhoto - photo networkImageView object
    private DynamicHeightNetworkImageView mImgPhoto;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Protected Methods:
 *      void initialize() - initializes the toolbar
 *      DynamicHeightNetworkImageView getImageViewPhoto() - get NetworkImageView photo component
 *      void setPhoto(String) - set photo using url string
 *      void setPhotoDescription(int) - set content description using string resource id
 *      void setPhotoDescription(String) - set content description
 */
/**************************************************************************************************/
/*
 * void initialize() - initializes the toolbar
 */
    @Override
    protected void initialize(int toolbarId, int menuId){
        super.initialize(toolbarId, menuId);

        //get imageView object for photo
        mImgPhoto = (DynamicHeightNetworkImageView)mActivity.findViewById(IMG_PHOTO_ID);
    }

    /*
     * DynamicHeightNetworkImageView getImageViewPhoto() - get ImageView photo component
     */
    public DynamicHeightNetworkImageView getImageViewPhoto(){
        return mImgPhoto;
    }

    /*
     * void setPhoto(Context,String) - set photo using url
     * @param context - activity context
     * @param photoUrl - url string to photo resource
     */
    public void setPhoto(Context context, String photoUrl){
        //get imageLoader component used by volley.jar to load images from net
        ImageLoader loader = NetworkImageLoaderHelper.getInstance(context).getImageLoader();

        //set image
        mImgPhoto.setImageUrl(photoUrl, loader);
    }

    /*
     * void setPhotoDescription(int) - set content description using string resource id
     */
    public void setPhotoDescription(int descriptionId){
        String description = mActivity.getString(descriptionId);
        setPhotoDescription(description);
    }

    /*
     * void setPhotoDescription(String) - set content description
     */
    public void setPhotoDescription(String description){
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
