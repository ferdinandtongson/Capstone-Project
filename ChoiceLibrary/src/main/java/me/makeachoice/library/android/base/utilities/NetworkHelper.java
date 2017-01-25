package me.makeachoice.library.android.base.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

import me.makeachoice.library.android.base.R;

import static android.R.attr.gravity;
import static android.R.id.message;

/**************************************************************************************************/
/*
 * NetworkHelper checks for network connectivity
 */

/**************************************************************************************************/

public final class NetworkHelper {

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    private static int STR_REFRESH_ID = R.string.dia_btn_refresh;
    private static int STR_CLOSE_ID = R.string.dia_btn_close;
    private static int STR_NO_NETWORK_ID = R.string.msg_no_network;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * NetworkHelper - constructor
 */
/**************************************************************************************************/
    public NetworkHelper(){}

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Class Methods:
 *      boolean hasConnection() - check if phone has network connection
 */
/**************************************************************************************************/
    /*
     * boolean hasConnection() - check if phone has network connection. If not it will notify the
     * user of the situation.
     * @return - status of network connection, true or false
     */
    public static boolean hasConnection(Context context){

        //get Connectivity Manger
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //get access to network information from phone
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        //check if we have connection
        if(networkInfo != null && networkInfo.isConnected()) {
            //we have connection, return true
            return true;
        }
        else{
            //we have no network connection, return false
            return false;
        }

    }

    /*
     * void showNoNetworkDialog(Context) - show AlertDialog telling user there is no network
     * connection
     * @param context - activity context
     */
    public static void showNoNetworkDialog(Context context){
        String noNetwork = context.getString(STR_NO_NETWORK_ID);

        //no connection, create alert dialog builder
        AlertDialog.Builder bldDialog = new AlertDialog.Builder(context);
        bldDialog.setMessage(noNetwork);
        bldDialog.setCancelable(true);

        //create alert dialog
        AlertDialog diaNoNetwork = bldDialog.create();

        //show dialog
        diaNoNetwork.show();
    }

    /*
     * void showNoNetworkDialog(Context,String) - show AlertDialog telling user there is no network
     * connection
     * @param context - activity context
     * @param message - alert dialog message
     */
    public static void showNoNetworkDialog(Context context, String message){

        //no connection, create alert dialog builder
        AlertDialog.Builder bldDialog = new AlertDialog.Builder(context);
        bldDialog.setMessage(message);
        bldDialog.setCancelable(true);

        //create alert dialog
        AlertDialog diaNoNetwork = bldDialog.create();

        //show dialog
        diaNoNetwork.show();
    }

    /*
     * void showNoNetworkDialog(Context,DialogInterface.OnClickListener,
     * DialogInterface.OnClickListener) - show AlertDialog telling user there is no network
     * connection
     * @param context - activity context
     * @param refreshListener - refresh button
     * @param closeListener - close button
     */
    public static void showNoNetworkDialog(Context context,
                                           DialogInterface.OnClickListener refreshListener,
                                           DialogInterface.OnClickListener closeListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ChoiceTheme_Dialog_Alert);

        String refresh = context.getString(STR_REFRESH_ID);
        String close = context.getString(STR_CLOSE_ID);
        String noNetwork = context.getString(STR_NO_NETWORK_ID);
        builder.setMessage(noNetwork)
                .setCancelable(false)
                .setPositiveButton(refresh, refreshListener)
                .setNegativeButton(close, closeListener);
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*
     * void showNoNetworkDialog(Context,String,DialogInterface.OnClickListener,
     * DialogInterface.OnClickListener) - show AlertDialog telling user there is no network
     * connection
     * @param context - activity context
     * @param message - alert dialog message
     * @param refreshListener - refresh button
     * @param closeListener - close button
     */
    public static void showNoNetworkDialog(Context context, String message,
                                                  DialogInterface.OnClickListener refreshListener,
                                                  DialogInterface.OnClickListener closeListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ChoiceTheme_Dialog_Alert);

        String refresh = context.getString(STR_REFRESH_ID);
        String close = context.getString(STR_CLOSE_ID);
        String noNetwork = context.getString(STR_NO_NETWORK_ID);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(refresh, refreshListener)
                .setNegativeButton(close, closeListener);
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*
     * void showNoNetworkToast(...) - show toast message, no network connection
     */
    public static void showNoNetworkToast(Context context, int duration){
        String noNetwork = context.getString(STR_NO_NETWORK_ID);
        Toast.makeText(context, noNetwork, duration).show();
    }

    /*
     * void showNoNetworkToast(...) - show toast message, no network connection
     */
    public static void showNoNetworkToast(Context context, String message, int duration){
        Toast.makeText(context, message, duration).show();

    }

    /*
     * void showNoNetworkToast(...) - show toast message, no network connection
     */
    public static void showNoNetworkToast(Context context, String message, int duration,
                                          int gravity, int xOffset, int yOffset){
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();

    }

/**************************************************************************************************/

}
