package me.makeachoice.gymratpta.controller.modelside.butler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.controller.modelside.firebase.ClientFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ClientLoader;
import me.makeachoice.gymratpta.controller.modelside.query.ClientQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.ClientContract;
import me.makeachoice.gymratpta.model.db.DBHelper;
import me.makeachoice.gymratpta.model.item.client.AppointmentCardItem;
import me.makeachoice.gymratpta.model.item.client.ClientFBItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_ACTIVE;
import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_RETIRED;

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

    private ClientItem mDeleteItem;

    //mAppointmentItem - appointment item
    private ScheduleItem mAppointmentItem;

    private HashMap<String, ClientItem> mClientMap;

    //mLoaderId - loader id number
    private int mLoaderId;

    //mClientLoader - loader class used to load client data
    private ClientLoader mClientLoader;

    //mLoadListener - used to listen for onScheduleLoaded events
    private OnClientLoadedListener mClientLoadListener;
    public interface OnClientLoadedListener{
        void onClientLoaded(ClientItem clientItem);
    }

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        void onLoaded(ArrayList<ClientItem> clientList);
    }

    //mSaveListener - used to listen for save events
    private OnSavedListener mSaveListener;
    public interface OnSavedListener{
        void onSaved();
    }

    //mDeleteListener - used to listen for delete events
    private OnDeletedListener mDeleteListener;
    public interface OnDeletedListener{
        void onDeleted();
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

        mClientLoader = new ClientLoader(mActivity, mUserId);
        mClientMap = new HashMap<>();
    }

    public HashMap<String, ClientItem> getClientMap(){
        return mClientMap;
    }

    public ClientItem getClientFromMap(String clientKey){
        if(mClientMap != null && mClientMap.containsKey(clientKey)){
            return mClientMap.get(clientKey);
        }
        else{
            return null;
        }
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
        String clientKey = item.clientKey;
        mLoaderId = loaderId;
        mClientLoadListener = listener;

        //load client data using client firebase key
        mClientLoader.loadClientsByFKey(clientKey, mLoaderId, new ClientLoader.OnClientLoadListener() {
            @Override
            public void onClientLoadFinished(Cursor cursor) {
                onClientLoaded(cursor);
            }
        });
    }

    public void loadAllClients(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //load client data
        mClientLoader.loadClients(mLoaderId, new ClientLoader.OnClientLoadListener() {
            @Override
            public void onClientLoadFinished(Cursor cursor) {
                onClientsLoaded(cursor);
            }
        });
    }

    public void loadActiveClients(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        String strActive = CLIENT_ACTIVE;

        //load client data
        mClientLoader.loadClientsByStatus(strActive, mLoaderId, new ClientLoader.OnClientLoadListener() {
            @Override
            public void onClientLoadFinished(Cursor cursor) {
                onClientsLoaded(cursor);
            }
        });
    }

    public void loadRetiredClients(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        String strRetired = CLIENT_RETIRED;

        //load client data
        mClientLoader.loadClientsByStatus(strRetired, mLoaderId, new ClientLoader.OnClientLoadListener() {
            @Override
            public void onClientLoadFinished(Cursor cursor) {
                onClientsLoaded(cursor);
            }
        });
    }

    private void onClientsLoaded(Cursor cursor){
        ArrayList<ClientItem> clientList = new ArrayList<>();
        mClientMap.clear();

        if(cursor != null && cursor.getCount() > 0){
            int count = cursor.getCount();

            for(int i = 0; i < count; i++){
                cursor.moveToPosition(i);
                ClientItem item = new ClientItem(cursor);

                clientList.add(item);
                mClientMap.put(item.fkey, item);

            }
        }

        mClientLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(clientList);
        }

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

            //cardItem = createAppointmentCardItem(mAppointmentItem, clientItem);
        }
        //destroy loader
        mClientLoader.destroyLoader(mLoaderId);

        if(mClientLoadListener != null){
            mClientLoadListener.onClientLoaded(clientItem);
        }

    }

    /*
     * createAppointmentCardItem(...) - create appointmentCard item consumed by adapter
     */
    public AppointmentCardItem createAppointmentCardItem(ScheduleItem appItem, ClientItem clientItem){
        //create appointmentCard item used by adapter
        AppointmentCardItem item = new AppointmentCardItem();
        item.clientName = clientItem.clientName;
        item.clientKey = clientItem.fkey;
        item.clientInfo = appItem.appointmentTime;
        item.datestamp = appItem.datestamp;
        item.profilePic = Uri.parse(clientItem.profilePic);
        item.routineName = appItem.routineName;
        item.isActive = true;

        return item;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Notes
 */
/**************************************************************************************************/

    public void saveClient(ClientItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

    }

    private ClientItem mSaveItem;

    /*
     * void saveToFirebase(ClientItem) - save to firebase
     */
    private void saveToFirebase(ClientItem saveItem){
        mSaveItem = saveItem;

        //get firebase helper instance
        ClientFirebaseHelper fbHelper = ClientFirebaseHelper.getInstance();

        //create firebase item
        ClientFBItem fbItem = new ClientFBItem();
        fbItem.clientName = saveItem.clientName;
        fbItem.email = saveItem.email;
        fbItem.phone = saveItem.phone;
        fbItem.firstSession = saveItem.firstSession;
        fbItem.goals = saveItem.goals;
        fbItem.status = saveItem.status;

        //save notes item to firebase
        fbHelper.addClient(mUserId, fbItem, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClientItem item = postSnapshot.getValue(ClientItem.class);

                    if(item.clientName.equals(mSaveItem.clientName)){
                        mSaveItem.fkey = postSnapshot.getKey();
                    }
                }
                //save to local database
                saveToDatabase(mSaveItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*
     * void saveToDatabase(...) - save to local database
     */
    private void saveToDatabase(ClientItem saveItem){
        //get uri value for routine name table
        Uri uriValue = ClientContract.CONTENT_URI;

        //appointment is new, add notes to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mClientMap.put(saveItem.clientName, saveItem);
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete
 */
/**************************************************************************************************/
    /*
     * void deleteClient(...) - delete from firebase
     */
    public void deleteClient(ClientItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete notes
        String clientKey = deleteItem.fkey;

        //get notes firebase helper instance
        ClientFirebaseHelper notesFB = ClientFirebaseHelper.getInstance();

        //delete notes from firebase
        notesFB.deleteClient(mUserId, clientKey, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deleteClientFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

    }

    /*
     * void deleteClientFromDatabase(...) - delete notes data from database
     */
    private void deleteClientFromDatabase(ClientItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.fkey;

        //get uri value from notes table
        Uri uri = ClientContract.CONTENT_URI;

        //remove notes from database
        mActivity.getContentResolver().delete(uri,
                ClientQueryHelper.fkeySelection,
                new String[]{mUserId, clientKey});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Update
 */
/**************************************************************************************************/
    private ClientItem mUpdateClient;
    public void updateClientGoals(ClientItem clientItem, OnSavedListener listener){
        mUpdateClient = clientItem;
        //save listener
        mSaveListener = listener;

        //save to firebase
        //get firebase helper instance
        ClientFirebaseHelper fbHelper = ClientFirebaseHelper.getInstance();

        //create firebase item
        fbHelper.setClientGoals(mUserId, mUpdateClient.fkey, mUpdateClient.goals, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateDatabase(mUpdateClient);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateClientStatus(ClientItem clientItem, OnSavedListener listener){
        mUpdateClient = clientItem;
        //save listener
        mSaveListener = listener;

        //save to firebase
        //get firebase helper instance
        ClientFirebaseHelper fbHelper = ClientFirebaseHelper.getInstance();

        //create firebase item
        fbHelper.setClientStatus(mUserId, mUpdateClient.fkey, mUpdateClient.status, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateDatabase(mUpdateClient);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /*
     * void saveToFirebase(ClientItem) - save to firebase
     */
    private void updateDatabase(ClientItem clientItem){
        // The URI Matcher used by this content provider.
        DBHelper mOpenHelper = new DBHelper(mActivity);;

        //open database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues newValues = clientItem.getContentValues();

        String where = ClientContract.COLUMN_FKEY + " = '" + clientItem.fkey + "'";
        db.update(ClientContract.TABLE_NAME, newValues, where, null);

        db.close();

        mSaveListener.onSaved();
    }


/**************************************************************************************************/

}
