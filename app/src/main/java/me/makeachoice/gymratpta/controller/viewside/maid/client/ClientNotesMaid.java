package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.NotesButler;
import me.makeachoice.gymratpta.controller.viewside.maid.GymRatRecyclerMaid;
import me.makeachoice.gymratpta.controller.viewside.recycler.adapter.client.NotesAdapter;
import me.makeachoice.gymratpta.model.item.client.ScheduleItem;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.model.item.client.NotesItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.gymratpta.view.dialog.NotesDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static me.makeachoice.gymratpta.controller.manager.Boss.DIA_NOTES_RECORD;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_NOTES;

/**************************************************************************************************/
/*
 * ClientNotesMaid display a list of SOAP notes of a given client
 *
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
    private NotesItem mSaveItem;

    private ArrayList<NotesItem> mData;
    private NotesAdapter mAdapter;
    private int mActiveNoteIndex;

    private boolean mEditingNotes;
    private NotesButler mNotesButler;
    private NotesDialog mNotesDialog;

    private NotesButler.OnLoadedListener mOnLoadListener =
            new NotesButler.OnLoadedListener() {
                @Override
                public void onLoaded(ArrayList<NotesItem> notesList) {
                    onNotesLoaded(notesList);
                }
            };


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
                           ScheduleItem scheduleItem){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;

        mClientItem = clientItem;

        mEditingNotes = false;

        mActiveNoteIndex = -1;

        createSaveNote(scheduleItem);
    }

    /*
     * void createSaveNote(ScheduleItem) - creates a blank NotesItem used for the current session if needed
     */
    private void createSaveNote(ScheduleItem appItem){
        mSaveItem = new NotesItem();
        mSaveItem.uid = mUserId;
        mSaveItem.clientKey = mClientItem.fkey;
        mSaveItem.timestamp = appItem.datestamp + " " + appItem.appointmentTime;
        mSaveItem.appointmentDate = appItem.appointmentDate;
        mSaveItem.appointmentTime = appItem.appointmentTime;
        mSaveItem.modifiedDate = appItem.appointmentDate;
        mSaveItem.subjectiveNotes = "";
        mSaveItem.objectiveNotes = "";
        mSaveItem.assessmentNotes = "";
        mSaveItem.planNotes = "";
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
        mNotesButler = new NotesButler(mActivity, mUserId, mClientItem.fkey);

    }

    public void start(){
        super.start();
        mNotesButler.loadNotes(LOADER_NOTES, mOnLoadListener);
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
 *      void initializeEmptyText() - textView used when recycler is empty
 *      void initializeAdapter() - adapter used by recycler component
 *      void initializeRecycler() - initialize recycler component
 *      NotesDialog initializeNotesDialog - initialize client notes dialog
 *      void updateEmptyText() - check if adapter is empty or not then updates empty textView
 */
/**************************************************************************************************/
    /*
     * void prepareFragment(View) - prepare components and data to be displayed by fragment
     */
    private void prepareFragment(){
        initializeEmptyText();

        initializeAdapter();

        initializeRecycler();
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
        mAdapter.setActiveIndex(mActiveNoteIndex);

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

        mNotesDialog.show(fm, DIA_NOTES_RECORD);

        return mNotesDialog;
    }

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
    private void onNotesLoaded(ArrayList<NotesItem> notesList){

        mData.clear();
        mActiveNoteIndex = -1;

        //get number of saved client notes
        int count = notesList.size();

        //loop through notes
        for(int i = 0; i < count; i++){
            //get note item
            NotesItem item = notesList.get(i);

            //check if note item is equal to appointment schedule timestamp
            if(item.timestamp.equals(mSaveItem.timestamp)){
                //save index of item in list as active
                mActiveNoteIndex = i;
                //exit loop
                i = count;
            }
        }

        //check if active index was found
        if(mActiveNoteIndex == -1){
            //no active index, add blank notes object to data list
            mData.add(mSaveItem);
            //set active index to blank notes object
            mActiveNoteIndex = 0;
        }

        //add saved client notes
        mData.addAll(notesList);

        //load client data
        prepareFragment();

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Class Methods
 *      void onEditNotes() - edit notes item
 *      void onItemClicked(int) - notes item click event occurred, display notes dialog
 *      void onNotesCanceled() - dialog cancel onClick event
 *      void onNotesSaved(ArrayList<String>) - dialog save onClick event
 */
/**************************************************************************************************/
    /*
     * void onEditNotes() - edit notes item
     */
    private void onEditNotes(int index){
        mEditingNotes = true;

        mSaveItem = mData.get(index);

        initializeNotesDialog(NotesDialog.MODE_EDIT, mSaveItem);
    }

    /*
     * void onItemClicked(int) - notes item click event occurred, display notes dialog
     */
    private void onItemClicked(int index){
        NotesItem item = mData.get(index);

        if(index == mActiveNoteIndex){
            mEditingNotes = true;
            initializeNotesDialog(NotesDialog.MODE_EDIT, item);
        }
        else{
            mEditingNotes = false;
            initializeNotesDialog(NotesDialog.MODE_READ, item);
        }
    }

    /*
     * void onNotesCanceled() - dialog cancel onClick event
     */
    private void onNotesCanceled(){
        mNotesDialog.dismiss();
    }

    /*
     * void onNotesSaved(ArrayList<String>) - dialog save onClick event
     */
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
 */
/**************************************************************************************************/
    /*
     * void saveNotes(NotesItem) - save notes item
     */
    private void saveNotes(NotesItem saveItem){
        mNotesButler.saveNotes(saveItem, new NotesButler.OnSavedListener() {
            @Override
            public void onSaved() {
                mNotesButler.loadNotes(LOADER_NOTES, mOnLoadListener);
            }
        });
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Delete Methods
 *      void deleteNotes(NotesItem) - delete notes
 *      void onNotesDeleted() - notes have been deleted from database
 */
/**************************************************************************************************/
    /*
     * void deleteNotes(NotesItem) - delete notes
     */
    private void deleteNotes(NotesItem deleteItem){
        mNotesButler.deleteNotes(deleteItem, new NotesButler.OnDeletedListener() {
            @Override
            public void onDeleted() {
                onNotesDeleted();
            }
        });
    }

    /*
     * void onNotesDeleted() - notes have been deleted from database
     */
    private void onNotesDeleted(){
        if(mEditingNotes) {
            //editing, save new notes used to replace old notes
            saveNotes(mSaveItem);
        }else{
            //load notes to update recycler view
            mNotesButler.loadNotes(LOADER_NOTES, mOnLoadListener);
        }

    }

/**************************************************************************************************/

}