package fr.utt.notepad26;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewNoteSaveDialog extends AppCompatActivity {

    SQLModule sqlModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_save_dialog);

        final EditText name = findViewById(R.id.saveDialogName);
        View saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                save(name);

                Intent intent = new Intent(NewNoteSaveDialog.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void save(EditText name){
        Intent myIntent = getIntent(); // gets the previously created intent
        String content = myIntent.getStringExtra("content");
        long date = myIntent.getLongExtra("date", 111111);

        sqlModule = new SQLModule(this, SQLModule.DATABASE_NAME, null, 1);

        Note newNote = new Note(0, name.getText().toString(), content, date);

        sqlModule.addNote(newNote);
    }
}
