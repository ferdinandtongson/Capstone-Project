package me.makeachoice.gymratpta.model.stubData;
import android.content.Context;

import java.util.ArrayList;

import me.makeachoice.gymratpta.R;
import me.makeachoice.gymratpta.model.item.exercise.ExerciseFBItem;

public class ExerciseStubData {

    public final static int ARMS = 0;
    public final static int BACK = 1;
    public final static int CARDIO = 2;
    public final static int CHEST = 3;
    public final static int CORE = 4;
    public final static int LEGS = 5;
    public final static int SHOULDERS = 6;

    private static ArrayList<ExerciseFBItem> mArms;
    private static ArrayList<ExerciseFBItem> mBack;
    private static ArrayList<ExerciseFBItem> mCardio;
    private static ArrayList<ExerciseFBItem> mChest;
    private static ArrayList<ExerciseFBItem> mCore;
    private static ArrayList<ExerciseFBItem> mLegs;
    private static ArrayList<ExerciseFBItem> mShoulders;
    
    public static void createDefaultExercises(Context ctx){
        initArmExercises(ctx);
        initBackExercises(ctx);
        initCardioExercises(ctx);
        initChestExercises(ctx);
        initCoreExercises(ctx);
        initLegExercises(ctx);
        initShoulderExercises(ctx);
    }

    public static ArrayList<ExerciseFBItem> getExercises(int category){
        switch(category){
            case ARMS:
                return mArms;
            case BACK:
                return mBack;
            case CARDIO:
                return mCardio;
            case CHEST:
                return mChest;
            case CORE:
                return mCore;
            case LEGS:
                return mLegs;
            case SHOULDERS:
                return mShoulders;
            default:
                return null;
        }
    }

    private static void initArmExercises(Context ctx){
        mArms = new ArrayList();
        
        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Close-Grip Barbell Bench Press";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "Cable Rope Overhead Triceps Extension";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "Triceps Pushdown";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "EZ-Bar Curl";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Cable Hammer Curls - Rope Attachment";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Barbell Curl";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "Palms-Down Wrist Curl Over Bench";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_arms);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mArms.add(item07);

    }

    private static void initBackExercises(Context ctx){
        mBack = new ArrayList();

        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Bent Over Barbell Row";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mBack.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "Seated Cable Rows";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mBack.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "V-Bar Pulldown";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mBack.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "Barbell Shrug";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mBack.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Back Extensions";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mBack.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Wide-Grip Lat Pulldown";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mBack.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "One-Arm Dumbbell Row";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_back);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mBack.add(item07);

    }

    private static void initCardioExercises(Context ctx){
        mCardio = new ArrayList();

        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Treadmill";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_distance);
        mCardio.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "Stationary Bike";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_distance);
        mCardio.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "Elliptical Trainer";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_distance);
        mCardio.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "Stair-Climber";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_distance);
        mCardio.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Rowing Machine";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_distance);
        mCardio.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Jump Rope";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mCardio.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "Jumping Jacks";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_cardio);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_time);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mCardio.add(item07);

    }

    private static void initChestExercises(Context ctx){
        mChest = new ArrayList();

        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Close-Grip Barbell Bench Press";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mChest.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "Triceps Pushdown";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mChest.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "Incline Bench Dumbbell Fly";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mChest.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "Pushups";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mChest.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Barbell Bench Press";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mChest.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Dips - Chest Version";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mChest.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "Decline Barbell Bench Press";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_chest);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mChest.add(item07);

    }

    private static void initCoreExercises(Context ctx){
        mCore = new ArrayList();

        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Cable Crunch";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mCore.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "Barbell Side Bend";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mCore.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "Crunches";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mCore.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "Reverse Crunch";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mCore.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Air Bike";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mCore.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Flat Bench Lying Leg Raise";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_none);
        mCore.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "Dumbbell Side Bend";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_core);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mCore.add(item07);

    }

    private static void initLegExercises(Context ctx){
        mLegs = new ArrayList();

        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Barbell Squat";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "Dumbbell Lunges";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "Leg Press";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "Lying Leg Curls";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Leg Extensions";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Standing Calf Raises";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "Barbell Deadlift";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_legs);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mLegs.add(item07);

    }

    private static void initShoulderExercises(Context ctx){
        mShoulders = new ArrayList();

        ExerciseFBItem item01 = new ExerciseFBItem();
        item01.exerciseName = "Barbell Shoulder Press";
        item01.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item01.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item01.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item01);

        ExerciseFBItem item02 = new ExerciseFBItem();
        item02.exerciseName = "One-Arm Side Laterals";
        item02.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item02.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item02.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item02);

        ExerciseFBItem item03 = new ExerciseFBItem();
        item03.exerciseName = "Lying Rear Delt Raise";
        item03.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item03.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item03.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item03);

        ExerciseFBItem item04 = new ExerciseFBItem();
        item04.exerciseName = "Reverse Fly";
        item04.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item04.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item04.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item04);

        ExerciseFBItem item05 = new ExerciseFBItem();
        item05.exerciseName = "Dumbbell Shoulder Press";
        item05.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item05.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item05.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item05);

        ExerciseFBItem item06 = new ExerciseFBItem();
        item06.exerciseName = "Standing Low-Pully Deltoid Raise";
        item06.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item06.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item06.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item06);

        ExerciseFBItem item07 = new ExerciseFBItem();
        item07.exerciseName = "Front Dumbbell Raise";
        item07.exerciseCategory = ctx.getString(R.string.exerciseCategory_shoulders);
        item07.recordPrimary = ctx.getString(R.string.exerciseData_reps);
        item07.recordSecondary = ctx.getString(R.string.exerciseData_weight);
        mShoulders.add(item07);

    }


}
