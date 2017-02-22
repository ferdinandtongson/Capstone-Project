package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.firebase.client.NotesFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.loader.NotesLoader;
import me.makeachoice.gymratpta.controller.modelside.query.NotesQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.NotesAdapter;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.AppointmentItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.NotesFBItem;
import me.makeachoice.gymratpta.model.item.client.NotesItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.dialog.NotesDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * TODO - add description
 * TODO - need to prevent addition of soap notes for same session
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *
 * Methods from MyMaid:
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      String getKey() - get maid key value
 *      Fragment getFragment() - get new instance fragment
 */
/**************************************************************************************************/

public class ClientNotesMaid extends GymRatRecyclerMaid implements BasicFragment.Bridge  {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private String mUserId;
    private MyActivity mActivity;
    private ClientItem mClientItem;
    private AppointmentItem mAppointmentItem;
    private NotesItem mSaveItem;

    private ArrayList<NotesItem> mData;
    private NotesAdapter mAdapter;

    private boolean mEditingNotes;
    private NotesLoader mNotesLoader;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientNotesMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientNotesMaid(...) - constructor
     */
    public ClientNotesMaid(String maidKey, int layoutId, String userId, ClientItem clientItem,
                           AppointmentItem appItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;

        mAppointmentItem = appItem;

        mEditingNotes = false;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 *  MyMaid Implementation
 *      View createView(LayoutInflater,ViewGroup,Bundle) - called by Fragment onCreateView()
 *      void createActivity(Bundle) - is called by Fragment onCreateActivity(...)
 *      void detach() - fragment is being disassociated from activity
 */
/**************************************************************************************************/
    /*
     * View createView(LayoutInflater, ViewGroup, Bundle) - is called by Fragment when onCreateView(...)
     * is called in that class. Prepares the Fragment View to be presented.
     */
    public View createView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState){
        //inflate fragment layout
        mLayout = inflater.inflate(mLayoutId, container, false);

        //return fragment
        return mLayout;
    }

    /*
     * void createActivity(Bundle) - is called by Fragment when onCreateActivity(...) is called in
     * that class. Sets child views in fragment before being seen by the user
     * @param bundle - saved instance states
     */
    @Override
    public void activityCreated(Bundle bundle){
        super.activityCreated(bundle);

        mActivity = (MyActivity)mFragment.getActivity();

        mData = new ArrayList<>();
        mNotesLoader = new NotesLoader(mActivity, mUserId, mClientItem.fkey);

        loadNotes();
        //mData = NotesStubData.createData(mActivity);
        //prepareFragment();
    }

    /*
     * void detach() - fragment is being disassociated from activity
     */
    @Override
    public void detach(){
        //fragment is being disassociated from Activity
        super.detach();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods:
 *      void prepareFragment(View) - prepare components and data to be displayed by fragment
 *      void initializeRecycler() - initialize recycler to display exercise items
 *      EditAddDialog initializeDialog(...) - create exercise edit/add dialog
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        initializeEmptyText();

        initializeAdapter();

        initializeRecycler();

        initializeFAB();
    }

    /*
     * void initializeEmptyText() - textView used when recycler is empty
     */
    private void initializeEmptyText(){
        //get "empty" textView message
        String emptyMsg = mFragment.getResources().getString(R.string.emptyRecycler_noNotes);

        //set message to textView
        setEmptyMessage(emptyMsg);
    }

