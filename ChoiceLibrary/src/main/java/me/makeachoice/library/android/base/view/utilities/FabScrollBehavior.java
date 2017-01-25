package me.makeachoice.library.android.base.view.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Usuario on 11/9/2016.
 */

public class FabScrollBehavior extends FloatingActionButton.Behavior {
    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
                                       final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {

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

        //if (dyConsumed > 0 && child.getVisibility() != View.VISIBLE) {
        if (target.getScrollY() > target.getBottom() && child.getVisibility() != View.VISIBLE) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            child.show();
        } else if (dyConsumed < 0 && child.getVisibility() == View.VISIBLE) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.hide();
        }
    }

}