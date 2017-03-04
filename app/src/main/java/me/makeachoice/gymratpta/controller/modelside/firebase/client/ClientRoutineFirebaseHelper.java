package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.exercise.RoutineFBItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;


/**************************************************************************************************/
/*
 * ClientRoutineFirebaseHelper helps in adding and requesting client routine data to and from firebase
 */
/**************************************************************************************************/

public class ClientRoutineFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public static String CHILD_EXERCISE = "exercise";
    public static String CHILD_CATEGORY = "category";
    public static String CHILD_NUM_SETS = "numOfSets";

    //PARENT - parent director
    private static String PARENT = "clientRoutine";

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

    private static ClientRoutineFirebaseHelper instance = null;

    public static ClientRoutineFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ClientRoutineFirebaseHelper();
        }
        return instance;
    }

    private ClientRoutineFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getClientRoutineReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getClientRoutineReferenceByClientKey(String userId, String clientKey){
        return getClientRoutineReference(userId).child(clientKey);
    }

    private DatabaseReference getClientRoutineReferenceByTimestamp(String userId, String clientKey,
                                                                  String timestamp){
        return getClientRoutineReference(userId).child(clientKey).child(timestamp);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addRoutineDataByTimestamp(String userId, String clientKey, String timestamp,
                                          ArrayList<RoutineItem> routines){
        RoutineItem routine;

        int count = routines.size();
        for(int i = 0; i < count; i++){
            routine = routines.get(i);

            RoutineFBItem item = new RoutineFBItem();
            item.exercise = routine.exercise;
            item.category = routine.category;
            item.numOfSets = routine.numOfSets;
            addRoutineByTimestamp(userId, clientKey, timestamp, routine.orderNumber, item);
        }
    }

    public void addRoutineByTimestamp(String userId, String clientKey, String timestamp, String strIndex, RoutineFBItem item){
        DatabaseReference ref = getClientRoutineReferenceByTimestamp(userId, clientKey, timestamp);

        ref.child(strIndex).setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteClientRoutine(String userId, String clientKey, String timestamp, ValueEventListener listener){
        DatabaseReference ref = getClientRoutineReferenceByTimestamp(userId, clientKey, timestamp);

        ref.addListenerForSingleValueEvent(listener);
        ref.removeValue();
    }

    public void deleteClientRoutineAtOrderNumber(String userId, String clientKey, String timestamp,
                                           String orderNumber, ValueEventListener listener){
        DatabaseReference ref = getClientRoutineReferenceByTimestamp(userId, clientKey, timestamp);

        ref.addListenerForSingleValueEvent(listener);
        ref.child(orderNumber).removeValue();
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestClientRoutineDataByClientKey(String userId, String clientKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientRoutineReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientRoutineDataByClientKey(String userId, String clientKey,
                                                    String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientRoutineReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientRoutineDataByTimestamp(String userId, String clientKey, String timestamp,
                                                   String orderBy,
                                                   OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientRoutineReferenceByTimestamp(userId, clientKey, timestamp);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
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
