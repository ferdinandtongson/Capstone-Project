package me.makeachoice.library.android.base.controller.manager;

import java.util.HashMap;

import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;


/**************************************************************************************************/
/*
 * MyHouseKeeperRegistry used to register HouseKeeper classes to a HashMap registry.
 */
/**************************************************************************************************/

public abstract class MyHouseKeeperRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 *      HashMap<String,MyHouseKeeper> mRegistry - buffer holding MyHouseKeeper classes
 */
/**************************************************************************************************/

    //mRegistry - buffer holding MyHouseKeeper classes
    protected HashMap<String, MyHouseKeeper> mRegistry = new HashMap<>();

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Abstract methods
 *      void initializeHouseKeepers() - initialize HouseKeepers
 */
/**************************************************************************************************/
    /*
     * void initializeHouseKeepers() - initialize HouseKeepers
     */
    protected abstract void initializeHouseKeepers();

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      MyHouseKeeper requestHouseKeeper(String) - request HouseKeeper from registry
 *      void registerHouseKeeper(String,MyHouseKeeper) - register HouseKeeper
 *      void unregisterHouseKeeper(String) - remove MyHouseKeeper from buffer
 *      void onFinish() - nulls all of the data in the buffer
 */
/**************************************************************************************************/
    /*
     * MyHouseKeeper getHouseKeeper(String) - request HouseKeeper from register
     */
    public MyHouseKeeper requestHouseKeeper(String keeperKey){
        //check if HouseKeeper with key is registered
        if(mRegistry.containsKey(keeperKey)){
            //return HouseKeeper requested
            return mRegistry.get(keeperKey);
        }

        //invalid HouseKeeper requested
        return null;
    }

    /*
     * void registerHouseKeeper(String,MyHouseKeeper) - register HouseKeeper
     */
    public void registerHouseKeeper(String keeperKey, MyHouseKeeper houseKeeper){
        mRegistry.put(keeperKey, houseKeeper);
    }

    /*
     * void unregisterHouseKeeper(String) - remove HouseKeeper from registry
     */
    public void unregisterHouseKeeper(String keeperKey){
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
