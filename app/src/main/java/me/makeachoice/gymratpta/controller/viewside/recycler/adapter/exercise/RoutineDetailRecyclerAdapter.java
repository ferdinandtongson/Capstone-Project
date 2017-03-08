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
 *      String MARKER - string value used to mark keep track of item locations
 *      String mStrSets - string value for "Sets"
 *
 *      Context mContext - activity context
 *      mItemLayoutId - item layout resource id
 *
 *      ArrayList<RoutineItem> mData - an array list of item data consumed by the adapter
 *      ArrayList<String> mMarkerList - an array list used to mark the location of an item when using
 *          drag and drop move
 *      OnClickListener mOnClickListener - onClick listener for item click event
 */
/**************************************************************************************************/

    //MARKER - string value used to mark keep track of item locations
    private static String MARKER = "routine";

    //mStrSets - string value for "Sets"
    private static String mStrSets;

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<RoutineItem> mData;

    //mMarkerList - an array list used to mark the location of an item when using drag and drop move
    private ArrayList<String> mMarkerList;

    //mOnClickListener - onClick listener for item click event
    private static View.OnClickListener mOnItemClickListener;

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
 * Getter & Setter Methods:
 *      int getItemCount() - get number of items in adapter
 *      RoutineItem getItem(int) - get item at given position
 *      ArrayList<RoutineItem> getData() - get array data used by recycler
 *      int getPosition(String) - get item position using it's markerId
 *
 *      void setOnItemClickListener(...) - set listener to listen for item click events
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

    /*
     * void setOnItemClickListener(...) - set listener to listen for item click events
     */
    public void setOnItemClickListener(View.OnClickListener listener){
        //set listener
        mOnItemClickListener = listener;
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
        RoutineItem moveItem = mData.remove(fromPosition);

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
 *      void addItem(RoutineItem) - dynamically add items to adapter
 *      void addItemAt(RoutineItem,int) - add item to adapter at specific position
 *      void replaceItemAt(...) - replace item at given position
 *      void swapData(ArrayList<RoutineItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(RoutineItem) - dynamically add items to adapter
     */
    public void addItem(RoutineItem item) {
        //clear marker id list
        mMarkerList.clear();

        //add item object to data array
        mData.add(item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void addItemAt(RoutineItem,int) - add item to adapter at specific position
     */
    public void addItemAt(RoutineItem item, int position){
        //clear marker id list
        mMarkerList.clear();

        //add item object to data array at position
        mData.add(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void replaceItemAt(...) - replace item at given position
     */
    public void replaceItemAt(RoutineItem item, int position){
        //clear marker id list
        mMarkerList.clear();

        //set new routineItem at given index position
        mData.set(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void swapData(ArrayList<RoutineItem>) - remove all data from adapter and replace with new data
     */
    public void swapData(ArrayList<RoutineItem> data){
        mData.clear();
        mData.addAll(data);
        mMarkerList.clear();
        notifyDataSetChanged();
    }

    /*
     * void clearData() - remove all data from adapter
     */
    public void clearData(){
        mData.clear();
        mMarkerList.clear();
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
            //get item from data
            RoutineItem item = mData.get(position);

            //update item order number
            item.orderNumber = String.valueOf(position);

            //add marker to list
            mMarkerList.add(MARKER + position);

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
        //set view that holds the children
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

        //save item position (is NOT updated when a drag-and-drop event occurs)
        mItemView.setTag(R.string.recycler_tagPosition, position);

        //save item
        mItemView.setTag(R.string.recycler_tagItem, item);

        //save marker, used to determine position
        mItemView.setTag(R.string.recycler_tagId, MARKER + position);

        if(mOnItemClickListener != null){
            //notify listener of onClick event
            mItemView.setOnClickListener(mOnItemClickListener);
        }

    }

    /*
     * void bindTextView(RoutineItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(RoutineItem item){

        String strInfo = item.category + " / " + item.numOfSets + " " + mStrSets;
        mTxtName.setText(item.exercise);
        mTxtName.setContentDescription(item.exercise);

        mTxtExerciseInfo.setText(strInfo);
        mTxtExerciseInfo.setContentDescription(strInfo);
    }
}

/**************************************************************************************************/

}
