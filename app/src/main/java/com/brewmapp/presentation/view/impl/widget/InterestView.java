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
        String imgUrl="";
        String text="";
        String text2="";
        int imgUrlDefault;

        text=model.getInterest_info().getTitle();
        if(TextUtils.isEmpty(text))
            text=model.getInterest_info().getName();


        text2=model.getInterest_info().getShort_text();
        if(TextUtils.isEmpty(text2))
            if(!TextUtils.isEmpty(model.getInterest_info().getText()))
                text2= Html.fromHtml(model.getInterest_info().getText()).toString();
        if(TextUtils.isEmpty(text2))
            text2=text;

        if(model.getInterest_info()!=null) {
            imgUrl=model.getInterest_info().getGetThumb();
            if(imgUrl!=null&&!imgUrl.contains("http"))
                imgUrl=ResourceHelper.getString(R.string.config_content_url)+imgUrl;
        }

        switch (model.getRelated_model()){
            case Keys.CAP_RESTO:
                imgUrlDefault=R.drawable.ic_default_resto;
                break;
            case Keys.CAP_BEER:
                imgUrlDefault=R.drawable.ic_default_beer;
                break;
            default:
                return;
        }

        title.setText(String.valueOf(text));
        shot_text.setText(String.valueOf(text2));
        if(TextUtils.isEmpty(imgUrl)||imgUrl.length()==0)
            Picasso.with(getContext()).load(imgUrlDefault).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(imgUrl).fit().centerInside().into(avatar);

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
        setOnClickListener(v -> listener.onModelAction(0,interest));

    }
}
