package me.makeachoice.gymratpta.controller.manager;

import java.util.Date;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.maid.StubMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientHistoryNotesMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientHistoryStatsMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.schedule.DailyMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.schedule.DayViewMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.schedule.ScheduleDetailMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.schedule.WeekViewMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.schedule.WeeklyMaid;
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
import me.makeachoice.gymratpta.controller.viewside.maid.client.ClientExerciseMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.session.SessionStatsMaid;
import me.makeachoice.gymratpta.controller.viewside.maid.session.SessionStatsViewPagerMaid;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
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
    public static final String MAID_DAY = "DailyMaid";
    public static final String MAID_WEEK = "WeeklyMaid";
    public static final String MAID_DAY_VP = "DayViewMaid";
    public static final String MAID_WEEK_VP = "WeekViewMaid";
    public static final String MAID_SCHEDULE_DETAIL = "ScheduleDetailMaid";

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
    public static final String MAID_SESSION_ROUTINE = "ClientExerciseMaid";
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
 * Schedul Maids:
 */
/**************************************************************************************************/

    public void initializeDailyMaid(String maidKey, int layoutId, String userId){
        //create maid
        DailyMaid maid = new DailyMaid(maidKey, layoutId, userId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeDayViewMaid(String maidKey, int layoutId, String userId, String datestamp, int counter){
        //create maid
        DayViewMaid maid = new DayViewMaid(maidKey, layoutId, userId, datestamp, counter);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeWeeklyMaid(String maidKey, int layoutId, String userId){
        //create maid
        WeeklyMaid maid = new WeeklyMaid(maidKey, layoutId, userId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeWeekViewMaid(String maidKey, int layoutId, String userId, String[] dateRange,
                                       Date startDate, int counter){
        //create maid
        WeekViewMaid maid = new WeekViewMaid(maidKey, layoutId, userId, dateRange, startDate, counter);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeScheduleDetailMaid(String maidKey, int layoutId, String userId, ClientItem item,
                                            ScheduleItem appItem){
        //create maid
        ScheduleDetailMaid maid = new ScheduleDetailMaid(maidKey, layoutId, userId, item, appItem);

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
                                          ScheduleItem appItem){
        //create maid
        ClientNotesMaid maid = new ClientNotesMaid(maidKey, layoutId, userId, item, appItem);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientStatsMaid(String maidKey, int layoutId, String userId, ClientItem item,
                                          ScheduleItem appItem){
        //create maid
        ClientStatsMaid maid = new ClientStatsMaid(maidKey, layoutId, userId, item, appItem);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientRoutineMaid(String maidKey, int layoutId, String userId, ClientItem item,
                                            ScheduleItem appItem){
        //create maid
        ClientExerciseMaid maid = new ClientExerciseMaid(maidKey, layoutId, userId, item, appItem);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientHistoryMaid(String maidKey, int layoutId){
        //create maid
        ClientHistoryMaid maid = new ClientHistoryMaid(maidKey, layoutId);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientHistoryNotesMaid(String maidKey, int layoutId, String userId,
                                                 ClientItem item){
        //create maid
        ClientHistoryNotesMaid maid = new ClientHistoryNotesMaid(maidKey, layoutId, userId, item);

        //register maid
        registerMaid(maidKey, maid);
    }

    public void initializeClientHistoryStatsMaid(String maidKey, int layoutId, String userId,
                                                 ClientItem item){
        //create maid
        ClientHistoryStatsMaid maid = new ClientHistoryStatsMaid(maidKey, layoutId, userId, item);

        //register maid
        registerMaid(maidKey, maid);
    }


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
