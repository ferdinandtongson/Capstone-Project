package me.makeachoice.gymratpta.view.dialog;

import android.database.Cursor;
import android.graphics.Rect;
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

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.recycler.BasicRecycler;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.ContactsAdapter;
import me.makeachoice.gymratpta.model.contract.contacts.ContactsColumns;
import me.makeachoice.gymratpta.model.item.ClientCardItem;
import me.makeachoice.gymratpta.model.item.ClientItem;
import me.makeachoice.gymratpta.model.item.ContactsItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * ContactListDialog display the list of contacts stored on the mobile device
 */
/**************************************************************************************************/

public class ContactListDialog extends DialogFragment implements RecyclerView.OnCreateContextMenuListener,
        MyActivity.OnContextItemSelectedListener{

/**************************************************************************************************/
/*
 * Class Variables
 *      mCreateContextMenuListener - "create context menu" event listener
 */
/**************************************************************************************************/

    private int LOADER_CONTACTS = 100;
    private TextView mTxtEmpty;
    private ProgressBar mProgressBar;
    private ContactsAdapter mAdapter;


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
 * Public Method
 *      String getExerciseName() - get exercise name from editText
 *      void setExerciseValues(String,String) - set exercise values used by dialog
 *      void setListeners(...) - set button onClick listeners
 */
/**************************************************************************************************/

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
 *      void create(MyActivity,Bundle) - called when Activity.onCreate is called
 *      void openBundle(Bundle) - opens bundle to set saved instance states during create()
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

        //load contact info
        loadContacts();

        return mRootView;
    }

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

        int dialogWidth = (int)(displayRectangle.width() * 0.9f); // specify a value here
        int dialogHeight = (int)(displayRectangle.height() * 0.8f); // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
   }

    @Override
    public void onDetach(){
        super.onDetach();
        mAdapter.closeCursor();
        getLoaderManager().destroyLoader(LOADER_CONTACTS);
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
 * Context Menu Methods - Implements ExerciseRecyclerAdapter.Bridge
 *      void contextMenuCreated(...) - context menu created in recycler adapter
 *      void contextMenuItemSelected(MenuItem) - menu item selected from context menu
 */
/**************************************************************************************************/
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    //get clientCardItem
    ContactsItem item = (ContactsItem)v.getTag(R.string.recycler_tagItem);

    //get client name
    String clientName = (item.contactName);

    //get context menu strings
    //String strActivate = mRootView.getString(R.string.context_menu_activate);
    //String strRetire = mRootView.getString(R.string.context_menu_retire);

    menu.add("Add: " + clientName);

    /*if(item.isActive){
        menu.add(0, CONTEXT_MENU_RETIRE, 0, strRetire);
    }
    else{
        menu.add(0, CONTEXT_MENU_ACTIVATE, 0, strActivate);
    }*/
}

    /*
     * boolean onMenuItemClick(MenuItem) - an item in the context menu has been clicked
     */
    public boolean onContextItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case CONTEXT_MENU_ACTIVATE:
                Log.d("Choice", "     activate");
                //TODO - need to reschedule session
                return true;
            case CONTEXT_MENU_RETIRE:
                Log.d("Choice", "     retire");
                //TODO - need to cancel session
                return true;
        }*/
        return false;
    }


    private ClientItem mClientItem;
    //private ContactsItem mContactItem;

    /*
     * void adapterItemLongClicked(MenuItem) - adapter item selected from list
     */
    public void contextMenuItemSelected(MenuItem item){
        /*String strActive = getString(R.string.client_active);

        mClientItem = new ClientItem();
        mClientItem.clientName = mContactItem.getContactName();
        mClientItem.clientStatus = strActive;

        mClientFB.addClient(mClientItem);
        addClientInfo();*/
    }

/**************************************************************************************************/

    private void loadContacts(){
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

    //private ClientInfoItem mInfoItem;

    /*private void addClientInfo(){
        mInfoItem = new ClientInfoItem();
        mInfoItem.clientName = mClientItem.getClientName();
        mInfoItem.clientStatus = mClientItem.getClientStatus();
        mInfoItem.firstSessionDate = "";
        mInfoItem.totalSessions = 0;
        mInfoItem.futureSessions = 0;
        mInfoItem.clientGoal = "";

        requestPhone();

    }*/

    /*private void requestPhone(){
        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(LOADER_PHONE, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                /*
                 * Makes search string into pattern and
                 * stores it in the selection array
                 */
                // Starts the query
                /*return new CursorLoader(
                        getContext(),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PROJECTION_PHONE,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + mContactItem.getContactId(),
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                String columnIndex = ContactsContract.CommonDataKinds.Phone.NUMBER;
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(columnIndex));
                    mInfoItem.clientPhone = phoneNumber;
                }
                else{
                    mInfoItem.clientPhone = "";
                }

                cursor.close();
                requestEmail();
                getLoaderManager().destroyLoader(LOADER_PHONE);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {}
        });

    }

    private void requestEmail(){
        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(LOADER_EMAIL, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                /*
                 * Makes search string into pattern and
                 * stores it in the selection array
                 */
                // Starts the query
                /*return new CursorLoader(
                        getContext(),
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        PROJECTION_PHONE,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + mContactItem.getContactId(),
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                String columnIndex = ContactsContract.CommonDataKinds.Email.DATA;
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    String email = cursor.getString(cursor.getColumnIndex(columnIndex));
                    mInfoItem.clientEmail = email;
                }
                else{
                    mInfoItem.clientEmail = "";
                }

                cursor.close();
                notifyBridge();
                getLoaderManager().destroyLoader(LOADER_EMAIL);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {}
        });

    }

    private void notifyBridge(){
        mBridge.addContactFromDialog(mInfoItem);
    }*/

}