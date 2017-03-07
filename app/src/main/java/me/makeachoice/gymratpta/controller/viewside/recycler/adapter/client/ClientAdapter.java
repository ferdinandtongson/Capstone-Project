package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;

import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_RETIRED;

/**************************************************************************************************/
/*
 * ClientAdapter is used by Client Screen and displays a list user clients
 */
/**************************************************************************************************/

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *      mCardDefaultColor - default background card color
 *      mCardRetiredColor - background card color used when client is retired
 *      HashMap<String,ClientItem> mContactIdMap - hashMap used to map contact names
 *
 *      Cursor mCursor - cursor consumed by adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    private ArrayList<ClientItem> mData;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mCardDefaultColor - default background card color
    private int mCardDefaultColor;

    //mCardRetiredColor - background card color used when client is retired
    private int mCardRetiredColor;

    //mOnClickListener - list item onClick event listener
    private static View.OnClickListener mOnClickListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ClientAdapter - constructor
     */
    public ClientAdapter(Context ctx, int itemLayoutId) {
        //set context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //get colors used as card background color
        mCardDefaultColor = DeprecatedUtility.getColor(mContext, R.color.card_activeBackground);
        mCardRetiredColor = DeprecatedUtility.getColor(mContext, R.color.card_retiredBackground);

        mData = new ArrayList<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      int getItemCount() - get number of items in adapter
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
     * ClientItem getItem(int) - get item at given position
     */
    public ClientItem getItem(int position){
        return mData.get(position);
    }

    /*
     * ArrayList<ClientItem> getData() - get array data used by recycler
     */
    public ArrayList<ClientItem> getData(){ return mData; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Listener Methods:
 *      void setOnImageClickListener(View.OnClickListener) - set listener for image profile onClick event
 *      void setOnLongClickListener(...) - set listener for list item onLongClick event
 *      void setOnClickListener(...) - set listener for list item onClick event
 */
/**************************************************************************************************/
    /*
     * void setOnClickListener(...) - set listener for list item onClick event
     */
    public void setOnClickListener(View.OnClickListener listener){
        mOnClickListener = listener;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      void addItem(ClientItem) - dynamically add items to adapter
 *      void adItemAt(ClientItem,int) - add item to adapter at specific position
 *      void removeItemAt(int) - remove item from adapter then refresh adapter
 *      void swapData(ArrayList<ClientItem>) - swap old data with new data
 *      void clearData() - remove all data from adapter
 */
/**************************************************************************************************/
    /*
     * void addItem(ClientItem) - dynamically add items to adapter
     */
    public void addItem(ClientItem item) {
        //add item object to data array
        mData.add(item);
        //notify adapter of data change
        this.notifyDataSetChanged();
    }

    /*
     * void adItemAt(ClientItem,int) - add item to adapter at specific position
     */
    public void addItemAt(ClientItem item, int position){
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
     * void swapData(ArrayList<ClientItem>) - swap old data with new data
     */
    public void swapData(ArrayList<ClientItem> data){
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
    public ClientAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //inflate the itemView
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(mItemLayoutId, viewGroup, false);

        //return ViewHolder
        return new ClientAdapter.MyViewHolder(itemView);
    }

    /*
     * void onBindViewHolder(ViewHolder, int) - where we bind our data to the views
     */
    @Override
    public void onBindViewHolder(ClientAdapter.MyViewHolder holder, int position) {
        // Extract info from cursor
        if(mData.size() > 0){
            ClientItem item = mData.get(position);

            //set default card color background
            int cardColor = mCardDefaultColor;

            //check client status, if false
            if(item.status.equals(CLIENT_RETIRED)){
                //change card color background
                cardColor = mCardRetiredColor;
            }

            //bind to viewHolder
            holder.bindItemView(item, position, cardColor);
            holder.bindTextView(item);
            holder.bindProfileImage(item);
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
 */
/**************************************************************************************************/
    /*
     * MyViewHolder - constructor
     */
    public MyViewHolder(View recycleView){
        super(recycleView);
        mItemView = (RelativeLayout)itemView.findViewById(R.id.choiceItem);
        mTxtName = (TextView)itemView.findViewById(R.id.itemClient_txtName);
        mTxtStatus = (TextView)itemView.findViewById(R.id.itemClient_txtStatus);
        mImgProfile = (CircleImageView)itemView.findViewById(R.id.itemClient_imgProfile);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(ClientItem,int) - bind data to cardView
 *      void bindTextView(ClientItem) - bind data to textView component
 *      void bindProfileImage(ClientItem) - bind data to circleImageView
 */
/**************************************************************************************************/
    /*
     * void bindItemView(ClientItem,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    public void bindItemView(ClientItem item, int position, int bgColor){
        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);
        mItemView.setBackgroundColor(bgColor);

        if(mOnClickListener != null){
            mItemView.setOnClickListener(mOnClickListener);
        }

    }

    /*
     * void bindTextView(ClientItem) - bind data to textView component; display contact name
     */
    public void bindTextView(ClientItem item) {
        mTxtName.setText(item.clientName);
        mTxtName.setContentDescription(item.clientName);
        mTxtStatus.setText(item.status);
        mTxtStatus.setContentDescription(item.status);
    }

    /*
     * void bindProfileImage(ClientItem) - bind data to circleImageView. Using Picasso, load the
     * image profile of client
     */
    private void bindProfileImage(ClientItem item){
        Picasso.with(itemView.getContext())
                .load(item.profilePic)
                .placeholder(R.drawable.gym_rat_black_48dp)
                .error(R.drawable.gym_rat_black_48dp)
                .into(mImgProfile);
    }

}

/**************************************************************************************************/

}
