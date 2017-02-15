package me.makeachoice.gymratpta.controller.viewside.maid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.recycler.BasicRecycler;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * TODO - add description
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *
 * Methods from MyMaid:
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      String getKey() - get maid key value
 *      Fragment getFragment() - get new instance fragment
 */

/**************************************************************************************************/

public abstract class GymRatRecyclerMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 *      mTxtEmpty - textView component displayed when recycler is empty
 *      mBasicRecycler - recycler component
 *      mFAB - floating action button component
 */
/**************************************************************************************************/
    //mTxtEmpty - textView component displayed when recycler is empty
    protected TextView mTxtEmpty;

    //mBasicRecycler - recycler component
    protected BasicRecycler mBasicRecycler;

    //mFAB - floating action button component
    protected FloatingActionButton mFAB;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  MyMaid Implementation
 *      void createActivity(Bundle) - is called by Fragment onCreateActivity(...)
 *      void detach() - fragment is being disassociated from activity
 */
/**************************************************************************************************/
    /*
     * void createActivity(Bundle) - is called by Fragment when onCreateActivity(...) is called in
     * that class. Sets child views in fragment before being seen by the user
     * @param bundle - saved instance states
     */
    @Override
    public void activityCreated(Bundle bundle){
        super.activityCreated(bundle);

        //initialize recycler components
        initializeRecyclerComponents();
    }

    /*
     * void detach() - fragment is being disassociated from activity
     */
    @Override
    public void detach(){
        //fragment is being disassociated from Activity
        super.detach();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeRecyclerComponents() - initialize recycler and related components
 */
/**************************************************************************************************/
    /*
     * void initializeRecyclerComponents() - initialize recycler and related components
     */
    private void initializeRecyclerComponents(){
        //initialize recycler component
        mBasicRecycler = new BasicRecycler(mLayout);

        //initialize "empty" textView component
        int emptyViewId = R.id.choiceEmptyView;
        mTxtEmpty = (TextView) mLayout.findViewById(emptyViewId);

        //initialize floating action button component
        int fabId = R.id.choiceFab;
        mFAB = (FloatingActionButton) mLayout.findViewById(fabId);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void setEmptyMessage(String) - set "empty" message to be displayed when recycler is empty
 *      void setOnClickFABListener(View.OnClickListener) - set onClick listener for FAB
 *      void isEmptyRecycler(boolean) - checks whether to display "empty" message or not
 */
/**************************************************************************************************/
    /*
     * void setEmptyMessage(String) - set "empty" message to be displayed when recycler is empty
     */
    protected void setEmptyMessage(String msg){
        mTxtEmpty.setText(msg);
    }

    /*
     * void setOnClickFABListener(View.OnClickListener) - set onClick listener for FAB
     */
    protected void setOnClickFABListener(View.OnClickListener listener){
        mFAB.setOnClickListener(listener);
    }

    /*
     * void isEmptyRecycler(boolean) - checks whether to display "empty" message or not
     */
    protected void isEmptyRecycler(boolean isEmpty){
        if(isEmpty){
            mTxtEmpty.setVisibility(View.VISIBLE);
        }
        else{
            mTxtEmpty.setVisibility(View.GONE);
        }
    }


/**************************************************************************************************/


}
