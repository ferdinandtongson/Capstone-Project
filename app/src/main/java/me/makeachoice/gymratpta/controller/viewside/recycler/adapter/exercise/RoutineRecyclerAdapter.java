package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class RoutineRecyclerAdapter extends RecyclerView.Adapter<RoutineRecyclerAdapter.MyViewHolder>{

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      mItemLayoutId - item layout resource id
 *
 *      ArrayList<RoutineSessionItem> mData - an array list of item data consumed by the adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    //mStrDescriptionSet - content description "number of sets:"
    private static String mStrDescriptionSet;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<RoutineSessionItem> mData;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineRecyclerAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * RoutineRecyclerAdapter - constructor
     */
    public RoutineRecyclerAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //set content description
        mStrDescriptionSet = mContext.getString(R.string.description_card_txtSets);

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
     * RoutineSessionItem getItem(int) - get item at given position
     */
    public RoutineSessionItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<RoutineSessionItem> getData() - get array data used by recycler
     */
    public ArrayList<RoutineSessionItem> getData(){ return mData; }


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
 *      void addItem(RoutineSessionItem) - dynamically add items to adapter
 *      void adItemAt(RoutineSessionItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<RoutineSessionItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(RoutineSessionItem) - dynamically add items to adapter
     */
    public void addItem(RoutineSessionItem item) {
        //add item object to data array
        mData.add(item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(RoutineSessionItem,int) - add item to adapter at specific position
     */
    public void addItemAt(RoutineSessionItem item, int position){
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
     * void swapData(ArrayList<RoutineSessionItem>) - remove all data from adapter and replace with new data
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
                RoutineSessionItem item = mData.get(position);

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
/*
 * MyViewHolder - constructor
 */
/**************************************************************************************************/
    public MyViewHolder(View recycleView){
        super(recycleView);
        //set CardView object
        mCrdView = (CardView)recycleView.findViewById(R.id.choiceCardView);

        mTxtRoutineName = (TextView)recycleView.findViewById(R.id.cardRoutine_txtName);

        mTxtExercise01 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise01);
        mTxtExercise02 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise02);
        mTxtExercise03 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise03);
        mTxtExercise04 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise04);
        mTxtExercise05 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise05);
        mTxtExercise06 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise06);
        mTxtExercise07 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise07);
        mTxtExercise08 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise08);
        mTxtExercise09 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise09);
        mTxtExercise10 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtExercise10);

        mTxtSet01 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet01);
        mTxtSet02 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet02);
        mTxtSet03 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet03);
        mTxtSet04 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet04);
        mTxtSet05 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet05);
        mTxtSet06 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet06);
        mTxtSet07 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet07);
        mTxtSet08 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet08);
        mTxtSet09 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet09);
        mTxtSet10 = (TextView)recycleView.findViewById(R.id.cardRoutine_txtSet10);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(RoutineSessionItem,int,int) - bind data to cardView
 *      void bindTextView(RoutineSessionItem) - bind data to textView components
 *      void setExerciseValues(...) - set exercise name and sets for routine
 */
/**************************************************************************************************/
    /*
     * void bindCardView(RoutineSessionItem,int,int) - bind data to itemView. Set tag values and contextMenu
     * listener, if not null
     */
    private void bindCardView(RoutineSessionItem item, int position) {

        mCrdView.setTag(R.string.recycler_tagPosition, position);
        mCrdView.setTag(R.string.recycler_tagItem, item);

        if(mCreateContextMenuListener != null){
            mCrdView.setOnCreateContextMenuListener(mCreateContextMenuListener);
        }

    }

    /*
     * void bindTextView(RoutineSessionItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(RoutineSessionItem item){
        mTxtRoutineName.setText(item.routineName);
        mTxtRoutineName.setContentDescription(item.routineName);

        setExerciseValues(mTxtExercise01, mTxtSet01, item.exercise01, item.set01);
        setExerciseValues(mTxtExercise02, mTxtSet02, item.exercise02, item.set02);
        setExerciseValues(mTxtExercise03, mTxtSet03, item.exercise03, item.set03);
        setExerciseValues(mTxtExercise04, mTxtSet04, item.exercise04, item.set04);
        setExerciseValues(mTxtExercise05, mTxtSet05, item.exercise05, item.set05);
        setExerciseValues(mTxtExercise06, mTxtSet06, item.exercise06, item.set06);
        setExerciseValues(mTxtExercise07, mTxtSet07, item.exercise07, item.set07);
        setExerciseValues(mTxtExercise08, mTxtSet08, item.exercise08, item.set08);
        setExerciseValues(mTxtExercise09, mTxtSet09, item.exercise09, item.set09);
        setExerciseValues(mTxtExercise10, mTxtSet10, item.exercise10, item.set10);
    }

    /*
     * void setExerciseValues(...) - set exercise name and sets for routine
     */
    private void setExerciseValues(TextView txtExercise, TextView txtSet, String exercise, int set){
        if(exercise == null || exercise.equals("")){
            txtExercise.setVisibility(View.GONE);
            txtSet.setVisibility(View.GONE);
        }
        else{
            txtExercise.setVisibility(View.VISIBLE);
            txtSet.setVisibility(View.VISIBLE);

            txtExercise.setText(exercise);
            txtExercise.setContentDescription(exercise);

            txtSet.setText(String.valueOf(set));
            txtSet.setContentDescription(mStrDescriptionSet + " " + set);
        }
    }

}

/**************************************************************************************************/

}
