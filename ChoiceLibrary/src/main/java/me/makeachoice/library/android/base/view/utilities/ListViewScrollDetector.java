package me.makeachoice.library.android.base.view.utilities;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;

/**************************************************************************************************/
/*
 * ListViewScrollDetector abstract class used to detect listView scroll events
 */
/**************************************************************************************************/

public abstract class ListViewScrollDetector implements AbsListView.OnScrollListener {

/**************************************************************************************************/
/*
 * Class Variables:
 *      AbsListView mListView - listView base class
 *      int mScrollThreshold - threshold amount need to trigger an onScrollUp or onScrollDown event
 *      int mLastScrollY - last top item scroll "y" value
 *      int mPreviousFirstVisibleItem - previous first visible item in listView
 */
/**************************************************************************************************/

    //mListView - listView base class
    private AbsListView mListView;

    //mScrollThreshold - threshold amount need to trigger an onScrollUp or onScrollDown event
    private int mScrollThreshold;

    //mLastScrollY - last top item scroll "y" value
    private int mLastScrollY;

    //mPreviousFirstVisibleItem - previous first visible item in listView
    private int mPreviousFirstVisibleItem;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      int getTopItemScrollY() - get top position of the first visible item in the ListView
 */
/**************************************************************************************************/
    /*
     * int getTopItemScrollY() - get top position of the first visible item in the ListView
     * @return - top position of view
     */
    private int getTopItemScrollY() {
        //make sure listView and first item in listView is not null, else return 0
        if (mListView == null || mListView.getChildAt(0) == null) return 0;

        //get first item in listView
        View topChild = mListView.getChildAt(0);

        //return top position of view item
        return topChild.getTop();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setters:
 *      void setListView(AbsListView) - set base ListView class object
 */
/**************************************************************************************************/
    /*
     * void setListView(AbsListView) - set base ListView class object
     * @param listView - listView object
     */
    public void setListView(@NonNull AbsListView listView) {
    mListView = listView;
}

    /*
     * void setScrollThreshold(int) - set the displacement number to trigger an onScroll event
     * @param scrollThreshold - displacement needed to trigger an onScroll event
     */
    public void setScrollThreshold(int scrollThreshold) {
        //set scroll threshold
        mScrollThreshold = scrollThreshold;
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Class Methods:
 *      void onScroll(AbsListView,int,int,int) - detect onScrollUp or onScrollDown event in listView.
 *      void onScrollStateChanged(AbsListView,int) - does nothing
 *      boolean isSameRow(int) - compares previous and current first visible list item.
 */
/**************************************************************************************************/
    /*
     * void onScroll(AbsListView,int,int,int) - detect onScrollUp or onScrollDown event in listView.
     * Filter accidental scroll / swipe events by using a scrolling threshold, if the threshold is
     * met or exceeded, the change in the y-axis (dy) is checked and either a scroll up or down
     * event is sent.
     * @param view - listView this listener is attached to
     * @param firstVisibleItem - get position number of first visible item in listView
     * @param visibleItemCount - get total number of visible items in listView
     * @param totalItemCount - get total number of items in listView
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //if there are no items in list, end method
        if(totalItemCount == 0) return;

        //if there are items in list, check if first visible item in list has changed
        if (isSameRow(firstVisibleItem)) {
            //if same, check top position of first visible item to see if scroll threshold is met
            int newScrollY = getTopItemScrollY();

            //check if scroll threshold is met
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;

            //if change is significant
            if (isSignificantDelta) {
                //check scroll direction
                if (mLastScrollY > newScrollY) {
                    //scroll up event
                    onScrollUp();
                } else {
                    //scroll down event
                    onScrollDown();
                }
            }

            //set last scroll top position
            mLastScrollY = newScrollY;
        } else {
            //first visible item in list has changed, check change direction
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                //first visible item is greater, scroll up event
                onScrollUp();
            } else {
                //first visible item is less, scroll down event
                onScrollDown();
            }

            //save top position of first visible item
            mLastScrollY = getTopItemScrollY();

            //save item position of first visible item
            mPreviousFirstVisibleItem = firstVisibleItem;
        }
    }

    /*
     * void onScrollStateChanged(AbsListView,int) - does nothing
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //does nothing
    }

    /*
     * boolean isSameRow(int) - compares previous and current first visible list item.
     * @return - true, if previous and current items are the same
     */
    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }


/**************************************************************************************************/
/*
 * Abstract Methods:
 *      void onScrollUp() - a scroll up event occurred
 *      void onScrollDown() - a scroll down event occurred
 *      void scrolledToLastItem(boolean) - detect if scroll event scrolled to the last item
 *      void setScrollThreshold() - set scroll threshold
 */
/**************************************************************************************************/

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    public abstract void scrolledToLastItem(boolean lastItem);

    public abstract void setScrollThreshold();

/**************************************************************************************************/

}