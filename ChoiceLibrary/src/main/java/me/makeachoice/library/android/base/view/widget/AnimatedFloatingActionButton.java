package me.makeachoice.library.android.base.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ListView;

import me.makeachoice.library.android.base.R;
import me.makeachoice.library.android.base.view.utilities.ListViewScrollDetector;
import me.makeachoice.library.android.base.view.utilities.RecyclerViewScrollDetector;

/**************************************************************************************************/
/*
 * AnimatedFloatingActionButton adds "show and hide" animation to FloatingActionButton
 */
/**************************************************************************************************/

public class AnimatedFloatingActionButton extends FloatingActionButton
{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int TRANSLATE_DURATION_MILLIS - translate animation time
 *      int SCROLL_THRESHOLD_ID - resource id for scroll threshold value
 *      Interpolator mInterpolator - animation interpolator
 *      boolean mVisible - visibility state of FAB
 *
 *      RecyclerViewScrollDetector mRecyclerScrollListener - scroll listener for recyclerView
 *      ListViewScrollDetector mListScrollListener - scroll listener for listView
 */
/**************************************************************************************************/

    //TRANSLATE_DURATION_MILLIS - translate animation time
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    //SCROLL_THRESHOLD_ID - resource id for scroll threshold value
    private int SCROLL_THRESHOLD_ID = R.dimen.fab_scroll_threshold;

    //Interpolator mInterpolator - animation interpolator
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    //boolean mVisible - visibility state of FAB
    private boolean mVisible;

    //mRecyclerScrollListener - scroll listener for recyclerView
    RecyclerViewScrollDetector mRecyclerScrollListener = new RecyclerViewScrollDetector(){
        @Override
        public void onScrollUp() {
            //hide(); - disabled
        }

        @Override
        public void onScrollDown() {
            //show(); - disabled
        }

        @Override
        public void scrolledToLastItem(boolean lastItem){
            if(lastItem){
                show();
            }
            else{
                hide();
            }
        }

        @Override
        public void setScrollThreshold() {
            setScrollThreshold(getResources().getDimensionPixelOffset(SCROLL_THRESHOLD_ID));
        }
    };

    //mListScrollListener - scroll listener for listView
    ListViewScrollDetector mListScrollListener = new ListViewScrollDetector(){
        @Override
        public void onScrollUp() {
            hide();
        }

        @Override
        public void onScrollDown() {
            show();
        }

        @Override
        public void scrolledToLastItem(boolean lastItem){
            if(lastItem){
                show();
            }
            else{
                hide();
            }
        }

        @Override
        public void setScrollThreshold() {
            setScrollThreshold(getResources().getDimensionPixelOffset(SCROLL_THRESHOLD_ID));
        }

    };

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * AnimatedFloatingActionButton - constructor
 */
/**************************************************************************************************/
    /*
     * AnimateFloatActionButton - constructor
     * @param context - current activity context
     * @param attrs - attribute set
     */
    public AnimatedFloatingActionButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        //set visibility state to true
        mVisible = true;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Attach Methods:
 *      void attachToRecyclerView(RecyclerView) - attach recyclerView events to FAB
 *      void attachToListView(ListView) - attach listView events to FAB
 */
/**************************************************************************************************/
    /*
     * void attachToRecyclerView(RecyclerView) - attach recyclerView events to FAB
     * @param recyclerView - recyclerView
     */
    public void attachToRecyclerView(@NonNull RecyclerView recyclerView){
        //add scroll listener to recyclerView
        recyclerView.addOnScrollListener(mRecyclerScrollListener);
    }

    /*
     * attachToListView(ListView) - attach listView events to FAB
     * @param listView - listView
     */
    public void attachToListView(@NonNull ListView listView){
        //add scroll listener to listView
        listView.setOnScrollListener(mListScrollListener);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Show & Hide Methods:
 *      void show() - show FAB, default animation true
 *      void show(boolean) - show FAB with animation option
 *      void hide() - hide FAB, default animation true
 *      void hide(boolean) - hide FAB with animation option
 */
/**************************************************************************************************/
    /*
     * void show() - show FAB, default animation true
     */
    public void show(){
        //
        show(true);
    }

    /*
     * void show(boolean) - show FAB with animation option
     * @param animate - animation flag used to determine whether to animate FAB or not
     */
    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    /*
     * void hide() - hide FAB, default animation true
     */
    public void hide() {
        hide(true);
    }

    /*
     * void hide(boolean) - hide FAB with animation option
     * @param animate - animation flag used to determine whether to animate FAB or not
     */
    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

 /**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void toggle(boolean,boolean,boolean) - toggle between showing and hiding FAB
 *      int getMarginBottom() - get bottom margin of layout
 */
/**************************************************************************************************/
    /*
     * void toggle(boolean,boolean,boolean) - toggle between showing and hiding FAB
     * @param visible - visibility state we want FAB in
     * @param animate - animate the showing or hiding of FAB
     * @param force - force the showing or hiding of FAB
     */
    private void toggle(final boolean visible, final boolean animate, boolean force) {
        //check if FAB is already in visibility state we want or forcing visibility state
        if (mVisible != visible || force){
            //requested visibility is different, change visibility state to requested state
            mVisible = visible;

            //get of FAB
            int height = getHeight();

            //check if height is zero and not forced
            if(height == 0 && !force) {
                //height is zero, FAB has not yet been created, get vto
                ViewTreeObserver vto = getViewTreeObserver();

                //check if viewTreeObserver is alive
                if (vto.isAlive()) {
                    //is alive, add preDrawListener to viewTreeObserver
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            //get current viewTreeObserver
                            ViewTreeObserver currentVto = getViewTreeObserver();

                            //check if current vto is alive
                            if (currentVto.isAlive()) {
                                //if yes, remove listener from vto
                                currentVto.removeOnPreDrawListener(this);
                            }

                            //toggle FAB
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }

            //get y-axis translation, if show, transY = 0, if hide, transY = move FAB under Margin
            int translationY = visible ? 0 : height + getMarginBottom();

            //check if animate FAB
            if(animate){
                //animate, animate FAB on the Y-axis
                this.animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                //do not animate, move FAB to new Y-coordinate
                setTranslationY(translationY*1.0f);

            }
        }
    }

    /*
     * int getMarginBottom() - get bottom margin of layout
     * @return - return bottom margin y
     */
    private int getMarginBottom(){
        //set bottom margin to zero
        int marginBottom = 0;

        //get layout parameter of viewGroup
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //check layoutParams is valid
        if(layoutParams instanceof ViewGroup.MarginLayoutParams){
            //get margin bottom value
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }

        //return margin bottom value
        return marginBottom;
    }

/**************************************************************************************************/

}