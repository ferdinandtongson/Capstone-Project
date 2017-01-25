package me.makeachoice.library.android.base.controller.viewside.recycler.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.library.android.base.R;

/**************************************************************************************************/
/*
 *  TODO - description
 *  TODO - create layout
 *  TODO - select stub data type (String or StubItem)
 *  TODO - create stub data
 *  TODO - create actual data for adapter to consume
 *  TODO - set layout to inflate
 *  TODO - define view components
 *  TODO - set layout component id
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * TODO - description
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

public class _templateRecyclerAdapter extends
        RecyclerView.Adapter<_templateRecyclerAdapter.MyViewHolder> {

/**************************************************************************************************/
/**
 * Class Variables
 *      ArrayList<String> mTitles - array strings
 *      Bridge mBridge - class implementing Bridge interface, typically a Maid class
 *
 * Interface:
 *      Bridge
 */
/**************************************************************************************************/

    //TODO - set temporary stub data type
    //mStubData - an array list of stub item data consumed by the adapter
    private ArrayList<StubItem> mStubData;

    //mBridge - class implementing Bridge, typically a Maid class
    private Bridge mBridge;

    //mLayout - layout used by recycler adapter
    private int mLayoutId;

    //Implemented communication line to a class
    public interface Bridge{
        //get Context of current Activity
        //Context getActivityContext();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * _templateRecyclerAdapter - constructor
 */
    public _templateRecyclerAdapter(int layoutId){
        //set bridge communication
        //mBridge = bridge;
        mLayoutId = layoutId;

        //TODO - create stub data
        mStubData = new ArrayList<StubItem>();

        /*StubItem item01 = new StubItem();
        item01.exerciseName = "Bench Press";
        item01.numbOfSets = 3;

        StubItem item02 = new StubItem();
        item02.exerciseName = "Bench Press";
        item02.numbOfSets = 3;

        StubItem item03 = new StubItem();
        item03.exerciseName = "Bench Press";
        item03.numbOfSets = 3;

        StubItem item04 = new StubItem();
        item04.exerciseName = "Bench Press";
        item04.numbOfSets = 3;

        StubItem item05 = new StubItem();
        item05.exerciseName = "Bench Press";
        item05.numbOfSets = 3;

        StubItem item06 = new StubItem();
        item06.exerciseName = "Bench Press";
        item06.numbOfSets = 3;

        StubItem item07 = new StubItem();
        item07.exerciseName = "Bench Press";
        item07.numbOfSets = 3;

        mStubData.add(item01);
        mStubData.add(item02);
        mStubData.add(item03);
        mStubData.add(item04);
        mStubData.add(item05);
        mStubData.add(item06);
        mStubData.add(item07);*/

    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * Getters:
 *      int getItemCount()
 *
 * Setters:
 *      void setBridge(Bridge) - set Bridge communication line
 *      void setData(ArrayList<String>)
 */
/**************************************************************************************************/
/**
 * int getItemCount() - get number of items in adapter
 * @return int - number of items in adapter
 */
    @Override
    public int getItemCount(){
        //TODO - set appropriate stub data
        if(mStubData != null){
            //return number of review items
            return mStubData.size();
        }
        return 0;
    }

    /*
     * void setBridge(Bridge) - set Bridge communication line
     */
    public void setBridge(Bridge bridge){
        mBridge = bridge;
    }

/*
 * void setData(ArrayList<String>) - get data to be display by the RecyclerView
 * @param data - list of data
 */
    public void setData(ArrayList<StubItem> data){
        mStubData = data;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * Implemented Methods from RecyclerView.Adapter:
 *      ViewHolder onCreateViewHolder(ViewGroup, int)
 *      void onBindViewHolder(ViewHolder, int)
 */
/**************************************************************************************************/
/**
 * ViewHolder onCreateViewHolder(ViewGroup, int) - inflates the layout and creates an instance
 * of the ViewHolder (PosterHolder) class. This instance is used to access the views in the
 * inflated layout and is only called when a new view must be created.
 * @param viewGroup - parent view that will hold the itemView, RecyclerView
 * @param i - position of the itemView
 * @return - ViewHolder class; ReviewHolder
 */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //TODO - set layout to inflate
        //inflate the itemView
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(mLayoutId, viewGroup, false);


        //return ViewHolder
        return new MyViewHolder(itemView);
    }

/**
 * void onBindViewHolder(ViewHolder, int) - where we bind our data to the views
 * @param holder - ViewHolder class; ReviewHolder
 * @param position - position of the itemView being bound
 */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //TODO - bind data to views
        //set title
        //holder.mTxtExerciseName.setText(mStubData.get(position).exerciseName);
        //holder.mTxtNumOfSets.setText(mStubData.get(position).numbOfSets);

    }

/**************************************************************************************************/





/**************************************************************************************************/
/**
 * ReviewHolder - extends RecyclerView.ViewHolder, a design pattern to increase performance. It
 * holds the references to the UI components for each item in a ListView or GridView
 */
/**************************************************************************************************/


    public static class MyViewHolder extends RecyclerView.ViewHolder{

/**************************************************************************************************/
/**
 * Child Views of the used by the ReviewRecycler
 */
/**************************************************************************************************/
        //TODO - define view components
        //mCrdReview - cardView that hold child views found below
        protected CardView mCrdView;

        protected TextView mTxtExerciseName;
        protected TextView mTxtNumOfSets;

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * MyViewHolder - constructor
 * @param recycleView - item layout containing the child views
 */
        public MyViewHolder(View recycleView){
            super(recycleView);
            //TODO - set layout component id
            //set CardView object
            mCrdView = (CardView)recycleView.findViewById(R.id.choiceCardView);

            //set TextView object used to display title
            //mTxtExerciseName = (TextView)recycleView.findViewById(R.id.card_txtExerciseName);
            //mTxtNumOfSets = (TextView)recycleView.findViewById(R.id.card_txtNumOfSets);

        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StubItem - used for debugging purposes
 */
/**************************************************************************************************/

    public class StubItem {

        public String exerciseName;

        public int numbOfSets;

        public String exerciseLastDate;
        public String[] reps;
        public String[] weight;
    }

/**************************************************************************************************/

}
