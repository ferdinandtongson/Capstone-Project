package me.makeachoice.gymratpta.model.stubData;

import android.content.Context;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.RoutineItem;
import me.makeachoice.gymratpta.model.item.exercise.RoutineNameFBItem;

public class RoutineStubData {

    public static ArrayList<RoutineNameFBItem> createDefaultRoutineNames(Context ctx){
        ArrayList<RoutineNameFBItem> routineNames = new ArrayList();

        RoutineNameFBItem item01 = new RoutineNameFBItem();
        item01.routineName = "Arm Routine";
        routineNames.add(item01);

        RoutineNameFBItem item02 = new RoutineNameFBItem();
        item02.routineName = "Back Routine";
        routineNames.add(item02);

        RoutineNameFBItem item03 = new RoutineNameFBItem();
        item03.routineName = "Core Routine";
        routineNames.add(item03);

        RoutineNameFBItem item04 = new RoutineNameFBItem();
        item04.routineName = "Chest Routine";
        routineNames.add(item04);

        RoutineNameFBItem item05 = new RoutineNameFBItem();
        item05.routineName = "Legs Routine";
        routineNames.add(item05);

        RoutineNameFBItem item06 = new RoutineNameFBItem();
        item06.routineName = "Shoulder Routine";
        routineNames.add(item06);

        return routineNames;
    }

