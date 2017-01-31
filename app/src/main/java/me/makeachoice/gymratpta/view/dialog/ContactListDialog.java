package me.makeachoice.gymratpta.view.dialog;

import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.viewside.Helper.ContactsHelper;
import me.makeachoice.gymratpta.controller.viewside.recycler.BasicRecycler;
import me.makeachoice.gymratpta.model.item.ClientItem;

/**************************************************************************************************/
/*
 * ContactListDialog //todo - add description
 */

/**************************************************************************************************/

public class ContactListDialog extends DialogFragment{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private TextView mTxtTitle;
    private TextView mTxtEmpty;

    private String mUserId;

    private BasicRecycler mBasicRecycler;
    //private ContactsAdapter mAdapter;

    //mBridge - class implementing Bridge, typically a Maid class
    private Bridge mBridge;

    //Implemented communication line to a class
    public interface Bridge{
        //void addContactFromDialog(ClientInfoItem infoItem);
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/
    /*
     * EditAddRoutineExerciseDialog - constructor
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

    public void setTitle(String title){
        mTxtTitle.setText(title);
    }

    public void setRecycler(BasicRecycler recycler){
        mBasicRecycler = recycler;
    }

    public void initDialog(String userId, Bridge bridge){
        //mUserId = userId;
        //mBridge = bridge;
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Initialize Dialog
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeTextView(View) - initialize textView component for dialog
 *      void initializeEditText(View) - initialize EditText component for dialog
 *      void initializeButton(View) - initialize button components for dialog
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.standard_dia_recycler, container, false);

        //create Basic recycler class
        //mBasicRecycler = new BasicRecycler(rootView, R.id.choiceRecycler);

        initializeTitleView(rootView);
        initializeEmptyView(rootView);

        return rootView;
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
        //mAdapter.closeCursor();
        //getLoaderManager().destroyLoader(LOADER_CONTACTS);
    }

    /*
     * void initializeTextView(View) - initialize textView component for dialog
     */
    private void initializeTitleView(View rootView){
        //String strContacts = rootView.getResources().getString(R.string.contacts_list);
        String strContacts = "Contacts List";
        //get textView component
        mTxtTitle = (TextView)rootView.findViewById(R.id.txtTitle);

        mTxtTitle.setText(strContacts);

        //set title string value
        //mTxtTitle.setText(mTitle);

    }

    private void initializeEmptyView(View rootView){

        String msg = rootView.getResources().getString(R.string.add_client);


        mTxtEmpty = (TextView)rootView.findViewById(R.id.choiceEmptyView);

        mTxtEmpty.setText(msg);
        mTxtEmpty.setTextSize(24f);

        //TODO - need to add logic switch
        mTxtEmpty.setVisibility(View.GONE);
    }

    private void updateAdapter(Cursor cursor){
        /*if(mAdapter == null){
            initializeAdapter(cursor);
        }
        else{
            //TODO - swap cursor
            //mAdapter.setCursor(cursor);
        }*/
    }


    private void initializeAdapter(Cursor cursor){
        // Put the result Cursor in the adapter for the ListView
        //mAdapter = new ContactsAdapter(this.getContext(), this, cursor, R.layout.item_contact);
        initializeRecycler();
    }

    /*
     * void initializeRecycler() - initialize recyclerView component with adapter
     */
    private void initializeRecycler(){

        //create LayoutManager for RecyclerView, in this case a linear list type LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());


        //create item decoration, line divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(),
                layoutManager.getOrientation());

        //set manager, adapter and fixState values
        //mBasicRecycler.initializeRecycler(layoutManager, mAdapter, true);
        //ad line divider to recycler
        mBasicRecycler.getRecycler().addItemDecoration(dividerItemDecoration);
        mBasicRecycler.getRecycler().setItemAnimator(new DefaultItemAnimator());

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Context Menu Methods - Implements ExerciseRecyclerAdapter.Bridge
 *      void contextMenuCreated(...) - context menu created in recycler adapter
 *      void contextMenuItemSelected(MenuItem) - menu item selected from context menu
 */
/**************************************************************************************************/
    /*
     * void contextMenuCreated(...) - context menu created in recycler adapter
     */
    public void contextMenuCreated(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        //get item position that created context menu
        //int mItemPosition = (int)v.getTag(R.string.tag_position);
        //mContactItem = (ContactsItem)v.getTag(R.string.tag_item);

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