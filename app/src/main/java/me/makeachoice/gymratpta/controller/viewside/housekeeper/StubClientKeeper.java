package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.firebase.ClientFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.ContactsLoader;
import me.makeachoice.gymratpta.controller.viewside.Helper.PermissionHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.exercise.ClientItemAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientColumns;
import me.makeachoice.gymratpta.model.item.ClientCardItem;
import me.makeachoice.gymratpta.model.item.client.ClientFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.view.activity.ClientDetailActivity;
import me.makeachoice.gymratpta.view.dialog.ContactListDialog;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.R.id.message;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**************************************************************************************************/
/*
 * TODO - Enable Context Menu
 *      todo - activate client
 *      todo - retire client
 * TODO - need to be able to display different client status
 *      todo - display active status only
 *      todo - display retired status only
 * TODO - need to display progress bar during loading
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
 *      String mUId - user id number
 *      HashMap<String,ClientItem> mClientMap - client hashMap, created by adapter and used by ContactListDialog
 *      ClientItemAdapter mAdapter - recycler adapter
 *      PermissionHelper mPermissionHelper - helps making permission requests
 *
 *      int mRecursiveCount - recursive counter used to update client data
 *      int mLoaderId - base loader id used to identify loader managers
 *      ClientItem mClientItem - client item used in recursion
 *      ArrayList<ClientItem> mClients - client list used in recursion
 */
