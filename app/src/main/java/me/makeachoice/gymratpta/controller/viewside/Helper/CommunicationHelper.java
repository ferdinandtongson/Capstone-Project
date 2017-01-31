package me.makeachoice.gymratpta.controller.viewside.Helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import me.makeachoice.gymratpta.R;

/**
 * Created by Usuario on 1/30/2017.
 */

public class CommunicationHelper {

    public static void sendEmail(Activity activity, String email){
        //get string resource values
        String strChooserHeader = activity.getResources().getString(R.string.msgEmail_send_using);
        String strNoClient = activity.getResources().getString(R.string.msgEmail_no_client);

        //create email intent
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        emailIntent.setType("text/plain");
        emailIntent.setType("message/rfc822");

        try {
            //start email chooser with header
            activity.startActivity(Intent.createChooser(emailIntent, strChooserHeader));
        } catch (android.content.ActivityNotFoundException ex) {
            //notify user no email client available
            Toast.makeText(activity, strNoClient, Toast.LENGTH_SHORT).show();
        }
    }

    public static void makeCall(Activity activity, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);

        //important - "tel: " must prefix number
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }
}
