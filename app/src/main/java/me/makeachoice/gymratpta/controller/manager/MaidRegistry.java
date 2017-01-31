package me.makeachoice.gymratpta.controller.manager;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.StubMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.appointment.DayMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.appointment.DayViewPagerMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.appointment.WeekPageMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.ExerciseMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.ExerciseViewPagerMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.RoutineMaid;
import me.makeachoice.gymratpta.model.item.ClientCardItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;

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

    public static final String MAID_EXERCISE = "ExerciseMaid";
    public static final String MAID_ROUTINE = "RoutineMaid";
    public static final String MAID_EXERCISE_VP = "ExerciseViewPagerMaid";

    public static final String MAID_DAY = "DayMaid";
    public static final String MAID_WEEK = "WeekPageMaid";
    public static final String MAID_DAY_VP = "DayViewPagerMaid";

    public static final String MAID_STUB = "StubMaid";

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

    public void initializeExerciseViewPagerMaid(String maidKey, int layoutId, ArrayList<ExerciseItem> exercises){
        //create maid
        ExerciseViewPagerMaid maid = new ExerciseViewPagerMaid(maidKey, layoutId, exercises);

        //register maid
        registerMaid(maidKey, maid);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Appointment Maids:
 */
/**************************************************************************************************/

    public void initializeDayMaid(String maidKey, int layoutId){
        //create maid
        DayMaid maid = new DayMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeDayViewPagerMaid(String maidKey, int layoutId, ArrayList<ClientCardItem> clients){
        //create maid
        DayViewPagerMaid maid = new DayViewPagerMaid(maidKey, layoutId, clients);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeWeekMaid(String maidKey, int layoutId){
        //create maid
        WeekPageMaid maid = new WeekPageMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

/**************************************************************************************************/


    public StubMaid initializeStubMaid(String maidKey){
        int layoutId = R.layout.stub_text;

        //create maid
        StubMaid maid = new StubMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);

        return maid;
    }
}
