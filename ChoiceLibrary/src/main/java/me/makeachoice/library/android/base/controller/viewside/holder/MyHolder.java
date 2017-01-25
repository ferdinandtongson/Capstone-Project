package me.makeachoice.library.android.base.controller.viewside.holder;

import android.view.View;

import java.util.HashMap;

import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * MyHolder is an Abstract class that contains a ViewHolder inner class.
 *
 * There are two functions of MyHolder classes:
 *      1) hold child views of either a Fragment or Activity, accessed by a Maid or HouseKeeper
 *         class respectively
 *      2) manage static layout/view resource ids, string and integer values
 */
/**************************************************************************************************/

public abstract class MyHolder {


/**************************************************************************************************/
/*
 * ViewHolder inner class used to hold child view being managed by Maids or HouseKeepers
 */
/**************************************************************************************************/

    public static class ViewHolder{

        //mHolderMap contains views with their resource id as a key
        HashMap<Integer, View> mHolderMap = new HashMap<>();

        /*
         * View getView(View,int) - get child view of a fragment, used by Maid class
         */
        public View getView(View layout, int id){
            //check if view has already been created
            if(mHolderMap.containsKey(id)){
                //already been created, return view
                return mHolderMap.get(id);
            }
            else {
                //view no created, create view
                View view = layout.findViewById(id);

                //add view to HashMap
                mHolderMap.put(id, view);

                //return view
                return view;
            }
        }

        /*
         * View getView(MyActivity,int) - get child view of an Activity, used by HouseKeeper class
         */
        public View getView(MyActivity activity, int id){
            //check if view has already been created
            if(mHolderMap.containsKey(id)){
                //already been created, return view
                return mHolderMap.get(id);
            }
            else {
                //view no created, create view
                View view = activity.findViewById(id);
                //add view to HashMap
                mHolderMap.put(id, view);

                //return view
                return view;
            }
        }

}

/**************************************************************************************************/


}
