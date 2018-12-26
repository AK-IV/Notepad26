package fr.utt.notepad26;

import android.text.format.DateFormat;

import java.util.Calendar;

public class Note {

    private int db_ID;
    private String noteName;
    private String noteContent;
    private long noteDate;

    public Note(int db_ID, String noteName, String noteContent, long noteDate) {
        this.db_ID = db_ID;
        this.noteName = noteName;
        this.noteContent = noteContent;
        this.noteDate = noteDate;
    }

    public Note() {
        this.db_ID = 1;
        this.noteName = "UNKNOWN";
        this.noteContent = "ASDASDAS";
        this.noteDate = 11111111;
    }

    public int getDB_ID() {
        return db_ID;
    }

    public void setDB_ID(int db_ID) {
        this.db_ID = db_ID;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public String unixTimestampToDateString() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.getNoteDate()*1000L);
        String date = DateFormat.format("dd-MM-yyyy \nHH:mm:ss", cal).toString();
        return date;
    }
}
