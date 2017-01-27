package me.makeachoice.gymratpta.controller.manager;

/**************************************************************************************************/
/*
 * HouseKeeper used to register HouseKeeper to a HashMap registry.
 *
 * Inherited Class Variables:
 *      HashMap<String, MyHouseKeeper> mRegistry;
 *
 * Inherited Methods:
 *      MyHouseKeeper requestHouseKeeper(String) - request HouseKeeper from registry
 *      void registerHouseKeeper(String,MyHouseKeeper) - register HouseKeeper
 *      void unregisterHouseKeeper(int) - remove MyHouseKeeper from buffer
 *      void onFinish() - nulls all of the data in the buffer
 *
 * Inherited Abstract Methods:
 *      void initializeHouseKeepers() - initialize HouseKeepers
 */

import android.util.Log;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.StubAppointmentKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.StubClientKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.StubExerciseKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.StubSessionKeeper;
import me.makeachoice.library.android.base.controller.manager.MyHouseKeeperRegistry;

/**************************************************************************************************/

public class HouseKeeperRegistry extends MyHouseKeeperRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private static HouseKeeperRegistry instance = null;

    public static String KEEPER_CLIENT = "Keeper Client";
    public static String KEEPER_APPOINTMENT = "Keeper Appointment";
    public static String KEEPER_SESSION = "Keeper Session";
    public static String KEEPER_EXERCISE = "Keeper Exercise";

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Singleton Constructor - HouseKeeperRegistry
 */
/**************************************************************************************************/
    /*
     * Constructor - initialize registry buffer
     */
    private HouseKeeperRegistry() {
    }

    /*
     * Singleton constructor
     */
    public static HouseKeeperRegistry getInstance() {
        if(instance == null) {
            instance = new HouseKeeperRegistry();
        }
        return instance;
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialize Methods:
 *      MyHouseKeeper requestHouseKeeper(String) - request HouseKeeper from registry
 *      void registerHouseKeeper(String,MyHouseKeeper) - register HouseKeeper
 *      void unregisterHouseKeeper(int) - remove MyHouseKeeper from buffer
 *      void onFinish() - nulls all of the data in the buffer
 */
/**************************************************************************************************/
    /*
     * void initializeHouseKeepers() - request HouseKeeper from register
     * @param keeperKey - registry key of HouseKeeper
     * @return - HouseKeeper object requested
     */
    public void initializeHouseKeepers(){
        Log.d("Choice", "HouseKeeperRegistry.initializeHouseKeepers");
        StubClientKeeper clientKeeper = new StubClientKeeper(R.layout.standard_activity);
        registerHouseKeeper(KEEPER_CLIENT, clientKeeper);

        StubAppointmentKeeper appointmentKeeper = new StubAppointmentKeeper(R.layout.standard_activity);
        registerHouseKeeper(KEEPER_APPOINTMENT, appointmentKeeper);

        StubSessionKeeper sessionKeeper = new StubSessionKeeper(R.layout.standard_activity);
        registerHouseKeeper(KEEPER_SESSION, sessionKeeper);

        StubExerciseKeeper exerciseKeeper = new StubExerciseKeeper(R.layout.standard_activity);
        registerHouseKeeper(KEEPER_EXERCISE, exerciseKeeper);

        //ClientKeeper clientKeeper = new ClientKeeper(R.layout.standard_recycler_all);
        //mKeeperRegistry.registerHouseKeeper(ClientKeeper.KEEPER_ID, clientKeeper);
    }


/**************************************************************************************************/

}
