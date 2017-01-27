package me.makeachoice.gymratpta.controller.manager;

import me.makeachoice.library.android.base.controller.manager.MyMaidRegistry;

/**************************************************************************************************/
/*
 * MaidRegistry registers MyMaid classes into a HashMap registry.
 */

/**************************************************************************************************/

public class MaidRegistry extends MyMaidRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 *      HashMap<Integer,MyMaid> mRegistry - buffer holding MyMaid classes
 */
/**************************************************************************************************/

    private static MaidRegistry instance = null;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Singleton Constructor - MaidRegistry
 */
/**************************************************************************************************/
    /*
     * Constructor - initialize registry buffer
     */
    private MaidRegistry() {
    }

    /*
     * Singleton constructor
     */
    public static MaidRegistry getInstance() {
        if(instance == null) {
            instance = new MaidRegistry();
        }
        return instance;
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


/**************************************************************************************************/

}
