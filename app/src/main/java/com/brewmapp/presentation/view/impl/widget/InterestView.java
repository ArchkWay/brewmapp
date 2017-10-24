package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Interest;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestView extends BaseLinearLayout implements InteractiveModelView<Interest> {
    @BindView(R.id.view_interest_avatar)    ImageView avatar;
    @BindView(R.id.view_interest_title)    TextView title;

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
        if(model.getInterest_info()!=null) {
            title.setText(String.valueOf(model.getInterest_info().getTitle()));
            Picasso.with(getContext()).load(ResourceHelper.getString(R.string.config_content_url)+model.getInterest_info().getGetThumb()).fit().centerCrop().into(avatar);
        }


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
