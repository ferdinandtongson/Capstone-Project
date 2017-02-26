package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import me.makeachoice.gymratpta.controller.modelside.loader.ClientLoader;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  ClientButler assists in loading, saving and deleting client data to database and firebase
 */
/**************************************************************************************************/

public class ClientButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mClientKey - client key
    private String mClientKey;

    //mAppointmentItem - appointment item
    private ScheduleItem mAppointmentItem;

    //mLoaderId - loader id number
    private int mLoaderId;

    //mClientLoader - loader class used to load client data
    private ClientLoader mClientLoader;

    //mLoadListener - used to listen for onScheduleLoaded events
    private OnClientLoadedListener mLoadListener;
    public interface OnClientLoadedListener{
        public void onClientLoaded(ClientItem clientItem, AppointmentCardItem cardItem);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public ClientButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        //mScheduleList = new ArrayList<>();
        mClientLoader = new ClientLoader(mActivity, mUserId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Client
 */
/**************************************************************************************************/
    /*
     * void loadClient() - load client data from database
     */
    public void loadClient(int loaderId, ScheduleItem item, OnClientLoadedListener listener){
        mLoaderId = loaderId;
        mAppointmentItem = item;
        mClientKey = item.clientKey;
        mLoadListener = listener;

        //load client data using client firebase key
        mClientLoader.loadClientsByFKey(mClientKey, mLoaderId, new ClientLoader.OnClientLoadListener() {
            @Override
            public void onClientLoadFinished(Cursor cursor) {
                onClientLoaded(cursor);
            }
        });
    }

    /*
     * void onClientLoaded(Cursor) - client data from database has been loaded
     */
    private void onClientLoaded(Cursor cursor){
        AppointmentCardItem cardItem = null;
        ClientItem clientItem = null;

        //check that cursor is Not null or empty
        if(cursor != null && cursor.getCount() > 0){
            //move cursor to first index position
            cursor.moveToFirst();

            //create client item from cursor data
            clientItem = new ClientItem(cursor);

            cardItem = createAppointmentCardItem(mAppointmentItem, clientItem);
        }
        //destroy loader
        mClientLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onClientLoaded(clientItem, cardItem);
        }

    }

    /*
     * createAppointmentCardItem(...) - create appointmentCard item consumed by adapter
     */
    private AppointmentCardItem createAppointmentCardItem(ScheduleItem appItem, ClientItem clientItem){
        //create appointmentCard item used by adapter
        AppointmentCardItem item = new AppointmentCardItem();
        item.clientName = clientItem.clientName;
        item.clientInfo = appItem.appointmentTime;
        item.profilePic = Uri.parse(clientItem.profilePic);
        item.routineName = appItem.routineName;
        item.isActive = true;

        return item;
    }

/**************************************************************************************************/

}
