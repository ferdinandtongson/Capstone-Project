package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.ScheduleFBItem;

/**************************************************************************************************/
/*
 * ScheduleFirebaseHelper helps in adding and requesting schedule appointment data to and from firebase
 */
/**************************************************************************************************/

public class ScheduleFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_APPOINTMENT_DATE = "appointmentDate";
    public final static String CHILD_APPOINTMENT_TIME = "appointmentTime";
    public final static String CHILD_CLIENT_KEY = "clientKey";
    public final static String CHILD_CLIENT_NAME = "clientName";
    public final static String CHILD_ROUTINE_NAME = "routineName";
    public final static String CHILD_STATUS = "status";

    //PARENT - parent director
    private final static String PARENT = "userSchedule";

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

    private static ScheduleFirebaseHelper instance = null;

    public static ScheduleFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ScheduleFirebaseHelper();
        }
        return instance;
    }

    private ScheduleFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getScheduleReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getScheduleReferenceByTimestamp(String userId, String timestamp){
        return getScheduleReference(userId).child(timestamp);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addScheduleDataByTimestamp(String userId, String timestamp, ArrayList<ScheduleFBItem> schedule){
        int count = schedule.size();
        for(int i = 0; i < count; i++){
            addScheduleByTimestamp(userId, timestamp, schedule.get(i));
        }

    }

    public void addScheduleByTimestamp(String userId, String timestamp, ScheduleFBItem item){
        DatabaseReference ref = getScheduleReferenceByTimestamp(userId, timestamp);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteSchedule(String userId, String timestamp, String clientName, ValueEventListener listener){
        DatabaseReference ref = getScheduleReferenceByTimestamp(userId, timestamp);

        Query appointmentQuery = ref.orderByChild(CHILD_CLIENT_NAME).equalTo(clientName);

        appointmentQuery.addListenerForSingleValueEvent(listener);

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestScheduleDataByTimestamp(String userId, String timestamp, String orderBy,
                                                OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getScheduleReferenceByTimestamp(userId, timestamp);

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
