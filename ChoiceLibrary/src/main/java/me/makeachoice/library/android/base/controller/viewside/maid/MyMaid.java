package me.makeachoice.library.android.base.controller.viewside.maid;

import android.os.Bundle;
import android.view.View;

import me.makeachoice.library.android.base.view.fragment.MyFragment;

/**************************************************************************************************/
/*
 * MyMaid abstract class manages MyFragment classes and is typically instantiated by MyHouseKeeper
 */
/**************************************************************************************************/

public abstract class MyMaid {

/**************************************************************************************************/
/*
 * Class Variables
 *      int mMaidId - id number of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      MyFragment mFragment - fragment being maintained by the Maid
 */
/**************************************************************************************************/

    //mMaidId - id number of instance Maid
    protected int mMaidId;

    //mLayoutId - resource id number of fragment layout
    protected int mLayoutId;

    //mLayout - fragment layout view holding the child views
    protected View mLayout;

    //mFragment - fragment being maintained by the Maid
    protected MyFragment mFragment;

/**************************************************************************************************/
/*
 * MyFragment Bridge Implementation:
 *      View createView(LayoutInflater,ViewGroup,Bundle) - NEED TO IMPLEMENT IN INSTANCE CLASS
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      int getMaidId() - get maid id number
 *      MyFragment getFragment() - get fragment maintained by maid
 */
/**************************************************************************************************/
    /*
     * void activityCreated() - called when Activity.onCreate() completed
     * @param bundle - saved instant state of fragment
     */
    public void activityCreated(Bundle bundle){
        //Activity.onCreate() completed
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
     * @param bundle - bundle object used to save any instance states
     */
    public void saveInstanceState(Bundle bundle){
        //save state to bundle
    }

    /*
     * int getMaidId() - get maid id number
     * @return - maid id number
     */
    public int getMaidId(){
        return mMaidId;
    }

    /*
     * MyFragment getFragment() - get fragment maintained by maid
     * @return - MyFragment maintained by maid
     */
    public MyFragment getFragment(){
        return mFragment;
    }

/**************************************************************************************************/


}
