package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.RoutineSessionItem;

/**************************************************************************************************/
/*
 * RoutineRecyclerAdapter displays user-defined exercise routines
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

public class RoutineRecyclerAdapter extends
        RecyclerView.Adapter<RoutineRecyclerAdapter.MyViewHolder>
        implements RecyclerView.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

/**************************************************************************************************/
/**
 * Class Variables
 *      ArrayList<RoutineItem> mData - an array list of item data consumed by the adapter
 *      mItemLayoutId - item layout resource id
 *      Bridge mBridge - class implementing Bridge interface, typically a Maid class
 *
 * Interface:
 *      Bridge
 */
/**************************************************************************************************/

    //mData - an array list of item data consumed by the adapter
    private ArrayList<RoutineSessionItem> mData;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    private Context mContext;

    //mBridge - class implementing Bridge, typically a Maid class
    private Bridge mBridge;

    //Implemented communication line to a class
    public interface Bridge{
        void contextMenuCreated(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
        void contextMenuItemSelected(MenuItem item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * RoutineRecyclerAdapter - constructor
 */
    public RoutineRecyclerAdapter(Context ctx, int itemLayoutId){
        mContext = ctx;

        //set bridge communication
        //mBridge = bridge;

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
 *      RoutineSessionItem getItem(int) - get item at given position
 *      void addItem(RoutineSessionItem) - dynamically add items to adapter
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<RoutineSessionItem>) - remove all data from adapter and replace with new data
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
     * RoutineSessionItem getItem(int) - get item at given position
     * @param - position;
     */
    public RoutineSessionItem getItem(int position){
        return mData.get(position);
    }

    public ArrayList<RoutineSessionItem> getData(){ return mData; }


    /*
     * void addItem(RoutineSessionItem) - dynamically add items to adapter
     * @param item - exercise routine item data object
     */
    public void addItem(RoutineSessionItem item) {
        //add item object to data array
        mData.add(item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    public void addItemAt(RoutineSessionItem item, int position){
        //add item object to data array at position
        mData.add(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    public void modifyItemAt(String routineName, String description, int position){
        //RoutineSessionItem dataItem = mData.get(position);
        //dataItem.routineName = routineName;

        //notify adapter of data change
        //this.notifyDataSetChanged();
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
    public void swapData(ArrayList<RoutineSessionItem> data){
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
            RoutineSessionItem item = mData.get(position);

            holder.mTxtRoutineName.setText(item.routineName);

            setExerciseValues(holder.mTxtExercise01, holder.mTxtSet01, item.exercise01, item.set01);
            setExerciseValues(holder.mTxtExercise02, holder.mTxtSet02, item.exercise02, item.set02);
            setExerciseValues(holder.mTxtExercise03, holder.mTxtSet03, item.exercise03, item.set03);
            setExerciseValues(holder.mTxtExercise04, holder.mTxtSet04, item.exercise04, item.set04);
            setExerciseValues(holder.mTxtExercise05, holder.mTxtSet05, item.exercise05, item.set05);
            setExerciseValues(holder.mTxtExercise06, holder.mTxtSet06, item.exercise06, item.set06);
            setExerciseValues(holder.mTxtExercise07, holder.mTxtSet07, item.exercise07, item.set07);
            setExerciseValues(holder.mTxtExercise08, holder.mTxtSet08, item.exercise08, item.set08);
            setExerciseValues(holder.mTxtExercise09, holder.mTxtSet09, item.exercise09, item.set09);
            setExerciseValues(holder.mTxtExercise10, holder.mTxtSet10, item.exercise10, item.set10);

            holder.mCrdView.setTag(position);
            holder.mCrdView.setOnCreateContextMenuListener(this);
        }

    }

    private void setExerciseValues(TextView txtExercise, TextView txtSet, String exercise, int set){
        if(exercise == null || exercise.equals("")){
            txtExercise.setVisibility(View.GONE);
            txtSet.setVisibility(View.GONE);
        }
        else{
            txtExercise.setVisibility(View.VISIBLE);
            txtSet.setVisibility(View.VISIBLE);

            txtExercise.setText(exercise);
            txtSet.setText(String.valueOf(set));
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

        if(mBridge != null){
            //notify bridge that context menu has been created
            mBridge.contextMenuCreated(menu, v, menuInfo);
        }
    }

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onMenuItemClick(MenuItem item){
        if(mBridge != null){
            //notify bridge that a context menu item has been clicked
            mBridge.contextMenuItemSelected(item);
        }
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
        //mCrdReview - cardView that hold child views found below
        protected CardView mCrdView;

        protected TextView mTxtRoutineName;

        protected TextView mTxtExercise01;
        protected TextView mTxtExercise02;
        protected TextView mTxtExercise03;
        protected TextView mTxtExercise04;
        protected TextView mTxtExercise05;
        protected TextView mTxtExercise06;
        protected TextView mTxtExercise07;
        protected TextView mTxtExercise08;
        protected TextView mTxtExercise09;
        protected TextView mTxtExercise10;

        protected TextView mTxtSet01;
        protected TextView mTxtSet02;
        protected TextView mTxtSet03;
        protected TextView mTxtSet04;
        protected TextView mTxtSet05;
        protected TextView mTxtSet06;
        protected TextView mTxtSet07;
        protected TextView mTxtSet08;
        protected TextView mTxtSet09;
        protected TextView mTxtSet10;


/**************************************************************************************************/

/**************************************************************************************************/
/**
 * MyViewHolder - constructor
 * @param recycleView - item layout containing the child views
 */
        public MyViewHolder(View recycleView){
            super(recycleView);
            //set CardView object
            mCrdView = (CardView)recycleView.findViewById(R.id.choiceCardView);

            mTxtRoutineName = (TextView)recycleView.findViewById(R.id.txtRoutineName);

            mTxtExercise01 = (TextView)recycleView.findViewById(R.id.txtExercise01);
            mTxtExercise02 = (TextView)recycleView.findViewById(R.id.txtExercise02);
            mTxtExercise03 = (TextView)recycleView.findViewById(R.id.txtExercise03);
            mTxtExercise04 = (TextView)recycleView.findViewById(R.id.txtExercise04);
            mTxtExercise05 = (TextView)recycleView.findViewById(R.id.txtExercise05);
            mTxtExercise06 = (TextView)recycleView.findViewById(R.id.txtExercise06);
            mTxtExercise07 = (TextView)recycleView.findViewById(R.id.txtExercise07);
            mTxtExercise08 = (TextView)recycleView.findViewById(R.id.txtExercise08);
            mTxtExercise09 = (TextView)recycleView.findViewById(R.id.txtExercise09);
            mTxtExercise10 = (TextView)recycleView.findViewById(R.id.txtExercise10);

            mTxtSet01 = (TextView)recycleView.findViewById(R.id.txtSet01);
            mTxtSet02 = (TextView)recycleView.findViewById(R.id.txtSet02);
            mTxtSet03 = (TextView)recycleView.findViewById(R.id.txtSet03);
            mTxtSet04 = (TextView)recycleView.findViewById(R.id.txtSet04);
            mTxtSet05 = (TextView)recycleView.findViewById(R.id.txtSet05);
            mTxtSet06 = (TextView)recycleView.findViewById(R.id.txtSet06);
            mTxtSet07 = (TextView)recycleView.findViewById(R.id.txtSet07);
            mTxtSet08 = (TextView)recycleView.findViewById(R.id.txtSet08);
            mTxtSet09 = (TextView)recycleView.findViewById(R.id.txtSet09);
            mTxtSet10 = (TextView)recycleView.findViewById(R.id.txtSet10);

        }
    }

/**************************************************************************************************/

}
