package me.makeachoice.gymratpta.controller.manager;

import me.makeachoice.gymratpta.controller.viewside.maid.exercise.ExerciseMaid;
import me.makeachoice.library.android.base.controller.manager.MyMaidRegistry;
import me.makeachoice.library.android.base.controller.viewside.maid.MyMaid;

/**************************************************************************************************/
/*
 * MaidRegistry registers MyMaid classes into a HashMap registry.
 */

/**************************************************************************************************/

public class MaidRegistry extends MyMaidRegistry {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private static MaidRegistry instance = null;

    public static final String MAID_EXERCISE = "Exercise Maid";

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
 * Exercise Maids:
 */
/**************************************************************************************************/

    public MyMaid initializeExerciseMaid(int maidId, int layoutId){
        //create article maid
        ExerciseMaid maid = new ExerciseMaid(maidId, layoutId);

        //register maid
        registerMaid(MAID_EXERCISE, maid);

        return maid;
    }


/**************************************************************************************************/

}
