package com.haitao.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by a55 on 2017/11/22.
 */

public class CharSequenceFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        for (int i = start; i < end; i++) {
            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("_") && !Character.toString(source.charAt(i)).equals("-")) {
                return "";
            }
        }

        return source;
    }
}
