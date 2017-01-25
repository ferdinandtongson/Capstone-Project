package me.makeachoice.library.android.base.controller.viewside.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.makeachoice.library.android.base.R;
import me.makeachoice.library.android.base.view.widget.DynamicHeightNetworkImageView;

/**************************************************************************************************/
/*
 * DHNetworkImageViewAdapter extends RecyclerView.Adapter. Displays a DynamicHeight NetworkImageView
 * (extended NetworkImageView from volley.jar) with a title and subtitle textView.
 *
 * Methods from RecyclerView.Adapter:
 *      int getItemCount()
 *      ViewHolder onCreateViewHolder(ViewGroup, int)
 *      void onBindViewHolder(ViewHolder, int)
 *
 * Inner Class:
 *      ReviewHolder extends RecyclerView.ViewHolder
 */

/**************************************************************************************************/

public abstract class DHNetworkImageViewAdapter extends
        RecyclerView.Adapter<DHNetworkImageViewAdapter.ViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      int DEFAULT_LAYOUT_ID - default layout resource id
 *      int DEFAULT_CARD_ID - default cardView id
 *      int DEFAULT_IMG_THUMBNAIL_ID - default networkImageView thumbnail id
 *      int DEFAULT_TXT_TITLE_ID - default textView title id
 *      int DEFAULT_TXT_SUBTITLE_ID - default textView subtitle id
 *
 *      int mLayoutId - xml layout resource id number
 *      mCardId - xml cardView resource id number
 *      mImgThumbnailId - xml networkImageView resource id number
 *      mTxtTitleId - xml textView resource id number
 *      mTxtSubtitleId - xml textView resource id number
 *
 *      Cursor mCursor - cursor data consumed by adapter
 *      Bridge mBridge - class implementing Bridge interface, typically a Maid class
 *
 * Bridge Interface:
 *      Context getActivityContext() - get current Activity context
 *      void cardClickEvent(int) - card onClickEvent
 */
