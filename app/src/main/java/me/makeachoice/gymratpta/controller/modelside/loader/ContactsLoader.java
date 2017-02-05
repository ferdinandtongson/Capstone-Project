package me.makeachoice.gymratpta.controller.modelside.loader;

import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import me.makeachoice.gymratpta.model.contract.contacts.ContactsColumns;
import me.makeachoice.gymratpta.model.item.client.ClientFBItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * ContactsLoader handles request to load contact information from the device
 */
/**************************************************************************************************/

public class ContactsLoader {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/
    
    //mActivity - activity context
    private static MyActivity mActivity;

    //private static FragmentActivity mFActivity;
    
    //mClientItem - client item object
    private static ClientItem mClientItem;

    private static ClientFBItem mClientFB;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Listeners
 */
/**************************************************************************************************/

    private static OnLoadFinishedListener mLoadFinishedListener;
    public interface OnLoadFinishedListener{
        void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor);
    }

    private static OnLoadContactInfoListener mLoadContactInfoListener;
    public interface OnLoadContactInfoListener{
        public void onLoadContactInfo(ClientItem item);
    }

    private static OnLoadContactPhoneListener mLoadContactPhoneListener;
    public interface OnLoadContactPhoneListener{
        public void onLoadContactPhone(ClientFBItem item);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Request Methods
 *      void requestContactIdAndProfile(...) - request contact id and profile picture of given client
 */
/**************************************************************************************************/
    /*
     * void requestContactIdAndProfile(...) - request contact id and profile picture of given client
     */
    public static void requestContactIdAndProfile(MyActivity activity, int loaderId,
                                                  ClientItem item, OnLoadContactInfoListener listener){
        //get activity context
        mActivity = activity;

        //get client item
        mClientItem = item;

        //get listener
        mLoadContactInfoListener = listener;

        // Initializes a loader for loading the contacts
        activity.getSupportLoaderManager().initLoader(loaderId, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                //get contacts info with client name
                return new CursorLoader(
                        mActivity,
                        ContactsContract.Contacts.CONTENT_URI,
                        ContactsColumns.PROJECTION_CONTACTS,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = '" + mClientItem.clientName + "'",
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    //get contact id
                    long contactId = cursor.getLong(ContactsColumns.INDEX_ID);

                    //add contact id to client item
                    mClientItem.contactId = contactId;
                    //add profile picture uri to client item
                    mClientItem.profilePic = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId).toString();

                    cursor.close();
                }

                //notify listener
                mLoadContactInfoListener.onLoadContactInfo(mClientItem);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {}
        });

    }

    public static void requestContacts(final FragmentActivity fActivity, int loaderId,  OnLoadFinishedListener listener){
        mLoadFinishedListener = listener;

        fActivity.getSupportLoaderManager().initLoader(loaderId, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                //contacts sort order
                String sort = "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC";

                //get cursor
                return new CursorLoader(
                        fActivity,
                        ContactsContract.Contacts.CONTENT_URI,
                        ContactsColumns.PROJECTION_CONTACTS,
                        null,
                        null,
                        sort);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {
                mLoadFinishedListener.onLoadFinished(objectLoader, c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
            }
        });

    }

    private static long mContactId;
    public static void requestPhoneInfo(final FragmentActivity fActivity, final int loaderId, long contactId,
                                        ClientFBItem item, OnLoadContactPhoneListener listener) {
        mContactId = contactId;
        mClientFB = item;
        fActivity.getSupportLoaderManager().initLoader(loaderId, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                //get cursor
                return new CursorLoader(
                        fActivity,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        ContactsColumns.PROJECTION_EMAIL,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + mContactId,
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

                mLoadContactPhoneListener.onLoadContactPhone(mClientItem);
                cursor.close();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {}
        });
    }

}
