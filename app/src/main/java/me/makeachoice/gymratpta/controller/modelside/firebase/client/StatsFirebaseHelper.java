package me.makeachoice.gymratpta.controller.modelside.firebase.client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.client.StatsFBItem;


public class StatsFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_APPOINTMENT_DATE = "appointmentDate";
    public final static String CHILD_APPOINTMENT_TIME = "appointmentTime";
    public final static String CHILD_MODIFIED_DATE = "modifiedDate";
    public final static String CHILD_STAT_WEIGHT = "statWeight";
    public final static String CHILD_STAT_BODY_FAT = "statBodyFat";
    public final static String CHILD_STAT_BMI = "statBMI";
    public final static String CHILD_STAT_NECK = "statNeck";
    public final static String CHILD_STAT_CHEST = "statChest";
    public final static String CHILD_STAT_RBICEP = "statRBicep";
    public final static String CHILD_STAT_LBICEP = "statLBicep";
    public final static String CHILD_STAT_WAIST = "statWaist";
    public final static String CHILD_STAT_NAVAL = "statNaval";
    public final static String CHILD_STAT_HIPS = "statHips";
    public final static String CHILD_STAT_RTHIGH = "statRThigh";
    public final static String CHILD_STAT_LTHIGH = "statLThigh";
    public final static String CHILD_STAT_RCALF = "statRCalf";
    public final static String CHILD_STAT_LCALF = "statLCalf";

    //PARENT - parent director
    private final static String PARENT = "clientStats";

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

    private static StatsFirebaseHelper instance = null;

    public static StatsFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new StatsFirebaseHelper();
        }
        return instance;
    }

    private StatsFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getStatsReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getStatsReferenceByClientKey(String userId, String clientKey){
        return getStatsReference(userId).child(clientKey);
    }

    private DatabaseReference getStatsReferenceByTimestamp(String userId, String clientKey,
                                                      String timestamp){
        return getStatsReference(userId).child(clientKey).child(timestamp);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addStatsDataByTimestamp(String userId, String clientKey, String timestamp,
                                   ArrayList<StatsFBItem> stats){
        int count = stats.size();
        for(int i = 0; i < count; i++){
            addStatsByTimestamp(userId, clientKey, timestamp, stats.get(i));
        }

    }

    public void addStatsByTimestamp(String userId, String clientKey, String timestamp, StatsFBItem item){
        DatabaseReference ref = getStatsReferenceByTimestamp(userId, clientKey, timestamp);
        ref.push().setValue(item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Data Methods
 */
/**************************************************************************************************/

    public void deleteStats(String userId, String clientKey, String timestamp,
                            String appointmentTime, ValueEventListener listener){
        DatabaseReference ref = getStatsReferenceByTimestamp(userId, clientKey, timestamp);

        Query statsQuery = ref.orderByChild(CHILD_APPOINTMENT_TIME).equalTo(appointmentTime);

        statsQuery.addListenerForSingleValueEvent(listener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestStatsDataByClientKey(String userId, String clientKey, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getStatsReferenceByClientKey(userId, clientKey);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestStatsDataByTimestamp(String userId, String clientKey, String timestamp, String orderBy,
                                            OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getStatsReferenceByTimestamp(userId, clientKey, timestamp);

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
