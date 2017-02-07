package me.makeachoice.gymratpta.controller.manager;

/**************************************************************************************************/
/*
 * Boss is the "boss", main controller of the app and interfaces with the View and Model. Boss
 * tries to adhere to the MVP (Model-View-Presenter) model so Model-View communication is
 * prevented; in MVC (Model-View-Controller) model, the Model and View can communicate
 *
 * Inherited Class Variables:
 *      Context mActivityContext - current Activity Context on display
 *      boolean mIsTablet - is a tablet device status flag
 *      MyHouseKeeperRegistry mKeeperRegistry - MyHouseKeeper registry
 *
 * Inherited Abstract Methods:
 *      void initHouseKeepers() - initializes HouseKeeper classes; they handle Activity UI
 *      void initStaff() - initialize Staff classes, maintains ArrayList and HashMap buffers
 *      void initValets() - initialize Valet classes, handles request to DB
 *      void initButlers() - initialize Butler classes, handles calls to APIs
 *      void onFinish() - application is about to close, clear all local buffer from memory
 *
 * Inherited Getters:
 *      Context getActivityContext() - get current Activity Context
 *      Executor getExecutor() - get AsyncTask thread pool executor
 *      boolean getIsTablet() - get if device is a tablet status flag
 *
 * Inherited Setters Methods:
 *      void setActivityContext(Context) - stores the Activity context
 *      void setIsTablet(boolean) - set tablet status flag
 *
 * Inherited HouseKeeper Registry Methods:
 *      MyHouseKeeper requestHouseKeeper(int) - get HouseKeeper requested
 *      void registerHouseKeeper(int,MyHouseKeeper) - put HouseKeeper into registry
 */

/**************************************************************************************************/
/*
 *  TODO - if first activity, need to modify AndroidManifest in <application ....>
 *      <application....
 *          android:name=".controller.Boss"
 *          android:theme="@style/ChoiceTheme.Activity">
 *  TODO - if first activity, need to modify app build.gradle in dependencies
 *      dependencies{....
 *      compile(name: 'ChoiceLibrary', ext: 'aar') ....}
 *  TODO - if first activity, need to modify <YourAppName> build.gradle in allprojects
 *      allprojects{
 *          repositories {
 *              jcenter()
 *              flatDir {
 *                 dirs 'libs'
 *             }
 *         }
 *      }
 */
/**************************************************************************************************/

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.makeachoice.gymratpta.BuildConfig;
import me.makeachoice.gymratpta.controller.modelside.firebase.CategoryFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.ExerciseFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.firebase.UserFirebaseHelper;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.user.UserColumns;
import me.makeachoice.gymratpta.model.db.DBHelper;
import me.makeachoice.gymratpta.model.item.UserItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryFBItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseFBItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.stubData.CategoryStubData;
import me.makeachoice.gymratpta.model.stubData.ExerciseStubData;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;

import static android.R.attr.category;

/**************************************************************************************************/

public class Boss extends MyBoss {

/**************************************************************************************************/
/*
 *  Public Variables:
 *      mKeeperRegistry - house keeper registry
 */
/**************************************************************************************************/

    //mKeeperRegistry - house keeper registry
    private HouseKeeperRegistry mKeeperRegistry;

    //mFirebaseAuth - firebase authentication instance
    private FirebaseAuth mFirebaseAuth;

    //mAuthStateListener - firebase authentication listener
    private FirebaseAuth.AuthStateListener mAuthStateListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization Methods:
 *      void onCreate() - called when application is first created
 *      void initHouseKeepers() - initializes HouseKeeper classes; they handle Activity UI
 *      void initStaff() - initialize Staff classes, maintains ArrayList and HashMap buffers
 *      void initValets() - initialize Valet classes, handles request to DB
 *      void initButlers() - initialize Butler classes, handles calls to APIs
 */
/**************************************************************************************************/
    /*
     * void onCreate() - called when application is first created. Initialize support classes
     */
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Choice", "Boss.onCreate");
        mKeeperRegistry = HouseKeeperRegistry.getInstance();
        mKeeperRegistry.initializeHouseKeepers();

        dropAllTables();

        initializeFirebaseAuth();

        //initialize database
        initDatabase();

        //initialize Staff classes
        initStaff();

        //initialize Valet classes
        initValets();

