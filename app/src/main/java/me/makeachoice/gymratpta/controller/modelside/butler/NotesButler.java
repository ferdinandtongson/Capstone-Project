package me.makeachoice.gymratpta.controller.modelside.butler;

import android.database.Cursor;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.modelside.firebase.client.NotesFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.NotesLoader;
import me.makeachoice.gymratpta.controller.modelside.query.NotesQueryHelper;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.item.client.NotesFBItem;
import me.makeachoice.gymratpta.model.item.client.NotesItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 *  NotesButler assists in loading, saving and deleting client notes to database and firebase
 */
/**************************************************************************************************/

public class NotesButler {

/**************************************************************************************************/
/*
 *  Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user authentication id
    private String mUserId;

    //mNotesList - list of notes loaded from database
    private ArrayList<NotesItem> mNotesList;

    //mDeleteItem - delete item from database
    private NotesItem mDeleteItem;


    //mLoaderId - loader id number
    private int mLoaderId;

    //mNotesLoader - loader class used to load notes data
    private NotesLoader mNotesLoader;

    //mLoadListener - used to listen for load events
    private OnLoadedListener mLoadListener;
    public interface OnLoadedListener{
        public void onLoaded(ArrayList<NotesItem> notesList);
    }

    //mSaveListener - used to listen for save events
    private OnSavedListener mSaveListener;
    public interface OnSavedListener{
        public void onSaved();
    }

    //mDeleteListener - used to listen for delete events
    private OnDeletedListener mDeleteListener;
    public interface OnDeletedListener{
        public void onDeleted();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public NotesButler(MyActivity activity, String userId, String clientKey){
        mActivity = activity;
        mUserId = userId;

        mNotesList = new ArrayList<>();
        mNotesLoader = new NotesLoader(mActivity, mUserId, clientKey);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Notes
 */
/**************************************************************************************************/
    /*
     * void loadNotes() - load notes data for given day from database
     */
    public void loadNotes(int loaderId, OnLoadedListener listener){
        mLoaderId = loaderId;
        mLoadListener = listener;

        //start loader to get notes data from database
        mNotesLoader.loadNotesByClientKey(mLoaderId, new NotesLoader.OnNotesLoadListener() {
            @Override
            public void onNotesLoadFinished(Cursor cursor) {
                onNotesLoaded(cursor);
            }
        });
    }

    /*
     * void onNotesLoaded(Cursor) - data from database has been loaded
     */
    private void onNotesLoaded(Cursor cursor){
        //clear list array
        mNotesList.clear();

        //get number of scheduled appointments loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create item from cursor data
            NotesItem item = new NotesItem(cursor);

            //add item to list
            mNotesList.add(item);
        }

        //destroy loader
        mNotesLoader.destroyLoader(mLoaderId);

        if(mLoadListener != null){
            mLoadListener.onLoaded(mNotesList);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Save Notes
 */
/**************************************************************************************************/

    public void saveNotes(NotesItem saveItem, OnSavedListener listener){
        //save listener
        mSaveListener = listener;

        //save to firebase
        saveToFirebase(saveItem);

        //save to local database
        saveToDatabase(saveItem);

    }

    /*
     * void saveToNotesFB(NotesItem) - save notes to notes firebase
     */
    private void saveToFirebase(NotesItem saveItem){
        //get firebase helper instance
        NotesFirebaseHelper fbHelper = NotesFirebaseHelper.getInstance();

        //create firebase item
        NotesFBItem fbItem = new NotesFBItem();
        fbItem.appointmentTime = saveItem.appointmentTime;
        fbItem.modifiedDate = saveItem.modifiedDate;
        fbItem.subjectiveNotes = saveItem.subjectiveNotes;
        fbItem.objectiveNotes = saveItem.objectiveNotes;
        fbItem.assessmentNotes = saveItem.assessmentNotes;
        fbItem.planNotes = saveItem.planNotes;

        //save notes item to firebase
        fbHelper.addNotesByTimestamp(mUserId, saveItem.clientKey, saveItem.timestamp, fbItem);

    }

    /*
     * void saveToDatabase(...) - save notes to local database
     */
    private void saveToDatabase(NotesItem saveItem){
        //get uri value for routine name table
        Uri uriValue = NotesContract.CONTENT_URI;

        //appointment is new, add notes to database
        mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        if(mSaveListener != null){
            mSaveListener.onSaved();
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Notes
 */
/**************************************************************************************************/
    /*
     * void deleteNotes(...) - delete notes from firebase
     */
    public void deleteNotes(NotesItem deleteItem, OnDeletedListener listener){
        mDeleteListener = listener;
        mDeleteItem = deleteItem;

        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String timestamp = deleteItem.timestamp;

        //get notes firebase helper instance
        NotesFirebaseHelper notesFB = NotesFirebaseHelper.getInstance();
        //delete notes from firebase
        notesFB.deleteNotes(mUserId, clientKey, timestamp, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deleteNotesFromDatabase(mDeleteItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

    }

    /*
     * void deleteNotesFromDatabase(...) - delete notes data from database
     */
    private void deleteNotesFromDatabase(NotesItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String appDate = deleteItem.appointmentDate;
        String appTime = deleteItem.appointmentTime;

        //get uri value from notes table
        Uri uri = NotesContract.CONTENT_URI;

        //remove notes from database
        mActivity.getContentResolver().delete(uri,
                NotesQueryHelper.clientKeyDateTimeSelection,
                new String[]{mUserId, clientKey, appDate, appTime});

        if(mDeleteListener != null){
            mDeleteListener.onDeleted();
        }
    }

/**************************************************************************************************/

}
