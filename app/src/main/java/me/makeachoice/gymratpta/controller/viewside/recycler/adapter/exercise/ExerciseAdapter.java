package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;

/**************************************************************************************************/
/*
 * ExerciseAdapter extends RecyclerView.Adapter. It displays a list of exercises defined by
 * the user.
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

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder>{

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<ExerciseItem> mData - an array list of item data consumed by the adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    private ArrayList<ExerciseItem> mData;


    //mOnLongClickListener - list item onLongClick event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mOnClickListener - list item onClick event click listener
    private static View.OnClickListener mOnClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseAdapter - constructor
     */
    public ExerciseAdapter(Context ctx, int itemLayoutId){
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
 *      ExerciseItem getItem(int) - get item at given position
 *      ArrayList<ExerciseItem> getData() - get array data used by recycler
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
     * ExerciseItem getItem(int) - get item at given position
     */
    public ExerciseItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<ExerciseItem> getData() - get array data used by recycler
     */
    public ArrayList<ExerciseItem> getData(){ return mData; }


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
 *      void addItem(ExerciseItem) - dynamically add items to adapter
 *      void adItemAt(ExerciseItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<ExerciseItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(ExerciseItem) - dynamically add items to adapter
     */
    public void addItem(ExerciseItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(ExerciseItem,int) - add item to adapter at specific position
     */
    public void addItemAt(ExerciseItem item, int position){
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
     * void swapData(ArrayList<ExerciseItem>) - swap old data with new data
     */
    public void swapData(ArrayList<ExerciseItem> data){
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

        ExerciseItem item = mData.get(position);

        //bind viewHolder components
        holder.bindItemView(item, position);
        holder.bindTextView(item);
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
 * Child Views of the used by the ReviewRecycler
 */
/**************************************************************************************************/

    //mItemView - view that hold child views found below
    private RelativeLayout mItemView;

    //mTxtName - textView component displaying exercise name
    private TextView mTxtName;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - constructor
 * @param recycleView - item layout containing the child views
 */
/**************************************************************************************************/

    public MyViewHolder(View recycleView){
        super(recycleView);
        //set CardView object
        mItemView = (RelativeLayout)recycleView.findViewById(R.id.choiceItem);

        //textView object used to display exercise
        mTxtName = (TextView)recycleView.findViewById(R.id.itemSimple_txtName);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindItemView(ExerciseItem,int,int) - bind data to itemView
 *      void bindTextView(ExerciseItem) - bind data to textView components
 */
/**************************************************************************************************/
    /*
     * void bindItemView(ExerciseItem,int,int) - bind data to itemView. Set tag values and contextMenu
     * listener, if not null
     */
    private void bindItemView(ExerciseItem item, int position) {

        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);

    }

    /*
     * void bindTextView(ExerciseItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(ExerciseItem item){
        mTxtName.setText(item.exerciseName);
        mTxtName.setContentDescription(item.exerciseName);
    }

}

/**************************************************************************************************/

}
