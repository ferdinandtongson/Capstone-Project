package me.makeachoice.library.android.base.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**************************************************************************************************/
/*
 * MyFragment abstract class extends Fragments
 */
/**************************************************************************************************/

public abstract class MyFragment extends Fragment {

/**************************************************************************************************/
/*
 * Class Variables
 *      String KEY_MAID_ID - key value used to store maid id into Bundle
 *      Integer mMaidId - id number of the Maid class taking care of the fragment
 *      View mLayout - View layout of the fragment
 *      Bridge mBridge - class implementing Bridge interface
 *
 * Bridge Interface:
 *      View createView(LayoutInflater,ViewGroup,Bundle) - called by onCreateView is fragment
 *          lifecycle, Fragment UI is drawn
 *      void activityCreated() - called by onActivityCreated() in fragment lifecycle,
 *          Activity.onCreate() completed
 *      void destroyView() - called by onDestroyView() in fragment lifecycle, fragment is being
 *          removed
 *      void detach() - called by onDetach() in fragment lifecycle, fragment is being disassociated
 *          from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 */
/**************************************************************************************************/

    //KEY_MAID_ID - key value used to store the maid id into Bundle
    protected static String KEY_MAID_ID = "MaidId";

    //mMaidId - id number of the Maid class taking care of the fragment
    protected Integer mMaidId;

    //mLayout - View layout of the fragment
    protected View mLayout;

    //mBridge - class implementing Bridge interface
    protected Bridge mBridge;

    //Implemented communication line, usually implemented by a Maid class
    public interface Bridge{
        //called by onCreateView() in fragment lifecycle, Fragment UI is drawn
        View createView(LayoutInflater inflater, ViewGroup container, Bundle bundle);

        //called by onActivityCreated() in fragment lifecycle, Activity.onCreate() completed
        void activityCreated(Bundle bundle);

        //called by onDestroyView() in fragment lifecycle, fragment is being removed
        void destroyView();

        //called by onDetach() in fragment lifecycle, fragment is being disassociated from Activity
        void detach();

        //called before onDestroy(), save state to bundle
        void saveInstanceState(Bundle bundle);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void onActivityCreated() - called when Activity.onCreate() completed
 *      void onDestroyView() - called when fragment is being removed
 *      void onDetach() - called when fragment is being disassociated from Activity
 *      void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      void setMaidId(Integer) - sets the id number of the Maid taking care of this Fragment
 */
/**************************************************************************************************/
    /*
     * void onActivityCreated() - called when Activity.onCreate() completed
     * @param bundle - saved instant state of fragment
     */
    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        mBridge.activityCreated(bundle);
    }

    /*
     * void onDestroyView() - called when fragment is being removed
     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mBridge.destroyView();
    }

    /*
     * void onDetach() - called when fragment is being disassociated from Activity
     */
    @Override
    public void onDetach(){
        super.onDetach();
        mBridge.detach();
    }

    /*
     * void onSaveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
     * @param bundle - bundle object used to save any instance states
     */
    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        mBridge.saveInstanceState(bundle);
    }

    /*
     * void setMaidId(Integer) - sets the id number of the Maid taking care of the fragment
     */
    public void setMaidId(Integer id){
        mMaidId = id;
    }


/**************************************************************************************************/

}

