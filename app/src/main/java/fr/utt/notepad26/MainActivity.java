package fr.utt.notepad26;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        Button newNoteBtn = findViewById(R.id.buttonAddNew);
        newNoteBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, NoteEditor.class);
                myIntent.putExtra("note_id", 0);
                MainActivity.this.startActivity(myIntent);
            }
        });


        final SQLModule sqlModule = new SQLModule(this, SQLModule.DATABASE_NAME, null, 1);

        noteList = sqlModule.getAllNotes();

        /*
        System.out.println("////////////////////////LIST OF SAVED NOTES :////////////////////////");

        for (Note row : noteList){
            System.out.println(row.getDB_ID() + " " + row.getNoteName() + " " + row.getNoteDate());
        }

        System.out.println("/////////////////////////////////////////////////////////////////////");
        */

        final NoteListAdapter noteAdapter = new NoteListAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Séparateur lignes
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //Set Adapter
        recyclerView.setAdapter(noteAdapter);

        //Touch Listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = noteList.get(position);

                Intent myIntent = new Intent(MainActivity.this, NoteEditor.class);
                myIntent.putExtra("note_id", note.getDB_ID());
                startActivity(myIntent);

            }

            @Override
            public void onLongClick(View view, final int position) {

                final Note note = noteList.get(position);

                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                //inflating menu from xml resource
                popup.inflate(R.menu.note_list_popup_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_note_delete:
                                Toast.makeText(getApplicationContext(), "DELETE THIS SHIT", Toast.LENGTH_SHORT).show();

                                sqlModule.delNote(note.getDB_ID());

                                noteList.remove(position);

                                noteAdapter.notifyItemRemoved(position);

                                break;
                            default:
                                //default
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        }));

        noteAdapter.notifyDataSetChanged();

    }
}
