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

import android.util.Log;

import me.makeachoice.library.android.base.controller.viewside.MaidRegistry;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;

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

    protected void initDatabase(){
        //
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

}
