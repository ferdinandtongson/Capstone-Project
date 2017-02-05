package me.makeachoice.gymratpta.controller.modelside.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.ClientFBItem;

/**************************************************************************************************/
/*
 * ClientFirebaseHelper helps in adding and requesting client data to and from firedatabase
 */
/**************************************************************************************************/

public class ClientFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_CLIENT_NAME = "clientName";
    public final static String CHILD_EMAIL = "email";
    public final static String CHILD_FIRST_SESSION = "firstSession";
    public final static String CHILD_GOALS = "goals";
    public final static String CHILD_PHONE = "phone";
    public final static String CHILD_STATUS = "status";

    //PARENT - parent director
    private final static String PARENT = "client";

    //mFireDB - firebase instance
    private FirebaseDatabase mFireDB;

    private OnDataLoadedListener mOnDataLoadedListener;

    //Implemented communication bridge
    public interface OnDataLoadedListener{
        //called when requested data has been loaded
        void onDataLoaded(DataSnapshot dataSnapshot);
        void onCancelled();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor:
 */
/**************************************************************************************************/

    private static ClientFirebaseHelper instance = null;

    public static ClientFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ClientFirebaseHelper();
        }
        return instance;
    }

    private ClientFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getClientReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addClientData(String userId, ArrayList<ClientFBItem> users){
        int count = users.size();
        for(int i = 0; i < count; i++){
            addClient(userId, users.get(i));
        }

    }

    public void addClient(String userId, ClientFBItem item){
        DatabaseReference ref = getClientReference(userId);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestClientData(String userId, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientData(String userId, String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientDataByClientName(String userId, String clientName, OnDataLoadedListener listener){
        Log.d("Choice", "ClientFBHelper.requestClientDataByClientName: " + clientName);
        //get reference
        DatabaseReference ref = getClientReference(userId);

        mOnDataLoadedListener = listener;

        ref.orderByChild("clientName").equalTo(clientName).addListenerForSingleValueEvent(mEventListener);

    }

/**************************************************************************************************/

    private ValueEventListener mEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mOnDataLoadedListener.onDataLoaded(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mOnDataLoadedListener.onCancelled();
        }
    };


}
