package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;

public class ButtonCreate extends AppCompatButton implements ViewUserItem.OnClickListener{

    private String nameNewResto;
    private String nameNewBeer;

    public ButtonCreate(Context context) {
        super(context);
    }

    public ButtonCreate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonCreate(Context context, AttributeSet attrs, int defStyleAttr) {
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

        if(!TextUtils.isEmpty(nameNewResto))
            activity.startActivity(new Intent(MultiFragmentActivityView.MODE_ADD_RESTO, Uri.parse(nameNewResto),activity, MultiFragmentActivity.class));
        else if(!TextUtils.isEmpty(nameNewBeer))
            activity.startActivity(new Intent(MultiFragmentActivityView.MODE_ADD_BEER, Uri.parse(nameNewBeer),activity, MultiFragmentActivity.class));

        activity.finish();
    }

    public void storeNewRestoName(String nameNewResto) {
        this.nameNewResto = nameNewResto.toString();
        nameNewBeer=null;
        if (!TextUtils.isEmpty(nameNewResto)) {
            nameNewResto = "\"" + nameNewResto.substring(0, 1).toUpperCase() + nameNewResto.substring(1) + "\"";
            setText(getContext().getString(R.string.text_button_creat_resto, nameNewResto));
        }else {
            setText(getContext().getString(R.string.text_button_creat_resto, ""));
        }
    }

    public void storeNewBeerName(String nameNewBeer) {
        this.nameNewBeer = nameNewBeer.toString();
        nameNewResto=null;
        if (!TextUtils.isEmpty(nameNewBeer)) {
            nameNewBeer= "\"" + nameNewBeer.substring(0, 1).toUpperCase() + nameNewBeer.substring(1) + "\"";
            setText(getContext().getString(R.string.text_button_create_beer, nameNewBeer));
        }else {
            setText(getContext().getString(R.string.text_button_create_beer, ""));
        }
    }

}
