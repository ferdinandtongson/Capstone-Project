package me.makeachoice.gymratpta.view.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ScheduleButler;
import me.makeachoice.gymratpta.controller.modelside.query.ScheduleQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.db.DBHelper;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;

import static me.makeachoice.gymratpta.R.string.today;

/**
 * AppointmentWidgetFactory create remoteViews for a listView defined in AppointmentWidgetProvider
 */
public class AppointmentWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    private ArrayList<ScheduleItem> mSchedules;

    private Context mContext;
    private String mMsgEmpty;

    public AppointmentWidgetFactory(Context context, Intent intent){
        Log.d("Choice", "AppointmentWidgetFactory");
        mContext = context;


        mSchedules = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mSchedules.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        ScheduleItem item = mSchedules.get(position);

        RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.item_appointment_small);
        mView.setTextViewText(R.id.txtClientName, item.clientName);
        mView.setTextViewText(R.id.txtTime, DateTimeHelper.convert24Hour(item.appointmentTime));

        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        Log.d("Choice", "ScheduleWidgetFactory.onCreate");
        initData();
    }

    @Override
    public void onDataSetChanged() {
        Log.d("Choice", "ScheduleWidgetFactory.onDataSetChanged");
        initData();
    }



    @Override
    public void onDestroy() {
    // Stop the cursor loader
    }

/**************************************************************************************************/

/**************************************************************************************************/
/**
 * Initialize Data
 */
/**************************************************************************************************/
public void initStubData(){
    ScheduleItem item01 = new ScheduleItem();
    item01.clientName = "Quess Starbringer";
    item01.appointmentTime = "09:00 - 10:00";
    ScheduleItem item02 = new ScheduleItem();
    item02.clientName = "Ferdinand Tongson";
    item02.appointmentTime = "11:00 - 12:00";
    ScheduleItem item03 = new ScheduleItem();
    item03.clientName = "Christine Newman";
    item03.appointmentTime = "15:00 - 16:00";
    ScheduleItem item04 = new ScheduleItem();
    item04.clientName = "Anthony Merra";
    item04.appointmentTime = "19:00 - 20:00";

    mSchedules.add(item01);
    mSchedules.add(item02);
    mSchedules.add(item03);
    mSchedules.add(item04);

}


    private void initData(){
        //clear schedule list
        mSchedules.clear();

        Log.d("Choice", "WidgetFactory.initData");
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fbAuth.getCurrentUser();

        //check if user is logged in
        if(user != null){
            //get userid
            String userId = user.getUid();

            //get database helper
            DBHelper dbHelper = new DBHelper(mContext);

            //open sqlite
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            //get schedule timestamp selection
            String selection = ScheduleQueryHelper.timestampSelection;

            //get the datestamp for today
            String today = DateTimeHelper.getDatestamp(0);

            //run query on schedule table
            Cursor c = db.query(ScheduleContract.TABLE_NAME, ScheduleContract.PROJECTION, selection,
                    new String[]{userId, today},
                    null, null, ScheduleContract.SORT_ORDER_TIME_CLIENT);

            //check size of cursor
            int count = c.getCount();

            //loop through cursor
            for(int i = 0; i < count; i++){
                c.moveToPosition(i);
                ScheduleItem item = new ScheduleItem(c);
                mSchedules.add(item);
            }

            if(mSchedules.isEmpty()){
                mMsgEmpty = "No Appointments";
            }

            c.close();
            dbHelper.close();
        }
        else{
            mMsgEmpty = "Not Logged In";
        }

    }




}
