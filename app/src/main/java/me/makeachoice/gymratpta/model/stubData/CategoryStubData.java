package me.makeachoice.gymratpta.model.stubData;


import android.content.Context;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.CategoryItem;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseItem;

public class CategoryStubData {


    public static ArrayList<CategoryItem> createDefaultCategories(Context ctx){
        ArrayList<CategoryItem> categories = new ArrayList();

        CategoryItem item01 = new CategoryItem();
        item01.categoryName = ctx.getString(R.string.defaultExercise_arms);
        categories.add(item01);

        CategoryItem item02 = new CategoryItem();
        item02.categoryName = ctx.getString(R.string.defaultExercise_back);
        categories.add(item02);

        CategoryItem item03 = new CategoryItem();
        item03.categoryName = ctx.getString(R.string.defaultExercise_cardio);
        categories.add(item03);

        CategoryItem item04 = new CategoryItem();
        item04.categoryName = ctx.getString(R.string.defaultExercise_chest);
        categories.add(item04);

        CategoryItem item05 = new CategoryItem();
        item05.categoryName = ctx.getString(R.string.defaultExercise_core);
        categories.add(item05);

        CategoryItem item06 = new CategoryItem();
        item06.categoryName = ctx.getString(R.string.defaultExercise_legs);
        categories.add(item06);

        CategoryItem item07 = new CategoryItem();
        item07.categoryName = ctx.getString(R.string.defaultExercise_shoulders);
        categories.add(item07);

        return categories;
    }

}