/**************************************************************************************************/

    //CONTEXT_MENU_ACTIVATE - "activate client" context menu id number
    private final static int CONTEXT_MENU_ACTIVATE = 0;

    //CONTEXT_MENU_RETIRE = "retire client" context menu id number
    private final static int CONTEXT_MENU_RETIRE = 1;

    //mUserId - user id number
    private String mUserId;

    //mClientMap - client hashMap, created by adapter and used by ContactListDialog
    private HashMap<String, ClientItem> mClientMap;

    //mAdapter - recycler adapter
    private ClientItemAdapter mAdapter;

    //mPermissionHelper - helps making permission requests
    private PermissionHelper mPermissionHelper;

    //mRecursiveCount - recursive counter used to update client data
    private int mRecursiveCount;

    //mLoaderId - base loader id used to identify loader managers
    private int mLoaderId = 100;

    //mClientItem - client item used in recursion
    private ClientItem mClientItem;

    //mClients - client list used in recursion
    private ArrayList<ClientItem> mClients;


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

        //get user id
        mUserId = mBoss.getUserId();

        //check if "read contacts" permission has been granted
        mPermissionHelper = new PermissionHelper(activity);
        mPermissionHelper.requestPermission(PermissionHelper.READ_CONTACTS_REQUEST);

        //TODO - set up progress bar
        //showProgressBar(true);
        loadClients();

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
    private void initializeLayout(Cursor cursor){
        //initialize "empty" textView used when recycler is empty
        initializeEmptyText();

        //initialize FAB
        initializeFAB();

        //initialize adapter used by recycler
        initializeAdapter(cursor);
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
    private void initializeAdapter(Cursor cursor) {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_client;

        //create adapter consumed by the recyclerView
        mAdapter = new ClientItemAdapter(mActivity, cursor, adapterLayoutId);

        //set create context menu listener
        mAdapter.setOnCreateContextMenuListener(this);

        //set item click listener event
        mAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(view);
            }
        });

        //set adapter in recycler
        mBasicRecycler.setAdapter(mAdapter);

        //get client hashMap
        mClientMap = mAdapter.getClientMap();

        if(cursor != null){
            //check if recycler has any data; if not, display "empty" textView
            checkForEmptyRecycler(cursor.getCount());
        }
        else{
            checkForEmptyRecycler(true);
        }
    }

    /*
     * ContactListDialog initializeDialog(...) - show contacts list dialog
     */
    private ContactListDialog initializeDialog() {
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        ContactListDialog dia = new ContactListDialog();
        dia.setClientMap(mClientMap);
        dia.setUserId(mUserId);

        dia.show(fm, "diaContactList");

        return dia;
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
            mPermissionHelper.requestPermission(PermissionHelper.READ_CONTACTS_REQUEST);
        }
        else{
            //show contacts dialog
            initializeDialog();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onCreateContextMenu(...) - create context menu called from Adapter
 *      void fabClicked(View) - float action button click event
 */
/**************************************************************************************************/
    /*
     * void onCreateContextMenu(...) - create context menu called from Adapter. Depending on the status
     * of the client, an Activate or Retire menu will appear.
     */
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

/**************************************************************************************************/
/*
 * Class Methods
 *      void loadClients() - loads clients onto cursor used by Adapter
 *      void loadFirebaseClients() - gets client data from Firebase
 *      void requestContactInfo() - request device contact id and profile picture
 *      void updateDatabase(ClientItem) - update local database with client data
 */
/**************************************************************************************************/
    /*
     * void loadClients() - loads clients onto cursor used by Adapter. First gets clients from Firebase,
     * loads client data to sqlite local database and then retrieves and delivers that data as a cursor
     */
    private void loadClients(){
        // Initializes a loader for loading clients
        mActivity.getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                //load clients from firebase, insert into local database
                loadFirebaseClients();

                //request client cursor from local database
                Uri uri = Contractor.ClientEntry.buildClientByUID(mUserId);
                //get cursor
                return new CursorLoader(
                        mActivity,
                        uri,
                        ClientColumns.PROJECTION,
                        null,
                        null,
                        Contractor.ClientEntry.SORT_ORDER_DEFAULT);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {
                //client cursor loaded, initialize layout
                initializeLayout(c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
            }
        });
    }


    /*
     * void loadFirebaseClients() - gets client data from Firebase
     */
    private void loadFirebaseClients(){
        //get client firebase instance
        final ClientFirebaseHelper clientFB = ClientFirebaseHelper.getInstance();

        //get orderBy string value
        String orderBy = ClientFirebaseHelper.CHILD_CLIENT_NAME;

        //request client data ordered by client name
        clientFB.requestClientData(mUserId, orderBy, new ClientFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(DataSnapshot dataSnapshot) {
                //initialize client list array
                mClients = new ArrayList();

                //loop through client data
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //get the data from snapshot
                    ClientFBItem client = postSnapshot.getValue(ClientFBItem.class);

                    //create client item, used for local database
                    ClientItem item = new ClientItem(client);
                    item.uid = mUserId;
                    item.fkey = postSnapshot.getKey();

                    //add item to client list
                    mClients.add(item);
                }

                //recursive counter
                mRecursiveCount = 0;
                requestContactInfo();
            }

            @Override
            public void onCancelled() {

            }
        });

    }

    /*
     * void requestContactInfo() - request device contact id and profile picture
     */
    private void requestContactInfo(){

        //check if recursive count is greater than client list size
        if (mRecursiveCount < mClients.size()) {
            //not greater, get client from list
            mClientItem = mClients.get(mRecursiveCount);

            //create loader Id
            int loaderId = mLoaderId + mRecursiveCount;

            //request client's contact id and profile from device
            ContactsLoader.requestContactIdAndProfile(mActivity, loaderId, mClientItem,
                    new ContactsLoader.OnLoadContactInfoListener() {
                @Override
                public void onLoadContactInfo(ClientItem item) {
                    //update database
                    updateDatabase(item);
                }
            });

        }
    }

    /*
     * void updateDatabase(ClientItem) - update local database with client data
     */
    private void updateDatabase(ClientItem item){
        //get uri value for client
        Uri uriValue = Contractor.ClientEntry.CONTENT_URI;

        //add client to sqlite database
        mActivity.getContentResolver().insert(uriValue, item.getContentValues());

        //update client data in list
        mClients.set(mRecursiveCount, item);

        //destroy loader manager
        mActivity.getLoaderManager().destroyLoader(mLoaderId + (mRecursiveCount));

        //increase recursive count
        mRecursiveCount++;

        //request client info of clients in list
        requestContactInfo();

    }

/**************************************************************************************************/

}
