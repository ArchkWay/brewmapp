package com.brewmapp.execution.tool;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;

import com.brewmapp.R;
import com.twitter.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleg on 29.09.17.
 */

public class HashTagHelper {

    private static final Pattern PATTERN = Pattern.compile("#(\\S+)");

    private Context context;
    private int newPostHashTagColor;
    private Extractor extractor = new Extractor();

    public HashTagHelper(Context context) {
        this.context = context;
        this.newPostHashTagColor = context.getResources().getColor(R.color.colorAccent);
    }

    public Spannable formatNewPost(String content) {
        List<String> tags = extractTags(content);
        Spannable spannable = new SpannableString(content);
        for(String tag: tags) {
            int counter = content.split("#" + tag, -1).length - 1;
            int lastIndex = 0;
            for(int i = 0; i < counter; i++) {
                int start = content.indexOf("#" + tag, lastIndex);
                int end = start + tag.length() + 1;
                lastIndex = end;
                spannable.setSpan(new ForegroundColorSpan(newPostHashTagColor), start, end, 0);

                //TODO !!!
                break;
            }
        }
        return spannable;
    }

    public String getSingleHashTag(String content) {
        try {
            return extractTags(content).get(0);
        } catch (Exception ignored) {
            return null;
        }
    }

    public List<String> extractTags(String string) {
        return extractor.extractHashtags(string);
    }
}
