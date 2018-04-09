package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;

public class CreateBeerButton extends AppCompatButton implements ViewUserItem.OnClickListener{

    public CreateBeerButton(Context context) {
        super(context);
    }

    public CreateBeerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CreateBeerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Activity activity;
        if(getContext() instanceof Activity)
            activity=((Activity)getContext());
        else
            return;
        activity.finish();
        activity.startActivity(new Intent(MultiFragmentActivityView.MODE_ADD_BEER, Uri.EMPTY,activity, MultiFragmentActivity.class));
    }
}
