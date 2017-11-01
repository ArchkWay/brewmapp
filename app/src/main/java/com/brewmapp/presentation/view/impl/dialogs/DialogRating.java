package com.brewmapp.presentation.view.impl.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.brewmapp.R;

/**
 * Created by xpusher on 11/1/2017.
 */

public class DialogRating extends Dialog {
    int rating=0;
    String title="";
    public DialogRating(@NonNull Context context,int rating,String title) {
        super(context);this.rating=rating;this.title=title;init(context);
    }

    public DialogRating(@NonNull Context context, @StyleRes int themeResId,int rating,String title) {
        super(context, themeResId);this.rating=rating;this.title=title;init(context);
    }

    protected DialogRating(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener,int rating,String title) {
        super(context, cancelable, cancelListener);this.rating=rating;this.title=title;init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_rank_dialog);
        setCancelable(true);
        ((RatingBar)findViewById(R.id.dialog_ratingbar)).setRating(rating);
        ((TextView) findViewById(R.id.rank_dialog_text1)).setText(title);
        ((Button) findViewById(R.id.rank_dialog_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        show();

    }

}
