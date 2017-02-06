package me.makeachoice.gymratpta.controller.viewside.maid.exercise;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.modelside.firebase.CategoryFirebaseHelper;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;
import me.makeachoice.gymratpta.controller.viewside.viewpager.StandardViewPager;
import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryColumns;
import me.makeachoice.gymratpta.model.item.exercise.CategoryFBItem;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;
import me.makeachoice.gymratpta.model.stubData.ExerciseStubData;
import me.makeachoice.gymratpta.view.fragment.BasicFragment;

/**************************************************************************************************/
/*
 * ExerciseMaid initializes and takes care of the viewPager fragment that show different lists of
 * exercises separated by category
 *
 * Variables from MyMaid:
 *      mMaidKey - key string of instance Maid
 *      int mLayoutId - resource id number of fragment layout
 *      View mLayout - fragment layout view holding the child views
 *      MyFragment mFragment - fragment being maintained by the Maid
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

public class ExerciseMaid extends MyMaid implements BasicFragment.Bridge{

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    private String mUserId;

    //mCategories - list of category items used by viewPager tabLayout
    ArrayList<String> mPageTitles;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * ExerciseMaid - constructor
 */
/**************************************************************************************************/
    /*
     * ExerciseMaid(...) - constructor
     */
    public ExerciseMaid(String maidKey, int layoutId, String userId){
        //get maidKey
        mMaidKey = maidKey;

        //fragment layout id number
        mLayoutId = layoutId;

        mUserId = userId;
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
        //prepareFragment();
        //initializeLayout(null);
        loadCategories();
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
        //load category and exercise data
        //loadData();

        //initialize maids used by viewPager
        initializeVPMaids();

        //initialize view pager
        StandardViewPager pager = new StandardViewPager(mFragment, mPageTitles, MaidRegistry.MAID_EXERCISE_VP);

    }

    /*
     * void loadData() - load category and exercise data
     */
    /*private void loadData(){
        //create exercise stub data
        //ExerciseStubData.createDefaultExercises(mFragment.getContext());

        //create category stub data
        ArrayList<CategoryItem> mCategories = CategoryStubData.createDefaultCategories(mFragment.getContext());

        mPageTitles = new ArrayList();
        int count = mCategories.size();
        for(int i = 0; i < count; i++){
            mPageTitles.add(mCategories.get(i).categoryName);
        }

    }*/

    /*
     * void initializeMaid(int) - initialize Maids used by the viewPager component
     */
    private void initializeVPMaids(){
        //layout resource id maid will use to create fragment
        //int layoutId = R.layout.standard_recycler_fab;
        int layoutId = R.layout.standard_recycler_fab;

        int count = mPageTitles.size();

        //initialize maids
        for(int i = 0; i < count; i++){
            //get exercise list
            ArrayList<ExerciseItem> exercises = ExerciseStubData.getExercises(i);

            //create unique maid id numbers using a base number
            String maidKey = MaidRegistry.MAID_EXERCISE_VP + i;

            MaidRegistry maidRegistry = MaidRegistry.getInstance();

            //initialize maid
            maidRegistry.initializeExerciseViewPagerMaid(maidKey, layoutId, exercises);
        }
    }

    private int LOADER_CATEGORY = 100;

/**************************************************************************************************/
    /*
     * void loadCategories() - loads categories onto cursor used by Adapter. First gets load categories
     * from Firebase, loads categories data to sqlite local database and then retrieves and delivers
     * that data as a cursor. If there is no categories, load from flat file to firebase first
     */
    private void loadCategories(){
        Log.d("Choice", "ExerciseMaid.loadCategories");
        // Initializes a loader for loading clients
        mFragment.getActivity().getSupportLoaderManager().initLoader(LOADER_CATEGORY, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                //load categories from firebase, insert into local database
                loadFirebaseCategories();

                //request client cursor from local database
                Uri uri = Contractor.CategoryEntry.buildCategoryByUID(mUserId);
                //get cursor
                return new CursorLoader(
                        mFragment.getActivity(),
                        uri,
                        CategoryColumns.PROJECTION,
                        null,
                        null,
                        Contractor.CategoryEntry.SORT_ORDER_DEFAULT);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
                Log.d("Choice", "ClientKeeper.onLoadFinished: " + cursor.getCount());
                mPageTitles = new ArrayList();

                int count = cursor.getCount();
                for(int i = 0; i < count; i++){
                    cursor.moveToPosition(i);
                    mPageTitles.add(cursor.getString(Contractor.CategoryEntry.INDEX_CATEGORY_NAME));
                    Log.d("Choice", "     category: " + cursor.getString(Contractor.CategoryEntry.INDEX_CATEGORY_NAME));
                }
                //category cursor loaded, initialize layout
                //mAdapter.setCursor(cursor);
                prepareFragment();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
            }
        });
    }

    private HashMap<String,String> mDoublesMap;

    /*
     * void loadFirebaseCategories() - gets category data from Firebase
     */
    private void loadFirebaseCategories(){
        Log.d("Choice", "ExerciseMaid.loadFirebaseCategories");
        //initialize category list array
        mDoublesMap = new HashMap();

        //get client firebase instance
        final CategoryFirebaseHelper categoryFB = CategoryFirebaseHelper.getInstance();

        //get orderBy string value
        String orderBy = CategoryFirebaseHelper.CHILD_CATEGORY_NAME;

        //request client data ordered by client name
        categoryFB.requestCategoryData(mUserId, orderBy, new CategoryFirebaseHelper.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(DataSnapshot dataSnapshot) {

                long childrenCount = dataSnapshot.getChildrenCount();
                Log.d("Choice", "     count: " + childrenCount);

                if(childrenCount > 0){
                    processFirebaseData(dataSnapshot);
                }
                else{
                    initializeCategoryData();
                }
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    private void processFirebaseData(DataSnapshot dataSnapshot){
        //loop through client data
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            //get the data from snapshot
            CategoryFBItem category = postSnapshot.getValue(CategoryFBItem.class);

            //create client item, used for local database
            CategoryItem item = new CategoryItem(category);
            item.uid = mUserId;
            item.fkey = postSnapshot.getKey();

            //add item to client list
            updateDatabase(item);
        }
    }

    private void updateDatabase(CategoryItem item){
        Log.d("Choice", "ExerciseMaid.updateDatabase: " + item.categoryName);
        //get uri value for client
        Uri uriValue = Contractor.CategoryEntry.CONTENT_URI;
        Log.d("Choice", "     uri: " + uriValue);

        if(!mDoublesMap.containsKey(item.categoryName)){
            mDoublesMap.put(item.categoryName, item.categoryName);
            //add category to sqlite database
            mFragment.getActivity().getContentResolver().insert(uriValue, item.getContentValues());
        }

    }


    private void initializeCategoryData(){

    }

}
