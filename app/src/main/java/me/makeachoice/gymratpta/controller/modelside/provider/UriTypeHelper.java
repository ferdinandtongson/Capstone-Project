package me.makeachoice.gymratpta.controller.modelside.provider;

import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;

import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_EXERCISE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_EXERCISE_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_EXERCISE_WITH_EXERCISE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_EXERCISE_WITH_TIMESTAMP;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_EXERCISE_WITH_TIMESTAMP_ORDER_NUMBER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_EXERCISE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_ROUTINE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_ROUTINE_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_ROUTINE_WITH_EXERCISE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_ROUTINE_WITH_ORDER_NUMBER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_ROUTINE_WITH_TIMESTAMP;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_ROUTINE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_WITH_STATUS;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_CATEGORY_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.EXERCISE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_DATE_TIME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_TIMESTAMP;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_EXERCISE_WITH_ORDER_NUMBER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_WITH_ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.SCHEDULE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.SCHEDULE_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.SCHEDULE_WITH_RANGE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.SCHEDULE_WITH_TIMESTAMP;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.STATS;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.STATS_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.STATS_WITH_DATE_TIME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.STATS_WITH_TIMESTAMP;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.STATS_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.USER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.USER_WITH_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.dbUriMatcher;

/**************************************************************************************************/
/*
 *  UriTypeHelper helps identify the uri type by the uri
 */
/**************************************************************************************************/

public class UriTypeHelper {

    public static String getType(Uri uri){
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = dbUriMatcher.match(uri);

        switch (match) {
            case USER:
                return Contractor.UserEntry.CONTENT_TYPE;
            case USER_WITH_KEY:
                return Contractor.UserEntry.CONTENT_ITEM_TYPE;
            case CLIENT:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case CLIENT_WITH_UID:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case CLIENT_WITH_FKEY:
                return Contractor.ClientEntry.CONTENT_ITEM_TYPE;
            case CLIENT_WITH_STATUS:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case CATEGORY:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case CATEGORY_WITH_UID:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case CATEGORY_WITH_FKEY:
                return Contractor.ClientEntry.CONTENT_ITEM_TYPE;
            case CATEGORY_WITH_NAME:
                return Contractor.ClientEntry.CONTENT_ITEM_TYPE;
            case EXERCISE:
                return ExerciseContract.CONTENT_TYPE;
            case EXERCISE_WITH_UID:
                return ExerciseContract.CONTENT_TYPE;
            case EXERCISE_WITH_CATEGORY_KEY:
                return ExerciseContract.CONTENT_TYPE;
            case EXERCISE_WITH_FKEY:
                return ExerciseContract.CONTENT_ITEM_TYPE;
            case EXERCISE_WITH_NAME:
                return ExerciseContract.CONTENT_ITEM_TYPE;
            case ROUTINE:
                return RoutineContract.CONTENT_TYPE;
            case ROUTINE_WITH_UID:
                return RoutineContract.CONTENT_TYPE;
            case ROUTINE_WITH_ROUTINE_NAME:
                return RoutineContract.CONTENT_TYPE;
            case ROUTINE_EXERCISE_WITH_ORDER_NUMBER:
                return RoutineContract.CONTENT_ITEM_TYPE;
            case ROUTINE_NAME:
                return Contractor.RoutineNameEntry.CONTENT_TYPE;
            case ROUTINE_NAME_WITH_UID:
                return Contractor.RoutineNameEntry.CONTENT_TYPE;
            case ROUTINE_NAME_WITH_NAME:
                return Contractor.RoutineNameEntry.CONTENT_ITEM_TYPE;
            case SCHEDULE:
                return ScheduleContract.CONTENT_TYPE;
            case SCHEDULE_WITH_TIMESTAMP:
                return ScheduleContract.CONTENT_TYPE;
            case SCHEDULE_WITH_CLIENT_KEY:
                return ScheduleContract.CONTENT_TYPE;
            case SCHEDULE_WITH_RANGE:
                return ScheduleContract.CONTENT_TYPE;
            case NOTES:
                return NotesContract.CONTENT_TYPE;
            case NOTES_WITH_UID:
                return NotesContract.CONTENT_TYPE;
            case NOTES_WITH_CLIENT_KEY:
                return NotesContract.CONTENT_TYPE;
            case NOTES_WITH_TIMESTAMP:
                return NotesContract.CONTENT_TYPE;
            case NOTES_WITH_DATE_TIME:
                return NotesContract.CONTENT_ITEM_TYPE;
            case STATS:
                return StatsContract.CONTENT_TYPE;
            case STATS_WITH_UID:
                return StatsContract.CONTENT_TYPE;
            case STATS_WITH_CLIENT_KEY:
                return StatsContract.CONTENT_TYPE;
            case STATS_WITH_TIMESTAMP:
                return StatsContract.CONTENT_TYPE;
            case STATS_WITH_DATE_TIME:
                return StatsContract.CONTENT_ITEM_TYPE;
            case CLIENT_ROUTINE:
                return ClientRoutineContract.CONTENT_TYPE;
            case CLIENT_ROUTINE_WITH_UID:
                return ClientRoutineContract.CONTENT_TYPE;
            case CLIENT_ROUTINE_WITH_CLIENT_KEY:
                return ClientRoutineContract.CONTENT_TYPE;
            case CLIENT_ROUTINE_WITH_TIMESTAMP:
                return ClientRoutineContract.CONTENT_TYPE;
            case CLIENT_ROUTINE_WITH_EXERCISE:
                return ClientRoutineContract.CONTENT_TYPE;
            case CLIENT_ROUTINE_WITH_ORDER_NUMBER:
                return ClientRoutineContract.CONTENT_TYPE;
            case CLIENT_EXERCISE:
                return ClientExerciseContract.CONTENT_TYPE;
            case CLIENT_EXERCISE_WITH_UID:
                return ClientExerciseContract.CONTENT_TYPE;
            case CLIENT_EXERCISE_WITH_CLIENT_KEY:
                return ClientExerciseContract.CONTENT_TYPE;
            case CLIENT_EXERCISE_WITH_EXERCISE:
                return ClientExerciseContract.CONTENT_TYPE;
            case CLIENT_EXERCISE_WITH_TIMESTAMP:
                return ClientExerciseContract.CONTENT_TYPE;
            case CLIENT_EXERCISE_WITH_TIMESTAMP_ORDER_NUMBER:
                return ClientExerciseContract.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

}
