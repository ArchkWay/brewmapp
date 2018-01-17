package com.brewmapp.presentation.view.impl.widget;

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
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Location;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestView extends BaseLinearLayout implements InteractiveModelView<Interest> {
    @BindView(R.id.view_interest_avatar)    ImageView avatar;
    @BindView(R.id.view_interest_title)    TextView title;
    @BindView(R.id.view_interest_shot_text)    TextView shot_text;
    @BindView(R.id.view_interest_craft)    TextView craft_text;
    @BindView(R.id.view_interest_container_distance)    View container_distance;
    @BindView(R.id.view_interest_container_metro)    View container_metro;
    @BindView(R.id.view_interest_text_distance)    TextView text_distance;
    @BindView(R.id.view_interest_text_metro)    TextView text_metro;

    private Interest interest;
    private Listener listener;


    public InterestView(Context context) {
        super(context);
    }

    public InterestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InterestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Interest model) {
        this.interest=model;

        container_distance.setVisibility(GONE);
        container_metro.setVisibility(GONE);

        switch (interest.getRelated_model()){
            case Keys.CAP_RESTO:
                handleModelResto();
                break;
            case Keys.CAP_BEER:
                handleModelBeer();
                break;
        }

        handleModelCommon();

    }

    @Override
    public Interest getModel() {
        return interest;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);

    }

    //***************************
    private void handleModelResto() {
        Location location=interest.getInterest_info().getLocation();
        try {
            text_metro.setText(location.getMetro().getName());
            container_metro.setVisibility(VISIBLE);
        }catch (Exception e){}
        try {
            text_distance.setText(String.format("%s м",location.getMetro().getDistance()));
            container_distance.setVisibility(VISIBLE);
        }catch (Exception e){}

        String tmpStr=null;
        try {tmpStr=interest.getInterest_info().getGetThumb();}catch (Exception e){tmpStr=null;}
        if(tmpStr==null)
            Picasso.with(getContext()).load(R.drawable.ic_default_resto).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(tmpStr).fit().centerCrop().into(avatar);

        setOnClickListener(v -> listener.onModelAction(Actions.ACTION_CLICK_ON_ITEM_INTEREST_RESTO,interest));
    }
    private void handleModelBeer() {
        try {craft_text.setVisibility(interest.getInterest_info().getCraft().equals("1")?VISIBLE:GONE);}catch (Exception e){}
        String tmpStr=null;
        try {tmpStr=interest.getInterest_info().getGetThumb();}catch (Exception e){tmpStr=null;}
        if(tmpStr==null)
            Picasso.with(getContext()).load(R.drawable.ic_default_beer).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(tmpStr).fit().centerInside().into(avatar);
        setOnClickListener(v -> listener.onModelAction(Actions.ACTION_CLICK_ON_ITEM_INTEREST_BEER,interest));
    }
    private void handleModelCommon() {
        String tmpStr;
        try {tmpStr=interest.getInterest_info().getFormatedTitle();}                        catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) title.setText(tmpStr);else tmpStr=null;
        if(tmpStr==null)
            try {tmpStr=interest.getInterest_info().getName();}                            catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) title.setText(tmpStr);else tmpStr=null;

        try {tmpStr=interest.getInterest_info().getShort_text();}                          catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) shot_text.setText(tmpStr); else tmpStr=null;
        if(tmpStr==null)
            try {tmpStr=Html.fromHtml(interest.getInterest_info().getText()).toString();}  catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) shot_text.setText(tmpStr); else tmpStr=null;


    }

}
