package me.makeachoice.library.android.base.view.utilities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**************************************************************************************************/
/*
 * RecyclerViewScrollDetector abstract class used to detect recyclerView scroll events
 */
/**************************************************************************************************/

public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int mScrollThreshold - threshold amount need to trigger an onScrollUp or onScrollDown event
 */
/**************************************************************************************************/

    //mScrollThreshold - threshold amount need to trigger an onScrollUp or onScrollDown event
    private int mScrollThreshold;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setters:
 *      void setScrollThreshold(int) - set the displacement number to trigger an onScroll event
 */
/**************************************************************************************************/
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
 *      void onScrolled(RecyclerView,int,int) - detect onScrollUp or onScrollDown event
 *      void onScrollStateChanged(RecyclerView,int) - detect if scrolled to the last item
 */
/**************************************************************************************************/
    /*
     * void onScrolled(RecyclerView,int,int) - detect onScrollUp or onScrollDown event in RecyclerView.
     * Filter accidental scroll / swipe events by using a scrolling threshold, if the threshold is met
     * or exceeded, the change in the y-axis (dy) is checked and either a scroll up or down event is
     * sent.
     * @param recyclerView - recyclerView this listener is attached to
     * @param dx - change in the x-axis of the recycler view
     * @param dy - change in the y-axis of the recycler view
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //check if the scrolling threshold is met, either positively or negatively
        if(Math.abs(dy) > mScrollThreshold){
            //scroll event exceeds threshold
            if(dy > 0){
                //change in y-axis is positive, scroll up event
                onScrollUp();
            }
            else{
                //change in y-axis is negative, scroll down event
                onScrollDown();
            }
        }
    }

    /*
     * void onScrollStateChanged(RecyclerView,int) - detect if scrolled to the last item. Checks the
     * number of items being held in the recyclerView, if there are none nothing happens. If there
     * are items in the recyclerView, waits for the scrolling state to go back to idle (stop
     * scrolling) before checking if the last item visible is the last item in the recyclerView. If
     * yes, sends a "scrolled to last item" event.
     * @param recyclerView - recyclerView this listener is attached to
     * @param newState - state of scroller
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        //get number of items held by recyclerView
        int count = recyclerView.getAdapter().getItemCount();

        //check that scroll state is idle, scrolling has stopped
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            //check that the number of items in recyclerView is not zero
            if(count != 0){
                //get the last visible item
                int lastVisibleItemPosition = ((LinearLayoutManager)
                        recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                //check last visible item is the last item in the recyclerView
                if(lastVisibleItemPosition != RecyclerView.NO_POSITION &&
                        lastVisibleItemPosition == count - 1){
                    //last visible item is the last item, scrolled to last item is true
                    scrolledToLastItem(true);
                }
                else{
                    //last visible item is NOT the last item, scrolled to last item is false
                    scrolledToLastItem(false);
                }
            }
        }

    }

/**************************************************************************************************/

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
