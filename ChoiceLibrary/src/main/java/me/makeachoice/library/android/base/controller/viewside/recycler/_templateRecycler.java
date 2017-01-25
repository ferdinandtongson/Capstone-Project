package me.makeachoice.library.android.base.controller.viewside.recycler;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**************************************************************************************************/
/*
 * TODO - description
 * TODO - change layout used by adapter
 * TODO - create recyclerAdapter
 * TODO - check if Bridge communication line is needed, currently no interface methods
 * TODO - need to see how to send data items or cursor to adapter
 * TODO - need to template recycler
 */
/**************************************************************************************************/


/**************************************************************************************************/
/*
 * TODO - description
 */

/**************************************************************************************************/

public class _templateRecycler {

/**************************************************************************************************/
/*
 * Class Variables:
 *      RecyclerView mRecycler - recycler view component
 *      Bridge mBridge - communication line interface
 */
/**************************************************************************************************/
    //TODO - change layout used by adapter
    //ADAPTER_LAYOUT_ID - layout resource file id used by recyclerView adapter
    //int ADAPTER_LAYOUT_ID = R.layout.card_appointment;

    //mRecycler - recycler view component
    RecyclerView mRecycler;

    //mBridge - communication line interface
    Bridge mBridge;

    //Implemented communication line
    public interface Bridge{
        //some kind of method
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * AppointmentDayRecycler - constructor
 */
/**************************************************************************************************/
    /*
     * AppointmentDayRecycler - constructor
     * @param activity - current activity
     * @param recyclerId - recycler resource id
     */
    public _templateRecycler(Activity activity, int recyclerId){
        //get RecyclerView from ViewHolder
        mRecycler = (RecyclerView)activity.findViewById(recyclerId);
    }

    /*
     * AppointmentDayRecycler - constructor
     * @param layout - layout holding recyclerView component; ie fragment layout
     * @param recyclerId - recycler resource id
     */
    public _templateRecycler(View layout, int recyclerId){
        //get RecyclerView from ViewHolder
        mRecycler = (RecyclerView)layout.findViewById(recyclerId);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Methods
 *      void setBridge(Bridge) - set communication line
 *      void setAdapter(RecyclerView.Adapter) - sets the recycler view adapter
 */
/**************************************************************************************************/

    public void initializeRecycler(RecyclerView.LayoutManager manager, boolean isFixedSize){
        //set layout manager of RecyclerView
        mRecycler.setLayoutManager(manager);

        //setHasFixedSize to true because 1)is true and 2)for optimization
        mRecycler.setHasFixedSize(isFixedSize);

        initializeAdapter();
    }

    private void initializeAdapter(){
        //TODO - create recyclerAdapter
        //AppointmentCardRecyclerAdapter adapter = new AppointmentCardRecyclerAdapter(ADAPTER_LAYOUT_ID);

        //mRecycler.setAdapter(adapter);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Methods
 *      void setBridge(Bridge) - set communication line
 */
/**************************************************************************************************/
    /*
     * void setBridge(Bridge) - set communication line
     */
    public void setBridge(Bridge bridge){
        mBridge = bridge;
    }

/**************************************************************************************************/

}
