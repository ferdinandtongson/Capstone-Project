package me.makeachoice.gymratpta.utilities;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Usuario on 11/9/2016.
 */

public class FabRecyclerScrollBehavior extends FloatingActionButton.Behavior{

    public FabRecyclerScrollBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
                                       final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        Log.d("Choice", "FabRecyclerScrollBehavior");
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
                               final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed);

        //get recyclerView component
        RecyclerView recycler = (RecyclerView)target;

        //get scrollOffset
        LinearLayoutManager lm = (LinearLayoutManager)recycler.getLayoutManager();
        int pos = lm.findFirstVisibleItemPosition();
        int last = lm.findLastCompletelyVisibleItemPosition();
        int showFabAt = lm.getItemCount() - 1;

        //if (dyConsumed > 0 && child.getVisibility() != View.VISIBLE) {
        if (last == showFabAt && child.getVisibility() != View.VISIBLE) {
            // User scrolled down and the FAB is currently not visible -> show the FAB
            child.show();

            //wait 3 seconds, then hid the FAB
            child.postDelayed(new Runnable() { public void run() { child.hide(); } }, 3000);

        } else if (dyConsumed < 0 && child.getVisibility() == View.VISIBLE) {
            // User scrolled up and the FAB is currently visible -> hide the FAB
            child.hide();

        }
    }

}