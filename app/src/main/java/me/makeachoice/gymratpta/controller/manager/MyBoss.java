package me.makeachoice.gymratpta.controller.manager;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.concurrent.Executor;

import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;


/**************************************************************************************************/
/*
 * MyBoss is an Abstract class that extends the Application class. It is the main controller of the
 * app and handles the classes that interface with the View and Model classes.
 *
 * MyBoss classes adhere to the MVC (Model-View-Controller) model.
 */
/**************************************************************************************************/

public abstract class MyBoss extends Application{

/**************************************************************************************************/
/*
 * Class Variables:
 *      Activity mActivity - current Activity
 *      SQLiteDatabase mDB - SQLiteDatabase object
 *      boolean mIsTablet - is a tablet device status flag
 *      MyHouseKeeperRegistry mKeeperRegistry - MyHouseKeeper registry
 *      MaidRegistry mMaidRegistry - MyMaid registry
 */
/**************************************************************************************************/

    //mActivity - current Activity
    protected Activity mActivity;

    //mDB - SQLiteDatabase object
    protected SQLiteDatabase mDB;

    //mIsTablet - is a tablet device status flag
    protected boolean mIsTablet;

    //mMaidRegistry - MyMaid registry
    protected MaidRegistry mMaidRegistry;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods:
 *      void initDatabase() - initialize database
 *      void initStaff() - initialize Staff classes, maintains ArrayList and HashMap buffers
 *      void initValets() - initialize Valet classes, handles request to DB
 *      void initButlers() - initialize Butler classes, handles calls to APIs
 *      void onFinish() - application is about to close, clear all local buffer from memory
 *      MyHouseKeeper requestHouseKeeper(String) - request house keeper with keeper key value
 */
/**************************************************************************************************/
    /*
     * void initDatabase() - initialize database
     */
    protected abstract void initDatabase();

    /*
     * void initStaff() - initializes Staff classes; they maintain ArrayList and HashMap buffers
     */
    protected abstract void initStaff();

    /*
     * void initValets() - initialize Valet classes; they handle database requests
     */
    protected abstract void initValets();

    /*
     * void initButlers() - initializes Butler classes; they handle making and processing API calls
     */
    protected abstract void initButlers();

    /*
     * void onFinish() - application is about to close, clear all local buffer from memory
     */
    public abstract void onFinish();

    //MyHouseKeeper requestHouseKeeper(String) - request house keeper with keeper key value
    public abstract MyHouseKeeper requestHouseKeeper(String keeperKey);


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      Activity getActivity() - get current Activity Context
 *      Executor getExecutor() - get AsyncTask thread pool executor
 *      boolean getIsTablet() - get if device is a tablet status flag
 */
/**************************************************************************************************/
    /*
     * Activity getActivity() - get current Activity Context
     * @return - current activity
     */
    public Activity getActivity(){
        return mActivity;
    }

    /*
     * Executor getExecutor() - get AsyncTask thread pool executor
     * @return - return pool executor
     */
    public Executor getExecutor(){
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    /*
     * boolean getIsTablet() - get if device is a tablet status flag
     * @return - flag if user device is a tablet or not
     */
    public boolean getIsTablet(){ return mIsTablet; }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setters Methods:
 *      void void setActivity(Activity) - stores the current Activity
 *      void setIsTablet(boolean) - set tablet status flag
 */
/**************************************************************************************************/
    /*
     * void setActivity(Activity) - stores the current Activity
     * @param activity - current Activity
     */
    public void setActivity(Activity activity){
        mActivity = activity;
    }

    /*
     * void setIsTablet(boolean) - set tablet status flag
     * @param isTablet - status of user device, if tablet device or not
     */
    public void setIsTablet(boolean isTablet){ mIsTablet = isTablet; }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * HouseKeeper and Maid Methods:
 */
/**************************************************************************************************/


/**************************************************************************************************/

}
