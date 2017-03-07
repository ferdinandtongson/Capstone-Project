package me.makeachoice.gymratpta.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.gymratpta.controller.manager.HouseKeeperRegistry;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.viewside.Helper.PermissionHelper.READ_CONTACTS_PERMISSIONS_REQUEST;

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

public class ClientActivity extends MyActivity {

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
            mBridge = (Bridge) mBoss.requestHouseKeeper(HouseKeeperRegistry.KEEPER_CLIENT);
        } catch (ClassCastException e) {
            throw new ClassCastException("HouseKeeper must implement Bridge interface");
        }

        //use HouseKeeper class to create activity
        mBridge.create(this, bundle);
    }

/**************************************************************************************************/

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                //get permission
                String permission = permissions[i];

                //check if user has denied request
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (! showRationale) {
                        //user does NOT want to see rational,
                    } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                        //get dialog title
                        String title = getString(R.string.permission_request);
                        //get dialog message
                        String msg = getString(R.string.msg_permissionRequest_readContacts);
                        showExplanation(title, msg, Manifest.permission.READ_CONTACTS, READ_CONTACTS_PERMISSIONS_REQUEST);
                    }
                }
            }
        }
    }

    /*
     * void showExplanation(...) - show alert dialog explaining permission request
     */
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        Log.d("Choice", "PermissionHelper.showExplanation");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestReadContactsPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    /*
     * void requestPermission(...) - start async permission request
     */
    @TargetApi(Build.VERSION_CODES.M)
    public  void requestReadContactsPermission(String permissionName, int permissionRequestCode) {
        Log.d("Choice", "PermissionHelper.requestPermission - ActivityCompat.");
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }

}
