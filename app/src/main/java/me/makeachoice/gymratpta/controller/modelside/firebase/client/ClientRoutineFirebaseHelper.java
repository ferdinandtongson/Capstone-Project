package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private DatabaseReference getClientRoutineReferenceByDate(String userId, String clientKey,
                                                                  String appointmentDate){
        return getClientRoutineReference(userId).child(clientKey).child(appointmentDate);
    }

    private DatabaseReference getClientRoutineReferenceByDateTime(String userId, String clientKey,
                                                                  String appointmentDate, String appointmentTime){
        return getClientRoutineReference(userId).child(clientKey).child(appointmentDate).child(appointmentTime);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addRoutineDataByDateTime(String userId, String clientKey, String appointmentDate,
                                   String appointmentTime, ArrayList<RoutineItem> routines){
        RoutineItem routine;

        int count = routines.size();
        for(int i = 0; i < count; i++){
            routine = routines.get(i);

            String strIndex = String.valueOf(routine.orderNumber);

            RoutineFBItem item = new RoutineFBItem();
            item.exercise = routine.exercise;
            item.category = routine.category;
            item.numOfSets = routine.numOfSets;
            addRoutineByDateTime(userId, clientKey, appointmentDate, appointmentTime, strIndex, item);
        }
    }

    public void addRoutineByDateTime(String userId, String clientKey, String appointmentDate, String appointmentTime,
                           String strIndex, RoutineFBItem item){
        DatabaseReference ref = getClientRoutineReferenceByDateTime(userId, clientKey,
                appointmentDate, appointmentTime);

        ref.child(strIndex).setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteClientRoutine(String userId, String clientKey, String appointmentDate,
                                    String appointmentTime, ValueEventListener listener){
        DatabaseReference ref = getClientRoutineReferenceByDateTime(userId, clientKey,
                appointmentDate, appointmentTime);

        ref.addListenerForSingleValueEvent(listener);
        ref.removeValue();
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

    public void requestClientRoutineDataByDateTime(String userId, String clientKey, String appointmentDay,
                                                   String appointmentTime, String orderBy,
                                                   OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientRoutineReferenceByDateTime(userId, clientKey,
                appointmentDay, appointmentTime);

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
