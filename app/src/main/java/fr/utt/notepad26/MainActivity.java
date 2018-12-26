package fr.utt.notepad26;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        Button newNoteBtn = findViewById(R.id.buttonAddNew);
        newNoteBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, NoteEditor.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        SQLModule sqlModule = new SQLModule(this, SQLModule.DATABASE_NAME, null, 1);

        ArrayList<Note> noteList = sqlModule.getAllNotes();

        /*
        System.out.println("////////////////////////LIST OF SAVED NOTES :////////////////////////");

        for (Note row : noteList){
            System.out.println(row.getDB_ID() + " " + row.getNoteName() + " " + row.getNoteDate());
        }

        System.out.println("/////////////////////////////////////////////////////////////////////");
        */

        NoteListAdapter noteAdapter = new NoteListAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Séparateur lignes
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(noteAdapter);

        noteAdapter.notifyDataSetChanged();

        //prepareTestData();
    }
}
