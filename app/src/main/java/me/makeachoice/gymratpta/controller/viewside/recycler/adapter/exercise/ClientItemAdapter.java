package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.NewClientItem;

/**
 * Created by Usuario on 2/4/2017.
 */

public class ClientItemAdapter extends RecyclerView.Adapter<ClientItemAdapter.MyViewHolder> {

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      Cursor mCursor - cursor consumed by adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item/card layout resource id
    private int mItemLayoutId;

    //mContactIdMap - hashMap used to map contact names
    private HashMap<String,NewClientItem> mClientMap;

    //mCursor - cursor consumed by adapter
    private Cursor mCursor;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

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
        Log.d("Choice", "ClientItemAdapter - constructor");
        //set context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        mClientMap = new HashMap();

        //set cursor
        mCursor = cursor;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      int getItemCount() - get number of items in adapter
 *      void closeCurosr() - close cursor
 */
/**************************************************************************************************/
    /*
     * int getItemCount() - get number of items in adapter
     */
    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    /*
     * HashMap<String,NewClientItem> getContactIdMap - get contact names map
     */
    public HashMap<String,NewClientItem> getClientMap(){ return mClientMap; }


    /*
     * void closeCurosr() - close cursor
     */
    public void closeCursor(){
        if(mCursor != null && !mCursor.isClosed()){
            mCursor.close();
            mCursor = null;
        }
    }

    /*
     * void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
     */
    public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener){
        mCreateContextMenuListener = listener;
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
        NewClientItem item = new NewClientItem();
        item = item.getItem(mCursor);

        mClientMap.put(item.clientName, item);

        //bind to viewHolder
        holder.bindItemView(item, position);
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
 *      void bindCardView(NewClientItem,int) - bind data to cardView
 *      void bindTextView(NewClientItem) - bind data to textView component
 *      void bindProfileImage(NewClientItem) - bind data to circleImageView
 */
/**************************************************************************************************/
    /*
     * void bindItemView(NewClientItem,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    public void bindItemView(NewClientItem item, int position){
        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);

        if(mCreateContextMenuListener != null){
            mItemView.setOnCreateContextMenuListener(mCreateContextMenuListener);
        }

    }

    /*
     * void bindTextView(NewClientItem) - bind data to textView component; display contact name
     */
    public void bindTextView(NewClientItem item) {
        mTxtName.setText(item.clientName);
        mTxtName.setContentDescription(item.clientName);
        mTxtStatus.setText(item.status);
        mTxtStatus.setContentDescription(item.status);
    }

    /*
     * void bindProfileImage(NewClientItem) - bind data to circleImageView. Using Picasso, load the
     * image profile of client
     */
    private void bindProfileImage(NewClientItem item){
        Picasso.with(itemView.getContext())
                .load(item.profilePic)
                .placeholder(R.drawable.gym_rat_black_48dp)
                .error(R.drawable.gym_rat_black_48dp)
                .into(mImgProfile);
    }

}

/**************************************************************************************************/

}