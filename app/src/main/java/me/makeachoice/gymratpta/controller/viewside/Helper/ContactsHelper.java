package me.makeachoice.gymratpta.controller.viewside.Helper;

/**************************************************************************************************/
/*
 * TODO - toast is not showing, need to debug
 */
/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ContactsHelper helps in accessing Contacts info from the users phone
 */

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/

public class ContactsHelper implements LoaderManager.LoaderCallbacks<Cursor> {

/**************************************************************************************************/
/*
 * Class Variables:
 *      ALL_REQUEST - identifier for requesting all permissions
 *      READ_CONTACTS_PERMISSIONS_REQUEST - identifier for request contact permission
 */
/**************************************************************************************************/

    private static final int LOADER_CONTACTS = 0;
    private static final int LOADER_PHONE = 1;
    private static final int LOADER_EMAIL = 2;


    private static final String[] PROJECTION_CONTACTS = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    private static final String[] PROJECTION_PHONE = {
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private static final String[] PROJECTION_EMAIL = {
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Email.TYPE
    };

    private MyActivity mActivity;
    private int mRequest;

    private OnContactsLoadedListener mOnContactsLoadedListener;
    public interface OnContactsLoadedListener{
        void onContactsLoaded(Cursor c);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ContactsHelper - constructor
 */
/**************************************************************************************************/

    public ContactsHelper(MyActivity activity){
        mActivity = activity;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Methods
 *      void requestContacts() - request contacts list
 *      void getPermissionToReadContacts() - request permission to read contacts info
 */
/**************************************************************************************************/
    /*
     * void requestContacts() - request contacts list
     */
    public void requestContacts(OnContactsLoadedListener listener){
        mRequest = LOADER_CONTACTS;
        mOnContactsLoadedListener = listener;
        // Initializes a loader for loading the contacts
        mActivity.getSupportLoaderManager().initLoader(LOADER_CONTACTS, null, this);
    }

    public void destroyLoader(){
        mActivity.getSupportLoaderManager().destroyLoader(mRequest);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch(loaderId){
            case LOADER_CONTACTS:
                String sort = "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC";
                // Starts the query
                return new CursorLoader(
                        mActivity,
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        sort);
            case LOADER_PHONE:
                return new CursorLoader(
                        mActivity,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PROJECTION_PHONE,
                        //ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + mContactItem.getContactId(),
                        null,
                        null,
                        null);
            case LOADER_EMAIL:
                return new CursorLoader(
                        mActivity,
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        PROJECTION_EMAIL,
                        //ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + mContactItem.getContactId(),
                        null,
                        null,
                        null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {
        if(mRequest == LOADER_CONTACTS && mOnContactsLoadedListener != null){
            mOnContactsLoadedListener.onContactsLoaded(c);
        }
        Log.d("Choice", "ContactsHelper.onLoadFinished: " + c.getCount());
        /*String columnIndex = ContactsContract.CommonDataKinds.Phone.NUMBER;
        if( c != null &&  c.getCount() > 0){
            c.moveToFirst();
            String phoneNumber =  c.getString( c.getColumnIndex(columnIndex));
            mInfoItem.clientPhone = phoneNumber;
        }
        else{
            mInfoItem.clientPhone = "";
        }

        c.close();
        requestEmail();
        getLoaderManager().destroyLoader(LOADER_PHONE);*/

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        /*if(mAdapter != null){
            //TODO - swap cursor
            //mAdapter.swapCursor(null);
        }*/
    }

}
