package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;

/**************************************************************************************************/
/*
 * AppointmentAdapter extends RecyclerView.Adapter. It displays a list of appointment card items with
 * each card displaying the profile image of the client, client name, routine name and time of appointment
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

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *      int mCardDefaultColor - default background card color
 *      int mCardRetiredColor - background card color used when client is retired
 *      String mStrRetired - "Retired" string value
 *
 *      ArrayList<AppointmentCardItem> mData - an array list of item data consumed by the adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 *      OnClickListener mIconClickListener - icon event click listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mCardDefaultColor - default background card color
    private int mCardDefaultColor;

    //mCardRetiredColor - background card color used when client is retired
    private int mCardRetiredColor;

    //mStrRetired - "Retired" string value
    private String mStrRetired;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<AppointmentCardItem> mData;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mImageClickListener - profile image event click listener
    private static View.OnClickListener mImageClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * AppointmentAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * AppointmentAdapter - constructor
     */
    public AppointmentAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //get colors used as card background color
        mCardDefaultColor = DeprecatedUtility.getColor(mContext, R.color.card_activeBackground);
        mCardRetiredColor = DeprecatedUtility.getColor(mContext, R.color.card_retiredBackground);

        //get "retired" status string value
        mStrRetired = mContext.getString(R.string.retired);

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //initialize data array
        mData = new ArrayList<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods:
 *      int getItemCount() - get number of items in adapter
 *      AppointmentCardItem getItem(int) - get item at given position
 *      ArrayList<AppointmentCardItem> getData() - get array data used by recycler
 */
