/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.makeachoice.gymratpta.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.ClientContract;
import me.makeachoice.gymratpta.model.contract.client.ClientExerciseContract;
import me.makeachoice.gymratpta.model.contract.client.ClientRoutineContract;
import me.makeachoice.gymratpta.model.contract.client.NotesContract;
import me.makeachoice.gymratpta.model.contract.client.ScheduleContract;
import me.makeachoice.gymratpta.model.contract.client.StatsContract;
import me.makeachoice.gymratpta.model.contract.exercise.CategoryContract;
import me.makeachoice.gymratpta.model.contract.exercise.ExerciseContract;
import me.makeachoice.gymratpta.model.contract.exercise.RoutineContract;
import me.makeachoice.gymratpta.model.db.table.TableHelper;


/*
 * Manages a local database for weather data.
 */
public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "gymratpta.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Choice", "DBHelper.onCreate");
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_CLIENT_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_SCHEDULE_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_EXERCISE_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_ROUTINE_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_ROUTINE_NAME_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_NOTES_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_STATS_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_CLIENT_ROUTINE_TABLE);
        sqLiteDatabase.execSQL(TableHelper.SQL_CREATE_CLIENT_EXERCISE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contractor.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClientContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExerciseContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RoutineContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contractor.RoutineNameEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotesContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StatsContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClientRoutineContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClientExerciseContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void dropTable(SQLiteDatabase sqLiteDatabase, String table){
        Log.d("Choice", "DBHelper.dropTable: " + table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table);
    }
}
