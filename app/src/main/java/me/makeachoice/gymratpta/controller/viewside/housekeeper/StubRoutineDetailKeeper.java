package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.modelside.firebase.RoutineFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.RoutineNameFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.RoutineNameLoader;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineNameQueryHelper;
import me.makeachoice.gymratpta.controller.modelside.query.RoutineQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.bottomnav.SaveNav;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.RoutineDetailMaid;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineFBItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static me.makeachoice.gymratpta.controller.manager.Boss.EXTRA_ROUTINE_UPDATE;

/**************************************************************************************************/
/*
 * TODO - need to add accessibility values to bottom navigation menu items
 *          todo - add content descriptions to menu items
 *          todo - add d-pad navigation
 * TODO - need to style components
 *          todo - bottom navigation
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineDetailKeeper maintains the main components of the routine detail screen. It is directly
 * responsible for handling toolbar and drawer events (see GymRatBaseKeeper), bottom navigation events
 * (see SaveNav) and the fragment container holding the fragment maintained by RoutineDetailMaid
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Variables from GymRatBaseKeeper:
 *      mBoss - Boss application
 *      mHomeDrawer - drawer navigation component
 *      mSelectedNavItemId - used to determine which menu item is selected in the drawer
 *
 * Methods from MyHouseKeeper
 *      void create(MyActivity,Bundle) - called by onCreate(Bundle) in the activity lifecycle
 *      boolean isTablet() - return device flag if device is tablet or not
 *
 * Methods from GymRatBaseKeeper
 *      void initializeNavigation() - initialize navigation ui
 *      void initializeToolbar() - initialize toolbar component
 *      void initializeDrawer() - initialize drawer navigation component
 *
 * MyHouseKeeper Implements MyActivity.Bridge
 *      void start() - called by onStart() in the activity lifecycle
 *      void resume() - called by onResume() in the activity lifecycle
 *      void pause() - called by onPause() in the activity lifecycle
 *      void stop() - called by onStop() in the activity lifecycle
 *      void destroy() - called by onDestroy() in the activity lifecycle
 *      void backPressed() - called by onBackPressed() in the activity
 *      void saveInstanceState(Bundle) - called before onDestroy(), save state to bundle
 *      void activityResult(...) - result of Activity called by this Activity
 */
/**************************************************************************************************/

public class StubRoutineDetailKeeper extends GymRatBaseKeeper implements MyActivity.Bridge {

/**************************************************************************************************/
/*
 * Class Variables:
 *      String mUserId - user id taken from firebase authentication
 *      boolean mIsNewRoutine - status flag if routine is new routine or old
 *
 *      RoutineDetailItem mRoutineDetailItem - details of the given routine
 *      RoutineNameItem mSaveRoutineName - routine name item being saved
 *      HashMap<String,RoutineNameItem> mRoutineNameMap - hashMap of routine names in database
 *      RoutineDetailMaid mMaid - routine detail maid
 */
/**************************************************************************************************/

    //mUserId - user id taken from firebase authentication
    private String mUserId;

    //mIsNewRoutine - status flag if routine is new routine or old
    private boolean mIsNewRoutine;

    //mRoutineDetailItem - details of the given routine
    private RoutineDetailItem mRoutineDetailItem;

    //mSaveRoutineName - routine name item being saved
    private RoutineNameItem mSaveRoutineName;

    //mRoutineNameMap - hashMap of routine names in database
    private HashMap<String, RoutineNameItem> mRoutineNameMap;

