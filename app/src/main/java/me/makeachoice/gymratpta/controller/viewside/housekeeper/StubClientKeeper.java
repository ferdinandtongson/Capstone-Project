package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.Helper.CommunicationHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.ClientRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.ClientCardItem;
import me.makeachoice.gymratpta.model.stubData.ClientStubData;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * TODO - need to get client profile picture from Contacts
 *          todo - load profile uri picture
 * TODO - Enable Context Menu
 *          todo - activate client
 *          todo - retire client
 * TODO - Add divider lines in Context Menu
 * TODO - Use client data for email and phone icon events
 * TODO - need to create Dialog Contact List
 * TODO - need to create Permissions Helper class
 * TODO - need to create SQLite Database
 * TODO - need to create Firebase project client table
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientKeeper is responsible for showing the list of clients the user has in GymRat and accessing
 * deeper level information about those clients. It extends GymRatRecyclerKeeper which extends
 * GymRatRecyclerBase so is directly responsible for drawer navigation, toolbar, "empty" textView,
 * recycler view and FAB components.
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
 * Variables from GymRatBaseKeeper:
 *      Boss mBoss - Boss application
 *      HomeToolbar mToolbar - toolbar component
 *      HomeDrawer mHomeDrawer - drawer navigation component
 *      int mToolbarMenuId - toolbar menu resource id
 *      int mBottomNavSelectedItemId - used to determine which menu item is selected in the drawer
 *
 * Variables from GymRateRecyclerKeeper:
 *      TextView mTxtEmpty - textView component displayed when recycler is empty
 *      BasicRecycler mBasicRecycler - recycler component
 *      FloatingActionButton mFAB - floating action button component

 * Methods from MyHouseKeeper
 *      void create(MyActivity,Bundle) - called by onCreate(Bundle) in the activity lifecycle
 *      boolean isTablet() - return device flag if device is tablet or not
 *
 * MyHouseKeeper Implements MyActivity.Bridge
 *      void start() - called by onStart() in the activity lifecycle
 *      void resume() - called by onResume() in the activity lifecycle
 *      void pause() - called by onPause() in the activity lifecycle
 *      void stop() - called by onStop() in the activity lifecycle
 *      void destroy() - called by onDestroy() in the activity lifecycle
 *      void backPressed() - called by onBackPressed() in the activity
 *      void saveInstanceState(Bundle) - called before onDestroy(), save state to bundle
 *      void activityResult(...) - result of Activity called by this Activity
 *
 */

/**************************************************************************************************/

public class StubClientKeeper extends GymRatRecyclerKeeper implements MyActivity.Bridge,
        RecyclerView.OnCreateContextMenuListener, MyActivity.OnContextItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables:
 *      int CONTEXT_MENU_ACTIVATE - "activate client" context menu id number
 *      int CONTEXT_MENU_RETIRE = "retire client" context menu id number
 *      ArrayList<ClientCardItem> mData - array of data used by ClientRecyclerAdapter
 *      ClientRecyclerAdapter mAdapter - recycler adapter
 */
/**************************************************************************************************/

    //CONTEXT_MENU_ACTIVATE - "activate client" context menu id number
    private final static int CONTEXT_MENU_ACTIVATE = 0;

    //CONTEXT_MENU_RETIRE = "retire client" context menu id number
    private final static int CONTEXT_MENU_RETIRE = 1;

    //mData - array of data used by ClientRecyclerAdapter
    private ArrayList<ClientCardItem> mData;

    //mAdapter - recycler adapter
    private ClientRecyclerAdapter mAdapter;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * ClientKeeper - constructor
     * @param layoutId - layout resource id used by Keeper
     */
    public StubClientKeeper(int layoutId){

        //get layout id from HouseKeeper Registry
        mActivityLayoutId = layoutId;

        //set toolbar menu resource id consumed by HomeToolbar
        mToolbarMenuId = R.menu.toolbar_menu;

        //flag used to determine which menu items is selected in drawer component
        mBottomNavSelectedItemId = R.id.nav_clients;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Activity Lifecycle Methods
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
 */
/**************************************************************************************************/
    /*
     * void create(MyActivity, Bundle) - called when onCreate is called in the activity. Sets the
     * activity layout, fragmentManager and other child views of the activity
     *
     * NOTE: Both FragmentManager and FAB are context sensitive and need to be recreated every time
     * onCreate() is called in the Activity
     * @param activity - current activity being shown
     * @param bundle - instant state values
     */
    public void create(MyActivity activity, Bundle bundle){
        super.create(activity, bundle);

        if(bundle != null){
            //open bundle to set saved instance states
            openBundle(bundle);
        }

        initializeLayout();
    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //set saved instance state data
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize ui
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize ui
     */
    private void initializeLayout(){
        //initialize "empty" textView used when recycler is empty
        initializeEmptyText();

        //initialize adapter used by recycler
        initializeAdapter();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" textView message
        String emptyMsg = mActivity.getResources().getString(R.string.emptyRecycler_addClient);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_client;

        mData = ClientStubData.createClientData(mActivity);

        //create adapter consumed by the recyclerView
        mAdapter = new ClientRecyclerAdapter(mActivity, adapterLayoutId);

        //set icon click listener
        mAdapter.setOnIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconClicked(view);
            }
        });

        //set create context menu listener
        mAdapter.setOnCreateContextMenuListener(this);

        //swap client data into adapter
        mAdapter.swapData(mData);

        //set adapter in recycler
        mBasicRecycler.setAdapter(mAdapter);

        //check if recycler has any data; if not, display "empty" textView
        checkForEmptyRecycler(mData.isEmpty());
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void backPressed() - called when Activity.onBackPressed is called
 */
/**************************************************************************************************/
    /*
     * void backPressed() - called when Activity.onBackPressed is called
     */
    @Override
    public void backPressed(){
    }

    private void iconClicked(View view){
        Log.d("Choice", "StubClientKeeper.onIconClicked");
        ClientCardItem item = (ClientCardItem)view.getTag(R.string.recycler_tagItem);
        int iconId = (int)view.getTag(R.string.recycler_tagId);

        switch(iconId){
            case ClientRecyclerAdapter.ICON_INFO:
                break;
            case ClientRecyclerAdapter.ICON_EMAIL:
                //TODO - need to put actual client email
                CommunicationHelper.sendEmail(mActivity, "stubemail@gmail.com");
                break;
            case ClientRecyclerAdapter.ICON_PHONE:
                //TODO - need to put actual client phone number
                CommunicationHelper.makeCall(mActivity, "5104782282");
                break;
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        //get clientCardItem
        ClientCardItem item = (ClientCardItem)v.getTag(R.string.recycler_tagItem);

        //get client name
        String clientName = (item.clientName);

        //get context menu strings
        String strActivate = mActivity.getString(R.string.context_menu_activate);
        String strRetire = mActivity.getString(R.string.context_menu_retire);

        //create context menu
        menu.setHeaderTitle(clientName);

        if(item.isActive){
            menu.add(0, CONTEXT_MENU_RETIRE, 0, strRetire);
        }
        else{
            menu.add(0, CONTEXT_MENU_ACTIVATE, 0, strActivate);
        }
    }

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_ACTIVATE:
                Log.d("Choice", "     activate");
                //TODO - need to reschedule session
                return true;
            case CONTEXT_MENU_RETIRE:
                Log.d("Choice", "     retire");
                //TODO - need to cancel session
                return true;
        }
        return false;
    }


/**************************************************************************************************/

}
