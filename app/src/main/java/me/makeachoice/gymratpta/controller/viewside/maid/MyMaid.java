package me.makeachoice.gymratpta.controller.viewside.maid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.gymratpta.view.fragment.MyFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * MyMaid abstract class manages MyFragment classes and is typically instantiated by MyHouseKeeper
 */
/**************************************************************************************************/

public abstract class MyMaid {

/**************************************************************************************************/
/*
 * Class Variables
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      MyFragment mFragment - fragment being maintained by the Maid
 */
/**************************************************************************************************/

    //mMaidKey - key string of instance Maid
    protected String mMaidKey;

    //mLayoutId - resource id number of fragment layout
    protected int mLayoutId;

    //mLayout - fragment layout view holding the child views
    protected View mLayout;

    //mFragment - fragment being maintained by the Maid
    protected MyFragment mFragment;

    //mActivity - activity context
    protected MyActivity mActivity;

/**************************************************************************************************/
/*
 * MyFragment Bridge Implementation:
 *      View createView(LayoutInflater,ViewGroup,Bundle) - NEED TO IMPLEMENT IN INSTANCE CLASS
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      String getKey() - get maid key value
 *      Fragment getFragment() - get new instance fragment
 */
/**************************************************************************************************/
    /*
     * void activityCreated() - called when Activity.onCreate() completed
     */
    public void activityCreated(Bundle bundle){
        //Activity.onCreate() completed
        mActivity = (MyActivity)mFragment.getActivity();
    }

    public void start(){

    }

    /*
     * void destroyView() - called when fragment is being removed
     */
    public void destroyView(){
        //fragment is being removed
    }

    /*
     * void detach() - called when fragment is being disassociated from Activity
     */
    public void detach(){
        //fragment is being disassociated from Activity
    }

    /*
     * void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
     */
    public void saveInstanceState(Bundle bundle){
        //save state to bundle
    }

    /*
     * String getKey() - get maid key value
     */
    public String getKey(){
        return mMaidKey;
    }

    /*
     * Fragment getFragment() - get new instance fragment
     */
    public Fragment getFragment(){
        mFragment = BasicFragment.newInstance(mMaidKey);
        return mFragment;
    }
/**************************************************************************************************/


}
