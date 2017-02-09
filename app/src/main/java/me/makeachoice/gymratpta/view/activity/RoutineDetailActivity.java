package me.makeachoice.gymratpta.view.activity;


import android.os.Bundle;

import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.manager.HouseKeeperRegistry;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  TODO - add activity description
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * TODO - add activity description,
 * the HouseKeeper does most of the work and has a bridge interface to most of the Lifecycle events
 * that happen in MyActivity (ie. onStart, onStop, etc). So all the activity needs to do is get a
 * HouseKeeper. The main purpose for this class is so that it's registered to AndroidManifest
 *
 * MyActivity Class Variables
 *      Bridge mBridge - class implementing Bridge interface
 *      OptionsMenuBridge mOptionsMenuBridge - class implementing OptionsMenuBridge interface
 *
 * MyActivity Inherited Methods:
 *      void onStart() - called when activity is visible to user
 *      void onResume() - called when user can start interacting with activity
 *      void onPause() - called when activity is going into the background
 *      void onStop() - called when activity is no longer visible to user
 *      void onDestroy() - called by finish() or system is saving space
 *      void onBackPressed() - called when the User presses the "Back" button
 *      void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      void onActivityResult(...) - result of Activity called by this Activity
 *      void finishActivity() closes the Activity
 *
 * MyActivity Inherited Methods for OptionsMenu
 *      boolean onCreateOptionsMenu(Menu) - called by onCreateOptionsMenu() in Activity lifecycle
 *      boolean onOptionsItemSelected(MenuItem) - called when user click on a Menu option in toolbar
 *      void setOptionsMenuBridge(OptionsMenuBridge) - add bridge communication to options events
 *
 */
/**************************************************************************************************/

public class RoutineDetailActivity extends MyActivity {

/**************************************************************************************************/
/*
 * Initialization Methods:
 *      void onCreate(Bundle) - called when activity is first created
 */

    /**************************************************************************************************/
    /*
     * void onCreate(Bundle) - called when activity is first created. Get Boss application class to
     *      set activity context and get HouseKeeper class for this activity.
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //get Boss Application
        Boss mBoss = (Boss) getApplicationContext();

        //register Activity context with Boss
        mBoss.setActivity(this);

        try {
            //check if HouseKeeper is implementing interface
            mBridge = (Bridge) mBoss.requestHouseKeeper(HouseKeeperRegistry.KEEPER_ROUTINE_DETAIL);
        } catch (ClassCastException e) {
            throw new ClassCastException("HouseKeeper must implement Bridge interface");
        }

        //use HouseKeeper class to create activity
        mBridge.create(this, bundle);
    }

/**************************************************************************************************/

}