package me.makeachoice.gymratpta.view.dialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.modelside.butler.CategoryButler;
import me.makeachoice.gymratpta.controller.modelside.butler.ExerciseButler;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.library.android.base.view.activity.MyActivity;

import static android.content.Context.MODE_PRIVATE;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_CATEGORY;
import static me.makeachoice.gymratpta.controller.manager.Boss.LOADER_EXERCISE_BASE;
import static me.makeachoice.gymratpta.controller.manager.Boss.PREF_SET_MAX;

/**************************************************************************************************/
/*
 * EditAddRoutineExerciseDialog is used for Adding or Editing Exercise items found in a user-defined
 * routine
 */
/**************************************************************************************************/

public class RoutineExerciseDialog extends DialogFragment {

/**************************************************************************************************/
/*
 * Class Variables
 *      MyActivity mActivity - activity context
 *      String mUserId - user id number from firebase authentication
 *      RoutineItem mRoutineItem - routine item object containing exercises and name of the routine
 *      
 *      HashMap<String,Integer> mCategoryMap - maps the category name to list index
 *      ArrayList<String> mCategories - list of CategoryItem objects
 *      int mCategoryIndex - list index of selected category
 *
 *      HashMap<String,Integer> mExerciseMap - maps the category name to list index
 *      ArrayList<String> mExercises - list of ExerciseItem objects
 *      int mExerciseIndex - list index of selected exercise
 *
 *      View mRootView - root view component containing dialog child components
 *      TextView mTxtSave - textView save button component
 *      Spinner mSpnCategory - spinner component displaying list of exercise categories
 *      Spinner mSpnExercise - spinner component displaying list of exercises defined in a category
 *      Spinner mSpnSets - spinner component displaying number of set to be done for the exercise
 *      OnSaveClickListener mSavedListener - notifies listener that the saved button was clicked
 */
/**************************************************************************************************/

    //mActivity - activity context
    private MyActivity mActivity;
    
    //mUserId - user id number from firebase authentication
    private String mUserId;

    private ExerciseButler mExerciseButler;
    private CategoryButler mCategoryButler;
    
    //mRoutineItem - routine item object containing exercises and name of the routine
    private RoutineItem mRoutineItem;

    //mCategoryMap - maps the category name to list index
    private HashMap<String,Integer> mCategoryMap;
    
    //mCategories - list of CategoryItem objects
    private ArrayList<CategoryItem> mCategories;
    
    //mCategoryNames - list of category names
    private ArrayList<String> mCategoryNames;
    
    //mCategoryIndex - list index of selected category
    private int mCategoryIndex;

    //mExerciseMap - maps the category name to list index
    private HashMap<String,Integer> mExerciseMap;
    
    //mExercises - list of ExerciseItem objects
    private ArrayList<ExerciseItem> mExercises;
    
    //mExerciseNames - list of exercise names
    private ArrayList<String> mExerciseNames;
    
    //mExerciseIndex - list index of selected exercise
    private int mExerciseIndex;

    //mSets -list of set numbers
    private ArrayList<String> mSets;

    //mSetIndex - list index of selected number of sets
    private int mSetIndex;

    //mRootView - root view component containing dialog child components
    private View mRootView;

    //mSpnCategory - spinner component displaying list of exercise categories
    private Spinner mSpnCategory;

    //mSpnExercise - spinner component displaying list of exercises defined in a category
    private Spinner mSpnExercise;

    //mSpnSets - spinner component displaying number of set to be done for the exercise
    private Spinner mSpnSets;

    //mSavedListener - notifies listener that the saved button was clicked
    private OnSaveClickListener mSavedListener;
    public interface OnSaveClickListener{
        void onSaveClicked(RoutineItem item);
    }

