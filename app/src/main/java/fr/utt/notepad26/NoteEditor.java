package fr.utt.notepad26;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NoteEditor extends AppCompatActivity {

    private int curNoteID;
    private SQLModule sqlModule;
    private EditText mainText;
    private boolean spinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        mainText = findViewById(R.id.mainText);

        sqlModule = new SQLModule(this, SQLModule.DATABASE_NAME, null, 1);

        Intent myIntent = getIntent(); // gets the previously created intent
        curNoteID = myIntent.getIntExtra("note_id", 0);
        String pw = myIntent.getStringExtra("password");

        initButtons();
        initSpinner();
        loadNoteData(curNoteID, pw);

    }

    private void initButtons(){

        View redColorBtn = findViewById(R.id.colorRed);
        View greenColorBtn = findViewById(R.id.colorGreen);
        View blueColorBtn = findViewById(R.id.colorBlue);
        View blackColorBtn = findViewById(R.id.colorBlack);

        View styleBoldBtn = findViewById(R.id.styleBold);
        View styleItalicBtn = findViewById(R.id.styleItalic);
        View styleUnderlineBtn = findViewById(R.id.styleUnderline);

        View alignLeft = findViewById(R.id.alignLeft);
        View alignCenter = findViewById(R.id.alignCenter);
        View alignRight = findViewById(R.id.alignRight);

        View saveBtn = findViewById(R.id.saveButton);


        redColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "RED", Toast.LENGTH_LONG).show();

                setTextColor("RED");
            }
        });

        greenColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"GREEN", Toast.LENGTH_LONG).show();

                setTextColor("GREEN");
            }
        });

        blueColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"BLUE", Toast.LENGTH_LONG).show();

                setTextColor("BLUE");
            }
        });

        blackColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"BLACK", Toast.LENGTH_LONG).show();

                setTextColor("black");
            }
        });

        /*

            ==============================================================================

         */

        styleBoldBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"BOLD", Toast.LENGTH_LONG).show();

                toggleStyle(mainText, "BOLD");
            }
        });

        styleItalicBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"ITALIC", Toast.LENGTH_LONG).show();

                toggleStyle(mainText, "ITALIC");
            }
        });

        styleUnderlineBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"UNDERLINE", Toast.LENGTH_LONG).show();

                toggleStyle(mainText, "UNDERLINE");
            }
        });

        /*

            ==============================================================================

         */

        alignLeft.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"LEFT ALIGN", Toast.LENGTH_LONG).show();

                alignText("LEFT");
            }
        });

        alignCenter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"CENTER ALIGN", Toast.LENGTH_LONG).show();

                alignText("CENTER");
            }
        });

        alignRight.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"RIGHT ALIGN", Toast.LENGTH_LONG).show();

                alignText("RIGHT");
            }
        });

        //======================================================================================

        saveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getBaseContext(),"SAVING", Toast.LENGTH_LONG).show();

                saveNote(mainText);

                /*
                Intent myIntent = new Intent(NoteEditor.this, MainActivity.class);

                NoteEditor.this.startActivity(myIntent);
                */
            }
        });

    }

    public int[] getCurrentCursorLineOffset()
    {
        int selectionStart = Selection.getSelectionStart(mainText.getText());
        int selectionEnd = Selection.getSelectionEnd(mainText.getText());

        Layout layout = mainText.getLayout();

        int startLine = layout.getLineForOffset(selectionStart);
        int endLine = layout.getLineForOffset(selectionEnd);

        if (selectionStart != -1) {

            return new int[]{layout.getLineStart(startLine), layout.getLineEnd(endLine)};
        }

        return new int[]{-1, -1};
    }

    private void alignText(String alignment){
        int selectionStart = mainText.getSelectionStart();
        int selectionEnd = mainText.getSelectionEnd();
        Layout.Alignment txtAlign;

        //GET THE LINE OFFSETS
        int[] curOffsets = getCurrentCursorLineOffset();

        Spannable str = mainText.getText();
        AlignmentSpan.Standard[] spans;

        spans = str.getSpans(curOffsets[0], curOffsets[1], AlignmentSpan.Standard.class);

        for (AlignmentSpan.Standard alignmentSpan : spans) {
            str.removeSpan(alignmentSpan);
        }

        switch (alignment){
            case "LEFT":
                txtAlign = Layout.Alignment.ALIGN_NORMAL;
                break;

            case "CENTER":
                txtAlign = Layout.Alignment.ALIGN_CENTER;
                break;

            case "RIGHT":
                txtAlign = Layout.Alignment.ALIGN_OPPOSITE;
                break;

            default:
                txtAlign = Layout.Alignment.ALIGN_NORMAL;
                break;
        }

        //SpannableStringBuilder stringBuilder = (SpannableStringBuilder) editText.getText();
        str.setSpan(new AlignmentSpan.Standard(txtAlign), curOffsets[0], curOffsets[1], 0);

    }

    private void initSpinner(){

        Spinner spinner = findViewById(R.id.spinner);

        List<String> list = new ArrayList<>();

        list.add("8");
        list.add("10");
        list.add("12");
        list.add("14");
        list.add("18");
        list.add("20");
        list.add("24");
        list.add("30");
        list.add("36");
        list.add("42");
        list.add("72");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spinnerInitialized){
                    spinnerInitialized = true;
                }
                else {

                    String selectedItem = parent.getItemAtPosition(position).toString();

                    //Toast.makeText(getBaseContext(), "SELECTED: " + selectedItem, Toast.LENGTH_SHORT).show();

                    EditText mainText = findViewById(R.id.mainText);

                    int selectionStart = mainText.getSelectionStart();
                    int selectionEnd = mainText.getSelectionEnd();
                    Spannable str = mainText.getText();
                    AbsoluteSizeSpan[] spans;

                    spans = str.getSpans(selectionStart, selectionEnd, AbsoluteSizeSpan.class);

                    for (AbsoluteSizeSpan sizeSpan : spans) {
                        str.removeSpan(sizeSpan);
                    }

                    //SpannableStringBuilder stringBuilder = (SpannableStringBuilder) mainText.getText();
                    str.setSpan(new AbsoluteSizeSpan(Integer.parseInt(selectedItem), true), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //mainText.setText(stringBuilder);

                    mainText.setSelection(selectionStart, selectionEnd);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void setTextColor(String txtColor) {

        int selectionStart = mainText.getSelectionStart();
        int selectionEnd = mainText.getSelectionEnd();
        int color;
        Spannable str = mainText.getText();
        ForegroundColorSpan[] spans;

        spans = str.getSpans(selectionStart, selectionEnd, ForegroundColorSpan.class);

        for (ForegroundColorSpan colorSpan : spans) {
            str.removeSpan(colorSpan);
        }

        switch (txtColor){
            case "RED":
                color = Color.RED;
                break;

            case "GREEN":
                color = Color.GREEN;
                break;

            case "BLUE":
                color = Color.BLUE;
                break;

            case "BLACK":
                color = Color.BLACK;
                break;

            default:
                color = Color.BLACK;
                break;
        }

        //SpannableStringBuilder stringBuilder = (SpannableStringBuilder) editText.getText();
        str.setSpan(new ForegroundColorSpan(color), selectionStart, selectionEnd, 0);

        //editText.setText(stringBuilder);

        mainText.setSelection(selectionStart, selectionEnd);

    }

    private void toggleStyle(EditText editText, String style) {
        // Gets the current cursor position, or the starting position of the
        // selection
        int selectionStart = editText.getSelectionStart();

        // Gets the current cursor position, or the end position of the
        // selection
        // Note: The end can be smaller than the start
        int selectionEnd = editText.getSelectionEnd();

        // Reverse if the case is what's noted above
        if (selectionStart > selectionEnd) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }

        // The selectionEnd is only greater than the selectionStart position
        // when the user selected a section of the text. Otherwise, the 2
        // variables
        // should be equal (the cursor position).
        if (selectionEnd > selectionStart) {
            Spannable str = editText.getText();
            boolean exists = false;
            StyleSpan[] styleSpans;

            switch (style) {
                case "BOLD":
                    styleSpans = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

                    // If the selected text-part already has BOLD style on it, then
                    // we need to disable it
                    for (StyleSpan styleSpan : styleSpans) {
                        if (styleSpan.getStyle() == Typeface.BOLD) {
                            str.removeSpan(styleSpan);
                            exists = true;
                        }
                    }

                    // Else we set BOLD style on it
                    if (!exists) {
                        str.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd,0);
                    }


                    break;
                case "ITALIC":
                    styleSpans = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

                    // If the selected text-part already has ITALIC style on it,
                    // then we need to disable it
                    for (StyleSpan styleSpan : styleSpans) {
                        if (styleSpan.getStyle() == Typeface.ITALIC) {
                            str.removeSpan(styleSpan);
                            exists = true;
                        }
                    }

                    // Else we set ITALIC style on it
                    if (!exists) {
                        str.setSpan(new StyleSpan(Typeface.ITALIC), selectionStart, selectionEnd, 0);
                    }

                    break;
                case "UNDERLINE":
                    UnderlineSpan[] underSpan = str.getSpans(selectionStart, selectionEnd, UnderlineSpan.class);

                    // If the selected text-part already has UNDERLINE style on it,
                    // then we need to disable it
                    for (UnderlineSpan anUnderSpan : underSpan) {
                        str.removeSpan(anUnderSpan);
                        exists = true;
                    }

                    // Else we set UNDERLINE style on it
                    if (!exists) {
                        str.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, 0);
                    }

                    break;
            }

            editText.setSelection(selectionStart, selectionEnd);
        }
    }

    public void loadNoteData(int noteID, String pw){

        if (noteID == 0) { return; }

        Spanned noteContent;

        Note note = sqlModule.getNote(noteID);

        if (!pw.equals("")){
            String decrypted_content;

            try {
                decrypted_content = AESCrypt.decrypt(pw, note.getNoteContent());
            } catch (Exception e) {
                System.out.println("ERROR DECRYPTING CONTENT!");
                e.printStackTrace();
                return;
            }

            noteContent = Html.fromHtml(decrypted_content, null, new CustomHTMLTagHandler());
        }
        else{
            noteContent = Html.fromHtml(note.getNoteContent(), null, new CustomHTMLTagHandler());
            //Spanned noteContent = Html.fromHtml(note.getNoteContent());
        }

        mainText.setText(noteContent);
    }

    public void saveNote(EditText mainText){

        //String textNoteName = "NOTE 1";
        Spannable spanText = mainText.getText();
        String htmlText = Html.toHtml(spanText);

        htmlText = htmlText.replaceAll("align=\"","style=\"text-align: ");
        htmlText = htmlText.replaceAll("span","spanned");

        Calendar cal = Calendar.getInstance();
        TimeZone timeZone =  cal.getTimeZone();
        Date cals = Calendar.getInstance(TimeZone.getDefault()).getTime();
        long milliseconds = cals.getTime();
        milliseconds = milliseconds + timeZone.getOffset(milliseconds);

        long unixTimeStamp = milliseconds / 1000L;

        if (curNoteID == 0) {

            Intent myIntent = new Intent(this, NewNoteSaveDialog.class);
            myIntent.putExtra("content", htmlText);
            myIntent.putExtra("date", unixTimeStamp);
            startActivity(myIntent);
        }
        else{
            Note note = sqlModule.getNote(curNoteID);

            Intent myIntent = getIntent();
            String pw = myIntent.getStringExtra("password");

            if (!pw.equals("")) {

                String content_enc;

                try {
                    content_enc = AESCrypt.encrypt(pw, htmlText);
                }catch (GeneralSecurityException e){
                    System.out.print("ERROR ENCRYPTING CONTENT");
                    return;
                }

                note.setNoteContent(content_enc);

            }else{
                note.setNoteContent(htmlText);
            }

            note.setNoteDate(unixTimeStamp);

            sqlModule.updateNote(note);

            showSaveNotification(NoteEditor.this);
        }

    }

    public void showSaveNotification(Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.setTitle("Confirmation");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.save_confirmation);


        Button btnok = dialog.findViewById(R.id.saveOKBtn);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
