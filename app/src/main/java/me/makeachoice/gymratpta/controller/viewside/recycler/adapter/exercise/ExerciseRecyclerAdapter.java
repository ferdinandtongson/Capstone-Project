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
 * ExerciseRecyclerAdapter extends RecyclerView.Adapter. It displays a list of exercises defined by
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

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.MyViewHolder>{

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

    //mData - an array list of item data consumed by the adapter
    private ArrayList<ExerciseItem> mData;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseRecyclerAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseRecyclerAdapter - constructor
     */
    public ExerciseRecyclerAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //initialize data array
        mData = new ArrayList<>();
    }

    public ExerciseRecyclerAdapter(int itemLayoutId){
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
 *      void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
 */
/**************************************************************************************************/
    /*
     * void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
     */
    public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener){
        mCreateContextMenuListener = listener;
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
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, mData.size());
    }

    /*
     * void swapData(ArrayList<ExerciseItem>) - remove all data from adapter and replace with new data
     */
    public void swapData(ArrayList<ExerciseItem> data){
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /*
     * void clearData() - remove all data from adapter
     */
    public void clearData(){
        mData.clear();
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
            ExerciseItem item = mData.get(position);

            //bind viewHolder components
            holder.bindItemView(item, position);
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
 *      void bindItemView(ClientCardItem,int,int) - bind data to cardView
 *      void bindTextView(ClientCardItem) - bind data to textView components
 *      void bindProfileImage(ClientCardItem) - bind data to circleImageView
 *      void bindIconImages(ClientCardItem) - bind data to imageView icons
 */
/**************************************************************************************************/
    /*
     * void bindItemView(ExerciseItem,int,int) - bind data to itemView. Set tag values and contextMenu
     * listener, if not null
     */
    private void bindItemView(ExerciseItem item, int position) {

        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);

        if(mCreateContextMenuListener != null){
            mItemView.setOnCreateContextMenuListener(mCreateContextMenuListener);
        }

    }

    /*
     * void bindTextView(ClientCardItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(ExerciseItem item){
        mTxtName.setText(item.exerciseName);
        mTxtName.setContentDescription(item.exerciseName);
    }

}

/**************************************************************************************************/

}
