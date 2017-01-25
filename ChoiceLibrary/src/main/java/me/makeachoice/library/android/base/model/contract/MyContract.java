package me.makeachoice.library.android.base.model.contract;

/**************************************************************************************************/
/*
 * MyContract is an Abstract base class used to create Contractor classes that contain definitions
 * for the URIs, column names, MIME types, and other meta-data about the ContentProvider. It also
 * contain static helper methods to manipulate the URIs
 */
/**************************************************************************************************/

public class MyContract {

/**************************************************************************************************/
/*
 * ContentProvider
 */
/**************************************************************************************************/
    /*
     * To query a content provider, you specify the query string in the form of a URI which has the
     * following format: <prefix>://<authority>/<data type/<id>
     *     prefix - always set to "content://"
     *     authority - specifies name of the content provider (ie com.tutorialspoint.statusprovider)
     *     data_type - type of data, if getting contacts from Contacts content provider, then the
     *     		data path would be "people" and the URI would be "content://contacts/people"
     *     id - specifies record id requested; if you are looking for contact number 5 in the Contacts
     *     		content provider then the URI would be "content://contacts/people/5
     */
    protected static final String CONTENT_PREFIX = "content://";

    /*
     * The MIME types returned by ContentProvider.getType have two distinct parts: <type>/<subType>
     * The <type> portion indicates type that is returned for a given URI by the ContentProvider, as
     * the query methods con only return Cursors the type should always be:
     * 		vnd.android.cursor.dir - Cursor to contain 0 through infinite items
     * 		vnd.android.cursor.item - Cursor to contain 1 item
     * The <subType> can be either a well known subtype or something unique to your application. For
     * example:
     * 		vnd.android.cursor.dir/vnd.com.example.xyzreader.items
     * 		vnd.android.cursor.item/vnd.com.example.xyzreader.items
     */
    protected static final String CONTENT_MIME_TYPE = "vnd.android.cursor.dir/";
    protected static final String CONTENT_MIME_ITEM_TYPE = "vnd.android.cursor.item/";

/**************************************************************************************************/

}
