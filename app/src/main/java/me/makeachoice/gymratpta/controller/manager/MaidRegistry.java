package me.makeachoice.gymratpta.controller.manager;

import java.util.HashMap;

import me.makeachoice.library.android.base.controller.viewside.maid.MyMaid;

/**************************************************************************************************/
/*
 * MaidRegistry registers MyMaid classes into a HashMap registry.
 */

/**************************************************************************************************/

public class MaidRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 *      HashMap<Integer,MyMaid> mRegistry - buffer holding MyMaid classes
 */
/**************************************************************************************************/

    //mRegistry - buffer holding MyMaid classes
    private HashMap<Integer, MyMaid> mRegistry = new HashMap<>();

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MaidRegistry - constructor, initialize hashMap registry
 */
/**************************************************************************************************/
    /*
     * Constructor - initialize registry buffer
     */
    public MaidRegistry(){
        //buffer holding MyMaid classes
        mRegistry = new HashMap<>();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods:
 *      MyMaid requestMaid(int) - request Maid from buffer
 *      void registerMaid(int,MyMaid) - register MyMaid into buffer
 *      void unregisterMaid(int) - remove MyMaid from buffer
 *      void onFinish() - nulls all of the data in the buffer
 */
/**************************************************************************************************/
    /*
     * MyMaid requestMaid(int) - request Maid from buffer
     * @param id - id number of Maid
     * @return - Maid object requested
     */
    public MyMaid requestMaid(int id){
        //check if maid with id number is in buffer
        if(mRegistry.containsKey(id)){
            //return maid requested
            return mRegistry.get(id);
        }

        //invalid maid requested
        return null;
    }

    /*
     * void registerMaid(int,MyMaid) - register MyMaid into buffer
     * @param id - id number of Maid
     * @param maid - maid object to be registered
     */
    public void registerMaid(int id, MyMaid maid){
        mRegistry.put(id, maid);
    }

    /*
     * void unregisterMaid(int) - remove MyMaid from buffer
     * @param id - id number of Maid
     */
    public void unregisterMaid(int id){
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
