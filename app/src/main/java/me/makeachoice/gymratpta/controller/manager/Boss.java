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

import android.content.Intent;
import android.content.SharedPreferences;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import me.makeachoice.gymratpta.BuildConfig;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.RoutineNameButler;
import me.makeachoice.gymratpta.controller.modelside.butler.UserButler;
import me.makeachoice.gymratpta.model.item.UserItem;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.widget.AppointmentWidgetProvider;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.R.string.routines;

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


    public static int LOADER_CATEGORY = 100;
    public static int LOADER_STATS = 200;
    public static int LOADER_NOTES = 300;
    public static int LOADER_SCHEDULE = 400;
    public static int LOADER_CLIENT = 500;
    public static int LOADER_EXERCISE_BASE = 600;
    public static int LOADER_ROUTINE = 700;
    public static int LOADER_ROUTINE_NAME = 800;
    public static int LOADER_CLIENT_ROUTINE = 900;
    public static int LOADER_CLIENT_EXERCISE = 1000;
    public static int LOADER_USER = 1100;

    public static String PREF_CLIENT_STATUS = "clientStatus"; //status of client (Active, Retired)
    public static String PREF_SESSION_STATUS = "sessionStatus"; //status of session (Pending, Canceled, Rescheduled, Complete)
    public static String PREF_DAY_MAX = "dayMax"; //maximum number of appointment days (30, 60, 90)
    public static String PREF_SET_MAX = "setMax"; //maximum number of sets in an exercise (10)
    public static String PREF_TIME_BUFFER = "timeBuffer"; //buffer time between appointments
    public static String PREF_DELETE_WARNING = "deleteWarning"; //show (or not) delete dialog warning
    public static String PREF_DELETE_WARNING_APPOINTMENT = "deleteWarningAppointment"; //show (or not) delete dialog warning for appointments

    public static String CLIENT_ACTIVE = "Active";
    public static String CLIENT_RETIRED = "Retired";
    public static String CLIENT_ALL = "All";

    public static String SESSION_CURRENT = "Current";
    public static String SESSION_ALL = "All";

    public static String EXTRA_ROUTINE_UPDATE = "update";

    public static int REQUEST_CODE_ROUTINE_DETAIL = 100;
    public static final int REQUEST_CODE_SIGN_IN = 200;

    public static String DIA_SCHEDULE = "diaScheduleDialog";
    public static String DIA_WARNING_DELETE = "diaWarningDelete";
    public static String DIA_CONTACTS = "diaContacts";
    public static String DIA_GOALS = "diaGoals";
    public static String DIA_EXERCISE_RECORD = "diaExerciseRecord";
    public static String DIA_EXERCISE = "diaExercise";
    public static String DIA_STATS_RECORD = "diaStatsRecord";
    public static String DIA_NOTES_RECORD = "diaNotesRecord";

    private UserButler mUserButler;

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
    private boolean mInitializeUser;
    /*
     * void onCreate() - called when application is first created. Initialize support classes
     */
    @Override
    public void onCreate(){
        super.onCreate();
        mRoutineNames = new ArrayList<>();

        mKeeperRegistry = HouseKeeperRegistry.getInstance();
        mKeeperRegistry.initializeHouseKeepers();

        //get authentication instance
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //initialize database
        initDatabase();

        //initialize Staff classes
        //initStaff();

        //initialize Valet classes
        initValets();

        //initialize Butler classes
        initButlers();
    }

    public MyHouseKeeper requestHouseKeeper(String keeperKey){
        return mKeeperRegistry.requestHouseKeeper(keeperKey);
    }

    //private DBHelper mDBHelper;
    protected void initDatabase(){
        //Log.d("Choice", "     initDatabase");
    }

    /*
     * void initStaff() - initializes Staff classes; they maintain ArrayList and HashMap buffers
     */
    protected void initStaff(){
        //Log.d("Choice", "     initStaff");

    }

    /*
     * void initValets() - initialize Valet classes; they handle request to DB
     */
    protected void initValets(){
        //Log.d("Choice", "     initValet");


    }

    /*
     * void initButlers() - initializes Butler classes; they handle making and processing API calls
     */
    protected void initButlers(){
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

/**************************************************************************************************/
/*
 *  User Firebase sign in and sign out
 */
/**************************************************************************************************/
    /*
     * void userSignIn() - user is asked to sign in with the list of options
     *      new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
     *      new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
     */
    public void userSignIn(){

        //start login process
        mActivity.startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setTheme(R.style.GymRatTheme)
                .build(), REQUEST_CODE_SIGN_IN);
    }

    /*
     * void userSignOut() - user signs out
     */
    public void signOutFirebaseAuth(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signOut();
        mUserItem = null;
        userSignIn();
    }

    /*
     * void checkInUser(...) - check user has local and network database allocated
     */
    public void checkInUser(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = mFirebaseAuth.getCurrentUser();

        setUser(fbUser);
        mUserButler = new UserButler((MyActivity)mActivity, mUserItem.uid);

        mUserButler.checkInUser(mUserItem, new UserButler.OnSavedListener() {
            @Override
            public void onSaved() {
                initPreferences();
            }
        });
    }

    private void initPreferences(){
        //public static final String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences shared = getSharedPreferences(mUserItem.uid, MODE_PRIVATE);

        //check if user already has created a shared preference
        if(!shared.contains(mUserItem.uid)){
            SharedPreferences.Editor editor = getSharedPreferences(mUserItem.uid, MODE_PRIVATE).edit();

            editor.putString(PREF_CLIENT_STATUS, CLIENT_ALL); //client status - ALL, ACTIVE, RETIRED
            editor.putString(PREF_SESSION_STATUS, SESSION_ALL); //session status - ALL, CURRENT
            editor.putInt(PREF_DAY_MAX, 60); //number of days that can be scrolled - 30, 60, 90
            editor.putInt(PREF_SET_MAX, 7); //maximum number of sets
            editor.putBoolean(PREF_DELETE_WARNING, true); //show delete warning dialog
            editor.putBoolean(PREF_DELETE_WARNING_APPOINTMENT, true); //show delete warning dialog
            editor.commit();
        }
    }



    private UserItem mCurrentUser;

    private UserItem saveUserItem(FirebaseUser user){
        UserItem item = new UserItem();
        item.uid = user.getUid();
        item.userName = user.getDisplayName();
        item.email = user.getEmail();
        item.status = "Free";

        return item;
    }


    private UserItem mUserItem;
    public void setUser(FirebaseUser user){
        //todo - need to check firebase for status
        mUserItem = new UserItem();
        mUserItem.uid = user.getUid();
        mUserItem.userName = user.getDisplayName();
        mUserItem.email = user.getEmail();
        mUserItem.status = "Free";
    }


    public String getUserId(){
        if(mUserItem != null){
            return mUserItem.uid;
        }
        return "";
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  Broadcast to widget
 */
/**************************************************************************************************/

    public void broadcastScheduleUpdate(ScheduleItem item){
        String today = DateTimeHelper.getDatestamp(0);
        String strUpdate = mActivity.getString(R.string.action_app_update);

        if(today.equals(item.datestamp)){
            Intent i = new Intent(this, AppointmentWidgetProvider.class);
            i.setAction(strUpdate);
            this.sendBroadcast(i);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  User Firebase sign in and sign out
 */
/**************************************************************************************************/

    private ClientItem mCurrentClient;
    public void setClient(ClientItem item){
        mCurrentClient = item;
    }
    public ClientItem getClient(){ return mCurrentClient; }


    private RoutineDetailItem mRoutineDetailItem;
    public RoutineDetailItem getRoutineDetail(){ return mRoutineDetailItem; }
    public void setRoutineDetailItem(RoutineDetailItem item){ mRoutineDetailItem = item; }
    
    private ScheduleItem mAppointmentItem;
    public ScheduleItem getAppointmentItem(){ return mAppointmentItem; }
    public void setAppointmentItem(ScheduleItem item){ mAppointmentItem = item; }

    private ArrayList<String> mRoutineNames;
    public ArrayList<String> getRoutineNames(){
        ArrayList<String> routines = new ArrayList<>();
        routines.addAll(mRoutineNames);
        return routines;
    }

    public ArrayList<String> getRoutineNamesForDialog(){
        ArrayList<String> routines = new ArrayList<>();
        if(!mRoutineNames.isEmpty()){
            String strNone = mActivity.getString(R.string.msg_none_selected);
            routines.add(strNone);
            routines.addAll(mRoutineNames);
            return routines;
        }
        else{
            loadRoutineNames();
            return routines;
        }
    }

    public void loadRoutineNames(){
        if(mRoutineNames.isEmpty()){
            mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mFirebaseAuth.getCurrentUser();
            RoutineNameButler butler = new RoutineNameButler((MyActivity)mActivity, user.getUid());
            butler.loadRoutineNames(LOADER_ROUTINE, new RoutineNameButler.OnLoadedListener() {
                @Override
                public void onLoaded(ArrayList<RoutineNameItem> routineList) {
                    int count = routineList.size();
                    for(int i = 0; i < count; i++){
                        mRoutineNames.add(routineList.get(i).routineName);
                    }
                }
            });
        }
    }

/**************************************************************************************************/

}