/**************************************************************************************************/

    //DEFAULT_LAYOUT_ID - default layout resource id
    private static int DEFAULT_LAYOUT_ID = R.layout.card_dh_networkimageview;

    //DEFAULT_CARD_ID - default cardView id
    private static int DEFAULT_CARD_ID = R.id.choiceCardView;

    //DEFAULT_IMG_THUMBNAIL_ID - default imageView thumbnail id
    private static int DEFAULT_IMG_THUMBNAIL_ID = R.id.card_imgThumbnail;

    //DEFAULT_TXT_TITLE_ID - default textView title id
    private static int DEFAULT_TXT_TITLE_ID = R.id.card_txtTitle;

    //DEFAULT_TXT_SUBTITLE_ID - default textView subtitle id
    private static int DEFAULT_TXT_SUBTITLE_ID = R.id.card_txtSubtitle;

    //mLayoutId - xml layout resource id number
    protected int mLayoutId;

    //mCardId - xml cardView resource id number
    protected int mCardId;

    //mImgThumbnailId - xml networkImageView resource id number
    protected int mImgThumbnailId;

    //mTxtTitleId - xml textView resource id number
    protected int mTxtTitleId;

    //mTxtSubtitleId - xml textView resource id number
    protected int mTxtSubtitleId;

    //mCursor - cursor data consumed by adapter
    protected Cursor mCursor;

    //mBridge - class implementing Bridge, typically a Maid class
    protected Bridge mBridge;

    //Implemented communication line to a class
    public interface Bridge{
        //get current Activity context
        Context getActivityContext();

        //card onClickEvent
        void cardClickEvent(ViewHolder holder, int position);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DHNetworkImageViewAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * DHNetworkImageViewAdapter(Bridge,Cursor,int) - constructor
     * @param bridge - communication bridge
     * @param cursor - cursor data consumed by adapter
     */
    public DHNetworkImageViewAdapter(Bridge bridge, Cursor cursor){
        //communication bridge
        mBridge = bridge;

        //cursor data consumed by adapter
        mCursor = cursor;

        //set layout id
        mLayoutId = DEFAULT_LAYOUT_ID;

        //set cardView id
        mCardId = DEFAULT_CARD_ID;

        //set networkImageView thumbnail id
        mImgThumbnailId = DEFAULT_IMG_THUMBNAIL_ID;

        //set textView title id
        mTxtTitleId = DEFAULT_TXT_TITLE_ID;

        //set textView subtitle id
        mTxtSubtitleId = DEFAULT_TXT_SUBTITLE_ID;
    }

    /*
     * DHNetworkImageViewAdapter(Bridge,Cursor,int) - constructor
     * @param bridge - communication bridge
     * @param cursor - cursor data consumed by adapter
     * @param layoutId - xml layout resource id
     */
    public DHNetworkImageViewAdapter(Bridge bridge, Cursor cursor, int layoutId){

        //communication bridge
        mBridge = bridge;

        //cursor data consumed by adapter
        mCursor = cursor;

        //set layout id
        mLayoutId = layoutId;

        //set cardView id
        mCardId = DEFAULT_CARD_ID;

        //set imageView thumbnail id
        mImgThumbnailId = DEFAULT_IMG_THUMBNAIL_ID;

        //set textView title id
        mTxtTitleId = DEFAULT_TXT_TITLE_ID;

        //set textView subtitle id
        mTxtSubtitleId = DEFAULT_TXT_SUBTITLE_ID;
    }

    /*
     * DHNetworkImageViewAdapter(Bridge,Cursor,int) - constructor
     * @param bridge - communication bridge
     * @param cursor - cursor data consumed by adapter
     * @param layoutId - xml layout resource id
     * @param imgThumbnailId - imageView thumbnail id
     * @param txtTitleId - textView title id
     * @param txtSubtitleId - textView subtitle id
     */
    public DHNetworkImageViewAdapter(Bridge bridge, Cursor cursor, int layoutId, int cardId,
                                     int imgThumbnailId, int txtTitleId, int txtSubtitleId){

        //communication bridge
        mBridge = bridge;

        //cursor data consumed by adapter
        mCursor = cursor;

        //set layout id
        mLayoutId = layoutId;

        //set cardView id
        mCardId = cardId;

        //set imageView thumbnail id
        mImgThumbnailId = imgThumbnailId;

        //set textView title id
        mTxtTitleId = txtTitleId;

        //set textView subtitle id
        mTxtSubtitleId = txtSubtitleId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods
 */
/**************************************************************************************************/

    public abstract void bindHolder(ViewHolder holder, int position);

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      int getItemCount() - get number of items in cursor
 *      long getItemId(int) - get item id number in given cursor position
 */
/**************************************************************************************************/
    /*
     * int getItemCount() - get number of items in cursor
     * @return int - number of items in cursor
     */
    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Implemented Methods from RecyclerView.Adapter:
 *      ViewHolder onCreateViewHolder(ViewGroup, int) - inflates layout and creates ViewHolder
 *      void onBindViewHolder(ViewHolder, int) - bind data to the views
 */
/**************************************************************************************************/
    /*
     * ViewHolder onCreateViewHolder(ViewGroup, int) - inflates the layout and creates an instance
     * of the ViewHolder (PosterHolder) class. This instance is used to access the views in the
     * inflated layout and is only called when a new view must be created.
     * @param viewGroup - parent view that will hold the itemView, RecyclerView
     * @param i - position of the itemView
     * @return - ViewHolder class; ReviewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){

        //inflate the itemView
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(mLayoutId, viewGroup, false);

        //View view = getLayoutInflater().inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(itemView, mCardId, mImgThumbnailId, mTxtTitleId, mTxtSubtitleId);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBridge.cardClickEvent(vh, vh.getAdapterPosition());
            }
        });

        //return ViewHolder
        return vh;
    }

    /*
     * void onBindViewHolder(ViewHolder, int) - bind data to the views
     * @param holder - ViewHolder class; ReviewHolder
     * @param position - position of the itemView being bound
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindHolder(holder, position);
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * ViewHolder - extends RecyclerView.ViewHolder, a design pattern to increase performance. It
 * holds the references to the UI components for each item in a ListView or GridView
 */
/**************************************************************************************************/

    public static class ViewHolder extends RecyclerView.ViewHolder {

/**************************************************************************************************/
/*
 * Child Views of the used by the ArticleAdapter
 */
/**************************************************************************************************/

        //mCard - cardView holding all the child components
        public CardView mCardView;

        //mImgThumbnail - custom imageView displaying thumbnail image
        public DynamicHeightNetworkImageView mImgThumbnail;

        //mTxtTitle - textView that shows title of article
        public TextView mTxtTitle;

        //mTxtSubtitle - textView that shows subtitle of article
        public TextView mTxtSubtitle;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ViewHolder - constructor
 */
/**************************************************************************************************/
        /*
         * ViewHolder - constructor
         * @param view - item layout containing the child views
         */
        public ViewHolder(View view, int cardId, int imgThumbnailId, int txtTitleId, int txtSubtitleId) {
            super(view);

            //get cardView
            mCardView = (CardView) view.findViewById(cardId);

            //get thumbnail imageView
            mImgThumbnail = (DynamicHeightNetworkImageView) view.findViewById(imgThumbnailId);

            //get title textView
            mTxtTitle = (TextView) view.findViewById(txtTitleId);

            //get subtitle textView
            mTxtSubtitle = (TextView) view.findViewById(txtSubtitleId);
        }
    }

/**************************************************************************************************/

}
