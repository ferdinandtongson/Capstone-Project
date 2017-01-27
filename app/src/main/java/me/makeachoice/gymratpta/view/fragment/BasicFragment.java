package me.makeachoice.gymratpta.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.makeachoice.gymratpta.controller.manager.Boss;
import me.makeachoice.library.android.base.view.fragment.MyFragment;

/**************************************************************************************************/
/*
 * BasicFragment handles the creation of a fragment then passes the maintenance of the fragment to
 * a Maid class.
 *
 * MyFragment Class Variables
 *      String KEY_MAID_ID - key value used to store maid id into Bundle
 *      Integer mMaidId - id number of the Maid class taking care of the fragment
 *      View mLayout - View layout of the fragment
 *      Bridge mBridge - class implementing Bridge interface
 *
 * MyFragment Inherited Methods:
 *      void onActivityCreated() - called when Activity.onCreate() completed
 *      void onDestroyView() - called when fragment is being removed
 *      void onDetach() - called when fragment is being disassociated from Activity
 *      void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      void setMaidId(Integer) - sets the id number of the Maid taking care of this Fragment
 *
 */
/**************************************************************************************************/

public class BasicFragment extends MyFragment {

    /*
     * Fragment subclasses require an empty default constructor. If you don't provide one but
     * specify a non-empty constructor, Lint will give you an error.
     *
     * Android may destroy and later re-create an activity and all its associated fragments when
     * the app goes into the background. When the activity comes back, its FragmentManager starts
     * re-creating fragments by using the empty default constructor. If it cannot find one, you
     * get an exception
     */
    public static BasicFragment newInstance(int id){
        BasicFragment f = new BasicFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();

        //add maid id to bundle
        args.putInt(KEY_MAID_ID, id);

        //set bundle in arguments
        f.setArguments(args);
        return f;
    }

    public BasicFragment(){}

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * FragmentActivity LifeCycle methods:
 *      View onCreateView(LayoutInflater,ViewGroup,Bundle) - called when the fragment UI is drawn
 *      void onActivityCreated(Bundle) - called when the activity the fragment is attached to
 *          completed its own Activity.onCreate( )
 *      void onSaveInstanceState(Bundle) - called before onDestroy(), where you save state values
 */
/**************************************************************************************************/
    /*
     * View onCreateView(LayoutInflater,ViewGroup,Bundle) - called when the fragment UI is drawn. To
     * draw a UI for your fragment, you must return a View from this method that is the root of your
     * fragment's layout. You can return null if the fragment does not provide a UI.
     *
     * This is called between onCreate(...) and onActivityCreated(...). If you return a View from
     * here, you will later be called in onDestroyView() when the view is being released.
     *
     * Remember setRetainInstance(true) if you want to retain fragment values during an orientation
     * change event.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //check if bundle has been sent/saved
        if(savedInstanceState != null){
            //get id number of maid maintaining this fragment
            mMaidId = savedInstanceState.getInt(KEY_MAID_ID);
        }

        //get application context, the Boss
        Boss boss = (Boss)getActivity().getApplicationContext();

        try{
            //make sure Maid is implementing the Bridge interface
            mBridge = (Bridge)boss.requestMaid(mMaidId);
        }catch(ClassCastException e){
            throw new ClassCastException("Maid must implement Bridge interface");
        }

        //create and return fragment layout view from file found in res/layout/xxx.xml,
        if(mLayout == null){
            //mLayout = inflater.inflate(mLayoutId, container, false);
            mLayout = mBridge.createView(inflater, container, savedInstanceState);
        }

        return mLayout;
    }

    /*
     * void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
     * @param bundle - bundle object used to save any instance states
     */
    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putInt(MyFragment.KEY_MAID_ID, mMaidId);
        super.onSaveInstanceState(bundle);
    }

/**************************************************************************************************/

}
