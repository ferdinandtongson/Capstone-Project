package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.utilities.DeprecatedUtility;

/**************************************************************************************************/
/*
 * ClientItemAdapter is used by Client Screen and displays a list user clients
 */
/**************************************************************************************************/

public class ClientItemAdapter extends RecyclerView.Adapter<ClientItemAdapter.MyViewHolder> {

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

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mCardDefaultColor - default background card color
    private int mCardDefaultColor;

    //mCardRetiredColor - background card color used when client is retired
    private int mCardRetiredColor;

    //mContactIdMap - hashMap used to map contact names
    private HashMap<String,ClientItem> mClientMap;

    //mCursor - cursor consumed by adapter
    private Cursor mCursor;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

    //mOnClickListener - onClick listener for item click event
    private static View.OnClickListener mOnItemClickListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientItemAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ClientItemAdapter - constructor
     */
    public ClientItemAdapter(Context ctx, Cursor cursor, int itemLayoutId) {
        //set context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        //get colors used as card background color
        mCardDefaultColor = DeprecatedUtility.getColor(mContext, R.color.card_activeBackground);
        mCardRetiredColor = DeprecatedUtility.getColor(mContext, R.color.card_retiredBackground);

        //initialize hashMap
        mClientMap = new HashMap();

        //set cursor
        mCursor = cursor;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      int getItemCount() - get number of items in adapter
 *      HashMap<String,ClientItem> getContactIdMap - get contact names map
 *      void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
 *      void setOnItemClickListener(...) - set listener to listen for item click events
 *      void closeCurosr() - close cursor
 */
/**************************************************************************************************/
    /*
     * int getItemCount() - get number of items in adapter
     */
    @Override
    public int getItemCount(){
        if(mCursor != null){
            return mCursor.getCount();
        }

        return 0;
    }

    /*
     * HashMap<String,ClientItem> getContactIdMap - get contact names map
     */
    public HashMap<String,ClientItem> getClientMap(){ return mClientMap; }

    /*
     * void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
     */
    public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener){
        mCreateContextMenuListener = listener;
    }

    /*
     * void setOnItemClickListener(...) - set listener to listen for item click events
     */
    public void setOnItemClickListener(View.OnClickListener listener){
        mOnItemClickListener = listener;
    }

    public void setCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    /*
     * void closeCurosr() - close cursor
     */
    public void closeCursor(){
        if(mCursor != null && !mCursor.isClosed()){
            mCursor.close();
            mCursor = null;
        }
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
    public ClientItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //inflate the itemView
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(mItemLayoutId, viewGroup, false);

        //return ViewHolder
        return new ClientItemAdapter.MyViewHolder(itemView);
    }

    /*
     * void onBindViewHolder(ViewHolder, int) - where we bind our data to the views
     */
    @Override
    public void onBindViewHolder(ClientItemAdapter.MyViewHolder holder, int position) {
        // Extract info from cursor
        mCursor.moveToPosition(position);
        ClientItem item = new ClientItem();
        item.addData(mCursor);

        mClientMap.put(item.clientName, item);

        //set default card color background
        int bgColor = mCardDefaultColor;

        String strRetired = mContext.getString(R.string.retired);
        //check client status, if false
        if(item.status.equals(strRetired)){
            //change card color background
            bgColor = mCardRetiredColor;
        }


        //bind to viewHolder
        holder.bindItemView(item, position, bgColor);
        holder.bindTextView(item);
        holder.bindProfileImage(item);
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

        if(mCreateContextMenuListener != null){
            mItemView.setOnCreateContextMenuListener(mCreateContextMenuListener);
        }

        if(mOnItemClickListener != null){
            mItemView.setOnClickListener(mOnItemClickListener);
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
