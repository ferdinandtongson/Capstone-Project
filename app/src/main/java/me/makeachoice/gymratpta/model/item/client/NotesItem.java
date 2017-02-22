package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.Contractor;
import me.makeachoice.gymratpta.model.contract.client.NotesColumns;

/**
 * Created by Usuario on 2/20/2017.
 */

public class NotesItem extends NotesFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String clientKey;
    public String appointmentDate;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public NotesItem(){}

    public NotesItem(NotesFBItem item){
        appointmentTime = item.appointmentTime;
        modifiedDate = item.modifiedDate;
        subjectiveNotes = item.subjectiveNotes;
        objectiveNotes = item.objectiveNotes;
        assessmentNotes = item.assessmentNotes;
        planNotes = item.planNotes;
    }

    public NotesItem(Cursor cursor){
        uid = cursor.getString(NotesColumns.INDEX_UID);
        clientKey = cursor.getString(NotesColumns.INDEX_CLIENT_KEY);
        appointmentDate = cursor.getString(NotesColumns.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(NotesColumns.INDEX_APPOINTMENT_TIME);
        modifiedDate = cursor.getString(NotesColumns.INDEX_MODIFIED_DATE);
        subjectiveNotes = cursor.getString(NotesColumns.INDEX_SUBJECTIVE_NOTES);
        objectiveNotes = cursor.getString(NotesColumns.INDEX_OBJECTIVE_NOTES);
        assessmentNotes = cursor.getString(NotesColumns.INDEX_ASSESSMENT_NOTES);
        planNotes = cursor.getString(NotesColumns.INDEX_PLAN_NOTES);
    }

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Public Methods
 *      ContentValues getContentValues() - convert item data to a ContentValues object
 */
/**************************************************************************************************/
    /*
     * ContentValues getContentValues() - convert item data to a ContentValues object
     */
    public ContentValues getContentValues(){
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(Contractor.NotesEntry.COLUMN_UID, uid);
        values.put(Contractor.NotesEntry.COLUMN_CLIENT_KEY, clientKey);
        values.put(Contractor.NotesEntry.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(Contractor.NotesEntry.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(Contractor.NotesEntry.COLUMN_MODIFIED_DATE, modifiedDate);
        values.put(Contractor.NotesEntry.COLUMN_SUBJECTIVE_NOTES, subjectiveNotes);
        values.put(Contractor.NotesEntry.COLUMN_OBJECTIVE_NOTES, objectiveNotes);
        values.put(Contractor.NotesEntry.COLUMN_ASSESSMENT_NOTES, assessmentNotes);
        values.put(Contractor.NotesEntry.COLUMN_PLAN_NOTES, planNotes);

        return values;
    }

/**************************************************************************************************/

}
