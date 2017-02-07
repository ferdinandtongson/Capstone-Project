package me.makeachoice.gymratpta.controller.modelside.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.exercise.RoutineFBItem;


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

    public final static String CHILD_ROUTINE_NAME = "routineName";
    public final static String CHILD_EXERCISE01 = "exercise01";
    public final static String CHILD_EXERCISE02 = "exercise02";
    public final static String CHILD_EXERCISE03 = "exercise03";
    public final static String CHILD_EXERCISE04 = "exercise04";
    public final static String CHILD_EXERCISE05 = "exercise05";
    public final static String CHILD_EXERCISE06 = "exercise06";
    public final static String CHILD_EXERCISE07 = "exercise07";
    public final static String CHILD_EXERCISE08 = "exercise08";
    public final static String CHILD_EXERCISE09 = "exercise09";
    public final static String CHILD_EXERCISE10 = "exercise10";
    public final static String CHILD_CATEGORY01 = "category01";
    public final static String CHILD_CATEGORY02 = "category02";
    public final static String CHILD_CATEGORY03 = "category03";
    public final static String CHILD_CATEGORY04 = "category04";
    public final static String CHILD_CATEGORY05 = "category05";
    public final static String CHILD_CATEGORY06 = "category06";
    public final static String CHILD_CATEGORY07 = "category07";
    public final static String CHILD_CATEGORY08 = "category08";
    public final static String CHILD_CATEGORY09 = "category09";
    public final static String CHILD_CATEGORY10 = "category10";
    public final static String CHILD_SET01 = "set01";
    public final static String CHILD_SET02 = "set02";
    public final static String CHILD_SET03 = "set03";
    public final static String CHILD_SET04 = "set04";
    public final static String CHILD_SET05 = "set05";
    public final static String CHILD_SET06 = "set06";
    public final static String CHILD_SET07 = "set07";
    public final static String CHILD_SET08 = "set08";
    public final static String CHILD_SET09 = "set09";
    public final static String CHILD_SET10 = "set10";

    //PARENT - parent director
    private final static String PARENT = "routine";

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

    private DatabaseReference getRoutineDetailReference(String userId, String fkey){
        return getRoutineReference(userId).child(fkey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addRoutineData(String userId, ArrayList<RoutineFBItem> routines){
        int count = routines.size();
        for(int i = 0; i < count; i++){
            addRoutine(userId, routines.get(i));
        }

    }

    public void addRoutine(String userId, RoutineFBItem item){
        DatabaseReference ref = getRoutineReference(userId);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Data Methods
 */
/**************************************************************************************************/

    public void setRoutineName(String userId, String fkey, String name){
        DatabaseReference refRoutine = getRoutineDetailReference(userId, fkey);

        refRoutine.child(CHILD_ROUTINE_NAME).setValue(name);
    }

    public void setRoutineDetail(String userId, String fkey, int index, String exercise, String category, int set){
        DatabaseReference refRoutine = getRoutineDetailReference(userId, fkey);

        switch(index){
            case 1:
                refRoutine.child(CHILD_EXERCISE01).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY01).setValue(category);
                refRoutine.child(CHILD_SET01).setValue(set);
                break;
            case 2:
                refRoutine.child(CHILD_EXERCISE02).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY02).setValue(category);
                refRoutine.child(CHILD_SET02).setValue(set);
                break;
            case 3:
                refRoutine.child(CHILD_EXERCISE03).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY03).setValue(category);
                refRoutine.child(CHILD_SET03).setValue(set);
                break;
            case 4:
                refRoutine.child(CHILD_EXERCISE04).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY04).setValue(category);
                refRoutine.child(CHILD_SET04).setValue(set);
                break;
            case 5:
                refRoutine.child(CHILD_EXERCISE05).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY05).setValue(category);
                refRoutine.child(CHILD_SET05).setValue(set);
                break;
            case 6:
                refRoutine.child(CHILD_EXERCISE06).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY06).setValue(category);
                refRoutine.child(CHILD_SET06).setValue(set);
                break;
            case 7:
                refRoutine.child(CHILD_EXERCISE07).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY07).setValue(category);
                refRoutine.child(CHILD_SET07).setValue(set);
                break;
            case 8:
                refRoutine.child(CHILD_EXERCISE08).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY08).setValue(category);
                refRoutine.child(CHILD_SET08).setValue(set);
                break;
            case 9:
                refRoutine.child(CHILD_EXERCISE09).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY09).setValue(category);
                refRoutine.child(CHILD_SET09).setValue(set);
                break;
            case 10:
                refRoutine.child(CHILD_EXERCISE10).setValue(exercise);
                refRoutine.child(CHILD_CATEGORY10).setValue(category);
                refRoutine.child(CHILD_SET10).setValue(set);
                break;
        }
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

    public void requestRoutineDataByName(String userId, String name, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getRoutineReference(userId);

        mOnDataLoadedListener = listener;

        ref.orderByChild(CHILD_ROUTINE_NAME).equalTo(name).addListenerForSingleValueEvent(mEventListener);

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
