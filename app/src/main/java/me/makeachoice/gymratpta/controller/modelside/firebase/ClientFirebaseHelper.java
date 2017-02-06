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
 * ClientFirebaseHelper helps in adding and requesting client data to and from firebase
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

    private DatabaseReference getClientDetailReference(String userId, String clientKey){
        return getClientReference(userId).child(clientKey);
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
 * Set Data Methods
 */
/**************************************************************************************************/

    public void setClientEmail(String userId, String clientKey, String email){
        DatabaseReference refClient = getClientDetailReference(userId, clientKey);

        refClient.child(CHILD_EMAIL).setValue(email);
    }

    public void setClientFirstSession(String userId, String clientKey, String firstSession){
        DatabaseReference refClient = getClientDetailReference(userId, clientKey);

        refClient.child(CHILD_FIRST_SESSION).setValue(firstSession);
    }

    public void setClientPhone(String userId, String clientKey, String phone){
        DatabaseReference refClient = getClientDetailReference(userId, clientKey);

        refClient.child(CHILD_PHONE).setValue(phone);
    }

    public void setClientGoals(String userId, String clientKey, String goals){
        DatabaseReference refClient = getClientDetailReference(userId, clientKey);

        refClient.child(CHILD_GOALS).setValue(goals);
    }

    public void setClientStatus(String userId, String clientKey, String status){
        DatabaseReference refClient = getClientDetailReference(userId, clientKey);

        refClient.child(CHILD_STATUS).setValue(status);
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
        //get reference
        DatabaseReference ref = getClientReference(userId);

        mOnDataLoadedListener = listener;

        ref.orderByChild(CHILD_CLIENT_NAME).equalTo(clientName).addListenerForSingleValueEvent(mEventListener);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * EventListeners
 */
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

/**************************************************************************************************/

}
