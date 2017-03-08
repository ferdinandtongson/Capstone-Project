package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.ClientAppCardItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;

/**************************************************************************************************/
/*
 * ClientAppAdapter extends RecyclerView.Adapter. It displays a list of appointment card items with
 * each card displaying appointment date, time and routine name used in appointment
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

public class ClientAppAdapter extends RecyclerView.Adapter<ClientAppAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<ClientAppCardItem> mData - an array list of item data consumed by the adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 *      OnClickListener mIconClickListener - icon event click listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<ClientAppCardItem> mData;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mOnClickListener - on card event click listener
    private static View.OnClickListener mOnClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * AppointmentAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * AppointmentAdapter - constructor
     */
    public ClientAppAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

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
 *      ClientAppCardItem getItem(int) - get item at given position
 *      ArrayList<ClientAppCardItem> getData() - get array data used by recycler
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
     * ClientAppCardItem getItem(int) - get item at given position
     */
    public ClientAppCardItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<ClientAppCardItem> getData() - get array data used by recycler
     */
    public ArrayList<ClientAppCardItem> getData(){ return mData; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Listener Methods:
 *      void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
 */
/**************************************************************************************************/
    /*
     * void setOnLongClickListener(...) - set listener for onLongClick event
     */
    public void setOnLongClickListener(View.OnLongClickListener listener){
        mOnLongClickListener = listener;
    }

    public void setOnClickListener(View.OnClickListener listener){
        mOnClickListener = listener;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(ClientAppCardItem) - dynamically add items to adapter
 *      void adItemAt(ClientAppCardItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<ClientAppCardItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(ClientAppCardItem) - dynamically add items to adapter
     */
    public void addItem(ClientAppCardItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(ClientAppCardItem,int) - add item to adapter at specific position
     */
    public void addItemAt(ClientAppCardItem item, int position){
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
     * void swapData(ArrayList<ClientAppCardItem>) - swap old data with new data
     */
    public void swapData(ArrayList<ClientAppCardItem> data){
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
            ClientAppCardItem item = mData.get(position);

            //bind viewHolder components
            holder.bindCardView(item, position);
            holder.bindTextView(item);
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

    private TextView mTxtTime;
    private TextView mTxtDate;
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
        mTxtTime = (TextView)recycleView.findViewById(R.id.cardClientApp_txtTime);
        mTxtDate = (TextView)recycleView.findViewById(R.id.cardClientApp_txtDate);
        mTxtRoutine = (TextView)recycleView.findViewById(R.id.cardClientApp_txtRoutine);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(ClientAppCardItem,int,int) - bind data to cardView
 *      void bindTextView(ClientAppCardItem) - bind data to textView components
 *      void bindProfileImage(ClientAppCardItem) - bind data to circleImageView
 *      void bindIconImages(ClientAppCardItem) - bind data to imageView icons
 */
/**************************************************************************************************/
    /*
     * void bindCardView(ClientAppCardItem,int,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    private void bindCardView(ClientAppCardItem item, int position) {

        mCardView.setTag(R.string.recycler_tagPosition, position);
        mCardView.setTag(R.string.recycler_tagItem, item);

        if(mOnLongClickListener != null){
            mCardView.setOnLongClickListener(mOnLongClickListener);
        }

        if(mOnClickListener != null){
            mCardView.setOnClickListener(mOnClickListener);
        }

    }

    /*
     * void bindTextView(ClientAppCardItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(ClientAppCardItem item){
        mTxtDate.setText(item.appointmentDate);
        mTxtDate.setContentDescription(item.appointmentDate);

        mTxtTime.setText(DateTimeHelper.convert24Hour(item.appointmentTime));
        mTxtTime.setContentDescription(DateTimeHelper.convert24Hour(item.appointmentTime));

        mTxtRoutine.setText(item.routineName);
        mTxtRoutine.setContentDescription(item.routineName);
    }

}

/**************************************************************************************************/

}
