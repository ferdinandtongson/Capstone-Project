package me.makeachoice.gymratpta.view.dialog;

import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.firebase.ClientFirebaseHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.BasicRecycler;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.ContactsAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.contacts.ContactsColumns;
import me.makeachoice.gymratpta.model.item.ContactsItem;
import me.makeachoice.gymratpta.model.item.client.ClientFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;

/**************************************************************************************************/
/*
 * TODO - Look at moving contacts loader to a loader class helper
 */
/**************************************************************************************************/


/**************************************************************************************************/
/*
 * ContactListDialog display the list of contacts stored on the mobile device
 */
/**************************************************************************************************/

public class ContactListDialog extends DialogFragment implements RecyclerView.OnCreateContextMenuListener,
        MenuItem.OnMenuItemClickListener {

/**************************************************************************************************/
/*
 * Class Variables
 *      mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    private final int LOADER_CONTACTS = 100;
    private final int LOADER_PHONE = 101;
    private final int LOADER_EMAIL = 102;

    private TextView mTxtEmpty;
    private ProgressBar mProgressBar;
    private ContactsAdapter mAdapter;
    private HashMap<String,ClientItem> mClientMap;
    private String mUserId;

    private ClientFBItem mClientFB;


    private final int CONTEXT_MENU_ADD = 0;
    private final int CONTEXT_MENU_DUPLICATE = 1;
    private ContactsItem mContactsItem;


    private BasicRecycler mBasicRecycler;

    private View mRootView;

    //mCreateContextMenuListener - "create context menu" event listener
    private static View.OnCreateContextMenuListener mCreateContextMenuListener;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ContactListDialog - constructor
 */
/**************************************************************************************************/
    /*
     * ContactListDialog - constructor
     */
    public ContactListDialog(){
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setter Method
 *      void setuserId(String) - user authentication id
 *      void setClientMap(HashMap<String,ClientItem>) - hashmap of current clients
 *      void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
 */
/**************************************************************************************************/
    /*
     * void setUserId(String) - user authentication id
     */
    public void setUserId(String uid){
    mUserId = uid;
}

    /*
     * void setClientMap(HashMap<String,ClientItem>) - hashmap of current clients
     */
    public void setClientMap(HashMap<String,ClientItem> hashMap){
        mClientMap = hashMap;
    }

    /*
     * void setOnCreateContextMenuListener(...) - set listener for "create context menu" event
     */
    public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener){
        mCreateContextMenuListener = listener;
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Fragment Lifecycle Methods
 *      void create(...) - called when Activity.onCreate is called
 *      void onStart() - called when dialog start is request
 *      void onDetach() - called when fragment is detached from activity
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get layout resource id
        int layoutId = R.layout.standard_dia_recycler;

        //create dialog layout
        mRootView = inflater.inflate(layoutId, container, false);

        //initialize and show progress bar
        initializeProgressBar();

        //request contact info
        requestContacts();

        return mRootView;
    }

    /*
     * void onStart() - called when dialog start is request. Gets the devices screen dimensions and
     * then calculates the size of the dialog
     */
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = this.getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        //calculate dialog dimensions relative to screen size
        int dialogWidth = (int)(displayRectangle.width() * 0.9f); // specify a value here
        int dialogHeight = (int)(displayRectangle.height() * 0.8f); // specify a value here

        //set dialog size
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
   }

    /*
     * void onDetach() - called when fragment is detached from activity. Destroys loader manager and
     * closes the cursor in the adapter
     */
    @Override
    public void onDetach(){
        super.onDetach();

        //destroy loader manager
        getLoaderManager().destroyLoader(LOADER_CONTACTS);

        //close cursor
        mAdapter.closeCursor();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialize Dialog
 *      void initializeProgressBar() - initialize ProgressBar component
 *      void initializeLayout(Cursor) - initialize dialog layout, after contact data has been loaded
 *      void initializeTitleView() - initialize textView component for dialog title
 *      void initializeEmptyView() - initialize textView component shown when adapter is empty
 *      void initializeAdapter(Cursor) - initialize adapter with contacts cursor
 *      void initializeRecycler() - initialize recyclerView component with adapter
 *      void checkForEmptyRecycler(boolean) - checks whether to display "empty" message or not
 */
/**************************************************************************************************/
    /*
     * void initializeLayout(Cursor) - initialize dialog layout, after contact data has been loaded
     */
    private void initializeLayout(Cursor cursor){

        initializeTitleView();
        initializeEmptyView();
        initializeAdapter(cursor);
        initializeRecycler();
    }

    /*
     * void initializeProgressBar() - initialize ProgressBar component. Shown at the beginning as
     * the contacts info is loaded.
     */
    private void initializeProgressBar(){
        mProgressBar = (ProgressBar)mRootView.findViewById(R.id.choiceProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /*
     * void initializeTitleView() - initialize textView component for dialog title
     */
    private void initializeTitleView(){
        //get dialog title
        String strTitle = mRootView.getResources().getString(R.string.dialogContacts_title);

        //get textView component
        TextView txtTitle = (TextView)mRootView.findViewById(R.id.txtTitle);

        //set title
        txtTitle.setText(strTitle);
    }

    /*
     * void initializeEmptyView() - initialize textView component shown when adapter is empty
     */
    private void initializeEmptyView(){
        //get "empty" message
        String msg = mRootView.getResources().getString(R.string.emptyRecylcer_noContacts);

        //get textView component
        mTxtEmpty = (TextView)mRootView.findViewById(R.id.choiceEmptyView);

        //set message
        mTxtEmpty.setText(msg);
    }

    /*
     * void initializeAdapter(Cursor) - initialize adapter with contacts cursor
     */
    private void initializeAdapter(Cursor cursor){
        //get item layout
        int layoutId = R.layout.item_contact;

        //create adapter
        mAdapter = new ContactsAdapter(this.getContext(), cursor, layoutId);
        mAdapter.setOnCreateContextMenuListener(this);
    }

    /*
     * void initializeRecycler() - initialize recyclerView component with adapter
     */
    private void initializeRecycler(){
        //create recycler
        mBasicRecycler = new BasicRecycler(mRootView);

        //set adapter
        mBasicRecycler.setAdapter(mAdapter);

        if(mAdapter == null || mAdapter.getItemCount() <= 0){
            checkForEmptyRecycler(true);
        }
        else{
            checkForEmptyRecycler(false);
        }
    }

    /*
     * void checkForEmptyRecycler(boolean) - checks whether to display "empty" message or not
     */
    protected void checkForEmptyRecycler(boolean isEmpty){
        if(isEmpty){
            mTxtEmpty.setVisibility(View.VISIBLE);
        }
        else{
            mTxtEmpty.setVisibility(View.GONE);
        }
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Methods:
 *      void requestContacts() - request contacts info stored in device
 *      void requestPhone() - request phone number of given contact
 *      void requestEmail() - request email of given contact
 *      void putClientInFirebase() - add client to firebase database
 *      void putClientInDatabase(DataSnapshot, String) - add client data into database
 */
/**************************************************************************************************/
    /*
     * void requestContacts() - request contacts info stored in device
     */
    private void requestContacts(){

        /*ContactsLoader.requestContacts(getActivity(), LOADER_CONTACTS, new ContactsLoader.OnLoadFinishedListener() {
            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                mProgressBar.setVisibility(View.GONE);
                mCursor = cursor;
                initializeLayout(mCursor);
            }
        });*/

        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(LOADER_CONTACTS, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                //contacts sort order
                String sort = "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC";

                //get cursor
                return new CursorLoader(
                        getActivity(),
                        ContactsContract.Contacts.CONTENT_URI,
                        ContactsColumns.PROJECTION_CONTACTS,
                        null,
                        null,
                        sort);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {
                mProgressBar.setVisibility(View.GONE);
                initializeLayout(c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
            }
        });
    }

    /*
     * void requestPhone() - request phone number of given contact
     */
    private void requestPhone(){
        Log.d("Choice", "Dialog.requestPhone");
        /*ContactsLoader.requestPhoneInfo(getActivity(), LOADER_PHONE, mContactsItem.contactId, mClientFB,
                new ContactsLoader.OnLoadContactPhoneListener(){
            @Override
            public void onLoadContactPhone(ClientFBItem item) {
                mProgressBar.setVisibility(View.GONE);
                mClientFB = item;
                Log.d("Choice", "     phone: " + mClientFB.phone);
                requestEmail();
                getLoaderManager().destroyLoader(LOADER_PHONE);
            }
        });*/


        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(LOADER_PHONE, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                //get cursor
               return new CursorLoader(
                        getContext(),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        ContactsColumns.PROJECTION_EMAIL,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + mContactsItem.contactId,
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    String phoneNumber = cursor.getString(ContactsColumns.INDEX_PHONE);
                    mClientFB.phone = phoneNumber;
                }
                else{
                    mClientFB.phone = "";
                }

                cursor.close();
                getLoaderManager().destroyLoader(LOADER_PHONE);
                requestEmail();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {}
        });

    }

    /*
     * void requestEmail() - request email of given contact
     */
    private void requestEmail(){
        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(LOADER_EMAIL, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                //get cursor
                return new CursorLoader(
                        getContext(),
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        ContactsColumns.PROJECTION_EMAIL,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + mContactsItem.contactId,
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                String columnIndex = ContactsContract.CommonDataKinds.Email.DATA;
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    String email = cursor.getString(cursor.getColumnIndex(columnIndex));
                    mClientFB.email = email;
                }
                else{
                    mClientFB.email = "";
                }

                cursor.close();
                getLoaderManager().destroyLoader(LOADER_EMAIL);
                putClientInFirebase();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {}
        });

    }

    /*
     * void putClientInFirebase() - add client to firebase database
     */
    private void putClientInFirebase(){
        Log.d("Choice", "Dialog.putClientInFirebase");
        //get client firebase instance
        final ClientFirebaseHelper clientFB = ClientFirebaseHelper.getInstance();

        DatabaseReference ref = clientFB.getClientReference(mUserId);

        Log.d("Choice", "     name: " + mClientFB.clientName);
        //add client to firebase
        clientFB.addClient(mUserId, mClientFB);

        clientFB.requestClientDataByClientName(mUserId, mClientFB.clientName,
                new ClientFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(DataSnapshot dataSnapshot) {
                //mClientFB = dataSnapshot.getValue(ClientFBItem.class);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //create client item used for database
                    ClientItem item = new ClientItem(mClientFB);
                    item.uid = mUserId;
                    item.fkey = postSnapshot.getKey();
                    item.contactId = mContactsItem.contactId;
                    item.profilePic = mContactsItem.profilePic.toString();

                    putClientInDatabase(item);
                }
            }

            @Override
            public void onCancelled() {

            }
        });



    }

    /*
     * void putClientInDatabase(DataSnapshot, String) - add client data into database
     */
    private void putClientInDatabase(ClientItem item){
        mIsDoubleCall = true;
        Log.d("Choice", "ContactListDialog.putClientInDatabase");


        Log.d("Choice", "     client: " + item.clientName);

        //get client uri
        Uri uriValue = Contractor.ClientEntry.CONTENT_URI;

        Log.d("Choice", "     uri: " + uriValue.toString());

        //insert client into database
        Uri uri = mRootView.getContext().getContentResolver().insert(uriValue, item.getContentValues());

        Log.d("Choice", "     return: " + uri.toString());

        mClientMap.put(item.clientName, item);

        Log.d("Choice", "     map: " + mClientMap.size());

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Context Menu Methods - recieved from recycler adapter
 *      void onCreateContextMenu(...) - event received from recycler adapter
 *      void contextMenuItemSelected(MenuItem) - menu item selected from context menu
 */
 /**************************************************************************************************/
    /*
     * void onCreateContextMenu(...) - event received from recycler adapter
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        //get contacts item
        mContactsItem = (ContactsItem)v.getTag(R.string.recycler_tagItem);

        //get client name
        String clientName = (mContactsItem.contactName);

        //get context menu strings
        String strAlready = mRootView.getResources().getString(R.string.msg_already_client);
        String strAdd = mRootView.getResources().getString(R.string.msg_add);

        if(mClientMap.containsKey(clientName)){
            menu.add(0, CONTEXT_MENU_DUPLICATE, 0, clientName + " " + strAlready);
        }
        else{
            menu.add(0, CONTEXT_MENU_ADD, 0, strAdd + " " + clientName);
        }

        int count = menu.size();
        for(int i = 0; i < count; i++){
            menu.getItem(i).setOnMenuItemClickListener(this);
        }
    }

    /*
     * boolean onContextItemSelected(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_ADD:
                addClient();
                return true;
            case CONTEXT_MENU_DUPLICATE:
                //does nothing
                return true;
        }
        return false;
    }

    private boolean mIsDoubleCall;
    /*
     * void addClient() - add client to firebase and database
     */
    public void addClient(){
        mIsDoubleCall = false;

        String strActive = mRootView.getResources().getString(R.string.active);

        mClientFB = new ClientFBItem();
        mClientFB.clientName = mContactsItem.contactName;
        mClientFB.email = "";
        mClientFB.phone = "";
        mClientFB.firstSession = "";
        mClientFB.goals = "";
        mClientFB.status = strActive;

        //request client phone number
        requestPhone();

    }

/**************************************************************************************************/


}