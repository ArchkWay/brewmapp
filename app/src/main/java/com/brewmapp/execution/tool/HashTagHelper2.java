package com.brewmapp.execution.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;

/**
 * Created by xpusher on 11/24/2017.
 */

public class HashTagHelper2 {
    public static final String SHARP = "#";
    public static final String SPAN_TAG = "tmp";
    Context context;

    public HashTagHelper2(TextView textView, String content){
        try {
            Spanned spannedText= Html.fromHtml(content.toString().replaceAll(SHARP,"<"+SPAN_TAG+">"+SHARP).replace("<br><br>","<br>"),null,htmlTagHandler);
            Spannable reversedText = revertSpanned(spannedText);
            textView.setText(reversedText);
            context=textView.getContext();
        }catch (Exception e){}
    }

    private Html.TagHandler htmlTagHandler= (opening, tag, output, xmlReader) -> {
        if(tag.startsWith(SPAN_TAG)) processSpan(opening, output, new AppearanceSpan());
    };
    private Spannable revertSpanned(Spanned stext) {
        Object[] spans = stext.getSpans(0, stext.length(), Object.class);
        Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
        if (spans != null && spans.length > 0) {
            for(int i = spans.length - 1; i >= 0; --i) {
                ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
            }
        }

        return ret;
    }
    private void processSpan(boolean opening, Editable output, AppearanceSpan span) {
        int len = output.length();
        if (opening) {
            output.setSpan(span, len, len, Spannable.SPAN_MARK_MARK);
        } else {
            AppearanceSpan[] objs = output.getSpans(0, len, span.getClass());
            int where = len;
            if (objs.length > 0) {
                for(int i = objs.length - 1; i >= 0; --i) {
                    if (output.getSpanFlags(objs[i]) == Spannable.SPAN_MARK_MARK) {
                        where = output.getSpanStart(objs[i]);
                        output.removeSpan(objs[i]);
                        break;
                    }
                }
            }

            if (where != len) {
                String param=output.toString().substring(where);
                String checkChar=" ";
                int end=param.indexOf(checkChar,1);
                //************************
                checkChar="#";
                if(end==-1)
                    end=param.indexOf(checkChar,1);
                else if(param.indexOf(checkChar,1)!=-1)
                    end=Math.min(end,param.indexOf(checkChar,1));
                //************************
                checkChar="/n";
                if(end==-1)
                    end=param.indexOf(checkChar,1);
                else if(param.indexOf(checkChar,1)!=-1)
                    end=Math.min(end,param.indexOf(checkChar,1));

                end=end==-1?param.length():end;
                param=param.substring(0,end);
                span.setParam(param.replaceAll("^[^a-яA-Я0-9\\s]+|[^a-яA-Я0-9\\s]+$", ""));
                output.setSpan(span, where, where+end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    class AppearanceSpan extends ClickableSpan {
        String param;

        public AppearanceSpan(){
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context,R.string.message_develop,Toast.LENGTH_SHORT).show();
            if(context instanceof MultiListActivity)
                ((MultiListActivity) context).finish();
            context.startActivity(new Intent(Keys.HASHTAG, Uri.parse(param),context, MultiListActivity.class));
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor(Color.RED);
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }

}