    private OnDismissListener mDismissListener;
    public interface OnDismissListener{
        void onDismiss(DialogInterface dialogInterface);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * RoutineExerciseDialog Constructor
 */
/**************************************************************************************************/
    /*
     * RoutineExerciseDialog - constructor
     */
    public RoutineExerciseDialog(){
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Setter Method
 *      void setDialogValues(MyActivity,String,RoutineItem) - set dialog values
 *      void setDialogValues(MyActivity,String) - set dialog values
 *      void setOnSavedListener(...) - set listener for saved click event
 */
/**************************************************************************************************/
    /*
     * void setDialogValues(MyActivity,String) - set dialog values
     */
    public void setDialogValues(MyActivity activity, String userId){
        setDialogValues(activity, userId, null);
    }

    /*
     * void setDialogValues(MyActivity,String,RoutineItem) - set dialog values
     */
    public void setDialogValues(MyActivity activity, String userId, RoutineItem item){
        //set activity context
        mActivity = activity;

        //set user id
        mUserId = userId;

        //set routineItem object
        mRoutineItem = item;

        //initialize category buffers
        mCategories = new ArrayList<>();
        mCategoryNames = new ArrayList<>();
        mCategoryMap = new HashMap<>();

        //initialize exercise buffers
        mExercises = new ArrayList<>();
        mExerciseNames = new ArrayList<>();
        mExerciseMap = new HashMap<>();

        //initialize butlers
        mExerciseButler = new ExerciseButler(mActivity, mUserId);
        mCategoryButler = new CategoryButler(mActivity, mUserId);

    }

    /*
     * void setOnSavedListener(...) - set listener for saved click event
     */
    public void setOnSavedListener(OnSaveClickListener listener){
        mSavedListener = listener;
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
 * Public Method
 *      View onCreateView(...) - called when dialog show is requested
 *      void initializeDialog() - initialize dialog components
 *      void initializeTitleTextView() - initialize textView title component
 *      void initializeSaveTextView() - initialize textView save component
 *      void initializeSetSpinner() - initialize spinner for number of exercise sets to do
 *      void initializeSpinners() - initialize spinner components
 *      void updateSpinner(...) - update spinner component with adapter object
 */
/**************************************************************************************************/
    /*
     * View onCreateView(...) - called when dialog show is requested
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //layout resource id number
        int layoutId = R.layout.dia_exercise_detail;

        //inflate root view, parent child components
        mRootView = inflater.inflate(layoutId, container, false);

        //load category data from database
        loadCategories();

        initializeDialog();

        return mRootView;
    }

    /*
     * void initializeDialog() - initialize dialog components
     */
    private void initializeDialog(){
        //initialize title textView components
        initializeTitleTextView();

        //initialize save textView component
        initializeSaveTextView();

        //initialize set spinner
        initializeSetSpinner();

        //initialize spinner components
        initializeSpinners();
    }

    /*
     * void initializeTextViewTitle() - initialize textView title component
     */
    private void initializeTitleTextView(){
        //get title string value
        String strTitle = mActivity.getString(R.string.exercise);

        //get title textView component
        TextView txtTitle = (TextView)mRootView.findViewById(R.id.diaExercise_txtTitle);

        //set title value
        txtTitle.setText(strTitle);

    }

    /*
     * void initializeTextViewSave() - initialize textView save component
     */
    private void initializeSaveTextView(){
        //get save textView component
        TextView txtSave = (TextView)mRootView.findViewById(R.id.diaExercise_txtSave);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked();
            }
        });
    }

    /*
     * void initializeSetSpinner() - initialize spinner for number of exercise sets to do
     */
    private void initializeSetSpinner(){
        //get shared preference
        SharedPreferences prefs = mActivity.getSharedPreferences(mUserId, MODE_PRIVATE);

        //get user-defined maximum number of exercise sets
        int setMax = prefs.getInt(PREF_SET_MAX, 5);

        //initialize set spinner
        mSpnSets = (Spinner)mRootView.findViewById(R.id.diaExercise_spnSet);
        mSpnSets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSetSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //initialize set list
        mSets = new ArrayList();

        //create set list
        for(int i = 1; i <= setMax; i++){
            String str = "" + i;
            mSets.add(str);
        }

        //check if routine item is not Null
        if(mRoutineItem != null){
            //get set index
            mSetIndex = Integer.valueOf(mRoutineItem.numOfSets) - 1;
        }
        else{
            //use default set index value
            mSetIndex = 0;
        }

        //update set spinner
        updateSpinner(mSpnSets, mSets, mSetIndex);
    }


    /*
     * void initializeSpinners() - initialize spinner components
     */
    private void initializeSpinners(){
        //initialize category spinner
        mSpnCategory = (Spinner)mRootView.findViewById(R.id.diaExercise_spnCategory);
        mSpnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onCategorySelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //initialize exercise spinner
        mSpnExercise = (Spinner)mRootView.findViewById(R.id.diaExercise_spnExercise);
        mSpnExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onExerciseSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /*
     * void updateSpinner(...) - update spinner component with adapter object
     */
    private void updateSpinner(Spinner spinner, ArrayList<String> values, int selectedIndex){
        //create string adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //clear old adapter data, if any
        spinner.setAdapter(null);

        //set adapter for spinner
        spinner.setAdapter(adapter);

        //set spinner selection
        spinner.setSelection(selectedIndex);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Load Database Methods
 *      void loadCategories() - load category data from database
 *      void loadExercises() - load exercise data from database
 *      void onCategoryDataLoaded(Cursor) - category data from database has been loaded
 *      void onExerciseDataLoaded(Cursor) - exercise data from database has been loaded
 */
/**************************************************************************************************/
    /*
     * void loadCategories() - load category data from database
     */
    private void loadCategories(){
        mCategoryButler.loadCategories(LOADER_CATEGORY, new CategoryButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<CategoryItem> categoryList) {
                onCategoriesLoaded(categoryList);
            }
        });
    }

