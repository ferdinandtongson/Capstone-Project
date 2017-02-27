package me.makeachoice.gymratpta.model.item.client;

import android.content.ContentValues;
import android.database.Cursor;

import me.makeachoice.gymratpta.model.contract.client.NotesContract;


public class NotesItem extends NotesFBItem {

/**************************************************************************************************/
/*
 * Class Variables
 */
/**************************************************************************************************/

    public String uid;
    public String clientKey;
    public String timestamp;

/**************************************************************************************************/

/**************************************************************************************************/
/*
 * Constructor
 */
/**************************************************************************************************/

    public NotesItem(){}

    public NotesItem(NotesFBItem item){
        appointmentDate = item.appointmentDate;
        appointmentTime = item.appointmentTime;
        modifiedDate = item.modifiedDate;
        subjectiveNotes = item.subjectiveNotes;
        objectiveNotes = item.objectiveNotes;
        assessmentNotes = item.assessmentNotes;
        planNotes = item.planNotes;
    }

    public NotesItem(Cursor cursor){
        uid = cursor.getString(NotesContract.INDEX_UID);
        clientKey = cursor.getString(NotesContract.INDEX_CLIENT_KEY);
        timestamp = cursor.getString(NotesContract.INDEX_TIMESTAMP);
        appointmentDate = cursor.getString(NotesContract.INDEX_APPOINTMENT_DATE);
        appointmentTime = cursor.getString(NotesContract.INDEX_APPOINTMENT_TIME);
        modifiedDate = cursor.getString(NotesContract.INDEX_MODIFIED_DATE);
        subjectiveNotes = cursor.getString(NotesContract.INDEX_SUBJECTIVE_NOTES);
        objectiveNotes = cursor.getString(NotesContract.INDEX_OBJECTIVE_NOTES);
        assessmentNotes = cursor.getString(NotesContract.INDEX_ASSESSMENT_NOTES);
        planNotes = cursor.getString(NotesContract.INDEX_PLAN_NOTES);
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
        values.put(NotesContract.COLUMN_UID, uid);
        values.put(NotesContract.COLUMN_CLIENT_KEY, clientKey);
        values.put(NotesContract.COLUMN_TIMESTAMP, timestamp);
        values.put(NotesContract.COLUMN_APPOINTMENT_DATE, appointmentDate);
        values.put(NotesContract.COLUMN_APPOINTMENT_TIME, appointmentTime);
        values.put(NotesContract.COLUMN_MODIFIED_DATE, modifiedDate);
        values.put(NotesContract.COLUMN_SUBJECTIVE_NOTES, subjectiveNotes);
        values.put(NotesContract.COLUMN_OBJECTIVE_NOTES, objectiveNotes);
        values.put(NotesContract.COLUMN_ASSESSMENT_NOTES, assessmentNotes);
        values.put(NotesContract.COLUMN_PLAN_NOTES, planNotes);

        return values;
    }

/**************************************************************************************************/

}
