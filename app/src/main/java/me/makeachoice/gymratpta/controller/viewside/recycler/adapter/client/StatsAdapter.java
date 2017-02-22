package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.StatsItem;

/**************************************************************************************************/
/*
 * StatsAdapter extends RecyclerView.Adapter. It displays a list of stats items with each card
 * displaying appointment date, time and stats
 *
 * Methods from RecyclerView.Adapter:
 *      int getItemCount()
 *      ViewHolder onCreateViewHolder(ViewGroup, int)
 *      void onBindViewHolder(ViewHolder, int)
 *
 * Inner Class:
 *      MyViewHolder extends RecyclerView.ViewHolder
 */
/**************************************************************************************************/

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<StatsItem> mData - an array list of item data consumed by the adapter
 *      mOnLongClickListener - list item onLongClick event listener
 *      mOnClickListener - list item onClick event click listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<StatsItem> mData;

    //mOnLongClickListener - list item onLongClick event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mOnClickListener - list item onClick event click listener
    private static View.OnClickListener mOnClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StatsAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * StatsAdapter - constructor
     */
    public StatsAdapter(Context ctx, int itemLayoutId){
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
 *      StatsItem getItem(int) - get item at given position
 *      ArrayList<StatsItem> getData() - get array data used by recycler
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
     * StatsItem getItem(int) - get item at given position
     */
    public StatsItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<StatsItem> getData() - get array data used by recycler
     */
    public ArrayList<StatsItem> getData(){ return mData; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Listener Methods:
 *      void setOnLongClickListener(...) - set list item onLongClick listener
 *      void setOnClickListener(...) - sett list item onClick listener
 */
/**************************************************************************************************/
    /*
     * void setOnLongClickListener(...) - set list item onLongClick listener
     */
    public void setOnLongClickListener(View.OnLongClickListener listener){
        mOnLongClickListener = listener;
    }

    /*
     * void setOnClickListener(...) - sett list item onClick listener
     */
    public void setOnClickListener(View.OnClickListener listener){
        mOnClickListener = listener;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(StatsItem) - dynamically add items to adapter
 *      void adItemAt(StatsItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<StatsItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(StatsItem) - dynamically add items to adapter
     */
    public void addItem(StatsItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(StatsItem,int) - add item to adapter at specific position
     */
    public void addItemAt(StatsItem item, int position){
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
     * void swapData(ArrayList<StatsItem>) - swap old data with new data
     */
    public void swapData(ArrayList<StatsItem> data){
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
/*
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
        Log.d("Choice", "StatsAdapter.onBindViewHolder: " + position);

        if(mData.size() > 0){
            // Extract info from cursor
            StatsItem item = mData.get(position);

            //bind viewHolder components
            holder.bindCardView(item, position);
            holder.bindTextView(item);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - extends RecyclerView.ViewHolder, a design pattern to increase performance. It
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
            mTxtTime = (TextView)recycleView.findViewById(R.id.cardStats_txtTime);
            mTxtDate = (TextView)recycleView.findViewById(R.id.cardStats_txtDate);

        }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(StatsItem,int,int) - bind data to cardView
 *      void bindTextView(StatsItem) - bind data to textView components
 */
/**************************************************************************************************/
    /*
     * void bindCardView(StatsItem,int,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
        private void bindCardView(StatsItem item, int position) {

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
         * void bindTextView(StatsItem) - bind data to textView components. Set text and context
         * description values.
         */
        private void bindTextView(StatsItem item){
            mTxtDate.setText(item.appointmentDate);
            mTxtDate.setContentDescription(item.appointmentDate);

            mTxtTime.setText(item.appointmentTime);
            mTxtTime.setContentDescription(item.appointmentTime);

        }

    }

/**************************************************************************************************/

}