        //initialize Butler classes
        initButlers();
    }

    public MyHouseKeeper requestHouseKeeper(String keeperKey){
        return mKeeperRegistry.requestHouseKeeper(keeperKey);
    }

    private void dropAllTables(){
        Log.d("Choice", "Boss.dropAllTables");
        DBHelper dbHelper = new DBHelper(this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.dropTable(db, Contractor.UserEntry.TABLE_NAME);
        dbHelper.dropTable(db, Contractor.ClientEntry.TABLE_NAME);
        dbHelper.dropTable(db, Contractor.CategoryEntry.TABLE_NAME);
        dbHelper.dropTable(db, Contractor.ExerciseEntry.TABLE_NAME);

        db.close();

        deleteDatabase(DBHelper.DATABASE_NAME);
    }

    //private DBHelper mDBHelper;
    protected void initDatabase(){
    }

    /*
     * void initStaff() - initializes Staff classes; they maintain ArrayList and HashMap buffers
     */
    protected void initStaff(){
        //initialize Portfolio staff
        //mPortfolioStaff = new PortfolioStaff(this);

        //set portfolioMap; this is for the ability to have more than one portfolio screen
        //mPortfolioStaff.setPortfolioMap(mPortfolioValet.getPortfolioMap());

    }

    /*
     * void initValets() - initialize Valet classes; they handle request to DB
     */
    protected void initValets(){

        //initialize portfolio valet
        //mPortfolioValet = new PortfolioValet(this);

        //initialize settings valet
        //mSettingsValet = new SettingsValet(this);

        //get settings item
        //mSettingsItem = mSettingsValet.getSettings();
    }

    /*
     * void initButlers() - initializes Butler classes; they handle making and processing API calls
     */
    protected void initButlers(){
        //initialize Yahoo butler
        //mYahooButler = new YahooButler(this);

        //initialize News butler
        //mNewsButler = new NewsButler(this);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Methods:
 *      void onFinish() - application is about to close, clear all local buffer from memory
 */
/**************************************************************************************************/
    /*
     * void onFinish() - application is about to close, clear all local buffer from memory
     */
    public void onFinish(){
        //clean up HouseKeeper registry
        mKeeperRegistry.onFinish();
    }

/**************************************************************************************************/

    /*
     * void initializeFirebaseAuth() - initialize Firebase authentication
     */
    private void initializeFirebaseAuth(){
        Log.d("Choice", "Boss.initializeFirebaseAuth");
        mAuthCounter = 0;
        //get authentication instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        //create firebase authentication listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(mAuthCounter == 0){
                    mAuthCounter++;

                    if(user == null){
                        //debug count used to prevent multiple userSignIn calls, known bug
                        userSignIn();
                    }
                    Log.d("Choice", "     user: " + user.getDisplayName());
                    saveUser(user);

                }
            }
        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    private int mAuthCounter;
    //REQUEST_CODE_SIGN_IN - request code used by firebaseUI
    private static final int REQUEST_CODE_SIGN_IN = 200;


    private void userSignIn(){
        mActivity.startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .build(), REQUEST_CODE_SIGN_IN);
    }



    private UserItem mCurrentUser;
    private void saveUser(FirebaseUser user){
        Log.d("Choice", "Boss.saveUser");
        mCurrentUser = saveUserItem(user);

        Uri uriValue = Contractor.UserEntry.buildUserByUID(user.getUid());
        Cursor cursor = getContentResolver().query(uriValue, UserColumns.PROJECTION, null, null, null);

        if(cursor == null || cursor.getCount() == 0){
            Log.d("Choice", "     putUserInDatabase");
            //user not in database, add user to database
            putUserInDatabase(user);
        }
        cursor.close();

        checkUserInFirebase(user);
    }

    private void checkUserInFirebase(FirebaseUser user){
        Log.d("Choice", "Boss.checkUserInFirebase: ");
        final UserFirebaseHelper userFB = UserFirebaseHelper.getInstance(user.getUid());

        userFB.requestUserData(user.getUid(), new UserFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(UserItem user) {
                if(user == null){
                    Log.d("Choice", "     add user to Firebase");
                    userFB.addUser(mCurrentUser);
                }
                else{
                    Log.d("Choice", "    in Firebase:" + user.userName);
                }
                initializeNewUser();
            }

            @Override
            public void onCancelled() {
                Log.d("Choice", "     user firebase data request canceled");
            }
        });
    }

    private UserItem saveUserItem(FirebaseUser user){
        UserItem item = new UserItem();
        item.uid = user.getUid();
        item.userName = user.getDisplayName();
        item.email = user.getEmail();
        item.status = "Free";

        return item;
    }

    public void putUserInDatabase(FirebaseUser user){
        Log.d("Choice", "Boss.putUserInDatabase");
        Uri uriValue = Contractor.UserEntry.CONTENT_URI;

        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(Contractor.UserEntry.COLUMN_UID, user.getUid());
        values.put(Contractor.UserEntry.COLUMN_USER_NAME, user.getDisplayName());
        values.put(Contractor.UserEntry.COLUMN_USER_EMAIL, user.getEmail());
        values.put(Contractor.UserEntry.COLUMN_USER_PHOTO, user.getPhotoUrl().toString());
        values.put(Contractor.UserEntry.COLUMN_USER_STATUS, "Free");

        Uri uri = getContentResolver().insert(uriValue, values);
        Log.d("Choice", "     uri: " + uri.toString());

    }

    public String getUserId(){
        return mCurrentUser.uid;
    }

    private ClientItem mCurrentClient;
    public void setClient(ClientItem item){
        mCurrentClient = item;
    }

    public ClientItem getClient(){ return mCurrentClient; }
    
    
    
    
    
    
    
    
    
    private void initializeNewUser(){
        Log.d("Choice", "Boss.initializeNewUser");
        initializeCategoryData();
    }

    private void initializeCategoryData(){
        Log.d("Choice", "Boss.initializeCategoryData");
        ExerciseStubData.createDefaultExercises(this);
        ArrayList<CategoryFBItem> categories = CategoryStubData.createDefaultCategories(this);

        CategoryFirebaseHelper categoryFB = CategoryFirebaseHelper.getInstance();

        int count = categories.size();
        for(int i = 0; i < count; i++){
            CategoryFBItem item = categories.get(i);

            categoryFB.addCategory(mCurrentUser.uid, item);
        }

        loadInitCategoryData();
    }

    private HashMap<String,String> mDoublesMap;
    private void loadInitCategoryData(){
        Log.d("Choice", "Boss.loadInitCategoryData");
        //initialize category list array
        mDoublesMap = new HashMap();

        //get client firebase instance
        final CategoryFirebaseHelper categoryFB = CategoryFirebaseHelper.getInstance();

        //get orderBy string value
        String orderBy = CategoryFirebaseHelper.CHILD_CATEGORY_NAME;

        //request client data ordered by client name
        categoryFB.requestCategoryData(mCurrentUser.uid, orderBy, new CategoryFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(DataSnapshot dataSnapshot) {

                processCategoryFirebaseData(dataSnapshot);
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    private ArrayList<CategoryItem> mCategories;
    private void processCategoryFirebaseData(DataSnapshot dataSnapshot){
        mCategories = new ArrayList();
        //loop through client data
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            //get the data from snapshot
            CategoryFBItem fbItem = postSnapshot.getValue(CategoryFBItem.class);

            //create client item, used for local database
            CategoryItem category = new CategoryItem(fbItem);
            category.uid = mCurrentUser.uid;
            category.fkey = postSnapshot.getKey();

            //add item to client list
            updateCategoryDatabase(category);
        }

        initializeExerciseData();
    }

    private void updateCategoryDatabase(CategoryItem item){
        Log.d("Choice", "Boss.updateDatabase: " + item.categoryName);
        //get uri value for client
        Uri uriValue = Contractor.CategoryEntry.CONTENT_URI;
        Log.d("Choice", "     uri: " + uriValue);

        if(!mDoublesMap.containsKey(item.categoryName)){
            mDoublesMap.put(item.categoryName, item.categoryName);
            mCategories.add(item);
            //add category to sqlite database
            getContentResolver().insert(uriValue, item.getContentValues());
        }

    }

    private void initializeExerciseData(){
        Log.d("Choice", "Boss.initializeCategoryData");
        ExerciseStubData.createDefaultExercises(this);

        ExerciseFirebaseHelper exerciseFB = ExerciseFirebaseHelper.getInstance();

        int count = mCategories.size();
        for(int i = 0; i < count; i++){
            CategoryItem item = mCategories.get(i);
            
            ArrayList<ExerciseFBItem> exercises = ExerciseStubData.getExercises(i);
            
            exerciseFB.addExerciseDataToCategory(mCurrentUser.uid, item.fkey, exercises);
        }

        mExerciseCounter = 0;
        loadInitExerciseData(mCategories.get(mExerciseCounter).fkey);
    }
    
    private int mExerciseCounter;
    private void loadInitExerciseData(String categoryKey){
        Log.d("Choice", "Boss.loadInitCategoryData");
        //initialize category list array
        mDoublesMap.clear();

        //get client firebase instance
        final ExerciseFirebaseHelper exerciseFB = ExerciseFirebaseHelper.getInstance();

        //get orderBy string value
        String orderBy = ExerciseFirebaseHelper.CHILD_EXERCISE_NAME;

        //request client data ordered by client name
        exerciseFB.requestExerciseData(mCurrentUser.uid, categoryKey, orderBy, 
                new ExerciseFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(DataSnapshot dataSnapshot) {

                processExerciseFirebaseData(dataSnapshot);
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    private void processExerciseFirebaseData(DataSnapshot dataSnapshot){
        CategoryItem categoryItem = mCategories.get(mExerciseCounter);
        
        //loop through client data
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            //get the data from snapshot
            ExerciseFBItem fbItem = postSnapshot.getValue(ExerciseFBItem.class);

            //create client item, used for local database
            ExerciseItem exercise = new ExerciseItem(fbItem);
            exercise.uid = mCurrentUser.uid;
            exercise.categoryKey = categoryItem.fkey;
            exercise.fkey = postSnapshot.getKey();

            //add item to client list
            updateExerciseDatabase(exercise);
        }

        mExerciseCounter++;
        
        if(mExerciseCounter < mCategories.size()){
            loadInitExerciseData(mCategories.get(mExerciseCounter).fkey);

        }
    }

    private void updateExerciseDatabase(ExerciseItem item){
        Log.d("Choice", "Boss.updateDatabase: " + item.exerciseName);
        //get uri value for client
        Uri uriValue = Contractor.ExerciseEntry.CONTENT_URI;
        Log.d("Choice", "     uri: " + uriValue);

        if(!mDoublesMap.containsKey(item.exerciseName)){
            mDoublesMap.put(item.exerciseName, item.exerciseName);
            //add category to sqlite database
            getContentResolver().insert(uriValue, item.getContentValues());
        }

    }

}
