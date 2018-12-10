package fr.utt.notepad26;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class noteEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_empty_note);

        final EditText mainText = findViewById(R.id.mainText);

        initButtons(mainText);

        initSpinner();

    }

    private void initButtons(final EditText mainText){

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


        redColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "RED", Toast.LENGTH_LONG).show();

                setTextColor(mainText, "RED");
            }
        });

        greenColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"GREEN", Toast.LENGTH_LONG).show();

                setTextColor(mainText, "GREEN");
            }
        });

        blueColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"BLUE", Toast.LENGTH_LONG).show();

                setTextColor(mainText, "BLUE");
            }
        });

        blackColorBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"BLACK", Toast.LENGTH_LONG).show();

                setTextColor(mainText, "black");
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

                alignText(mainText, "LEFT");
            }
        });

        alignCenter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"CENTER ALIGN", Toast.LENGTH_LONG).show();

                alignText(mainText, "CENTER");
            }
        });

        alignRight.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"RIGHT ALIGN", Toast.LENGTH_LONG).show();

                alignText(mainText, "RIGHT");
            }
        });

    }

    public int[] getCurrentCursorLineOffset(EditText editText)
    {
        int selectionStart = Selection.getSelectionStart(editText.getText());
        int selectionEnd = Selection.getSelectionEnd(editText.getText());

        Layout layout = editText.getLayout();

        int startLine = layout.getLineForOffset(selectionStart);
        int endLine = layout.getLineForOffset(selectionEnd);

        if (selectionStart != -1) {

            return new int[]{layout.getLineStart(startLine), layout.getLineEnd(endLine)};
        }

        return new int[]{-1, -1};
    }

    private void alignText(EditText editText, String alignment){
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        Layout.Alignment txtAlign;

        //GET THE LINE OFFSETS
        int[] curOffsets = getCurrentCursorLineOffset(editText);

        Spannable str = editText.getText();
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

        //editText.setText(stringBuilder);

        //editText.setSelection(selectionStart, selectionEnd);
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
                str.setSpan(new AbsoluteSizeSpan(Integer.parseInt(selectedItem), true), selectionStart, selectionEnd, 0);

                //mainText.setText(stringBuilder);

                mainText.setSelection(selectionStart, selectionEnd);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void setTextColor(EditText editText, String txtColor) {

        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        int color;
        Spannable str = editText.getText();
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

        editText.setSelection(selectionStart, selectionEnd);

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
}
