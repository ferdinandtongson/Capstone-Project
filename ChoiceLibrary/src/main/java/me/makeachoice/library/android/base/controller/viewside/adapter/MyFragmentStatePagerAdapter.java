package me.makeachoice.library.android.base.controller.viewside.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**************************************************************************************************/
/*
 * MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter to handle the switching between
 * different fragments. Used when there are a large set of Fragments to manage. The adapter needs
 * getCount() and getItem() to properly function. The count is needed so the adapter knows how many
 * pages to account for and getItem() is for the adapter to properly display the requested fragment
 *
 * Note: all you have to do is instantiate this class and the the class implement the bridge
 * provides the necessary count and array of fragments
 */
/**************************************************************************************************/

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

/**************************************************************************************************/
/*
 * Class Variables
 *      mTitleList - list of page titles used by adapter
 *      mTitleArray - string array of page titles used by adapter
 *      Bridge mBridge - class implementing Bridge, typically a Maid class
 */
/**************************************************************************************************/

    //mTitleList - list of page titles used by adapter
    protected ArrayList<String> mTitleList;

    //mTitleArray - string array of page titles used by adapter
    protected String[] mTitleArray;

    //mBridge - class implementing Bridge, typically a Maid class
    protected Bridge mBridge;

    //Implemented communication line to a class
    public interface Bridge{
        //request fragment from Keeper
        Fragment requestFragment(int position);

        //get number of fragments in adapter
        int requestFragmentCount();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * MyFragmentStatePagerAdapter - constructor
 * @param bridge - communication bridge
 */
/**************************************************************************************************/
    public MyFragmentStatePagerAdapter(FragmentManager fm, Bridge bridge){
        super(fm);

        mBridge = bridge;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Getters:
 *      int getCount(int) - get number of items in adapter
 *      Fragment getItem(int) - get fragment in given position
 *      CharSequence getPageTitle(int) - get page title in given position
 */
/**************************************************************************************************/
    /*
     * int getCount(int) - get number of items in adapter
     */
    @Override
    public int getCount() {
        return mBridge.requestFragmentCount();
    }

    /*
     * Fragment getItem(int) - get fragment in given position
     */
    @Override
    public Fragment getItem(int position) {
        return mBridge.requestFragment(position);
    }

    /*
     * CharSequence getPageTitle(int) - get page title in given position
     */
    public CharSequence getPageTitle(int position)
    {
        if(mTitleList != null){
            return mTitleList.get(position);
        }
        else if(mTitleArray != null){
            return mTitleArray[position];
        }
        else{
            return "- no title list -";
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setters:
 *      void setPageTitle(ArrayList<String>) - set title list used by adapter
 *      void setPageTitle(String[]) - set title array used by adapter
 */
/**************************************************************************************************/
    /*
     * void setPageTitle(ArrayList<String>) - set title list used by adapter
     */
    public void setPageTitle(ArrayList<String> titleList){
        mTitleList = titleList;
    }

    /*
     * void setPageTitle(String[]) - set title array used by adapter
     */
    public void setPageTitle(String[] titleArray){
        mTitleArray = titleArray;
    }

/**************************************************************************************************/


}