    //mMaid - routine detail maid
    private RoutineDetailMaid mMaid;



/**************************************************************************************************/

/**************************************************************************************************/
/*
 * StubRoutineDetailKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * StubRoutineDetailKeeper - constructor
     */
    public StubRoutineDetailKeeper(int layoutId){

        //get layout id
        mActivityLayoutId = layoutId;

        //get menu for toolbar
        mToolbarMenuId = R.menu.toolbar_menu;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Activity Lifecycle Methods
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
 */
/**************************************************************************************************/
    /*
     * void create(MyActivity, Bundle) - called when onCreate is called in the activity. Sets the
     * activity layout, fragmentManager and other child views of the activity
     *
     * NOTE: Both FragmentManager and FAB are context sensitive and need to be recreated every time
     * onCreate() is called in the Activity
     * @param activity - current activity being shown
     * @param bundle - instant state values
     */
    public void create(MyActivity activity, Bundle bundle){
        super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        //load routine names
        loadRoutineNames();

        //initialize keeper values
        initializeValues();

        //initialize layout
        initializeLayout();

    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //TODO - handle saved instance states from bundle
        //set saved instance state data
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods:
 *      void initializeValues() - initialize values used by keeper
 *      void initializeLayout() - initialize maids and bottom navigation
 *      void initializeMaid() - initialize ExerciseMaid and RoutineMaid
 *      void initializeBottomNavigation - initialize bottom navigation for the exercise screen
 */
/**************************************************************************************************/
    /*
     * void initializeValues() - initialize values used by keeper
     */
    private void initializeValues(){
        //initialize hashMap used for mapping routine names
        mRoutineNameMap = new HashMap<>();

        //get user id from Boss
        mUserId = mBoss.getUserId();

        //get routine detail item from Boss
        mRoutineDetailItem = mBoss.getRoutineDetail();

        //check routine name value
        if(mRoutineDetailItem.routineName.isEmpty()){
            //if empty, set new routine flag as true
            mIsNewRoutine = true;
        }
        else{
            //has routine name, set new routine flag as false
            mIsNewRoutine = false;
        }

        //create intent
        Intent intent = mActivity.getIntent();

        //put return result in intent extra
        intent.putExtra(EXTRA_ROUTINE_UPDATE, false);

        //set result
        mActivity.setResult(RESULT_OK, intent);


    }

    /*
     * void initializeLayout() - initialize maids and bottom navigation
     */
    private void initializeLayout(){

        //initialize maids
        initializeMaid();

        //initialize bottom navigation
        initializeBottomNavigation();
    }


    /*
     * void initializeMaid() - initialize RoutineDetailMaid
     */
    private void initializeMaid(){
        //get maid registry
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        //layout used by routine detail screen
        int layoutId = R.layout.fragment_routine_detail;

        //initialize routine detail maid
        maidRegistry.initializeRoutineDetailMaid(MaidRegistry.MAID_ROUTINE_DETAIL, layoutId,
                mUserId, mRoutineDetailItem);

        //get routine detail maid from registry
        mMaid = (RoutineDetailMaid)maidRegistry.requestMaid(MaidRegistry.MAID_ROUTINE_DETAIL);

        //load fragment maintained by routine detail maid
        loadFragment(mMaid);

    }

    /*
     * void initializeBottomNavigation - initialize bottom navigation used for saving routine details
     */
    private void initializeBottomNavigation(){

        //create bottom navigator
        SaveNav nav = new SaveNav(mActivity);

        //set onSave listener event
        nav.setSaveListener(new SaveNav.OnSaveRoutineListener() {
            @Override
            public void onSaveRoutine() {
                //routine save details event occurred
                onRoutineSaved();
            }

            @Override
            public void onCancelRoutine(){
                //routine cancel event occurred
                onRoutineCanceled();
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void loadFragment(MyMaid) - load appropriate fragment requested by user
 *      void loadRoutines() - load routine data from database
 */
/**************************************************************************************************/

    /*
     * void loadFragment(MyMaid) - load appropriate fragment requested by user
     */
    private void loadFragment(MyMaid maid){
        //get fragment manger
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();

        //get fragment transaction object
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //get fragment managed by maid
        Fragment fragment = maid.getFragment();

        //add fragment to fragment container
        fragmentTransaction.replace(R.id.choiceFragmentContainer, fragment);

        //commit fragment transaction
        fragmentTransaction.commit();
    }

    /*
     * void loadRoutines() - load routine data from database
     */
    private void loadRoutineNames(){
        //start loader to get routine data from database
        RoutineNameLoader.loadRoutineNames(mActivity, mUserId, new RoutineNameLoader.OnRoutineNameLoadListener() {
            @Override
            public void onRoutineNameLoadFinished(Cursor cursor) {
                onRoutineNameDataLoaded(cursor);
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void backPressed() - called when Activity.onBackPressed is called
 *      void onRoutineSaved() - routine save onClick event occurred
 *      void onRoutineCanceled() - routine edit or creation was canceled
 *      void onRoutineNameDataLoaded(Cursor) - routine name data has been loaded
 *      void setReturnResult(boolean) - set return result to calling parent activity
 */
/**************************************************************************************************/
    /*
     * void backPressed() - called when Activity.onBackPressed is called
     */
    @Override
    public void backPressed(){
    }

    /*
     * void onRoutineSaved() - routine save onClick event occurred
     */
    private void onRoutineSaved(){
        //get routine name from maid editText value
        String newName = mMaid.getRoutineName().trim();

        //get routine exercise list from maid
        ArrayList<RoutineItem> exerciseList = mMaid.getRoutineExerciseList();

        //validate routine name and exercise list
        if(validName(newName) && validList(exerciseList)){

            //both name and list are valid, create routine name object
            mSaveRoutineName = new RoutineNameItem();
            mSaveRoutineName.uid = mUserId;
            mSaveRoutineName.routineName = newName;

            //save routine name
            saveRoutineName(mSaveRoutineName);

            //save exercise list
            saveExerciseList(newName, exerciseList);

            //set return result to calling parent activity
            setReturnResult(RESULT_OK);
        }
    }

    /*
     * void onRoutineCanceled() - routine edit or creation was canceled
     */
    private void onRoutineCanceled(){
        //set return result to calling parent activity
        setReturnResult(RESULT_CANCELED);
    }

    /*
     * void onRoutineNameDataLoaded(Cursor) - routine name data has been loaded
     */
    private void onRoutineNameDataLoaded(Cursor cursor){
        //clear routine name hashMap
        mRoutineNameMap.clear();

        //get size of cursor
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move to cursor to index
            cursor.moveToPosition(i);

            //create routine name item
            RoutineNameItem item = new RoutineNameItem(cursor);

            //add item to hashMap
            mRoutineNameMap.put(item.routineName, item);
        }

        //destroy loader
        RoutineNameLoader.destroyLoader(mActivity);
    }

    /*
     * void setReturnResult(boolean) - set return result to calling parent activity
     */
    private void setReturnResult(int result){
        //create intent
        Intent intent = mActivity.getIntent();

        //set result
        mActivity.setResult(result, intent);

        //close activity
        mActivity.finishActivity();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Validation Methods
 *      boolean validName(String) - check if routine name is valid
 *      boolean nameIsEmpty(String) - check to see if the routine name is null or empty
 *      boolean validCharacters(String) - check for invalid characters in string
 *      boolean validList(ArrayList<RoutineItem> - check if list is valid
 */
/**************************************************************************************************/
    /*
     * boolean validName(String) - check if routine name is valid
     */
    private boolean validName(String newRoutineName){
        //create messages to notify user if name is invalid
        String msgEmptyName = mActivity.getString(R.string.msg_routine_name_blank);
        String msgInvalidCharacter = mActivity.getString(R.string.msg_invalid_character_routine_name);
        String msgDuplicateName = mActivity.getString(R.string.msg_routine_name_exists);

        //check if routine name is blank
        if(nameIsEmpty(newRoutineName)){
            //routine name is blank, show toast message
            Toast.makeText(mActivity, msgEmptyName, Toast.LENGTH_LONG).show();
            return false;
        }

        //check if routine name contains invalid characters
        if(!validCharacters(newRoutineName)){
            //routine name contains invalid characters, show toast message
            Toast.makeText(mActivity, msgInvalidCharacter, Toast.LENGTH_LONG).show();
            return false;
        }


        //check if routine being edited or newly created
        if(!mIsNewRoutine){
            //routine being edited, get old routine name
            String oldName = mRoutineDetailItem.routineName;

            //check if routine name has changed
            if(newRoutineName.equals(oldName)){
                //not changed, name is valid
                return true;
            }
            else{
                //routine name has changed, check if duplicate of another routine name
                if(mRoutineNameMap.containsKey(newRoutineName)){
                    //new name is duplicate of another name, show toast message
                    Toast.makeText(mActivity, msgDuplicateName, Toast.LENGTH_LONG).show();
                    return false;
                }
                else{
                    //routine name not duplicate, name is valid
                    return true;
                }
            }
        }
        else{
            //routine is newly created, check for duplicate
            if(mRoutineNameMap.containsKey(newRoutineName)){
                //new name is duplicate of another name, show toast message
                Toast.makeText(mActivity, msgDuplicateName, Toast.LENGTH_LONG).show();
                return false;
            }
            else{
                //routine name not duplicate, name is valid
                return true;
            }
        }
    }

    /*
     * boolean nameIsEmpty(String) - check to see if the routine name is null or empty
     */
    private boolean nameIsEmpty(String routineName){
        //check if routine name is null or empty
        if(routineName == null || routineName.isEmpty()){
            //is null or empty, return true
            return true;
        }

        //not null or empty, return false
        return false;
    }

    /*
     * boolean validCharacters(String) - check for invalid characters in string
     */
    private boolean validCharacters(String routineName){
        //get number of characters in string
        int count = routineName.length();

        //loop through characters
        for (int i = 0; i < count; i++) {
            //get character at index
            char currentChar = routineName.charAt(i);

            //check if character is valid
            if (!Character.isLetterOrDigit(currentChar) && !Character.isWhitespace(currentChar) &&
                    currentChar != '_' && currentChar != '-') {
                //invalid character, return false
                return false;
            }
        }

        return true;
    }

    /*
     * boolean validList(ArrayList<RoutineItem> - check if list is valid
     */
    private boolean validList(ArrayList<RoutineItem> routineList){
        if(routineList != null && !routineList.isEmpty()){
            return true;
        }

        return false;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void saveRoutineName(RoutineNameItem) - save routine name to firebase and local database
 *      void saveRoutineNameToFirebase(RoutineNameItem) - save routine name to firebase
 *      void saveRoutineNameToDatabase(RoutineNameItem) - save routine name to local database
 *      void saveListToFirebase(...) - save routine exercise list to firebase
 *      void saveListToFirebase(...) - save routine exercise list to firebase
 *      void saveListToDatabase(...) - save routine exercise list to database
 */
/**************************************************************************************************/
    /*
     * void saveRoutineName(RoutineNameItem) - save routine name to firebase and local database
     */
    private void saveRoutineName(RoutineNameItem nameItem){
        //save to firebase
        saveRoutineNameToFirebase(nameItem);

        //save to local database
        saveRoutineNameToDatabase(nameItem);

    }

    /*
     * void saveRoutineNameToFirebase(RoutineNameItem) - save routine name to firebase
     */
    private void saveRoutineNameToFirebase(RoutineNameItem nameItem){
        //get routine name firebase helper instance for routine name and routine
        RoutineNameFirebaseHelper routineNameFB = RoutineNameFirebaseHelper.getInstance();
        RoutineFirebaseHelper routineFB = RoutineFirebaseHelper.getInstance();

        //get routine name from routine detail item
        String oldName = mRoutineDetailItem.routineName;

        //check if routine detail is NOT new and routine name is NOT the same
        if(!mIsNewRoutine && !oldName.equals(nameItem.routineName)){
            //routine name has been edited, remove old routine name from firebase
            routineNameFB.deleteRoutineName(mUserId, oldName);

            //remove routine from routine firebase
            routineFB.deleteRoutine(mUserId, oldName);
        }

        //save routine name
        routineNameFB.addRoutineName(mUserId, nameItem.routineName);
    }

    /*
     * void saveRoutineNameToDatabase(RoutineNameItem) - save routine name to local database
     */
    private void saveRoutineNameToDatabase(RoutineNameItem nameItem){
        //get uri value for routine name table
        Uri uriValue = Contractor.RoutineNameEntry.CONTENT_URI;

        //get name from routine detail item
        String oldName = mRoutineDetailItem.routineName;

        //check if routine detail is NOT new
        if(!mIsNewRoutine){
            //routine is being edited, check if routine name is NOT the same
            if(!oldName.equals(nameItem.routineName)){
                //routine name has been edited, remove old routine name from database
                int rowDeleted = mActivity.getContentResolver().delete(uriValue,
                        RoutineNameQueryHelper.routineNameSelection, new String[]{mUserId, oldName});

                //add new routine name to database
                mActivity.getContentResolver().insert(uriValue, nameItem.getContentValues());
            }

        }
        else{
            //routine is new, add routine name to database
            mActivity.getContentResolver().insert(uriValue, nameItem.getContentValues());
        }

    }

    /*
     * void saveExerciseList(...) - save routine exercise list to firebase and database
     */
    private void saveExerciseList(String routineName, ArrayList<RoutineItem> exerciseList){
        //save exercise list to firebase
        saveListToFirebase(routineName, exerciseList);

        //save exercise list to database
        saveListToDatabase(routineName, exerciseList);
    }

    /*
     * void saveListToFirebase(...) - save routine exercise list to firebase
     */
    private void saveListToFirebase(String routineName, ArrayList<RoutineItem> exerciseList){
        //get exercise list count of old list
        int oldCount = mRoutineDetailItem.routineExercises.size();

        //get exercise list count of new list
        int newCount = exerciseList.size();

        //create routine count buffer
        int routineCount;
        if(oldCount > newCount){
            //set routineCount to oldCount if greater than newCount
            routineCount = oldCount;
        }
        else{
            //set routineCount to newCount if greater or equal to oldCount
            routineCount = newCount;
        }

        //get firebase instance
        RoutineFirebaseHelper routineFB = RoutineFirebaseHelper.getInstance();

        //loop through list
        for(int i = 0; i < routineCount; i++){

            //check if index is less than newCount
            if(i < newCount){
                //get routine exercise item from new list and set routine name and order number
                RoutineItem detailItem = exerciseList.get(i);
                detailItem.routineName = routineName;
                detailItem.orderNumber = i;

                //create firebase item
                RoutineFBItem fbItem = new RoutineFBItem();
                fbItem.category = detailItem.category;
                fbItem.exercise = detailItem.exercise;
                fbItem.numOfSets = detailItem.numOfSets;

                //add item to firebase
                routineFB.addRoutine(mUserId, detailItem, fbItem);
            }
            else{
                //if index is greater than newCount, delete routine exercise from firebase
                routineFB.deleteRoutineExercise(mUserId, routineName, String.valueOf(i));
            }

        }

    }

    /*
     * void saveListToDatabase(...) - save routine exercise list to database
     */
    private void saveListToDatabase(String routineName, ArrayList<RoutineItem> exerciseList){
        //get uri for routine table
        Uri uriValue = Contractor.RoutineEntry.CONTENT_URI;

        //check if routine is a new routine
        if(!mIsNewRoutine) {
            //get name from routine detail item
            String oldName = mRoutineDetailItem.routineName;

            //remove old exercise routines from routine
            int rowDeleted = mActivity.getContentResolver().delete(uriValue,
                    RoutineQueryHelper.routineNameSelection, new String[]{mUserId, oldName});
        }

        //get number of exercises in routine
        int routineCount = exerciseList.size();

        //loop through list
        for(int i = 0; i < routineCount; i++){
            //get routine item
            RoutineItem item = exerciseList.get(i);
            item.uid = mUserId;
            item.routineName = routineName;
            item.orderNumber = i;

            //add routine to sqlite database
            Uri uri = mActivity.getContentResolver().insert(uriValue, item.getContentValues());
        }

    }

/**************************************************************************************************/

}
