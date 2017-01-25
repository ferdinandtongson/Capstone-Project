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
 *      HashMap<String,MyHouseKeeper> MyHouseKeeper - buffer holding MyHouseKeeper classes
 */
/**************************************************************************************************/

    //mRegistry - buffer holding MyHouseKeeper classes
    private HashMap<String, MyHouseKeeper> mRegistry = new HashMap<>();

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
 *      void unregisterHouseKeeper(int) - remove MyHouseKeeper from buffer
 *      void onFinish() - nulls all of the data in the buffer
 */
/**************************************************************************************************/
    /*
     * MyHouseKeeper getHouseKeeper(int) - request HouseKeeper from register
     * @param keeperKey - registry key of HouseKeeper
     * @return - HouseKeeper object requested
     */
    public MyHouseKeeper requestHouseKeeper(String keeperKey){
        //check if HouseKeeper with with key is registered
        if(mRegistry.containsKey(keeperKey)){
            //return HouseKeeper requested
            return mRegistry.get(keeperKey);
        }

        //invalid HouseKeeper requested
        return null;
    }

    /*
     * void registerHouseKeeper(String,MyHouseKeeper) - register HouseKeeper
     * @param keeperKey - registry key of HouseKeeper
     * @param houseKeeper - HouseKeeper object to be registered
     */
    public void registerHouseKeeper(String keeperKey, MyHouseKeeper houseKeeper){
        mRegistry.put(keeperKey, houseKeeper);
    }

    /*
     * void unregisterHouseKeeper(int) - remove HouseKeeper from registry
     * @param keeperKey - registry key of HouseKeeper
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
