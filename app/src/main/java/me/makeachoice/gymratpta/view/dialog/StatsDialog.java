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
import me.makeachoice.gymratpta.model.item.client.StatsItem;
import me.makeachoice.gymratpta.utilities.DateTimeHelper;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * StatsDialog is used for displaying client stats
 */
/**************************************************************************************************/

public class StatsDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public final static int MODE_READ = 0;
    public final static int MODE_EDIT = 1;

    private StatsItem mStatsItem;
    private StatsItem mPreviousItem;

    private TextView mTxtDateTime;

    private TextView mTxtWeight;
    private TextView mTxtPrevWeight;
    private EditText mEdtWeight;

    private TextView mTxtBodyFat;
    private TextView mTxtPrevBodyFat;
    private EditText mEdtBodyFat;

    private TextView mTxtBMI;
    private TextView mTxtPrevBMI;
    private EditText mEdtBMI;

    private TextView mTxtNeck;
    private TextView mTxtPrevNeck;
    private EditText mEdtNeck;

    private TextView mTxtChest;
    private TextView mTxtPrevChest;
    private EditText mEdtChest;

    private TextView mTxtRBicep;
    private TextView mTxtPrevRBicep;
    private EditText mEdtRBicep;

    private TextView mTxtLBicep;
    private TextView mTxtPrevLBicep;
    private EditText mEdtLBicep;

    private TextView mTxtWaist;
    private TextView mTxtPrevWaist;
    private EditText mEdtWaist;

    private TextView mTxtNavel;
    private TextView mTxtPrevNavel;
    private EditText mEdtNavel;

    private TextView mTxtHips;
    private TextView mTxtPrevHips;
    private EditText mEdtHips;

    private TextView mTxtRThigh;
    private TextView mTxtPrevRThigh;
    private EditText mEdtRThigh;

    private TextView mTxtLThigh;
    private TextView mTxtPrevLThigh;
    private EditText mEdtLThigh;

    private TextView mTxtRCalf;
    private TextView mTxtPrevRCalf;
    private EditText mEdtRCalf;

    private TextView mTxtLCalf;
    private TextView mTxtPrevLCalf;
    private EditText mEdtLCalf;

    private TextView mTxtSave;
    private TextView mTxtCancel;

    private View mRootView;

    private MyActivity mActivity;
    private String mUserId;

    private int mMode;

    private View.OnClickListener mOnCancelListener;

    private OnSaveListener mOnSaveListener;
    public interface OnSaveListener{
        public void onSave(StatsItem saveItem);
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
    public StatsDialog(){}

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
    public void setDialogValues(MyActivity activity, String userId, int mode, StatsItem item,
                                StatsItem prevItem){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;

        mStatsItem = item;
        mPreviousItem = prevItem;

        //get dialog mode
        mMode = mode;


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
        mRootView = inflater.inflate(R.layout.dia_client_stats, container, false);

        //initialize textView component
        initializeDateTimeView();
        initializeStatsView();
        initializeButtonView();
        initializeModeUI();
        initializeValues();
        return mRootView;
    }

    /*
     * void initializeDateTimeView() - initialize date/time textView component
     */
    private void initializeDateTimeView(){
        //create date/time string value
        String dateTime = mStatsItem.appointmentDate + " (" + mStatsItem.appointmentTime + ")";

        //get textView component
        mTxtDateTime = (TextView)mRootView.findViewById(R.id.diaStats_txtDateTime);

        //set title string value
        mTxtDateTime.setText(dateTime);
    }

    /*
     * void initializeStatsView() - initialize stats related components
     */
    private void initializeStatsView(){
        //get components
        mTxtWeight = (TextView)mRootView.findViewById(R.id.diaStats_txtWeightStat);
        mTxtPrevWeight = (TextView)mRootView.findViewById(R.id.diaStats_txtWeightPrevious);
        mEdtWeight = (EditText)mRootView.findViewById(R.id.diaStats_edtWeightStat);

        mTxtBodyFat = (TextView)mRootView.findViewById(R.id.diaStats_txtBodyFatStat);
        mTxtPrevBodyFat = (TextView)mRootView.findViewById(R.id.diaStats_txtBodyFatPrevious);
        mEdtBodyFat = (EditText)mRootView.findViewById(R.id.diaStats_edtBodyFatStat);

        mTxtBMI = (TextView)mRootView.findViewById(R.id.diaStats_txtBMIStat);
        mTxtPrevBMI = (TextView)mRootView.findViewById(R.id.diaStats_txtBMIPrevious);
        mEdtBMI = (EditText)mRootView.findViewById(R.id.diaStats_edtBMIStat);

        mTxtNeck = (TextView)mRootView.findViewById(R.id.diaStats_txtNeckStat);
        mTxtPrevNeck = (TextView)mRootView.findViewById(R.id.diaStats_txtNeckPrevious);
        mEdtNeck = (EditText)mRootView.findViewById(R.id.diaStats_edtNeckStat);

        mTxtChest = (TextView)mRootView.findViewById(R.id.diaStats_txtChestStat);
        mTxtPrevChest = (TextView)mRootView.findViewById(R.id.diaStats_txtChestPrevious);
        mEdtChest = (EditText)mRootView.findViewById(R.id.diaStats_edtChestStat);

        mTxtRBicep = (TextView)mRootView.findViewById(R.id.diaStats_txtRBicepStat);
        mTxtPrevRBicep = (TextView)mRootView.findViewById(R.id.diaStats_txtRBicepPrevious);
        mEdtRBicep = (EditText)mRootView.findViewById(R.id.diaStats_edtRBicepStat);

        mTxtLBicep = (TextView)mRootView.findViewById(R.id.diaStats_txtLBicepStat);
        mTxtPrevLBicep = (TextView)mRootView.findViewById(R.id.diaStats_txtLBicepPrevious);
        mEdtLBicep = (EditText)mRootView.findViewById(R.id.diaStats_edtLBicepStat);

        mTxtWaist = (TextView)mRootView.findViewById(R.id.diaStats_txtWaistStat);
        mTxtPrevWaist = (TextView)mRootView.findViewById(R.id.diaStats_txtWaistPrevious);
        mEdtWaist = (EditText)mRootView.findViewById(R.id.diaStats_edtWaistStat);

        mTxtNavel = (TextView)mRootView.findViewById(R.id.diaStats_txtNavelStat);
        mTxtPrevNavel = (TextView)mRootView.findViewById(R.id.diaStats_txtNavelPrevious);
        mEdtNavel = (EditText)mRootView.findViewById(R.id.diaStats_edtNavelStat);

        mTxtHips = (TextView)mRootView.findViewById(R.id.diaStats_txtHipsStat);
        mTxtPrevHips = (TextView)mRootView.findViewById(R.id.diaStats_txtHipsPrevious);
        mEdtHips = (EditText)mRootView.findViewById(R.id.diaStats_edtHipsStat);

        mTxtRThigh = (TextView)mRootView.findViewById(R.id.diaStats_txtRThighStat);
        mTxtPrevRThigh = (TextView)mRootView.findViewById(R.id.diaStats_txtRThighPrevious);
        mEdtRThigh = (EditText)mRootView.findViewById(R.id.diaStats_edtRThighStat);

        mTxtLThigh = (TextView)mRootView.findViewById(R.id.diaStats_txtLThighStat);
        mTxtPrevLThigh = (TextView)mRootView.findViewById(R.id.diaStats_txtLThighPrevious);
        mEdtLThigh = (EditText)mRootView.findViewById(R.id.diaStats_edtLThighStat);

        mTxtRCalf = (TextView)mRootView.findViewById(R.id.diaStats_txtRCalfStat);
        mTxtPrevRCalf = (TextView)mRootView.findViewById(R.id.diaStats_txtRCalfPrevious);
        mEdtRCalf = (EditText)mRootView.findViewById(R.id.diaStats_edtRCalfStat);

        mTxtLCalf = (TextView)mRootView.findViewById(R.id.diaStats_txtLCalfStat);
        mTxtPrevLCalf = (TextView)mRootView.findViewById(R.id.diaStats_txtLCalfPrevious);
        mEdtLCalf = (EditText)mRootView.findViewById(R.id.diaStats_edtLCalfStat);
    }

    /*
     * void initializeButtonView() - initialize "save" and "cancel" button textView components
     */
    private void initializeButtonView(){
        //get textView components used as buttons
        mTxtSave = (TextView)mRootView.findViewById(R.id.diaStats_txtSave);
        mTxtCancel = (TextView)mRootView.findViewById(R.id.diaStats_txtCancel);

        //set onSaveClick listener
        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveStats();
            }
        });

        //set onCancelClick listener
        mTxtCancel.setOnClickListener(mOnCancelListener);
    }


    /*
     * void initializeModeUI() - shows and hides view components depending on dialog mode
     */
    private void initializeModeUI(){
        int editVisibility;
        int readVisibility;

        if(mMode == MODE_READ){
            readVisibility = View.VISIBLE;
            editVisibility = View.INVISIBLE;
            mTxtSave.setVisibility(View.GONE);
            mTxtCancel.setVisibility(View.GONE);
        }
        else{
            readVisibility = View.INVISIBLE;
            editVisibility = View.VISIBLE;
            mTxtSave.setVisibility(View.VISIBLE);
            mTxtCancel.setVisibility(View.VISIBLE);
        }


        mTxtWeight.setVisibility(readVisibility);
        mEdtWeight.setVisibility(editVisibility);

        mTxtBodyFat.setVisibility(readVisibility);
        mEdtBodyFat.setVisibility(editVisibility);

        mTxtBMI.setVisibility(readVisibility);
        mEdtBMI.setVisibility(editVisibility);

        mTxtNeck.setVisibility(readVisibility);
        mEdtNeck.setVisibility(editVisibility);

        mTxtChest.setVisibility(readVisibility);
        mEdtChest.setVisibility(editVisibility);

        mTxtRBicep.setVisibility(readVisibility);
        mEdtRBicep.setVisibility(editVisibility);

        mTxtLBicep.setVisibility(readVisibility);
        mEdtLBicep.setVisibility(editVisibility);

        mTxtWaist.setVisibility(readVisibility);
        mEdtWaist.setVisibility(editVisibility);

        mTxtNavel.setVisibility(readVisibility);
        mEdtNavel.setVisibility(editVisibility);

        mTxtHips.setVisibility(readVisibility);
        mEdtHips.setVisibility(editVisibility);

        mTxtRThigh.setVisibility(readVisibility);
        mEdtRThigh.setVisibility(editVisibility);

        mTxtLThigh.setVisibility(readVisibility);
        mEdtLThigh.setVisibility(editVisibility);

        mTxtRCalf.setVisibility(readVisibility);
        mEdtRCalf.setVisibility(editVisibility);

        mTxtLCalf.setVisibility(readVisibility);
        mEdtLCalf.setVisibility(editVisibility);

    }

    private void initializeValues(){
        mTxtWeight.setText(String.valueOf(mStatsItem.statWeight));
        mTxtPrevWeight.setText(getPrevious(mPreviousItem.statWeight));
        mEdtWeight.setText(getStat(mStatsItem.statWeight));

        mTxtBodyFat.setText(String.valueOf(mStatsItem.statBodyFat));
        mTxtPrevBodyFat.setText(getPrevious(mPreviousItem.statBodyFat));
        mEdtBodyFat.setText(getStat(mStatsItem.statBodyFat));

        mTxtBMI.setText(String.valueOf(mStatsItem.statBMI));
        mTxtPrevBMI.setText(getPrevious(mPreviousItem.statBMI));
        mEdtBMI.setText(getStat(mStatsItem.statBMI));

        mTxtNeck.setText(String.valueOf(mStatsItem.statNeck));
        mTxtPrevNeck.setText(getPrevious(mPreviousItem.statNeck));
        mEdtNeck.setText(getStat(mStatsItem.statNeck));

        mTxtChest.setText(String.valueOf(mStatsItem.statChest));
        mTxtPrevChest.setText(getPrevious(mPreviousItem.statChest));
        mEdtChest.setText(getStat(mStatsItem.statChest));

        mTxtRBicep.setText(String.valueOf(mStatsItem.statRBicep));
        mTxtPrevRBicep.setText(getPrevious(mPreviousItem.statRBicep));
        mEdtRBicep.setText(getStat(mStatsItem.statRBicep));

        mTxtLBicep.setText(String.valueOf(mStatsItem.statLBicep));
        mTxtPrevLBicep.setText(getPrevious(mPreviousItem.statLBicep));
        mEdtLBicep.setText(getStat(mStatsItem.statLBicep));

        mTxtWaist.setText(String.valueOf(mStatsItem.statWaist));
        mTxtPrevWaist.setText(getPrevious(mPreviousItem.statWaist));
        mEdtWaist.setText(getStat(mStatsItem.statWaist));

        mTxtNavel.setText(String.valueOf(mStatsItem.statNavel));
        mTxtPrevNavel.setText(getPrevious(mPreviousItem.statNavel));
        mEdtNavel.setText(getStat(mStatsItem.statNavel));

        mTxtHips.setText(String.valueOf(mStatsItem.statHips));
        mTxtPrevHips.setText(getPrevious(mPreviousItem.statHips));
        mEdtHips.setText(getStat(mStatsItem.statHips));

        mTxtRThigh.setText(String.valueOf(mStatsItem.statRThigh));
        mTxtPrevRThigh.setText(getPrevious(mPreviousItem.statRThigh));
        mEdtRThigh.setText(getStat(mStatsItem.statRThigh));

        mTxtLThigh.setText(String.valueOf(mStatsItem.statLThigh));
        mTxtPrevLThigh.setText(getPrevious(mPreviousItem.statLThigh));
        mEdtLThigh.setText(getStat(mStatsItem.statLThigh));

        mTxtRCalf.setText(String.valueOf(mStatsItem.statRCalf));
        mTxtPrevRCalf.setText(getPrevious(mPreviousItem.statRCalf));
        mEdtRCalf.setText(getStat(mStatsItem.statRCalf));

        mTxtLCalf.setText(String.valueOf(mStatsItem.statLCalf));
        mTxtPrevLCalf.setText(getPrevious(mPreviousItem.statLCalf));
        mEdtLCalf.setText(getStat(mStatsItem.statLCalf));
    }

    private String getPrevious(double statPrev){
        if (statPrev != 0){
            return String.valueOf(statPrev);
        }
        return "-";
    }

    private String getStat(double stat){
        if(stat != 0){
            return String.valueOf(stat);
        }
        return "";
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Event Methods
 */
/**************************************************************************************************/

    private void onSaveStats(){
        mStatsItem.modifiedDate = DateTimeHelper.getDatestamp(0);
        mStatsItem.statWeight = convertEditTextToStat(mEdtWeight.getText().toString());
        mStatsItem.statBodyFat = convertEditTextToStat(mEdtBodyFat.getText().toString());
        mStatsItem.statBMI = convertEditTextToStat(mEdtBMI.getText().toString());
        mStatsItem.statNeck = convertEditTextToStat(mEdtNeck.getText().toString());
        mStatsItem.statChest = convertEditTextToStat(mEdtChest.getText().toString());
        mStatsItem.statRBicep = convertEditTextToStat(mEdtRBicep.getText().toString());
        mStatsItem.statLBicep = convertEditTextToStat(mEdtLBicep.getText().toString());
        mStatsItem.statWaist = convertEditTextToStat(mEdtWaist.getText().toString());
        mStatsItem.statNavel = convertEditTextToStat(mEdtNavel.getText().toString());
        mStatsItem.statHips = convertEditTextToStat(mEdtHips.getText().toString());
        mStatsItem.statRThigh = convertEditTextToStat(mEdtRThigh.getText().toString());
        mStatsItem.statLThigh = convertEditTextToStat(mEdtLThigh.getText().toString());
        mStatsItem.statRCalf = convertEditTextToStat(mEdtRCalf.getText().toString());
        mStatsItem.statLCalf = convertEditTextToStat(mEdtLCalf.getText().toString());

        if(mOnSaveListener != null){
            mOnSaveListener.onSave(mStatsItem);
        }
    }

    private double convertEditTextToStat(String stat){
        if(stat.isEmpty()){
            return 0;
        }
        return Double.valueOf(stat);
    }

    /*
     * void onDismiss(DialogInterface) - dialog dismiss event occurred
     */
    @Override
    public void onDismiss(DialogInterface dialogInterface){
        //check for listener
        if(mDismissListener != null){
            //notify listener for dimiss event
            mDismissListener.onDismiss(dialogInterface);
        }
    }

/**************************************************************************************************/


}