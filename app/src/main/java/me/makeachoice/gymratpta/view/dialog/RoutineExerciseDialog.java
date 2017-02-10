package me.makeachoice.gymratpta.view.dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.loader.CategoryLoader;
import me.makeachoice.gymratpta.controller.modelside.loader.ExerciseLoader;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

/**************************************************************************************************/
/*
 * EditAddRoutineExerciseDialog is used for Adding or Editing Exercise items found a routine
 */

/**************************************************************************************************/

public class RoutineExerciseDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private View.OnClickListener mSaveListener;
    private View.OnClickListener mCancelListener;

    private TextView mTxtTitle;
    private Spinner mSpnCategory;
    private Spinner mSpnExercise;

    private String mUserId;

    private RoutineItem mRoutineItem;
    private ArrayList<CategoryItem> mCategories;
    private ArrayList<String> mCategoryNames;
    private HashMap<String,Integer> mCategoryMap;
    private int mPositionCategory;
    private String mSelectedCategory;

    private ArrayList<ExerciseItem> mExercises;
    private ArrayList<String> mExerciseNames;
    private HashMap<String,Integer> mExerciseMap;
    private int mPositionExercise;
    private String mSelectedExercise;

    //ExerciseCategoryFirebase mFirebase;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/
    /*
     * EditAddRoutineExerciseDialog - constructor
     */
    public RoutineExerciseDialog(){
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Method
 *      String getExerciseName() - get exercise name from editText
 *      void setExerciseValues(String,String) - set exercise values used by dialog
 *      void setListeners(...) - set button onClick listeners
 */
/**************************************************************************************************/

    public void setTitle(String title){
        mTxtTitle.setText(title);
    }
    public void setRoutineItem(RoutineItem item){ mRoutineItem = item; }

    private MyActivity mActivity;
    public void initDialog(MyActivity activity, String userId){
        mUserId = userId;
        mActivity = activity;

        mExerciseNames = new ArrayList<>();

        //initializeCategory();
        //initializeExercise();
    }

    private void loadCategory(){
        CategoryLoader.loadCategories(mActivity, mUserId, new CategoryLoader.OnCategoryLoadListener() {
            @Override
            public void onCategoryLoadFinished(Cursor cursor) {
                initializeCategoryData(cursor);
            }
        });


    }

    private void initializeCategoryData(Cursor cursor){
        mCategories = new ArrayList();
        mCategoryNames = new ArrayList();
        mCategoryMap = new HashMap<>();


        int count = cursor.getCount();
        for(int i = 0; i < count; i++){
            cursor.moveToPosition(i);
            CategoryItem item = new CategoryItem(cursor);

            String categoryName = cursor.getString(Contractor.CategoryEntry.INDEX_CATEGORY_NAME);
            mCategories.add(item);
            mCategoryNames.add(categoryName);
            mCategoryMap.put(categoryName, i);

        }

        if(mRoutineItem != null){
            mPositionCategory = mCategoryMap.get(mRoutineItem.category);
        }
        else{
            mPositionCategory = 0;
        }

    }

    private void loadExercise(){
        String categoryKey = mCategories.get(mPositionCategory).fkey;

        ExerciseLoader.loadExercises(mActivity, mUserId, categoryKey, new ExerciseLoader.OnExerciseLoadListener() {
            @Override
            public void onExerciseLoadFinished(Cursor cursor) {
                initializeExerciseData(cursor);
            }
        });


    }

    private void initializeExerciseData(Cursor cursor){
        mExercises = new ArrayList();
        mExerciseNames = new ArrayList();
        mExerciseMap = new HashMap();

        int count = cursor.getCount();
        for(int i = 0; i < count; i++){
            cursor.moveToPosition(i);

            ExerciseItem item = new ExerciseItem(cursor);
            mExerciseNames.add(item.exerciseName);
            mExercises.add(item);
            mExerciseMap.put(item.exerciseName, i);
        }

        if(mRoutineItem != null){
            mPositionExercise = mExerciseMap.get(mRoutineItem.exercise);
        }
        else{
            mPositionExercise = 0;
        }

        initializeTextView();
        initializeSpinner();

    }

    /*
     * void setExerciseValues(String,String) - set exercise values used by dialog
     */
    public void setExerciseValues(RoutineItem item){
        //mExerciseRoutineItem = item;

    }

    /*
     * void setOnClickListeners(...) - set button onClick listeners
     */
    public void setOnClickListeners(View.OnClickListener saveListener, View.OnClickListener cancelListener){
        mSaveListener = saveListener;
        mCancelListener = cancelListener;
    }

    private View mRootView;

/**************************************************************************************************/


/**************************************************************************************************/
/*
 * Initialize Dialog
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeTextView(View) - initialize textView component for dialog
 *      void initializeEditText(View) - initialize EditText component for dialog
 *      void initializeButton(View) - initialize button components for dialog
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dia_routine_detail, container, false);


        loadCategory();
        //initialize textView component
        //initializeTextView(rootView);

        //initialize editText component
        //initializeSpinner(rootView);

        //initialize button component
        //initializeButton(rootView);

        return mRootView;
    }

    /*
     * void initializeTextView(View) - initialize textView component for dialog
     */
    private void initializeTextView(){
        //get textView component
        mTxtTitle = (TextView)mRootView.findViewById(R.id.txtTitle);

        mTxtTitle.setText("Edit Exericse");

        //set title string value
        //mTxtTitle.setText(mTitle);

    }

    private void initializeSpinner(){
        Spinner spnCategory = (Spinner)mRootView.findViewById(R.id.spnCategory);
        Spinner spnExercise = (Spinner)mRootView.findViewById(R.id.spnExercise);
        Spinner spnSets = (Spinner)mRootView.findViewById(R.id.spnSet);

        ArrayAdapter<String> adpCategory = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, mCategoryNames);
        adpCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adpExercise = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, mExerciseNames);

        // Apply the adapter to the spinner
        spnCategory.setAdapter(adpCategory);
        spnCategory.setSelection(mPositionCategory);


        spnExercise.setAdapter(adpExercise);
        spnExercise.setSelection(mPositionExercise);

        Integer[] items = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<Integer>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, items);

        spnSets.setAdapter(numAdapter);
    }

    /*
     * void initializeEditText(View) - initialize EditText component for dialog
     */
    private void initializeEditText(){
        //String strHint = mRootView.getResources().getString(R.string.exerciseAddDialog_editText_hint);

        //get editText view component

    }

    /*
     * void initializeButton(View) - initialize button components for dialog
     */
    private void initializeButton(){
        //get save button view component
        Button btnSave = (Button)mRootView.findViewById(R.id.btnSave);
        if(mSaveListener != null){
            //add listener
            btnSave.setOnClickListener(mSaveListener);
        }


        //get cancel button view component
        /*Button btnCancel = (Button)mRootView.findViewById(R.id.btnCancel);
        if(mCancelListener != null){
            //add listener
            btnCancel.setOnClickListener(mCancelListener);
        }*/


    }

/**************************************************************************************************/

}