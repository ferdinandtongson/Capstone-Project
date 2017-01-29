package me.makeachoice.gymratpta.controller.viewside.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;

/**************************************************************************************************/
/*
 * ExerciseRecyclerAdapter extends RecyclerView.Adapter. It is used to display exercises defined by
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

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.MyViewHolder>
    implements RecyclerView.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

/**************************************************************************************************/
/**
 * Class Variables
 *      ArrayList<ExerciseItem> mData - an array list of item data consumed by the adapter
 *      mItemLayoutId - item layout resource id
 *      mColorWhite - color white defined in resource colors
 *      mColorPrimary - color primary defined in resource colors
 *      Bridge mBridge - class implementing Bridge interface, typically a Maid class
 *
 * Interface:
 *      Bridge
 */
/**************************************************************************************************/

    //mData - an array list of item data consumed by the adapter
    private ArrayList<ExerciseItem> mData;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    private Context mContext;
    //mBridge - class implementing Bridge, typically a Maid class
    //private Bridge mBridge;

    //Implemented communication line to a class
    public interface Bridge{
        void contextMenuCreated(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
        void contextMenuItemSelected(MenuItem item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
    /*
     * ExerciseRecyclerAdapter - constructor
     */
    public ExerciseRecyclerAdapter(Context ctx, int itemLayoutId){
        mContext = ctx;

        //set bridge communication
        //mBridge = bridge;

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
 * Public Methods:
 *      int getItemCount() - get number of items in adapter
 *      ExerciseItem getItem(int) - get item at given position
 *      void addItem(ExerciseItem) - dynamically add items to adapter
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<ExerciseItem>) - remove all data from adapter and replace with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * int getItemCount() - get number of items in adapter
     * @return int - number of items in adapter
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
     * @param - position;
     */
    public ExerciseItem getItem(int position){
        return mData.get(position);
    }

    public ArrayList<ExerciseItem> getData(){ return mData; }

    /*
     * void addItem(ExerciseItem) - dynamically add items to adapter
     * @param item - exercise item data object
     */
    public void addItem(ExerciseItem item) {
        //add item object to data array
        mData.add(item);
        Log.d("Choice", "ExerciseRecyclerAdapter: " + mData.size());
        Log.d("Choice", "     " + item.exerciseName);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    public void addItemAt(ExerciseItem item, int position){
        //add item object to data array at position
        mData.add(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    public void modifyItemAt(String exerciseName, int position){
        ExerciseItem dataItem = mData.get(position);
        dataItem.exerciseName = exerciseName;

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
     * @param viewGroup - parent view that will hold the itemView, RecyclerView
     * @param i - position of the itemView
     * @return - ViewHolder class; ReviewHolder
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
     * @param holder - ViewHolder class; ReviewHolder
     * @param position - position of the itemView being bound
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(mData.size() > 0){
            ExerciseItem exerciseItem = mData.get(position);

            holder.mTxtExerciseName.setText(exerciseItem.exerciseName);

            holder.mTxtExerciseName.setTag(R.string.recycler_tagPosition, position);
            holder.mTxtExerciseName.setTag(R.string.recycler_tagItem, exerciseItem);
            holder.mTxtExerciseName.setOnCreateContextMenuListener(this);
        }

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Context Menu Methods:
 *      void onCreateContextMenu(...) - create context menu
 *      boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
 */
/**************************************************************************************************/
    /*
     * void onCreateContextMenu(...) - create context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //create string values for menu
        String strEdit = mContext.getString(R.string.edit);
        String strDelete = mContext.getString(R.string.delete);

        //add menu and set menu item click listener
        menu.add(strEdit).setOnMenuItemClickListener(this);
        menu.add(strDelete).setOnMenuItemClickListener(this);

        /*if(mBridge != null){
            //notify bridge that context menu has been created
            mBridge.contextMenuCreated(menu, v, menuInfo);
        }*/
    }

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onMenuItemClick(MenuItem item){
        /*if(mBridge != null){
            //notify bridge that a context menu item has been clicked
            mBridge.contextMenuItemSelected(item);
        }*/
        return true;
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

        //mTxtExerciseName - textView component displaying exercise name
        private TextView mTxtExerciseName;

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
            mTxtExerciseName = (TextView)recycleView.findViewById(R.id.item_txtExerciseName);
        }
    }

/**************************************************************************************************/

}
