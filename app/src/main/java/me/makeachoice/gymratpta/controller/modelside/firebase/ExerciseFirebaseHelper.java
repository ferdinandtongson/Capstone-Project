package me.makeachoice.gymratpta.controller.modelside.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.exercise.ExerciseFBItem;


/**************************************************************************************************/
/*
 * ExerciseFirebaseHelper helps in adding and requesting exercise data from firebase
 */
/**************************************************************************************************/

public class ExerciseFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_EXERCISE_NAME = "exerciseName";
    public final static String CHILD_EXERCISE_CATEGORY = "exerciseCategory";
    public final static String CHILD_EXERCISE_REC_PRIMARY = "recordPrimary";
    public final static String CHILD_EXERCISE_REC_SECONDARY = "recordSecondary";

    //PARENT - parent director
    private final static String PARENT = "exercise";

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

    private static ExerciseFirebaseHelper instance = null;

    public static ExerciseFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ExerciseFirebaseHelper();
        }
        return instance;
    }

    private ExerciseFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getExerciseReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getExerciseCategoryReference(String userId, String categoryKey){
        return getExerciseReference(userId).child(categoryKey);
    }

    private DatabaseReference getExerciseDetailReference(String userId, String categoryKey, String exerciseKey){
        return getExerciseReference(userId).child(categoryKey).child(exerciseKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addExerciseDataToCategory(String userId, String categoryKey, ArrayList<ExerciseFBItem> exercises){
        int count = exercises.size();
        for(int i = 0; i < count; i++){
            addExerciseToCategory(userId, categoryKey, exercises.get(i));
        }

    }

    public void addExerciseToCategory(String userId, String categoryKey, ExerciseFBItem item){
        DatabaseReference ref = getExerciseCategoryReference(userId, categoryKey);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Data Methods
 */
/**************************************************************************************************/

    public void setExerciseName(String userId, String categoryKey, String fkey, String name){
        DatabaseReference refExercise = getExerciseDetailReference(userId, categoryKey, fkey);

        refExercise.child(CHILD_EXERCISE_NAME).setValue(name);
    }

    public void setExerciseCategory(String userId, String categoryKey, String fkey, String category){
        DatabaseReference refExercise = getExerciseDetailReference(userId, categoryKey, fkey);

        refExercise.child(CHILD_EXERCISE_CATEGORY).setValue(category);
    }

    public void setExercisePrimaryRecord(String userId, String categoryKey, String fkey, String recordType){
        DatabaseReference refExercise = getExerciseDetailReference(userId, categoryKey, fkey);

        refExercise.child(CHILD_EXERCISE_REC_PRIMARY).setValue(recordType);
    }

    public void setExerciseSecondaryRecord(String userId, String categoryKey, String fkey, String recordType){
        DatabaseReference refExercise = getExerciseDetailReference(userId, categoryKey, fkey);

        refExercise.child(CHILD_EXERCISE_REC_SECONDARY).setValue(recordType);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestExerciseData(String userId, String categoryKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getExerciseCategoryReference(userId, categoryKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestExerciseData(String userId, String categoryKey, String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getExerciseCategoryReference(userId, categoryKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestExerciseDataByName(String userId, String categoryKey, String name, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getExerciseCategoryReference(userId, categoryKey);

        mOnDataLoadedListener = listener;

        ref.orderByChild(CHILD_EXERCISE_NAME).equalTo(name).addListenerForSingleValueEvent(mEventListener);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteExercise(String userId, String categoryKey, String exercise, ValueEventListener listener){
        DatabaseReference ref = getExerciseCategoryReference(userId, categoryKey);

        Query query = ref.orderByChild(CHILD_EXERCISE_NAME).equalTo(exercise);

        query.addListenerForSingleValueEvent(listener);
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
