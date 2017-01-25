package me.makeachoice.library.android.base.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**************************************************************************************************/
/*
 *  TODO - look and test configuration changes
 *  Configuration Change: change in orientation, language, input devices, etc causes current activity
 *      to be destroyed, going through onPuase(), onStop(), and onDestroy()
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyActivity abstract class extends AppCompatActivity.
 *
 * Activity is the base class of all other activities and the relationship among them is:
 *      Activity <-- FragmentActivity <-- AppCompatActivity <-- ActionBarActivity
 *
 * "<--" means inherits from; also ActionBarActivity is depreciated.
 *      Activity - is the base
 *      FragmentActivity - provides the ability to use Fragment
 *      AppComptActivity - provides features to ActionBar
 *
 */
/**************************************************************************************************/

public abstract class MyActivity extends AppCompatActivity {

/**************************************************************************************************/
/*
 * Class Variables
 *      Bridge mBridge - class implementing Bridge interface
 *      OptionsMenuBridge mOptionsMenuBridge - class implementing OptionsMenuBridge interface
 *
 * Bridge Interface:
 *      void create(MyActivity,Bundle) - called by onCreate(Bundle) in the activity lifecycle
 *      void start() - called by onStart() in the activity lifecycle
 *      void resume() - called by onResume() in the activity lifecycle
 *      void pause() - called by onPause() in the activity lifecycle
 *      void stop() - called by onStop() in the activity lifecycle
 *      void destroy() - called by onDestroy() in the activity lifecycle
 *      void backPressed() - called by onBackPressed() in the activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      void activityResult(...) - result returned from Activity called by this Activity
 *
 * OptionsMenuBridge Interface:
 *      void createOptionsMenu(Menu) - called by onCreateOptionsMenu(Menu) if Options Menu exist
 *      void optionsItemSelected(MenuItem) - called by onOptionsItemSelected(MenuItem) when a menu
 *          item is clicked
 */
/**************************************************************************************************/

    //mBridge - class implementing Bridge interface
    protected Bridge mBridge;

    //mOptionsMenuBridge - class implementing OptionsMenuBridge interface
    protected OptionsMenuBridge mOptionsMenuBridge;


    //Implemented communication line, usually implemented by a HouseKeeper class
    public interface Bridge{
        //called by onCreate(Bundle) in the activity lifecycle
        void create(MyActivity activity, Bundle bundle);

        //called by onStart() in the activity lifecycle
        void start();

        //called by onResume() in the activity lifecycle
        void resume();

        //called by onPause() in the activity lifecycle
        void pause();

        //called by onStop() in the activity lifecycle
        void stop();

        //called by onDestroy() in the activity lifecycle
        void destroy();

        //called by onBackPressed() in the activity
        void backPressed();

        //called before onDestroy(), save state to bundle
        void saveInstanceState(Bundle bundle);

        //result returned from Activity called by this Activity
        void activityResult(int requestCode, int resultCode, Intent data);
    }

    //Implemented communication line to Options Menu events
    public interface OptionsMenuBridge{
        //called by onCreateOptionsMenu(Menu) if Options Menu is present
        void createOptionsMenu(Menu menu);

        //called by onOptionsItemSelected(MenuItem) when a menu item is clicked
        boolean optionsItemSelected(MenuItem item);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void onStart() - called when activity is visible to user
 *      void onResume() - called when user can start interacting with activity
 *      void onPause() - called when activity is going into the background
 *      void onStop() - called when activity is no longer visible to user
 *      void onDestroy() - called by finish() or system is saving space
 *      void onBackPressed() - called when the User presses the "Back" button
 *      void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      void onActivityResult(...) - result of Activity called by this Activity
 *      void finishActivity() - closes the Activity
 */
/**************************************************************************************************/
    /*
     * void onStart() - called when activity is visible to user. Place to register broadcast receivers
     */
    @Override
    public void onStart(){
        super.onStart();
        mBridge.start();
    }

    /*
     * void onResume() - called when user can start interacting with activity. Place to begin animation
     *      open exclusive-access devices (such as cameras) and restore activity state, if any.
     */
    @Override
    public void onResume(){
        super.onResume();
        mBridge.resume();
    }


    /*
     * void onPause() - called when activity is going into the background. Place to do lightweight
     *      operations, new activity will not be created until this activity completes onPause(), such
     *      as saving persistent state or stopping animations or other things to make the switch to
     *      the next activity as fast as possible.
     */
    @Override
    public void onPause(){
        super.onPause();
        mBridge.pause();
    }

    /*
     * void onStop() - called when activity is no longer visible to user. Place to unregister broadcast
     *      receivers.
     */
    @Override
    public void onStop() {
        super.onStop();
        mBridge.stop();
    }

    /*
     * void onDestroy() - called by finish() or system is saving space. Use for any final cleanup
     */
    @Override
    public void onDestroy(){
        mBridge.destroy();
        super.onDestroy();
    }

    /*
     * void onBackPressed() - called when the User presses the "Back" button
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBridge.backPressed();
    }

    /*
     * void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
     * @param bundle - bundle object used to save any instance states
     */
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mBridge.saveInstanceState(bundle);
    }

    /*
     * void onActivityResult(...) - result of Activity called by this Activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mBridge.activityResult(requestCode, resultCode, data);
    }

    /*
     * void finishActivity() - closes the Activity
     */
    public void finishActivity(){
        this.finish();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods for OptionsMenu
 *      boolean onCreateOptionsMenu(Menu) - called by onCreateOptionsMenu() in Activity lifecycle
 *      boolean onOptionsItemSelected(MenuItem) - called when user click on a Menu option in toolbar
 *      void setOptionsMenuBridge(OptionsMenuBridge) - add bridge communication to options events
 */
/**************************************************************************************************/
    /*
     * boolean onCreateOptionsMenu(Menu) - called by onCreateOptionsMenu() in Activity lifecycle
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(mOptionsMenuBridge != null){
            mOptionsMenuBridge.createOptionsMenu(menu);
        }
        return true;
    }

    /*
     * boolean onOptionsItemSelected(MenuItem) - called when user click on a Menu option in toolbar
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        super.onOptionsItemSelected(item);
        if(mOptionsMenuBridge != null){
            mOptionsMenuBridge.optionsItemSelected(item);
        }
        return true;
    }

    /*
     * void setOptionsMenuBridge(OptionsMenuBridge) - add bridge communication to options events
     */
    public void setOptionsMenuBridge(OptionsMenuBridge menuBridge){
        mOptionsMenuBridge = menuBridge;
    }

/**************************************************************************************************/

}
