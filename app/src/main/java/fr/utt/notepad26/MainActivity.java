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
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NoteListEntry> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteListAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        Button newNoteBtn = findViewById(R.id.buttonAddNew);
        newNoteBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, noteEditor.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        noteAdapter = new NoteListAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Séparateur lignes
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(noteAdapter);

        prepareTestData();
    }

    private void prepareTestData() {
        NoteListEntry note = new NoteListEntry("Note1", new Date());
        noteList.add(note);

        note = new NoteListEntry("Note2", new Date());
        noteList.add(note);

        note = new NoteListEntry("Note3", new Date());
        noteList.add(note);

        note = new NoteListEntry("Note4", new Date());
        noteList.add(note);

        note = new NoteListEntry("Note5", new Date());
        noteList.add(note);

        noteAdapter.notifyDataSetChanged();
    }
}
