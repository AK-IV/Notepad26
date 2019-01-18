package fr.utt.notepad26;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CustomHTMLTagHandler implements Html.TagHandler {

    final HashMap<String, String> attributes = new HashMap<>();
    private ArrayList<String> styleTypes = new ArrayList<>();
    private String color = "000000";
    private int size = 12;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        /*if (tag.equalsIgnoreCase("strike") || tag.equals("s")) {
            processStrike(opening, output);
        }*/

        if(tag.equalsIgnoreCase("spanned")){

            System.out.println("FOUND SPAN");

            if (opening) {

                System.out.println("OPENING");

                processAttributes(xmlReader);

                System.out.println("PROCESSED ATTRIBUTES");

                if (attributes.containsKey("style")) {

                    System.out.println("FOUND STYLE ATTRIBUTE");

                    String style = attributes.get("style");

                    if (style.contains("font-size:")) {
                        styleTypes.add("size");

                        System.out.println("FOUND FONT-SIZE ATTRIBUTE");
                        size = Integer.parseInt(style.replace("font-size:", "").replace("px", ""));

                        System.out.println("SIZE = " + size);

                        output.setSpan(new AbsoluteSizeSpan(size, true), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                    }
                    else if(style.contains("color:")){
                        styleTypes.add("color");

                        color = style.replace("color:", "").replace(";", "");

                        System.out.println("COLOR = " + color);

                        output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                    }
                }
            } else {
                System.out.println("CLOSING");

                Object obj;

                int lastStyleIndex = styleTypes.size() - 1;
                String lastStyle = styleTypes.get(lastStyleIndex);

                switch (lastStyle){

                    case "size":
                        obj = getLast(output, AbsoluteSizeSpan.class);
                        break;
                    case "color":
                        obj = getLast(output, ForegroundColorSpan.class);
                        break;
                    default:
                        obj = getLast(output, AbsoluteSizeSpan.class);
                }

                int where = output.getSpanStart(obj);

                output.removeSpan(obj);
                styleTypes.remove(lastStyleIndex);

                System.out.println("SETTING SPAN - " + "START : " + where + " END : " + output.length());

                if (lastStyle.equals("size")){
                    output.setSpan(new AbsoluteSizeSpan(size, true), where, output.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else if(lastStyle.equals("color")){
                    output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), where, output.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    private void processAttributes(final XMLReader xmlReader) {
        System.out.println("PROCESSING ATTRIBUTES");
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            /**
             * MSH: Look for supported attributes and add to hash map.
             * This is as tight as things can get :)
             * The data index is "just" where the keys and values are stored.
             */
            for (int i = 0; i < len; i++)
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
        }
    }

    private void processSize(boolean opening, Editable output, int size) {
        int len = output.length();
        if (opening) {
            output.setSpan(new AbsoluteSizeSpan(size), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, AbsoluteSizeSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(new AbsoluteSizeSpan(size, true), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if(objs.length == 0) {
            return null;
        } else {
            for (int i=objs.length; i > 0; i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
            return null;
        }
    }
}