    /*
     * void loadExercises() - load exercise data from database
     */
    private void loadExercises(){
        //get category key
        String categoryKey = mCategories.get(mCategoryIndex).fkey;

        //start loader to get exercise data from database
        mExerciseButler.loadExercises(categoryKey, LOADER_EXERCISE_BASE, new ExerciseButler.OnLoadedListener() {
            @Override
            public void onLoaded(ArrayList<ExerciseItem> exerciseList) {
                onExerciseDataLoaded(exerciseList);
            }
        });
    }

    /*
     * void onCategoryDataLoaded(..) - category data from database has been loaded
     */
    private void onCategoriesLoaded(ArrayList<CategoryItem> categoryList){

        //get number of categories
        int count = categoryList.size();

        //loop through categories
        for(int i = 0; i < count; i++){
            //create category item object
            CategoryItem item = categoryList.get(i);

            //set category buffers
            mCategories.add(item);
            mCategoryNames.add(item.categoryName);
            mCategoryMap.put(item.categoryName, i);

        }

        //check if routine item object is not Null
        if(mRoutineItem != null){
            //get category index that matches category from routine item object
            mCategoryIndex = mCategoryMap.get(mRoutineItem.category);
        }
        else{
            //set category index to default
            mCategoryIndex = 0;
        }

        //update category spinner with list of categories
        updateSpinner(mSpnCategory, mCategoryNames, mCategoryIndex);
    }

    /*
     * void onExerciseDataLoaded(Cursor) - exercise data from database has been loaded
     */
    private void onExerciseDataLoaded(ArrayList<ExerciseItem> exerciseList){
        mExercises.clear();
        mExerciseNames.clear();
        mExerciseMap.clear();

        //get number of exercises loaded
        int count = exerciseList.size();

        //loop through exercises
        for(int i = 0; i < count; i++){
            //create exercise item object with cursor
            ExerciseItem item = exerciseList.get(i);

            //set exercise buffers
            mExercises.add(item);
            mExerciseNames.add(item.exerciseName);
            mExerciseMap.put(item.exerciseName, i);
        }

        //check if routine item object is not Null
        if(mRoutineItem != null){
            //check if exercise map contains exercise in routine item object
            if(mExerciseMap.containsKey(mRoutineItem.exercise)){
                //get exercise index that matches exercise from routine item object
                mExerciseIndex = mExerciseMap.get(mRoutineItem.exercise);
            }
            else{
                //exercise in routine item object is not in list, set default index
                mExerciseIndex = 0;
            }
        }
        else{
            //set default index
            mExerciseIndex = 0;
        }

        //update exercise spinner with exercise list
        updateSpinner(mSpnExercise, mExerciseNames, mExerciseIndex);

    }


/**************************************************************************************************/

/**************************************************************************************************/
/*
 * OnEvent Methods
 *      void onCategorySelected(int) - category was selected from category spinner
 *      void onExerciseSelected(int) - exercise was selected from exercise spinner
 *      void onSetSelected(int) - number of sets for exercises was selected from set spinner
 *      void onSaveClicked() - "save" textView button was clicked
 */
/**************************************************************************************************/
    /*
     * void onCategorySelected(int) - category was selected from category spinner
     */
    private void onCategorySelected(int index){
        //get category spinner index
        mCategoryIndex = index;

        //load exercises of selected category
        loadExercises();
    }

    /*
     * void onExerciseSelected(int) - exercise was selected from exercise spinner
     */
    private void onExerciseSelected(int index){
        mExerciseIndex = index;
    }

    /*
     * void onSetSelected(int) - number of sets for exercises was selected from set spinner
     */
    private void onSetSelected(int index){
        mSetIndex = index;
    }

    /*
     * void onSaveClicked() - "save" textView button was clicked
     */
    private void onSaveClicked(){
        //create RoutineItem object
        RoutineItem item = new RoutineItem();

        //check if user is editing an old routine item
        if(mRoutineItem != null){
            //get data from old routine item
            item.uid = mRoutineItem.uid;
            item.routineName = mRoutineItem.routineName;
            item.orderNumber = mRoutineItem.orderNumber;
        }
        else{
            //initialize routine item data
            item.uid = "";
            item.routineName = "";
            item.orderNumber = "-1";
        }

        //save selected routine options
        item.category = mCategoryNames.get(mCategoryIndex);
        item.exercise = mExerciseNames.get(mExerciseIndex);
        item.numOfSets = mSets.get(mSetIndex);

        //check for listener
        if(mSavedListener != null){
            //notify listener of onSave click event
            mSavedListener.onSaveClicked(item);
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