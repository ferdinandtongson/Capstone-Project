package me.makeachoice.gymratpta.view.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Usuario on 7/12/2016.
 */
public class AppointmentWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppointmentWidgetFactory(this.getApplicationContext(), intent);
    }
}
