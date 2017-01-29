package me.makeachoice.gymratpta.controller.manager;

import android.util.Log;

import me.makeachoice.gymratpta.controller.viewside.maid.exercise.ExerciseMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.RoutineMaid;
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
 */
/**************************************************************************************************/

    private static MaidRegistry instance = null;

    public static final String MAID_EXERCISE = "Exercise Maid";
    public static final String MAID_ROUTINE = "Routine Maid";
    public static final String MAID_EXERCISE_LIST = "Exercise List Maid";

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

    public void initializeExerciseMaid(String maidKey, int layoutId){
        //create maid
        ExerciseMaid maid = new ExerciseMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeRoutineMaid(String maidKey, int layoutId){
        //create maid
        RoutineMaid maid = new RoutineMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeExerciseListMaid(String maidKey, int layoutId, int maidIndex){
        //create maid
        //ExerciseListMaid maid = new ExerciseListMaid(maidKey, layoutId, maidIndex);

        //register maid
        //registerMaid(maidKey, maid);
    }

    public void showRegistry(){
        int count = mRegistry.size();
        Log.d("Choice", "MaidRegistry - size " + count);


    }

/**************************************************************************************************/

}
