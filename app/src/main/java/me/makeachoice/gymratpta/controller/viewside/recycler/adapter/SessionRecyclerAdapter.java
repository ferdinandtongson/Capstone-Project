package me.makeachoice.gymratpta.controller.viewside.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.ContactsItem;

/**************************************************************************************************/
/*
 *  TODO - create actual data for adapter to consume
 *  TODO - set layout to inflate
 *  TODO - define view components
 *  TODO - set layout component id
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * AppointmentCardRecyclerAdapter displays appointment cards in list format
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

public class SessionRecyclerAdapter extends
        RecyclerView.Adapter<SessionRecyclerAdapter.MyViewHolder>
        implements RecyclerView.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

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

    //mData - an array list of item data consumed by the adapter
    private ArrayList<StubItem> mData;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    private Context mContext;

    //mBridge - class implementing Bridge, typically a Maid class
    private Bridge mBridge;

    //Implemented communication line to a class
    public interface Bridge{
        void itemClicked(View view);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * _templateRecyclerAdapter - constructor
 */
    public SessionRecyclerAdapter(Context ctx, int itemLayoutId){
        //get context
        mContext = ctx;

        //set bridge communication
        //mBridge = bridge;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

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
        if(mData != null){
            //return number of review items
            return mData.size();
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
    public void setStubData(ArrayList<StubItem> data){
        mData = data;
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
            // Extract info from cursor
            StubItem item = mData.get(position);
            holder.bind(position, item);
        }

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
        //mCrdReview - cardView that hold child views found below
        protected CardView mCrdView;
        protected RelativeLayout mItemView;

        protected TextView mTxtSessionDate;
        protected TextView mTxtSessionTime;

        protected CircleImageView mImgProfile;
        protected TextView mTxtName;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - constructor
 * @param recycleView - item layout containing the child views
 */
        public MyViewHolder(View recycleView){
            super(recycleView);
            //mCrdView = (CardView)recycleView.findViewById(R.id.choiceCardView);
            mItemView = (RelativeLayout)recycleView.findViewById(R.id.choiceItem);

            //set TextView object used to display title
            //mTxtSessionDate = (TextView)recycleView.findViewById(R.id.txtDate);
            mTxtSessionTime = (TextView)recycleView.findViewById(R.id.txtTime);

            mImgProfile = (CircleImageView)recycleView.findViewById(R.id.imgProfile);
            mTxtName = (TextView)recycleView.findViewById(R.id.txtContactName);

        }

        public void bind(int position, StubItem item) {

            mTxtSessionTime.setText(item.appointmentTime);

            mItemView.setTag(R.string.recycler_tagPosition, position);
            mItemView.setTag(R.string.recycler_tagItem, item);
            //mItemView.setOnCreateContextMenuListener(this);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mBridge.itemClicked(view);
                }
            });

            mTxtName.setText(item.clientName);
            /*Picasso.with(itemView.getContext())
                    .load("test")
                    .placeholder(R.drawable.gym_rat_48dp)
                    .error(R.drawable.gym_rat_48dp)
                    .into(holder.mImgProfile);*/
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
        Log.d("Choice", "ERRAdapter.onCreateContextMenu");
        //create string values for menu
        String strEdit = mContext.getString(R.string.edit);
        String strDelete = mContext.getString(R.string.delete);

        //add menu and set menu item click listener
        menu.add("Reschedule").setOnMenuItemClickListener(this);
        menu.add("Delete").setOnMenuItemClickListener(this);

        if(mBridge != null){
            //notify bridge that context menu has been created
            //mBridge.contextMenuCreated(menu, v, menuInfo);
        }
    }

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onMenuItemClick(MenuItem item){
        if(mBridge != null){
            //notify bridge that a context menu item has been clicked
            //mBridge.contextMenuItemSelected(item);
        }
        return true;
    }

/**************************************************************************************************/

    public static class StubItem {

        public String clientName;

        public String clientPhoneNumber;

        public String clientSMS;

        public String clientWorkout;

        public String appointmentTime;

    }

}
