package me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseColumns;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;

/**************************************************************************************************/
/*
 * ExerciseRecyclerAdapter extends RecyclerView.Adapter. It displays a list of exercises defined by
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

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.MyViewHolder>{

/**************************************************************************************************/
/*
 * Class Variables
 *      Context mContext - activity context
 *      int mItemLayoutId - item/card layout resource id
 *
 *      ArrayList<ExerciseItem> mData - an array list of item data consumed by the adapter
 *      OnCreateContextMenuListener mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    //mContext - activity context
    private Context mContext;

    //mItemLayoutId - item layout resource id
    private int mItemLayoutId;

    //mCursor - cursor consumed by adapter
    private Cursor mCursor;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

    //mOnClickListener - onClick listener for item click event
    private static View.OnClickListener mOnItemClickListener;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseRecyclerAdapter - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseRecyclerAdapter - constructor
     */
    public ExerciseRecyclerAdapter(Context ctx, Cursor cursor, int itemLayoutId){
        //get context
        mContext = ctx;

        //set item layout resource id
        mItemLayoutId = itemLayoutId;

        mCursor = cursor;

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
        if(mCursor != null){
            return mCursor.getCount();
        }

        return 0;
    }

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

        ExerciseItem item = new ExerciseItem(mCursor);

        //bind viewHolder components
        holder.bindItemView(item, position);
        holder.bindTextView(item);
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

    //mTxtName - textView component displaying exercise name
    private TextView mTxtName;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyViewHolder - constructor
 * @param recycleView - item layout containing the child views
 */
/**************************************************************************************************/

    public MyViewHolder(View recycleView){
        super(recycleView);
        //set CardView object
        mItemView = (RelativeLayout)recycleView.findViewById(R.id.choiceItem);

        //textView object used to display exercise
        mTxtName = (TextView)recycleView.findViewById(R.id.itemSimple_txtName);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Binding Methods
 *      void bindItemView(ExerciseItem,int,int) - bind data to itemView
 *      void bindTextView(ExerciseItem) - bind data to textView components
 */
/**************************************************************************************************/
    /*
     * void bindItemView(ExerciseItem,int,int) - bind data to itemView. Set tag values and contextMenu
     * listener, if not null
     */
    private void bindItemView(ExerciseItem item, int position) {

        mItemView.setTag(R.string.recycler_tagPosition, position);
        mItemView.setTag(R.string.recycler_tagItem, item);

        if(mCreateContextMenuListener != null){
            mItemView.setOnCreateContextMenuListener(mCreateContextMenuListener);
        }

    }

    /*
     * void bindTextView(ExerciseItem) - bind data to textView components. Set text and context
     * description values.
     */
    private void bindTextView(ExerciseItem item){
        mTxtName.setText(item.exerciseName);
        mTxtName.setContentDescription(item.exerciseName);
    }

}

/**************************************************************************************************/

}
