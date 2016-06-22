package com.landscape.weight;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 1 on 2016/4/8.
 */
public class EditInputFilter {
    public static final int EN = 1;
    public static final int CN = 2;
    public static final int NUMBER = 4;
    public static final int LINE = 8;
    public static final int EMOJI = 16;

    static String enFormat = "a-zA-Z";
    static String cnFormat = "\\u4E00-\\u9FA5";
    static String numberFormat = "\\d";
    static String lineFormat = "_";
    static String emojiFormat = "[\uD83C\uDC00-\uD83C\uDFFF]|[\uD83D\uDC00-\uD83D\uDFFF]|[\u2600-\u27ff]";

    public static InputFilter getFilter(int filters) {
        String format = "[";
        if ((filters & EN) == EN) {
            format += enFormat;
        }
        if ((filters & CN) == CN) {
            format += cnFormat;
        }
        if ((filters & NUMBER) == NUMBER) {
            format += numberFormat;
        }
        if ((filters & LINE) == LINE) {
            format += lineFormat;
        }
        format +="]*";
        if ((filters & EMOJI) == EMOJI) {
            format = emojiFormat;
        }
        final Pattern enPattern = Pattern.compile(format, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher validMatcher = enPattern.matcher(source);
                if (validMatcher.matches()) {
                    return source;
                } else {
                    return "";
                }
            }
        };
    }

    public static InputFilter getIgnoreFilter(int filters) {
        String format = "[";
        if ((filters & EN) == EN) {
            format += enFormat;
        }
        if ((filters & CN) == CN) {
            format += cnFormat;
        }
        if ((filters & NUMBER) == NUMBER) {
            format += numberFormat;
        }
        if ((filters & LINE) == LINE) {
            format += lineFormat;
        }
        format +="]*";
        if ((filters & EMOJI) == EMOJI) {
            format = emojiFormat;
        }
        final Pattern enPattern = Pattern.compile(format, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher validMatcher = enPattern.matcher(source);
                if (validMatcher.find()) {
                    return "";
                } else {
                    return source;
                }
            }
        };
    }

    public static void addFilter(TextView textView, InputFilter inputFilter) {
        InputFilter[] inputFilters = textView.getFilters();
        InputFilter[] filters = new InputFilter[inputFilters.length + 1];
        System.arraycopy(inputFilters, 0, filters, 0, inputFilters.length);
        filters[inputFilters.length] = inputFilter;
        textView.setFilters(filters);
    }

}
