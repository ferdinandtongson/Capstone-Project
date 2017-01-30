package me.makeachoice.gymratpta.model.stubData;

import android.content.Context;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.RoutineSessionItem;

public class RoutineStubData {

    public static ArrayList<RoutineSessionItem> createDefaultRoutineSessions(Context ctx){
        ArrayList<RoutineSessionItem> routines = new ArrayList();

        RoutineSessionItem item01 = new RoutineSessionItem();
        item01.routineName = "Arm Routine";
        item01.exercise01 = "Jump Rope";
        item01.category01 = ctx.getString(R.string.exerciseCategory_cardio);
        item01.set01 = 1;
        item01.exercise02 = "EZ-Bar Curl";
        item01.category02 = ctx.getString(R.string.exerciseCategory_arms);
        item01.set02 = 4;
        item01.exercise03 = "Close-Grip Barbell Bench Press";
        item01.category03 = ctx.getString(R.string.exerciseCategory_arms);
        item01.set03 = 4;
        item01.exercise04 = "Cable Rope Overhead Triceps Extension";
        item01.category04 = ctx.getString(R.string.exerciseCategory_arms);
        item01.set04 = 4;
        item01.exercise05 = "Cable Hammer Curls - Rope Attachment";
        item01.category05 = ctx.getString(R.string.exerciseCategory_arms);
        item01.set05 = 4;
        routines.add(item01);

        RoutineSessionItem item02 = new RoutineSessionItem();
        item02.routineName = "Back Routine";
        item02.exercise01 = "Rowing Machine";
        item02.category01 = ctx.getString(R.string.exerciseCategory_cardio);
        item02.set01 = 1;
        item02.exercise02 = "Wide-Grip Lat Pulldown";
        item02.category02 = ctx.getString(R.string.exerciseCategory_back);
        item02.set02 = 4;
        item02.exercise03 = "V-Bar Pulldown";
        item02.category03 = ctx.getString(R.string.exerciseCategory_back);
        item02.set03 = 4;
        item02.exercise04 = "Seated Cable Rows";
        item02.category04 = ctx.getString(R.string.exerciseCategory_back);
        item02.set04 = 4;
        item02.exercise05 = "Barbell Shrug";
        item02.category05 = ctx.getString(R.string.exerciseCategory_back);
        item02.set05 = 4;
        routines.add(item02);

        RoutineSessionItem item03 = new RoutineSessionItem();
        item03.routineName = "Core Routine";
        item03.exercise01 = "Jumping Jacks";
        item03.category01 = ctx.getString(R.string.exerciseCategory_cardio);
        item03.set01 = 3;
        item03.exercise02 = "Air Bike";
        item03.category02 = ctx.getString(R.string.exerciseCategory_core);
        item03.set02 = 3;
        item03.exercise03 = "Stair-Climber";
        item03.category03 = ctx.getString(R.string.exerciseCategory_cardio);
        item03.set03 = 1;
        item03.exercise04 = "Crunches";
        item03.category04 = ctx.getString(R.string.exerciseCategory_core);
        item03.set04 = 3;
        item03.exercise05 = "Jump Rope";
        item03.category05 = ctx.getString(R.string.exerciseCategory_cardio);
        item03.set05 = 1;
        routines.add(item03);

        RoutineSessionItem item04 = new RoutineSessionItem();
        item04.routineName = "Chest Routine";
        item04.exercise01 = "Treadmill";
        item04.category01 = ctx.getString(R.string.exerciseCategory_cardio);
        item04.set01 = 1;
        item04.exercise02 = "Pushups";
        item04.category02 = ctx.getString(R.string.exerciseCategory_chest);
        item04.set02 = 4;
        item04.exercise03 = "Barbell Bench Press";
        item04.category03 = ctx.getString(R.string.exerciseCategory_chest);
        item04.set03 = 4;
        item04.exercise04 = "Decline Barbell Bench Press";
        item04.category04 = ctx.getString(R.string.exerciseCategory_chest);
        item04.set04 = 4;
        item04.exercise05 = "Incline Bench Dumbbell Fly";
        item04.category05 = ctx.getString(R.string.exerciseCategory_chest);
        item04.set05 = 4;
        routines.add(item04);

        RoutineSessionItem item05 = new RoutineSessionItem();
        item05.routineName = "Legs Routine";
        item05.exercise01 = "Elliptical Trainer";
        item05.category01 = ctx.getString(R.string.exerciseCategory_cardio);
        item05.set01 = 1;
        item05.exercise02 = "Barbell Squat";
        item05.category02 = ctx.getString(R.string.exerciseCategory_legs);
        item05.set02 = 4;
        item05.exercise03 = "Lying Leg Curls";
        item05.category03 = ctx.getString(R.string.exerciseCategory_legs);
        item05.set03 = 4;
        item05.exercise04 = "Leg Press";
        item05.category04 = ctx.getString(R.string.exerciseCategory_legs);
        item05.set04 = 4;
        item05.exercise05 = "Standing Calf Raises";
        item05.category05 = ctx.getString(R.string.exerciseCategory_legs);
        item05.set05 = 4;
        routines.add(item05);

        RoutineSessionItem item06 = new RoutineSessionItem();
        item06.routineName = "Shoulder Routine";
        item06.exercise01 = "Jumping Jacks";
        item06.category01 = ctx.getString(R.string.exerciseCategory_cardio);
        item06.set01 = 3;
        item06.exercise02 = "Barbell Shoulder Press";
        item06.category02 = ctx.getString(R.string.exerciseCategory_shoulders);
        item06.set02 = 4;
        item06.exercise03 = "Reverse Fly";
        item06.category03 = ctx.getString(R.string.exerciseCategory_shoulders);
        item06.set03 = 4;
        item06.exercise04 = "Dumbbell Shoulder Press";
        item06.category04 = ctx.getString(R.string.exerciseCategory_shoulders);
        item06.set04 = 4;
        item06.exercise05 = "One-Arm Side Laterals";
        item06.category05 = ctx.getString(R.string.exerciseCategory_shoulders);
        item06.set05 = 4;
        routines.add(item06);

        return routines;
    }

}
