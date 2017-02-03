package me.makeachoice.gymratpta.controller.viewside.recycler.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.contract.contacts.ContactsColumns;
import me.makeachoice.gymratpta.model.item.ContactsItem;

/**************************************************************************************************/
/*
 * ContactsAdapter extends RecyclerView.Adapter. It is used to display contacts info taken from the
 * users phone. Displays photo and contact name
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

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
    implements RecyclerView.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

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

    //mCursor - cursor consumed by adapter
    private Cursor mCursor;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

    private boolean mIsValidClient;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ContactsAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ContactsAdapter - constructor
     */
    public ContactsAdapter(Context ctx, Cursor cursor, int itemLayoutId) {
        Log.d("Choice", "ContactsAdapter - constructor");
        //set context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

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
        // Extract info from cursor
        mCursor.moveToPosition(position);
        long contactId = mCursor.getLong(ContactsColumns.INDEX_ID);

        // Create contact model
        ContactsItem item = new ContactsItem();
        item.contactId = contactId;
        item.contactName = mCursor.getString(ContactsColumns.INDEX_PRIMARY_NAME);
        item.profilePic = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

        //bind to viewHolder
        holder.bindItemView(item, position);
        holder.bindTextView(item);
        holder.bindProfileImage(item);
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
        Log.d("Choice", "ContactsAdapter.onCreateContextMenu");
        /*Boss boss = (Boss) mContext.getApplicationContext();

        ClientFirebase firebase = ClientFirebase.getInstance(boss.getUser().getUid());

        ContactsItem item = (ContactsItem)v.getTag(R.string.tag_item);

        if(firebase.isClient(item.contactName)){
            String strAlready = item.contactName + " is already a client";
            menu.add(strAlready);
            mIsValidClient = false;
        }
        else{
            Log.d("Choice", "     valid new client");
            mIsValidClient = true;

            //create string values for menu
            String strAdd = mContext.getString(R.string.add) + ": " + item.contactName;

            //add menu and set menu item click listener
            menu.add(strAdd).setOnMenuItemClickListener(this);

            if(mBridge != null){
                //notify bridge that context menu has been created
                mBridge.contextMenuCreated(menu, v, menuInfo);
            }
        }*/

    }

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onMenuItemClick(MenuItem item){
        /*Log.d("Choice", "ContactsAdapter.onMenuItemClick");
        if(mBridge != null){
            if(mIsValidClient){
                //notify bridge that a context menu item has been clicked
                mBridge.contextMenuItemSelected(item);
            }
        }*/
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

    //mItemView - view that hold child views found below
    private LinearLayout mItemView;

    private CircleImageView mImgProfile;
    private TextView mTxtName;

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
        mItemView = (LinearLayout)itemView.findViewById(R.id.choiceItem);
        mTxtName = (TextView)itemView.findViewById(R.id.itemContact_txtName);
        mImgProfile = (CircleImageView)itemView.findViewById(R.id.itemContact_imgProfile);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindCardView(ContactsItem,int) - bind data to cardView
 *      void bindTextView(ContactsItem) - bind data to textView component
 *      void bindProfileImage(ContactsItem) - bind data to circleImageView
 */
/**************************************************************************************************/
    /*
     * void bindItemView(ContactsItem,int) - bind data to cardView. Set tag values, bg color,
     * and contextMenu listener, if not null
     */
    public void bindItemView(ContactsItem item, int position){
        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);

        if(mCreateContextMenuListener != null){
            mItemView.setOnCreateContextMenuListener(mCreateContextMenuListener);
        }

    }

    /*
     * void bindTextView(ContactsItem) - bind data to textView component; display contact name
     */
    public void bindTextView(ContactsItem item) {
        mTxtName.setText(item.contactName);
        mTxtName.setContentDescription(item.contactName);
    }

    /*
     * void bindProfileImage(ContactsItem) - bind data to circleImageView. Using Picasso, load the
     * image profile of client
     */
    private void bindProfileImage(ContactsItem item){
        Picasso.with(itemView.getContext())
                .load(item.profilePic)
                .placeholder(R.drawable.gym_rat_black_48dp)
                .error(R.drawable.gym_rat_black_48dp)
                .into(mImgProfile);
    }

}

/**************************************************************************************************/

}
