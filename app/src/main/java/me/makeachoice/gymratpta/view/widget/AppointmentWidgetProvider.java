package me.makeachoice.gymratpta.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.view.activity.SessionActivity;
import me.makeachoice.library.android.base.utilities.NetworkHelper;

/**
 * AppointmentWidgetProvider is responsible for creating the widget and all the corresponding views. It is
 * also responsible for responding to broadcast request
 */
public class AppointmentWidgetProvider extends AppWidgetProvider {

    private static int mWidgetId;

/**************************************************************************************************/
/**
 * Initialize Widget view methods
 *      void onReceive(Context,Intent) - called when a broadcast request is received
 *      void checkBroadcastReceived(Context,Intent) - check intent of broadcast
 *      void onUpdate(Context,AppWidgetManager,int[]) - Called in response to the broadcast request
 */
/**************************************************************************************************/
/**
 * void onReceive(Context,Intent) - called when a broadcast request is received. Checks if there
 * is a network available before checking on what broadcast was received
 * @param context - activity context
 * @param intent - intent of the broadcast
 */
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        //check if there is network connection
        if(NetworkHelper.hasConnection(context)){
            //have network, check intent of broadcast
            checkBroadcastReceived(context, intent);
        }
        else{
            //notify user that network is down
            sendToastMessage(context, context.getString(R.string.msg_no_network));
        }
    }

/**
 * void checkBroadcastReceived(Context,Intent) - check intent of broadcast
 * @param context - activity context
 * @param intent - intent of the broadcast
 */
    private void checkBroadcastReceived(Context context, Intent intent){
        //widget update intent, widget received appointment update
        String widgetUpdate = context.getString(R.string.action_widget_update);
        //widget refresh intent, widget requesting appointment updates
        String widgetRefresh = context.getString(R.string.action_widget_refresh);
        //app update intent, app notifying widget that appointments have been updated
        String appUpdate = context.getString(R.string.action_app_update);

        //get widget manager instance
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        //check if widgetId is zero
        if(mWidgetId == 0){
            //call onUpdate to get valid widget id
            ComponentName name = new ComponentName(context.getPackageName(),
                    "me.makeachoice.gymratpta.view.widget.AppointmentWidgetProvider");
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(name));
        }


        //get type of broadcast received
        if(intent.getAction().equals(widgetUpdate)){
            //firebase notification, notify user that appointments have been updated
            //sendToastMessage(context, context.getString("Appointments have been updated"));
            sendToastMessage(context, "Appointments updated!");

            //notify listView adapter to refresh data
            appWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.widgetApp_lstAppointments);
        }
        else if(intent.getAction().equals(appUpdate)){
            //notify listView adapter to refresh data
            appWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.widgetApp_lstAppointments);

        }
        else if(intent.getAction().equals(widgetRefresh)){

            //notify user that a appointment refresh has been requested
            //sendToastMessage(context, context.getString(R.string.str_refresh_requested));
            sendToastMessage(context, "Refreshing Appointments");

            //notify listView adapter to refresh data
            appWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.widgetApp_lstAppointments);

        }

    }

/**
 * void onUpdate(Context,AppWidgetManager,int[]) - Called in response to the
 * ACTION_APPWIDGET_UPDATE and ACTION_APPWIDGET_RESTORED broadcasts to provide RemoteViews of a
 * set of AppWidgets
 * @param context - activity context
 * @param appWidgetManager - widget manager
 * @param appWidgetIds - set of app widget id numbers
 */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        Log.d("Choice", "AppointmentWidgetProvider.onUpdate");

        //loop through set of app widgets
        for (int widgetId : appWidgetIds){
            //initialize widget GUI
            RemoteViews mView = initViews(context, widgetId);

            //update widget manager with updated widget
            appWidgetManager.updateAppWidget(widgetId, mView);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * Initialize Widget view methods
 *      RemoteViews initViews(Context,AppWidgetManager,int) - initialize the views in the widget
 *      void initTitle(Context,RemoteViews) - initialize Title textView
 *      void initRefreshButton(Context,RemoteViews,int) - initialize refresh button
 *      void initListView(Context,RemoteViews,int) - initialize listView for stock quotes
 */
/**************************************************************************************************/
/**
 * RemoteViews initViews(Context,AppWidgetManager,int) - initialize the views in the widget
 * @param context - activity context
 * @param widgetId - id number of widget
 * @return - widget layout view
 */
    private RemoteViews initViews(Context context, int widgetId){
        Log.d("Choice", "AppointmentWidgetProvider.initViews: " + widgetId);
        //save widgetId to static buffer
        mWidgetId = widgetId;

        //get the widget layout view
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_appointments);

        //initialize textView Title
        initTitle(context, view);

        //initialize imageView refresh button
        initRefreshButton(context, view, widgetId);

        //initialize listView of stock quotes
        initListView(context, view, widgetId);

        //return view
        return view;
    }

/**
 * void initTitle(Context,RemoteViews) - initialize Title textView. Create Intent and set intent
 * to start StockHawk application when textView Title is clicked
 * @param context - activity context
 * @param view - remoteViews, widget layout view holding the children of the widget
 */
    private void initTitle(Context context, RemoteViews view){
        Intent intent = new Intent(context, SessionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        view.setOnClickPendingIntent(R.id.widgetApp_txtTitle, pendingIntent);
    }

/**
 * void initRefreshButton(Context,RemoteViews,int) - initialize refresh quotes button.
 * Create Intent and set the intent to broadcast to refresh the stock quotes when imageView is
 * clicked
 * @param context - activity context
 * @param view - remoteViews, widget layout view holding the children of the widget
 */
    private void initRefreshButton(Context context, RemoteViews view, int widgetId){

        //get intent action string
        String actionRefresh = context.getString(R.string.action_widget_refresh);

        //create Intent to refresh quotes in widget
        Intent onClickIntent = new Intent(actionRefresh);

        //add widget id to intent
        onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        //create pending intent to broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,onClickIntent,0);

        //attach pending intent to refresh button
        view.setOnClickPendingIntent(R.id.widgetApp_imgRefresh, pendingIntent);
    }

/**
 * void initListView(Context,RemoteViews,int) - initialize listView for appointments. Create
 * intent to start a Service for the remote adapter.
 * @param context - activity context
 * @param view - remoteViews, widget layout view holding the children of the widget
 * @param widgetId - id number of widget
 */
    private void initListView(Context context, RemoteViews view, int widgetId){
        //create Intent to start service for remote adapter
        Intent intent = new Intent(context, AppointmentWidgetService.class);

        //add widget id to intent
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        //set data schemer for intent
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        //attach intent to listView for stock quotes
        view.setRemoteAdapter(R.id.widgetApp_lstAppointments, intent);

        //set empty view text message
        setEmptyTextViewMessage(context, view);

        //set empty view for listView
        view.setEmptyView(R.id.widgetApp_lstAppointments, R.id.widgetApp_txtEmpty);


    }

/**************************************************************************************************/

    private void setEmptyTextViewMessage(Context context, RemoteViews view){

        //set empty view text message
        view.setTextViewText(R.id.widgetApp_txtEmpty, noDataMessage(context));

    }

    private String noDataMessage(Context context){

        //check if we have connection
        if(NetworkHelper.hasConnection(context)) {
            //we have connection just no data
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user == null){
                return context.getString(R.string.msg_please_login);
            }
            else{
                return context.getString(R.string.no_sessions);
            }
        }
        else{
            //we have no network connection
            return context.getString(R.string.msg_short_no_network);
        }
    }

    private void sendToastMessage(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }



}
