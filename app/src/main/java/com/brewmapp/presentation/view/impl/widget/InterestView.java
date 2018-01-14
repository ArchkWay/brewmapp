package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.TextTools;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestView extends BaseLinearLayout implements InteractiveModelView<Interest> {
    @BindView(R.id.view_interest_avatar)    ImageView avatar;
    @BindView(R.id.view_interest_title)    TextView title;
    @BindView(R.id.view_interest_shot_text)    TextView shot_text;
    @BindView(R.id.view_interest_craft)    TextView craft_text;

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
        try {
            craft_text.setVisibility(model.getInterest_info().getCraft().equals("1")?VISIBLE:GONE);
        }catch (Exception e){}
        String tmpStr;
        try {tmpStr=model.getInterest_info().getFormatedTitle();}                        catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) title.setText(tmpStr);else tmpStr=null;
        if(tmpStr==null)
            try {tmpStr=model.getInterest_info().getName();}                            catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) title.setText(tmpStr);else tmpStr=null;

        try {tmpStr=model.getInterest_info().getShort_text();}                          catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) shot_text.setText(tmpStr); else tmpStr=null;
        if(tmpStr==null)
            try {tmpStr=Html.fromHtml(model.getInterest_info().getText()).toString();}  catch (Exception e){tmpStr=null;}   if(!TextUtils.isEmpty(tmpStr)) shot_text.setText(tmpStr); else tmpStr=null;

        try {tmpStr=model.getInterest_info().getGetThumb();}catch (Exception e){tmpStr=null;}

        if(tmpStr==null) {
            switch (model.getRelated_model()) {
                case Keys.CAP_RESTO:
                    Picasso.with(getContext()).load(R.drawable.ic_default_resto).fit().centerCrop().into(avatar);
                    break;
                case Keys.CAP_BEER:
                    Picasso.with(getContext()).load(R.drawable.ic_default_beer).fit().centerCrop().into(avatar);
                    break;
            }
        }else {
            switch (model.getRelated_model()) {
                case Keys.CAP_RESTO:
                    Picasso.with(getContext()).load(tmpStr).fit().centerCrop().into(avatar);
                    break;
                case Keys.CAP_BEER:
                    Picasso.with(getContext()).load(tmpStr).fit().centerInside().into(avatar);
                    break;
            }

        }

        setOnClickListener(v -> listener.onModelAction(
                0,interest
        ));
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
}
