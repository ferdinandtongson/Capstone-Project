package me.makeachoice.gymratpta.controller.manager;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.StubMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.appointment.DayMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.appointment.DayViewPagerMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.appointment.WeekMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientHistoryMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientInfoMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientNotesMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientScheduleMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientStatsMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.ExerciseMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.ExerciseViewPagerMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.RoutineDetailMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.exercise.RoutineMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.session.SessionNotesViewPagerMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientRoutineMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.session.SessionStatsMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.session.SessionStatsViewPagerMaid;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineDetailItem;

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

    //Appointment Screen maids
    public static final String MAID_DAY = "DayMaid";
    public static final String MAID_WEEK = "WeekMaid";
    public static final String MAID_DAY_VP = "DayViewPagerMaid";

    //Client Screen maids
    public static final String MAID_CLIENT_INFO = "ClientInfoMaid";
    public static final String MAID_CLIENT_SCHEDULE = "ClientScheduleMaid";
    public static final String MAID_CLIENT_HISTORY = "ClientHistoryMaid";
    public static final String MAID_CLIENT_HISTORY_VP = "ClientHistoryViewPagerMaid";

    //Exercise Screen maids
    public static final String MAID_EXERCISE = "ExerciseMaid";
    public static final String MAID_ROUTINE = "RoutineMaid";
    public static final String MAID_EXERCISE_VP = "ExerciseViewPagerMaid";
    public static final String MAID_ROUTINE_DETAIL = "RoutineDetailMaid";

    //Session screen maids
    public static final String MAID_SESSION_ROUTINE = "ClientRoutineMaid";
    public static final String MAID_SESSION_STATS = "SessionStatsMaid";
    public static final String MAID_SESSION_NOTES = "SessionNotesMaid";
    public static final String MAID_SESSION_STATS_VP = "SessionStatsViewPagerMaid";
    public static final String MAID_SESSION_NOTES_VP = "SessionNotesViewPagerMaid";

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
 * Appointment Maids:
 */
/**************************************************************************************************/

    public void initializeDayMaid(String maidKey, int layoutId, String userId){
        //create maid
        DayMaid maid = new DayMaid(maidKey, layoutId, userId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeDayViewPagerMaid(String maidKey, int layoutId, String userId, String appointmentDate, int counter){
        //create maid
        DayViewPagerMaid maid = new DayViewPagerMaid(maidKey, layoutId, userId, appointmentDate, counter);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeWeekMaid(String maidKey, int layoutId, String userId){
        //create maid
        WeekMaid maid = new WeekMaid(maidKey, layoutId, userId);

        //register maid
        registerMaid(maidKey, maid);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Client Maids:
 */
/**************************************************************************************************/

    public void initializeClientInfoMaid(String maidKey, int layoutId, ClientItem item){
        //create maid
        ClientInfoMaid maid = new ClientInfoMaid(maidKey, layoutId, item);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientScheduleMaid(String maidKey, int layoutId, ClientItem item){
        //create maid
        ClientScheduleMaid maid = new ClientScheduleMaid(maidKey, layoutId, item.uid, item);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientNotesMaid(String maidKey, int layoutId, String userId, ClientItem item,
                                          AppointmentItem appItem){
        //create maid
        ClientNotesMaid maid = new ClientNotesMaid(maidKey, layoutId, userId, item, appItem);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientStatsMaid(String maidKey, int layoutId, String userId, ClientItem item,
                                          AppointmentItem appItem){
        //create maid
        ClientStatsMaid maid = new ClientStatsMaid(maidKey, layoutId, userId, item, appItem);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientRoutineMaid(String maidKey, int layoutId, String userId, ClientItem item,
                                            AppointmentItem appItem){
        //create maid
        ClientRoutineMaid maid = new ClientRoutineMaid(maidKey, layoutId, userId, item, appItem);

        //register maid
        registerMaid(maidKey, maid);
    }



    public void initializeClientHistoryMaid(String maidKey, int layoutId){
        //create maid
        ClientHistoryMaid maid = new ClientHistoryMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    /*public void initializeClientHistoryViewPagerMaid(String maidKey, int layoutId, ArrayList<ClientCardItem> clients){
        //create maid
        //DayViewPagerMaid maid = new DayViewPagerMaid(maidKey, layoutId, clients);

        //register maid
        //registerMaid(maidKey, maid);
    }*/

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Exercise Maids:
 */
/**************************************************************************************************/

    public void initializeExerciseMaid(String maidKey, int layoutId, String userId){
        //create maid
        ExerciseMaid maid = new ExerciseMaid(maidKey, layoutId, userId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeRoutineMaid(String maidKey, int layoutId, String userId){
        //create maid
        RoutineMaid maid = new RoutineMaid(maidKey, layoutId, userId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeExerciseViewPagerMaid(String maidKey, int layoutId, int index, CategoryItem categoryItem){
        //create maid
        ExerciseViewPagerMaid maid = new ExerciseViewPagerMaid(maidKey, layoutId, index, categoryItem);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeRoutineDetailMaid(String maidKey, int layoutId, String userId, RoutineDetailItem routineItem){
        //create maid
        RoutineDetailMaid maid = new RoutineDetailMaid(maidKey, layoutId, userId, routineItem);

        //register maid
        registerMaid(maidKey, maid);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Session Maids:
 */
/**************************************************************************************************/

    public void initializeSessionStatsMaid(String maidKey, int layoutId){
        //create maid
        SessionStatsMaid maid = new SessionStatsMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeSessionStatsViewPagerMaid(String maidKey, int layoutId){
        //create maid
        SessionStatsViewPagerMaid maid = new SessionStatsViewPagerMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeSessionNotesViewPagerMaid(String maidKey, int layoutId){
        //create maid
        SessionNotesViewPagerMaid maid = new SessionNotesViewPagerMaid(maidKey, layoutId);

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
