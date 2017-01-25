package me.makeachoice.library.android.base.controller.viewside;

import java.util.HashMap;

import me.makeachoice.library.android.base.controller.viewside.housekeeper.MyHouseKeeper;

/**************************************************************************************************/
/*
 * HouseKeeperRegistry registers MyHouseKeeper classes to a HashMap registry.
 */

/**************************************************************************************************/

public class HouseKeeperRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 *      HashMap<Integer,MyHouseKeeper> mRegistry - buffer holding MyHouseKeeper classes
 */
/**************************************************************************************************/

    //mRegistry - buffer holding MyHouseKeeper classes
    private HashMap<Integer, MyHouseKeeper> mRegistry = new HashMap<>();


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * HouseKeeperRegistry - constructor, initialize hashMap registry
 */
/**************************************************************************************************/
    /*
     * Constructor - initialize registry buffer
     */
    public HouseKeeperRegistry(){
        //buffer holding MyHouseKeeper classes
        mRegistry = new HashMap<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      MyHouseKeeper requestHouseKeeper(int) - request HouseKeeper from buffer
 *      void registerHouseKeeper(MyHouseKeeper,int) - register HouseKeeper into buffer
 *      void unregisterHouseKeeper(int) - remove MyHouseKeeper from buffer
 *      void onFinish() - nulls all of the data in the buffer
 */
/**************************************************************************************************/
    /*
     * MyHouseKeeper getHouseKeeper(int) - request HouseKeeper from buffer
     * @param id - id number of HouseKeeper
     * @return - HouseKeeper object requested
     */
    public MyHouseKeeper requestHouseKeeper(int id){
        //check if HouseKeeper with id number is in buffer
        if(mRegistry.containsKey(id)){
            //return HouseKeeper requested
            return mRegistry.get(id);
        }

        //invalid HouseKeeper requested
        return null;
    }

    /*
     * void registerHouseKeeper(int,MyHouseKeeper) - register MyHouseKeeper into buffer.
     * @param id - id number of HouseKeeper
     * @param houseKeeper - HouseKeeper object to be registered
     */
    public void registerHouseKeeper(int id, MyHouseKeeper houseKeeper){
        mRegistry.put(id, houseKeeper);
    }

    /*
     * void unregisterHouseKeeper(int) - remove MyHouseKeeper from buffer
     * @param id - id number of HouseKeeper
     */
    public void unregisterHouseKeeper(int id){
        mRegistry.remove(id);
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
