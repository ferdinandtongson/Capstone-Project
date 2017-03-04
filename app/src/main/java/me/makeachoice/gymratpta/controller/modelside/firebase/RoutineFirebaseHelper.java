package me.makeachoice.gymratpta.controller.modelside.firebase;

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
 * RoutineFirebaseHelper helps in add and requesting routine data from firebase
 */
/**************************************************************************************************/

public class RoutineFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public static String CHILD_ROUTINE_NAME = "routineName";
    public static String CHILD_ORDER_NUMBER = "orderNumber";
    public static String CHILD_EXERCISE = "exercise";
    public static String CHILD_CATEGORY = "category";
    public static String CHILD_NUM_SETS = "numOfSets";

    //PARENT - parent director
    private static String PARENT = "routine";

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

    private static RoutineFirebaseHelper instance = null;

    public static RoutineFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new RoutineFirebaseHelper();
        }
        return instance;
    }

    private RoutineFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getRoutineReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getRoutineDetailReference(String userId, String routineName){
        return getRoutineReference(userId).child(routineName);
    }

    private DatabaseReference getRoutineExerciseReference(String userId, String routineName, String orderNumber){
        return getRoutineReference(userId).child(routineName).child(orderNumber);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addRoutineData(String userId, ArrayList<RoutineItem> routines){
        RoutineItem routine;

        int count = routines.size();
        for(int i = 0; i < count; i++){
            routine = routines.get(i);

            RoutineFBItem item = new RoutineFBItem();
            item.exercise = routine.exercise;
            item.category = routine.category;
            item.numOfSets = routine.numOfSets;
            addRoutine(userId, routine, item);
        }

    }

    public void addRoutine(String userId, RoutineItem routine, RoutineFBItem item){
        DatabaseReference ref = getRoutineExerciseReference(userId, routine.routineName, routine.orderNumber);
        ref.setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Data Methods
 */
/**************************************************************************************************/

    public void setRoutineName(String userId, String routineName, String name){
        DatabaseReference refRoutine = getRoutineDetailReference(userId, routineName);

        refRoutine.child(CHILD_ROUTINE_NAME).setValue(name);
    }

    public void setRoutineDetail(String userId, String routineName, int index, String exercise, String category, int set){
        DatabaseReference refRoutine = getRoutineDetailReference(userId, routineName);


  }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Remove Data Methods
 */
/**************************************************************************************************/

    public void deleteRoutine(String userId, String routineName, ValueEventListener listener){
        DatabaseReference refRoutine = getRoutineDetailReference(userId, routineName);

        refRoutine.removeValue();
        refRoutine.addListenerForSingleValueEvent(listener);
    }

    public void deleteRoutineExerciseByIndex(String userId, String routineName, String index, ValueEventListener listener){
        DatabaseReference refRoutine = getRoutineDetailReference(userId, routineName);

        refRoutine.child(index).removeValue();
        refRoutine.addListenerForSingleValueEvent(listener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestRoutineData(String userId, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getRoutineReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestRoutineData(String userId, String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getRoutineReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestRoutineDetailByName(String userId, String routineName, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getRoutineDetailReference(userId, routineName);

        mOnDataLoadedListener = listener;

        ref.orderByChild(CHILD_ORDER_NUMBER).addListenerForSingleValueEvent(mEventListener);

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
