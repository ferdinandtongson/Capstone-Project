package me.makeachoice.gymratpta.controller.modelside.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.exercise.CategoryFBItem;

/**************************************************************************************************/
/*
 * CategoryFirebaseHelper helps in adding and requesting category data to and from firebase
 */
/**************************************************************************************************/

public class CategoryFirebaseHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    public final static String CHILD_CATEGORY_NAME = "categoryName";

    //PARENT - parent director
    private final static String PARENT = "category";

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

    private static CategoryFirebaseHelper instance = null;

    public static CategoryFirebaseHelper getInstance() {
        if(instance == null) {
            instance = new CategoryFirebaseHelper();
        }
        return instance;
    }

    private CategoryFirebaseHelper(){

        mFireDB = FirebaseDatabase.getInstance();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getter Methods
 */
/**************************************************************************************************/

    public DatabaseReference getCategoryReference(String userId){
        return mFireDB.getReference().child(PARENT).child(userId);
    }

    private DatabaseReference getCategoryDetailReference(String userId, String clientKey){
        return getCategoryReference(userId).child(clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Add Data Methods
 */
/**************************************************************************************************/

    public void addCategoryData(String userId, ArrayList<CategoryFBItem> categories, ValueEventListener listener){
        int count = categories.size();
        for(int i = 0; i < count; i++){
            addCategory(userId, categories.get(i), listener);
        }

    }

    public void addCategory(String userId, CategoryFBItem item, ValueEventListener listener){
        DatabaseReference ref = getCategoryReference(userId);
        ref.child(item.categoryName).setValue(item);
        ref.addListenerForSingleValueEvent(listener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Set Data Methods
 */
/**************************************************************************************************/

    public void setCategoryName(String userId, String clientKey, String name){
        DatabaseReference refClient = getCategoryDetailReference(userId, clientKey);

        refClient.child(CHILD_CATEGORY_NAME).setValue(name);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Data Methods
 */
/**************************************************************************************************/

    public void requestCategoryData(String userId, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getCategoryReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.addListenerForSingleValueEvent(mEventListener);
    }

    public void requestCategoryData(String userId, String orderBy, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getCategoryReference(userId);

        mOnDataLoadedListener = listener;

        //add event listener to reference
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(mEventListener);
    }

    public void requestCategoryDataByName(String userId, String name, OnDataLoadedListener listener){
        //get reference
        DatabaseReference ref = getCategoryReference(userId);

        mOnDataLoadedListener = listener;

        ref.orderByChild(CHILD_CATEGORY_NAME).equalTo(name).addListenerForSingleValueEvent(mEventListener);

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
