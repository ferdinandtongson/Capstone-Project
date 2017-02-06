package me.makeachoice.gymratpta.model.stubData;


import android.content.Context;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.CategoryFBItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;

public class CategoryStubData {


    public static ArrayList<CategoryFBItem> createDefaultCategories(Context ctx){
        ArrayList<CategoryFBItem> categories = new ArrayList();

        CategoryFBItem item01 = new CategoryFBItem();
        item01.categoryName = ctx.getString(R.string.exerciseCategory_arms);
        categories.add(item01);

        CategoryFBItem item02 = new CategoryFBItem();
        item02.categoryName = ctx.getString(R.string.exerciseCategory_back);
        categories.add(item02);

        CategoryFBItem item03 = new CategoryFBItem();
        item03.categoryName = ctx.getString(R.string.exerciseCategory_cardio);
        categories.add(item03);

        CategoryFBItem item04 = new CategoryFBItem();
        item04.categoryName = ctx.getString(R.string.exerciseCategory_chest);
        categories.add(item04);

        CategoryFBItem item05 = new CategoryFBItem();
        item05.categoryName = ctx.getString(R.string.exerciseCategory_core);
        categories.add(item05);

        CategoryFBItem item06 = new CategoryFBItem();
        item06.categoryName = ctx.getString(R.string.exerciseCategory_legs);
        categories.add(item06);

        CategoryFBItem item07 = new CategoryFBItem();
        item07.categoryName = ctx.getString(R.string.exerciseCategory_shoulders);
        categories.add(item07);

        return categories;
    }

}
