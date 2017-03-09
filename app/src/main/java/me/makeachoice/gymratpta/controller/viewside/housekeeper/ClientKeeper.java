package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.viewside.Helper.PermissionHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.ClientAdapter;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.dialog.ContactListDialog;
import me.makeachoice.library.android.base.controller.viewside.bartender.MyBartender;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_ACTIVE;
import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_ALL;
import static me.makeachoice.gymratpta.controller.manager.Boss.CLIENT_RETIRED;
import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_CONTACTS;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CLIENT;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_CLIENT_STATUS;

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

public class ClientKeeper extends GymRatRecyclerKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 *      String mUId - user id number
 *      HashMap<String,ClientItem> mClientMap - client hashMap, created by adapter and used by ContactListDialog
 *      ClientAdapter mAdapter - recycler adapter
 *      PermissionHelper mPermissionHelper - helps making permission requests
 *
 *      int mRecursiveCount - recursive counter used to update client data
 *      int mLoaderId - base loader id used to identify loader managers
 *      ClientItem mClientItem - client item used in recursion
 *      ArrayList<ClientItem> mClients - client list used in recursion
 */
/**************************************************************************************************/

    //mClientMap - client hashMap, created by adapter and used by ContactListDialog
    private HashMap<String, ClientItem> mClientMap;

    //mAdapter - recycler adapter
    private ClientAdapter mAdapter;

    //mPermissionHelper - helps making permission requests
    private PermissionHelper mPermissionHelper;

    //mClients - client list used in recursion
    private ArrayList<ClientItem> mClients;

    private ClientButler mClientButler;
    private String mStatus;
    private int mDialogCounter;


    ClientButler.OnLoadedListener mLoadListener = new ClientButler.OnLoadedListener() {
        @Override
        public void onLoaded(ArrayList<ClientItem> clientList) {
            onClientsLoaded(clientList);
        }
    };

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
    public ClientKeeper(int layoutId){
        //get layout id from HouseKeeper Registry
        mActivityLayoutId = layoutId;

        //set toolbar menu resource id consumed by HomeToolbar
        mToolbarMenuId = R.menu.toolbar_client_menu;

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

        //get user id
        mClients = new ArrayList();
        mDialogCounter = -1;

        mPermissionHelper = new PermissionHelper(mActivity);



    }

    /*
     * void openBundle(Bundle) - opens bundle to set saved instance states during create().
     */
    protected void openBundle(Bundle bundle){
        //set saved instance state data
    }

    public void start(){
        super.start();
        if(mIsAuth){
            if(!mInitialized){
                mInitialized = true;
                initializeValues();
                requestClients(mStatus);
            }
            else{
                requestClients(mStatus);
            }
        }
    }

    private void initializeValues(){
        //get user shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user preference to want to receive deletion warning
        mStatus = prefs.getString(PREF_CLIENT_STATUS, CLIENT_ALL);
        mClientButler = new ClientButler(mActivity, mUserId);

        initializeLayout();

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            //request Read Contacts permission
            mPermissionHelper.getPermissionToReadContacts();
        }

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

        //initialize FAB
        initializeFAB();
        initializeToolbar();

        //initialize adapter used by recycler
        initializeAdapter();

        initializeRecycler();
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

    private void initializeToolbar(){
        mHomeToolbar.setOnMenuItemClick(new MyBartender.OnMenuItemClick() {
            @Override
            public void onMenuItemClick(MenuItem menuItem) {
                onMenuChangeClientStatus(menuItem);
            }
        });
    }

    private void initializeFAB(){
        String description = mActivity.getString(R.string.description_fab_client);
        mFAB.setContentDescription(description);

        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClicked(view);
            }
        });
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_client;

        //create adapter consumed by the recyclerView
        mAdapter = new ClientAdapter(mActivity, adapterLayoutId);

        //set item click listener event
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(view);
            }
        });

        //swap data into adapter
        mAdapter.swapData(mClients);

        //get client hashMap
        mClientMap = mClientButler.getClientMap();

        //check if recycler has any data; if not, display "empty" textView
        updateEmptyText();
    }

    /*
     * void initializeRecycler() - initialize recycler component
     */
    private void initializeRecycler(){
        //set adapter
        mBasicRecycler.setAdapter(mAdapter);
    }

    /*
     * ContactListDialog initializeDialog(...) - show contacts list dialog
     */
    private ContactListDialog initializeDialog() {
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        ContactListDialog dia = new ContactListDialog();

        if(mDialogCounter == 0) {

            dia.setClientMap(mClientMap);
            dia.setOnAddClientListener(new ContactListDialog.OnAddClientListener() {
                @Override
                public void onAddClient(ClientItem clientItem) {
                    clientItem.uid = mUserId;
                    clientItem.firstSession = "";
                    clientItem.goals = "";
                    clientItem.status = CLIENT_ACTIVE;

                    mDialogCounter = -1;
                    addClient(clientItem);
                }
            });

            dia.show(fm, DIA_CONTACTS);
        }

        return dia;
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Update methods
 */
/**************************************************************************************************/


    private void updateLayout(){

        //swap data into adapter
        mAdapter.swapData(mClients);

        //get client hashMap
        mClientMap = mClientButler.getClientMap();

        //check if recycler has any data; if not, display "empty" textView
        updateEmptyText();
    }


    /*
     * void updateEmptyText() - check if adapter is empty or not then updates empty textView
     */
    private void updateEmptyText(){
        if(mClients.size() > 0){
            //is not empty
            isEmptyRecycler(false);
        }
        else{
            //is empty
            isEmptyRecycler(true);
        }
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void backPressed() - called when Activity.onBackPressed is called
 *      void onItemClicked(View) - called when item is clicked on
 *      void fabClicked(View) - float action button click event
 */
/**************************************************************************************************/
    /*
     * void backPressed() - called when Activity.onBackPressed is called
     */
    @Override
    public void backPressed(){
    }

    /*
     * void onItemClicked(View) - called when item is clicked on
     */
    private void onItemClicked(View view){
        ClientItem item = (ClientItem)view.getTag(R.string.recycler_tagItem);
        Intent intent = new Intent(mActivity, ClientDetailActivity.class);
        mBoss.setClient(item);
        mActivity.startActivity(intent);
    }

    /*
     * void fabClicked(View) - float action button click event. Checks if use has granted READ_CONTACTS
     * permission; If not, requests permission from user. If so, opens contacts list dialog
     */
    private void fabClicked(View view){
        //check if user has given READ_CONTACTS permission
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            //request Read Contacts permission
            mPermissionHelper.getPermissionToReadContacts();
        }
        else{
            mDialogCounter = 0;
            //show contacts dialog
            initializeDialog();
        }
    }

    private void onMenuChangeClientStatus(MenuItem menuItem){
        int id = menuItem.getItemId();
        switch(id){
            case R.id.toolbar_client_retired:
                mStatus = CLIENT_RETIRED;
                break;
            case R.id.toolbar_client_active:
                mStatus = CLIENT_ACTIVE;
                break;
            case R.id.toolbar_client_all:
                mStatus = CLIENT_ALL;
                break;
            default:
                mStatus = CLIENT_ALL;
        }

        requestClients(mStatus);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void loadClients() - loads clients onto cursor used by Adapter
 *      void loadFirebaseClients() - gets client data from Firebase
 *      void requestContactInfo() - request device contact id and profile picture
 *      void updateDatabase(ClientItem) - update local database with client data
 */
/**************************************************************************************************/
    private void requestClients(String status){

        if(status.equals(CLIENT_ACTIVE)){
            mClientButler.loadActiveClients(LOADER_CLIENT, mLoadListener);
        }
        else if(status.equals(CLIENT_RETIRED)){
            mClientButler.loadRetiredClients(LOADER_CLIENT, mLoadListener);
        }
        else{
            mClientButler.loadAllClients(LOADER_CLIENT, mLoadListener);

        }

    }

    /*
     * void loadClients() - loads clients onto cursor used by Adapter. First gets clients from Firebase,
     * loads client data to sqlite local database and then retrieves and delivers that data as a cursor
     */
    private void onClientsLoaded(ArrayList<ClientItem> clientList){
        mClients.clear();
        mClients.addAll(clientList);

        updateLayout();
    }

/**************************************************************************************************/

    private ClientItem mSaveItem;
    private void addClient(ClientItem clientItem){
        mSaveItem = clientItem;
        mClientButler.saveClient(clientItem, new ClientButler.OnSavedListener() {
            @Override
            public void onSaved() {
                Toast.makeText(mActivity, mSaveItem.clientName + " added", Toast.LENGTH_LONG).show();
                requestClients(mStatus);
            }
        });

    }
}
