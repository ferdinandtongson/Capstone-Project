package me.makeachoice.gymratpta.controller.modelside.firebase;

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

    //PARENT - parent director
    public final static String PARENT = "client";

    //mFireDB - firebase instance
    private FirebaseDatabase mFireDB;

    //mUserId - user id
    private String mUserId;

    private OnDataLoadedListener mOnDataLoadedListener;

    //Implemented communication bridge
    public interface OnDataLoadedListener{
        //called when requested data has been loaded
        void onDataLoaded(ClientFBItem user);
        void onCancelled();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor:
 */
/**************************************************************************************************/

    private static ClientFirebaseHelper instance = null;

    protected ClientFirebaseHelper() {
        // Exists only to defeat instantiation.
    }

    public static ClientFirebaseHelper getInstance(String userId) {
        if(instance == null) {
            instance = new ClientFirebaseHelper(userId);
        }
        return instance;
    }

    private ClientFirebaseHelper(String userId){
        mUserId = userId;

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

    public void setUserId(String userId){
        mUserId = userId;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addClientData(ArrayList<ClientFBItem> users){
        int count = users.size();
        for(int i = 0; i < count; i++){
            addClient(users.get(i));
        }

    }

    public void addClient(ClientFBItem item){
        DatabaseReference ref = getClientReference(mUserId);
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

/**************************************************************************************************/

    private ValueEventListener mEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ClientFBItem item = dataSnapshot.getValue(ClientFBItem.class);
            mOnDataLoadedListener.onDataLoaded(item);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mOnDataLoadedListener.onCancelled();
        }
    };


}
