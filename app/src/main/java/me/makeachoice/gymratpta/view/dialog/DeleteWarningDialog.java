package me.makeachoice.gymratpta.view.dialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import me.makeachoice.gymratpta.R;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_DELETE_WARNING;

/**************************************************************************************************/
/*
 * DeleteWarningDialog displays a dialog warning the user of a deletion request
 */
/**************************************************************************************************/

public class DeleteWarningDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;

    //mUserId - user id number from firebase authentication
    private String mUserId;

    private String mItemName;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    private OnDismissListener mDismissListener;
    public interface OnDismissListener{
        public void onDismiss(DialogInterface dialogInterface);
    }

    private OnDeleteListener mDeleteListener;
    public interface OnDeleteListener{
        public void onDelete();
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * DeleteWarningDialog Constructor
 */
/**************************************************************************************************/
    /*
     * DeleteWarningDialog - constructor
     */
    public DeleteWarningDialog(){
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setter Method
 *      void setDialogValues(...) - set dialog values
 *      void setOnDismissListener(...) - set listener for dialog dismiss events
 *      void setOnDeleteListener(...) - set listener for delete events
 */
/**************************************************************************************************/
    /*
     * void setDialogValues(...) - set dialog values
     */
    public void setDialogValues(MyActivity activity, String userId, String itemName){
        //get activity context
        mActivity = activity;

        //get user id from firebase authentication
        mUserId = userId;

        //get item name
        mItemName = itemName;
    }

    /*
     * void setOnDismissListener(...) - set listener for dialog dismiss events
     */
    public void setOnDismissListener(OnDismissListener listener){
        mDismissListener = listener;
    }

    /*
     * void setOnDeleteListener(...) - set listener for delete events
     */
    public void setOnDeleteListener(OnDeleteListener listener){
        mDeleteListener = listener;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeTitleTextView() - initialize textView title component
 *      void initializeButtonTextView() - initialize textView button component
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //layout resource id number
        int layoutId = R.layout.dia_delete_message;

        //inflate root view, parent child components
        mRootView = inflater.inflate(layoutId, container, false);

        //initialize title textView components
        initializeTitleTextView();

        //initialize button textView component
        initializeButtonTextView();

        //initialize checkBox component
        initializeCheckBos();

        return mRootView;
    }

    /*
     * void initializeTitleTextView() - initialize textView title component
     */
    private void initializeTitleTextView(){
        //get title string value
        String strDelete = mActivity.getString(R.string.delete);

        //get title textView component
        TextView txtTitle = (TextView)mRootView.findViewById(R.id.diaDelete_txtTitle);

        //set title value
        txtTitle.setText(strDelete + ": " + mItemName);

    }

    /*
     * void initializeButtonTextView() - initialize textView button component
     */
    private void initializeButtonTextView(){
        //get delete textView component
        TextView txtDelete = (TextView)mRootView.findViewById(R.id.diaDelete_txtDelete);

        //set delete listener
        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked(view);
            }
        });

        //get cancel textView component
        TextView txtCancel = (TextView)mRootView.findViewById(R.id.diaDelete_txtCancel);

        //set cancel listener
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initializeCheckBos(){
        //get checkBox component
        CheckBox chkWarning = (CheckBox)mRootView.findViewById(R.id.diaDelete_chkNoShow);

        //set listener for on check change event
        chkWarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onShowWarningStatusChanged(b);
            }
        });

    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Method
 *      void onDeleteClicked() - delete textView component was clicked
 *      void onShowWarningStatusChanged(boolean) - "delete warning" dialog status has changed
 *      void onDismiss(DialogInterface) - dialog dismiss event occurred
 */
/**************************************************************************************************/
    /*
     * void onDeleteClicked() - delete textView component was clicked
     */
    private void onDeleteClicked(View view){
        //check for listener
        if(mDeleteListener != null){
            //notify listener of delete click event
            mDeleteListener.onDelete();
        }
    }

    /*
     * void onShowWarningStatusChanged(boolean) - "delete warning" dialog status has changed
     */
    private void onShowWarningStatusChanged(boolean showWarning){
        //get user shared preferences
        SharedPreferences.Editor editor = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE).edit();

        //set "delete warning" preference (to show or not)
        editor.putBoolean(PREF_DELETE_WARNING, !showWarning);

        //commit preference
        editor.commit();
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
