package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 21.10.2017.
 */

public class InterestAddViewResto extends BaseLinearLayout implements InteractiveModelView<Resto>,View.OnClickListener{
    @BindView(R.id.view_interest_avatar)    ImageView avatar;
    @BindView(R.id.view_interest_title)    TextView title;
    @BindView(R.id.view_interest_shot_text)    TextView shot_text;
    @BindView(R.id.view_interest_button_select)    ImageView arrow_right;
    @BindView(R.id.view_interest_text_distance)    TextView text_distance;
    @BindView(R.id.view_interest_text_metro)    TextView text_metro;
    @BindView(R.id.view_interest_container_metro)    View container_metro;
    @BindView(R.id.view_interest_container_distance)    View container_distance;

    private Resto resto;
    private Listener listener;
    TextView text_i_owner;
    TextView text_raiting;


    public InterestAddViewResto(Context context) {
        super(context);
    }

    public InterestAddViewResto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestAddViewResto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InterestAddViewResto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Resto resto) {
        this.resto =resto;

        if(TextUtils.isEmpty(resto.getThumb()))
            Picasso.with(getContext()).load(R.drawable.ic_default_resto).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(resto.getThumb()).fit().centerCrop().into(avatar);

        title.setText(new StringBuilder().append(resto.getName()).toString());

        String tmpStr=new StringBuilder().append(resto.getAdressFormat()).toString();
        if(TextUtils.isEmpty(tmpStr))       tmpStr=new StringBuilder().append(Html.fromHtml(resto.getText())).toString();
        if(!TextUtils.isEmpty(tmpStr))      shot_text.setText(tmpStr);

        if(getContext() instanceof MultiListActivity) {
            setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_MODEL, resto));
            arrow_right.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_VIEW_MODEL, resto));
        }else {
            setOnClickListener(v -> listener.onModelAction(FilterRestoField.NAME, resto));
        }

        try {
            container_distance.setVisibility(VISIBLE);
            text_distance.setText(String.format("%.2f км", Float.valueOf(resto.getDistance().getDistance())/1000));
        }catch (Exception e){
            container_distance.setVisibility(INVISIBLE);
        }

        try {
            container_metro.setVisibility(VISIBLE);
            text_metro.setText(resto.getDistance().getMetro().get(0).getName());
        }catch (Exception e){
            container_metro.setVisibility(INVISIBLE);
        }

        if(text_i_owner!=null)
            text_i_owner.setOnClickListener(this);

    }

    @Override
    public Resto getModel() {
        return resto;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        text_i_owner= (TextView) findViewById(R.id.view_interest_text_i_owner);
        text_raiting= (TextView) findViewById(R.id.view_interest_text_raiting);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_interest_text_i_owner:
                Starter.MultiFragmentActivity_MODE_FORM_I_OWNER((Activity) getContext());
                break;
        }
    }
}
