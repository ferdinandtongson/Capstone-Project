package me.makeachoice.gymratpta.model.contract.client;

/**************************************************************************************************/
/*
 *  StatsColumns are columns used in stats table in the sqlite database
 */
/**************************************************************************************************/

public class StatsColumns {

    // Table name
    public static final String TABLE_NAME = "client_stats";

    //user id
    public static final String COLUMN_UID = "uid";

    //client key
    public static final String COLUMN_CLIENT_KEY = "client_key";

    //appointment date
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    //appointment time
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    //modified date
    public static final String COLUMN_MODIFIED_DATE = "modified_date";

    //stat weight
    public static final String COLUMN_STAT_WEIGHT = "stat_weight";

    //stat body fat
    public static final String COLUMN_STAT_BODY_FAT = "stat_body_fat";

    //stat bmi
    public static final String COLUMN_STAT_BMI = "stat_bmi";

    //stat neck
    public static final String COLUMN_STAT_NECK = "stat_neck";

    //stat chest
    public static final String COLUMN_STAT_CHEST = "stat_chest";

    //stat rbicep
    public static final String COLUMN_STAT_RBICEP = "stat_rbicep";

    //stat lbicep
    public static final String COLUMN_STAT_LBICEP = "stat_lbicep";

    //stat waist
    public static final String COLUMN_STAT_WAIST = "stat_waist";

    //stat naval
    public static final String COLUMN_STAT_NAVAL = "stat_naval";

    //stat hips
    public static final String COLUMN_STAT_HIPS = "stat_hips";

    //stat rthigh
    public static final String COLUMN_STAT_RTHIGH = "stat_rthigh";

    //stat lthigh
    public static final String COLUMN_STAT_LTHIGH = "stat_lthigh";

    //stat rcalf
    public static final String COLUMN_STAT_RCALF = "stat_rcalf";

    //stat lcalf
    public static final String COLUMN_STAT_LCALF = "stat_lcalf";

    //sort by date and time
    public static final String SORT_ORDER_DATE_TIME = COLUMN_APPOINTMENT_DATE + " ASC, " + COLUMN_APPOINTMENT_TIME + " ASC";


    // A "projection" defines the columns that will be returned for each row
    public static final String[] PROJECTION =
            {
                    COLUMN_UID,
                    COLUMN_CLIENT_KEY,
                    COLUMN_APPOINTMENT_DATE,
                    COLUMN_APPOINTMENT_TIME,
                    COLUMN_MODIFIED_DATE,
                    COLUMN_STAT_WEIGHT,
                    COLUMN_STAT_BODY_FAT,
                    COLUMN_STAT_BMI,
                    COLUMN_STAT_NECK,
                    COLUMN_STAT_CHEST,
                    COLUMN_STAT_RBICEP,
                    COLUMN_STAT_LBICEP,
                    COLUMN_STAT_WAIST,
                    COLUMN_STAT_NAVAL,
                    COLUMN_STAT_HIPS,
                    COLUMN_STAT_RTHIGH,
                    COLUMN_STAT_LTHIGH,
                    COLUMN_STAT_RCALF,
                    COLUMN_STAT_LCALF
            };

    public static final int INDEX_UID = 0;
    public static final int INDEX_CLIENT_KEY = 1;
    public static final int INDEX_APPOINTMENT_DATE = 2;
    public static final int INDEX_APPOINTMENT_TIME = 3;
    public static final int INDEX_MODIFIED_DATE = 4;
    public static final int INDEX_STAT_WEIGHT = 5;
    public static final int INDEX_STAT_BODY_FAT = 6;
    public static final int INDEX_STAT_BMI = 7;
    public static final int INDEX_STAT_NECK = 8;
    public static final int INDEX_STAT_CHEST = 9;
    public static final int INDEX_STAT_RBICEP = 10;
    public static final int INDEX_STAT_LBICEP = 11;
    public static final int INDEX_STAT_WAIST = 12;
    public static final int INDEX_STAT_NAVAL = 13;
    public static final int INDEX_STAT_HIPS = 14;
    public static final int INDEX_STAT_RTHIGH = 15;
    public static final int INDEX_STAT_LTHIGH = 16;
    public static final int INDEX_STAT_RCALF = 17;
    public static final int INDEX_STAT_LCALF = 18;

}
