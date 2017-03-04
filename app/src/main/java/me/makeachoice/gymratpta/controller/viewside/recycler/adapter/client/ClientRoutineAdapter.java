package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.ClientRoutineItem;

/**************************************************************************************************/
/*
 * ClientRoutineAdapter extends RecyclerView.Adapter.
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

public class ClientRoutineAdapter extends RecyclerView.Adapter<ClientRoutineAdapter.MyViewHolder>  {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<ClientRoutineItem> mData - an array list of item data consumed by the adapter
 *      mOnLongClickListener - list item onLongClick event listener
 *      mOnClickListener - list item onClick event click listener
 */
/**************************************************************************************************/

    //MARKER - string value used to mark keep track of item locations
    private static String MARKER = "routine";

    //mStrSets - string value for "Sets"
    private static String mStrSets;

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<ClientRoutineItem> mData;

    private ArrayList<Drawable> mBgStatusList;

    //mOnLongClickListener - list item onLongClick event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mOnClickListener - list item onClick event click listener
    private static View.OnClickListener mOnClickListener;

    //mMarkerList - an array list used to mark the location of an item when using drag and drop move
    private ArrayList<String> mMarkerList;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientRoutineAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ClientRoutineAdapter - constructor
     */
    public ClientRoutineAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //initialize data array
        mData = new ArrayList<>();

        //initialize marker list
        mMarkerList = new ArrayList<>();

        //string value "sets"
        mStrSets = mContext.getString(R.string.sets).toLowerCase();

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods:
 *      int getItemCount() - get number of items in adapter
 *      ClientRoutineItem getItem(int) - get item at given position
 *      ArrayList<ClientRoutineItem> getData() - get array data used by recycler
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
     * ClientRoutineItem getItem(int) - get item at given position
     */
    public ClientRoutineItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<ClientRoutineItem> getData() - get array data used by recycler
     */
    public ArrayList<ClientRoutineItem> getData(){ return mData; }

    /*
     * int getPosition(String) - get item position using it's markerId
     */
    public int getPosition(String markerId){
        //get size of marker list
        int count = mMarkerList.size();

        //loop through list
        for(int i = 0; i < count; i++){

            //check if markerId matches item in list
            if(markerId.equals(mMarkerList.get(i))){
                //if match, return index
                return i;
            }
        }
        return -1;
    }

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

    public void setStatusList(ArrayList<Drawable> bgList){
        mBgStatusList = bgList;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods:
 *      void onItemDismiss(int) - item was removed with side-swipe event
 *      void onItemMove(int,int) - item was moved with drag-and-drop event
 */
/**************************************************************************************************/
    /*
     * void onItemDismiss(int) - item was removed with side-swipe event
     */
    public void onItemDismiss(int position) {
        //remove item from list
        mData.remove(position);

        //remove marker from list
        mMarkerList.remove(position);

        //notify adapter
        notifyItemRemoved(position);
    }

    /*
     * void onItemMove(int,int) - item was moved with drag-and-drop event
     */
    public void onItemMove(int fromPosition, int toPosition) {
        //remove item, that was dragged, from list
        ClientRoutineItem moveItem = mData.remove(fromPosition);

        //add item to new location in list
        mData.add(toPosition, moveItem);

        //remove marker, attached to item that was dragged, from list
        String positionId = mMarkerList.remove(fromPosition);

        //add marker to new location in list
        mMarkerList.add(toPosition, positionId);

        //notify adapter
        notifyItemMoved(fromPosition, toPosition);
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(ClientRoutineItem) - dynamically add items to adapter
 *      void adItemAt(ClientRoutineItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<ClientRoutineItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(ClientRoutineItem) - dynamically add items to adapter
     */
    public void addItem(ClientRoutineItem item) {
        //clear marker id list
        mMarkerList.clear();

        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(ClientRoutineItem,int) - add item to adapter at specific position
     */
    public void addItemAt(ClientRoutineItem item, int position){
        //clear marker id list
        mMarkerList.clear();

        //add item object to data array at position
        mData.add(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void removeItemAt(int) - remove item from adapter then refresh adapter
     */
    public void removeItemAt(int position){
        //clear marker id list
        mMarkerList.clear();

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
     * void swapData(ArrayList<ClientRoutineItem>) - swap old data with new data
     */
    public void swapData(ArrayList<ClientRoutineItem> data){
        //clear marker id list
        mMarkerList.clear();

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
        //clear marker id list
        mMarkerList.clear();

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

        if(mData.size() > 0){
            // Extract info from cursor
            ClientRoutineItem item = mData.get(position);

            //update item order number
            item.orderNumber = String.valueOf(position + 1);

            //add marker to list
            mMarkerList.add(MARKER + position);

            //bind viewHolder components
            holder.bindCardView(item, mBgStatusList.get(position), position);
            holder.bindTextView(item);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ViewHolder - extends RecyclerView.ViewHolder, a design pattern to increase performance. It
 * holds the references to the UI components for each item in a ListView or GridView
 */
/**************************************************************************************************/

    public static class MyViewHolder extends RecyclerView.ViewHolder{

/**************************************************************************************************/
/*
 * Child Views used by the ReviewRecycler
 */
/**************************************************************************************************/

    //mItemView - cardView that hold child views found below
    protected RelativeLayout mItemView;


    protected TextView mTxtName;
    protected TextView mTxtExerciseInfo;

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
            //set view that holds the children
            mItemView = (RelativeLayout)recycleView.findViewById(R.id.choiceItem);

            mTxtName = (TextView)recycleView.findViewById(R.id.itemExerciseDetail_txtName);
            mTxtExerciseInfo = (TextView)recycleView.findViewById(R.id.itemExerciseDetail_txtInfo);
        }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(ClientRoutineItem,int,int) - bind data to cardView
 *      void bindTextView(ClientRoutineItem) - bind data to textView components
 */
/**************************************************************************************************/
    /*
     * void bindCardView(ClientRoutineItem,int,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
        private void bindCardView(ClientRoutineItem item, Drawable bg, int position) {

            //save item position (is NOT updated when a drag-and-drop event occurs)
            mItemView.setTag(R.string.recycler_tagPosition, position);

            //save item
            mItemView.setTag(R.string.recycler_tagItem, item);

            //save marker, used to determine position
            mItemView.setTag(R.string.recycler_tagId, MARKER + position);

            mItemView.setBackground(bg);

            if(mOnClickListener != null){
                //notify listener of onClick event
                mItemView.setOnClickListener(mOnClickListener);
            }

            if(mOnLongClickListener != null){
                mItemView.setOnLongClickListener(mOnLongClickListener);
            }
        }

        /*
         * void bindTextView(ClientRoutineItem) - bind data to textView components. Set text and context
         * description values.
         */
        private void bindTextView(ClientRoutineItem item){
            String strInfo = item.category + " / " + item.numOfSets + " " + mStrSets;
            mTxtName.setText(item.exercise);
            mTxtName.setContentDescription(item.exercise);

            mTxtExerciseInfo.setText(strInfo);
            mTxtExerciseInfo.setContentDescription(strInfo);

        }

    }

/**************************************************************************************************/


}
