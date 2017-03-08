package me.makeachoice.gymratpta.controller.viewside.Helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import me.makeachoice.gymratpta.R;
import me.makeachoice.library.android.base.view.activity.MyActivity;


/**************************************************************************************************/
/*
 * PermissionHelper helps in requesting permission to be granted by the user.
 */
/**************************************************************************************************/

public class PermissionHelper implements ActivityCompat.OnRequestPermissionsResultCallback{

/**************************************************************************************************/
/*
 * Class Variables:
 *      ALL_REQUEST - identifier for requesting all permissions
 *      READ_CONTACTS_PERMISSIONS_REQUEST - identifier for request contact permission
 */
/**************************************************************************************************/

    //ALL_REQUEST - identifier for requesting all permissions
    public static final int ALL_REQUEST = 0;

    //READ_CONTACTS_PERMISSIONS_REQUEST - identifier for request contact permission
    public static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    private MyActivity mActivity;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * PermissionHelper - constructor
 */
/**************************************************************************************************/

    public PermissionHelper(MyActivity activity){
        mActivity = activity;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Permission Methods
 *      void requestPermission(int) - request permission from user
 *      void getPermissionToReadContacts() - request permission to read contacts info
 */
/**************************************************************************************************/

    /*
     * void getPermissionToReadContacts() - request permission to read contacts info. Permission
     * request is only valid for Marshmallow (api 23, 6.0)
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadContacts() {
        //check if permission has NOT been granted
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            //show permission request dialog with async handler
            requestReadContactsPermission(Manifest.permission.READ_CONTACTS, READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getFragmentPermissionToReadContacts(Fragment fragment) {
        //check if permission has NOT been granted
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            //show permission request dialog with async handler
            requestReadContactsFragmentPermission(fragment, Manifest.permission.READ_CONTACTS,
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void requestPermission(...) - start async permission request
 *      void showExplanation(...) - show alert dialog explaining permission request
 *      void onRequestPermissionResult() - async result of permission request
 */
/**************************************************************************************************/
    /*
     * void requestPermission(...) - start async permission request
     */
    @TargetApi(Build.VERSION_CODES.M)
    public  void requestReadContactsPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{permissionName}, permissionRequestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public  void requestReadContactsFragmentPermission(Fragment frag, String permissionName, int permissionRequestCode) {
        FragmentCompat.requestPermissions(frag,
                new String[]{permissionName}, permissionRequestCode);
    }

    /*
     * void showExplanation(...) - show alert dialog explaining permission request
     */
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestReadContactsPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void onRequestPermissionResult() - async result of permission request
 */
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
                    boolean showRationale = mActivity.shouldShowRequestPermissionRationale(permission);
                    if (! showRationale) {
                        //user does NOT want to see rational,
                    } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                        //get dialog title
                        String title = mActivity.getString(R.string.permission_request);
                        //get dialog message
                        String msg = mActivity.getString(R.string.msg_permissionRequest_readContacts);
                        showExplanation(title, msg, Manifest.permission.READ_CONTACTS, READ_CONTACTS_PERMISSIONS_REQUEST);
                    }
                }
            }
        }
    }
/**************************************************************************************************/

}
