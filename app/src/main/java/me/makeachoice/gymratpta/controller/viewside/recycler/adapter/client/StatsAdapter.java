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
import me.makeachoice.gymratpta.model.item.client.StatsItem;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;

/**************************************************************************************************/
/*
 * StatsAdapter extends RecyclerView.Adapter. It displays a list of stats items with each card
 * displaying appointment date, time and stats
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

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<StatsItem> mData - an array list of item data consumed by the adapter
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
    private ArrayList<StatsItem> mData;
    private static StatsItem mFirstItem;
    private static ArrayList<StatsItem> mPrevList;

    //mOnLongClickListener - list item onLongClick event listener
    private static View.OnLongClickListener mOnLongClickListener;

    //mOnClickListener - list item onClick event click listener
    private static View.OnClickListener mOnClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StatsAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * StatsAdapter - constructor
     */
    public StatsAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //get colors used as card background color
        mCurrentColor = DeprecatedUtility.getColor(mContext, R.color.card_activeBackground);
        mOldColor = DeprecatedUtility.getColor(mContext, R.color.card_retiredBackground);

        mStrModified = mContext.getString(R.string.modified);

        //initialize data array
        mData = new ArrayList<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods:
 *      int getItemCount() - get number of items in adapter
 *      StatsItem getItem(int) - get item at given position
 *      ArrayList<StatsItem> getData() - get array data used by recycler
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
     * StatsItem getItem(int) - get item at given position
     */
    public StatsItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<StatsItem> getData() - get array data used by recycler
     */
    public ArrayList<StatsItem> getData(){ return mData; }

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

    public void setFirstItem(StatsItem item){
        mFirstItem = item;
    }

    public void setPreviousItems(ArrayList<StatsItem> statsList){
        mPrevList = statsList;
    }

    public void setActiveIndex(int index){
        mActiveIndex = index;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(StatsItem) - dynamically add items to adapter
 *      void adItemAt(StatsItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<StatsItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(StatsItem) - dynamically add items to adapter
     */
    public void addItem(StatsItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(StatsItem,int) - add item to adapter at specific position
     */
    public void addItemAt(StatsItem item, int position){
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
     * void swapData(ArrayList<StatsItem>) - swap old data with new data
     */
    public void swapData(ArrayList<StatsItem> data){
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
            StatsItem item = mData.get(position);

            //bind viewHolder components
            holder.bindCardView(item, position);
            holder.bindTextView(item, position);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - extends RecyclerView.ViewHolder, a design pattern to increase performance. It
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

    private TextView mTxtWeight;
    private TextView mTxtPrevWeight;
    private TextView mTxtFirstWeight;

    private TextView mTxtBodyFat;
    private TextView mTxtPrevBodyFat;
    private TextView mTxtFirstBodyFat;

    private TextView mTxtBMI;
    private TextView mTxtPrevBMI;
    private TextView mTxtFirstBMI;

    private TextView mTxtNeck;
    private TextView mTxtPrevNeck;
    private TextView mTxtFirstNeck;

    private TextView mTxtChest;
    private TextView mTxtPrevChest;
    private TextView mTxtFirstChest;

    private TextView mTxtRBicep;
    private TextView mTxtPrevRBicep;
    private TextView mTxtFirstRBicep;

    private TextView mTxtLBicep;
    private TextView mTxtPrevLBicep;
    private TextView mTxtFirstLBicep;

    private TextView mTxtWaist;
    private TextView mTxtPrevWaist;
    private TextView mTxtFirstWaist;

    private TextView mTxtNavel;
    private TextView mTxtPrevNavel;
    private TextView mTxtFirstNavel;

    private TextView mTxtHips;
    private TextView mTxtPrevHips;
    private TextView mTxtFirstHips;

    private TextView mTxtRThigh;
    private TextView mTxtPrevRThigh;
    private TextView mTxtFirstRThigh;

    private TextView mTxtLThigh;
    private TextView mTxtPrevLThigh;
    private TextView mTxtFirstLThigh;

    private TextView mTxtRCalf;
    private TextView mTxtPrevRCalf;
    private TextView mTxtFirstRCalf;

    private TextView mTxtLCalf;
    private TextView mTxtPrevLCalf;
    private TextView mTxtFirstLCalf;

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
        mTxtTime = (TextView)recycleView.findViewById(R.id.cardStats_txtTime);
        mTxtDate = (TextView)recycleView.findViewById(R.id.cardStats_txtDate);

        mTxtWeight = (TextView)recycleView.findViewById(R.id.cardStats_txtWeightStat);
        mTxtPrevWeight = (TextView)recycleView.findViewById(R.id.cardStats_txtWeightPrevious);
        mTxtFirstWeight = (TextView)recycleView.findViewById(R.id.cardStats_txtWeightFirst);

        mTxtBodyFat = (TextView)recycleView.findViewById(R.id.cardStats_txtBodyFatStat);
        mTxtPrevBodyFat = (TextView)recycleView.findViewById(R.id.cardStats_txtBodyFatPrevious);
        mTxtFirstBodyFat = (TextView)recycleView.findViewById(R.id.cardStats_txtBodyFatFirst);

        mTxtBMI = (TextView)recycleView.findViewById(R.id.cardStats_txtBMIStat);
        mTxtPrevBMI = (TextView)recycleView.findViewById(R.id.cardStats_txtBMIPrevious);
        mTxtFirstBMI = (TextView)recycleView.findViewById(R.id.cardStats_txtBMIFirst);

        mTxtNeck = (TextView)recycleView.findViewById(R.id.cardStats_txtNeckStat);
        mTxtPrevNeck = (TextView)recycleView.findViewById(R.id.cardStats_txtNeckPrevious);
        mTxtFirstNeck = (TextView)recycleView.findViewById(R.id.cardStats_txtNeckFirst);

        mTxtChest = (TextView)recycleView.findViewById(R.id.cardStats_txtChestStat);
        mTxtPrevChest = (TextView)recycleView.findViewById(R.id.cardStats_txtChestPrevious);
        mTxtFirstChest = (TextView)recycleView.findViewById(R.id.cardStats_txtChestFirst);

        mTxtRBicep = (TextView)recycleView.findViewById(R.id.cardStats_txtRBicepStat);
        mTxtPrevRBicep = (TextView)recycleView.findViewById(R.id.cardStats_txtRBicepPrevious);
        mTxtFirstRBicep = (TextView)recycleView.findViewById(R.id.cardStats_txtRBicepFirst);

        mTxtLBicep = (TextView)recycleView.findViewById(R.id.cardStats_txtLBicepStat);
        mTxtPrevLBicep = (TextView)recycleView.findViewById(R.id.cardStats_txtLBicepPrevious);
        mTxtFirstLBicep = (TextView)recycleView.findViewById(R.id.cardStats_txtLBicepFirst);

        mTxtWaist = (TextView)recycleView.findViewById(R.id.cardStats_txtWaistStat);
        mTxtPrevWaist = (TextView)recycleView.findViewById(R.id.cardStats_txtWaistPrevious);
        mTxtFirstWaist = (TextView)recycleView.findViewById(R.id.cardStats_txtWaistFirst);

        mTxtNavel = (TextView)recycleView.findViewById(R.id.cardStats_txtNavelStat);
        mTxtPrevNavel = (TextView)recycleView.findViewById(R.id.cardStats_txtNavelPrevious);
        mTxtFirstNavel = (TextView)recycleView.findViewById(R.id.cardStats_txtNavelFirst);

        mTxtHips = (TextView)recycleView.findViewById(R.id.cardStats_txtHipsStat);
        mTxtPrevHips = (TextView)recycleView.findViewById(R.id.cardStats_txtHipsPrevious);
        mTxtFirstHips = (TextView)recycleView.findViewById(R.id.cardStats_txtHipsFirst);

        mTxtRThigh = (TextView)recycleView.findViewById(R.id.cardStats_txtRThighStat);
        mTxtPrevRThigh = (TextView)recycleView.findViewById(R.id.cardStats_txtRThighPrevious);
        mTxtFirstRThigh = (TextView)recycleView.findViewById(R.id.cardStats_txtRThighFirst);

        mTxtLThigh = (TextView)recycleView.findViewById(R.id.cardStats_txtLThighStat);
        mTxtPrevLThigh = (TextView)recycleView.findViewById(R.id.cardStats_txtLThighPrevious);
        mTxtFirstLThigh = (TextView)recycleView.findViewById(R.id.cardStats_txtLThighFirst);

        mTxtRCalf = (TextView)recycleView.findViewById(R.id.cardStats_txtRCalfStat);
        mTxtPrevRCalf = (TextView)recycleView.findViewById(R.id.cardStats_txtRCalfPrevious);
        mTxtFirstRCalf = (TextView)recycleView.findViewById(R.id.cardStats_txtRCalfFirst);

        mTxtLCalf = (TextView)recycleView.findViewById(R.id.cardStats_txtLCalfStat);
        mTxtPrevLCalf = (TextView)recycleView.findViewById(R.id.cardStats_txtLCalfPrevious);
        mTxtFirstLCalf = (TextView)recycleView.findViewById(R.id.cardStats_txtLCalfFirst);

        mTxtModified = (TextView)recycleView.findViewById(R.id.cardStats_txtModified);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(StatsItem,int,int) - bind data to cardView
 *      void bindTextView(StatsItem) - bind data to textView components
 */
/**************************************************************************************************/
    /*
     * void bindCardView(StatsItem,int,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    private void bindCardView(StatsItem item, int position) {

        mCardView.setTag(R.string.recycler_tagPosition, position);
        mCardView.setTag(R.string.recycler_tagItem, item);

        if(position == mActiveIndex){
            mCardView.setCardBackgroundColor(mCurrentColor);
            mTxtModified.setVisibility(View.GONE);
        }
        else{
            mCardView.setCardBackgroundColor(mOldColor);
            if(mOnLongClickListener != null){
                mCardView.setOnLongClickListener(mOnLongClickListener);
            }
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
     * void bindTextView(StatsItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(StatsItem item, int position){
        int currentColor;
        if(position == mActiveIndex){
            currentColor = mCurrentColor;
        }
        else{
            currentColor = mOldColor;
        }
        int prevIndex = mPrevList.size() - 1 - position;

        StatsItem prevItem;
        StatsItem firstItem;
        prevItem = mPrevList.get(prevIndex);

        if(prevIndex != 0){
            firstItem = mFirstItem;
        }
        else{
            firstItem = prevItem;
        }

        mTxtDate.setText(item.appointmentDate);
        mTxtDate.setContentDescription(item.appointmentDate);

        mTxtTime.setText(item.appointmentTime);
        mTxtTime.setContentDescription(item.appointmentTime);

        mTxtWeight.setText(convertStat(item.statWeight));
        mTxtPrevWeight.setText(convertStat(prevItem.statWeight));
        mTxtFirstWeight.setText(convertStat(firstItem.statWeight));
        mTxtWeight.setBackgroundColor(currentColor);
        mTxtPrevWeight.setBackgroundColor(currentColor);
        mTxtFirstWeight.setBackgroundColor(currentColor);

        mTxtBodyFat.setText(convertStat(item.statBodyFat));
        mTxtPrevBodyFat.setText(convertStat(prevItem.statBodyFat));
        mTxtFirstBodyFat.setText(convertStat(firstItem.statBodyFat));
        mTxtBodyFat.setBackgroundColor(currentColor);
        mTxtPrevBodyFat.setBackgroundColor(currentColor);
        mTxtFirstBodyFat.setBackgroundColor(currentColor);

        mTxtBMI.setText(convertStat(item.statBMI));
        mTxtPrevBMI.setText(convertStat(prevItem.statBMI));
        mTxtFirstBMI.setText(convertStat(firstItem.statBMI));
        mTxtBMI.setBackgroundColor(currentColor);
        mTxtPrevBMI.setBackgroundColor(currentColor);
        mTxtFirstBMI.setBackgroundColor(currentColor);

        mTxtNeck.setText(convertStat(item.statNeck));
        mTxtPrevNeck.setText(convertStat(prevItem.statNeck));
        mTxtFirstNeck.setText(convertStat(firstItem.statNeck));
        mTxtNeck.setBackgroundColor(currentColor);
        mTxtPrevNeck.setBackgroundColor(currentColor);
        mTxtFirstNeck.setBackgroundColor(currentColor);

        mTxtChest.setText(convertStat(item.statChest));
        mTxtPrevChest.setText(convertStat(prevItem.statChest));
        mTxtFirstChest.setText(convertStat(firstItem.statChest));
        mTxtChest.setBackgroundColor(currentColor);
        mTxtPrevChest.setBackgroundColor(currentColor);
        mTxtFirstChest.setBackgroundColor(currentColor);

        mTxtRBicep.setText(convertStat(item.statRBicep));
        mTxtPrevRBicep.setText(convertStat(prevItem.statRBicep));
        mTxtFirstRBicep.setText(convertStat(firstItem.statRBicep));
        mTxtRBicep.setBackgroundColor(currentColor);
        mTxtPrevRBicep.setBackgroundColor(currentColor);
        mTxtFirstRBicep.setBackgroundColor(currentColor);

        mTxtLBicep.setText(convertStat(item.statLBicep));
        mTxtPrevLBicep.setText(convertStat(prevItem.statLBicep));
        mTxtFirstLBicep.setText(convertStat(firstItem.statLBicep));
        mTxtLBicep.setBackgroundColor(currentColor);
        mTxtPrevLBicep.setBackgroundColor(currentColor);
        mTxtFirstLBicep.setBackgroundColor(currentColor);

        mTxtWaist.setText(convertStat(item.statWaist));
        mTxtPrevWaist.setText(convertStat(prevItem.statWaist));
        mTxtFirstWaist.setText(convertStat(firstItem.statWaist));
        mTxtWaist.setBackgroundColor(currentColor);
        mTxtPrevWaist.setBackgroundColor(currentColor);
        mTxtFirstWaist.setBackgroundColor(currentColor);

        mTxtNavel.setText(convertStat(item.statNavel));
        mTxtPrevNavel.setText(convertStat(prevItem.statNavel));
        mTxtFirstNavel.setText(convertStat(firstItem.statNavel));
        mTxtNavel.setBackgroundColor(currentColor);
        mTxtPrevNavel.setBackgroundColor(currentColor);
        mTxtFirstNavel.setBackgroundColor(currentColor);

        mTxtHips.setText(convertStat(item.statHips));
        mTxtPrevHips.setText(convertStat(prevItem.statHips));
        mTxtFirstHips.setText(convertStat(firstItem.statHips));
        mTxtHips.setBackgroundColor(currentColor);
        mTxtPrevHips.setBackgroundColor(currentColor);
        mTxtFirstHips.setBackgroundColor(currentColor);

        mTxtRThigh.setText(convertStat(item.statRThigh));
        mTxtPrevRThigh.setText(convertStat(prevItem.statRThigh));
        mTxtFirstRThigh.setText(convertStat(firstItem.statRThigh));
        mTxtRThigh.setBackgroundColor(currentColor);
        mTxtPrevRThigh.setBackgroundColor(currentColor);
        mTxtFirstRThigh.setBackgroundColor(currentColor);

        mTxtLThigh.setText(convertStat(item.statLThigh));
        mTxtPrevLThigh.setText(convertStat(prevItem.statLThigh));
        mTxtFirstLThigh.setText(convertStat(firstItem.statLThigh));
        mTxtLThigh.setBackgroundColor(currentColor);
        mTxtPrevLThigh.setBackgroundColor(currentColor);
        mTxtFirstLThigh.setBackgroundColor(currentColor);

        mTxtRCalf.setText(convertStat(item.statRCalf));
        mTxtPrevRCalf.setText(convertStat(prevItem.statRCalf));
        mTxtFirstRCalf.setText(convertStat(firstItem.statRCalf));
        mTxtRCalf.setBackgroundColor(currentColor);
        mTxtPrevRCalf.setBackgroundColor(currentColor);
        mTxtFirstRCalf.setBackgroundColor(currentColor);

        mTxtLCalf.setText(convertStat(item.statLCalf));
        mTxtPrevLCalf.setText(convertStat(prevItem.statLCalf));
        mTxtFirstLCalf.setText(convertStat(firstItem.statLCalf));
        mTxtLCalf.setBackgroundColor(currentColor);
        mTxtPrevLCalf.setBackgroundColor(currentColor);
        mTxtFirstLCalf.setBackgroundColor(currentColor);

    }



    private String convertStat(double stat){
        if(stat == 0){
            return "-";
        }
        return String.valueOf(stat);
    }


}

/**************************************************************************************************/

}
