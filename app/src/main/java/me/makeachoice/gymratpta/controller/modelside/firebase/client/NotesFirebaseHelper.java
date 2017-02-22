package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.NotesFBItem;

/**************************************************************************************************/
/*
 * NotesFirebaseHelper helps in adding and requesting client notes data to and from firebase
 */
/**************************************************************************************************/

public class NotesFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_APPOINTMENT_TIME = "appointmentTime";
    public final static String CHILD_MODIFIED_DATE = "modifiedDate";
    public final static String CHILD_SUBJECTIVE_NOTES = "subjectiveNotes";
    public final static String CHILD_OBJECTIVE_NOTES = "objectiveNotes";
    public final static String CHILD_ASSESSMENT_NOTES = "assessmentNotes";
    public final static String CHILD_PLAN_NOTES = "planNotes";

    //PARENT - parent director
    private final static String PARENT = "clientNotes";

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

    private static NotesFirebaseHelper instance = null;

    public static NotesFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new NotesFirebaseHelper();
        }
        return instance;
    }

    private NotesFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getNotesReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getNotesReferenceByClientKey(String userId, String clientKey){
        return getNotesReference(userId).child(clientKey);
    }

    private DatabaseReference getNotesReferenceByDate(String userId, String clientKey,
                                                                 String appointmentDate){
        return getNotesReference(userId).child(clientKey).child(appointmentDate);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addNotesDataByDate(String userId, String clientKey, String appointmentDate,
                                              ArrayList<NotesFBItem> notes){
        int count = notes.size();
        for(int i = 0; i < count; i++){
            addNotesByDate(userId, clientKey, appointmentDate, notes.get(i));
        }

    }

    public void addNotesByDate(String userId, String clientKey, String appointmentDate, NotesFBItem item){
        DatabaseReference ref = getNotesReferenceByDate(userId, clientKey, appointmentDate);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteNotes(String userId, String clientKey, String appointmentDate,
                                  String appointmentTime, ValueEventListener listener){
        DatabaseReference ref = getNotesReferenceByDate(userId, clientKey, appointmentDate);

        Query notesQuery = ref.orderByChild(CHILD_APPOINTMENT_TIME).equalTo(appointmentTime);

        final String appTime = appointmentTime;

        notesQuery.addListenerForSingleValueEvent(listener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestNotesDataByClientKey(String userId, String clientKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getNotesReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestNotesDataByClientKey(String userId, String clientKey, String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getNotesReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestNotesDataByDate(String userId, String clientKey, String appointmentDay, String orderBy,
                                                OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getNotesReferenceByDate(userId, clientKey, appointmentDay);

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
