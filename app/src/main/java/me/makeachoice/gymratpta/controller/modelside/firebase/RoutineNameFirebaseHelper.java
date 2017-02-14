package me.makeachoice.gymratpta.controller.modelside.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**************************************************************************************************/
/*
 * RoutineNameFirebaseHelper helps in accessing routine name data from firebase
 */
/**************************************************************************************************/

public class RoutineNameFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 *      String PARENT - parent director
 *      FirebaseDatabase mFireDB - firebase instance
 *      OnDataLoadedListener mOnDataLoadListener - listens for onDataLoaded events
 */
/**************************************************************************************************/

    //PARENT - parent director
    private static String PARENT = "routineName";

    //mFireDB - firebase instance
    private FirebaseDatabase mFireDB;

    //mOnDataLoadListener - listens for onDataLoaded events
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

    private static RoutineNameFirebaseHelper instance = null;

    public static RoutineNameFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new RoutineNameFirebaseHelper();
        }
        return instance;
    }

    private RoutineNameFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getRoutineNameReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addRoutineNameData(String userId, ArrayList<String> routineNames){
        int count = routineNames.size();
        for(int i = 0; i < count; i++){
            addRoutineName(userId, routineNames.get(i));
        }

    }

    public void addRoutineName(String userId, String routineName){
        DatabaseReference ref = getRoutineNameReference(userId).child(routineName);

        ref.setValue("");
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteRoutineName(String userId, String routineName){
        DatabaseReference refRoutine = getRoutineNameReference(userId).child(routineName);

        refRoutine.removeValue();

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestRoutineData(String userId, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getRoutineNameReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestRoutineData(String userId, String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getRoutineNameReference(userId);

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