    public static ArrayList<RoutineItem> createDefaultRoutines(Context ctx){
        ArrayList<RoutineItem> routines = new ArrayList();

        RoutineItem item01 = new RoutineItem();
        item01.routineName = "Arm Routine";
        item01.orderNumber = 1;
        item01.exercise = "Jump Rope";
        item01.category = ctx.getString(R.string.exerciseCategory_cardio);
        item01.numOfSets = 1;
        routines.add(item01);

        RoutineItem item02 = new RoutineItem();
        item02.routineName = "Arm Routine";
        item02.orderNumber = 2;
        item02.exercise = "EZ-Bar Curl";
        item02.category = ctx.getString(R.string.exerciseCategory_arms);
        item02.numOfSets = 4;
        routines.add(item02);

        RoutineItem item03 = new RoutineItem();
        item03.routineName = "Arm Routine";
        item03.orderNumber = 3;
        item03.exercise = "Close-Grip Barbell Bench Press";
        item03.category = ctx.getString(R.string.exerciseCategory_arms);
        item03.numOfSets = 4;
        routines.add(item03);

        RoutineItem item04 = new RoutineItem();
        item04.routineName = "Arm Routine";
        item04.orderNumber = 4;
        item04.exercise = "Cable Rope Overhead Triceps Extension";
        item04.category = ctx.getString(R.string.exerciseCategory_arms);
        item04.numOfSets = 4;
        routines.add(item04);

        RoutineItem item05 = new RoutineItem();
        item05.routineName = "Arm Routine";
        item05.orderNumber = 5;
        item05.exercise = "Cable Hammer Curls - Rope Attachment";
        item05.category = ctx.getString(R.string.exerciseCategory_arms);
        item05.numOfSets = 4;
        routines.add(item05);

        RoutineItem item06 = new RoutineItem();
        item06.routineName = "Back Routine";
        item06.orderNumber = 1;
        item06.exercise = "Rowing Machine";
        item06.category = ctx.getString(R.string.exerciseCategory_cardio);
        item06.numOfSets = 1;
        routines.add(item06);

        RoutineItem item07 = new RoutineItem();
        item07.routineName = "Back Routine";
        item07.orderNumber = 2;
        item07.exercise = "Wide-Grip Lat Pulldown";
        item07.category = ctx.getString(R.string.exerciseCategory_back);
        item07.numOfSets = 4;
        routines.add(item07);

        RoutineItem item08 = new RoutineItem();
        item08.routineName = "Back Routine";
        item08.orderNumber = 3;
        item08.exercise = "V-Bar Pulldown";
        item08.category = ctx.getString(R.string.exerciseCategory_back);
        item08.numOfSets = 4;
        routines.add(item08);

        RoutineItem item09 = new RoutineItem();
        item09.routineName = "Back Routine";
        item09.orderNumber = 4;
        item09.exercise = "Seated Cable Rows";
        item09.category = ctx.getString(R.string.exerciseCategory_back);
        item09.numOfSets = 4;
        routines.add(item09);

        RoutineItem item10 = new RoutineItem();
        item10.routineName = "Back Routine";
        item10.orderNumber = 5;
        item10.exercise = "Barbell Shrug";
        item10.category = ctx.getString(R.string.exerciseCategory_back);
        item10.numOfSets = 4;
        routines.add(item10);

        RoutineItem item11 = new RoutineItem();
        item11.routineName = "Core Routine";
        item11.orderNumber = 1;
        item11.exercise = "Jumping Jacks";
        item11.category = ctx.getString(R.string.exerciseCategory_cardio);
        item11.numOfSets = 3;
        routines.add(item11);

        RoutineItem item12 = new RoutineItem();
        item12.routineName = "Core Routine";
        item12.orderNumber = 2;
        item12.exercise = "Air Bike";
        item12.category = ctx.getString(R.string.exerciseCategory_core);
        item12.numOfSets = 3;
        routines.add(item12);

        RoutineItem item13 = new RoutineItem();
        item13.routineName = "Core Routine";
        item13.orderNumber = 3;
        item13.exercise = "Stair-Climber";
        item13.category = ctx.getString(R.string.exerciseCategory_cardio);
        item13.numOfSets = 1;
        routines.add(item13);

        RoutineItem item14 = new RoutineItem();
        item14.routineName = "Core Routine";
        item14.orderNumber = 4;
        item14.exercise = "Crunches";
        item14.category = ctx.getString(R.string.exerciseCategory_core);
        item14.numOfSets = 3;
        routines.add(item14);

        RoutineItem item15 = new RoutineItem();
        item15.routineName = "Core Routine";
        item15.orderNumber = 5;
        item15.exercise = "Jump Rope";
        item15.category = ctx.getString(R.string.exerciseCategory_cardio);
        item15.numOfSets = 1;
        routines.add(item15);

        RoutineItem item16 = new RoutineItem();
        item16.routineName = "Chest Routine";
        item16.orderNumber = 1;
        item16.exercise = "Treadmill";
        item16.category = ctx.getString(R.string.exerciseCategory_cardio);
        item16.numOfSets = 1;
        routines.add(item16);

        RoutineItem item17 = new RoutineItem();
        item17.routineName = "Chest Routine";
        item17.orderNumber = 2;
        item17.exercise = "Pushups";
        item17.category = ctx.getString(R.string.exerciseCategory_chest);
        item17.numOfSets = 4;
        routines.add(item17);

        RoutineItem item18 = new RoutineItem();
        item18.routineName = "Chest Routine";
        item18.orderNumber = 3;
        item18.exercise = "Barbell Bench Press";
        item18.category = ctx.getString(R.string.exerciseCategory_chest);
        item18.numOfSets = 4;
        routines.add(item18);

        RoutineItem item19 = new RoutineItem();
        item19.routineName = "Chest Routine";
        item19.orderNumber = 4;
        item19.exercise = "Decline Barbell Bench Press";
        item19.category = ctx.getString(R.string.exerciseCategory_chest);
        item19.numOfSets = 4;
        routines.add(item19);

        RoutineItem item20 = new RoutineItem();
        item20.routineName = "Chest Routine";
        item20.orderNumber = 5;
        item20.exercise = "Incline Bench Dumbbell Fly";
        item20.category = ctx.getString(R.string.exerciseCategory_chest);
        item20.numOfSets = 4;
        routines.add(item20);

        RoutineItem item21 = new RoutineItem();
        item21.routineName = "Legs Routine";
        item21.orderNumber = 1;
        item21.exercise = "Elliptical Trainer";
        item21.category = ctx.getString(R.string.exerciseCategory_cardio);
        item21.numOfSets = 1;
        routines.add(item21);

        RoutineItem item22 = new RoutineItem();
        item22.routineName = "Legs Routine";
        item22.orderNumber = 2;
        item22.exercise = "Barbell Squat";
        item22.category = ctx.getString(R.string.exerciseCategory_legs);
        item22.numOfSets = 4;
        routines.add(item22);

        RoutineItem item23 = new RoutineItem();
        item23.routineName = "Legs Routine";
        item23.orderNumber = 3;
        item23.exercise = "Lying Leg Curls";
        item23.category = ctx.getString(R.string.exerciseCategory_legs);
        item23.numOfSets = 4;
        routines.add(item23);

        RoutineItem item24 = new RoutineItem();
        item24.routineName = "Legs Routine";
        item24.orderNumber = 4;
        item24.exercise = "Leg Press";
        item24.category = ctx.getString(R.string.exerciseCategory_legs);
        item24.numOfSets = 4;
        routines.add(item24);

        RoutineItem item25 = new RoutineItem();
        item25.routineName = "Legs Routine";
        item25.orderNumber = 5;
        item25.exercise = "Standing Calf Raises";
        item25.category = ctx.getString(R.string.exerciseCategory_legs);
        item25.numOfSets = 4;
        routines.add(item25);

        RoutineItem item26 = new RoutineItem();
        item26.routineName = "Shoulder Routine";
        item26.orderNumber = 1;
        item26.exercise = "Jumping Jacks";
        item26.category = ctx.getString(R.string.exerciseCategory_cardio);
        item26.numOfSets = 3;
        routines.add(item26);

        RoutineItem item27 = new RoutineItem();
        item27.routineName = "Shoulder Routine";
        item27.orderNumber = 2;
        item27.exercise = "Barbell Shoulder Press";
        item27.category = ctx.getString(R.string.exerciseCategory_shoulders);
        item27.numOfSets = 4;
        routines.add(item27);

        RoutineItem item28 = new RoutineItem();
        item28.routineName = "Shoulder Routine";
        item28.orderNumber = 3;
        item28.exercise = "Reverse Fly";
        item28.category = ctx.getString(R.string.exerciseCategory_shoulders);
        item28.numOfSets = 4;
        routines.add(item28);

        RoutineItem item29 = new RoutineItem();
        item29.routineName = "Shoulder Routine";
        item29.orderNumber = 4;
        item29.exercise = "Dumbbell Shoulder Press";
        item29.category = ctx.getString(R.string.exerciseCategory_shoulders);
        item29.numOfSets = 4;
        routines.add(item29);

        RoutineItem item30 = new RoutineItem();
        item30.routineName = "Shoulder Routine";
        item30.orderNumber = 5;
        item30.exercise = "One-Arm Side Laterals";
        item30.category = ctx.getString(R.string.exerciseCategory_shoulders);
        item30.numOfSets = 4;
        routines.add(item30);

        return routines;
    }

}
