package me.makeachoice.library.android.base.controller.viewside.maid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.makeachoice.library.android.base.R;
import me.makeachoice.library.android.base.view.fragment.MyFragment;

/**************************************************************************************************/
/*
 * MyFragmentTitleMaid manages a Fragment with a simple title textView
 *
 * Variables from MyMaid
 *      int mMaidId - id number of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      MyFragment mFragment - fragment being maintained by the Maid
 *
 * Methods From MyMaid
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void destroyView() - called when fragment is being removed
 *      void detach() - called when fragment is being disassociated from Activity
 *      void saveInstanceState(Bundle) - called before onDestroy( ), save state to bundle
 *      int getMaidId() - get maid id number
 *      MyFragment getFragment() - get fragment maintained by maid
 *
 *  TODO - add ripple effect
 *  TODO - test if content description is enabled, do I need to set an onClick listener?
 *
 */
/**************************************************************************************************/

public abstract class MyFragmentTitleMaid extends MyMaid implements MyFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 *      int TXT_TITLE_ID - resource id number for textView Title object
 *      TextView mTxtTitle - title textView object
 *
 *      String mTitle - title string value
 *      String mTitleDescription - title description string value
 *      String mTitleVisibility - title textView visibility value
 */
/**************************************************************************************************/

    //TXT_TITLE_ID - resource id number for textView Title object
    protected final static int TXT_TITLE_ID = R.id.fragment_txtTitle;

    //mTxtTitle - title textView object
    private TextView mTxtTitle;

    //mTitle - title string value
    private String mTitle;

    //mTitleDescription - title description string value
    private String mTitleDescription;

    //mTitleVisibility - visibility value of title textView
    private int mTitleVisibility = View.VISIBLE;


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Initialization methods
 *      void initialize(int,int) - initialize fragment
 *      void initTitle(int) - initialize title view, used only to set view visibility
 *      void initTitle(String) - initialize title view, used only to set title value
 *      void initTitle(String,String) - initialize title view, set title and description value
 *      void initTitle(String,String,int) - initialize title view, set title, description and visibility
 */
/**************************************************************************************************/
    /*
     * void initialize(int,int) - initialize fragment
     * @param maidId - maid id number
     * @param layoutId - fragment layout resource id number
     */
    protected void initialize(int maidId, int layoutId){
        //get id number for maid instance
        mMaidId = maidId;

        //get layout resource id number
        mLayoutId = layoutId;
    }

    /*
     * void initTitle(int) - initialize title view, used only to set view visibility
     * @param visibility - textView visibility value
     */
    public void initTitle(int visibility){
        initTitle("", "", visibility);
    }

    /*
     * void initTitle(String) - initialize title view, used only to set title value
     * @param title - title string value
     */
    public void initTitle(String title){
        initTitle(title, "", View.VISIBLE);
    }

    /*
     * void initTitle(String,String) - initialize title view, set title and description value
     * @param title - title string value
     * @param description - title content description value
     */
    public void initTitle(String title, String description){
        initTitle(title, description, View.VISIBLE);
    }

    /*
     * void initTitle(String,String,int) - initialize title view, set title, description and visibility
     * @param title - title string value
     * @param description - title content description value
     * @param visibility - textView visibility value
     */
    public void initTitle(String title, String description, int visibility){
        //title string value
        mTitle = title;

        //title content description string value
        mTitleDescription = description;

        //title visibility value
        mTitleVisibility = visibility;
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Lifecycle Methods:
 *      View createView(LayoutInflater, ViewGroup, Bundle) - Fragment UI is drawn
 *      void activityCreated() - called when Activity.onCreate() completed
 *      void createTitleView() - create title textView object
 */
/**************************************************************************************************/
    /*
     * View createView(LayoutInflater, ViewGroup, Bundle) - Fragment UI is drawn
     * @param inflater - layoutInflater to inflate the xml fragment layout resource file
     * @param container - view that will hold the fragment view
     * @param bundle - saved instance states
     * @return - fragment layout
     */
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(mLayoutId, container, false);

        return mLayout;
    }

    /*
     * void activityCreated() - called when Activity.onCreate() completed.
     * @param bundle - saved instance states
     */
    @Override
    public void activityCreated(Bundle bundle){
        super.activityCreated(bundle);

        //create title textView
        createTitleView();
    }

    /*
     * void createTitleView() - create title textView object
     */
    private void createTitleView(){
        //get Title textView object
        mTxtTitle = (TextView)mLayout.findViewById(TXT_TITLE_ID);

        //set Title textView visibility
        setTitleVisibility(mTitleVisibility);

        //if textView is not "GONE"
        if(mTitleVisibility != View.GONE){
            //set title value
            setTitle(mTitle);

            //set title content description value
            setTitleDescription(mTitleDescription);
        }
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setters:
 *      void setTitle(String) - set title string value
 *      void setTitleDescription(String) - set title content description string value
 *      void setTitleVisibility(int) - set title textView visibility
 */
/**************************************************************************************************/
    /*
     * void setTitle(String) - set title string value
     * @param title - title string value
     */
    public void setTitle(String title){
        //title string value
        mTitle = title;

        //make sure title textView has been created
        if(mTxtTitle != null){
            //set title string value
            mTxtTitle.setText(mTitle);
        }
    }

    /*
     * void setTitleDescription(String) - set title content description string value
     * @param description - content description string value
     */
    public void setTitleDescription(String description){
        //title content description string value
        mTitleDescription = description;

        //make sure title textView has been created
        if(mTxtTitle != null){
            //set title content description
            mTxtTitle.setContentDescription(mTitleDescription);
        }
    }

    /*
     * void setTitleVisibility(int) - set title textView visibility
     * @param visibility - textView visibility (VISIBLE, INVISIBLE, GONE)
     */
    public void setTitleVisibility(int visibility){
        //set title textView visibility value
        mTitleVisibility = visibility;

        //make sure title textView has been created
        if(mTxtTitle != null){

            //check visibility value is valid
            if(mTitleVisibility == View.VISIBLE){
                //set title textView to Visible
                mTxtTitle.setVisibility(View.VISIBLE);
            }else if(mTitleVisibility == View.INVISIBLE){
                //set title textView to Invisible (not seen but takes up layout space)
                mTxtTitle.setVisibility(View.INVISIBLE);
            }else if(mTitleVisibility == View.GONE){
                //set title textView to Gone (removed from layout space)
                mTxtTitle.setVisibility(View.GONE);
            }else{
                //invalid value, set title textView to Visible
                mTxtTitle.setVisibility(View.VISIBLE);

                //set title textView to Visible
                mTitleVisibility = View.VISIBLE;
            }
        }
    }

/**************************************************************************************************/


}
