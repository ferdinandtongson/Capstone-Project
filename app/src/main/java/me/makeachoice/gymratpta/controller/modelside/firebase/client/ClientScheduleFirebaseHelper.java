package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;

/**************************************************************************************************/
/*
 * ClientScheduleFirebaseHelper helps in adding and requesting client schedule data to and from firebase
 */
/**************************************************************************************************/

public class ClientScheduleFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_APPOINTMENT_DATE = "appointmentDate";
    public final static String CHILD_APPOINTMENT_TIME = "appointmentTime";
    public final static String CHILD_CLIENT_NAME = "clientName";
    public final static String CHILD_STATUS = "status";

    //PARENT - parent director
    private final static String PARENT = "clientSchedule";

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

    private static ClientScheduleFirebaseHelper instance = null;

    public static ClientScheduleFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ClientScheduleFirebaseHelper();
        }
        return instance;
    }

    private ClientScheduleFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getClientScheduleReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getClientScheduleReferenceByClientKey(String userId, String clientKey){
        return getClientScheduleReference(userId).child(clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addClientScheduleDataByClientKey(String userId, String clientKey, ArrayList<ClientAppFBItem> schedule){
        int count = schedule.size();
        for(int i = 0; i < count; i++){
            addClientScheduleByClientKey(userId, clientKey, schedule.get(i));
        }

    }

    public void addClientScheduleByClientKey(String userId, String clientKey, ClientAppFBItem item){
        DatabaseReference ref = getClientScheduleReferenceByClientKey(userId, clientKey);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteSchedule(String userId, String clientKey, String appointmentDate,
                                  String appointmentTime, ValueEventListener listener){
        DatabaseReference ref = getClientScheduleReferenceByClientKey(userId, clientKey);

        Query scheduleQuery = ref.orderByChild(CHILD_APPOINTMENT_DATE).equalTo(appointmentDate);

        final String appTime = appointmentTime;

        scheduleQuery.addListenerForSingleValueEvent(listener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestClientScheduleDataByClientKey(String userId, String clientKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientScheduleReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientScheduleDataByClientKey(String userId, String appointmentDay, String orderBy,
                                                OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientScheduleReferenceByClientKey(userId, appointmentDay);

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
