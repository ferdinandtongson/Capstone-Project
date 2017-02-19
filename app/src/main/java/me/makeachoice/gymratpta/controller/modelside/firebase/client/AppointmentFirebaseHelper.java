package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.AppointmentFBItem;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**************************************************************************************************/
/*
 * AppointmentFirebaseHelper helps in adding and requesting client appointment data to and from firebase
 */
/**************************************************************************************************/

public class AppointmentFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_APPOINTMENT_TIME = "appointmentTime";
    public final static String CHILD_CLIENT_KEY = "clientKey";
    public final static String CHILD_CLIENT_NAME = "clientName";
    public final static String CHILD_ROUTINE_NAME = "routineName";
    public final static String CHILD_STATUS = "status";

    //PARENT - parent director
    private final static String PARENT = "appointment";

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

    private static AppointmentFirebaseHelper instance = null;

    public static AppointmentFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new AppointmentFirebaseHelper();
        }
        return instance;
    }

    private AppointmentFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getAppointmentReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getAppointmentReferenceByDay(String userId, String appointmentDay){
        return getAppointmentReference(userId).child(appointmentDay);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addAppointmentDataByDay(String userId, String appointmentDay, ArrayList<AppointmentFBItem> appointments){
        int count = appointments.size();
        for(int i = 0; i < count; i++){
            addAppointmentByDay(userId, appointmentDay, appointments.get(i));
        }

    }

    public void addAppointmentByDay(String userId, String appointmentDay, AppointmentFBItem item){
        DatabaseReference ref = getAppointmentReferenceByDay(userId, appointmentDay);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteAppointment(String userId, String appointmentDay, String clientName,
                                  String appointmentTime, ValueEventListener listener){
        DatabaseReference ref = getAppointmentReferenceByDay(userId, appointmentDay);

        Query appointmentQuery = ref.orderByChild(CHILD_CLIENT_NAME).equalTo(clientName);

        final String appTime = appointmentTime;

        appointmentQuery.addListenerForSingleValueEvent(listener);

    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestAppointmentDataByDate(String userId, String appointmentDate, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getAppointmentReferenceByDay(userId, appointmentDate);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestAppointmentDataByDate(String userId, String appointmentDate, String orderBy,
                                                OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getAppointmentReferenceByDay(userId, appointmentDate);

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
