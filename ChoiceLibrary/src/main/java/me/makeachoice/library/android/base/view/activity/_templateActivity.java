package me.makeachoice.library.android.base.view.activity;

import android.os.Bundle;

/**************************************************************************************************/
/*
 *  TODO - if first activity, need to modify AndroidManifest in <activity>
 *      <activity android:name=".view.activity.MainActivity">
 *  TODO - create Boss class
 *  TODO - if first activity, need to modify AndroidManifest in <application ....>
 *      <application....
 *          android:name=".controller.Boss"
 *          android:theme="@style/ChoiceTheme.Activity">
 *  TODO - if first activity, need to modify app build.gradle in dependencies
 *      dependencies{....
 *      compile(name: 'ChoiceLibrary', ext: 'aar') ....}
 *  TODO - if first activity, need to modify <YourAppName> build.gradle in allprojects
 *      allprojects{
 *          repositories {
 *              jcenter()
 *              flatDir {
 *                 dirs 'libs'
 *             }
 *         }
 *      }
 *  TODO - add activity description
 *  TODO - create HouseKeeper class, then come back
 *  TODO - uncomment after Boss is created
 *  TODO - need to register HouseKeeper in Boss to be assigned to this activity
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

public class _templateActivity extends MyActivity {

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

        //TODO - uncomment after Boss is created
        //get Boss Application
        //Boss mBoss = (Boss) getApplicationContext();

        //register Activity context with Boss
        //mBoss.setActivityContext(this);

        try {
            //TODO - need to create HouseKeeper to act as bridge
            //check if HouseKeeper is implementing interface
            //mBridge = (Bridge) mBoss.requestHouseKeeper(_tempKeeper.KEEPER_ID);
        } catch (ClassCastException e) {
            throw new ClassCastException("HouseKeeper must implement Bridge interface");
        }

        //use HouseKeeper class to create activity
        mBridge.create(this, bundle);
    }

/**************************************************************************************************/

}
