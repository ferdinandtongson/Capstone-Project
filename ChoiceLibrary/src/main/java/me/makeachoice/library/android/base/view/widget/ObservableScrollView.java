/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.makeachoice.library.android.base.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
/**************************************************************************************************/
/*
 * ObservableScrollView extends ScrollView by adding a scroll listener
 */
/**************************************************************************************************/

public class ObservableScrollView extends ScrollView {

/**************************************************************************************************/
/*
 * Class Variables:
 *      ScrollListener mScrollListener - scroll change interface listener
 *
 * ScrollListener Interface:
 *      void onScrollChanged() - scrollView scroll change event
 */
/**************************************************************************************************/
    //mScrollListener - scroll change interface listener
    private ScrollListener mScrollListener;

    public static interface ScrollListener {
        //scrollView scroll change event
        public void onScrollChanged();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ObservableScrollView - constructors
 */
/**************************************************************************************************/

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onScrollChanged(int,int,int,int) - called when horizontal or vertical scroll changed
 *      void onLayout(boolean,int,int,int,int) - called from layout when view should assign a size
 *              and position to each of its children
 *      int computeVerticalScrollRange() - get vertical scroll range
 *      void setOnScrollListener(ScrollListener) - set onScrollListener interface
 */
/**************************************************************************************************/
    /*
     * void onScrollChanged(int,int,int,int) - called when horizontal or vertical scroll changed
     * @param l - current horizontal scroll origin
     * @param t - current vertical scroll origin
     * @param oldl - previous horizontal scroll origin
     * @param oldt - previous vertical scroll origin
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //check for scroll listener
        if (mScrollListener != null) {
            //notify scroll listener of scroll change
            mScrollListener.onScrollChanged();
        }
    }

    /*
     * void onLayout(boolean,int,int,int,int) - called from layout when view should assign a size
     * and position to each of its children
     * @param changed - new size or position for this view
     * @param l - left position, relative to parent
     * @param t - top position, relative to parent
     * @param r - right position, relative to parent
     * @param b - bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //check scroll y position
        int scrollY = getScrollY();
        // hack to call onScrollChanged on screen rotate
        if (scrollY > 0 && mScrollListener != null) {
            //notify scroll listener
            mScrollListener.onScrollChanged();
        }
    }

    /*
     * int computeVerticalScrollRange() - get vertical scroll range
     */
    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    /*
     * void setOnScrollListener(ScrollListener) - set onScrollListener interface
     */
    public void setOnScrollListener(ScrollListener listener) {
        mScrollListener = listener;
    }

/**************************************************************************************************/

}