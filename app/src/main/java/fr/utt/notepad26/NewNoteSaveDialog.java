package fr.utt.notepad26;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewNoteSaveDialog extends AppCompatActivity {

    SQLModule sqlModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_save_dialog);

        final EditText name = findViewById(R.id.saveDialogName);
        final EditText password = findViewById(R.id.saveDialogPW);

        View saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                save(name, password);

                Intent intent = new Intent(NewNoteSaveDialog.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void save(EditText name, EditText password){
        Intent myIntent = getIntent(); // gets the previously created intent
        String content = myIntent.getStringExtra("content");
        long date = myIntent.getLongExtra("date", 111111);

        sqlModule = new SQLModule(this, SQLModule.DATABASE_NAME, null, 1);

        long note_id;
        String pw_str = password.getText().toString();
        String pw_hash = hash(pw_str);

        if(!pw_str.replace(" ", "").equals("")) {

            String content_enc;

            try {
                content_enc = AESCrypt.encrypt(pw_str, content);
            }catch (GeneralSecurityException e){
                System.out.print("ERROR ENCRYPTING CONTENT");
                return;
            }

            Note newNote = new Note(0, name.getText().toString(), content_enc, date);
            note_id = sqlModule.addNote(newNote);

            System.out.println("PW HASH :" + pw_hash);
            sqlModule.addNotePassword(note_id, pw_hash);
        }
        else{
            Note newNote = new Note(0, name.getText().toString(), content, date);

            note_id = sqlModule.addNote(newNote);
        }

        System.out.println("ADDED NOTE WITH ID :" + note_id);



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
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }

}
