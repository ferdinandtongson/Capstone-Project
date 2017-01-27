package me.makeachoice.gymratpta.controller.viewside.recycler;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.makeachoice.gymratpta.R;

/**************************************************************************************************/
/*
 * Basic Recycler creates a basic recyclerView component for general use.
 */
/**************************************************************************************************/

public class BasicRecycler {

/**************************************************************************************************/
/*
 * Class Variables:
 *      RecyclerView mRecycler - recycler view component
 */
/**************************************************************************************************/

    private final int DEFAULT_RECYCLER_ID = R.id.choiceRecycler;

    //mRecycler - recycler view component
    RecyclerView mRecycler;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * BasicRecycler - constructor
 */
/**************************************************************************************************/
    /*
     * BasicRecycler - constructor
     */
    public BasicRecycler(Activity activity){
        //get RecyclerView from ViewHolder
        mRecycler = (RecyclerView)activity.findViewById(DEFAULT_RECYCLER_ID);
        initializeRecycler(activity);
    }

    public BasicRecycler(Activity activity, int recyclerId){
        //get RecyclerView from ViewHolder
        mRecycler = (RecyclerView)activity.findViewById(recyclerId);
        initializeRecycler(activity);
    }

    /*
     * BasicRecycler - constructor
     */
    public BasicRecycler(View layout){
        //get RecyclerView from ViewHolder
        mRecycler = (RecyclerView)layout.findViewById(DEFAULT_RECYCLER_ID);
        initializeRecycler(layout.getContext());
    }

    public BasicRecycler(View layout, int recyclerId){
        //get RecyclerView from ViewHolder
        mRecycler = (RecyclerView)layout.findViewById(recyclerId);
        initializeRecycler(layout.getContext());
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods
 *      void setAdapter(RecyclerView.Adapter) - sets the recycler view adapter
 */
/**************************************************************************************************/
    /*
     * void initializeRecycler(....) - initializes recyclerView for general use
     */
    private void initializeRecycler(Context ctx){
        //create LayoutManager for RecyclerView, in this case a linear list type LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);

        //set layout manager of RecyclerView
        mRecycler.setLayoutManager(layoutManager);

        //create item decoration, line divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ctx,
                layoutManager.getOrientation());

        //ad line divider to recycler
        mRecycler.addItemDecoration(dividerItemDecoration);

        //setHasFixedSize to true if recyclerView size isn't affected by adapter content
        mRecycler.setHasFixedSize(true);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter & Setter Methods
 *      void setListener(RecyclerView.OnClickListener) - set onClick listener
 */
/**************************************************************************************************/
    /*
     * RecyclerView getRecycler() - get recyclerView component
     */
    public RecyclerView getRecycler(){
        return mRecycler;
    }

    /*
     * void setListener(RecyclerView.OnClickListener) - set onClick listener
     */
    public void setListener(RecyclerView.OnClickListener listener){
        mRecycler.setOnClickListener(listener);
    }

    public void setVisibility(int visibility){
        mRecycler.setVisibility(visibility);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecycler.setAdapter(adapter);
    }

/**************************************************************************************************/

}
