package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.UserFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.UserLoader;
import me.makeachoice.gymratpta.model.contract.user.UserContract;
import me.makeachoice.gymratpta.model.item.UserItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryFBItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseFBItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.model.stubData.CategoryStubData;
import me.makeachoice.gymratpta.model.stubData.ExerciseStubData;
import me.makeachoice.gymratpta.model.stubData.RoutineStubData;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CATEGORY;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_USER;

/**************************************************************************************************/
/*
 *  UserButler assists in loading, saving and deleting client notes to database and firebase
 */
/**************************************************************************************************/

public class UserButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mLoaderId - loader id number
    private int mLoaderId;

    private MyActivity mActivity;
    private String mUserId;

    //UserLoader - loader class used to load user data
    private UserLoader mUserLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        void onLoaded(UserItem userItem);
    }

    //mSaveListener - used to listen for save events
    private OnSavedListener mSaveListener;
    public interface OnSavedListener{
        void onSaved();
    }

    //mDeleteListener - used to listen for delete events
    private OnDeletedListener mDeleteListener;
    public interface OnDeletedListener{
        void onDeleted();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public UserButler(MyActivity activity, String userId){
        mActivity = activity;
        mUserId = userId;

        mUserLoader = new UserLoader(mActivity, mUserId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load User
 */
/**************************************************************************************************/
    /*
     * void loadUser() - load notes data for given day from database
     */
    public void loadUser(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get notes data from database
        mUserLoader.loadUser(mLoaderId, new UserLoader.OnUserLoadListener() {
            @Override
            public void onUserLoadFinished(Cursor cursor) {
                onUserLoaded(cursor);
            }
        });
    }

    /*
     * void onUserLoaded(Cursor) - data from database has been loaded
     */
    private void onUserLoaded(Cursor cursor){

        //get number of scheduled appointments loaded
        int count = cursor.getCount();

        UserItem item = null;
        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(0);

            //create item from cursor data
            item = new UserItem(cursor);
            i = count;
        }

        //destroy loader
        mUserLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(item);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save User
 */
/**************************************************************************************************/

    public void saveUser(UserItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

        //save to local database
        saveToDatabase(saveItem);

    }

    /*
     * void saveToUserFB(UserItem) - save notes to notes firebase
     */
    private void saveToFirebase(UserItem saveItem){
        //get firebase helper instance
        UserFirebaseHelper fbHelper = UserFirebaseHelper.getInstance(saveItem.uid);

        //save notes item to firebase
        fbHelper.addUser(saveItem.getFBItem());

    }

    /*
     * void saveToDatabase(...) - save notes to local database
     */
    private void saveToDatabase(UserItem saveItem){
        //get uri value for routine name table
        Uri uriValue = UserContract.CONTENT_URI;

        //appointment is new, add notes to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete User
 */
/**************************************************************************************************/

    private UserItem mCheckInUser;
    public void checkInUser(UserItem userItem, OnSavedListener listener){
        mCheckInUser = userItem;

        mSaveListener = listener;

        //get firebase helper instance
        UserFirebaseHelper fbHelper = UserFirebaseHelper.getInstance(userItem.uid);

        //check if user is in Firebase
        fbHelper.requestUserData(userItem.uid, new UserFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(UserItem user) {
                if(user == null){
                    //not in Firebase, save use to Firebase
                    saveToFirebase(mCheckInUser);
                }
                checkInDatabase(mCheckInUser);
            }

            @Override
            public void onCancelled() {
                //does nothing
            }
        });
    }

    private void checkInDatabase(UserItem userItem){
        loadUser(LOADER_USER, new OnLoadedListener() {
            @Override
            public void onLoaded(UserItem userItem){
                if(userItem == null){
                    initializeCategoryData();
                }
                else{
                    mSaveListener.onSaved();
                }
            }
        });
    }


/**************************************************************************************************/

    public void initializeCategoryData(){
        mFBCategories = CategoryStubData.createDefaultCategories(mActivity);

        mCategoryButler = new CategoryButler(mActivity, mUserId);

        mCategoryCounter = 0;
        saveCategories(mCategoryCounter);
    }

    private int mCategoryCounter;
    private ArrayList<CategoryFBItem> mFBCategories;
    private CategoryButler mCategoryButler;

    private void saveCategories(int counter){
        if(counter < mFBCategories.size()){

            CategoryFBItem fbItem = mFBCategories.get(counter);

            CategoryItem categoryItem = new CategoryItem(fbItem);
            categoryItem.uid = mUserId;

            mCategoryButler.saveCategory(categoryItem, new CategoryButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    mCategoryCounter++;
                    saveCategories(mCategoryCounter);
                }
            });

        }
        else{
            initializeExerciseData();
        }
    }

    private ArrayList<CategoryItem> mCategoryList;
    private void initializeExerciseData(){
        mExerciseButler = new ExerciseButler(mActivity, mUserId);
        ExerciseStubData.createDefaultExercises(mActivity);

        mCategoryList = new ArrayList<>();
        mCategoryButler.loadCategories(LOADER_CATEGORY, new CategoryButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<CategoryItem> categoryList) {
                mCategoryList.addAll(categoryList);
                mCategoryCounter = 0;
                initializeCategoryExerciseData(mCategoryCounter);
            }
        });

    }

    private ArrayList<ExerciseFBItem> mFBExercises;
    private CategoryItem mCategoryItem;
    private int mExerciseCounter;
    private ExerciseButler mExerciseButler;
    private void initializeCategoryExerciseData(int categoryCounter){
        if(categoryCounter < mCategoryList.size()){
            mCategoryItem = mCategoryList.get(categoryCounter);
            mFBExercises = ExerciseStubData.getExercises(categoryCounter);
            mExerciseCounter = 0;
            saveExercise(mExerciseCounter, mCategoryItem);
        }
        else{
            initializeRoutineExerciseData();
        }
    }

    private void saveExercise(int counter, CategoryItem categoryItem){
        if(counter < mFBExercises.size()){
            ExerciseFBItem fbItem = mFBExercises.get(counter);

            ExerciseItem exerciseItem = new ExerciseItem(fbItem);
            exerciseItem.uid = mUserId;
            exerciseItem.categoryKey = categoryItem.fkey;

            mExerciseButler.saveExercise(exerciseItem, new ExerciseButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    mExerciseCounter++;
                    saveExercise(mExerciseCounter, mCategoryItem);
                }
            });
        }
        else{
            mCategoryCounter++;
            initializeCategoryExerciseData(mCategoryCounter);
        }
    }

    private RoutineNameButler mRoutineNameButler;
    private RoutineButler mRoutineButler;
    private void initializeRoutineExerciseData(){
        ArrayList<String> routineNameList = RoutineStubData.createDefaultRoutineNames();

        mRoutineNameButler = new RoutineNameButler(mActivity, mUserId);
        int count = routineNameList.size();
        for(int i = 0; i < count; i++){
            RoutineNameItem item = new RoutineNameItem();
            item.routineName = routineNameList.get(i);
            item.uid = mUserId;

            mRoutineNameButler.saveRoutineName(item, null);
        }

        //finished exercise category loading
        saveUser(mCheckInUser, mSaveListener);
        initializeRoutine();
    }

    private void initializeRoutine(){
        mRoutineButler = new RoutineButler(mActivity, mUserId);
        ArrayList<RoutineItem> routines = RoutineStubData.createDefaultRoutines(mActivity);

        int count = routines.size();
        for(int i = 0; i < count; i++){
            RoutineItem item = routines.get(i);
            item.uid = mUserId;
            mRoutineButler.saveRoutine(item, null);
        }


    }
}