    /*
     * void initializeAdapter() - adapter used by recycler component
     */
    private void initializeAdapter() {
        //layout resource file id used by recyclerView adapter
        int adapterLayoutId = R.layout.card_client_notes;

        //create adapter consumed by the recyclerView
        mAdapter = new NotesAdapter(mFragment.getActivity(), adapterLayoutId);

        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                onItemClicked(index);
            }
        });

        mAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //get item index
                int index = (int)view.getTag(R.string.recycler_tagPosition);

                //edit appointment item
                onEditNotes(index);

                return false;
            }
        });

        //swap client data into adapter
        mAdapter.swapData(mData);

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
     * void initializeFAB() - initialize FAB component
     */
    private void initializeFAB(){
        //get content description string value
        String description = mFragment.getString(R.string.description_fab_appointment);

        //add content description to FAB
        mFAB.setContentDescription(description);

        //add listener for onFABClick events
        setOnClickFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onFABClick event occurred
                onFabClicked(view);
            }
        });
    }

    /*
     * NotesDialog initializeNotesDialog - initialize client notes dialog
     */
    private NotesDialog initializeNotesDialog(int mode, NotesItem item){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mNotesDialog = new NotesDialog();
        mNotesDialog.setDialogValues(mActivity, mUserId, mode, item);

        if(mode != NotesDialog.MODE_READ){
            mNotesDialog.setOnSaveListener(new NotesDialog.OnSaveNotesListener() {
                @Override
                public void onSaveNotes(ArrayList<String> notes) {
                    onNotesSaved(notes);
                }
            });

            mNotesDialog.setOnCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNotesCanceled();
                }
            });

            mNotesDialog.setCancelable(false);
        }
        else{
            mNotesDialog.setCancelable(true);
        }

        mNotesDialog.show(fm, "diaScheduleAppointment");

        return mNotesDialog;
    }

    private NotesDialog mNotesDialog;

    /*
     * void updateEmptyText() - check if adapter is empty or not then updates empty textView
     */
    private void updateEmptyText(){
        if(mAdapter.getItemCount() > 0){
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
 *      void loadNotes() - load client notes data from database
 *      void onNotesLoaded(Cursor) - notes from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadNotes() - load client notes data from database
     */
    private void loadNotes(){
        //start loader to get appointment data from database
        mNotesLoader.loadNotesByClientKey(new NotesLoader.OnNotesLoadListener() {
            @Override
            public void onNotesLoadFinished(Cursor cursor) {
                onNotesLoaded(cursor);
            }
        });
    }

    /*
     * void onNotesLoaded(Cursor) - notes from database has been loaded
     */
    private void onNotesLoaded(Cursor cursor){
        //clear notes list array
        mData.clear();

        //get number of notes loaded
        int count = cursor.getCount();

        //loop through cursor
        for(int i = 0; i < count; i++){
            //move cursor to index position
            cursor.moveToPosition(i);

            //create notes item from cursor data
            NotesItem item = new NotesItem(cursor);

            //add notes item to list
            mData.add(item);
        }

        //destroy loader
        mNotesLoader.destroyLoader();

        //load client data
        prepareFragment();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
 *      void onProfileImageClicked(View) - profile image clicked, open client info screen
 *      void onEditAppointment() - edit appointment item
 *      void onSaveAppointment(...) - save appointment item
 *      void onAppointmentDeleteRequest(int) - delete appointment requested
 */
/**************************************************************************************************/
    /*
     * void onFabClicked(View) - floating action button clicked, open schedule appointment dialog
     */
    private void onFabClicked(View view){
        mEditingNotes = true;

        mSaveItem = new NotesItem();
        mSaveItem.uid = mUserId;
        mSaveItem.clientKey = mClientItem.fkey;
        mSaveItem.appointmentDate = mAppointmentItem.appointmentDate;
        mSaveItem.appointmentTime = mAppointmentItem.appointmentTime;
        mSaveItem.modifiedDate = mAppointmentItem.appointmentDate;
        mSaveItem.subjectiveNotes = "";
        mSaveItem.objectiveNotes = "";
        mSaveItem.assessmentNotes = "";
        mSaveItem.planNotes = "";

        initializeNotesDialog(NotesDialog.MODE_ADD, mSaveItem);
    }

    /*
     * void onEditNotes() - edit notes item
     */
    private void onEditNotes(int index){
        mEditingNotes = true;

        mSaveItem = mData.get(index);

        initializeNotesDialog(NotesDialog.MODE_EDIT, mSaveItem);
    }


    private void onItemClicked(int index){
        NotesItem item = mData.get(index);

        initializeNotesDialog(NotesDialog.MODE_READ, item);
    }

    private void onNotesCanceled(){
        mNotesDialog.dismiss();
    }

    private void onNotesSaved(ArrayList<String> notes){
        mSaveItem.subjectiveNotes = notes.get(0);
        mSaveItem.objectiveNotes = notes.get(1);
        mSaveItem.assessmentNotes = notes.get(2);
        mSaveItem.planNotes = notes.get(3);

        if(mEditingNotes){
            mSaveItem.modifiedDate = DateTimeHelper.getToday();
            deleteNotes(mSaveItem);
        }
        else{
            saveNotes(mSaveItem);
        }

        mNotesDialog.dismiss();
    }

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Save Methods
 *      void saveNotes(NotesItem) - save notes item
 *      void saveAppointmentToFirebase(NotesItem) - save notes to firebase
 *      void saveAppointmentToDatabase(...) - save notes to local database
 */
/**************************************************************************************************/
    /*
     * void saveNotes(NotesItem) - save notes item
     */
    private void saveNotes(NotesItem saveItem){
        //save to firebase
        saveNotesToFirebase(saveItem);

        //save to local database
        saveNotesToDatabase(saveItem);

    }

    /*
     * void saveNotesToFirebase(NotesItem) - save notes to firebase
     */
    private void saveNotesToFirebase(NotesItem saveItem){
        //get notes firebase helper instance
        NotesFirebaseHelper notesFB = NotesFirebaseHelper.getInstance();

        //create notes firebase item
        NotesFBItem notesFBItem = new NotesFBItem();
        notesFBItem.appointmentTime = saveItem.appointmentTime;
        notesFBItem.modifiedDate = saveItem.modifiedDate;
        notesFBItem.subjectiveNotes = saveItem.subjectiveNotes;
        notesFBItem.objectiveNotes = saveItem.objectiveNotes;
        notesFBItem.assessmentNotes = saveItem.assessmentNotes;
        notesFBItem.planNotes = saveItem.planNotes;

        //save notes item to firebase
        notesFB.addNotesByDate(mUserId, saveItem.clientKey, saveItem.appointmentDate, notesFBItem);

    }

    /*
     * void saveNotesToDatabase(...) - save notes to local database
     */
    private void saveNotesToDatabase(NotesItem saveItem){
        //get uri value for routine name table
        Uri uriValue = Contractor.NotesEntry.CONTENT_URI;

        //appointment is new, add notes to database
        Uri uri = mActivity.getContentResolver().insert(uriValue, saveItem.getContentValues());

        //load notes to refresh recycler view
        loadNotes();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Methods
 *      void deleteNotes(NotesItem) - delete notes
 *      void deleteNotesFromFirebase(...) - delete notes from firebase
 *      void deleteNotesFromDatabase(...) - delete notes data from database
 */
/**************************************************************************************************/
    /*
     * void deleteNotes(NotesItem) - delete notes
     */
    private void deleteNotes(NotesItem deleteItem){
        deleteNotesFromFirebase(deleteItem);
    }

    /*
     * void deleteNotesFromFirebase(...) - delete notes from firebase
     */
    private void deleteNotesFromFirebase(NotesItem deleteItem){
        //create string values used to delete notes
        String clientKey = deleteItem.clientKey;
        String appDate = deleteItem.appointmentDate;
        final String appTime = deleteItem.appointmentTime;

        //get notes firebase helper instance
        NotesFirebaseHelper notesFB = NotesFirebaseHelper.getInstance();
        //delete notes from firebase
        notesFB.deleteNotes(mUserId, clientKey, appDate, appTime, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    NotesFBItem notes = postSnapshot.getValue(NotesFBItem.class);
                    if(notes.appointmentTime.equals(appTime)){
                        postSnapshot.getRef().removeValue();
                    }
                }

                if(mEditingNotes){
                    saveNotesToFirebase(mSaveItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //does nothing
            }
        });

        deleteNotesFromDatabase(mSaveItem);
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
        Uri uri = Contractor.NotesEntry.CONTENT_URI;

        //remove notes from database
        int notesDeleted = mActivity.getContentResolver().delete(uri,
                NotesQueryHelper.clientKeyDateTimeSelection,
                new String[]{mUserId, clientKey, appDate, appTime});

        if(mEditingNotes) {
            //editing, save new notes used to replace old notes
            saveNotesToDatabase(mSaveItem);
        }else{
            //load notes to update recycler view
            loadNotes();
        }
    }

/**************************************************************************************************/

}