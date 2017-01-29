package me.makeachoice.gymratpta.controller.viewside.housekeeper;

import android.os.Bundle;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.ClientRecyclerAdapter;
import me.makeachoice.gymratpta.model.item.ContactsItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  TODO - housekeeper description
 *  TODO - create activity class
 *  TODO - create activity layout
 *  TODO - define housekeeper id in res/values/strings.xml
 *  <!-- HouseKeeper Names -->
 *  <string name="housekeeper_main" translatable="false">Main HouseKeeper</string>
 *  TODO - initialize and register housekeeper in Boss class
 *  TODO - handle saved instance states from bundle
 *  TODO - initialize mobile layout components
 *  TODO - initialize activity components, for example
 *  TODO - initialize tablet layout components
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * TODO - housekeeper description,
 * Each housekeeper is responsible for an activity. It communicates directly with Boss, Activity
 * and Maids maintaining fragments within the Activity, if any.
 *
 * Variables from MyHouseKeeper:
 *      int TABLET_LAYOUT_ID - default id value defined in res/layout-sw600/*.xml to signify a tablet device
 *      MyActivity mActivity - current Activity
 *      int mActivityLayoutId - Activity resource layout id
 *      boolean mIsTablet - boolean flag used to determine if device is a tablet
 *
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

public class StubClientKeeper extends GymRatRecyclerKeeper implements MyActivity.Bridge{

/**************************************************************************************************/
/*
 * Class Variables:
 */
/**************************************************************************************************/

    ArrayList<ContactsItem> mData;
    private ClientRecyclerAdapter mAdapter;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * _templateKeeper - constructor
 */
/**************************************************************************************************/
    /*
     * _templateKeeper - constructor
     * @param layoutId - layout resource id used by Keeper
     */
    public StubClientKeeper(int layoutId){

        //get layout id
        mActivityLayoutId = layoutId;
        mSelectedNavItemId = R.id.nav_clients;
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
        //TODO - uncomment after Boss is defined
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
        //TODO - handle saved instance states from bundle
        //set saved instance state data
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Layout Initialization Methods:
 *      void initializeLayout() - initialize ui
 */
/**************************************************************************************************/
    /*
     * void initializeLayout() - initialize ui for mobile device
     */
    private void initializeLayout(){
        String emptyMsg = mActivity.getResources().getString(R.string.emptyRecycler_addClients);
        setEmptyMessage(emptyMsg);

        initializeAdapter();
    }

    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.item_client;

        mData = createContactsStub();

        //create adapter consumed by the recyclerView
        mAdapter = new ClientRecyclerAdapter(mActivity, adapterLayoutId);
        mAdapter.swapData(mData);

        mBasicRecycler.setAdapter(mAdapter);

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


/**************************************************************************************************/



    private ArrayList<ContactsItem> createContactsStub(){
        ArrayList<ContactsItem> contactList = new ArrayList();

        ContactsItem item01 = new ContactsItem();
        item01.contactName = "Quess Starbringer";
        item01.clientStatus = "Active";

        ContactsItem item02 = new ContactsItem();
        item02.contactName = "Quess Starbringer";
        item02.clientStatus = "Active";

        ContactsItem item03 = new ContactsItem();
        item03.contactName = "Quess Starbringer";
        item03.clientStatus = "Active";

        ContactsItem item04 = new ContactsItem();
        item04.contactName = "Quess Starbringer";
        item04.clientStatus = "Active";

        ContactsItem item05 = new ContactsItem();
        item05.contactName = "Quess Starbringer";
        item05.clientStatus = "Active";

        ContactsItem item06 = new ContactsItem();
        item06.contactName = "Quess Starbringer";
        item06.clientStatus = "Active";

        ContactsItem item07 = new ContactsItem();
        item07.contactName = "Quess Starbringer";
        item07.clientStatus = "Active";

        ContactsItem item08 = new ContactsItem();
        item08.contactName = "Quess Starbringer";
        item08.clientStatus = "Active";

        ContactsItem item09 = new ContactsItem();
        item09.contactName = "Quess Starbringer";
        item09.clientStatus = "Active";

        ContactsItem item10 = new ContactsItem();
        item10.contactName = "Quess Starbringer";
        item10.clientStatus = "Active";

        contactList.add(item01);
        contactList.add(item02);
        contactList.add(item03);
        contactList.add(item04);
        contactList.add(item05);
        contactList.add(item06);
        contactList.add(item07);
        contactList.add(item08);
        contactList.add(item09);
        contactList.add(item10);

        return contactList;
    }

}
