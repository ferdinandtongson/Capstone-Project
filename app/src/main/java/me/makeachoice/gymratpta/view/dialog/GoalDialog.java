package me.makeachoice.gymratpta.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * GoalDialog is used for inputting client goals
 */
/**************************************************************************************************/

public class GoalDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private EditText mEdtGoals;
    private TextView mTxtSave;
    private TextView mTxtCancel;
    private View mRootView;

    private MyActivity mActivity;
    private String mUserId;
    private String mGoals;

    private View.OnClickListener mOnCancelListener;

    private OnSaveListener mOnSaveListener;
    public interface OnSaveListener{
        void onSaveGoals(String goals);
    }

    private OnDismissListener mDismissListener;
    public interface OnDismissListener{
        void onDismiss(DialogInterface dialogInterface);
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
    public GoalDialog(){}

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
    public void setDialogValues(MyActivity activity, String userId, ClientItem item){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;

        mGoals = item.goals;
    }

    /*
     * void setOnSaveListener(...) - set listener for onSave event
     */
    public void setOnSaveListener(OnSaveListener saveListener){
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
        mRootView = inflater.inflate(R.layout.dia_goal, container, false);

        //initialize textView component
        initializeGoalsView();
        initializeButtonView();
        return mRootView;
    }

    /*
     * void initializeNotesView() - initialize notes related components
     */
    private void initializeGoalsView(){
        //get editText component
        mEdtGoals = (EditText)mRootView.findViewById(R.id.diaGoals_edtGoals);
        mEdtGoals.setText(mGoals);
    }

    /*
     * void initializeButtonView() - initialize "save" and "cancel" button textView components
     */
    private void initializeButtonView(){
        //get textView components used as buttons
        mTxtSave = (TextView)mRootView.findViewById(R.id.diaGoals_txtSave);
        mTxtCancel = (TextView)mRootView.findViewById(R.id.diaGoals_txtCancel);

        //set onSaveClick listener
        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnSaveListener != null){
                    mOnSaveListener.onSaveGoals(mEdtGoals.getText().toString());
                    dismiss();
                }
            }
        });

        //set onCancelClick listener
        mTxtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 *      void onNotesSelected(int) - user selected notes type from spinner component
 */
/**************************************************************************************************/

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