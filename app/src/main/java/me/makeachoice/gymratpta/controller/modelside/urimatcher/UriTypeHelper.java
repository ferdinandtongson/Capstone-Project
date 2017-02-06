package me.makeachoice.gymratpta.controller.modelside.urimatcher;

import android.net.Uri;

import me.makeachoice.gymratpta.model.contract.Contractor;

import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CATEGORY;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CATEGORY_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CATEGORY_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CATEGORY_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CLIENT;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CLIENT_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CLIENT_WITH_STATUS;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.CLIENT_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.EXERCISE;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.EXERCISE_WITH_CATEGORY_KEY;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.EXERCISE_WITH_FKEY;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.EXERCISE_WITH_NAME;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.EXERCISE_WITH_UID;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.USER;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.USER_WITH_KEY;
import static me.makeachoice.gymratpta.controller.modelside.urimatcher.DBUriMatcher.dbUriMatcher;

/**
 * Created by Usuario on 2/6/2017.
 */

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
                return Contractor.ClientEntry.CONTENT_TYPE;
            case EXERCISE_WITH_UID:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case EXERCISE_WITH_CATEGORY_KEY:
                return Contractor.ClientEntry.CONTENT_TYPE;
            case EXERCISE_WITH_FKEY:
                return Contractor.ClientEntry.CONTENT_ITEM_TYPE;
            case EXERCISE_WITH_NAME:
                return Contractor.ClientEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

}
