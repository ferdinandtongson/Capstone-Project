package me.makeachoice.gymratpta.controller.viewside.maid.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import me.makeachoice.gymratpta.controller.modelside.firebase.ClientFirebaseHelper;
import me.makeachoice.gymratpta.controller.modelside.query.ClientQueryHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.client.ClientItem;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**
 * Created by Usuario on 1/31/2017.
 */

public class ClientInfoMaid extends MyMaid  implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private final static int STATUS_ACTIVE = 0;
    private final static int STATUS_RETIRED = 1;

    private ClientItem mClientItem;

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
        initializeInfoText();
        initializeTrainingText();
        initializeIconImage();
        initializeProfileImage();
        initializeSpinner();

        //load category and exercise data
        loadData();
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

    private void initializeTrainingText(){
        TextView txtStartDate = (TextView)mLayout.findViewById(R.id.clientInfo_txtStartDate);
        txtStartDate.setText("- start date -");

        TextView txtTotalSession = (TextView)mLayout.findViewById(R.id.clientInfo_txtTotal);
        txtTotalSession.setText("Total: ");

        TextView txtScheduledSession = (TextView)mLayout.findViewById(R.id.clientInfo_txtScheduled);
        txtScheduledSession.setText("Scheduled: ");
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

        if(mClientItem.status.equals("Retired")){
            spnStatus.setSelection(STATUS_RETIRED);
        }
    }

    /*
     * void loadData() - load category and exercise data
     */
    private void loadData(){

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
        String strActive = mFragment.getString(R.string.active);
        String strRetired = mFragment.getString(R.string.retired);

        if(mSpinnerCount > 0){
            switch(index){
                case STATUS_ACTIVE:
                    Log.d("Choice", "Status - Active");
                    updateClient(strActive);
                    break;
                case STATUS_RETIRED:
                    Log.d("Choice", "Status - Retired");
                    updateClient(strRetired);
                    break;
            }
        }

        mSpinnerCount++;
    }

    private void updateClient(String status){
        mClientItem.status = status;

        ClientFirebaseHelper clientFB = ClientFirebaseHelper.getInstance();
        clientFB.setClientStatus(mClientItem.uid, mClientItem.fkey, status);

        putClientInDatabase(mClientItem);
    }

    /*
 * void putClientInDatabase(DataSnapshot, String) - add client data into database
 */
    private void putClientInDatabase(ClientItem item){
        Log.d("Choice", "ContactListDialog.putClientInDatabase");

        Log.d("Choice", "     client: " + item.clientName);

        //get client uri
        Uri uriValue = Contractor.ClientEntry.CONTENT_URI;

        //query selection - client.uid = ? AND fkey = ?
        String whereClause = ClientQueryHelper.fkeySelection;
        String[] whereArgs = new String[]{item.uid, item.fkey };

        //insert client into database
        int updateCount = mFragment.getContext().getContentResolver().update(uriValue, item.getContentValues(),
                whereClause, whereArgs);

        Log.d("Choice", "     update: " + updateCount);
    }

}