/**************************************************************************************************/
    /*
     * int getItemCount() - get number of items in adapter
     */
    @Override
    public int getItemCount(){
        if(mData != null){
            //return number of items
            return mData.size();
        }
        return 0;
    }

    /*
     * AppointmentCardItem getItem(int) - get item at given position
     */
    public AppointmentCardItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<AppointmentCardItem> getData() - get array data used by recycler
     */
    public ArrayList<AppointmentCardItem> getData(){ return mData; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Listener Methods:
 *      void setOnIconClickListener(View.OnClickListener) - set listener for image icon click events
 *      void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
 */
/**************************************************************************************************/
    /*
     * void setOnImageClickListener(View.OnClickListener) - set listener for image icon click events
     */
    public void setOnImageClickListener(View.OnClickListener listener){
        mImageClickListener = listener;
    }

    /*
     * void setOnLongClickListener(...) - set listener for onLongClick event
     */
    public void setOnLongClickListener(View.OnLongClickListener listener){
        mOnLongClickListener = listener;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(AppointmentCardItem) - dynamically add items to adapter
 *      void adItemAt(AppointmentCardItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<AppointmentCardItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(AppointmentCardItem) - dynamically add items to adapter
     */
    public void addItem(AppointmentCardItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(AppointmentCardItem,int) - add item to adapter at specific position
     */
    public void addItemAt(AppointmentCardItem item, int position){
        //add item object to data array at position
        mData.add(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void removeItemAt(int) - remove item from adapter then refresh adapter
     */
    public void removeItemAt(int position){
        //remove item at given position
        mData.remove(position);

        //notify item was removed
        this.notifyItemRemoved(position);
        //notify data range has changed
        this.notifyItemRangeRemoved(position, mData.size());
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void swapData(ArrayList<AppointmentCardItem>) - swap old data with new data
     */
    public void swapData(ArrayList<AppointmentCardItem> data){
        //clear old data
        mData.clear();
        //add new data
        mData.addAll(data);
        //notify adapter of data change
        notifyDataSetChanged();
    }

    /*
     * void clearData() - remove all data from adapter
     */
    public void clearData(){
        //clear data
        mData.clear();
        //notify adapter of data change
        notifyDataSetChanged();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * Implemented Methods from RecyclerView.Adapter:
 *      ViewHolder onCreateViewHolder(ViewGroup, int)
 *      void onBindViewHolder(ViewHolder, int)
 */
/**************************************************************************************************/
    /*
     * ViewHolder onCreateViewHolder(ViewGroup, int) - inflates the layout and creates an instance
     * of the ViewHolder (PosterHolder) class. This instance is used to access the views in the
     * inflated layout and is only called when a new view must be created.
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //inflate the itemView
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(mItemLayoutId, viewGroup, false);

        //return ViewHolder
        return new MyViewHolder(itemView);
    }

    /*
     * void onBindViewHolder(ViewHolder, int) - where we bind our data to the views
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(mData.size() > 0){
            // Extract info from cursor
            AppointmentCardItem item = mData.get(position);

            //set default card color background
            int cardColor = mCardDefaultColor;

            //check client status, if false
            if(!item.isActive){
                //change card color background
                cardColor = mCardRetiredColor;
            }

            //bind viewHolder components
            holder.bindCardView(item, position, cardColor);
            holder.bindTextView(item);
            holder.bindProfileImage(item, position);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ReviewHolder - extends RecyclerView.ViewHolder, a design pattern to increase performance. It
 * holds the references to the UI components for each item in a ListView or GridView
 */
/**************************************************************************************************/

public static class MyViewHolder extends RecyclerView.ViewHolder{

/**************************************************************************************************/
/*
 * Child Views used by the ReviewRecycler
 */
/**************************************************************************************************/

    //mCardView - view that hold child views found below
    private CardView mCardView;

    private CircleImageView mImgProfile;
    private TextView mTxtInfo;
    private TextView mTxtName;
    private TextView mTxtRoutine;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - constructor
 */
/**************************************************************************************************/
    /*
     * MyViewHolder - constructor
     */
    public MyViewHolder(View recycleView){
        super(recycleView);
        //get view components held by ViewHolder
        mCardView = (CardView)recycleView.findViewById(R.id.choiceCardView);
        mImgProfile = (CircleImageView)recycleView.findViewById(R.id.cardAppointment_imgProfile);
        mTxtInfo = (TextView)recycleView.findViewById(R.id.cardAppointment_txtInfo);
        mTxtName = (TextView)recycleView.findViewById(R.id.cardAppointment_txtName);
        mTxtRoutine = (TextView)recycleView.findViewById(R.id.cardAppointment_txtRoutine);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(AppointmentCardItem,int,int) - bind data to cardView
 *      void bindTextView(AppointmentCardItem) - bind data to textView components
 *      void bindProfileImage(AppointmentCardItem) - bind data to circleImageView
 *      void bindIconImages(AppointmentCardItem) - bind data to imageView icons
 */
/**************************************************************************************************/
    /*
     * void bindCardView(AppointmentCardItem,int,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    private void bindCardView(AppointmentCardItem item, int position, int cardColor) {

        mCardView.setTag(R.string.recycler_tagPosition, position);
        mCardView.setTag(R.string.recycler_tagItem, item);
        mCardView.setCardBackgroundColor(cardColor);

        if(mOnLongClickListener != null){
            mCardView.setOnLongClickListener(mOnLongClickListener);
        }

    }

    /*
     * void bindTextView(AppointmentCardItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(AppointmentCardItem item){
        mTxtName.setText(item.clientName);
        mTxtName.setContentDescription(item.clientName);

        mTxtInfo.setText(item.clientInfo);
        mTxtInfo.setContentDescription(item.clientInfo);

        mTxtRoutine.setText(item.routineName);
        mTxtRoutine.setContentDescription(item.routineName);
    }

    /*
     * void bindProfileImage(AppointmentCardItem) - bind data to circleImageView. Using Picasso, load the
     * image profile of client
     */
    private void bindProfileImage(AppointmentCardItem item, int position){
        mImgProfile.setTag(R.string.recycler_tagPosition, position);
        mImgProfile.setTag(R.string.recycler_tagItem, item);
        mImgProfile.setOnClickListener(mImageClickListener);

        Picasso.with(itemView.getContext())
                .load(item.profilePic)
                .placeholder(R.drawable.gym_rat_black_48dp)
                .error(R.drawable.gym_rat_black_48dp)
                .into(mImgProfile);
    }

}

/**************************************************************************************************/

}
