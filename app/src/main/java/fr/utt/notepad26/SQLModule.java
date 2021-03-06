package fr.utt.notepad26;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLModule extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notesdb";
    public static final int DATABASE_VERSION = 1;

    public static final String NOTES_TABLE = "notes";
    public static final String NOTES_PW_TABLE = "notes_passwords";

    public static final String NOTE_ID = "id";
    public static final String NOTE_NAME = "name";
    public static final String NOTE_CONTENTS = "contents";
    public static final String NOTE_DATE = "date";

    public static final String NOTE_PW_ID = "id";
    public static final String NOTE_PW_NOTE_ID = "note_id";
    public static final String NOTE_PW_HASH = "pw_hash";

    public static final String CREATE_NOTES_TABLE = "CREATE TABLE "
            + NOTES_TABLE + "(" + NOTE_ID + " INTEGER PRIMARY KEY, "
            + NOTE_NAME + " TEXT, " + NOTE_CONTENTS + " TEXT, "
            + NOTE_DATE + " LONG " +
            ")";

    public static final String CREATE_NOTES_PW_TABLE = "CREATE TABLE "
            + NOTES_PW_TABLE + "(" + NOTE_PW_ID + " INTEGER PRIMARY KEY, "
            + NOTE_PW_NOTE_ID + " INTEGER, " + NOTE_PW_HASH + " TEXT " +
            ")";

    public SQLModule(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

/*
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("CREATING DB");
        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_NOTES_PW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLModule.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_PW_TABLE);

        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_NOTES_PW_TABLE);
    }

    public long addNote(Note note){

        System.out.println("ATTEMPTING TO ADD NOTE TO DB USING SQLMODULE");
        System.out.println(note.getNoteName() + " " + note.getNoteContent());

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTE_NAME, note.getNoteName());
        values.put(NOTE_CONTENTS, note.getNoteContent());
        values.put(NOTE_DATE, note.getNoteDate());

        System.out.println("CONTENT: " + note.getNoteContent());

        return database.insert(NOTES_TABLE, null, values);
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NOTE_DATE, note.getNoteDate());
        cv.put(NOTE_NAME, note.getNoteName());
        cv.put(NOTE_CONTENTS, note.getNoteContent());

        db.update(NOTES_TABLE, cv, NOTE_ID + "=" + note.getDB_ID(), null);

        System.out.println("CONTENT: " + note.getNoteContent());
    }


    public Note getNote(int note_id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(NOTES_TABLE,
                new String[] {
                        NOTE_NAME, NOTE_CONTENTS, NOTE_DATE
                },
                NOTE_ID + "=?",
                new String[] {
                        String.valueOf(note_id)
                },
                null, null, null, null
        );


        if (cursor != null)
            cursor.moveToFirst();

        System.out.println("LOADED CONTENT: " + cursor.getString(cursor.getColumnIndex(NOTE_CONTENTS)));

        return new Note(
                note_id,
                cursor.getString(cursor.getColumnIndex(NOTE_NAME)),
                cursor.getString(cursor.getColumnIndex(NOTE_CONTENTS)),
                cursor.getInt(cursor.getColumnIndex(NOTE_DATE))
        );

    }

    public void delNote(int noteID){

        SQLiteDatabase db = getWritableDatabase();

        String deleteQuery = "DELETE " + NOTES_TABLE + " WHERE " + NOTE_ID +"=?";

        db.delete(NOTES_TABLE, NOTE_ID+"=?", new String[]{String.valueOf(noteID)});

        db.delete(NOTES_PW_TABLE, NOTE_PW_NOTE_ID+"=?", new String[]{String.valueOf(noteID)});

    }

    public int countNotes() {
        return 0;
    }


    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + NOTES_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note =  new Note(
                        cursor.getInt(cursor.getColumnIndex(NOTE_ID)),
                        cursor.getString(cursor.getColumnIndex(NOTE_NAME)),
                        cursor.getString(cursor.getColumnIndex(NOTE_CONTENTS)),
                        cursor.getInt(cursor.getColumnIndex(NOTE_DATE))
                );
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // return contact list
        return noteList;
    }

    public String getNotePassword(int note_id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(NOTES_PW_TABLE,
                new String[] {
                        NOTE_PW_ID, NOTE_PW_NOTE_ID, NOTE_PW_HASH
                },
                NOTE_PW_NOTE_ID + "=?",
                new String[] {
                        String.valueOf(note_id)
                },
                null, null, null, null
        );

        cursor.moveToFirst();

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return "";
        }
        else {
            System.out.println("GOT PW HASH : " + cursor.getString(cursor.getColumnIndex(NOTE_PW_HASH)));

            return cursor.getString(cursor.getColumnIndex(NOTE_PW_HASH));
        }

    }

    public long addNotePassword(long note_id, String pwHash){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTE_PW_NOTE_ID, note_id);
        values.put(NOTE_PW_HASH, pwHash);

        return database.insert(NOTES_PW_TABLE, null, values);
    }

}
