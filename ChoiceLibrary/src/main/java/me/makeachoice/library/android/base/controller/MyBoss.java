package me.makeachoice.library.android.base.controller;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.concurrent.Executor;

import me.makeachoice.library.android.base.controller.viewside.HouseKeeperRegistry;
import me.makeachoice.library.android.base.controller.viewside.MaidRegistry;
import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;
import me.makeachoice.library.android.base.controller.viewside.maid.MyMaid;

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
 *      Context mActivityContext - current Activity Context on display
 *      SQLiteDatabase mDB - SQLiteDatabase object
 *      boolean mIsTablet - is a tablet device status flag
 *      HouseKeeperRegistry mKeeperRegistry - MyHouseKeeper registry
 *      MaidRegistry mMaidRegistry - MyMaid registry
 */
/**************************************************************************************************/

    //mActivityContext - current Activity Context on display
    protected Context mActivityContext;

    //mDB - SQLiteDatabase object
    protected SQLiteDatabase mDB;

    //mIsTablet - is a tablet device status flag
    protected boolean mIsTablet;

    //mKeeperRegistry - MyHouseKeeper registry
    protected HouseKeeperRegistry mKeeperRegistry;

    //mMaidRegistry - MyMaid registry
    protected MaidRegistry mMaidRegistry;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract Methods:
 *      void initDatabase() - initialize database
 *      void initHouseKeepers() - initializes HouseKeeper classes; they handle Activity UI
 *      void initStaff() - initialize Staff classes, maintains ArrayList and HashMap buffers
 *      void initValets() - initialize Valet classes, handles request to DB
 *      void initButlers() - initialize Butler classes, handles calls to APIs
 *      void onFinish() - application is about to close, clear all local buffer from memory
 */
/**************************************************************************************************/
    /*
     * void initDatabase() - initialize database
     */
    protected abstract void initDatabase();

    /*
     * void initHouseKeepers() - initializes HouseKeeper classes; they handle Activity UI
     */
    protected abstract void initHouseKeepers();

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


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      Context getActivityContext() - get current Activity Context
 *      Executor getExecutor() - get AsyncTask thread pool executor
 *      boolean getIsTablet() - get if device is a tablet status flag
 */
/**************************************************************************************************/
    /*
     * Context getActivityContext() - get current Activity Context
     * @return - context of current activity
     */
    public Context getActivityContext(){
        return mActivityContext;
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
 *      void setActivityContext(Context) - stores the Activity context
 *      void setIsTablet(boolean) - set tablet status flag
 */
/**************************************************************************************************/
    /*
     * void setActivityContext(Context) - stores the Activity Context
     * @param ctx - Activity context
     */
    public void setActivityContext(Context ctx){
        //set Activity context - in this case there is only MainActivity
        mActivityContext = ctx;
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
 *      MyHouseKeeper requestHouseKeeper(int) - get HouseKeeper requested
 *      void registerMaid(int,MyMaid) - register Maid
 *      MyMaid requestMaid(int) - get requested Maid
 *      void unregisterMaid(int) - remove Maid from registry
 */
/**************************************************************************************************/
    /*
     * MyHouseKeeper requestHouseKeeper(int) - get requested HouseKeeper
     * @param id - id number of HouseKeeper
     * @return - HouseKeeper object
     */
    public MyHouseKeeper requestHouseKeeper(int id){
        //get requested HouseKeeper from buffer
        return mKeeperRegistry.requestHouseKeeper(id);
    }

    /*
     * void registerMaid(int,MyMaid) - register Maid
     * @param id - Maid id number
     * @param maid - MyMaid class
     */
    public void registerMaid(int id, MyMaid maid){
        //register Maid
        mMaidRegistry.registerMaid(id, maid);
    }

    /*
     * MyMaid requestMaid(int) - get requested Maid
     * @param id - id number of Maid
     * @return - Maid object
     */
    public MyMaid requestMaid(int id){
        return mMaidRegistry.requestMaid(id);
    }

    /*
     * void unregisterMaid(int) - remove Maid from registry
     * @param id - id number of Maid
     */
    public void unregisterMaid(int id){
        //remove maid from registry
        mMaidRegistry.unregisterMaid(id);
    }


/**************************************************************************************************/

}
