package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.ClientExerciseFBItem;

/**************************************************************************************************/
/*
 * ClientExerciseFirebaseHelper helps in adding and requesting client exercise data to and from firebase
 */
/**************************************************************************************************/

public class ClientExerciseFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public static String CHILD_APPOINTMENT_DATE = "appointmentDate";
    public static String CHILD_APPOINTMENT_TIME = "appointmentTime";
    public static String CHILD_EXERCISE = "exercise";
    public static String CHILD_CATEGORY = "category";
    public static String CHILD_SET_NUMBER = "setNumber";
    public static String CHILD_PRIMARY_LABEL = "primaryLabel";
    public static String CHILD_PRIMARY_VALUE = "primaryValue";
    public static String CHILD_SECONDARY_LABEL = "secondaryLabel";
    public static String CHILD_SECONDARY_VALUE = "secondaryValue";

    //PARENT - parent director
    private static String PARENT = "clientExercise";

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

    private static ClientExerciseFirebaseHelper instance = null;

    public static ClientExerciseFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ClientExerciseFirebaseHelper();
        }
        return instance;
    }

    private ClientExerciseFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getClientExerciseReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getClientExerciseReferenceByClientKey(String userId, String clientKey){
        return getClientExerciseReference(userId).child(clientKey);
    }

    private DatabaseReference getClientExerciseReferenceByTimestamp(String userId, String clientKey,
                                                                   String timestamp){
        return getClientExerciseReference(userId).child(clientKey).child(timestamp);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addClientExerciseDataByClientKey(String userId, String clientKey,
                                                 ArrayList<ClientExerciseFBItem> exercises){
        ClientExerciseFBItem item;

        int count = exercises.size();
        for(int i = 0; i < count; i++){
            item = exercises.get(i);
            addClientExerciseByClientKey(userId, clientKey, item);
        }
    }

    public void addClientExerciseByClientKey(String userId, String clientKey, ClientExerciseFBItem item){
        DatabaseReference ref = getClientExerciseReferenceByClientKey(userId, clientKey);

        ref.setValue(item);
    }

    public void adClientExerciseReferenceByTimestamp(String userId, String clientKey, String timestamp,
                                                     ClientExerciseFBItem item){

        DatabaseReference ref = getClientExerciseReferenceByTimestamp(userId, clientKey, timestamp);

        ref.push().setValue(item);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteClientExerciseByExercise(String userId, String clientKey, String timestamp,
                                     String exercise, ValueEventListener listener){

        DatabaseReference ref = getClientExerciseReferenceByTimestamp(userId, clientKey, timestamp);

        Query exerciseQuery = ref.orderByChild(CHILD_EXERCISE).equalTo(exercise);

        exerciseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        ref.addListenerForSingleValueEvent(listener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestClientExerciseDataByClientKey(String userId, String clientKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientExerciseReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientExerciseDataByClientKey(String userId, String clientKey,
                                                    String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientExerciseReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientExerciseDataByTimestamp(String userId, String clientKey, String timestamp,
                                                     String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientExerciseReferenceByTimestamp(userId, clientKey, timestamp);

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
