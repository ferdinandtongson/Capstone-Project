package me.makeachoice.library.android.base.controller.manager;

import java.util.HashMap;

import me.makeachoice.library.android.base.controller.viewside.maid.MyMaid;


/**************************************************************************************************/
/*
 * MyMaidRegistry used to register Maid classes to a HashMap registry.
 */
/**************************************************************************************************/

public abstract class MyMaidRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 *      HashMap<String,MyMaid> mRegistry - buffer holding MyMaid classes
 */
/**************************************************************************************************/

    //mRegistry - buffer holding MyMaid classes
    protected HashMap<String, MyMaid> mRegistry = new HashMap<>();

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract methods
 */
/**************************************************************************************************/

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      MyMaid requestHouseKeeper(String) - request Maid from registry
 *      void registerHouseKeeper(String,MyMaid) - register Maid
 *      void unregisterMaid(String) - remove Maid from registry
 *      void onFinish() - nulls all of the data in the buffer
 */
/**************************************************************************************************/
    /*
     * MyMaid requestMaid(String) - request Maid from register
     */
    public MyMaid requestMaid(String keeperKey){
        //check if Maid with key is registered
        if(mRegistry.containsKey(keeperKey)){
            //return Maid requested
            return mRegistry.get(keeperKey);
        }

        //invalid Maid requested
        return null;
    }

    /*
     * void registerMaid(String,MyMaid) - register Maid
     */
    public void registerMaid(String keeperKey, MyMaid houseKeeper){
        mRegistry.put(keeperKey, houseKeeper);
    }

    /*
     * void unregisterMaid(String) - remove Maid from registry
     */
    public void unregisterMaid(String keeperKey){
        mRegistry.remove(keeperKey);
    }

    /*
     * void onFinish() - nulls all of the data in the buffer
     */
    public void onFinish(){
        //clear and null refresh buffer
        mRegistry.clear();
        mRegistry = null;
    }

/**************************************************************************************************/

}
