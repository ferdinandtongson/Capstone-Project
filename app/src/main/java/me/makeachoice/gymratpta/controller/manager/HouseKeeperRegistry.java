package me.makeachoice.gymratpta.controller.manager;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.ClientDetailKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.ExerciseKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.ScheduleDetailKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.SessionDetailKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.SessionKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.ScheduleKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.ClientKeeper;
import me.makeachoice.gymratpta.controller.viewside.housekeeper.StubRoutineDetailKeeper;
import me.makeachoice.library.android.base.controller.manager.MyHouseKeeperRegistry;

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
/**************************************************************************************************/

public class HouseKeeperRegistry extends MyHouseKeeperRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private static HouseKeeperRegistry instance = null;

    public static String KEEPER_CLIENT = "Keeper Client";
    public static String KEEPER_CLIENT_DETAIL = "Keeper Client Detail";
    public static String KEEPER_SCHEDULE = "Keeper Schedule";
    public static String KEEPER_SCHEDULE_DETAIL = "Keeper Schedule Detail";
    public static String KEEPER_SESSION = "Keeper Session";
    public static String KEEPER_SESSION_DETAIL = "Keeper Session Detail";
    public static String KEEPER_EXERCISE = "Keeper Exercise";
    public static String KEEPER_ROUTINE_DETAIL = "Keeper Routine Detail";

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
 *      void initializeHouseKeepers() - initialize and register HouseKeepers
 */
/**************************************************************************************************/
    /*
     * void initializeHouseKeepers() - initialize and register HouseKeepers
     */
    public void initializeHouseKeepers(){
        ClientKeeper clientKeeper = new ClientKeeper(R.layout.activity_recycler);
        registerHouseKeeper(KEEPER_CLIENT, clientKeeper);

        ClientDetailKeeper clientDetailKeeper = new ClientDetailKeeper(R.layout.activity_bottom_nav);
        registerHouseKeeper(KEEPER_CLIENT_DETAIL, clientDetailKeeper);

        ScheduleKeeper scheduleKeeper = new ScheduleKeeper(R.layout.activity_bottom_nav);
        registerHouseKeeper(KEEPER_SCHEDULE, scheduleKeeper);

        ScheduleDetailKeeper scheduleDetailKeeper = new ScheduleDetailKeeper(R.layout.activity_bottom_nav);
        registerHouseKeeper(KEEPER_SCHEDULE_DETAIL, scheduleDetailKeeper);

        SessionKeeper sessionKeeper = new SessionKeeper(R.layout.activity_recycler);
        registerHouseKeeper(KEEPER_SESSION, sessionKeeper);

        SessionDetailKeeper sessionDetailKeeper = new SessionDetailKeeper(R.layout.activity_bottom_nav);
        registerHouseKeeper(KEEPER_SESSION_DETAIL, sessionDetailKeeper);

        ExerciseKeeper exerciseKeeper = new ExerciseKeeper(R.layout.activity_bottom_nav);
        registerHouseKeeper(KEEPER_EXERCISE, exerciseKeeper);

        StubRoutineDetailKeeper routineDetailKeeper = new StubRoutineDetailKeeper(R.layout.activity_bottom_nav);
        registerHouseKeeper(KEEPER_ROUTINE_DETAIL, routineDetailKeeper);
    }


/**************************************************************************************************/

}
