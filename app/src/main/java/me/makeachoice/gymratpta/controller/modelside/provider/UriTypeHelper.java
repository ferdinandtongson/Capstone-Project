package me.makeachoice.gymratpta.controller.modelside.provider;

import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.Contractor;

import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_CLIENT_KEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_DAY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.APPOINTMENT_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CATEGORY_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.CLIENT;
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
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_DATE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_DATE_TIME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.NOTES_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_EXERCISE_WITH_ORDER_NUMBER;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_NAME_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_WITH_ROUTINE_NAME;
import static me.makeachoice.gymratpta.controller.modelside.provider.UriMatcherHelper.ROUTINE_WITH_UID;
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
                return Contractor.ExerciseEntry.CONTENT_TYPE;
            case EXERCISE_WITH_UID:
                return Contractor.ExerciseEntry.CONTENT_TYPE;
            case EXERCISE_WITH_CATEGORY_KEY:
                return Contractor.ExerciseEntry.CONTENT_TYPE;
            case EXERCISE_WITH_FKEY:
                return Contractor.ExerciseEntry.CONTENT_ITEM_TYPE;
            case EXERCISE_WITH_NAME:
                return Contractor.ExerciseEntry.CONTENT_ITEM_TYPE;
            case ROUTINE:
                return Contractor.RoutineEntry.CONTENT_TYPE;
            case ROUTINE_WITH_UID:
                return Contractor.RoutineEntry.CONTENT_TYPE;
            case ROUTINE_WITH_ROUTINE_NAME:
                return Contractor.RoutineEntry.CONTENT_TYPE;
            case ROUTINE_EXERCISE_WITH_ORDER_NUMBER:
                return Contractor.RoutineEntry.CONTENT_ITEM_TYPE;
            case ROUTINE_NAME:
                return Contractor.RoutineNameEntry.CONTENT_TYPE;
            case ROUTINE_NAME_WITH_UID:
                return Contractor.RoutineNameEntry.CONTENT_TYPE;
            case ROUTINE_NAME_WITH_NAME:
                return Contractor.RoutineNameEntry.CONTENT_ITEM_TYPE;
            case APPOINTMENT:
                return Contractor.AppointmentEntry.CONTENT_TYPE;
            case APPOINTMENT_WITH_DAY:
                return Contractor.AppointmentEntry.CONTENT_TYPE;
            case APPOINTMENT_WITH_CLIENT_KEY:
                return Contractor.AppointmentEntry.CONTENT_TYPE;
            case APPOINTMENT_WITH_FKEY:
                return Contractor.AppointmentEntry.CONTENT_ITEM_TYPE;
            case NOTES:
                return Contractor.NotesEntry.CONTENT_TYPE;
            case NOTES_WITH_UID:
                return Contractor.NotesEntry.CONTENT_TYPE;
            case NOTES_WITH_CLIENT_KEY:
                return Contractor.NotesEntry.CONTENT_TYPE;
            case NOTES_WITH_DATE:
                return Contractor.NotesEntry.CONTENT_TYPE;
            case NOTES_WITH_DATE_TIME:
                return Contractor.NotesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

}
