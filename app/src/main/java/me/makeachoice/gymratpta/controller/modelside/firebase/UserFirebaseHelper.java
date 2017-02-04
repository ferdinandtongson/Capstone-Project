package me.makeachoice.gymratpta.controller.modelside.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.UserItem;

/**************************************************************************************************/
/*
 * UserFirebaseHelper helps to add and pull user data to and from Firebase
 */
/**************************************************************************************************/

public class UserFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    //PARENT - parent director
    public final static String PARENT = "user";

    //mFireDB - firebase instance
    private FirebaseDatabase mFireDB;

    //mUserId - user id
    private String mUserId;

    private OnDataLoadedListener mOnDataLoadedListener;

    //Implemented communication bridge
    public interface OnDataLoadedListener{
        //called when requested data has been loaded
        void onDataLoaded(UserItem user);
        void onCancelled();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor:
 */
/**************************************************************************************************/

    private static UserFirebaseHelper instance = null;

    protected UserFirebaseHelper() {
        // Exists only to defeat instantiation.
    }

    public static UserFirebaseHelper getInstance(String userId) {
        if(instance == null) {
            instance = new UserFirebaseHelper(userId);
        }
        return instance;
    }

    private UserFirebaseHelper(String userId){
        mUserId = userId;

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getUserReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addUserData(ArrayList<UserItem> users){
        int count = users.size();
        for(int i = 0; i < count; i++){
            addUser(users.get(i));
        }

    }

    public void addUser(UserItem item){
        DatabaseReference ref = getUserReference(item.uid);
        ref.setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestUserData(String userId, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getUserReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

/**************************************************************************************************/

    private ValueEventListener mEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            UserItem item = dataSnapshot.getValue(UserItem.class);
            mOnDataLoadedListener.onDataLoaded(item);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mOnDataLoadedListener.onCancelled();
        }
    };

}
