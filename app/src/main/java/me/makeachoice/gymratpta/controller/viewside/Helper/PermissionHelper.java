package me.makeachoice.gymratpta.controller.viewside.Helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import me.makeachoice.gymratpta.R;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * TODO - toast is not showing, need to debug
 */
/**************************************************************************************************/

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
    public static final int READ_CONTACTS_REQUEST = 1;

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
     * void requestPermission(int) - request permission from user
     */
    public void requestPermission(int request){
        Log.d("Choice", "PermissionHelper: " + request);
        switch(request){
            case ALL_REQUEST:
                //request permission for all features
                getPermissionToReadContacts();
                break;
            case READ_CONTACTS_REQUEST:
                //request permission to read contacts info
                getPermissionToReadContacts();
                break;
        }
    }

    /*
     * void getPermissionToReadContacts() - request permission to read contacts info. Permission
     * request is only valid for Marshmallow (api 23, 6.0)
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissionToReadContacts() {
        //check if permission has NOT been granted
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            //check if user has denied request
            if (mActivity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                //get dialog title
                String title = mActivity.getString(R.string.permission_request);
                //get dialog message
                String msg = mActivity.getString(R.string.msg_permissionRequest_readContacts);

                //show explanation dialog
                showExplanation(title, msg, Manifest.permission.READ_CONTACTS, READ_CONTACTS_REQUEST);
            }

            //show permission request dialog with async handler
            requestPermission(Manifest.permission.READ_CONTACTS, READ_CONTACTS_REQUEST);
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
    private  void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{permissionName}, permissionRequestCode);
    }

    /*
     * void showExplanation(...) - show alert dialog explaining permission request
     */
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        Log.d("Choice", "PermissionHelper.showExplanation");
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
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
    /*
     * void onRequestPermissionResult() - async result of permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String granted = mActivity.getString(R.string.msg_permissionRequest_readContacts_granted);
                Toast.makeText(mActivity, granted, Toast.LENGTH_SHORT).show();
            } else {
                String denied = mActivity.getString(R.string.msg_permissionRequest_readContacts_denied);
                Toast.makeText(mActivity, denied, Toast.LENGTH_SHORT).show();
            }

        }
    }

/**************************************************************************************************/

}
