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
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;

/**************************************************************************************************/
/*
 * RoutineDetailRecyclerAdapter displays a list of exercise details defined in a particular routine
 */
/**************************************************************************************************/

public class RoutineDetailRecyclerAdapter extends RecyclerView.Adapter<RoutineDetailRecyclerAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      mItemLayoutId - item layout resource id
 *
 *      ArrayList<RoutineDetailItem> mData - an array list of item data consumed by the adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    //mStrDescriptionSet - content description "number of sets:"
    //private static String mStrDescriptionSet;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<RoutineItem> mData;

    //mCreateContextMenuListener - "create context menu" event listener
    ///private static View.OnCreateContextMenuListener mCreateContextMenuListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineDetailRecyclerAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineDetailRecyclerAdapter - constructor
     */
    public RoutineDetailRecyclerAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //set content description
        //mStrDescriptionSet = mContext.getString(R.string.description_card_txtSets);

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
     * RoutineItem getItem(int) - get item at given position
     */
    public RoutineItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<RoutineItem> getData() - get array data used by recycler
     */
    public ArrayList<RoutineItem> getData(){ return mData; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(RoutineItem) - dynamically add items to adapter
 *      void adItemAt(RoutineItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<RoutineItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(RoutineItem) - dynamically add items to adapter
     */
    public void addItem(RoutineItem item) {
        //add item object to data array
        mData.add(item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(RoutineItem,int) - add item to adapter at specific position
     */
    public void addItemAt(RoutineItem item, int position){
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
     * void swapData(ArrayList<RoutineItem>) - remove all data from adapter and replace with new data
     */
    public void swapData(ArrayList<RoutineItem> data){
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

            if(mData.size() > 0){
                // Extract info from cursor
                RoutineItem item = mData.get(position);

                //bind viewHolder components
                holder.bindCardView(item, position);
                holder.bindTextView(item);
            }

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
    public MyViewHolder(View recycleView){
        super(recycleView);
        //set CardView object
        mItemView = (RelativeLayout)recycleView.findViewById(R.id.choiceItem);

        mTxtName = (TextView)recycleView.findViewById(R.id.itemExerciseDetail_txtName);
        mTxtExerciseInfo = (TextView)recycleView.findViewById(R.id.itemExerciseDetail_txtInfo);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(RoutineItem,int,int) - bind data to cardView
 *      void bindTextView(RoutineItem) - bind data to textView components
 *      void setExerciseValues(...) - set exercise name and sets for routine
 */
/**************************************************************************************************/
/*
 * void bindCardView(RoutineItem,int,int) - bind data to itemView. Set tag values and contextMenu
 * listener, if not null
 */
    private void bindCardView(RoutineItem item, int position) {

        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);

    }

    /*
     * void bindTextView(RoutineItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(RoutineItem item){
        String strInfo = item.category + " / " + item.numOfSets + " sets";
        mTxtName.setText(item.exercise);
        mTxtExerciseInfo.setText(strInfo);
    }


}

/**************************************************************************************************/


}
