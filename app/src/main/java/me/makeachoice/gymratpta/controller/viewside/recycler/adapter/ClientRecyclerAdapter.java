package me.makeachoice.gymratpta.controller.viewside.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ClientRecyclerAdapter extends RecyclerView.Adapter<ClientRecyclerAdapter.MyViewHolder> {

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
    private ArrayList<ContactsItem> mData;

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
    /*
     * ExerciseRecyclerAdapter - constructor
     */
    public ClientRecyclerAdapter(Context ctx, int itemLayoutId){
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
 *      ContactsItem getItem(int) - get item at given position
 *      void addItem(ContactsItem) - dynamically add items to adapter
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<ContactsItem>) - remove all data from adapter and replace with new data
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
     * ContactsItem getItem(int) - get item at given position
     * @param - position;
     */
    public ContactsItem getItem(int position){
        return mData.get(position);
    }

    public ArrayList<ContactsItem> getData(){ return mData; }

    /*
     * void addItem(ContactsItem) - dynamically add items to adapter
     * @param item - exercise item data object
     */
    public void addItem(ContactsItem item) {
        //add item object to data array
        mData.add(item);
        Log.d("Choice", "ClientRecyclerAdapter: " + mData.size());
        Log.d("Choice", "     " + item.contactName);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    public void addItemAt(ContactsItem item, int position){
        //add item object to data array at position
        mData.add(position, item);

        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    public void modifyItemAt(String exerciseName, int position){
        //ContactsItem dataItem = mData.get(position);
        //dataItem.exerciseName = exerciseName;

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
     * void swapData(ArrayList<ContactsItem>) - remove all data from adapter and replace with new data
     */
    public void swapData(ArrayList<ContactsItem> data){
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
            // Extract info from cursor
            ContactsItem item = mData.get(position);
            holder.bind(position, item);
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

        private CircleImageView mImgProfile;
        private TextView mTxtName;
        private TextView mTxtStatus;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - constructor
 * @param recycleView - item layout containing the child views
 */
/**************************************************************************************************/

        public MyViewHolder(View recycleView){
            super(recycleView);
            mItemView = (RelativeLayout)recycleView.findViewById(R.id.choiceItem);
            mImgProfile = (CircleImageView)recycleView.findViewById(R.id.imgProfile);
            mTxtName = (TextView)recycleView.findViewById(R.id.txtContactName);
            mTxtStatus = (TextView)recycleView.findViewById(R.id.txtClientStatus);

        }

        public void bind(int position, ContactsItem item) {

            mItemView.setTag(R.string.recycler_tagPosition, position);
            mItemView.setTag(R.string.recycler_tagItem, item);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mBridge.itemClicked(view);
                }
            });

            mTxtName.setText(item.contactName);
            /*mTxtStatus.setText(contact.getClientStatus());
            Picasso.with(itemView.getContext())
                    .load(contact.profilePic)
                    .placeholder(R.drawable.gym_rat_48dp)
                    .error(R.drawable.gym_rat_48dp)
                    .into(mImgProfile);*/
        }
    }

/**************************************************************************************************/

}
