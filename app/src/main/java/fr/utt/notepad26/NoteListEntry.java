package fr.utt.notepad26;

import java.util.Date;

public class NoteListEntry {

    private String name;
    private Date noteDate;

    public NoteListEntry(){
        this.name = "NAME";
        this.noteDate = new Date();
    }

    public NoteListEntry(String name, Date date){
        this.name = name;
        this.noteDate = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoteShortDate() {
        return this.noteDate.getDay() + "/" + this.noteDate.getMonth() + "/" + this.noteDate.getYear();
    }


    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

}
