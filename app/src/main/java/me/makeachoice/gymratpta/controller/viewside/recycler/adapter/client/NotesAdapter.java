package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.NotesItem;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;

/**************************************************************************************************/
/*
 * NotesAdapter extends RecyclerView.Adapter. It displays a list of note items with each card
 * displaying appointment date, time and first line of SOAP notes
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

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<NotesItem> mData - an array list of item data consumed by the adapter
 *      mOnLongClickListener - list item onLongClick event listener
 *      mOnClickListener - list item onClick event click listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mCurrentColor - background current card color
    private static int mCurrentColor;

    //mOldColor - background card color used for old notes
    private static int mOldColor;

    private static int mActiveIndex;
    private static String mStrModified;

    //mData - an array list of item data consumed by the adapter
    private ArrayList<NotesItem> mData;

    //mOnLongClickListener - list item onLongClick event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mOnClickListener - list item onClick event click listener
    private static View.OnClickListener mOnClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * NotesAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * NotesAdapter - constructor
     */
    public NotesAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //get colors used as card background color
        mCurrentColor = DeprecatedUtility.getColor(mContext, R.color.card_activeBackground);
        mOldColor = DeprecatedUtility.getColor(mContext, R.color.lightgray);

        mStrModified = mContext.getString(R.string.modified);

        //initialize data array
        mData = new ArrayList<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods:
 *      int getItemCount() - get number of items in adapter
 *      NotesItem getItem(int) - get item at given position
 *      ArrayList<NotesItem> getData() - get array data used by recycler
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
     * NotesItem getItem(int) - get item at given position
     */
    public NotesItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<NotesItem> getData() - get array data used by recycler
     */
    public ArrayList<NotesItem> getData(){ return mData; }

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

    public void setActiveIndex(int index){
        mActiveIndex = index;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(NotesItem) - dynamically add items to adapter
 *      void adItemAt(NotesItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<NotesItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(NotesItem) - dynamically add items to adapter
     */
    public void addItem(NotesItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(NotesItem,int) - add item to adapter at specific position
     */
    public void addItemAt(NotesItem item, int position){
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
     * void swapData(ArrayList<NotesItem>) - swap old data with new data
     */
    public void swapData(ArrayList<NotesItem> data){
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

        if(mData.size() > 0){
            // Extract info from cursor
            NotesItem item = mData.get(position);

            //bind viewHolder components
            holder.bindCardView(item, position);
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

    //mCardView - view that hold child views found below
    private CardView mCardView;

    private TextView mTxtTime;
    private TextView mTxtDate;
    private TextView mTxtSubjective;
    private TextView mTxtObjective;
    private TextView mTxtAssessment;
    private TextView mTxtPlan;
    private TextView mTxtModified;

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
        mTxtTime = (TextView)recycleView.findViewById(R.id.cardNotes_txtTime);
        mTxtDate = (TextView)recycleView.findViewById(R.id.cardNotes_txtDate);

        mTxtSubjective = (TextView)recycleView.findViewById(R.id.cardNotes_txtSubjective);
        mTxtObjective = (TextView)recycleView.findViewById(R.id.cardNotes_txtObjective);
        mTxtAssessment = (TextView)recycleView.findViewById(R.id.cardNotes_txtAssessment);
        mTxtPlan = (TextView)recycleView.findViewById(R.id.cardNotes_txtPlan);

        mTxtModified = (TextView)recycleView.findViewById(R.id.cardNotes_txtModified);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(NotesItem,int,int) - bind data to cardView
 *      void bindTextView(NotesItem) - bind data to textView components
 */
/**************************************************************************************************/
    /*
     * void bindCardView(NotesItem,int,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    private void bindCardView(NotesItem item, int position) {

        mCardView.setTag(R.string.recycler_tagPosition, position);
        mCardView.setTag(R.string.recycler_tagItem, item);

        if(position == mActiveIndex){
            mCardView.setCardBackgroundColor(mCurrentColor);
            mTxtModified.setVisibility(View.GONE);
        }
        else{
            if(mOnLongClickListener != null){
                mCardView.setOnLongClickListener(mOnLongClickListener);
            }
            mCardView.setCardBackgroundColor(mOldColor);

            String modified = mStrModified + ": " + item.modifiedDate;
            mTxtModified.setText(modified);
            mTxtModified.setContentDescription(modified);
            mTxtModified.setVisibility(View.VISIBLE);
        }

        if(mOnClickListener != null){
            mCardView.setOnClickListener(mOnClickListener);
        }

    }

    /*
     * void bindTextView(NotesItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(NotesItem item){
        mTxtDate.setText(item.appointmentDate);
        mTxtDate.setContentDescription(item.appointmentDate);

        mTxtTime.setText(item.appointmentTime);
        mTxtTime.setContentDescription(item.appointmentTime);

        mTxtSubjective.setText("S - " + item.subjectiveNotes);
        mTxtSubjective.setContentDescription(item.subjectiveNotes);

        mTxtObjective.setText("O - " + item.objectiveNotes);
        mTxtObjective.setContentDescription(item.objectiveNotes);

        mTxtAssessment.setText("A - " + item.assessmentNotes);
        mTxtAssessment.setContentDescription(item.assessmentNotes);

        mTxtPlan.setText("P - " + item.planNotes);
        mTxtPlan.setContentDescription(item.planNotes);

    }

}

/**************************************************************************************************/


}
