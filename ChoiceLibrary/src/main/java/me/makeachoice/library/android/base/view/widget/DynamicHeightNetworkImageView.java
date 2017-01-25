package me.makeachoice.library.android.base.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**************************************************************************************************/
/*
 * DynamicHeightNetworkImageView extends NetworkImageView taken from volley.jar
 */
/**************************************************************************************************/

public class DynamicHeightNetworkImageView extends NetworkImageView {

/**************************************************************************************************/
/*
 * Class Variables:
 *      float mAspectRatio - aspect ratio used for image
 *      int mErrorResId - resource error image id
 *      DynamicHeightNetworkImageView.LoadImageListener mListener - load image interface
 *
 * LoadImageListener Interface:
 *      void onLoadImageError() - error image resource id is being loaded
 *      void onLoadImage() - bitmap image has been loaded
 */
/**************************************************************************************************/

    //mAspectRatio - aspect ratio used for image
    private float mAspectRatio = 1.5f;

    //mErrorResId - resource error image id
    private int mErrorResId;

    //mListener - load image interface
    private DynamicHeightNetworkImageView.LoadImageListener mListener;

    public interface LoadImageListener
    {
        //error image resource id is being loaded
        public void onLoadImageError();

        //bitmap image has been loaded
        public void onLoadImage();
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DynamicHeightNetworkImageView - constructors
 */
/**************************************************************************************************/
    /*
     * DynamicHeightNetworkImageView(Context) - constructor
     * @param context - activity or application context
     */
    public DynamicHeightNetworkImageView(Context context) {
        super(context);
    }

    /*
     * DynamicHeightNetworkImageView(Context) - constructor
     * @param context - activity or application context
     * @param attrs - attributes of view object
     */
    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * DynamicHeightNetworkImageView(Context) - constructor
     * @param context - activity or application context
     * @param attrs - attributes of view object
     * @param defStyle - style of view object
     */
    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onMeasure(int,int) - measure with of object then set dimensions to aspect ratio
 *      void setErrorImageResId(int) - set resource error image id
 *      void setImageResource(int) - set resource image id
 *      void setImageBitmap(Bitmap) - load bitmap image
 *      void setListener(DynamicHeightNetworkImageView.Listener) - set listener interface
 *      void setAspectRatio(float) - set aspect ratio for image
 */
/**************************************************************************************************/
    /*
     * void onMeasure(int,int) - measure with of object then set dimensions to aspect ratio
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get width of imageView
        int measuredWidth = getMeasuredWidth();

        //set measured dimension of imageView to aspect ratio
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
    }

    /*
     * void setErrorImageResId(int) - set resource error image id
     */
    @Override
    public void setErrorImageResId(int errorImage) {
        //save resource error image id
        mErrorResId = errorImage;
        super.setErrorImageResId(errorImage);
    }

    /*
     * void setImageResource(int) - set resource image id
     */
    @Override
    public void setImageResource(int resId) {
        //check image resource id
        if (resId == mErrorResId) {
            //image id is load error image id, check for listener
            if(mListener != null){
                //notify listener, load error image is being used
                mListener.onLoadImageError();
            }
        }
        super.setImageResource(resId);
    }

    /*
     * void setImageBitmap(Bitmap) - load bitmap image
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        //check for listener
        if(mListener != null){
            //notify listener, bitmap image has been loaded
            mListener.onLoadImage();
        }
        super.setImageBitmap(bm);
    }

    /*
     * void setListener(DynamicHeightNetworkImageView.Listener) - set listener interface
     */
    public void setListener(DynamicHeightNetworkImageView.LoadImageListener listener){
        //save listener
        mListener = listener;
    }

    /*
     * void setAspectRatio(float) - set aspect ratio for image
     */
    public void setAspectRatio(float aspectRatio) {
        //save ratio to variable
        mAspectRatio = aspectRatio;

        //request layout change
        requestLayout();
    }


/**************************************************************************************************/

}
