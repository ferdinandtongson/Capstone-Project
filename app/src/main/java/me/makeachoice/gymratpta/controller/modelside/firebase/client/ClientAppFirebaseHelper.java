package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.ClientAppFBItem;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**************************************************************************************************/
/*
 * ClientAppFirebaseHelper helps in adding and requesting client appointment data to and from firebase
 */
/**************************************************************************************************/

public class ClientAppFirebaseHelper {

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
    private final static String PARENT = "clientAppointment";

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

    private static ClientAppFirebaseHelper instance = null;

    public static ClientAppFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new ClientAppFirebaseHelper();
        }
        return instance;
    }

    private ClientAppFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getClientAppReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getClientAppReferenceByClientKey(String userId, String clientKey){
        return getClientAppReference(userId).child(clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addClientAppDataByClientKey(String userId, String clientKey, ArrayList<ClientAppFBItem> appointments){
        int count = appointments.size();
        for(int i = 0; i < count; i++){
            addClientAppByClientKey(userId, clientKey, appointments.get(i));
        }

    }

    public void addClientAppByClientKey(String userId, String clientKey, ClientAppFBItem item){
        DatabaseReference ref = getClientAppReferenceByClientKey(userId, clientKey);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteAppointment(String userId, String clientKey, String appointmentDay, String appointmentTime){
        DatabaseReference refRoutine = getClientAppReferenceByClientKey(userId, clientKey);

        Query appointmentQuery = refRoutine.orderByChild(CHILD_CLIENT_NAME).equalTo(appointmentDay);

        final String appTime = appointmentTime;

        appointmentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClientAppFBItem appointment = postSnapshot.getValue(ClientAppFBItem.class);
                    if(appointment.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        refRoutine.removeValue();

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestClientAppDataByClientKey(String userId, String clientKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientAppReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestClientAppDataByClientKey(String userId, String appointmentDay, String orderBy,
                                                OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getClientAppReferenceByClientKey(userId, appointmentDay);

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
