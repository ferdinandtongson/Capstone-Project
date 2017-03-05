package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.ClientButler;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.view.dialog.GoalDialog;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * ClientInfoMaid displays client contact info and exercise goals
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

public class ClientInfoMaid extends MyMaid  implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private final static int ACTIVE_STATUS = 0;
    private final static int RETIRED_STATUS = 1;

    private String mStrActive;
    private String mStrRetired;

    private ClientItem mClientItem;

    private GoalDialog mGoalDialog;
    private ClientButler mClientButler;

    //used so client status is updated during initialization
    private int mSpinnerCount;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ClientInfoMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ClientInfoMaid(...) - constructor
     */
    public ClientInfoMaid(String maidKey, int layoutId, ClientItem item){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mClientItem = item;

        mSpinnerCount = 0;
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


        mStrActive = mFragment.getString(R.string.active);
        mStrRetired = mFragment.getString(R.string.retired);

        mClientButler = new ClientButler(mActivity, mClientItem.uid);

        //prepare fragment components
        prepareFragment();
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
    private void prepareFragment() {
        Log.d("Choice", "ClientInfoMaid.prepareFragment");
        initializeInfoText();
        initializeTrainingText();
        initializeIconImage();
        initializeProfileImage();
        initializeSpinner();

    }

    private void initializeInfoText(){
        String strPhone = mFragment.getString(R.string.phone) + ": ";
        String strEmail = mFragment.getString(R.string.email) + ": ";

        TextView txtName = (TextView)mLayout.findViewById(R.id.clientInfo_txtName);
        txtName.setText(mClientItem.clientName);
        txtName.setContentDescription(mClientItem.clientName);

        TextView txtPhone = (TextView)mLayout.findViewById(R.id.clientInfo_txtPhone);
        txtPhone.setText(strPhone + mClientItem.phone);
        txtPhone.setContentDescription(strPhone + mClientItem.phone);

        TextView txtEmail = (TextView)mLayout.findViewById(R.id.clientInfo_txtEmail);
        txtEmail.setText(strEmail + mClientItem.email);
        txtEmail.setContentDescription(strEmail + mClientItem.email);
    }

    private TextView mTxtEmptyGoals;
    private TextView mTxtGoals;
    private void initializeTrainingText(){
        mTxtGoals = (TextView)mLayout.findViewById(R.id.clientInfo_txtGoals);
        mTxtGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeGoalDialog();
            }
        });

        mTxtEmptyGoals = (TextView)mLayout.findViewById(R.id.clientInfo_txtEmptyGoals);
        mTxtEmptyGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeGoalDialog();
            }
        });

        updateGoalView();
    }

    private void initializeIconImage(){
        ImageView imgPhone = (ImageView)mLayout.findViewById(R.id.clientInfo_imgPhone);
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhoneClicked();
            }
        });

        ImageView imgEmail = (ImageView)mLayout.findViewById(R.id.clientInfo_imgEmail);
        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEmailClicked();
            }
        });
    }

    private void initializeProfileImage(){
        CircleImageView imgProfile = (CircleImageView)mLayout.findViewById(R.id.clientInfo_imgProfile);

        Picasso.with(mLayout.getContext())
                .load(mClientItem.profilePic)
                .placeholder(R.drawable.gym_rat_black_48dp)
                .error(R.drawable.gym_rat_black_48dp)
                .into(imgProfile);

    }

    private void initializeSpinner(){
        Spinner spnStatus = (Spinner)mLayout.findViewById(R.id.clientInfo_spnStatus);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                onStatusSelected(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(mClientItem.status.equals(mStrRetired)){
            spnStatus.setSelection(RETIRED_STATUS);
        }
        else{
            spnStatus.setSelection(ACTIVE_STATUS);
        }
    }

    /*
 * NotesDialog initializeGoalDialog - initialize client notes dialog
 */
    private GoalDialog initializeGoalDialog(){
        //get fragment manager
        FragmentManager fm = mActivity.getSupportFragmentManager();

        //create dialog
        mGoalDialog = new GoalDialog();
        mGoalDialog.setDialogValues(mActivity, mClientItem.uid, mClientItem);

        mGoalDialog.setOnSaveListener(new GoalDialog.OnSaveListener() {
            @Override
            public void onSaveGoals(String goals) {
                onGoalsSaved(goals);
            }
        });

        mGoalDialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoalDialog.dismiss();
            }
        });

        mGoalDialog.setCancelable(true);

        mGoalDialog.show(fm, "diaNotes");

        return mGoalDialog;
    }


    private void updateGoalView(){
        String goals = mClientItem.goals;
        if(goals.isEmpty()){
            mTxtEmptyGoals.setVisibility(View.VISIBLE);
        }
        else{
            mTxtEmptyGoals.setVisibility(View.GONE);
        }

        mTxtGoals.setText(goals);
    }

/**************************************************************************************************/

    private void onPhoneClicked(){

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mClientItem.phone));
        mFragment.startActivity(intent);
    }

    private void onEmailClicked(){
        String strSendEmail = mFragment.getString(R.string.send_email_using);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mClientItem.email});
        mFragment.startActivity(Intent.createChooser(intent, strSendEmail));
    }

    private void onStatusSelected(int index){
        //Note: onStatusSelected is called during initialization, use SpinnerCount to prevent save
        if(mSpinnerCount > 0){
            switch(index){
                case ACTIVE_STATUS:
                    mClientItem.status = mStrActive;
                    break;
                case RETIRED_STATUS:
                    mClientItem.status = mStrRetired;
                    break;
            }

            mClientButler.updateClientStatus(mClientItem, new ClientButler.OnSavedListener() {
                @Override
                public void onSaved() {
                    //does nothing
                }
            });

        }

        mSpinnerCount++;
    }

    private void onGoalsSaved(String goals){
        mClientItem.goals = goals;
        mClientButler.updateClientGoals(mClientItem, new ClientButler.OnSavedListener() {
            @Override
            public void onSaved() {
                updateGoalView();
            }
        });
    }

/**************************************************************************************************/

}
