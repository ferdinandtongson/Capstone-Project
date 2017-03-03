package me.makeachoice.gymratpta.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.NotesItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * NotesDialog is used for displaying SOAP notes
 */
/**************************************************************************************************/

public class NotesDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public final static int MODE_READ = 0;
    public final static int MODE_EDIT = 1;
    public final static int MODE_ADD = 2;

    private NotesItem mNotesItem;

    private ScrollView mScrNotes;

    private TextView mTxtDateTime;
    private TextView mTxtNotes;
    private EditText mEdtNotes;
    private TextView mTxtSave;
    private TextView mTxtCancel;
    private View mRootView;

    private MyActivity mActivity;
    private String mUserId;

    private ArrayList<String> mSoapNotes;
    private String[] mSoapArray;
    private int mMode;
    private int mOldIndex;

    private View.OnClickListener mOnCancelListener;

    private OnSaveNotesListener mOnSaveListener;
    public interface OnSaveNotesListener{
        public void onSaveNotes(ArrayList<String> notes);
    }

    private OnDismissListener mDismissListener;
    public interface OnDismissListener{
        public void onDismiss(DialogInterface dialogInterface);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/
    /*
     * NotesDialog - constructor
     */
    public NotesDialog(){}

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 *      void setDialogValues(MyActivity,String) - set dialog values
 *      void setOnSaveListener(...) - set listener for onSave event
 *      void setOnCancelListener(...) - set listener for onCancel event
 */
/**************************************************************************************************/
    /*
     * void setDialogValues(MyActivity,String) - set dialog values
     */
    public void setDialogValues(MyActivity activity, String userId, int mode, NotesItem item ){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;

        mNotesItem = item;

        //initialize soap array
        mSoapArray = mActivity.getResources().getStringArray(R.array.soap_notes);

        //initialize soap notes buffer
        initializeSoapNotes();

        //get dialog mode
        mMode = mode;

        //previous notes index
        mOldIndex = 0;

    }

    /*
     * void setOnSaveListener(...) - set listener for onSave event
     */
    public void setOnSaveListener(OnSaveNotesListener saveListener){
        mOnSaveListener = saveListener;
    }

    /*
     * void setOnCancelListener(...) - set listener for onCancel event
     */
    public void setOnCancelListener(View.OnClickListener cancelListener){
        mOnCancelListener = cancelListener;
    }

    /*
     * void setOnDismissListener(...) - set listener for dialog dismiss events
     */
    public void setOnDismissListener(OnDismissListener listener){
        mDismissListener = listener;
    }


/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Initialize Dialog
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeDateTimeView() - initialize date/time textView component
 *      void initializeNotesView() - initialize notes related components
 *      void initializeButtonView() - initialize "save" and "cancel" button textView components
 *      void initializeSpinner() - initialize spinner component used to select SOAP note to display
 *      void initializeModeUI() - shows and hides view components depending on dialog mode
 *      void initializeSoapNotes() - initialize soap notes buffer
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //used to handle bug with onCreateView being called twice
        mRootView = inflater.inflate(R.layout.dia_client_notes, container, false);

        //initialize textView component
        initializeDateTimeView();
        initializeNotesView();
        initializeButtonView();
        initializeSpinner();
        initializeModeUI();
        return mRootView;
    }

    /*
     * void initializeDateTimeView() - initialize date/time textView component
     */
    private void initializeDateTimeView(){
        //create date/time string value
        String dateTime = mNotesItem.appointmentDate + " (" + mNotesItem.appointmentTime + ")";

        //get textView component
        mTxtDateTime = (TextView)mRootView.findViewById(R.id.diaNotes_txtDateTime);

        //set title string value
        mTxtDateTime.setText(dateTime);
    }

    /*
     * void initializeNotesView() - initialize notes related components
     */
    private void initializeNotesView(){
        //get textView and scrollView component
        mTxtNotes = (TextView)mRootView.findViewById(R.id.diaNotes_txtNotes);
        mScrNotes = (ScrollView)mRootView.findViewById(R.id.diaNotes_scrNotes);

        //get editText component
        mEdtNotes = (EditText)mRootView.findViewById(R.id.diaNotes_edtNotes);
    }

    /*
     * void initializeButtonView() - initialize "save" and "cancel" button textView components
     */
    private void initializeButtonView(){
        //get textView components used as buttons
        mTxtSave = (TextView)mRootView.findViewById(R.id.diaNotes_txtSave);
        mTxtCancel = (TextView)mRootView.findViewById(R.id.diaNotes_txtCancel);

        //set onSaveClick listener
        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSoapNotes.set(mOldIndex, mEdtNotes.getText().toString());
                mOnSaveListener.onSaveNotes(mSoapNotes);
            }
        });

        //set onCancelClick listener
        mTxtCancel.setOnClickListener(mOnCancelListener);
    }

    /*
     * void initializeSpinner() - initialize spinner component used to select SOAP note to display
     */
    private void initializeSpinner(){
        //get spinner component
        Spinner spnNotes = (Spinner)mRootView.findViewById(R.id.diaNotes_spnNotes);

        //create adapter consumed by spinner
        ArrayAdapter<String> adpNotes = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, mSoapArray);
        adpNotes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set onItemSelected listener
        spnNotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //notify note was selected
                onNotesSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //does nothing
            }
        });

        //set adapter to spinner
        spnNotes.setAdapter(adpNotes);
    }

    /*
     * void initializeModeUI() - shows and hides view components depending on dialog mode
     */
    private void initializeModeUI(){
        //check mode
        if(mMode == MODE_READ){
            //read mode, display textView and scrollView
            mTxtNotes.setVisibility(View.VISIBLE);
            mScrNotes.setVisibility(View.VISIBLE);

            //hide editText and textView buttons
            mEdtNotes.setVisibility(View.GONE);
            mTxtSave.setVisibility(View.GONE);
            mTxtCancel.setVisibility(View.GONE);
        }
        else{
            //add or edit mode, hide textView and scrollView
            mTxtNotes.setVisibility(View.GONE);
            mScrNotes.setVisibility(View.GONE);

            //show editText and textView buttons
            mEdtNotes.setVisibility(View.VISIBLE);
            mTxtSave.setVisibility(View.VISIBLE);
            mTxtCancel.setVisibility(View.VISIBLE);
        }
    }

    /*
     * void initializeSoapNotes() - initialize soap notes buffer
     */
    private void initializeSoapNotes(){
        mSoapNotes = new ArrayList<>();
        mSoapNotes.add(mNotesItem.subjectiveNotes);
        mSoapNotes.add(mNotesItem.objectiveNotes);
        mSoapNotes.add(mNotesItem.assessmentNotes);
        mSoapNotes.add(mNotesItem.planNotes);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void onNotesSelected(int) - user selected notes type from spinner component
 */
/**************************************************************************************************/
    /*
     * void onNotesSelected(int) - user selected notes type from spinner component
     */
    private void onNotesSelected(int index){
        //get notes for note type selected
        String notes = mSoapNotes.get(index);

        //check dialog mode
        if(mMode == MODE_READ){
            //read mode, show notes in textView
            mTxtNotes.setText(notes);
        }
        else{
            //edit or add mode, save notes before changing notes
            mSoapNotes.set(mOldIndex, mEdtNotes.getText().toString());

            //display note from type selected
            mEdtNotes.setText(notes);

            //save note index selected
            mOldIndex = index;
        }
    }

    /*
     * void onDismiss(DialogInterface) - dialog dismiss event occurred
     */
    @Override
    public void onDismiss(DialogInterface dialogInterface){
        //check for listener
        if(mDismissListener != null){
            //notify listener for dismiss event
            mDismissListener.onDismiss(dialogInterface);
        }
    }

/**************************************************************************************************/


}