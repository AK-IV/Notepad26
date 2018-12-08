package fr.utt.notepad26;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewEmptyNote extends AppCompatActivity {

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

                SpannableStringBuilder stringBuilder = (SpannableStringBuilder) mainText.getText();
                stringBuilder.setSpan(new AbsoluteSizeSpan(Integer.parseInt(selectedItem), true), selectionStart, selectionEnd, 0);

                mainText.setText(stringBuilder);

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

        SpannableStringBuilder stringBuilder = (SpannableStringBuilder) editText.getText();
        stringBuilder.setSpan(new ForegroundColorSpan(color), selectionStart, selectionEnd, 0);

        editText.setText(stringBuilder);

        editText.setSelection(selectionStart, selectionEnd);

        /*
        String completetext=editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        //int differenz = selectionEnd - selectionStart;

        String selectedText = editText.getText().toString().substring(selectionStart, selectionEnd);
        Log.d("selectedText", "selectedText " + selectedText + "|" + selectionStart + "|" + selectionEnd);

        String part1 = editText.getText().toString().substring(0, selectionStart);

        String part2 = editText.getText().toString().substring(selectionStart, selectionEnd);

        String part3 = editText.getText().toString().substring(selectionEnd,completetext.length() );

        editText.setText(Html.fromHtml(part1 + "<font color='"+txtColor+"'>" + part2+ "</font>" + part3));
        */
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

        // The selectionEnd is only greater then the selectionStart position
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
                        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
                        str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), selectionStart, selectionEnd,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
                        str.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }

                    break;
            }

            editText.setSelection(selectionStart, selectionEnd);
        }
    }
}
