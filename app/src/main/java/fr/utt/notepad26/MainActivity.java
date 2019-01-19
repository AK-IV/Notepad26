package fr.utt.notepad26;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> noteList;

    final SQLModule sqlModule = new SQLModule(this, SQLModule.DATABASE_NAME, null, 1);

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

                System.out.println("NOTE ID : " + note.getDB_ID());

                String pw_hash = sqlModule.getNotePassword(note.getDB_ID());

                System.out.println("PASSWORD : " + pw_hash);

                if (!pw_hash.equals("")){
                    showPasswordDialog(MainActivity.this, note.getDB_ID());
                } else {
                    Intent myIntent = new Intent(MainActivity.this, NoteEditor.class);
                    myIntent.putExtra("note_id", note.getDB_ID());
                    startActivity(myIntent);
                }

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

        SharedPreferences mPrefs = getSharedPreferences("rgpd_settings", 0);
        String mString = mPrefs.getString("rgpd_show", "yes");

        if (mString.equals("yes")){
            showRGPDDialog(MainActivity.this);
        }

    }

    public void showPasswordDialog(Activity activity, final int noteID){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.password_dialog);

        final EditText userPWInput = dialog.findViewById(R.id.noteListPassword);

        Button btnok = dialog.findViewById(R.id.pwDialogOK);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_pw = userPWInput.getText().toString();

                String pw_hash_DB = sqlModule.getNotePassword(noteID);

                if (pw_hash_DB.equals(hash(input_pw))){
                    Intent myIntent = new Intent(MainActivity.this, NoteEditor.class);
                    myIntent.putExtra("note_id", noteID);
                    startActivity(myIntent);
                }

                /*tv.setText(et.getText().toString());
                tv2.setText(et2.getText().toString());
                tv3.setText(et3.getText().toString());*/
                dialog.dismiss();
            }
        });

        Button btncn = (Button) dialog.findViewById(R.id.pwDialogCancel);
        btncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showRGPDDialog(Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setTitle("RGPD");
        dialog.setContentView(R.layout.rgpd_notice_dialog);

        TextView RGPD_Text = dialog.findViewById(R.id.rgpd_text);

        RGPD_Text.setText("Cette application ne stoque aucune donnée à caractère personnel sans le" +
                " consentement de l'utilisateur." + "\n" +
                "\nCette application ne communique aucune donnée avec le web.");

        Button btnok = dialog.findViewById(R.id.RGPD_ok_btn);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences mPrefs = getSharedPreferences("rgpd_settings", 0);
                SharedPreferences.Editor mEditor = mPrefs.edit();

                CheckBox rgpd_checkbox = dialog.findViewById(R.id.rgpd_checkbox);

                if (rgpd_checkbox.isChecked()){
                    mEditor.putString("rgpd_show", "no").apply();
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public String hash(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String salt = "salty_salt";
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< passHash.length ;i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }
}
